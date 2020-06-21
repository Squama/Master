package com.radish.master.controller.prints;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.project.ProjectTeam;
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
		List<ProjectTeam> htbzs = budgetService.find(" from ProjectTeam where 1=1 ");
        request.setAttribute("htbzs", JSONArray.toJSONString(htbzs));
		return prefix+"volume_list";
	}
	
	@RequestMapping("/salaryBz")
	public String salaryBz(HttpServletRequest request){
		request.setAttribute("uid", SecurityUtil.getUserId());
		request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
		List<ProjectTeam> htbzs = budgetService.find(" from ProjectTeam where 1=1 ");
		request.setAttribute("bz", JSONArray.toJSONString(htbzs));
		return prefix+"salaryBz_list";
	}
}
