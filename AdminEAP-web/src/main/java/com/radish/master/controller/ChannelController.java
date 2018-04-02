package com.radish.master.controller;

import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.Mat;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Channel;
import com.radish.master.entity.Materiel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/channel")
public class ChannelController {
	
	private String prefix="/materialSpace/channel/";
	
	@Autowired
	private BaseService baseService;
	
	@RequestMapping(value="/index",method = RequestMethod.GET)
	public String index(){
		return prefix+"channel_list"	;
	}

    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        request.setAttribute("doWhat",request.getParameter("doWhat"));
        return prefix+"channel_edit";
    }
    @RefreshCSRFToken
    @RequestMapping(value="/chooseArea",method = RequestMethod.GET)
    public String chooseArea(String box,HttpServletRequest request){
        request.setAttribute("box", box);
        return prefix+"chooseArea";
    }
	
    @RefreshCSRFToken
    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        request.setAttribute("doWhat",request.getParameter("doWhat"));
        return prefix+"channel_edit";
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
    public Result save(String mat_id,Channel c,String c_id,HttpServletRequest request) throws UnsupportedEncodingException{
    	c.setId(null);
    	Materiel m = baseService.get(Materiel.class, mat_id);
    	
    	c.setMat_ID(m.getMat_number());
    	c.setCreate_time(new Date());
    	Double price = c.getPrice();
    	c.setPrice(new BigDecimal(price).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
    	c.setIsValid("1");
    	String id = (String)baseService.save(c);
    	if(c_id!=null){
        	Channel c1 = baseService.get(Channel.class, c_id);
        	if(c1!=null){
        		c1.setIsValid("0");
            	baseService.update(c1);
        	}
    	}
    	
    	Result r = new Result();
    	r.setSuccess(true);
    	r.setCode(id);
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
    @RequestMapping(value="/getChannelList",method = RequestMethod.POST)
    @ResponseBody
    public Result getChannelList(String mat_id){
    	List<Channel> list = baseService.find("select c from com.radish.master.entity.Channel c where c.mat_ID='"+mat_id+"' and c.isValid = '1'");
    	Result r = new Result();
    	r.setData(list);
    	r.setSuccess(true);
    	return r;
    }
}
