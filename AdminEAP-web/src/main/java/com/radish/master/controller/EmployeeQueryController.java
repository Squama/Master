package com.radish.master.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.annotations.common.util.StringHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.annotation.RefreshCSRFToken;
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
import com.radish.master.entity.JobDeptRt;
import com.radish.master.entity.common.UserExport;
import com.radish.master.service.CommonService;
import com.radish.master.service.WechatService;
import com.radish.master.system.CodeException;
import com.radish.master.system.ReportSXSSF;
import com.radish.master.system.SpringUtil;
import com.radish.master.system.TimeUtil;
import com.radish.master.system.report.Column;

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

    @Resource
    private CommonService commonService;

    private final static String initPassword = "123456";

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDepartmentCombobox()));
        request.setAttribute("eduOptions", JSONArray.toJSONString(commonService.getEducationCombobox()));
        request.setAttribute("ethOptions", JSONArray.toJSONString(commonService.getEthnicCombobox()));
        return "workmanage/employee/employeeQuery_list";
    }

    @RequestMapping(value = "/deletelist", method = RequestMethod.GET)
    public String deleteList(HttpServletRequest request) {
        request.setAttribute("auditStatus", 20);
        return "workmanage/employee/employeeQuery_list";
    }

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String manage(HttpServletRequest request) {
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDepartmentCombobox()));
        return "workmanage/employee/employee_manage";
    }

    @RefreshCSRFToken
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(HttpServletRequest request) {
        request.setAttribute("eduOptions", JSONArray.toJSONString(commonService.getEducationCombobox()));
        request.setAttribute("ethOptions", JSONArray.toJSONString(commonService.getEthnicCombobox()));
        return "workmanage/employee/employee_add";
    }

    @RefreshCSRFToken
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        request.setAttribute("eduOptions", JSONArray.toJSONString(commonService.getEducationCombobox()));
        request.setAttribute("ethOptions", JSONArray.toJSONString(commonService.getEthnicCombobox()));
        return "workmanage/employee/employee_edit";
    }

    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public String audit() {
        return "workmanage/employee/employee_audit";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        request.setAttribute("eduOptions", JSONArray.toJSONString(commonService.getEducationCombobox()));
        request.setAttribute("ethOptions", JSONArray.toJSONString(commonService.getEthnicCombobox()));
        return "workmanage/employee/employee_detail";
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    @ResponseBody
    public User get(@PathVariable("id") String id) {
        return baseService.get(User.class, id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private User getUser(String id) {

        return userService.get(User.class, id);
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public Result save(User user, HttpServletRequest request) {
        if (StrUtil.isEmpty(user.getId())) {
            // 设置初始密码
            user.setPassword(EncryptUtil.getPassword(initPassword, user.getLoginName()));
            user.setAuditStatus("30");
            String userId = userService.save(user).toString();
            userRoleService.setRoleForRegisterUser(userId);
            // 头像和用户管理
            userService.updateUserAvatar(user, request.getRealPath("/"));
            wechatService.setUserRole(user.getJobId(), user);
        } else {
            boolean flag = false;
            String oldJobID = "";
            User oldUser = this.getUser(user.getId());
            if (!oldUser.getLoginName().equals(user.getLoginName())) {
                oldUser.setPassword(EncryptUtil.getPassword(initPassword, user.getLoginName()));
            }
            if (!oldUser.getJobId().equals(user.getJobId())) {
                flag = true;
                oldJobID = oldUser.getJobId();
            }
            SpringUtil.copyPropertiesIgnoreNull(user, oldUser);
            oldUser.setUpdateDateTime(new Date());
            userService.update(oldUser);
            if (flag) {
                wechatService.clearUserRole(oldJobID, oldUser);
                wechatService.setUserRole(oldUser.getJobId(), oldUser);
            }
        }
        return new Result(true);
    }

    @RequestMapping(value = "/tochangepwd", method = RequestMethod.GET)
    public String toChangePWD() {
        return "workmanage/employee/changePWD";
    }

    @RequestMapping(value = "/changepwd")
    @ResponseBody
    public Result savePWD(String userpwd) {
        User user = userService.get(User.class, SecurityUtil.getUserId());
        user.setPassword(EncryptUtil.getPassword(userpwd, user.getLoginName()));

        try {
            userService.save(user);
        } catch (Exception e) {
            return new Result(false);
        }

        return new Result(true);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id) {

        User employee = this.get(id);
        employee.setAuditStatus("20");
        employee.setUpdateDateTime(new Date());
        userService.update(employee);
        return new Result();
    }

    @RequestMapping(value = "/getRylx", method = RequestMethod.POST)
    @ResponseBody
    public Result getRylx() {
        List<Dict> d = baseService.findBySql("select * from tbl_dict  where code='JOBS'", Dict.class);
        List<Dict> list = baseService.findBySql("select * from tbl_dict where parent_id='" + d.get(0).getId() + "'", Dict.class);
        Result r = new Result();
        r.setData(list);
        return r;
    }
    @RequestMapping(value = "/changeJob", method = RequestMethod.POST)
    @ResponseBody
    public Result changeJob(HttpServletRequest request) {
    	String deptid= request.getParameter("deptid");
    	Result r = new Result();
    	List<Dict> d = baseService.findBySql("select * from tbl_dict  where code='JOBS'", Dict.class);
    	if(StringHelper.isEmpty(deptid)){
            List<Dict> list = baseService.findBySql("select * from tbl_dict where parent_id='" + d.get(0).getId() + "'", Dict.class);
            r.setData(list);	
    	}else{
    		String sql = "select * from tbl_dict where parent_id='" + d.get(0).getId() + "'";
    		List<JobDeptRt> gxs = baseService.findBySql("select * from tbl_job_dept  where deptId='"+deptid+"'", JobDeptRt.class);
    		if(gxs.size()>0){
    			String jobids = "";
    			for(JobDeptRt gx:gxs){
    				jobids +="'"+gx.getJobId()+"',";
    			}
    			jobids = jobids.substring(0, jobids.length()-1);
    			sql += " and id in("+jobids+")";
    			List<Dict> list = baseService.findBySql(sql, Dict.class);
    			r.setData(list);
    		}else{
    			List<Dict> list = baseService.findBySql(sql, Dict.class);
    			r.setData(list);
    		}
    		
    	}
        
        return r;
    }
    

    @RequestMapping(value = "/auditResult/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result auditResult(@PathVariable("id") String id) {

        User employee = this.get(id);
        if ("20".equals(employee.getAuditStatus())) {// 离职
            List<UserRole> r = baseService.findBySql("select * from tbl_user_role  where userID='" + id + "'", UserRole.class);
            for (UserRole u : r) {
                userService.delete(u);
            }
            userService.delete(employee);
            return new Result();
        } else if ("30".equals(employee.getAuditStatus())) {// 入职
            employee.setAuditStatus("10");
        }
        userService.update(employee);
        return new Result();
    }

    @RequestMapping(value = "/getexcel", method = RequestMethod.POST)
    @ResponseBody
    public String exportExcel(User user, HttpServletRequest request, HttpServletResponse response) {
        try {
            ArrayList<String[]> rows = new ArrayList<>();
            String[] header = { "序号", "姓名", "性别", "部门", "职务", "工种", "手机", "座机", "身份证号", "学历", "民族", "基本工资", "紧急联系人", "紧急联系人电话" };
            rows.add(header);
            getExcel(user, rows);
            String[] cells = new String[14];
            for (int i = 0; i < cells.length; i++) {
                cells[i] = "";
            }
            cells[9] = "总计";
            cells[10] = String.valueOf(rows.size() - 1).concat("条");
            rows.add(cells);
            Column[] columns = getColumn();
            ReportSXSSF r = new ReportSXSSF("员工花名册", "", columns, rows);

            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"Employee_ " + TimeUtil.getCurrentDate() + ".xlsx\"");
            OutputStream output = response.getOutputStream();
            r.export(output);

        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private ArrayList<String[]> getExcel(User user, ArrayList<String[]> rows) throws CodeException {
        StringBuilder buffer = new StringBuilder();
        
        buffer.append("SELECT U.id,U.`name`,CASE WHEN U.sex='1' THEN '男' ELSE '女' END as sex,O.`name` deptId,D.`name` jobId,U.work_type workType,U.mobile,U.telphone, ");
        buffer.append("U.identification_number identificationNumber,E.`name` education,M.`name` ethnic, U.basic_salary basicSalary, ");
        buffer.append("U.emergency_contact emergencyContact, U.emergency_contact_phone emergencyContactPhone ");
        buffer.append("FROM tbl_user U ");
        buffer.append("LEFT JOIN tbl_org O ");
        buffer.append("ON U.dept_id = O.id ");
        buffer.append("LEFT JOIN tbl_dict D ");
        buffer.append("ON U.job_id = D.id ");
        buffer.append("LEFT JOIN ");
        buffer.append("(select code,name from tbl_dict where parent_id = '402881f053cd670d0153cd6d6be40000') E ");
        buffer.append("ON U.education = E.`code` ");
        buffer.append("LEFT JOIN ");
        buffer.append("(select code,name from tbl_dict where parent_id = '402881f053e55e710153e570c0890001') M ");
        buffer.append("ON U.ethnic = M.`code` ");
        buffer.append("WHERE 1=1 ");
        
        Map<String, Object> params = new HashMap<String, Object>();

        if (!StrUtil.isEmpty(user.getDeptId())) {
            buffer.append(" AND U.dept_id=:deptId ");
            params.put("deptId", user.getDeptId());
        }

        if (!StrUtil.isEmpty(user.getName())) {
            buffer.append(" AND U.name LIKE '%:name%' ");
            params.put("name", user.getName());
        }

        if (!StrUtil.isEmpty(user.getLoginName())) {
            buffer.append(" AND U.login_name LIKE '%:loginName%' ");
            params.put("loginName", user.getLoginName());
        }
        try {
            List<UserExport> users = commonService.findBySql(buffer.toString(), params, UserExport.class);
            for (int i = 0; i < users.size(); i++) {
                UserExport userColumn = users.get(i);

                String[] cells = new String[14];

                cells[0] = String.valueOf(i + 1);
                cells[1] = userColumn.getName();
                cells[2] = userColumn.getSex();
                cells[3] = userColumn.getDeptId();
                cells[4] = userColumn.getJobId();
                cells[5] = userColumn.getWorkType();
                cells[6] = userColumn.getMobile();
                cells[7] = userColumn.getTelphone();
                cells[8] = userColumn.getIdentificationNumber();
                cells[9] = userColumn.getEducation();
                cells[10] = userColumn.getEthnic();
                cells[11] = userColumn.getBasicSalary();
                cells[12] = userColumn.getEmergencyContact();
                cells[13] = userColumn.getEmergencyContactPhone();
                        
                rows.add(cells);
            }
        } catch (Exception e) {
            throw new CodeException(e.getMessage());
        }
        return rows;
    }

    private Column[] getColumn() {
        Column[] columns = new Column[14];

        columns[0] = new Column();
        columns[0].setAlign(Column.ALIGN_CENTER);
        columns[0].setWidth(2000);

        columns[1] = new Column();
        columns[1].setAlign(Column.ALIGN_CENTER);
        columns[1].setWidth(6000);

        columns[2] = new Column();
        columns[2].setAlign(Column.ALIGN_CENTER);
        columns[2].setWidth(6000);

        columns[3] = new Column();
        columns[3].setAlign(Column.ALIGN_CENTER);
        columns[3].setWidth(6000);

        columns[4] = new Column();
        columns[4].setAlign(Column.ALIGN_CENTER);
        columns[4].setWidth(6000);

        columns[5] = new Column();
        columns[5].setAlign(Column.ALIGN_CENTER);
        columns[5].setWidth(5000);

        columns[6] = new Column();
        columns[6].setAlign(Column.ALIGN_CENTER);
        columns[6].setWidth(5000);

        columns[7] = new Column();
        columns[7].setAlign(Column.ALIGN_RIGHT);
        columns[7].setWidth(6000);

        columns[8] = new Column();
        columns[8].setAlign(Column.ALIGN_RIGHT);
        columns[8].setWidth(6000);
        
        columns[9] = new Column();
        columns[9].setAlign(Column.ALIGN_CENTER);
        columns[9].setWidth(8000);
        
        columns[10] = new Column();
        columns[10].setAlign(Column.ALIGN_RIGHT);
        columns[10].setWidth(6000);
        
        columns[11] = new Column();
        columns[11].setAlign(Column.ALIGN_CENTER);
        columns[11].setWidth(8000);
        
        columns[12] = new Column();
        columns[12].setAlign(Column.ALIGN_CENTER);
        columns[12].setWidth(8000);
        
        columns[13] = new Column();
        columns[13].setAlign(Column.ALIGN_CENTER);
        columns[13].setWidth(8000);

        return columns;
    }

}
