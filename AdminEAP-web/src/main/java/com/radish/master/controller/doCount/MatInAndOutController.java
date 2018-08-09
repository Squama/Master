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
import com.radish.master.entity.doCount.InAndOut;
import com.radish.master.entity.doCount.InAndOutDet;
import com.radish.master.entity.doCount.InAndOutDetJc;
import com.radish.master.entity.volumePay.InstockChannel;
import com.radish.master.pojo.MatInAndOut;
import com.radish.master.pojo.Options;
import com.radish.master.service.BudgetService;
import com.radish.master.system.Arith;

@Controller
@RequestMapping("/matinandout")
public class MatInAndOutController {
	private String prefix="/doCount/matInAndOut/";
	
	private Arith ari;
	@Resource
    private BudgetService budgetService;
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("/addIndex")
	public String addIndex(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		List<Materiel> mats = baseService.find(" from Materiel where 1=1 ");
		request.setAttribute("matOptions", JSONArray.toJSONString(mats));
		return prefix +"addIndex";
	}
	
	//根据选择的项目、物料、时间段进行统计对于期初、入库、出库、结存，返回前台以列表显示
	@RequestMapping("/doInAndOutForMat")
	@ResponseBody
	public Map<String,Object> doInAndOutForMat(HttpServletRequest request,InAndOut tj){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String,Object> m = new HashMap<String,Object>();
		//拿到物料
		List<Materiel> wl = baseService.find(" from Materiel where mat_number='"+tj.getMatId()+"'");
		
		tj.setStatus("10");
		tj.setMatName(wl.get(0).getMat_name());
		tj.setStandard(wl.get(0).getMat_standard());
		Project xm = baseService.get(Project.class, tj.getProjectId());
		tj.setProName(xm.getProjectName());
		baseService.save(tj);
		
		//计算期初,拿到开始日期之前的结存
		List<InAndOutDetJc> jcs = getQc(tj);
		for(InAndOutDetJc jc :jcs){
			InAndOutDet qc = new InAndOutDet();
			qc.setQcje(jc.getJcje());
			qc.setQcsl(jc.getJcsl());
			qc.setPid(tj.getId());
			qc.setBz("期初数");
			qc.setFz(sdf.format(tj.getStartdate())+"前仓库结存");
			qc.setRq(tj.getStartdate());
			qc.setNumber("");
			qc.setStandard(wl.get(0).getMat_standard());
			qc.setUnit(wl.get(0).getUnit());
			qc.setJcsl("1");
			baseService.save(qc);
			jc.setPid(qc.getId());
			baseService.save(jc);
		}
		
		//拿到出入库调度数据，按照日期排序，然后进行计算（1个统计----多条出入库记录----多条结存）
		String sql = "select * from v_instockInfo where projectId='"+tj.getProjectId()+"' ";
		sql+=" and matNumber ='"+tj.getMatId()+"' ";
		sql += " and  rq > '"+sdf.format(tj.getStartdate())+"' and rq <= '"+sdf.format(tj.getEnddate())+"'";
		
		List<MatInAndOut> sts = baseService.findMapBySql(sql, new Object[]{}, new Type[]{}, MatInAndOut.class);
		//存入明细表 ，并计算结存。存入结存表
		for(int i=0;i<sts.size();i++){
			String type = sts.get(i).getType();
			
			InAndOutDet qc = new InAndOutDet();
			//判断是出库还是入库
			if("1".equals(type)||"5".equals(type)||"4".equals(type)){//入库
				qc.setRksl(sts.get(i).getRkl());
				if(sts.get(i).getPrice()!=null){
					qc.setRkje(sts.get(i).getPrice());
				}else{
					qc.setRkje("0");
				}
				qc.setBz(sts.get(i).getBz());
			}else{//出库
				qc.setCksl(sts.get(i).getRkl());
				if(sts.get(i).getPrice()!=null){
					qc.setCkje(sts.get(i).getPrice());
				}else{
					qc.setCkje("0");
				}
				if(sts.get(i).getBz()==null){
					qc.setBz("出库");
				}else{
					qc.setBz(sts.get(i).getBz());
				}
			}
			qc.setPid(tj.getId());
			qc.setFz("");
			qc.setRq(sts.get(i).getRq());
			qc.setNumber(sts.get(i).getNumber());
			qc.setStandard(sts.get(i).getMatStandard());
			qc.setUnit(sts.get(i).getUnit());
			jcs = doJc(jcs,sts.get(i));
			qc.setJcsl(jcs.size()+"");
			baseService.save(qc);
			for(InAndOutDetJc jc :jcs){
				jc.setPid(qc.getId());
				baseService.save(jc);
			}
		}
		m.put("success", "true");
		m.put("id", tj.getId());
		return m;
	}
	
