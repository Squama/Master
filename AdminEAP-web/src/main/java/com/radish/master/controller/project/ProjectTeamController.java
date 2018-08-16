/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.project;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.project.ProjectTeam;
import com.radish.master.entity.project.UserTeam;
import com.radish.master.service.BudgetService;
import com.radish.master.service.ProjectService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月19日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/project/team")
public class ProjectTeamController {

    @Resource
    private ProjectService projectService;
    
    @Resource
    private BudgetService budgetService;
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/team/list";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("leaderOptions", JSONArray.toJSONString(projectService.getUserCombobox()));
        return "projectmanage/team/add";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/edit/{id}",method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("leaderOptions", JSONArray.toJSONString(projectService.getTeamMemberNonManagerComboboxByTeam(id)));
        return "projectmanage/team/edit";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    public ProjectTeam getMechInfo(String id){
        return projectService.get(ProjectTeam.class, id);
    }
    
    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(ProjectTeam projectTeam, HttpServletRequest request){
        
        
        if(StrUtil.isEmpty(projectTeam.getId())){
            projectTeam.setStatus("10");
            projectTeam.setUpdateDateTime(new Date());
            projectService.save(projectTeam);
            User user = projectService.get(User.class, projectTeam.getTeamLeaderID());
            
            UserTeam userTeam = new UserTeam();
            userTeam.setUserID(user.getId());
            userTeam.setUserName(user.getName());
            userTeam.setTeamID(projectTeam.getId());
            userTeam.setTeamCode(projectTeam.getTeamCode());
            userTeam.setTeamName(projectTeam.getTeamName());
            userTeam.setProjectID(projectTeam.getProjectID());
            userTeam.setProjectName(projectTeam.getProjectName());
            
            projectService.save(userTeam);
        }else{
            ProjectTeam oldPT = projectService.get(ProjectTeam.class, projectTeam.getId());
            oldPT.setProjectID(projectTeam.getProjectID());
            oldPT.setProjectName(projectTeam.getProjectName());
            oldPT.setUpdateDateTime(new Date());
            oldPT.setRegistion(projectTeam.getRegistion());
            oldPT.setConstruction(projectTeam.getConstruction());
            oldPT.setTeamLeader(projectTeam.getTeamLeader());
            oldPT.setTeamLeaderID(projectTeam.getTeamLeaderID());
            
            projectService.update(oldPT);
        }
        
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", projectTeam.getId());
        return new Result(true, map);
    }
    
    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        
        ProjectTeam pt = projectService.get(ProjectTeam.class, id);
        try {
            projectService.delete(pt);
        } catch (Exception e) {
            return new Result(false);
        }
        return new Result();
    }
}
