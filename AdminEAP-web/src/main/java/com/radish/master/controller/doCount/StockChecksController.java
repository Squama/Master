package com.radish.master.controller.doCount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;








import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Materiel;
import com.radish.master.entity.Project;
import com.radish.master.entity.doCount.StockChecks;
import com.radish.master.entity.doCount.StockChecksMat;
import com.radish.master.entity.doCount.StockChecksMatDet;
import com.radish.master.entity.volumePay.InstockChannel;
import com.radish.master.pojo.MatInAndOut;
import com.radish.master.pojo.Options;
import com.radish.master.service.BudgetService;
import com.radish.master.system.Arith;

@Controller
@RequestMapping("/stockchecks")
public class StockChecksController {
	private String prefix="/doCount/stockChecks/";
	
	private Arith ari;
	@Resource
    private BudgetService budgetService;
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("/addIndex")
	public String addIndex(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		return prefix +"addIndex";
	}
	
	//根据选择的项目、物料、时间段进行统计对于期初、入库、出库、结存，返回前台以列表显示
	@RequestMapping("/dochecks")
	@ResponseBody
	public Map<String,Object> doInAndOutForMat(HttpServletRequest request,StockChecks tj){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String,Object> m = new HashMap<String,Object>();
		
		Project xm = baseService.get(Project.class, tj.getProjectId());
		tj.setProName(xm.getProjectName());
		baseService.save(tj);
		
		//项目下所有库存物料的分类,matNumber
		String wlsql = "select matNumber  from v_instockInfo "
				+ "where projectId='"+tj.getProjectId()+"' "
				+ "GROUP BY matNumber ORDER BY matNumber";
		List<MatInAndOut> sts = baseService.findMapBySql(wlsql, new Object[]{}, new Type[]{}, MatInAndOut.class);
		
		//循环拿到的物料种类,进行计算
		for(MatInAndOut st : sts){
			StockChecksMat tjwl = new StockChecksMat();
			tjwl.setPid(tj.getId());
			tjwl.setMatNumber(st.getMatNumber());
			List<Materiel> wl = baseService.find(" from Materiel where mat_number='"+st.getMatNumber()+"'");
			tjwl.setMatName(wl.get(0).getMat_name());
			tjwl.setStandard(wl.get(0).getMat_standard());
			tjwl.setUnit(wl.get(0).getUnit());
			baseService.save(tjwl);
			//计算，返回明细数量
			String sl = doChecks(tj,tjwl);
			tjwl.setSl(sl);
			baseService.update(tjwl);
		}
		
		m.put("success", "true");
		m.put("id", tj.getId());
		return m;
	}
	
	//计算库存  期初  入库 出库  结存  按照金额
	public String doChecks(StockChecks tj,StockChecksMat tjwl){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//计算期初
		List<StockChecksMatDet> mx =  getQc(tj,tjwl);
		
		//根据物料编号、价格、类型 去分组 拿到数量  ..统计时间断内
		String sql = "select matNumber , CAST(sum(rkl) AS CHAR(60))   rkl ,price,type   from v_instockInfo where projectId='"+tj.getProjectId()+"' ";
		sql+=" and matNumber ='"+tjwl.getMatNumber()+"' ";
		sql += " and  rq > '"+sdf.format(tj.getStartdate())+"' and rq <= '"+sdf.format(tj.getEnddate())+"'"
				+ "  GROUP BY matNumber , price,type ";//开始日期之前都计入期初
		
		List<MatInAndOut> sts = baseService.findMapBySql(sql, new Object[]{}, new Type[]{}, MatInAndOut.class);
		
		for(int i=0;i<sts.size();i++){
			String type = sts.get(i).getType();
			//赋值
			StockChecksMatDet jc = new StockChecksMatDet();
			jc.setQcsl(sts.get(i).getRkl());
			if(sts.get(i).getPrice()!=null){
				jc.setQcje(sts.get(i).getPrice());
			}else{
				jc.setQcje("0");
			}
			boolean a = true;
			for(int j=0;j<mx.size();j++){//遍历查看结存集合是否有相同单价的材料，有-数量处理  无-加入集合
				if(Double.valueOf(jc.getQcje()).equals(Double.valueOf(mx.get(j).getQcje()))){
					//处理数量
					addMatSlCrk(jc,mx.get(j),type);
					a = false;
					break;
				}
			}
			if(a){//如果没有，就加入集合
				if("1".equals(type)||"5".equals(type)||"4".equals(type)){//入库
					jc.setRksl(sts.get(i).getRkl());
					jc.setCksl("0");
					jc.setJcsl(sts.get(i).getRkl());
					jc.setQcsl("0");
					
				}else{//出库
					jc.setCksl(sts.get(i).getRkl());
					jc.setRksl("0");
					jc.setJcsl("-"+sts.get(i).getRkl());
					jc.setQcsl("0");
				}
				mx.add(jc);
			}
			
		}
		for(StockChecksMatDet m :mx){
			Double sl = 0.0;
			//去除期初 入库 出库 结存数量都是0的记录
			if(sl.equals(Double.valueOf(m.getJcsl()))&&sl.equals(Double.valueOf(m.getQcsl()))
					&&sl.equals(Double.valueOf(m.getRksl()))&&sl.equals(Double.valueOf(m.getCksl()))){
				continue;
			}
			m.setPid(tjwl.getId());
			baseService.save(m);
		}
		return mx.size()+"";
	}
	
