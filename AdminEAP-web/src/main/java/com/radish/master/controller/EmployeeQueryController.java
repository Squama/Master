package com.radish.master.controller;

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

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.Role;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.entity.UserRole;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.base.service.UserRoleService;
import com.cnpc.framework.base.service.UserService;
import com.cnpc.framework.utils.CodeException;
import com.cnpc.framework.utils.EncryptUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.JobDeptRt;
import com.radish.master.entity.UserLeave;
import com.radish.master.entity.common.UserExport;
import com.radish.master.entity.review.ReviewFile;
import com.radish.master.pojo.OptionsDept;
import com.radish.master.service.CommonService;
import com.radish.master.service.WechatService;
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
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDepartmentCombobox()));
        request.setAttribute("eduOptions", JSONArray.toJSONString(commonService.getEducationCombobox()));
        request.setAttribute("ethOptions", JSONArray.toJSONString(commonService.getEthnicCombobox()));
        return "workmanage/employee/employeeDelete_list";
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
        request.setAttribute("regionCodes", JSONArray.toJSONString(commonService.getRegionCode()));
        return "workmanage/employee/employee_add";
    }

    @RefreshCSRFToken
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        request.setAttribute("eduOptions", JSONArray.toJSONString(commonService.getEducationCombobox()));
        request.setAttribute("ethOptions", JSONArray.toJSONString(commonService.getEthnicCombobox()));
        request.setAttribute("regionCodes", JSONArray.toJSONString(commonService.getRegionCode()));
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
        request.setAttribute("regionCodes", JSONArray.toJSONString(commonService.getRegionCode()));
        return "workmanage/employee/employee_detail";
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    @ResponseBody
    public User get(@PathVariable("id") String id) {
        return baseService.get(User.class, id);
    }
    
    @RequestMapping(value = "/getLeave/{id}", method = RequestMethod.POST)
    @ResponseBody
    public UserLeave getLeave(@PathVariable("id") String id) {
        return baseService.get(UserLeave.class, id);
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
            user.setZzStatus("0");
            user.setAuditdept(user.getDeptId());
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
            if (oldUser.getJobId()!=null&&!oldUser.getJobId().equals(user.getJobId())) {
                flag = true;
                oldJobID = oldUser.getJobId();
            }
            SpringUtil.copyPropertiesIgnoreNull(user, oldUser);
            oldUser.setRegionCode(user.getRegionCode());
            oldUser.setDutyContent(user.getDutyContent());
            oldUser.setUpdateDateTime(new Date());
            oldUser.setAuditdept(user.getAuditdept());
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
            String[] header = { "序号", "姓名", "性别", "出生日期", "民族", "身份证号码", "学历", "手机", "座机","现住址", "职位", "入职时间" };
            rows.add(header);
            getExcel(user, rows);
            String[] cells = new String[12];
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
        buffer.append("U.emergency_contact emergencyContact, U.emergency_contact_phone emergencyContactPhone, ");
        buffer.append("U.birthday birthday, U.hireDate hireDate , U.address address ");
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
        if (!StrUtil.isEmpty(user.getAuditStatus())) {
            buffer.append(" AND U.audit_status = :auditStatus ");
            params.put("auditStatus", user.getAuditStatus().replace(",", ""));
        }
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<UserExport> users = commonService.findBySql(buffer.toString(), params, UserExport.class);
            for (int i = 0; i < users.size(); i++) {
                UserExport userColumn = users.get(i);

                String[] cells = new String[12];

                cells[0] = String.valueOf(i + 1);
                cells[1] = userColumn.getName();
                cells[2] = userColumn.getSex();
                if(userColumn.getBirthday()==null){
                	cells[3] = "";
                }else{
                	cells[3] = sdf.format(userColumn.getBirthday());
                }
                cells[4] = userColumn.getEthnic();
                cells[5] = userColumn.getIdentificationNumber();
                cells[6] = userColumn.getEducation();
                cells[7] = userColumn.getMobile();
                cells[8] = userColumn.getTelphone();
                cells[9] = userColumn.getAddress();
                cells[10] = userColumn.getJobId();
                if(userColumn.getHireDate()==null){
                	 cells[11] = "";
                }else{
                	 cells[11] = sdf.format(userColumn.getHireDate());
                }
                /*cells[3] = userColumn.getDeptId();
                cells[5] = userColumn.getWorkType();
                cells[11] = userColumn.getBasicSalary();
                cells[12] = userColumn.getEmergencyContact();
                cells[13] = userColumn.getEmergencyContactPhone();*/
                        
                rows.add(cells);
            }
        } catch (Exception e) {
            throw new CodeException(e.getMessage());
        }
        return rows;
    }

    private Column[] getColumn() {
        Column[] columns = new Column[12];

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
        columns[7].setAlign(Column.ALIGN_CENTER);
        columns[7].setWidth(6000);

        columns[8] = new Column();
        columns[8].setAlign(Column.ALIGN_CENTER);
        columns[8].setWidth(6000);
        
        columns[9] = new Column();
        columns[9].setAlign(Column.ALIGN_CENTER);
        columns[9].setWidth(8000);
        
        columns[10] = new Column();
        columns[10].setAlign(Column.ALIGN_CENTER);
        columns[10].setWidth(6000);
        
        columns[11] = new Column();
        columns[11].setAlign(Column.ALIGN_CENTER);
        columns[11].setWidth(8000);
        
        /*columns[12] = new Column();
        columns[12].setAlign(Column.ALIGN_CENTER);
        columns[12].setWidth(8000);
        
        columns[13] = new Column();
        columns[13].setAlign(Column.ALIGN_CENTER);
        columns[13].setWidth(8000);*/

        return columns;
    }
    
    @RequestMapping("/getZzUser")
    @ResponseBody
    public Map<String,Object> getZzUser(){
    	Map<String,Object> r = new HashMap<String, Object>();
    	String uid = SecurityUtil.getUserId();
    	//判断是否为办公室人员
    	List<Role> roles = baseService.findMapBySql("select id  from tbl_role where code='office'", new Object[]{}, new Type[]{StringType.INSTANCE}, Role.class); 
    	if(roles.size()>0){
    		List<UserRole> gxs = baseService.findMapBySql("select id  from tbl_user_role where roleId='"+roles.get(0).getId()+"' and userId='"+uid+"'", new Object[]{}, new Type[]{StringType.INSTANCE}, UserRole.class); 
    		if(gxs.size()>0){
    			String sql = "SELECT name,deptName  "
					         + "FROM tbl_user "
			                 + "WHERE 1=1 and hireDate is not null and  DATE_SUB(DATE_ADD(hireDate, INTERVAL 2 MONTH),INTERVAL 1 WEEK)<now() "
			 				 + "and zzStatus ='0' and audit_status='10' ";
    			
    			List<User> u =baseService.findMapBySql(sql, new Object[]{}, new Type[]{StringType.INSTANCE}, User.class); 
    			if(u.size()>0){
    				String str = "近期需转正人员：";
    				for(User user:u){
    					str += user.getName()+"("+user.getDeptName()+")/";
    				}
    				r.put("message", str);
    				r.put("success", true);
    			}else{
    				r.put("message", "暂无需转正人员");
    				r.put("success", false);
    			}
    		}else{
    			r.put("message", "不是办公人员");
				r.put("success", false);
    		}
    	}else{
    		r.put("message", "无office角色");
			r.put("success", false);
    	}
    	//判断是否存在未读收文
    	long count = baseService.countBySql("select count(1) from tbl_writingslook lk where 1=1 and lk.lookid='"+uid+"' and lk.islook='0' and exists (select * from tbl_writings sx where sx.id = lk.writingid)");
    	if(count>0){
    		String str = " 您有"+count+"条未读收文，请及时查看!";
    		r.put("swxx", str);
    	}
    	//判断是否存在方案指定人员未审批数据
    	long countfa = baseService.countBySql("select count(1) from tbl_planApprove lk where 1=1 and lk.oneAuidt='"+uid+"' and lk.status='50' ");
    	if(countfa>0){
    		String str = " 您有"+countfa+"条指定人员方案审批未审核！";
    		r.put("fasp", str);
    	}
    	return r;
    };
    /**
	 * 离职按钮
	 * @author wzh
	 * @创建时间 2019年5月4日 上午11:08:10
	 * @return
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
	 */
    @RequestMapping(value = "/deleteLz/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteLz(@PathVariable("id") String id) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        User employee = this.get(id);
        employee.setAuditStatus("50");
        employee.setUpdateDateTime(new Date());
        userService.update(employee);
        UserLeave lzn = new UserLeave();
		PropertyUtils.copyProperties(lzn, employee);
		lzn.setUserId(employee.getId());
		
		List<UserLeave> lz = baseService.findBySql("select * from tbl_userLeave  where userId='" + lzn.getUserId() + "'", UserLeave.class);
		if(lz.size()<=0){
			lzn.setLeaveReason("办公室删除");
			lzn.setLeaveTime(new Date());
			lzn.setLeaveStatus("40");
			baseService.save(lzn);
		}
		 List<UserRole> r = baseService.findBySql("select * from tbl_user_role  where userID='" + lzn.getUserId() + "'", UserRole.class);
         for (UserRole u : r) {
        	 baseService.delete(u);
         }
        return new Result();
    }
	@RequestMapping("/getAuditDepts")
	@ResponseBody
	public Result getAuditDepts(){
		 String hql = "select id value,name data,pname pdata  from v_depts  ";
	     List<OptionsDept> funcs = baseService.findMapBySql(hql, new Object[]{}, new Type[]{StringType.INSTANCE}, OptionsDept.class);
	     return new Result(true, funcs);
	}
}
