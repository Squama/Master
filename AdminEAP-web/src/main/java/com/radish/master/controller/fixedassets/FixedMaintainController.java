package com.radish.master.controller.fixedassets;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.Org;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.fixedassets.FixedAssetsStk;
import com.radish.master.entity.fixedassets.FixedAssetsStkTx;
import com.radish.master.entity.fixedassets.FixedAssetsUse;
import com.radish.master.entity.fixedassets.FixedMaintain;
import com.radish.master.entity.fixedassets.FixedMaintainTx;
import com.radish.master.entity.review.MaxNumber;
import com.radish.master.system.Arith;

/**
 * 固定资产请修
 * @author wzh
 * @创建时间 2019年4月1日 下午3:59:48
 * @return
 */
@Controller
@RequestMapping("/fixedmaintain")
public class FixedMaintainController {
	String prefix = "/fixedassets/maintain/";
	@Autowired
	private BaseService baseService;
	@Resource
	private RuntimePageService runtimePageService;
	
	@RequestMapping("/listindex")
	public String listindex(HttpServletRequest request){
		String isone = request.getParameter("isone");
		request.setAttribute("isone", isone);
		request.setAttribute("qxr", SecurityUtil.getUser().getLoginName());
		return prefix+"listindex";
	}
	
	public  String maxNum(){ 
		List<String> result = baseService.find("select max(mat.id) from com.radish.master.entity.review.MaxNumber mat");
		if(result.size()>0&&!"".equals(result.get(0))&&StringHelper.isNotEmpty(result.get(0))){
			String num = result.get(0);
			
			MaxNumber m = new MaxNumber();
			Integer newId = Integer.valueOf(num)+1;
			m.setId(newId+"");
			baseService.save(m);
			return num;
		}else{
			MaxNumber m = new MaxNumber();
			m.setId("1001");
			baseService.save(m);
			return "1001";
		}
	}
	
	@RequestMapping("/add")
	public String add(HttpServletRequest request){
		String  id = request.getParameter("id");
		User u = SecurityUtil.getUser();
		if(id==null){
			String str =maxNum();
			
			Calendar date = Calendar.getInstance();
			String year = String.valueOf(date.get(Calendar.YEAR));
			String strs = year+str;
			
			
			request.setAttribute("bh",strs);
			request.setAttribute("ryid", u.getId());
			request.setAttribute("xm", u.getLoginName());
			if(u.getDeptId()!=null){
				Org bm = baseService.get(Org.class, u.getDeptId());
				if(bm==null){
					request.setAttribute("msg","未找到所属部门，请联系办公管理人员！");	
				}
			}else{
				request.setAttribute("msg","请联系办公管理人员，完善个人部门信息！");
			}
		}else{
			request.setAttribute("id", id);
			request.setAttribute("ryid", u.getId());
			request.setAttribute("xm", u.getLoginName());
		}
		String isone = request.getParameter("isone");
		request.setAttribute("isone", isone);
		if("10".equals(isone)){//个人申请
			return prefix+"addone";
		}
		return prefix+"add";
	}
	
	@RequestMapping("/cwlook")
	public String cwlook(HttpServletRequest request){
		String  id = request.getParameter("id");
		request.setAttribute("id", id);
		
		return prefix+"look";
	}
	@RequestMapping("/addMx")
	public String addMx(HttpServletRequest request){
		String  zcid = request.getParameter("zcid");
		String  wjid = request.getParameter("wjid");
		String  isone = request.getParameter("isone");
		String  sqid = request.getParameter("sqid");
		String  sl = request.getParameter("sl");
		request.setAttribute("zcid", zcid);
		request.setAttribute("isone", isone);
		request.setAttribute("sqid", sqid);
		request.setAttribute("sl", sl);
		request.setAttribute("wjid", wjid);
		return prefix+"addMx";
	}
	/**
	 * 部门审核列表
	 * @author wzh
	 * @创建时间 2019年4月8日 下午2:04:27
	 * @return
	 */
	@RequestMapping("/bmshindex")
	public String bmshindex(HttpServletRequest request){
		User u = SecurityUtil.getUser();
		String deptid = u.getDeptId();
		request.setAttribute("deptid", deptid);
		return prefix+"bmshList";
	}
	
