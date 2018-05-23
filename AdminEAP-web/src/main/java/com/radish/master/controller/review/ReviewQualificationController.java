package com.radish.master.controller.review;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Project;
import com.radish.master.entity.common.ActivitiReview;
import com.radish.master.entity.review.MaxNumber;
import com.radish.master.entity.review.ReviewBid;
import com.radish.master.entity.review.ReviewQualification;

@Controller
@RequestMapping("/qualireview")
public class ReviewQualificationController {
	String prefix = "/Reviews/qualireview/";
	
	@Autowired
	private BaseService baseService;
	@Resource
	 private RuntimePageService runtimePageService;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request){
		List<Project> p = baseService.findMapBySql("select p.project_name projectName ,p.id id  from tbl_project p ", new Object[]{}, new Type[]{StringType.INSTANCE}, Project.class);
		request.setAttribute("projectOptions", JSONArray.toJSONString(p));
		return prefix +"index";
	}
	
	@RequestMapping("/add")
	public String addIndex(HttpServletRequest request){
		List<Project> p = baseService.findMapBySql("select p.project_name projectName ,p.id id  from tbl_project p ", new Object[]{}, new Type[]{StringType.INSTANCE}, Project.class);
		request.setAttribute("projectOptions", JSONArray.toJSONString(p));
		String spid = request.getParameter("spid");
		request.setAttribute("spid",spid);
		
		//新增生成编号
		if(StringHelper.isEmpty(spid)){
			String str =maxNum();
			
			Calendar date = Calendar.getInstance();
			String year = String.valueOf(date.get(Calendar.YEAR));
			String strs = "ZBZG"+year+str;
			
			request.setAttribute("bh",strs);
		}
		return prefix +"addIndex";
	}
	@RequestMapping("/auidLook/{id}")
	public String auidLook(@PathVariable("id") String id,HttpServletRequest request){
		List<Project> p = baseService.findMapBySql("select p.project_name projectName ,p.id id  from tbl_project p ", new Object[]{}, new Type[]{StringType.INSTANCE}, Project.class);
		request.setAttribute("projectOptions", JSONArray.toJSONString(p));
		request.setAttribute("spid",id);
		return prefix +"auidIndex";
	}
	
	@RequestMapping("/look")
	public String look(HttpServletRequest request){
		List<Project> p = baseService.findMapBySql("select p.project_name projectName ,p.id id  from tbl_project p ", new Object[]{}, new Type[]{StringType.INSTANCE}, Project.class);
		request.setAttribute("projectOptions", JSONArray.toJSONString(p));
		String id = request.getParameter("spid");
		request.setAttribute("spid",id);
		return prefix +"lookInfo";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public Result save(HttpServletRequest request,ReviewQualification ps){
		String id = request.getParameter("id");
		Result r = new Result();
		if(StringHelper.isNotEmpty(id)){//修改
			ReviewQualification p = baseService.get(ReviewQualification.class,id);
			if(!p.getProjectId().equals(ps.getProjectId())){
				Project xm = baseService.get(Project.class, ps.getProjectId());
				p.setProjectName(xm.getProjectName());
				p.setProjectId(ps.getProjectId());
			}
			p.setBider(ps.getBider());
			p.setBuildSize(ps.getBuildSize());
			p.setBuilder(ps.getBuilder());
			p.setBuildAddress(ps.getBuildAddress());
			p.setRequires(ps.getRequires());
			baseService.update(p);
			r.setSuccess(true);
		}else{//保存
			String pid = ps.getProjectId();
			Project p = baseService.get(Project.class, pid);
			ps.setProjectName(p.getProjectName());
			ps.setStatus("10");
			ps.setCreateDate(new Date());
			baseService.save(ps);
			r.setSuccess(true);
		}
		return r;
	}
	
	@RequestMapping("/load")
	@ResponseBody
	public Result load(HttpServletRequest request){
		String id = request.getParameter("id");
		ReviewQualification sp = baseService.get(ReviewQualification.class,id);
		Result r = new Result();
		
		r.setSuccess(true);
		r.setData(sp);
		return r;
	}
	@RequestMapping("/loadSpyj")
	@ResponseBody
	public Result loadSpyj(HttpServletRequest request){
		String id = request.getParameter("id");
		
		List<ActivitiReview> sps = baseService.find(" from ActivitiReview where businessKey='"+id+"'");
		Result r = new Result();
		
		r.setSuccess(true);
		r.setData(sps);
		return r;
	}
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(HttpServletRequest request){
		String id = request.getParameter("spid");
		ReviewQualification sp = baseService.get(ReviewQualification.class,id);
		baseService.delete(sp);
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	
	public  String maxNum(){ 
		List<String> result = baseService.find("select max(mat.id) from com.radish.master.entity.review.MaxNumber mat");
		if(result.size()>0&&!"".equals(result.get(0))&&StringHelper.isNotEmpty(result.get(0))){
			String num = result.get(0);
			
			MaxNumber m = new MaxNumber();
			Integer newId = Integer.valueOf(num)+1;
			m.setId(newId+"");
			baseService.save(m);
			return num;
		}else{
			MaxNumber m = new MaxNumber();
			m.setId("1001");
			baseService.save(m);
			return "1001";
		}
	}
	@RequestMapping("/start")
	@ResponseBody
	public Result start(String id) {
		ReviewQualification sp = baseService.get(ReviewQualification.class,id);
		sp.setStatus("20");
		baseService.update(sp);
		
		User user = SecurityUtil.getUser();
        String name =sp.getProjectName()+"|招投标资格【评审】";

        // businessKey
        String businessKey = sp.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey("qualiReview", name, variables, user.getId(), businessKey);
    }
}
