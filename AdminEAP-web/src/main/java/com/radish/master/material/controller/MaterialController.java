package com.radish.master.material.controller;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.entity.UserAvatar;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.material.entity.Materiel;

@Controller
@RequestMapping("/material")
public class MaterialController {
	
	private String prefix="/materialSpace/material/";
	
	@Autowired
	private BaseService baseService;
	
	@RequestMapping(value="/index",method = RequestMethod.GET)
	public String index(){
		return prefix+"materiel_list"	;
	}
	
	@RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(){
        return prefix+"materiel_add";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        request.setAttribute("doWhat",request.getParameter("doWhat"));
        return prefix+"materiel_edit";
    }
	
    @RefreshCSRFToken
    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        request.setAttribute("doWhat",request.getParameter("doWhat"));
        return prefix+"materiel_edit";
    }
    
    @RequestMapping(value="/getFl",method = RequestMethod.POST)
    @ResponseBody
    public Result getFl(HttpServletRequest request){
    	String parent_id = request.getParameter("p_id");
    	String sql = " select * from tbl_dict where parent_id='"+parent_id+"'";
    	List<Dict> list= baseService.findBySql(sql, Dict.class);
    	
    	Result r = new Result();
    	r.setData(list);
    	return r;
    }
    
    @VerifyCSRFToken
    @RequestMapping(value="/save",method = RequestMethod.POST)
    @ResponseBody
    public Result save(HttpServletRequest request,Materiel materiel){
    	String ss1 = request.getParameter("ss1");
    	String ss2 = request.getParameter("ss2");
    	String ss3 = request.getParameter("ss3");
    	String ss4 = request.getParameter("ss4");
    	if(ss1!=null){
    		materiel.setParent_ID(ss1);
    	}
    	if(ss2!=null){
    		materiel.setParent_ID(ss2);
    	}
    	if(ss3!=null){
    		materiel.setParent_ID(ss3);
    	}
    	if(ss4!=null){
    		materiel.setParent_ID(ss4);
    	}
    	materiel.setCreate_time(new Date());
    	//暂写随机数
    	Random ra = new Random();
    	String s = "wl"+ra.nextInt(10000);
    	materiel.setMat_number(s);
    	String id = (String)baseService.save(materiel);
    	Result r = new Result();
    	r.setSuccess(true);
    	return new Result();
    }
    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
    	Materiel m = baseService.get(Materiel.class, id);
    	baseService.delete(m);
    	Result r = new Result();
    	r.setSuccess(true);
    	return r;
    }
    @RequestMapping(value="/get",method = RequestMethod.POST)
    @ResponseBody
    public Result getById(String id){
    	Materiel m=baseService.get(Materiel.class, id);
    	Result r = new Result();
    	r.setData(m);
    	return r;
    }
    @RequestMapping(value="/getSsfl",method = RequestMethod.POST)
    @ResponseBody
    public Result getSsfl(String parent_id){
    	String ssfl = "";
    	Dict d1 = baseService.get(Dict.class, parent_id);
    	ssfl = d1.getName();
    	parent_id = d1.getParentId();
      	while(true){
      		parent_id = d1.getParentId();
      		if(!"1029".equals(parent_id)){
      			d1=baseService.get(Dict.class, parent_id);
      			parent_id= d1.getParentId();
      			ssfl = d1.getName()+"-"+ssfl;
      		}else{
      			break;
      		}
      		
      	}
      	
      	Result r = new Result();
      	r.setMessage(ssfl);
    	return r;
    }
    
    @VerifyCSRFToken
    @RequestMapping(value="/update",method = RequestMethod.POST)
    @ResponseBody
    public Result update(HttpServletRequest request,Materiel ma){
    	String id = ma.getId();
    	Materiel m=baseService.get(Materiel.class, id);
    	m.setMat_name(ma.getMat_name());
    	m.setMat_standard(ma.getMat_standard());
    	m.setUnit(ma.getUnit());
    	m.setIsValid(ma.getIsValid());
    	
    	baseService.update(m);
    	Result r = new Result();
    	r.setSuccess(true);
    	return r;
    }
}
