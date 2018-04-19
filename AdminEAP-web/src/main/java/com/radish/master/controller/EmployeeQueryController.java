package com.radish.master.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.entity.UserRole;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.base.service.UserRoleService;
import com.cnpc.framework.base.service.UserService;
import com.cnpc.framework.utils.EncryptUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.service.WechatService;
import com.radish.master.system.SpringUtil;

/**
* 员工信息查询管理控制器
* @author dongy
*/
@Controller
@RequestMapping("/employeeQuery")
public class EmployeeQueryController {

    @Resource
    private BaseService baseService;
    
    @Resource
    private UserService userService;
    
    @Resource
    private WechatService wechatService;

    @Resource
    private UserRoleService userRoleService;
    
    private final static String initPassword= "123456";

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "workmanage/employee/employeeQuery_list";
    }
    
    @RequestMapping(value="/deletelist",method = RequestMethod.GET)
    public String deleteList(HttpServletRequest request){
        request.setAttribute("auditStatus", 20);
        return "workmanage/employee/employeeQuery_list";
    }
    
    @RequestMapping(value="/manage",method = RequestMethod.GET)
    public String manage(){
        return "workmanage/employee/employee_manage";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(){
        return "workmanage/employee/employee_add";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "workmanage/employee/employee_edit";
    }
    
    @RequestMapping(value="/audit",method = RequestMethod.GET)
    public String audit(){
        return "workmanage/employee/employee_audit";
    }

    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "employee/employeeQuery_detail";
    }

    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public User get(@PathVariable("id") String id){
        return baseService.get(User.class, id);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private User getUser(String id) {

        return userService.get(User.class, id);
    }

    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(User user, HttpServletRequest request){
        if (StrUtil.isEmpty(user.getId())) {
            //设置初始密码
            user.setPassword(EncryptUtil.getPassword(initPassword,user.getLoginName()));
            user.setAuditStatus("30");
            String userId = userService.save(user).toString();
            userRoleService.setRoleForRegisterUser(userId);
            //头像和用户管理
            userService.updateUserAvatar(user, request.getRealPath("/"));
            wechatService.setUserRole(user.getJobId(), user);
        } else {
        	boolean flag = false;
        	String oldJobID = "";
            User oldUser=this.getUser(user.getId());
            if(!oldUser.getLoginName().equals(user.getLoginName())){
            	oldUser.setPassword(EncryptUtil.getPassword(initPassword,user.getLoginName()));
            }
            if(!oldUser.getJobId().equals(user.getJobId())){
            	flag=true;
            	oldJobID = oldUser.getJobId();
            }
            SpringUtil.copyPropertiesIgnoreNull(user, oldUser);
            oldUser.setUpdateDateTime(new Date());
            userService.update(oldUser);
            if(flag){
            	wechatService.clearUserRole(oldJobID, oldUser);
            	wechatService.setUserRole(oldUser.getJobId(), oldUser);
            }
        }
        return new Result(true);
    }
    
    @RequestMapping(value="/tochangepwd",method = RequestMethod.GET)
    public String toChangePWD(){
        return "workmanage/employee/changePWD";
    }

    @VerifyCSRFToken
    @RequestMapping(value="/changepwd")
    @ResponseBody
    public Result savePWD(String userpwd){
        User user = userService.get(User.class, SecurityUtil.getUserId());
        user.setPassword(EncryptUtil.getPassword(userpwd,user.getLoginName()));
        
        try{
        	userService.save(user);
        }catch (Exception e) {
        	return new Result(false);
		}
        
        return new Result(true);
    }
    
    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        
        User employee=this.get(id);
        employee.setAuditStatus("20");
        employee.setUpdateDateTime(new Date());
        userService.update(employee);
        return new Result();
    }
    @RequestMapping(value="/getRylx",method = RequestMethod.POST)
    @ResponseBody
    public Result getRylx(){
		List<Dict> d = baseService.findBySql("select * from tbl_dict  where code='JOBS'",Dict.class);
    	List<Dict> list = baseService.findBySql("select * from tbl_dict where parent_id='"+d.get(0).getId()+"'",Dict.class);
      	Result r = new Result();
      	r.setData(list);
    	return r;
    }
    @RequestMapping(value="/auditResult/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result auditResult(@PathVariable("id") String id){
        
        User employee=this.get(id);
        if("20".equals(employee.getAuditStatus())){//离职
        	List<UserRole> r = baseService.findBySql("select * from tbl_user_role  where userID='"+id+"'",UserRole.class);
        	for(UserRole u:r){
        		userService.delete(u);
        	}
        	userService.delete(employee);
        	return new Result();
        }else if("30".equals(employee.getAuditStatus())){//入职
        	employee.setAuditStatus("10");
        }
        userService.update(employee);
        return new Result();
    }

}
