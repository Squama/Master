package com.radish.master.controller.prints;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.service.BudgetService;
import com.radish.master.service.CommonService;

@Controller
@RequestMapping("/prints")
public class PrintsController {
	String prefix = "/Prints/";
	 @Resource
	    private BudgetService budgetService;
	 @Resource
		private CommonService commonService;
	
	@RequestMapping("/projectVolume")
	public String projectVolume(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return prefix+"volume_list";
	}
	
	@RequestMapping("/salaryBz")
	public String salaryBz(HttpServletRequest request){
		request.setAttribute("uid", SecurityUtil.getUserId());
		request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
		return prefix+"salaryBz_list";
	}
}