	/**
	 * 部门审核框
	 * @author wzh
	 * @创建时间 2019年4月8日 下午2:03:14
	 * @return
	 */
	@RequestMapping("/bmsh")
	public String bmsh(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		return prefix+"bmsh";
	}
	
	/**
	 * 部门接收
	 * @author wzh
	 * @创建时间 2019年4月8日 下午2:03:14
	 * @return
	 */
	@RequestMapping("/bmjs")
	public String bmjs(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		return prefix+"bmjs";
	}
	
	/**
	 * 领导审核
	 * @author wzh
	 * @创建时间 2019年4月8日 下午3:07:13
	 * @return
	 */
	@RequestMapping("/auidtBoss/{id}")//审核查看页
	public String auidtBoss(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("id", id);
		return prefix+"auidtBoss";
		
	}
	/**
	 * 流程送检
	 * @author wzh
	 * @创建时间 2019年4月8日 下午3:07:48
	 * @return
	 */
	@RequestMapping("/auidtSj/{id}")//审核查看页
	public String auidtSj(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("id", id);
		return prefix+"auidtSj";
		
	}
	
	
	
	@RequestMapping("/loadqx")
	@ResponseBody
	public Result loadqx(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		FixedMaintain qx = baseService.get(FixedMaintain.class, id);
		r.setData(qx);
		
		return r;
	}
	
	
	@RequestMapping("/saveqx")
	@ResponseBody
	public Result saveqx(HttpServletRequest request,FixedMaintain qx){
		Result r = new Result();
		String id = request.getParameter("id");
		//是个人还是库管
		String isone = request.getParameter("isone");
		User u = SecurityUtil.getUser();
		Org bm = baseService.get(Org.class, u.getDeptId());
		if(id==null){//保存
			qx.setSqrid(u.getId());
			qx.setSqr(u.getLoginName());
			qx.setQxdeptid(bm.getId());
			qx.setQxdept(bm.getName());
			qx.setCreatedate(new Date());
			qx.setStatus("10");
			qx.setIsone(isone);
			baseService.save(qx);
			id =  qx.getId();
		}else{
			FixedMaintain q = baseService.get(FixedMaintain.class, id);
			q.setWxtype(qx.getWxtype());
			baseService.update(q);
		}
		r.setCode(id);
		return r;
	}
	
	/**
	 * 删除请修数据
	 * @author wzh
	 * @创建时间 2019年4月2日 下午1:11:14
	 * @return
	 */
	@RequestMapping("/delqx")
	@ResponseBody
	public Result delqx(HttpServletRequest request,FixedMaintain qx){
		Arith ari = new Arith();
		Result r = new Result();
		String sqid = request.getParameter("sqid");
		FixedMaintain q = baseService.get(FixedMaintain.class, sqid);
		List<FixedMaintainTx> mxs = baseService.find(" from FixedMaintainTx where pid='"+sqid+"'");
		for(FixedMaintainTx mx:mxs){
			if("20".equals(q.getIsone())){//库存加回去
				FixedAssetsStk kc = baseService.get(FixedAssetsStk.class, mx.getZcid());//库存
				Double sy = ari.add(Double.valueOf(kc.getQuantityAvl()), Double.valueOf(mx.getQuantity()));
				kc.setQuantityAvl(sy.toString());
				baseService.update(kc);
			}
			//删除库存明细
			List<FixedAssetsStkTx> kcmxs = baseService.find(" from FixedAssetsStkTx where source_tx_id='"+mx.getId()+"'");
			for(FixedAssetsStkTx kcmx:kcmxs){
				baseService.delete(kcmx);
			}
			//删除维修明细
			baseService.delete(mx);
		}
		baseService.delete(q);
		return r;
	}
	
