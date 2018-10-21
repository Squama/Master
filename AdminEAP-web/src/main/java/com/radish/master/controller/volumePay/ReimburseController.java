package com.radish.master.controller.volumePay;

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
import com.radish.master.entity.ProAccount;
import com.radish.master.entity.ProAccountDet;
import com.radish.master.entity.review.MaxNumber;
import com.radish.master.entity.volumePay.Loans;
import com.radish.master.entity.volumePay.Reimburse;
import com.radish.master.entity.volumePay.ReimburseDet;
import com.radish.master.service.BudgetService;
import com.radish.master.system.Arith;

@Controller
@RequestMapping("/reimburse")
public class ReimburseController {
	String prefix = "/VolumePay/Reimburse/";
	
	@Autowired
	private BaseService baseService;
	@Resource
    private BudgetService budgetService;
	@Resource
	private RuntimePageService runtimePageService;
	
	@RequestMapping("/addindex")
	public String addindex(HttpServletRequest request){
		request.setAttribute("jkr", SecurityUtil.getUserId());
		return prefix+"index";
	}
	@RequestMapping("/bmshindex")
	public String bmshindex(HttpServletRequest request){
		User u = SecurityUtil.getUser();
		String deptid = u.getDeptId();
		request.setAttribute("deptid", deptid);
		return prefix+"bmshindex";
	}
	//财务付款页
	@RequestMapping("/cwfkindex")
	public String cwfkindex(HttpServletRequest request){
		return prefix+"cwfkindex";
	}
	@RequestMapping("/cwlook")
	public String cwlook(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		String  id = request.getParameter("id");
		String lx = request.getParameter("lx");
		request.setAttribute("id", id);
		request.setAttribute("lx", lx);
		return prefix+"cwlook";
	}
	@RequestMapping("/auidtCw/{id}")//审核查看页
	public String auidtMx(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		request.setAttribute("id", id);
		return prefix+"auidLookCw";
		
	}
	@RequestMapping("/auidtCwfzr/{id}")//审核查看页
	public String auidtCwfzr(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		request.setAttribute("id", id);
		return prefix+"auidtLookCwfzr";
		
	}
	@RequestMapping("/auidtBoss/{id}")//审核查看页
	public String auidtBoss(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		request.setAttribute("id", id);
		return prefix+"auidtLookBoss";
		
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
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		if(id==null){
			String str =maxNum();
			
			Calendar date = Calendar.getInstance();
			String year = String.valueOf(date.get(Calendar.YEAR));
			String strs = year+str;
			
			User u = SecurityUtil.getUser();
			request.setAttribute("bh",strs);
			request.setAttribute("ryid", u.getId());
			request.setAttribute("xm", u.getName());
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
		}
		
		return prefix+"add";
	}
	@RequestMapping("/bmlook")
	public String bmlook(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		String  id = request.getParameter("id");
		request.setAttribute("id", id);
		return prefix+"bmlook";
	}
	
	@RequestMapping("/addBxmx")
	public String addBxmx(HttpServletRequest request){
		String bxid = request.getParameter("bxid");
		request.setAttribute("bxid", bxid);
		return prefix +"addMX";
	}
	
	
	@RequestMapping("/saveBx")
	@ResponseBody
	public Result saveBx(HttpServletRequest request,Reimburse bx){
		Result r = new Result();
		String id  =request.getParameter("id");
		if(id==null){//新增
			bx.setBxdate(new Date());
			String userid = bx.getReerid();
			User u = baseService.get(User.class, userid);
			Org bm = baseService.get(Org.class, u.getDeptId());
			bx.setDept(bm.getName());
			bx.setPid(u.getDeptId());
			bx.setStatus("10");
			bx.setIsjz("0");
			bx.setMoney("0");//初始金额为0
			if("10".equals(bx.getType())){
				bx.setProid(null);
			}
			baseService.save(bx);
			id = bx.getId();
		}else{
			Reimburse b = baseService.get(Reimburse.class, id);
			b.setContent(bx.getContent());
			b.setType(bx.getType());
			if("10".equals(bx.getType())){
				b.setProid(null);
			}else if("20".equals(bx.getType())){
				b.setProid(bx.getProid());
			}
			baseService.update(b);
		}
		r.setCode(id);
		return r;
	}
	
	@RequestMapping("/load")
	@ResponseBody
	public Result load(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Reimburse bx = baseService.get(Reimburse.class, id);
		r.setData(bx);
		r.setCode(id);
		return r;
	}
	@RequestMapping("/deleteBx")
	@ResponseBody
	public Result deleteBx(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Reimburse bx = baseService.get(Reimburse.class, id);
		List<ReimburseDet> bxmxs = baseService.find(" from ReimburseDet where reimburseId='"+id+"'");
		for(ReimburseDet mx : bxmxs){
			baseService.delete(mx);
		}
		baseService.delete(bx);
		return r;
	}
	
