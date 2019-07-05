package com.radish.master.controller.volumePay;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.ProAccount;
import com.radish.master.entity.ProAccountDet;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.project.ProjectTeam;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryVolume;
import com.radish.master.entity.volumePay.Loans;
import com.radish.master.entity.volumePay.VolumePay;
import com.radish.master.service.BudgetService;
import com.radish.master.service.CommonService;
import com.radish.master.system.Arith;

@Controller
@RequestMapping("/salarypay")
public class SalayrPayController {
	String prefix = "/VolumePay/salary/";
	 @Resource
	    private BudgetService budgetService;
	 @Resource
		private CommonService commonService;
	 
	 @Resource
	 private RuntimePageService runtimePageService;
	
	@Autowired
	private BaseService baseService;
	
	
	@RequestMapping("/payindex")
	public String payinde(HttpServletRequest request){
		request.setAttribute("uid", SecurityUtil.getUserId());
		request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
		List<ProjectTeam> htbzs = baseService.find(" from ProjectTeam where 1=1 ");
		request.setAttribute("bz", JSONArray.toJSONString(htbzs));
		return prefix+"salaryPay_list";
	}
	@RequestMapping("/auidtMx/{id}")
	public String auidtMx(HttpServletRequest request,@PathVariable("id") String id){
		request.setAttribute("id", id);
		Salary zf = baseService.get(Salary.class, id);
		if(null!=zf){
			if(zf.getType()!=null){
				request.setAttribute("type",zf.getType());
				if("20".equals(zf.getType())||"40".equals(zf.getType())){
					return prefix+"auidtMxGl";
				}else if("50".equals(zf.getType())){
					return prefix+"auidtMxMw";
				}
			}
		}
		return prefix+"auidtMx";
	}
	@RequestMapping("/chooseZffs")
	public String chooseZffs(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		request.setAttribute("zdr", SecurityUtil.getUserId());
		return prefix +"zffs";
	}
			
	@RequestMapping("/start")
	@ResponseBody
	public Result start(String id) {
		Salary zf = baseService.get(Salary.class, id);
		
		User user = SecurityUtil.getUser();

		zf.setStatus("80");

		baseService.update(zf);
		String zflx="";
		if("10".equals(zf.getType())){
			zflx = "专业作业班组班组";
		}else if("20".equals(zf.getType())){
			zflx = "管理人员";
		}else if("30".equals(zf.getType())){
			zflx = "点工班组";
		}else if("40".equals(zf.getType())){
			zflx = "机关人员";
		}

        String name = zflx + " 工资单支付【审核】";

        // businessKey
        String businessKey = zf.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey("salaryPay", name, variables, user.getId(), businessKey);
    }
	@RequestMapping("/dotrue")
	@ResponseBody
	public Result dotrue(String id) {
		Result r = new Result();
		Salary zf = baseService.get(Salary.class, id);
		zf.setStatus("100");
		baseService.update(zf);
		return r;
	}
	//记账
			@RequestMapping("/dojz")
			@ResponseBody
			public Result dojz(HttpServletRequest request){
				Arith arith = new Arith();
				String id = request.getParameter("id");
				Salary j = baseService.get(Salary.class, id);
				
				String xmid = "";
				String content = "";
				if("40".equals(j.getType())){//公司账本 机关人员工资
					xmid = "1";
					content="机关人员工资支付";
				}else{//项目账本 其他类型人员工资
					xmid = j.getProjectID();
					if("10".equals(j.getType())){
						content="专业作业班组工资支付";
					}else if("20".equals(j.getType())){
						content="管理人员工资支付";
					}else if("30".equals(j.getType())){
						content="点工班组工资支付";
					}else if("50".equals(j.getType())){
						content="门卫机修工资支付";
					}
				}
				
				List<ProAccount> xmz = baseService.find(" from ProAccount where projectId='"+xmid+"'");
				
				User u = SecurityUtil.getUser();
				//账目明细
				ProAccountDet mx = new ProAccountDet();
				mx.setCreateDate(new Date());
				mx.setAbstracts(content);
				mx.setZmtype("2");
				mx.setOutMoney(j.getApplySum());
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
					}else{
						zb.setAccountName(j.getProjectName());
					}
					
					baseService.save(zb);
					zb.setAllMoney(arith.sub(0.0,Double.valueOf(j.getApplySum()))+"");
					mx.setProjectAccountId(zb.getId());
					baseService.save(mx);
					baseService.update(zb);
				}else{
					ProAccount zb = xmz.get(0);
					zb.setAllMoney(arith.sub(Double.valueOf(zb.getAllMoney()),Double.valueOf(j.getApplySum()) )+"");
					mx.setProjectAccountId(zb.getId());
					baseService.save(mx);
					baseService.update(zb);
				}
				j.setIsjz("10");
				baseService.update(j);
				Result r = new Result();
				return r;
			}
}