	//获取期初的方法---查询初始化入库、再计算某日期之前的出入库调度数据，计算完成后返回即可，去掉数量为0的数据
	public List<StockChecksMatDet> getQc(StockChecks tj,StockChecksMat tjwl){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//根据物料编号、价格、类型 去分组 拿到数量
		String sql = "select matNumber , rkl ,price,type   from v_instockInfo where projectId='"+tj.getProjectId()+"' ";
		sql+=" and matNumber ='"+tjwl.getMatNumber()+"' ";
		sql += " and  rq <= '"+sdf.format(tj.getStartdate())+"' "
				+ "  GROUP BY matNumber , rkl ,price,type ";//开始日期之前都计入期初
		
		List<MatInAndOut> sts = baseService.findMapBySql(sql, new Object[]{}, new Type[]{}, MatInAndOut.class);
		
		
		List<StockChecksMatDet> ls = new ArrayList<StockChecksMatDet>();
		for(int i=0;i<sts.size();i++){
			String type = sts.get(i).getType();
			//赋值
			StockChecksMatDet jc = new StockChecksMatDet();
		
			jc.setQcsl(sts.get(i).getRkl());
			jc.setJcsl("0");
			jc.setRksl("0");
			jc.setCksl("0");
			if(sts.get(i).getPrice()!=null){
				jc.setQcje(sts.get(i).getPrice());
				jc.setJcje(sts.get(i).getPrice());
			}else{
				jc.setQcje("0");
				jc.setJcje("0");
			}
			
			if(ls.size()==0){//第一次
				jc.setJcsl(jc.getQcsl());
				ls.add(jc);
			}else{
				boolean a = true;
				for(int j=0;j<ls.size();j++){//遍历查看结存集合是否有相同单价的材料，有-数量处理  无-加入集合
					if(Double.valueOf(jc.getQcje()).equals(Double.valueOf(ls.get(j).getQcje()))){
						//处理数量
						String sl = cutMatSl(jc.getQcsl(),ls.get(j).getQcsl(),type);
						ls.get(j).setQcsl(sl);
						ls.get(j).setJcsl(sl);
						a = false;
						break;
					}
				}
				if(a){//加入集合
					jc.setJcsl(jc.getQcsl());
					ls.add(jc);
				}
			}
			
		}
		/*Double sl = 0.0;
		Iterator<StockChecksMatDet> it = ls.iterator();
		while(it.hasNext()){
			StockChecksMatDet x = it.next();
			
		    if(sl.equals(Double.valueOf(x.getJcsl()))){
		        it.remove();
		    }
		}*/
		return ls;
	}
	
	//处理期初物料数量
	public String cutMatSl(String dqsl,String yysl,String type){
		Double a = 0.0;
		if("1".equals(type)||"5".equals(type)||"4".equals(type)){//入库，相加
			a = ari.add(Double.valueOf(dqsl), Double.valueOf(yysl));
		}else{//出库、相减
			a = ari.sub(Double.valueOf(yysl),Double.valueOf(dqsl));
		}
		return a.toString();
		
	}
	//处理出入库物料数量        当前                     已有     类型
	public void addMatSlCrk(StockChecksMatDet xz,StockChecksMatDet yy,String type){
		
		if("1".equals(type)||"5".equals(type)||"4".equals(type)){//入库，入库量相加  结存相加 +期初
			yy.setRksl((ari.add(Double.valueOf(xz.getQcsl()),Double.valueOf(yy.getRksl())))+"");
			Double jc = ari.add(Double.valueOf(xz.getQcsl()),Double.valueOf(yy.getJcsl()));
			yy.setJcsl(jc+"");
		}else{//出库、出库量相加、结存 - 出库 +期初
			yy.setCksl((ari.add(Double.valueOf(xz.getQcsl()),Double.valueOf(yy.getCksl())))+"");
			Double jc =ari.sub(Double.valueOf(yy.getJcsl()),Double.valueOf(xz.getQcsl()));
			yy.setJcsl(jc+"");
		}
	}
	
	
	//获取统计结果
	@RequestMapping("/getStockChecks")
	@ResponseBody
	public Map<String,Object> getInAndOutInfo(HttpServletRequest request){
		Map<String,Object> m = new HashMap<String, Object>();
		String tjid = request.getParameter("id");
		
		StockChecks tj = baseService.get(StockChecks.class, tjid);
		List<StockChecksMat> mxs =   baseService.find(" from StockChecksMat where pid='"+tjid+"'");
		List<Map<String,Object>> mxjcs = new ArrayList<Map<String,Object>>();
		for(StockChecksMat mx: mxs){
			Map<String,Object> mxjc = new HashMap<String, Object>();
			List<StockChecksMatDet> jcs =   baseService.find(" from StockChecksMatDet where pid='"+mx.getId()+"'");
			mxjc.put("mx", mx);
			mxjc.put("jcs", jcs);
			mxjcs.add(mxjc);
		}
		m.put("tj", tj);
		m.put("mxjcs", mxjcs);
		
		return m;
	}
}
