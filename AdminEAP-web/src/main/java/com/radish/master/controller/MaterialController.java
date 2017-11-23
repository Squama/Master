package com.radish.master.controller;

import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.dao.BaseDao;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.Mat;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Materiel;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/material")
public class MaterialController {
	
	private String prefix="/materialSpace/material/";
	
	@Autowired
	private BaseService baseService;
	@Autowired
	private SessionFactory sessionFactory;
	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
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
    @RefreshCSRFToken
    @RequestMapping(value="/detail_his",method = RequestMethod.GET)
    public String detail_his(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return prefix+"materiel_his";
    }
    
    @RequestMapping(value="/getFl",method = RequestMethod.POST)
    @ResponseBody
    public Result getFl(HttpServletRequest request){
    	List<Mat> m = new ArrayList<Mat>();
    	String parent_id = request.getParameter("p_id");
    	String sql ="";
    	if(parent_id==null){
    		m = baseService.findBySql("select * from tbl_dic_mat where parent_id is null", Mat.class);
    		sql = " select * from tbl_dic_mat where parent_id='"+m.get(0).getId()+"'";
    	}else{
    		sql = " select * from tbl_dic_mat where parent_id='"+parent_id+"'";
    	}
    	 
    	List<Mat> list= baseService.findBySql(sql, Mat.class);
    	
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
    	
    	String mat_id = materiel.getParent_ID();
    	Mat m = baseService.get(Mat.class, mat_id);
    	String code = m.getCode();
    	String[] strs = code.split("_");
    	String str = strs[1];
    	//拿到当前数据库数据的最大值
    	List<String> list = baseService.find("select max(ma.mat_number) from com.radish.master.entity.Materiel ma where ma.mat_number like '"+str+"%'");
    	if(list.get(0)==null){
    		materiel.setMat_number(str+"100001");
    	}else{
    		String s= list.get(0);
    		String num = s.substring(s.length()-6);
    		int i = Integer.parseInt(num);
    		i++;
    		materiel.setMat_number(str+i);
    	}
    	/*//暂写随机数
    	Random ra = new Random();
    	String s = str+ra.nextInt(10000);
    	materiel.setMat_number(s);*/
    	String id = (String)baseService.save(materiel);
    	Result r = new Result();
    	r.setSuccess(true);
    	return r;
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
    	Mat d1 = baseService.get(Mat.class, parent_id);
    	ssfl = d1.getName();
    	parent_id = d1.getParentId();
      	while(true){
      		parent_id = d1.getParentId();

      		if(parent_id!=null){
      			d1=baseService.get(Mat.class, parent_id);
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