	@RequestMapping("/saveMx")
	@ResponseBody
	public Result saveMx(HttpServletRequest request,FixedMaintainTx mx){
		Arith ari = new Arith();
		Result r = new Result();
		String sqid = request.getParameter("sqid");
		String isone = request.getParameter("isone");
		String zcid = request.getParameter("zcid");//资产库存id
		String wjid = request.getParameter("wjid");//外借id
		FixedMaintain q = baseService.get(FixedMaintain.class, sqid);
		mx.setPid(sqid);
		mx.setZcid(zcid);
		mx.setSybmid(q.getQxdeptid());
		mx.setSybm(q.getQxdept());
		mx.setGhsl("0");
		baseService.save(mx);
		if("20".equals(isone)){//库存操作 需要库存剩余减去数量 存入库存表剩余  并存入操作明细表
			FixedAssetsStk kc = baseService.get(FixedAssetsStk.class, zcid);//库存
			Double sy = ari.sub(Double.valueOf(kc.getQuantityAvl()), Double.valueOf(mx.getQuantity()));
			kc.setQuantityAvl(sy.toString());
			baseService.update(kc);
			mx.setName(kc.getName());
			mx.setNorm(kc.getNorm());
			
			FixedAssetsStkTx kcmx = new FixedAssetsStkTx();
			kcmx.setFixedAssetsID(zcid);
			kcmx.setOperation("40");
			kcmx.setOperator(q.getSqr());
			kcmx.setOperateTime(new Date());
			kcmx.setAmount(mx.getQuantity());
			kcmx.setBalance(sy.toString());
			kcmx.setPrice(kc.getPrice());
			kcmx.setRemark("库存资产请修");
			kcmx.setSourceTxID(mx.getId());
			baseService.save(kcmx);
		}else{//只存操作明细表
			FixedAssetsUse wj = baseService.get(FixedAssetsUse.class, wjid);//外借
			
			mx.setName(wj.getName());
			mx.setNorm(wj.getNorm());
			mx.setZcid(wj.getStkID());
			
			FixedAssetsStkTx kcmx = new FixedAssetsStkTx();
			kcmx.setFixedAssetsID(zcid);
			kcmx.setOperation("40");
			kcmx.setOperator(q.getSqr());
			kcmx.setOperateTime(new Date());
			kcmx.setAmount(mx.getQuantity());
			kcmx.setBalance("/");
			kcmx.setPrice(wj.getPrice());
			kcmx.setRemark("个人领用请修");
			kcmx.setSourceTxID(mx.getId());
			baseService.save(kcmx);
			//如果是外借 存入外借信息id  方便计算外借信息中维修了多少，可请修数量= 未还数量-请修数量
			mx.setWjid(wjid);
			baseService.update(mx);
		}
		return r;
	}
	/**
	 * 删除请修明细
	 * @author wzh
	 * @创建时间 2019年4月2日 下午1:11:25
	 * @return
	 */
	@RequestMapping("/delMx")
	@ResponseBody
	public Result delMx(HttpServletRequest request){
		Result r = new Result();
		Arith ari = new Arith();
		String sqid = request.getParameter("sqid");
		String mxid = request.getParameter("mxid");
		FixedMaintain q = baseService.get(FixedMaintain.class, sqid);
		FixedMaintainTx mx = baseService.get(FixedMaintainTx.class, mxid);
		if("20".equals(q.getIsone())){//库存加回去
			FixedAssetsStk kc = baseService.get(FixedAssetsStk.class, mx.getZcid());//库存
			Double sy = ari.add(Double.valueOf(kc.getQuantityAvl()), Double.valueOf(mx.getQuantity()));
			kc.setQuantityAvl(sy.toString());
			baseService.update(kc);
		}
		//删除库存明细
		List<FixedAssetsStkTx> kcmxs = baseService.find(" from FixedAssetsStkTx where source_tx_id='"+mxid+"'");
		for(FixedAssetsStkTx kcmx:kcmxs){
			baseService.delete(kcmx);
		}
		//删除维修明细
		baseService.delete(mx);
		return r;
	}
	/**
	 * 提交部门审核
	 * @author wzh
	 * @创建时间 2019年4月8日 上午11:06:01
	 * @return
	 */
	@RequestMapping("/submitBm")
	@ResponseBody
	public Result submitBm(HttpServletRequest request){
		Result r = new Result();
		String sqid = request.getParameter("sqid");
		List<FixedMaintainTx> mxs = baseService.find(" from FixedMaintainTx where pid='"+sqid+"'");
		if(mxs.size()<=0){
			r.setSuccess(false);
			r.setMessage("请添加至少一条维修明细！");
			return r;
		}
		
		FixedMaintain q = baseService.get(FixedMaintain.class, sqid);
		q.setStatus("20");
		baseService.update(q);
		return r;
	}
	/**
	 * 部门审核提交 bmsubmit
	 * @author wzh
	 * @创建时间 2019年4月8日 下午2:35:23
	 * @return
	 */
	@RequestMapping("/bmsubmit")
	@ResponseBody
	public Result bmsubmit(HttpServletRequest request,FixedMaintain sq){
		String id = request.getParameter("id");
		String lx = request.getParameter("lx");
		
		FixedMaintain q = baseService.get(FixedMaintain.class, id);
		User user = SecurityUtil.getUser();
		if("10".equals(lx)){//通过
			q.setStatus("40");
			String name ="部门："+q.getQxdept()+",姓名："+q.getSqr()+",请修【审核】";

	        // businessKey
	        String businessKey = q.getId();

	        // 配置流程变量
	        Map<String, Object> variables = new HashMap<>();
	        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
	        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
	        variables.put("taskName", name);

	        // 启动流程
	        runtimePageService.startProcessInstanceByKey("Maintain", name, variables, user.getId(), businessKey);
			
			
		}else if("20".equals(lx)){//驳回
			q.setStatus("30");
		}
		q.setBmyj(sq.getBmyj());
		q.setBmshr(user.getName());
		baseService.update(q);
		Result r = new Result();
		return r;
	}
	/**
	 * 保存送检信息
	 * @author wzh
	 * @创建时间 2019年4月8日 下午3:54:37
	 * @return
	 */
	@RequestMapping("/savesjxx")
	@ResponseBody
	public Result savesjxx(HttpServletRequest request,FixedMaintain qx){
		Result r = new Result();
		String id = request.getParameter("id");
		User u = SecurityUtil.getUser();
		FixedMaintain q = baseService.get(FixedMaintain.class, id);
		q.setJxdate(qx.getJxdate());
		q.setCxje(qx.getCxje());
		q.setCxjhdate(qx.getCxjhdate());
		q.setCxcs(qx.getCxcs());
		q.setCxfk(qx.getCxfk());
		q.setJxr(u.getLoginName());
		baseService.update(q);
		return r;
	}
	/**
	 * 保存验收信息
	 * @author wzh
	 * @创建时间 2019年4月8日 下午3:57:20
	 * @return
	 */
	@RequestMapping("/saveysxx")
	@ResponseBody
	public Result saveysxx(HttpServletRequest request,FixedMaintain qx){
		Result r = new Result();
		String id = request.getParameter("id");
		User u = SecurityUtil.getUser();
		FixedMaintain q = baseService.get(FixedMaintain.class, id);
		q.setYscontent(qx.getYscontent());
		q.setYsjhdate(qx.getYsjhdate());
		q.setYsr(u.getLoginName());
		q.setStatus("80");
		baseService.update(q);
		return r;
	}
}
