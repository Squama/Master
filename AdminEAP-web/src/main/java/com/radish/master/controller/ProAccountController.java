package com.radish.master.controller;

import java.util.List;

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
import com.radish.master.entity.ProAccount;
import com.radish.master.entity.ProAccountDet;
import com.radish.master.pojo.Options;
import com.radish.master.system.Arith;

@Controller
@RequestMapping("/proaccount")
public class ProAccountController {
	String prefix = "/proAccount/";
	
	@Autowired
	private BaseService baseService;
	@Autowired
	private SessionFactory sessionFactory;
	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	@RequestMapping("/addList")
	public String addList(HttpServletRequest request){
		List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
		request.setAttribute("xm", JSONArray.toJSONString(xm));
		return prefix +"addList";
	}
	
	@RequestMapping("/addAccountIndex")
	public String addAccountIndex(HttpServletRequest request){
		List xm = baseService.findMapBySql("select id value, project_name data from tbl_project where id not in(select zm.projectId from tbl_projectAccount zm)", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
		request.setAttribute("projectOptions", JSONArray.toJSONString(xm));
		
		String id = request.getParameter("id");
		request.setAttribute("zmid", id);
		return prefix +"addAccountIndex";
	}
	
	@RequestMapping("/accountDetList")
	public String accountDetList(HttpServletRequest request){
		String zmid = request.getParameter("id");
		request.setAttribute("zmid", zmid);
		
		ProAccount zm = baseService.get(ProAccount.class, zmid);
		request.setAttribute("zm", JSONArray.toJSONString(zm));
		return prefix +"accountDetList";
	}
	@RequestMapping("/addDetIndex")
	public String addDetIndex(HttpServletRequest request){
		String zmid = request.getParameter("zmid");
		String zmmxid = request.getParameter("zmmxid");
		
		List<User> ul = baseService.findMapBySql("select u.name name ,u.id id  from tbl_user u ", new Object[]{}, new Type[]{StringType.INSTANCE}, User.class);
		request.setAttribute("userOptions", JSONArray.toJSONString(ul));
		
		request.setAttribute("zmid", zmid);
		request.setAttribute("zmmxid", zmmxid);
		
		return prefix +"addDetIndex";
	}
	
	@RequestMapping("/saveAccount")
	@ResponseBody
	public Result saveAccount(HttpServletRequest request,ProAccount zm){
		String id = request.getParameter("id");
		Result r = new Result();
		if(id==null){//新增
			zm.setAllMoney("0");
			baseService.save(zm);
			r.setData(zm);
		}else{//修改
			ProAccount z = baseService.get(ProAccount.class, id);
			z.setAccountName(zm.getAccountName());
			baseService.update(z);
			r.setData(z);
		}
		
		r.setSuccess(true);
		return r;
		
	}
	@RequestMapping("/getAccount")
	@ResponseBody
	public Result getAccount(HttpServletRequest request){
		String id = request.getParameter("id");
		ProAccount zm = baseService.get(ProAccount.class, id);
		Result r = new Result();
		r.setData(zm);
		r.setSuccess(true);
		return r;
	}
	@RequestMapping("/saveAccountDet")
	@ResponseBody
	public Result saveAccountDet(HttpServletRequest request,ProAccountDet mx){
		Arith arith = new Arith();
		String mxid = request.getParameter("id");
		String money = request.getParameter("money");
		Result r = new Result();
		ProAccount zm = baseService.get(ProAccount.class, mx.getProjectAccountId());
		
		if(mxid==null){//新增
			if("1".equals(mx.getZmtype())){//收入
				mx.setInMoney(money);
				zm.setAllMoney(arith.add(Double.valueOf(money), Double.valueOf(zm.getAllMoney()))+"");
			}else if("2".equals(mx.getZmtype())){//支出
				mx.setOutMoney(money);
				zm.setAllMoney(arith.sub(Double.valueOf(zm.getAllMoney()), Double.valueOf(money))+"");
			}
			User sh = baseService.get(User.class, mx.getAuditId());
			User jz = baseService.get(User.class, mx.getAccounterId());
			mx.setAuditName(sh.getName());
			mx.setAccounter(jz.getName());
			mx.setStatus("10");
			mx.setCreateId(SecurityUtil.getUserId());
			baseService.save(mx);
			r.setData(mx);
		}else{//编辑
			ProAccountDet m = baseService.get(ProAccountDet.class, mxid);
			//先修改账本总额
			if("1".equals(m.getZmtype())){
				
				zm.setAllMoney(arith.sub(Double.valueOf(zm.getAllMoney()), Double.valueOf(m.getInMoney()))+"");
				m.setInMoney("");
			}else if("2".equals(m.getZmtype())){
				zm.setAllMoney(arith.add(Double.valueOf(m.getOutMoney()), Double.valueOf(zm.getAllMoney()))+"");
				m.setOutMoney("");
			}
			
			if("1".equals(mx.getZmtype())){//收入
				m.setInMoney(money);
				zm.setAllMoney(arith.add(Double.valueOf(money), Double.valueOf(zm.getAllMoney()))+"");
			}else if("2".equals(mx.getZmtype())){//支出
				m.setOutMoney(money);
				zm.setAllMoney(arith.sub(Double.valueOf(zm.getAllMoney()), Double.valueOf(money))+"");
			}
			
			User sh = baseService.get(User.class, mx.getAuditId());
			User jz = baseService.get(User.class, mx.getAccounterId());
			m.setAuditName(sh.getName());
			m.setAccounter(jz.getName());
			m.setZmtype(mx.getZmtype());
			m.setCreateDate(mx.getCreateDate());
			m.setAbstracts(mx.getAbstracts());
			m.setRemark(mx.getRemark());
			
			baseService.update(m);
			r.setData(m);
		}
		r.setSuccess(true);
		return r;
	}
	
	@RequestMapping("/getAccountDet")
	@ResponseBody
	public Result getAccountDet(HttpServletRequest request){
		String mxid = request.getParameter("id");
		ProAccountDet mx  = baseService.get(ProAccountDet.class, mxid);
		Result r = new Result();
		r.setData(mx);
		r.setSuccess(true);
		return r;
	}
	@RequestMapping("/delAccountDet")
	@ResponseBody
	public Result delAccountDet(HttpServletRequest request){
		Arith arith = new Arith();
		String id =request.getParameter("id");
		ProAccountDet m = baseService.get(ProAccountDet.class, id);
		ProAccount zm = baseService.get(ProAccount.class, m.getProjectAccountId());
		if("1".equals(m.getZmtype())){
			
			zm.setAllMoney(arith.sub(Double.valueOf(zm.getAllMoney()), Double.valueOf(m.getInMoney()))+"");
			
		}else if("2".equals(m.getZmtype())){
			zm.setAllMoney(arith.add(Double.valueOf(m.getOutMoney()), Double.valueOf(zm.getAllMoney()))+"");
			
		}
		
		baseService.delete(m);
		baseService.update(zm);
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
}
