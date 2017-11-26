package com.radish.master.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radish.master.service.DutyCheckService;

@Controller
@RequestMapping("/dutycheck")
public class DutyCheckController {
	@Autowired
	private DutyCheckService dcs;
	
	private String prefix="/safetyManage/dutycheck/";
	
	@RequestMapping("/index")
	public String index(){
		return prefix+"dutycheck_list";
	}
	@RequestMapping("/edit")
	public String edit(String id,HttpServletRequest request){
		request.setAttribute("id", id);
	    request.setAttribute("doWhat",request.getParameter("doWhat"));
		return prefix+"dutycheck_add";
	}
}
