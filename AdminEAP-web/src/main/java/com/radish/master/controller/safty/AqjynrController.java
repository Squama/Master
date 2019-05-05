package com.radish.master.controller.safty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.controller.UploaderController;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.FileResult;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.DateUtil;
import com.cnpc.framework.utils.FileUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Project;
import com.radish.master.entity.project.Worker;
import com.radish.master.entity.qualityCheck.CheckFkd;
import com.radish.master.entity.safty.AqCheck;
import com.radish.master.entity.safty.Aqjy;
import com.radish.master.entity.safty.Aqjynr;
import com.radish.master.entity.safty.JobDuty;
import com.radish.master.entity.safty.SafeFile;

@Controller
@RequestMapping("/sjjynr")
public class AqjynrController {
	
	@Autowired
	private BaseService baseService;
	@Resource
	 private RuntimePageService runtimePageService;
	
	private String prefix = "/safetyManage/sjjy/";
	
	@RequestMapping("/jynr")
	public String jobduty(HttpServletRequest request){
		return prefix +"list";
	}
	@RequestMapping("/addnr")
	public String addnr(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("id",id);
		return prefix +"jynr";
	}
	@RequestMapping("/alllist")
	public String alllist(HttpServletRequest request){
		List<Worker> p = baseService.findMapBySql("select id  ,name  from tbl_worker ", new Object[]{}, new Type[]{StringType.INSTANCE}, Worker.class);
		request.setAttribute("rys", JSONArray.toJSONString(p));
		List<Aqjynr> jys = baseService.findMapBySql("select id  ,descs  from tbl_aqsjjy ", new Object[]{}, new Type[]{StringType.INSTANCE}, Aqjynr.class);
		request.setAttribute("jynrs", JSONArray.toJSONString(jys));
		return prefix +"alllist";
	}
	@RequestMapping("/add")
	public String add(HttpServletRequest request){
		List<Worker> p = baseService.findMapBySql("select id  ,name  from tbl_worker ", new Object[]{}, new Type[]{StringType.INSTANCE}, Worker.class);
		request.setAttribute("rys", JSONArray.toJSONString(p));
		List<Aqjynr> jys = baseService.findMapBySql("select id  ,descs  from tbl_aqsjjy ", new Object[]{}, new Type[]{StringType.INSTANCE}, Aqjynr.class);
		request.setAttribute("jys", JSONArray.toJSONString(jys));
		String id = request.getParameter("id");
		request.setAttribute("id",id);
		String lx = request.getParameter("lx");
		request.setAttribute("lx",lx);
		return prefix +"add";
	}
	@RequestMapping("/auidtbz/{id}")
	public String auidtbz(@PathVariable("id") String id,HttpServletRequest request){
		List<Worker> p = baseService.findMapBySql("select id  ,name  from tbl_worker ", new Object[]{}, new Type[]{StringType.INSTANCE}, Worker.class);
		request.setAttribute("rys", JSONArray.toJSONString(p));
		request.setAttribute("id",id);
		return prefix +"auidtBz";
	}
	@RequestMapping("/auidtgs/{id}")
	public String auditgs(@PathVariable("id") String id,HttpServletRequest request){
		List<Worker> p = baseService.findMapBySql("select id  ,name  from tbl_worker ", new Object[]{}, new Type[]{StringType.INSTANCE}, Worker.class);
		request.setAttribute("rys", JSONArray.toJSONString(p));
		request.setAttribute("id",id);
		return prefix +"auidtGs";
	}
	@RequestMapping("/auidtxm/{id}")
	public String auidtxm(@PathVariable("id") String id,HttpServletRequest request){
		List<Worker> p = baseService.findMapBySql("select id  ,name  from tbl_worker ", new Object[]{}, new Type[]{StringType.INSTANCE}, Worker.class);
		request.setAttribute("rys", JSONArray.toJSONString(p));
		request.setAttribute("id",id);
		return prefix +"auidtXm";
	}
	
	@RequestMapping("/savenr")
	@ResponseBody
	public Result savenr(HttpServletRequest request,Aqjynr nr){
		Result r = new Result();
		String id = request.getParameter("id");
		User u = SecurityUtil.getUser();
		if(id!=null){//修改
			Aqjynr n = baseService.get(Aqjynr.class, id);
			n.setCreate_time(new Date());
			n.setCreate_name_ID(u.getId());
    		n.setCreate_name(u.getName());
    		n.setBzname(nr.getBzname());
    		n.setXmname(nr.getXmname());
    		n.setGsname(nr.getGsname());
    		n.setDescs(nr.getDescs());
    		baseService.update(n);
		}else{//保存
			nr.setCreate_time(new Date());
			nr.setCreate_name_ID(u.getId());
    		nr.setCreate_name(u.getName());
    		baseService.save(nr);
		}
		r.setSuccess(true);
		return r;
	}
	@RequestMapping("/load")
	@ResponseBody
	public Aqjynr load(HttpServletRequest request,String id){
		Aqjynr n = baseService.get(Aqjynr.class, id);
		return n;
	}
	
	@RequestMapping("/getWorker")
	@ResponseBody
	public Worker getWorker(HttpServletRequest request){
		String workid = request.getParameter("workid");
		Worker w = baseService.get(Worker.class, workid);
		return w;
	}
	
	@RequestMapping("/saveJy")
	@ResponseBody
	public Result savenr(HttpServletRequest request){
		Result r = new Result();
		String jyid = request.getParameter("jyid");
		/*List<Aqjynr> nrs = baseService.find(" from Aqjynr where 1=1 ");
		if(nrs.size()<=0){//修改
			r.setSuccess(false);
			r.setMessage("请先维护三级教育内容!");
			return r;
		}*/
		Aqjynr nr =baseService.get(Aqjynr.class, jyid);
		String workid = request.getParameter("workerid");
		Worker w = baseService.get(Worker.class, workid);
		User u = SecurityUtil.getUser();
		Aqjy jy = new Aqjy();
		jy.setWorkerid(workid);
		jy.setName(w.getName());
		jy.setSex(w.getSex());
		jy.setWhcd(w.getWhcd());
		jy.setWorkType(w.getWorkType());
		jy.setBirthday(w.getBirthday());
		jy.setJctime(w.getJctime());
		jy.setAddress(w.getAddress());
		jy.setBzjy(nr.getBzname());
		jy.setXmjy(nr.getXmname());
		jy.setGsjy(nr.getGsname());
		jy.setJyid(nr.getId());
		jy.setJyname(nr.getDescs());
		jy.setStatus("20");
		jy.setCreate_time(new Date());
		jy.setCreate_name_ID(u.getId());
		jy.setCreate_name(u.getName());
		baseService.save(jy);
		 String name =w.getName()+"(三级安全教育)";
        // businessKey
        String businessKey = jy.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, u.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey("aqjy", name, variables, u.getId(), businessKey);
	}
	
	@RequestMapping("/loadJy")
	@ResponseBody
	public Aqjy loadJy(String id){
		Aqjy nrs = baseService.get(Aqjy.class, id);
		return nrs;
	}
	
}
