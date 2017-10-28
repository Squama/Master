package com.radish.master.material.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cnpc.framework.base.service.BaseService;

@Controller
@RequestMapping("/material")
public class MaterialController {
	
	private String prefix="/materialSpace/material/";
	
	@Autowired
	private BaseService baseService;
	
	@RequestMapping(value="/index",method = RequestMethod.GET)
	public String index(){
		return prefix+"materiel_list"	;
	}
	
	
}
