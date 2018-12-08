package com.radish.master.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.BuildDiary;
import com.radish.master.pojo.Options;

@Controller
@RequestMapping("/builddiary")
public class BuildDiaryController {
	private String prefix="/BuildDiary/";
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("/listIndex")
	public String listIndex(HttpServletRequest request){
		//列表添加人员条件
		User u = SecurityUtil.getUser();
		request.setAttribute("useid", u.getId());
		
		List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
		request.setAttribute("xm", JSONArray.toJSONString(xm));
		return  prefix +"listIndex";
	}
	@RequestMapping("/addIndex")
	public String addIndex(HttpServletRequest request){
		//列表添加人员条件
		User u = SecurityUtil.getUser();
		request.setAttribute("use", JSONArray.toJSONString(u));
		
		//判断人员是否存在班组人员表，存在，搜出班组的项目，不存在 搜出全部项目
		String sql = "select a.id value, a.project_name data from tbl_project a where "
				+ " exists (select b.* from tbl_user_team b where a.id = b.project_id and b.user_id = '"+u.getId()+"')";
		List tjxm = baseService.findMapBySql(sql, new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
		if(tjxm.size()>0){
			request.setAttribute("xm", JSONArray.toJSONString(tjxm));
		}else{
			List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
			request.setAttribute("xm", JSONArray.toJSONString(xm));
		}
		
		return  prefix +"add_Index";
	}
	
	@RequestMapping("/edit")
	public String editIndex(String id,HttpServletRequest request){
		List xm = baseService.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
		request.setAttribute("xm", JSONArray.toJSONString(xm));
		request.setAttribute("id", id);
        request.setAttribute("doWhat",request.getParameter("doWhat"));
		return prefix +"edit";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public Result save(HttpServletRequest request,BuildDiary rz ){
		User u = SecurityUtil.getUser();
		rz.setUserid(u.getId());
		String i = (String)baseService.save(rz);
		Result r = new Result();
		r.setCode(i);
		return r;
	}
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(HttpServletRequest request,String id){
		
		BuildDiary rz= baseService.get(BuildDiary.class, id);
		baseService.delete(rz);
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	@RequestMapping("/load")
	@ResponseBody
	public Result load(HttpServletRequest request){
		String id = request.getParameter("id");
		BuildDiary rz= baseService.get(BuildDiary.class, id);
		String useid = rz.getUserid();
		User u = baseService.get(User.class, useid);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rz", rz);
		map.put("use", u);
		Result r = new Result();
		r.setSuccess(true);
		r.setData(map);
		return r;
	}
	@RequestMapping("update")
	@ResponseBody
	public Result update(HttpServletRequest request,BuildDiary rz){
		User u = SecurityUtil.getUser();
		rz.setUserid(u.getId());
		baseService.update(rz);
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
}
