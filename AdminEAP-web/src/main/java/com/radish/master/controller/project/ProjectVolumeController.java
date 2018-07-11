/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.cnpc.framework.activiti.pojo.ActivityVo;
import com.cnpc.framework.activiti.pojo.TaskVo;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.activiti.service.TaskPageService;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.PageInfo;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.query.entity.QueryCondition;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.project.LaborSub;
import com.radish.master.service.BudgetService;
import com.radish.master.service.ProjectService;
import com.radish.master.system.SpringUtil;

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
    public String list(HttpServletRequest request){
    	request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
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
    
    @RefreshCSRFToken
    @RequestMapping(value="/detailbusiness/{id}",method = RequestMethod.GET)
    public String detailBusiness(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/volume/volume_detail_business";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/detailfinal/{id}",method = RequestMethod.GET)
    public String detailFinal(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/volume/volume_detail_final";
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
    
    @RequestMapping(value="/checkconstworker",method = RequestMethod.POST)
    @ResponseBody
    public Result checkConstWorker(String userID, String userName){
        User currentUser = SecurityUtil.getUser();
        
        if(!userID.equals(currentUser.getId())){
            return new Result(false,"本分项只可由施工员【"+userName+"】进行上报！");
        }else{
            return new Result(true);
        }
    }
    
    @RequestMapping(value="/getlabor")
    @ResponseBody
    public Result getLabor(String projectID){
        return new Result(true, JSONArray.toJSONString(projectService.getLaborComboboxByProject(projectID)));
    }
    
    @RequestMapping(value="/getlaborsub")
    @ResponseBody
    public Result getLaborSub(String laborID){
        return new Result(true, JSONArray.toJSONString(projectService.getLaborSubComboboxByLabor(laborID)));
    }
    
    @RequestMapping(value="/getprojectsub")
    @ResponseBody
    public Result getProjectSub(String projectID){
        return new Result(true, JSONArray.toJSONString(projectService.getProjectSubCombobox(projectID)));
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
    
    @RequestMapping(value="/getlaborsubinfo")
    @ResponseBody
    public LaborSub getLaborSubInfo(String laborSubID){
        return projectService.get(LaborSub.class, laborSubID);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(ProjectVolume projectVolume, HttpServletRequest request){
        SimpleDateFormat myFormat=new SimpleDateFormat("yyyy-MM-dd");
        if(projectVolume.getStartTime().compareTo(projectVolume.getEndTime()) != -1){
            return new Result(false, "开始时间必须小于结束时间");
        }
        
        String startTime = myFormat.format(projectVolume.getStartTime()) + " 00:00:00";
        String endTime = myFormat.format(projectVolume.getEndTime()) + " 23:59:59";
        
        /*Calendar calendar = Calendar.getInstance();
        calendar.clear();
        
        calendar.set(projectVolume.getStartTime().getYear()+1900, projectVolume.getStartTime().getMonth()+1, projectVolume.getStartTime().getDay(), 0, 0, 0);
        String startTime = String.valueOf(calendar.getTimeInMillis());
        
        calendar.clear();
        calendar.set(projectVolume.getEndTime().getYear()+1900, projectVolume.getEndTime().getMonth()+1, projectVolume.getEndTime().getDay(), 23, 59, 59);
        String endTime = String.valueOf(calendar.getTimeInMillis());*/
        
        
        List<ProjectVolume> list = projectService.checkTimePeriod(projectVolume.getProjectID(), projectVolume.getLaborID(), projectVolume.getProjectSubID(), startTime, endTime, projectVolume.getId());
        if(!list.isEmpty()){
            return new Result(false, "上报时间段不可重叠");
        }
        
        if(StrUtil.isEmpty(projectVolume.getId())){
            projectVolume.setCreateDateTime(new Date());
            projectVolume.setCreateTime(new Date());
            projectVolume.setStatus("10");
            projectVolume.setBusinessMech(projectVolume.getApplyMech());
            projectVolume.setBusinessLabour(projectVolume.getApplyLabour());
            projectVolume.setBusinessMat(projectVolume.getApplyMat());
            projectVolume.setBusinessDebit(projectVolume.getApplyDebit());
            projectVolume.setBusinessPack(projectVolume.getApplyPack());
            projectVolume.setBusinessSub(projectVolume.getApplySub());
            projectVolume.setFinalMech(projectVolume.getApplyMech());
            projectVolume.setFinalLabour(projectVolume.getApplyLabour());
            projectVolume.setFinalMat(projectVolume.getApplyMat());
            projectVolume.setFinalDebit(projectVolume.getApplyDebit());
            projectVolume.setFinalPack(projectVolume.getApplyPack());
            projectVolume.setFinalSub(projectVolume.getApplySub());
            projectService.save(projectVolume);
        }else{
            ProjectVolume oldProjectVolume = projectService.get(ProjectVolume.class, projectVolume.getId());
            /*oldProjectVolume.setProjectID(projectVolume.getProjectID());
            oldProjectVolume.setProjectName(projectVolume.getProjectName());
            oldProjectVolume.setStartTime(projectVolume.getStartTime());
            oldProjectVolume.setEndTime(projectVolume.getEndTime());
            oldProjectVolume.setLaborID(projectVolume.getLaborID());
            oldProjectVolume.setVolume(projectVolume.getVolume());
            oldProjectVolume.setApplyMech(projectVolume.getApplyMech());
            oldProjectVolume.setApplyLabour(projectVolume.getApplyLabour());
            oldProjectVolume.setApplyMat(projectVolume.getApplyMat());
            oldProjectVolume.setApplyDebit(projectVolume.getApplyDebit());
            oldProjectVolume.setApplyPack(projectVolume.getApplyPack());
            oldProjectVolume.setApplySub(projectVolume.getApplySub());
            oldProjectVolume.setBusinessMech(projectVolume.getApplyMech());
            oldProjectVolume.setBusinessLabour(projectVolume.getApplyLabour());
            oldProjectVolume.setBusinessMat(projectVolume.getApplyMat());
            oldProjectVolume.setBusinessDebit(projectVolume.getApplyDebit());
            oldProjectVolume.setBusinessPack(projectVolume.getApplyPack());
            oldProjectVolume.setBusinessSub(projectVolume.getApplySub());
            oldProjectVolume.setFinalMech(projectVolume.getApplyMech());
            oldProjectVolume.setFinalLabour(projectVolume.getApplyLabour());
            oldProjectVolume.setFinalMat(projectVolume.getApplyMat());
            oldProjectVolume.setFinalDebit(projectVolume.getApplyDebit());
            oldProjectVolume.setFinalPack(projectVolume.getApplyPack());
            oldProjectVolume.setFinalSub(projectVolume.getApplySub());*/
            
            SpringUtil.copyPropertiesIgnoreNull(projectVolume, oldProjectVolume);
            
            oldProjectVolume.setUpdateDateTime(new Date());
            oldProjectVolume.setCreateTime(new Date());
            
            projectService.update(oldProjectVolume);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", projectVolume.getId());
        return new Result(true, map);
        
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/savebusiness")
    @ResponseBody
    public Result saveBusiness(ProjectVolume projectVolume, HttpServletRequest request){
        
        ProjectVolume oldProjectVolume = projectService.get(ProjectVolume.class, projectVolume.getId());
        oldProjectVolume.setBusinessMech(projectVolume.getBusinessMech());
        oldProjectVolume.setBusinessLabour(projectVolume.getBusinessLabour());
        oldProjectVolume.setBusinessMat(projectVolume.getBusinessMat());
        oldProjectVolume.setBusinessDebit(projectVolume.getBusinessDebit());
        oldProjectVolume.setBusinessPack(projectVolume.getBusinessPack());
        oldProjectVolume.setBusinessSub(projectVolume.getBusinessSub());
        oldProjectVolume.setUpdateDateTime(new Date());
        
        try{
            projectService.update(oldProjectVolume); 
        }catch (Exception e) {
            return new Result(false);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", projectVolume.getId());
        return new Result(true, map);
        
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/savefinal")
    @ResponseBody
    public Result saveFinal(ProjectVolume projectVolume, HttpServletRequest request){
        
        ProjectVolume oldProjectVolume = projectService.get(ProjectVolume.class, projectVolume.getId());
        oldProjectVolume.setFinalMech(projectVolume.getFinalMech());
        oldProjectVolume.setFinalLabour(projectVolume.getFinalLabour());
        oldProjectVolume.setFinalMat(projectVolume.getFinalMat());
        oldProjectVolume.setFinalDebit(projectVolume.getFinalDebit());
        oldProjectVolume.setFinalPack(projectVolume.getFinalPack());
        oldProjectVolume.setFinalSub(projectVolume.getFinalSub());
        oldProjectVolume.setUpdateDateTime(new Date());
        
        try{
            projectService.update(oldProjectVolume); 
        }catch (Exception e) {
            return new Result(false);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", projectVolume.getId());
        return new Result(true, map);
        
    }
    
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
