/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.ActivityVo;
import com.cnpc.framework.activiti.pojo.TaskVo;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.activiti.service.TaskPageService;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.PageInfo;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.query.entity.QueryCondition;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.service.BudgetService;
import com.radish.master.service.ProjectService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年3月5日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/projectvolume")
public class ProjectVolumeController {
    
    @Resource
    private ProjectService projectService;
    
    @Resource
    private BudgetService budgetService;
    
    @Resource
    private TaskPageService taskPageService;
    
    @Resource
    private RuntimePageService runtimePageService;
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "projectmanage/volume/volume_list";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        
        return "projectmanage/volume/volume_edit";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/edit/{id}",method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/volume/volume_edit";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/volume/volume_detail";
    }
    
    @RequestMapping(value="/check",method = RequestMethod.POST)
    @ResponseBody
    public Result check(String businessKey){
        ProjectVolume pv = projectService.get(ProjectVolume.class, businessKey);
        
        if("20".equals(pv.getStatus())){
            return new Result(false,"质量员、安全员和施工员尚未全部同意！！");
        }else{
            return new Result(true);
        }
    }
    
    @RequestMapping(value="/getlabor")
    @ResponseBody
    public Result getLabor(String projectID){
        return new Result(true, JSONArray.toJSONString(projectService.getLaborComboboxByProject(projectID)));
    }
    
    @RequestMapping(value="/getpingxing")
    @ResponseBody
    public Result getPX(String id){
    	
    	List<Map<String, Object>> conditions = new ArrayList<Map<String, Object>>();
    	Map<String, String> map = new HashMap<String, String>();
    	String zhiliang = "info";
    	String zhiliangsugg = "info";
    	String anquan = "info";
    	String anquansugg = "info";
    	String shigong = "info";
    	String shigongsugg = "info";
    	
    	Map<String, Object> condition = new HashMap<String, Object>();
    	condition.put("key", "businessKey");
    	condition.put("value", id);
    	
    	conditions.add(condition);
    	
    	QueryCondition q = new QueryCondition();
    	q.setConditions(conditions);
    	
    	List<TaskVo> taskVOList = taskPageService.getTaskToDoList(q, new PageInfo());
    	if(taskVOList.size() > 0){
        	List<Map<String, Object>> conditionsR = new ArrayList<Map<String, Object>>();
        	
        	Map<String, Object> conditionR = new HashMap<String, Object>();
        	conditionR.put("key", "processInstanceId");
        	conditionR.put("value", taskVOList.get(0).getProcessInstanceId());
        	
        	conditionsR.add(conditionR);
        	
        	QueryCondition qc = new QueryCondition();
        	qc.setConditions(conditionsR);
        	
    		List<ActivityVo> list = runtimePageService.getActivityList(qc, new PageInfo());
    		for(ActivityVo vo : list){
    			if(vo.getActivityState() == "0"){
    				if("zhiliang".equals(vo.getActivityId())){
        				zhiliang=vo.getApproved();
        				zhiliangsugg=vo.getSuggestion();
        			}else if("anquan".equals(vo.getActivityId())){
        				anquan=vo.getApproved();
        				anquansugg=vo.getSuggestion();
        			}else if("shigong".equals(vo.getActivityId())){
        				shigong=vo.getApproved();
        				shigongsugg=vo.getSuggestion();
        			}
    			}
    		}
    		
    	}
    	map.put("zhiliang", zhiliang);
    	map.put("zhiliangsugg", zhiliangsugg);
    	map.put("anquan", anquan);
    	map.put("anquansugg", anquansugg);
    	map.put("shigong", shigong);
    	map.put("shigongsugg", shigongsugg);
    	
        return new Result(true, map);
    }
    
    @RequestMapping(value="/getlaborinfo")
    @ResponseBody
    public Labor getLaborInfo(String laborID){
        return projectService.get(Labor.class, laborID);
    }
    
    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(ProjectVolume projectVolume, HttpServletRequest request){
        if(StrUtil.isEmpty(projectVolume.getId())){
            projectVolume.setCreateDateTime(new Date());
            projectVolume.setStatus("10");
            projectService.save(projectVolume);
        }else{
            ProjectVolume oldProjectVolume = projectService.get(ProjectVolume.class, projectVolume.getId());
            oldProjectVolume.setProjectID(projectVolume.getProjectID());
            oldProjectVolume.setProjectName(projectVolume.getProjectName());
            oldProjectVolume.setStartTime(projectVolume.getStartTime());
            oldProjectVolume.setEndTime(projectVolume.getEndTime());
            oldProjectVolume.setLaborID(projectVolume.getLaborID());
            oldProjectVolume.setEngineerMoney(projectVolume.getEngineerMoney());
            oldProjectVolume.setVolume(projectVolume.getVolume());
            oldProjectVolume.setUpdateDateTime(new Date());
            projectService.update(oldProjectVolume);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", projectVolume.getId());
        return new Result(true, map);
        
    }
    
    @VerifyCSRFToken
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {
        return projectService.startVolumeFlow(projectService.get(ProjectVolume.class, id),"projectVolume");
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private ProjectVolume getProject(String id) {
        return projectService.get(ProjectVolume.class, id);
    }
}
