package com.radish.master.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Province;

@Controller
@RequestMapping("/SS")
public class GetAreaController {
	@Autowired
	private BaseService baseService;
	
	@RequestMapping("/loadSf")
	@ResponseBody
	public Result loadSf(){
		String sql = "select * from tbl_province where LevelType='1'";
		List<Province> list= baseService.findBySql(sql,Province.class);
		Result r = new Result();
		r.setData(list);
		return r;
	}
	
	@RequestMapping("/loadSzcs")
	@ResponseBody
	public Result loadSzcs(String parentId){
		String sql = "select * from tbl_province where LevelType='2' and ParentId='"+parentId+"'";
		List<Province> list= baseService.findBySql(sql,Province.class);
		Result r = new Result();
		r.setData(list);
		return r;
	}
	
	@RequestMapping("/loadSzcq")
	@ResponseBody
	public  Result loadSzcq(String parentId){
		String sql = "select * from tbl_province where LevelType='3' and ParentId='"+parentId+"'";
		List<Province> list= baseService.findBySql(sql,Province.class);
		Result r = new Result();
		r.setData(list);
		return r;
	}
	
}