	//获取期初的方法---查询初始化入库、再计算某日期之前的出入库调度数据，计算完成后返回即可，去掉数量为0的数据
	public List<InAndOutDetJc> getQc(InAndOut tj){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String sql = "select * from v_instockInfo where projectId='"+tj.getProjectId()+"' ";
		sql+=" and matNumber ='"+tj.getMatId()+"' ";
		sql += " and  rq <= '"+sdf.format(tj.getStartdate())+"' ";//开始日期之前都计入期初
		
		List<MatInAndOut> sts = baseService.findMapBySql(sql, new Object[]{}, new Type[]{}, MatInAndOut.class);
		
		
		List<InAndOutDetJc> ls = new ArrayList<InAndOutDetJc>();
		for(int i=0;i<sts.size();i++){
			String type = sts.get(i).getType();
			//赋值
			InAndOutDetJc jc = new InAndOutDetJc();
			jc.setJcsl(sts.get(i).getRkl());
			if(sts.get(i).getPrice()!=null){
				jc.setJcje(sts.get(i).getPrice());
			}else{
				jc.setJcje("0");
			}
			
			
			if(ls.size()==0){//第一次
				ls.add(jc);
			}else{
				boolean a = true;
				for(int j=0;j<ls.size();j++){//遍历查看结存集合是否有相同单价的材料，有-数量处理  无-加入集合
					if(Double.valueOf(jc.getJcje()).equals(Double.valueOf(ls.get(j).getJcje()))){
						//处理数量
						String sl = cutMatSl(jc.getJcsl(),ls.get(j).getJcsl(),type);
						ls.get(j).setJcsl(sl);
						a = false;
						break;
					}
				}
				if(a){//加入集合
					ls.add(jc);
				}
			}
			
		}
		//数量为0，从集合中去除
		Double sl = 0.0;
		Iterator<InAndOutDetJc> it = ls.iterator();
		while(it.hasNext()){
			InAndOutDetJc x = it.next();
			
		    if(sl.equals(Double.valueOf(x.getJcsl()))){
		        it.remove();
		    }
		}
		return ls;
	}
	
	//处理物料数量
	public String cutMatSl(String dqsl,String yysl,String type){
		Double a = 0.0;
		if("1".equals(type)||"5".equals(type)||"4".equals(type)){//入库，相加
			a = ari.add(Double.valueOf(dqsl), Double.valueOf(yysl));
		}else{//出库、相减
			a = ari.sub(Double.valueOf(yysl),Double.valueOf(dqsl));
		}
		return a.toString();
		
	}
	
	//实时计算结存数量
	public List<InAndOutDetJc> doJc(List<InAndOutDetJc> ls,MatInAndOut crks){
		List<InAndOutDetJc> jcs = new ArrayList<InAndOutDetJc>();
		for(InAndOutDetJc j:ls){
			InAndOutDetJc newJc = new InAndOutDetJc();
			newJc.setJcje(j.getJcje());
			newJc.setJcsl(j.getJcsl());
			jcs.add(newJc);
		}
		
		String type = crks.getType();
		InAndOutDetJc jc = new InAndOutDetJc();
		jc.setJcsl(crks.getRkl());
		if(crks.getPrice()!=null){
			jc.setJcje(crks.getPrice());
		}else{
			jc.setJcje("0");
		}
		if(jcs.size()==0){//没数据
			jcs.add(jc);
		}else{
			boolean a = true;
			for(int j=0;j<jcs.size();j++){//遍历查看结存集合是否有相同单价的材料，有-数量处理  无-加入集合
				if(Double.valueOf(jc.getJcje()).equals(Double.valueOf(jcs.get(j).getJcje()))){
					//处理数量
					String sl = cutMatSl(jc.getJcsl(),jcs.get(j).getJcsl(),type);
					jcs.get(j).setJcsl(sl);
					a = false;
					break;
				}
			}
			if(a){//加入集合
				jcs.add(jc);
			}
		}
		//赋予结存新实例，塞进list返回
		Double sl = 0.0;
		Iterator<InAndOutDetJc> it = jcs.iterator();
		while(it.hasNext()){
			InAndOutDetJc x = it.next();
			
		    if(sl.equals(Double.valueOf(x.getJcsl()))){
		        it.remove();
		    }
		}
		return jcs;
	}
	
	//获取统计结果
	@RequestMapping("/getInAndOutInfo")
	@ResponseBody
	public Map<String,Object> getInAndOutInfo(HttpServletRequest request){
		Map<String,Object> m = new HashMap<String, Object>();
		String tjid = request.getParameter("id");
		
		InAndOut tj = baseService.get(InAndOut.class, tjid);
		List<InAndOutDet> mxs =   baseService.find(" from InAndOutDet where pid='"+tjid+"'");
		List<Map<String,Object>> mxjcs = new ArrayList<Map<String,Object>>();
		for(InAndOutDet mx: mxs){
			Map<String,Object> mxjc = new HashMap<String, Object>();
			List<InAndOutDetJc> jcs =   baseService.find(" from InAndOutDetJc where pid='"+mx.getId()+"'");
			mxjc.put("mx", mx);
			mxjc.put("jcs", jcs);
			mxjcs.add(mxjc);
		}
		m.put("tj", tj);
		m.put("mxjcs", mxjcs);
		
		return m;
	}
}
