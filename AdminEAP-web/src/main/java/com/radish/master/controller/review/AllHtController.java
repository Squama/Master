package com.radish.master.controller.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Labor;
import com.radish.master.entity.Project;
import com.radish.master.entity.review.ReviewLabor;
import com.radish.master.entity.review.ReviewMat;

@Controller
@RequestMapping("/allht")
public class AllHtController {
	String prefix = "/Reviews/allDelete/";
	
	@Autowired
	private BaseService baseService;
	@Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private HistoryService historyService;
    
    @RequestMapping("/index")
	public String index(HttpServletRequest request){
		List<Project> p = baseService.findMapBySql("select p.project_name projectName ,p.id id  from tbl_project p ", new Object[]{}, new Type[]{StringType.INSTANCE}, Project.class);
		request.setAttribute("projectOptions", JSONArray.toJSONString(p));
		
		return prefix +"index";
	}
    @RequestMapping("/deleteSg")
    @ResponseBody
    public Result deleteSg(HttpServletRequest request){
    	String id = request.getParameter("id");
		ReviewLabor sp = baseService.get(ReviewLabor.class,id);
		baseService.delete(sp);
		Result r = deleteLc(id);
    	return r;
    }
    @RequestMapping("/deleteCg")
    @ResponseBody
    public Result deleteCg(HttpServletRequest request){
    	String id = request.getParameter("id");
		ReviewMat sp = baseService.get(ReviewMat.class,id);
		baseService.delete(sp);
		Result r = deleteLc(id);
    	return r;
    }
    @RequestMapping("/deleteLw")
    @ResponseBody
    public Result deleteLw(HttpServletRequest request){
    	String id = request.getParameter("id");
    	Labor labor = baseService.get(Labor.class, id);
    	baseService.delete(labor);
    	Result r = deleteLc(id);
    	return r;
    }
    
    public Result deleteLc(String id ){
    	Result r = new Result();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(id).singleResult();
        if(processInstance!=null){
        	String instanceId = processInstance.getProcessInstanceId();
            try {
                runtimeService.deleteProcessInstance(instanceId, "流程实例管理界面删除");
                historyService.deleteHistoricProcessInstance(instanceId);
                return new Result(true);
            } catch (Exception ex) {
                return new Result(false, "删除实例失败", "失败原因:" + ex.getMessage());
            }
        }
        return new Result(true);
        
    }
}
