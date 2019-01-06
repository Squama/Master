package com.radish.master.controller.qualityCheck;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Project;
import com.radish.master.entity.qualityCheck.Gclxd;
import com.radish.master.entity.qualityCheck.Yclrc;
import com.radish.master.entity.review.MaxNumber;
import com.radish.master.service.BudgetService;

@Controller
@RequestMapping("/ymat")
public class YclrcController {
	
private String prefix="QualityChecks/yclrc/";
	
	@Autowired
	private BaseService baseService;
	@Resource
    private BudgetService budgetService;
	
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
	
	
	@RequestMapping("/add")
	public String add(HttpServletRequest request){
		request.setAttribute("xm", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		String id=request.getParameter("id");
		if(id==null){
			String str =maxNum();
			request.setAttribute("bh","MAT"+str);
		}
		request.setAttribute("id",id);
		return prefix +"addIndex";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/index")
	public String list(HttpServletRequest request) {
    	request.setAttribute("xm", JSONArray.toJSONString(budgetService.getProjectCombobox()));
    	
        return prefix+"index";
    }
	
	@RequestMapping("/save")
	@ResponseBody
	public Result save(HttpServletRequest request,Yclrc ps){
		String id = request.getParameter("id");
		Result r = new Result();
		User u = SecurityUtil.getUser();
		if(StringHelper.isNotEmpty(id)){//修改
			Yclrc p = baseService.get(Yclrc.class,id);
			if(!p.getProid().equals(ps.getProid())){
				Project xm = baseService.get(Project.class, ps.getProid());
				p.setProname(xm.getProjectName());
				p.setProid(ps.getProid());
			}
			p.setMatname(ps.getMatname());
			p.setJctime(ps.getJctime());
			p.setJcsl(ps.getJcsl());
			p.setGhdw(ps.getGhdw());
			p.setSybw(ps.getSybw());
			p.setDesc(ps.getDesc());
			baseService.update(p);
			r.setSuccess(true);
		}else{//保存
			String pid = ps.getProid();
			Project p = baseService.get(Project.class, pid);
			ps.setProname(p.getProjectName());
			ps.setCreate_name(u.getName());
			ps.setCreate_name_ID(u.getId());
			ps.setCreate_time(new Date());
			baseService.save(ps);
			r.setSuccess(true);
			id = ps.getId();
		}
		return r;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(HttpServletRequest request){
		String id = request.getParameter("id");
		Yclrc sp = baseService.get(Yclrc.class,id);
		baseService.delete(sp);
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	@RequestMapping("/load")
	@ResponseBody
	public Result load(HttpServletRequest request){
		String id = request.getParameter("id");
		Yclrc sp = baseService.get(Yclrc.class,id);
		Result r = new Result();
		r.setSuccess(true);
		r.setData(sp);
		return r;
	}
}
