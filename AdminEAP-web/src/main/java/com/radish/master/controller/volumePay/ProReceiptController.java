package com.radish.master.controller.volumePay;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.common.util.StringHelper;
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
import com.radish.master.entity.Project;
import com.radish.master.entity.review.MaxNumber;
import com.radish.master.entity.volumePay.ProReceipt;
import com.radish.master.service.BudgetService;
import com.radish.master.system.Arith;

//收据功能
@Controller
@RequestMapping("/proreceipt")
public class ProReceiptController {
	private String prefix="/VolumePay/receipt/";
	
	@Resource
    private BudgetService budgetService;
	
	@Autowired
	private BaseService baseService;
	
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		request.setAttribute("zdr", SecurityUtil.getUserId());
		return prefix+"list";
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
	@RequestMapping("/edit")
	public String edit(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		String id =request.getParameter("id");
		String type = request.getParameter("type");
		request.setAttribute("id", id);
		request.setAttribute("type", type);
		if(id==null){
			String str =maxNum();
			
			Calendar date = Calendar.getInstance();
			String year = String.valueOf(date.get(Calendar.YEAR));
			String strs = "SJ"+year+str;
			
			request.setAttribute("number",strs);
		}
		
		return prefix +"edit";
	}
	@RequestMapping("/load")
	@ResponseBody
	public Result load(HttpServletRequest request){
		String id = request.getParameter("id");
		ProReceipt jk = baseService.get(ProReceipt.class, id);
		Result r = new Result();
		r.setData(jk);
		return r;
	}
	@RequestMapping("/save")
	@ResponseBody
	public Result save(HttpServletRequest request,ProReceipt jk){
		String id = request.getParameter("id");
		User u = SecurityUtil.getUser();
		if(id==null){
			jk.setCreateId(u.getId());
			jk.setIsrz("10");
			baseService.save(jk);
		}else{
			ProReceipt j = baseService.get(ProReceipt.class, id);
			j.setBz(jk.getBz());
			j.setContent(jk.getContent());
			j.setCreateDate(jk.getCreateDate());
			j.setJkr(jk.getJkr());
			j.setMoney(jk.getMoney());
			j.setProId(jk.getProId());
			j.setType(jk.getType());
			baseService.update(j);
		}
		Result r = new Result();
		
		return r;
	}
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(HttpServletRequest request){
		String id = request.getParameter("id");
		ProReceipt jk = baseService.get(ProReceipt.class, id);
		baseService.delete(jk);
		Result r = new Result();
		
		return r;
	}
	@RequestMapping("/doJz")
	@ResponseBody
	public Result doJz(HttpServletRequest request){
		Arith arith = new Arith();
		User u = SecurityUtil.getUser();
		Result r = new Result();
		String id = request.getParameter("id");
		ProReceipt jk = baseService.get(ProReceipt.class, id);
		Project xm = baseService.get(Project.class,jk.getProId());
		List<ProAccount> xmz = baseService.find(" from ProAccount where projectId='"+jk.getProId()+"'");
		
		//账目明细
		ProAccountDet mx = new ProAccountDet();
		mx.setCreateDate(jk.getCreateDate());
		mx.setAbstracts(jk.getJkr()+"("+jk.getContent()+")");
		mx.setZmtype("1");
		mx.setInMoney(jk.getMoney());
		mx.setAccounter(u.getName());
		mx.setAccounterId(u.getId());
		mx.setAuditName(u.getName());
		mx.setAuditId(u.getId());
		mx.setStatus("10");
		if(xmz.size()<=0){//无账本，先生成账本
			ProAccount zb = new ProAccount();
			zb.setProjectId(jk.getProId());
			zb.setAccountName(xm.getProjectName());
			baseService.save(zb);
			zb.setAllMoney(arith.add(Double.valueOf(jk.getMoney()), 0.0)+"");
			mx.setProjectAccountId(zb.getId());
			baseService.save(mx);
			baseService.update(zb);
		}else{
			ProAccount zb = xmz.get(0);
			zb.setAllMoney(arith.add(Double.valueOf(jk.getMoney()), Double.valueOf(zb.getAllMoney()))+"");
			mx.setProjectAccountId(zb.getId());
			baseService.save(mx);
			baseService.update(zb);
		}
		jk.setIsrz("20");
		baseService.update(jk);
		return r;
	}
}
