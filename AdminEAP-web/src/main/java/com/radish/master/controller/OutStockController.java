package com.radish.master.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.BudgetTx;
import com.radish.master.entity.Materiel;
import com.radish.master.entity.Outstock;
import com.radish.master.entity.OutstockDet;
import com.radish.master.entity.Project;
import com.radish.master.entity.Purchase;
import com.radish.master.entity.Stock;
import com.radish.master.entity.StockChannel;
import com.radish.master.pojo.Options;
import com.radish.master.system.Arith;

@Controller
@RequestMapping("/outstock")
public class OutStockController {
String prefix = "/outstock/";
	
	@Autowired
	private BaseService baseService;
	@Autowired
	private SessionFactory sessionFactory;
	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}
	@RequestMapping("/addIndex")
	public String addIndex(){
		return prefix +"addIndex_list";
	}
	@RequestMapping("/add")
	public String addEdit(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("outstockId", id);
		
		List<User> ul = baseService.findMapBySql("select u.name name ,u.id id  from tbl_user u ", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("userOptions", JSONArray.toJSONString(ul));
		
		List<Project> p = baseService.findMapBySql("select p.project_name projectName ,p.id id  from tbl_project p ", new Object[]{}, new Type[]{StringType.INSTANCE}, Project.class);
		request.setAttribute("projectOptions", JSONArray.toJSONString(p));
		
		return prefix +"addIndex_Edit";
	}
	
	@RequestMapping("/look")
	public String lookEdit(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("outstockId", id);
		
		String lx = request.getParameter("lx");
		request.setAttribute("lx", lx);
		
		List<User> ul = baseService.findMapBySql("select u.name name ,u.id id  from tbl_user u ", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("userOptions", JSONArray.toJSONString(ul));
		
		List<Project> p = baseService.findMapBySql("select p.project_name projectName ,p.id id  from tbl_project p ", new Object[]{}, new Type[]{StringType.INSTANCE}, Project.class);
		request.setAttribute("projectOptions", JSONArray.toJSONString(p));
		
		return prefix +"lookIndex_Edit";
	}
	
	
	@RequestMapping("/save")
	@ResponseBody
	public Result saveOutStock(HttpServletRequest request,Outstock ck){
		
		ck.setCreateDate(new Date());
		ck.setStatus("10");
		
		Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
		String str = "CK"+year;
		List<Integer> list = baseService.find("select max(mat.xh) from com.radish.master.entity.Outstock mat");
    	if(list.isEmpty()||list.get(0)==null){
    		ck.setNumber(str+"100001");
    		ck.setXh(100001);
    	}else{
    		Integer i= list.get(0);
    		i++;
    		ck.setNumber(str+i);
    		ck.setXh(i);
    	}
    	ck.setCreatId(SecurityUtil.getUserId());
    	//先手动输入使用部位
    	//BudgetTx bt = baseService.get(BudgetTx.class, ck.getBudgetTxId());
    	//ck.setSybw(bt.getRegionName());
    	baseService.save(ck);
		
		Result r = new Result();
		r.setSuccess(true);
		r.setData(ck);
		return r;
		
	}
	
	@RequestMapping("/getoutstock")
	@ResponseBody
	public Result getoutstock(HttpServletRequest request,String id){
		Outstock ck = baseService.get(Outstock.class, id);
		
		Result r = new Result();
		r.setSuccess(true);
		r.setData(ck);
		return r;
	}
	/**
		 * 根据项目获取班组和部位
		 * @author wangzhihao
		 * @创建时间 2018年4月19日 下午9:49:42
		 * @return
		 */
	@RequestMapping("/getBzAndBw")
	@ResponseBody
	public Map<String,Object> getBzAndBw(HttpServletRequest request){
		Map<String,Object> m = new HashMap<String,Object>();
		String xmid = request.getParameter("xmid");
		
		List bws = baseService.findMapBySql("select id value, region_name data from tbl_budget_tx where project_id='"+xmid+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
		List teams = baseService.findMapBySql("select id value, team_name data from tbl_project_team where project_id='"+xmid+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
		
		m.put("bws", bws);
		m.put("teams", teams);
		m.put("success", "true");
		return m;
	}
	
	@RequestMapping("/addOutstockDet")
	@ResponseBody
	public Result addOutstockDet(HttpServletRequest request,String stockid,String remark,Double ckl){
		Arith arith = new Arith();
		String ckid = request.getParameter("ckid");
		Double yskcl = ckl;
		//库存
		Stock kc = baseService.get(Stock.class, stockid);
		//先进先出。。库存渠道
		String sql = "select * from tbl_stock_channel where project_id = '"+kc.getProject_id()+"' "
				+ " and mat_id='"+kc.getMat_id()+"' and available_num>0  order by create_date_time";
		List<StockChannel> qds = baseService.findBySql(sql, StockChannel.class);
		//物料信息
		List<Materiel> wls =baseService.findBySql(" select * from tbl_materiel where mat_number='"+kc.getMat_id()+"'", Materiel.class); 
		
		for(StockChannel qd :qds){//新增出库明细（循环库存渠道）
			OutstockDet ckmx = new OutstockDet();
			ckmx.setMatName(wls.get(0).getMat_name());
			ckmx.setMatNumber(wls.get(0).getMat_number());
			ckmx.setMatStandard(wls.get(0).getMat_standard());
			ckmx.setUnit(wls.get(0).getUnit());
			ckmx.setOutstockId(ckid);
			ckmx.setStockId(stockid);
			ckmx.setsChannelId(qd.getId());
			ckmx.setPrice(qd.getPrice()+"");
			ckmx.setRemark(remark);
			
			if(qd.getAvailable_num()>=ckl){//该渠道库存可用大于出库
				ckmx.setCkl(ckl+"");
				
				Double syky = arith.sub(qd.getAvailable_num(), ckl);
				qd.setAvailable_num(syky);//可用数量
				Double syzl = arith.sub(qd.getStock_num(), ckl);
				qd.setStock_num(syzl);//总量
				
	
				
				ckl =0.0;
			}else{
				ckmx.setCkl(qd.getAvailable_num() +"");
				
				
				ckl = arith.sub(ckl,qd.getAvailable_num());
				qd.setAvailable_num(0.0);
				qd.setStock_num(0.0);
			}
			baseService.save(ckmx);
			baseService.update(qd);
			if(ckl<=0){
				break;
			}
		}
		//总库存数量减少
		kc.setAvailable_num(arith.sub(kc.getAvailable_num(), yskcl));
		kc.setStock_num(arith.sub(kc.getStock_num(), yskcl));
		baseService.update(kc);
		
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	
	@RequestMapping("/delOutstockDet")
	@ResponseBody
	public Result delOutstockDet(HttpServletRequest request){
		Arith arith = new Arith();
		String kcmxid = request.getParameter("id");
		OutstockDet kcmx = baseService.get(OutstockDet.class, kcmxid);
		Double ckl = Double.valueOf(kcmx.getCkl());
		
		//库存加回
		Stock kc = baseService.get(Stock.class, kcmx.getStockId());
		Double syky = arith.add(kc.getAvailable_num(), ckl);
		kc.setAvailable_num(syky);
		Double syzl = arith.add(kc.getStock_num(), ckl);
		kc.setStock_num(syzl);
		
		//渠道库存加回
		StockChannel qd = baseService.get(StockChannel.class, kcmx.getsChannelId());
		Double qdsyky  = arith.add(qd.getAvailable_num(), ckl);
		qd.setAvailable_num(qdsyky);//可用数量
		Double qdsyzl = arith.add(qd.getStock_num(), ckl);
		qd.setStock_num(qdsyzl);//总量
		
		baseService.update(kc);
		baseService.update(qd);
		
		baseService.delete(kcmx);
		
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	
	@RequestMapping("/deleteCkd")
	@ResponseBody
	public Result deleteCkd(HttpServletRequest request){
		Arith arith = new Arith();
		String ckid = request.getParameter("ckid");
		Outstock ck = baseService.get(Outstock.class, ckid);
		List<OutstockDet> ckmxs = baseService.findBySql(" select * from tbl_outstock_det where outstockId='"+ckid+"'", OutstockDet.class); 
		for(OutstockDet ckmx :ckmxs){
			String kcmxid = ckmx.getId();
			OutstockDet kcmx = baseService.get(OutstockDet.class, kcmxid);
			Double ckl = Double.valueOf(kcmx.getCkl());
			
			//库存加回
			Stock kc = baseService.get(Stock.class, kcmx.getStockId());
			Double syky = arith.add(kc.getAvailable_num(), ckl);
			kc.setAvailable_num(syky);
			Double syzl = arith.add(kc.getStock_num(), ckl);
			kc.setStock_num(syzl);
			
			//渠道库存加回
			StockChannel qd = baseService.get(StockChannel.class, kcmx.getsChannelId());
			Double qdsyky  = arith.add(qd.getAvailable_num(), ckl);
			qd.setAvailable_num(qdsyky);//可用数量
			Double qdsyzl = arith.add(qd.getStock_num(), ckl);
			qd.setStock_num(qdsyzl);//总量
			
			baseService.update(kc);
			baseService.update(qd);
			
			baseService.delete(kcmx);
		}
		baseService.delete(ck);
		
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	@RequestMapping("/doYes")
	@ResponseBody
	public Result doYes(HttpServletRequest request){
		String ckid = request.getParameter("ckid");
		Outstock ck = baseService.get(Outstock.class, ckid);
		ck.setStatus("20");
		ck.setOutdate(new Date());
		baseService.update(ck);
		
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	
}
