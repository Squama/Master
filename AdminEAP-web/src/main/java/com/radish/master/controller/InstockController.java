package com.radish.master.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.StringHelper;
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
import com.radish.master.entity.Channel;
import com.radish.master.entity.Instock;
import com.radish.master.entity.InstockDet;
import com.radish.master.entity.Purchase;
import com.radish.master.entity.PurchaseDet;
import com.radish.master.entity.Stock;
import com.radish.master.entity.StockChannel;
import com.radish.master.entity.StockHistory;
import com.radish.master.system.Arith;

@Controller
@RequestMapping("/instock")
public class InstockController {
	String prefix = "/instock/";
	
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
	
	@RequestMapping("/checkIndex")
	public String checkIndex(){
		return prefix +"check/checkIndex_list";
	}
	@RequestMapping("/rkIndex")
	public String rkIndex(){
		return prefix +"rk/rkIndex_list";
	}
	
	@RequestMapping("/add")
	public String addEdit(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("instockId", id);
		
		List<User> ul = baseService.findMapBySql("select u.name name ,u.id id  from tbl_user u where u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("userOptions", JSONArray.toJSONString(ul));
		
		if(id==null){//新增只能看见40、50状态的采购单
			List<Purchase> ps = baseService.findMapBySql("select p.purchase_name name ,p.id id  from tbl_purchase p where p.status in (40,50)", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
			request.setAttribute("purchaseOptions", JSONArray.toJSONString(ps));
		}else{
			List<Purchase> ps = baseService.findMapBySql("select p.purchase_name name ,p.id id  from tbl_purchase p where p.status in (40,50,60)", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
			request.setAttribute("purchaseOptions", JSONArray.toJSONString(ps));
		}
		
		return prefix +"addIndex_Edit";
	}
	
	@RequestMapping("/checkEdit")
	public String checkEdit(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("instockId", id);
		
		List<User> ul = baseService.findMapBySql("select u.name name ,u.id id  from tbl_user u where u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("userOptions", JSONArray.toJSONString(ul));
		
		if(id==null){//新增只能看见40、50状态的采购单
			List<Purchase> ps = baseService.findMapBySql("select p.purchase_name name ,p.id id  from tbl_purchase p where p.status in (40,50)", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
			request.setAttribute("purchaseOptions", JSONArray.toJSONString(ps));
		}else{
			List<Purchase> ps = baseService.findMapBySql("select p.purchase_name name ,p.id id  from tbl_purchase p where p.status in (40,50,60)", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
			request.setAttribute("purchaseOptions", JSONArray.toJSONString(ps));
		}
		request.setAttribute("hdname", SecurityUtil.getUserId());
		return prefix +"check/checkIndex_Edit";
	}
	@RequestMapping("/rkEdit")
	public String rkEdit(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("instockId", id);
		
		List<User> ul = baseService.findMapBySql("select u.name name ,u.id id  from tbl_user u where u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("userOptions", JSONArray.toJSONString(ul));
		
		if(id==null){//新增只能看见40、50状态的采购单
			List<Purchase> ps = baseService.findMapBySql("select p.purchase_name name ,p.id id  from tbl_purchase p where p.status in (40,50)", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
			request.setAttribute("purchaseOptions", JSONArray.toJSONString(ps));
		}else{
			List<Purchase> ps = baseService.findMapBySql("select p.purchase_name name ,p.id id  from tbl_purchase p where p.status in (40,50,60)", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
			request.setAttribute("purchaseOptions", JSONArray.toJSONString(ps));
		}
		request.setAttribute("rkname", SecurityUtil.getUserId());
		return prefix +"rk/rkIndex_Edit";
	}
	@RequestMapping("/look")
	public String lookEdit(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("instockId", id);
		
		List<User> ul = baseService.findMapBySql("select u.name name ,u.id id  from tbl_user u where u.audit_status = 10", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("userOptions", JSONArray.toJSONString(ul));
		
		if(id==null){//新增只能看见40、50状态的采购单
			List<Purchase> ps = baseService.findMapBySql("select p.purchase_name name ,p.id id  from tbl_purchase p where p.status in (40,50)", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
			request.setAttribute("purchaseOptions", JSONArray.toJSONString(ps));
		}else{
			List<Purchase> ps = baseService.findMapBySql("select p.purchase_name name ,p.id id  from tbl_purchase p where p.status in (40,50,60)", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
			request.setAttribute("purchaseOptions", JSONArray.toJSONString(ps));
		}
		
		return prefix +"look_Edit";
	}
	
	
	@RequestMapping("/save")
	@ResponseBody
	public Result saveInstock(HttpServletRequest request,Instock rk){
		String cgid= rk.getPurchaseId();
		Purchase cg = baseService.get(Purchase.class, cgid);
		rk.setProjectId(cg.getProjectID());
		rk.setCreateDate(new Date());
		rk.setStatus("10");
		Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
		String str = "RK"+year;
		List<Integer> list = baseService.find("select max(mat.xh) from com.radish.master.entity.Instock mat");
    	if(list.isEmpty()||list.get(0)==null){
    		rk.setNumber(str+"100001");
    		rk.setXh(100001);
    	}else{
    		Integer i= list.get(0);
    		i++;
    		rk.setNumber(str+i);
    		rk.setXh(i);
    	}
    	rk.setCreatId(SecurityUtil.getUserId());
    	baseService.save(rk);
		
		Result r = new Result();
		r.setSuccess(true);
		r.setData(rk);
		return r;
	}
	
	@RequestMapping("/getinstock")
	@ResponseBody
	public Result getInstock(HttpServletRequest request,String id){
		Instock i = baseService.get(Instock.class, id);
		List<String>  hj = baseService.find("select sum(mx.price*mx.rkl) from com.radish.master.entity.InstockDet mx where  mx.instockId='"+id+"'");
		Result r = new Result();
		r.setSuccess(true);
		r.setData(i);
		r.setCode(hj.get(0));
		return r;
	}
	
	@RequestMapping("/addStockDet")
	@ResponseBody
	public Result addStockDet(HttpServletRequest request,String remark){
		Arith arith = new Arith();
		
		String cgid= request.getParameter("cgid");
		String rksl = request.getParameter("rksl");
		String dmrkl = request.getParameter("dmrkl");
		
		String rkid = request.getParameter("rkid");
		PurchaseDet cg = baseService.get(PurchaseDet.class, cgid);
		
		//如果入库单有该物料，则进行累加
		List<InstockDet> cgs = baseService.findBySql("select mx.* from tbl_instock_det mx where mx.pDetId='"+cgid+"' and mx.instockId='"+rkid+"'",InstockDet.class);
		if(cgs.size()>0){
			InstockDet rkmx = cgs.get(0);
			rkmx.setRkl(arith.add(Double.parseDouble(rkmx.getRkl()),Double.parseDouble(rksl))+"");
			//1
			if(!StringHelper.isEmpty(dmrkl)){
				rkmx.setDmrkl(arith.add(Double.parseDouble(rkmx.getDmrkl()),Double.parseDouble(dmrkl))+"");
			}
			//有备注就覆盖
			if(!StringHelper.isEmpty(remark)){
				rkmx.setRemark(remark);
			}
			baseService.update(rkmx);
			
			cg.setSurplusQuantity(arith.sub(cg.getSurplusQuantity(), Double.parseDouble(rksl)));
			baseService.update(cg);
			
			cgStatus(cg.getPurchaseID());
			
			Result r = new Result();
			r.setSuccess(true);
			return r;
		}
		
		
		
		InstockDet rkmx = new InstockDet();
		rkmx.setInstockId(rkid);
		rkmx.setMatName(cg.getMatName());
		rkmx.setMatNumber(cg.getMatNumber());
		rkmx.setMatStandard(cg.getMatStandard());
		rkmx.setpDetId(cgid);
		rkmx.setPrice(cg.getPrice());
		rkmx.setRkl(rksl);
		rkmx.setUnit(cg.getUnit());
		rkmx.setChannelId(cg.getChannelID());
		rkmx.setTeamId(cg.getTeamCode());
		rkmx.setTeamName(cg.getTeamName());
		//2
		if(StringHelper.isEmpty(dmrkl)){//多买入库量
			rkmx.setDmrkl("0");
		}else{
			rkmx.setDmrkl(dmrkl);
		}
		rkmx.setRemark(remark);
		baseService.save(rkmx);
		
		cg.setSurplusQuantity(arith.sub(cg.getSurplusQuantity(), Double.parseDouble(rksl)));
		if(cg.getSurplusQuantity()<=0){
			cg.setStatus("60");
		}else{
			cg.setStatus("50");
		}
		baseService.update(cg);
		
		//判断采购单是否完全入库
		cgStatus(cg.getPurchaseID());
		
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	//判断采购单是否完全入库
	public void cgStatus(String cgid){
		List<PurchaseDet> cgs = baseService.findBySql("select mx.* from tbl_purchase_det mx where mx.purchase_id='"+cgid+"'",PurchaseDet.class);
		Boolean rk = false;
		for(PurchaseDet cg:cgs){
			if(cg.getSurplusQuantity()>0){
				rk = true;
				break;
			}
		}
		Purchase cg = baseService.get(Purchase.class, cgid);
		if(rk){
			cg.setStatus("50");
		}else{
			cg.setStatus("60");
		}
		baseService.update(cg);
	}
	@RequestMapping("/deleteStockDet")
	@ResponseBody
	public Result deleteStockDet(HttpServletRequest request){
		String rkmxid = request.getParameter("id");
		InstockDet rkmx = baseService.get(InstockDet.class, rkmxid);
		Arith arith = new Arith();
		PurchaseDet cgmx = baseService.get(PurchaseDet.class, rkmx.getpDetId());
		cgmx.setSurplusQuantity(arith.add(cgmx.getSurplusQuantity(), Double.parseDouble(rkmx.getRkl())));
		cgmx.setStatus("50");
		baseService.update(cgmx);
		baseService.delete(rkmx);
		
		//编辑采购单为部分入库
		Purchase cg = baseService.get(Purchase.class, cgmx.getPurchaseID());
		cg.setStatus("50");
		baseService.update(cg);
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	/**
	 * 删除入库单
	 * @author wangzhihao
	 * @创建时间 2018年4月2日 下午3:52:26
	 * @return
	 */
	@RequestMapping("/deleteRkd")
	@ResponseBody
	public Result deleteRkd(HttpServletRequest request){
		Arith arith = new Arith();
		Result r = new Result();
		String rkid = request.getParameter("rkid");
		Instock rk = baseService.get(Instock.class, rkid);
		if(!"10".equals(rk.getStatus())){
			r.setSuccess(false);
			r.setMessage("已核对或者已入库的入库单不能删除");
			return r;
		}
		List<InstockDet> rkmxs = baseService.findBySql("select mx.* from tbl_instock_det mx where  mx.instockId='"+rkid+"'",InstockDet.class);
		//删除入库明细。恢复采购单数量和状态
		for(InstockDet rkmx:rkmxs){
			String cgmxid = rkmx.getpDetId();
			PurchaseDet cgmx = baseService.get(PurchaseDet.class, cgmxid);
			cgmx.setSurplusQuantity(arith.add(cgmx.getSurplusQuantity(), Double.parseDouble(rkmx.getRkl())));
			cgmx.setStatus("50");
			baseService.update(cgmx);
			baseService.delete(rkmx);
		}
		Purchase cg = baseService.get(Purchase.class, rk.getPurchaseId());
		cg.setStatus("50");
		baseService.update(cg);
		baseService.delete(rk);
		r.setSuccess(true);
		return r;
		
	}
	
	/**
	 * 核对入库单
	 * @author wangzhihao
	 * @创建时间 2018年4月2日 下午4:00:47
	 * @return
	 */
	@RequestMapping("/docheck")
	@ResponseBody
	public Result docheck(HttpServletRequest request){
		Result r = new Result();
		String rkid = request.getParameter("rkid");
		Instock rk = baseService.get(Instock.class, rkid);
		if(!"10".equals(rk.getStatus())){
			r.setSuccess(false);
			r.setMessage("非新建的入库单不能进行核对");
			return r;
		}
		
		rk.setChecker_id(SecurityUtil.getUserId());
		rk.setStatus("20");
		baseService.update(rk);
		
		r.setSuccess(true);
		return r;
	}
	
	/**
	 * 入库单入库
	 * @author wangzhihao
	 * @创建时间 2018年4月2日 下午2:59:46
	 * @return
	 */
	@RequestMapping("/doRk")
	@ResponseBody
	public Result doRk(HttpServletRequest request){
		Arith arith = new Arith();
		String rkid = request.getParameter("rkid");
		Instock rk = baseService.get(Instock.class, rkid);
		List<InstockDet> rkmxs = baseService.findBySql("select mx.* from tbl_instock_det mx where  mx.instockId='"+rkid+"'",InstockDet.class);
 		
		for(InstockDet rkmx:rkmxs){
			//入库stock
			Stock stock = new Stock();
	        String sql = " select * from tbl_stock where mat_id='"+rkmx.getMatNumber()+"' and project_ID='"+rk.getProjectId()+"'";
	        List<Stock> list= baseService.findBySql(sql, Stock.class);
	        if(list.size()==0){//同一种物料在一个项目下无记录，做新增
	            //新增库存记录（入库）
	            stock.setProject_id(rk.getProjectId());
	            stock.setMat_id(rkmx.getMatNumber());
	            stock.setStock_num(Double.parseDouble(rkmx.getRkl()));
	            stock.setFrozen_num(0.0);
	            stock.setAvailable_num(Double.parseDouble(rkmx.getRkl()));
	            stock.setUsetype("1");//1:采购入库，2：调度入库
	            stock.setStorage_person_id(SecurityUtil.getUserId());
	            stock.setStorage_time(new Date());
	            baseService.save(stock);
	        }else{
	            //同一种物料在一个项目下原有库存，更新数量
	            stock = baseService.get(Stock.class,list.get(0).getId());
	            stock.setStock_num(arith.add(stock.getStock_num() ,Double.parseDouble(rkmx.getRkl())));
	            stock.setAvailable_num(arith.add(stock.getAvailable_num(),Double.parseDouble(rkmx.getRkl())));
	            baseService.update(stock);
	        }
			
			//入库渠道编辑
			saveChannel(rkmx.getMatNumber(),rk.getProjectId(),rkmx.getChannelId(),Double.parseDouble(rkmx.getRkl()));
			//入库历史
			StockHistory tockHistory = new StockHistory();
	        tockHistory.setProject_id(rk.getProjectId());
	        tockHistory.setMat_id(rkmx.getMatNumber());
	        tockHistory.setStock_change_num(Double.parseDouble(rkmx.getRkl()));
	        tockHistory.setOperation_bill_ID(rk.getNumber()); //.set 入库单编号
	        tockHistory.setStockSource(rkmx.getId());//库存操作来源
	        tockHistory.setUsetpye("1");//1：采购入库 2：调度入库 3：消耗出库  4：调度出库  5：初始库存
	        tockHistory.setOperation_person_id(SecurityUtil.getUserId());
	        tockHistory.setOperation_time(new Date());
	        baseService.save(tockHistory);
		}
		//更新入库单状态，入库人员id，入库日期
		rk.setStatus("30");
		rk.setIner_id(SecurityUtil.getUserId());
		rk.setIndate(new Date());
		baseService.update(rk);
		
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	
	//库存渠道保存 入库
    public Boolean saveChannel(String mat_ID, String project_ID, String channel_ID, Double stockNum) {
    	Arith arith = new Arith();
        StockChannel stockChannel = getStockChannel(mat_ID,project_ID,channel_ID);
        Double price = getChannelByID(channel_ID).getPrice();
        if(stockChannel==null){
            stockChannel = new StockChannel();
            stockChannel.setMat_id(mat_ID);
            stockChannel.setProject_id(project_ID);
            stockChannel.setChannel_id(channel_ID);
            stockChannel.setStock_num(stockNum);
            stockChannel.setAvailable_num(stockNum);
            stockChannel.setPrice(price);
            stockChannel.setFrozen_num(0.0);
            baseService.save(stockChannel);
        }else{
            stockChannel.setStock_num(arith.add(stockChannel.getStock_num(),stockNum));
            stockChannel.setAvailable_num(arith.add(stockChannel.getAvailable_num(),stockNum));
            baseService.update(stockChannel);
        }
        
        return true;
    }
    public StockChannel getStockChannel(String mat_ID, String project_ID, String channel_ID) {
        String sql = " select * from tbl_stock_channel where mat_id='" + mat_ID + "' and project_ID='" + project_ID + "' and channel_id ='"+channel_ID+"'";
        List<StockChannel> list = baseService.findBySql(sql, StockChannel.class);
        if(list.size()>0){
            return list.get(0);
        }else {
            return  null;
        }
    }
    public Channel getChannelByID(String channel_ID){
        String sql = "select * from tbl_channel where id = '"+channel_ID+"'";
        List<Channel> list = baseService.findBySql(sql,Channel.class);
        if(list.size()>0){
            return  list.get(0);
        }
        return  null;
    }
    
}
