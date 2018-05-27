package com.radish.master.controller.workmanage;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
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
import com.radish.master.entity.review.ReviewBid;
import com.radish.master.service.CommonService;
import com.radish.master.system.SpringUtil;

@Controller
@RequestMapping("/leaveuser")
public class LeaveUserController {
	String prefix = "workmanage/leaveuser";
	
	@Resource
	 private RuntimePageService runtimePageService;
	
	@Autowired
	private BaseService baseService;
	
	@Resource
    private CommonService commonService;
	
	@RequestMapping("/addindex")
	public String addindex(HttpServletRequest request){
		String useid = SecurityUtil.getUserId();
		List<UserLeave> lzs = baseService.findBySql("select * from tbl_userLeave where userId='"+useid+"' and leaveStatus<>'50'", UserLeave.class);
		if(lzs.size()>0){
			request.setAttribute("users", JSONArray.toJSONString(lzs.get(0)));
		}
		request.setAttribute("id", useid);
        request.setAttribute("eduOptions", JSONArray.toJSONString(commonService.getEducationCombobox()));
        request.setAttribute("ethOptions", JSONArray.toJSONString(commonService.getEthnicCombobox()));
		return prefix +"/addindex";
	}
	@RequestMapping("/audit/{id}")
	public String auid(@PathVariable("id") String id,HttpServletRequest request){
		request.setAttribute("users", JSONArray.toJSONString(baseService.get(UserLeave.class, id)));
		request.setAttribute("eduOptions", JSONArray.toJSONString(commonService.getEducationCombobox()));
        request.setAttribute("ethOptions", JSONArray.toJSONString(commonService.getEthnicCombobox()));
		return prefix +"/auidindex";
	}
	@RequestMapping("/addSq")
	public String addSq(HttpServletRequest request){
		return prefix +"/addSq";
	}
	
	@RequestMapping("/start")
	@ResponseBody
	public Result start(UserLeave lz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String uid = SecurityUtil.getUserId();
		User u = baseService.get(User.class, uid);
		u.setAuditStatus("20");
		UserLeave lzn = new UserLeave();
		PropertyUtils.copyProperties(lzn, u);
		lzn.setUserId(u.getId());
		/*lzn.setName(u.getName());
		lzn.setSex(u.getSex());
		lzn.setBirthday(u.getBirthday());
		lzn.setLoginName(u.getLoginName());
		lzn.setPassword(u.getPassword());
		lzn.setEmail(u.getEmail());
		lzn.setTelphone(u.getTelphone());
		lzn.setMobile(u.getMobile());
		lzn.setQq(u.getQq());
		lzn.setWechat(u.getWechat());
		lzn.setOpenAccount(u.getOpenAccount());
		lzn.setIsSuperAdmin(u.getIsSuperAdmin());
		lzn.setDeptId(u.getDeptId());
		lzn.setJobId(u.getJobId());
		lzn.setAuditStatus(u.getAuditStatus());
		lzn.setIdentificationNumber(u.getIdentificationNumber());
		lzn.setEthnic(u.getEthnic());
		lzn.setEducation(u.getEducation());
		lzn.setAddress(u.getAddress());
		lzn.setEmergencyContactPhone(u.getEmergencyContactPhone());
		lzn.setEmergencyContact(u.getEmergencyContact());
		lzn.setWorkType(u.getWorkType());
		lzn.setBasicSalary(u.getBasicSalary());
		lzn.setDeptName(u.getDeptName());
		lzn.setJobNumber(u.getJobNumber());
		lzn.setAge(u.getAge());
		lzn.setPolitical(u.getPolitical());
		lzn.setSomeTitle(u.getSomeTitle());
		lzn.setSchool(u.getSchool());
		lzn.setProfessional(u.getProfessional());
		lzn.setBloodType(u.getBloodType());
		lzn.setBankCount(u.getBankCount());
		lzn.setFileId(u.getFileId());
		lzn.setHireDate(u.getHireDate());
		lzn.setZzStatus(u.getZzStatus());*/
		
		
		lzn.setLeaveReason(lz.getLeaveReason());
		lzn.setLeaveTime(lz.getLeaveTime());
		lzn.setLeaveStatus("10");
		baseService.save(lzn);
		baseService.update(u);
		
        String name ="【"+u.getName()+"的离职审批】";

        // businessKey
        String businessKey = lzn.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, u.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey("leaveUser", name, variables, u.getId(), businessKey);
    }
	
}
