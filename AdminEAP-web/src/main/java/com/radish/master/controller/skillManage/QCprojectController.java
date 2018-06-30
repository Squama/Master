package com.radish.master.controller.skillManage;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.radish.master.entity.skillManage.QCproject;
import com.radish.master.service.BudgetService;

@Controller
@RequestMapping("/qcproject")
public class QCprojectController {
	private String prefix ="SkillManage/Qc/";
	
	@Autowired
	private BaseService baseService;
	@Resource
    private BudgetService budgetService;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request){
		request.setAttribute("xm", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		return prefix + "list";
	}
	@RequestMapping("/add")
	public String addIndex(HttpServletRequest request){
		request.setAttribute("xm", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		return prefix + "addIndex";
	}
	@RequestMapping("save")
	@ResponseBody
	public Result save(HttpServletRequest request,QCproject qc){
		Result r = new Result();
		String id = request.getParameter("id");
		User u = SecurityUtil.getUser();
		if(id==null){//保存
			qc.setCreate_time(new Date());
			qc.setCreate_name_ID(u.getId());
			qc.setCreate_name(u.getName());
			Project p = baseService.get(Project.class, qc.getProid());
			qc.setProname(p.getProjectName());
			r.setCode(qc.getId());
			baseService.save(qc);
		}else{
			QCproject q = baseService.get(QCproject.class, id);
			q.setProid(qc.getProid());
			Project p = baseService.get(Project.class, qc.getProid());
			q.setProname(p.getProjectName());
			q.setMd(qc.getMd());
			q.setGcgk(qc.getGcgk());
			q.setXzgk(qc.getXzgk());
			q.setXzcy(qc.getXzcy());
			q.setXzjh(qc.getXzjh());
			q.setXtly(qc.getXtly());
			q.setXzdc(qc.getXzdc());
			q.setLz(qc.getLz());
			q.setYyfx(qc.getYyfx());
			q.setYyqr(qc.getYyqr());
			q.setDc(qc.getDc());
			q.setDcssqk(qc.getDcssqk());
			q.setJg(qc.getJg());
			q.setGgcs(qc.getGgcs());
			q.setZj(qc.getZj());
			baseService.update(q);
			r.setCode(id);
		}
		return r;
	}
	@RequestMapping("load")
	@ResponseBody
	public QCproject load(HttpServletRequest request){
		String id = request.getParameter("id");
		return baseService.get(QCproject.class, id);
	}
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(HttpServletRequest request){
		String id = request.getParameter("id");
		QCproject qc = baseService.get(QCproject.class, id);
		baseService.delete(qc);
		Result r = new Result();
		return r;
	}
}
