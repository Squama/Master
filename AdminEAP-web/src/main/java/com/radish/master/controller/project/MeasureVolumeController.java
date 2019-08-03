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
import com.radish.master.entity.MeasureVolume;
import com.radish.master.entity.ProjectVolume;
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
* dongyan      2018年11月17日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/measurevolume")
public class MeasureVolumeController {

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
        return "projectmanage/volume/measure/volume_list";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        
        return "projectmanage/volume/measure/volume_edit";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/edit/{id}",method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/volume/measure/volume_edit";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/volume/measure/volume_detail";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/detailbusiness/{id}",method = RequestMethod.GET)
    public String detailBusiness(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/volume/measure/volume_detail_business";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/detailfinal/{id}",method = RequestMethod.GET)
    public String detailFinal(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/volume/measure/volume_detail_final";
    }
    
    @RequestMapping(value="/detailouter/{id}",method = RequestMethod.GET)
    public String detailOuter(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/volume/measure/detail_outer";
    }
    
    @RequestMapping(value="/check",method = RequestMethod.POST)
    @ResponseBody
    public Result check(String businessKey){
        MeasureVolume pv = projectService.get(MeasureVolume.class, businessKey);
        
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
        return new Result(true);
       /* if(!userID.equals(currentUser.getId())){
            return new Result(false,"本分项只可由施工员【"+userName+"】进行上报！");
        }else{
            return new Result(true);
        }*/
    }
    
    @RequestMapping(value="/getprojectsub")
    @ResponseBody
    public Result getProjectSub(String projectID){
        return new Result(true, JSONArray.toJSONString(projectService.getProjectSubCombobox(projectID)));
    }
    
    @RequestMapping(value="/getpoint")
    @ResponseBody
    public Result getPoint(String projectID){
        return new Result(true, JSONArray.toJSONString(projectService.getPointTeamComboboxByProject(projectID)));
    }
    
    @RequestMapping(value="/getpack")
    @ResponseBody
    public Result getPack(String projectID, String teamID){
        return new Result(true, JSONArray.toJSONString(projectService.getPackCombobox(projectID, teamID)));
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
    
    @RequestMapping(method = RequestMethod.POST, value="/delete")
    @ResponseBody
    public Result delete(MeasureVolume projectVolume, HttpServletRequest request){
    	String id = request.getParameter("id");
    	MeasureVolume pv = projectService.get(MeasureVolume.class, id);
    	projectService.delete(pv);
    	return new Result();
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(MeasureVolume measureVolume, HttpServletRequest request){
        SimpleDateFormat myFormat=new SimpleDateFormat("yyyy-MM-dd");
        if(measureVolume.getStartTime().compareTo(measureVolume.getEndTime()) != -1){
            return new Result(false, "开始时间必须小于结束时间");
        }
        
        String startTime = myFormat.format(measureVolume.getStartTime()) + " 00:00:00";
        String endTime = myFormat.format(measureVolume.getEndTime()) + " 23:59:59";
        
        List<MeasureVolume> list = projectService.checkMeasureTimePeriod(measureVolume.getProjectID(), measureVolume.getMeasureType(), measureVolume.getProjectSubID(), startTime, endTime, measureVolume.getId());
        if(!list.isEmpty()){
            return new Result(false, "上报时间段不可重叠");
        }
        
        if(StrUtil.isEmpty(measureVolume.getId())){
            measureVolume.setCreateDateTime(new Date());
            measureVolume.setCreateTime(new Date());
            measureVolume.setStatus("10");
            measureVolume.setBusinessMech(measureVolume.getApplyMech());
            measureVolume.setBusinessLabour(measureVolume.getApplyLabour());
            measureVolume.setBusinessMat(measureVolume.getApplyMat());
            measureVolume.setBusinessDebit(measureVolume.getApplyDebit());
            measureVolume.setBusinessPack(measureVolume.getApplyPack());
            measureVolume.setBusinessSub(measureVolume.getApplySub());
            measureVolume.setFinalMech(measureVolume.getApplyMech());
            measureVolume.setFinalLabour(measureVolume.getApplyLabour());
            measureVolume.setFinalMat(measureVolume.getApplyMat());
            measureVolume.setFinalDebit(measureVolume.getApplyDebit());
            measureVolume.setFinalPack(measureVolume.getApplyPack());
            measureVolume.setFinalSub(measureVolume.getApplySub());
            projectService.save(measureVolume);
        }else{
            MeasureVolume oldMeasureVolume = projectService.get(MeasureVolume.class, measureVolume.getId());
            
            SpringUtil.copyPropertiesIgnoreNull(measureVolume, oldMeasureVolume);
            
            oldMeasureVolume.setUpdateDateTime(new Date());
            oldMeasureVolume.setCreateTime(new Date());
            
            projectService.update(oldMeasureVolume);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", measureVolume.getId());
        return new Result(true, map);
        
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/savebusiness")
    @ResponseBody
    public Result saveBusiness(MeasureVolume measureVolume, HttpServletRequest request){
        
        MeasureVolume oldMeasureVolume = projectService.get(MeasureVolume.class, measureVolume.getId());
        oldMeasureVolume.setBusinessMech(measureVolume.getBusinessMech());
        oldMeasureVolume.setBusinessLabour(measureVolume.getBusinessLabour());
        oldMeasureVolume.setBusinessMat(measureVolume.getBusinessMat());
        oldMeasureVolume.setBusinessDebit(measureVolume.getBusinessDebit());
        oldMeasureVolume.setBusinessPack(measureVolume.getBusinessPack());
        oldMeasureVolume.setBusinessSub(measureVolume.getBusinessSub());
        oldMeasureVolume.setUpdateDateTime(new Date());
        
        try{
            projectService.update(oldMeasureVolume); 
        }catch (Exception e) {
            return new Result(false);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", measureVolume.getId());
        return new Result(true, map);
        
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/savefinal")
    @ResponseBody
    public Result saveFinal(MeasureVolume measureVolume, HttpServletRequest request){
        
        MeasureVolume oldMeasureVolume = projectService.get(MeasureVolume.class, measureVolume.getId());
        oldMeasureVolume.setFinalMech(measureVolume.getFinalMech());
        oldMeasureVolume.setFinalLabour(measureVolume.getFinalLabour());
        oldMeasureVolume.setFinalMat(measureVolume.getFinalMat());
        oldMeasureVolume.setFinalDebit(measureVolume.getFinalDebit());
        oldMeasureVolume.setFinalPack(measureVolume.getFinalPack());
        oldMeasureVolume.setFinalSub(measureVolume.getFinalSub());
        oldMeasureVolume.setUpdateDateTime(new Date());
        
        try{
            projectService.update(oldMeasureVolume); 
        }catch (Exception e) {
            return new Result(false);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", measureVolume.getId());
        return new Result(true, map);
        
    }
    
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {
        return projectService.startMeasureVolumeFlow(projectService.get(MeasureVolume.class, id),"measureVolume");
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private MeasureVolume getProject(String id) {
        return projectService.get(MeasureVolume.class, id);
    }
    
    
}
