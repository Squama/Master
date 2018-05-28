package com.radish.master.controller.workmanage;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.UserLeave;
import com.radish.master.service.CommonService;

@Controller
@RequestMapping("/dzzuser")
public class DzzUserController {
	String prefix = "workmanage/dzzuser/";
	
	@Resource
	 private RuntimePageService runtimePageService;
	
	@Autowired
	private BaseService baseService;
	@Resource
    private CommonService commonService;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request){
		return prefix +"dzzList";
	}
	
	@RequestMapping("/deptAuidtIndex")
	public String deptAuidtIndex(HttpServletRequest request){
		User u = SecurityUtil.getUser();
		String deptid = u.getDeptId();
		request.setAttribute("deptid", deptid);
		return prefix + "deptAuidtList";
	}
	
	@RequestMapping("/getCheck")
	public String getCheck(HttpServletRequest request){
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		return prefix + "getCheck";
	}
	
	@RequestMapping("/submitDept")
	@ResponseBody
	public Result submitDept(HttpServletRequest request){
		String id = request.getParameter("id");
		User u  = baseService.get(User.class, id);
		u.setZzView("");
		u.setZzDeptDesc("");
		u.setZzFaildDesc("");
		u.setZzStatus("10");
		baseService.update(u);
		
		Result r = new Result();
		r.setSuccess(true);
		return r;
	}
	@RequestMapping("/audit/{id}")
	public String auid(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("users", JSONArray.toJSONString(baseService.get(User.class, id)));
		request.setAttribute("eduOptions", JSONArray.toJSONString(commonService.getEducationCombobox()));
        request.setAttribute("ethOptions", JSONArray.toJSONString(commonService.getEthnicCombobox()));
		return prefix +"/auidindex";
	}
	
	@RequestMapping("/start")
	@ResponseBody
	public Result start(User user,String id) {
		String uid = SecurityUtil.getUserId();
		User u = baseService.get(User.class, id);
		u.setZzDeptDesc(user.getZzDeptDesc());
		u.setZzStatus("20");
		baseService.update(u);
		User use = SecurityUtil.getUser();
        String name ="【"+u.getName()+"的转正审批】";

        // businessKey
        String businessKey = u.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, use.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey("zzUser", name, variables, u.getId(), businessKey);
    }
}
