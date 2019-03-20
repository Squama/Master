package com.radish.master.controller.volumePay;

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
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryVolume;
import com.radish.master.entity.volumePay.VolumePay;
import com.radish.master.service.BudgetService;
import com.radish.master.service.CommonService;

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
		return prefix+"salaryPay_list";
	}
	@RequestMapping("/auidtMx/{id}")
	public String auidtMx(HttpServletRequest request,@PathVariable("id") String id){
		request.setAttribute("id", id);
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
}
