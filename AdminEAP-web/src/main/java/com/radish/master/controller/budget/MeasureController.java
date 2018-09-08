/**
 * 
 */
package com.radish.master.controller.budget;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.project.Measure;
import com.radish.master.entity.project.MeasureConsume;
import com.radish.master.service.CommonService;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年8月8日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/budget/measure")
public class MeasureController {

    @Resource
    private CommonService commonService;
    
    @Resource
    private RuntimePageService runtimePageService;
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(HttpServletRequest request){
    	request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "budgetmanage/measure/list";
    }
    
    @RequestMapping(value="/consumelist",method = RequestMethod.GET)
    public String consumeList(HttpServletRequest request){
    	request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "budgetmanage/measure/consume_list";
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request){
        request.setAttribute("id", id);
        return "budgetmanage/measure/detail";
    }
    
    @RequestMapping(value="/consumedetail",method = RequestMethod.GET)
    public String consumeDetail(String projectID, String projectSubID, HttpServletRequest request){
    	String smeasureHql = "from Measure where projectID=:projectID AND projectSubID=:projectSubID";
        Map<String, Object> consumeParams = new HashMap<>();
        consumeParams.put("projectID", projectID);
        consumeParams.put("projectSubID", projectSubID);
        Measure m = commonService.get(smeasureHql, consumeParams);
        request.setAttribute("projectID", m.getProjectID());
        request.setAttribute("projectName", m.getProjectName());
        request.setAttribute("projectSubID", m.getProjectSubID());
        request.setAttribute("projectSubName", m.getProjectSubName());
        return "budgetmanage/measure/consume_detail";
    }
    
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        
        return "budgetmanage/measure/edit";
    }
    
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id, HttpServletRequest request){
        request.setAttribute("id", id);
        
        return "budgetmanage/measure/edit";
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(Measure measure, HttpServletRequest request){
        measure.setStatus("10");
        measure.setUpdateDateTime(new Date());
        
        try {
        	if (StrUtil.isEmpty(measure.getId())) {
        		commonService.save(measure);
        	}else{
        		Measure oldMeasure = commonService.get(Measure.class, measure.getId());
        		SpringUtil.copyPropertiesIgnoreNull(measure, oldMeasure);
        		commonService.update(oldMeasure);
        	}
            
        } catch (Exception e) {
            return new Result(false,"保存失败！");
        }
        return new Result(true, measure);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    public Measure getMechInfo(String id){
        return commonService.get(Measure.class, id);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getsum")
    @ResponseBody
    public Measure getMeasureSum(String projectID, String projectSubID){
    	String smeasureHql = "SELECT id,create_date_time,deleted,update_date_time,version, "
    			+ "project_id,project_name,project_sub_id,project_sub_name,'123' name,"
    			+ "SUM(safe) safe,SUM(civil) civil,SUM(envir) envir,SUM(temp) temp,SUM(night) night, "
    			+ "SUM(twice) twice,SUM(winter) winter,SUM(protect) protect,SUM(retest) retest ,status "
    			+ "from tbl_measure where project_id=:projectID AND project_sub_id=:projectSubID AND status = '30'";
        Map<String, Object> consumeParams = new HashMap<>();
        consumeParams.put("projectID", projectID);
        consumeParams.put("projectSubID", projectSubID);
        //Measure mc = commonService.getBySql(smeasureHql, consumeParams);
        List<Measure> mcList = commonService.findBySql(smeasureHql, consumeParams, Measure.class);
        return mcList.get(0);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getconsume")
    @ResponseBody
    public MeasureConsume getMeasureConsume(String projectID, String projectSubID){
    	String smeasureHql = "from MeasureConsume where projectID=:projectID AND projectSubID=:projectSubID";
        Map<String, Object> consumeParams = new HashMap<>();
        consumeParams.put("projectID", projectID);
        consumeParams.put("projectSubID", projectSubID);
        MeasureConsume mc = commonService.get(smeasureHql, consumeParams);
        return mc;
    }
    
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {
        Measure measure = commonService.get(Measure.class, id);
        measure.setStatus("20");
        measure.setUpdateDateTime(new Date());
        
        commonService.update(measure);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "申请总价措施项目费，项目：" + measure.getProjectName() +",子项：" + measure.getProjectSubName();
        
        //businessKey
        String businessKey = measure.getId();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey("measureBudget", name, variables,
                user.getId(), businessKey);
    }
    
}
