package com.radish.master.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.Mat;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.DutyCheck;
import com.radish.master.service.BudgetService;
import com.radish.master.service.DutyCheckService;

@Controller
@RequestMapping("/dutycheck")
public class DutyCheckController {
	@Autowired
	private DutyCheckService dcs;
	
	@Autowired
	private BaseService baseService;
	@Autowired
    private BudgetService budgetService;
	private String prefix="/safetyManage/dutycheck/";
	
	@RequestMapping("/index")
	public String index(){
		return prefix+"dutycheck_list";
	}
	@RequestMapping("/addIndex")
	public String addIndex(HttpServletRequest request){
		request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
		return prefix+"dutycheck_addIndex";
	}
	@RequestMapping("/edit")
	public String edit(String id,HttpServletRequest request){
		request.setAttribute("id", id);
	    request.setAttribute("doWhat",request.getParameter("doWhat"));
	    if(id==null){
	    	return prefix+"dutycheck_add";
	    }else{
	    	return prefix+"dutycheck_edit";
	    }
		
	}
	@RequestMapping("/checkMX")
	public String checkMX(String lx,HttpServletRequest request){
		return prefix+"BD/bd"+lx;
	}
	@RequestMapping("/getProject")
	public String getProject(){
		return prefix+"getProject";
	}
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public Result save(HttpServletRequest request,DutyCheck dc){
		String id = request.getParameter("id");
		Result r = new Result();
		if(id==null){
			dc.setIsValid("1");
			String i = dcs.save(dc);
			r.setCode(i);
		}else{
			DutyCheck dcl = dcs.load(id);
			dcl.setName(dc.getName());
			dcl.setProject_id(dc.getProject_id());
			dcl.setProject_name(dc.getProject_name());
			dcl.setCheck_name(dc.getCheck_name());
			dcl.setDeduction(dc.getDeduction());
			dcl.setUnitName(dc.getUnitName());
			dcl.setDuties(dc.getDuties());
			dcl.setCheck_time(dc.getCheck_time());
			dcl.setScore(dc.getScore());
			dcl.setCheck_result(dc.getCheck_result());
			dcl.setJc(dc.getJc());
			dcs.saveOrUpdate(dcl);
		}
		
    	r.setSuccess(true);
    	return r;
	}
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(String id){
		DutyCheck dc = baseService.get(DutyCheck.class, id);
		Result r = new Result();
		try{
		baseService.delete(dc);
		}catch(Exception e){
			r.setSuccess(false);
			return r;
		}
		r.setSuccess(true);
		return r;
	}
	@RequestMapping("/load")
	@ResponseBody
	public DutyCheck load(String id){
		DutyCheck dc = baseService.get(DutyCheck.class, id);
		return dc;
	}
	@RequestMapping(value="/getRylx",method = RequestMethod.POST)
    @ResponseBody
    public Result getRylx(){
		List<Dict> d = baseService.findBySql("select * from tbl_dict  where code='PROJECT_ZRYLX'",Dict.class);
    	List<Dict> list = baseService.findBySql("select * from tbl_dict where parent_id='"+d.get(0).getId()+"'",Dict.class);
      	Result r = new Result();
      	r.setData(list);
    	return r;
    }
}
