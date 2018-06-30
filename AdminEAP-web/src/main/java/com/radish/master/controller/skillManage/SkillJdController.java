package com.radish.master.controller.skillManage;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Project;
import com.radish.master.entity.review.MaxNumber;
import com.radish.master.entity.skillManage.SkillJd;
import com.radish.master.service.BudgetService;

@Controller
@RequestMapping("/jsjd")
public class SkillJdController {
	private String prefix ="SkillManage/Jsjd/";
	
	@Autowired
	private BaseService baseService;
	@Resource
    private BudgetService budgetService;
	
	@RequestMapping("/gsList")
	public String gsList(HttpServletRequest request){
		request.setAttribute("xm", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		String lx = request.getParameter("lx");
		request.setAttribute("lx",lx);
		return prefix +"gsList";
	}
	@RequestMapping("/xmList")
	public String xmList(HttpServletRequest request){
		request.setAttribute("xm", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		String lx = request.getParameter("lx");
		request.setAttribute("lx",lx);
		return prefix +"xmList";
	}
	@RequestMapping("/xmjdList")
	public String xmjdList(HttpServletRequest request){
		String lx = request.getParameter("lx");
		request.setAttribute("lx",lx);
		String proid = request.getParameter("proid");
		Project p = baseService.get(Project.class, proid);
		request.setAttribute("proid",proid);
		request.setAttribute("pName",p.getProjectName());
		return prefix +"xmjdList";
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
	@RequestMapping("/add")
	public String add(HttpServletRequest request){
		request.setAttribute("xm", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		String ck = request.getParameter("ck");
		String proid = request.getParameter("proid");
		String type = request.getParameter("type");
		String id=request.getParameter("id");
		if(id==null){
			String str =maxNum();
			
			if("10".equals(type)){
				String strs = "GS"+str;
				request.setAttribute("bh",strs);
			}else if("20".equals(type)){
				String strs = "XM"+str;
				request.setAttribute("bh",strs);
			}
			
		}
		
		request.setAttribute("proid",proid);
		request.setAttribute("ck",ck);
		request.setAttribute("type",type);
		request.setAttribute("id",id);
		
		return prefix +"addIndex";
	}
	@RequestMapping("/save")
	@ResponseBody
	public Result save(HttpServletRequest request,SkillJd jd){
		Result r = new Result();
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		User u = SecurityUtil.getUser();
		if(id==null){//保存
			jd.setCreate_time(new Date());
			jd.setCreate_name_ID(u.getId());
			jd.setCreate_name(u.getName());
			if("20".equals(type)){//项目的固定项目
				String proId = request.getParameter("proId");
				jd.setProid(proId);
			}
			Project p = baseService.get(Project.class, jd.getProid());
			jd.setProname(p.getProjectName());
			baseService.save(jd);
			r.setCode(jd.getId());
		}else{
			SkillJd j = baseService.get(SkillJd.class, id);
			if("10".equals(type)){//公司级允许修改项目
				j.setProid(jd.getProid());
				Project p = baseService.get(Project.class, jd.getProid());
				j.setProname(p.getProjectName());
			}
			j.setTy(jd.getTy());
			j.setDx(jd.getDx());
			j.setJtwz(jd.getJtwz());
			j.setContent(jd.getJtwz());
			baseService.update(j);
			r.setCode(id);
		}
		return r;
	}
	@RequestMapping("/load")
	@ResponseBody
	public SkillJd load(String id){
		return baseService.get(SkillJd.class, id);
	}
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(String id){
		Result r= new Result();
		SkillJd jd =  baseService.get(SkillJd.class, id);
		baseService.delete(jd);
		return r;
	}
}
