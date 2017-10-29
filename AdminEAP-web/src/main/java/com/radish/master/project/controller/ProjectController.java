/**
 * Copyright © 2017 周庆博和他的朋友们有限公司
 */
package com.radish.master.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author dongy
 *
 */
@Controller
@RequestMapping("/project")
public class ProjectController {

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "projectmanage/project/project_list";
    }
	
}
