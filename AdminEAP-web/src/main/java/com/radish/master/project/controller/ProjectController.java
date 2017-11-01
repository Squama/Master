/**
 * Copyright © 2017 周庆博和他的朋友们有限公司
 */
package com.radish.master.project.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Project;

/**
 * @author dongy
 *
 */
@Controller
@RequestMapping("/project")
public class ProjectController {
    
    @Resource
    private BaseService baseService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "projectmanage/project/project_list";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(){
        return "projectmanage/project/project_add";
    }
	
    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(Project project, HttpServletRequest request){
        project.setStatus("10");
        
        baseService.save(project);
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", project.getId());
        return new Result(true, map);
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/uploadfile",method = RequestMethod.POST)
    public String step2(Project project, HttpServletRequest request){
        request.setAttribute("id", project.getId());
        return "projectmanage/project/project_add_step2";
    }
}