	@RequestMapping("/saveBxMx")
	@ResponseBody
	public Result saveBxMx(HttpServletRequest request,ReimburseDet mx){
		
		Result r = new Result();
		String bxid = request.getParameter("bxid");
		mx.setReimburseId(bxid);
		baseService.save(mx);
		docountJe(bxid);//计算报销金额
		return r;
	}
	@RequestMapping("/deleteBxMx")
	@ResponseBody
	public Result saveBxMx(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		ReimburseDet mx = baseService.get(ReimburseDet.class, id);
		String bxid = mx.getReimburseId();
		baseService.delete(mx);
		docountJe(bxid);//计算报销金额
		return r;
	}
	
	public void docountJe(String bxid){
		Arith ari = new Arith();
		Reimburse bx = baseService.get(Reimburse.class, bxid);
		List<ReimburseDet> bxmxs = baseService.find(" from ReimburseDet where reimburseId='"+bxid+"'");
		Double je = 0.0;
		for(ReimburseDet mx : bxmxs){
			je = ari.add(Double.valueOf(mx.getFpmoney()), je);
		}
		bx.setMoney(je+"");
		baseService.update(bx);
	}
	
	//提交部门审核
	@RequestMapping("/bmsh")
	@ResponseBody
	public Result bmsh(HttpServletRequest request){
		Result r = new Result();
		String id = request.getParameter("id");
		Reimburse bx = baseService.get(Reimburse.class, id);
		bx.setStatus("20");
		baseService.update(bx);
		return r;
	}
	
	//部门审核
		@RequestMapping("/bmsubmit")
		@ResponseBody
		public Result bmsubmit(HttpServletRequest request,Reimburse jk){
			String id = request.getParameter("id");
			String lx = request.getParameter("lx");
			Reimburse bx = baseService.get(Reimburse.class, id);
			
			User user = SecurityUtil.getUser();
			Org bm = baseService.get(Org.class, bx.getPid());
			if("10".equals(lx)){//通过
				bx.setStatus("40");
				String name ="部门："+bm.getName()+",姓名："+jk.getReername()+",报销【审核】";

		        // businessKey
		        String businessKey = bx.getId();

		        // 配置流程变量
		        Map<String, Object> variables = new HashMap<>();
		        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
		        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
		        variables.put("taskName", name);

		        // 启动流程
		        runtimePageService.startProcessInstanceByKey("Reimburse", name, variables, user.getId(), businessKey);
				
				
			}else if("20".equals(lx)){//驳回
				bx.setStatus("30");
			}
			bx.setBmyj(jk.getBmyj());
			bx.setBmshr(user.getName());
			baseService.update(bx);
			Result r = new Result();
			return r;
		}
	
		//确认付款
		@RequestMapping("/dofk")
		@ResponseBody
		public Result dofk(HttpServletRequest request){
			String id = request.getParameter("id");
			Reimburse bx = baseService.get(Reimburse.class, id);
			bx.setStatus("90");
			baseService.update(bx);
			Result r = new Result();
			return r;
			
		}
		
		
		//记账
				@RequestMapping("/dojz")
				@ResponseBody
				public Result dojz(HttpServletRequest request){
					Arith arith = new Arith();
					String id = request.getParameter("id");
					Reimburse bx = baseService.get(Reimburse.class, id);
					
					String xmid = "";
					String content = bx.getDept()+"-"+bx.getReername()+"报销";
					if("10".equals(bx.getType())){//公司账本
						xmid = "1";
					}else if("20".equals(bx.getType())){//项目账本
						xmid = bx.getProid();
					}
					
					List<ProAccount> xmz = baseService.find(" from ProAccount where projectId='"+xmid+"'");
					
					User u = SecurityUtil.getUser();
					//账目明细
					ProAccountDet mx = new ProAccountDet();
					mx.setCreateDate(new Date());
					mx.setAbstracts(content);
					mx.setZmtype("2");
					mx.setOutMoney(bx.getMoney());
					mx.setAccounter(u.getName());
					mx.setAccounterId(u.getId());
					//mx.setAuditName(u.getName());
					//mx.setAuditId(u.getId());
					mx.setStatus("10");
					if(xmz.size()<=0){//无账本，先生成账本
						ProAccount zb = new ProAccount();
						zb.setProjectId(xmid);
						if("1".equals(xmid)){
							zb.setAccountName("公司账本");
						}
						
						baseService.save(zb);
						zb.setAllMoney(arith.sub(0.0,Double.valueOf(bx.getMoney()))+"");
						mx.setProjectAccountId(zb.getId());
						baseService.save(mx);
						baseService.update(zb);
					}else{
						ProAccount zb = xmz.get(0);
						zb.setAllMoney(arith.sub(Double.valueOf(zb.getAllMoney()),Double.valueOf(bx.getMoney()))+"");
						mx.setProjectAccountId(zb.getId());
						baseService.save(mx);
						baseService.update(zb);
					}
					bx.setIsjz("10");
					baseService.update(bx);
					Result r = new Result();
					return r;
				}
	
}
