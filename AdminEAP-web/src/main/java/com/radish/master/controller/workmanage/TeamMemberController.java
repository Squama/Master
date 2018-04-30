/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.workmanage;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.radish.master.entity.project.ProjectTeam;
import com.radish.master.entity.project.UserTeam;
import com.radish.master.service.ProjectService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月25日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/project/team/member")
public class TeamMemberController {
    
    @Resource
    private ProjectService projectService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "workmanage/teammember/list";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/edit/{id}",method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id,HttpServletRequest request) {
        request.setAttribute("id", id);
        request.setAttribute("teamOptions", JSONArray.toJSONString(projectService.getMemberTeamComboboxByProject(id)));
        request.setAttribute("userOptions", JSONArray.toJSONString(projectService.getUserCombobox()));
        request.setAttribute("userTeamOptions", JSONArray.toJSONString(projectService.getUserTeamCombobox()));
        return "workmanage/teammember/edit";
    }
    
    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(String projectID, String teamID, String userID, HttpServletRequest request){
        ProjectTeam projectTeam = projectService.get(ProjectTeam.class, teamID);
        User user = projectService.get(User.class, userID);
        
        UserTeam userTeam = new UserTeam();
        userTeam.setUserID(user.getId());
        userTeam.setUserName(user.getName());
        userTeam.setTeamID(projectTeam.getId());
        userTeam.setTeamCode(projectTeam.getTeamCode());
        userTeam.setTeamName(projectTeam.getTeamName());
        userTeam.setProjectID(projectID);
        userTeam.setProjectName(projectTeam.getProjectName());
        
        projectService.save(userTeam);
        
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", userTeam.getId());
        return new Result(true, map);
    }
    
    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        
        UserTeam ut = projectService.get(UserTeam.class, id);
        try {
            projectService.delete(ut);
        } catch (Exception e) {
            return new Result(false);
        }
        return new Result();
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "workmanage/teammember/detail";
    }
    
}
