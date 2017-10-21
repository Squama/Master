package com.radish.master.usu.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.cnpc.framework.utils.EncryptUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.usu.entity.Employee;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.base.service.UserRoleService;
import com.cnpc.framework.base.service.UserService;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;

/**
* 员工信息查询管理控制器
* @author jrn
* 2017-10-17 13:41:51由代码生成器自动生成
*/
@Controller
@RequestMapping("/employeeQuery")
public class EmployeeQueryController {

    @Resource
    private BaseService baseService;
    
    @Resource
    private UserService userService;

    @Resource
    private UserRoleService userRoleService;
    
    private final static String initPassword= "silabo";

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
            String userId = userService.save(user).toString();
            userRoleService.setRoleForRegisterUser(userId);
            //头像和用户管理
            userService.updateUserAvatar(user, request.getRealPath("/"));
        } else {
            User oldUser=this.getUser(user.getId());
            if(oldUser.getLoginName().equals(user.getLoginName())){
               oldUser.setPassword(EncryptUtil.getPassword(initPassword,user.getLoginName()));
            }
            BeanUtils.copyProperties(user,oldUser);
            oldUser.setUpdateDateTime(new Date());
            userService.update(oldUser);
        }
        return new Result(true);
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        User employee=this.get(id);
        try{
            baseService.delete(employee);
            return new Result();
        }
        catch(Exception e){
            return new Result(false,"该数据已经被引用，不可删除");
        }
    }



}
