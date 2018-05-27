/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.entity.UserRole;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.UserLeave;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.SalaryVolume;
import com.radish.master.entity.volumePay.VolumePay;
import com.radish.master.system.Arith;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月13日    Create this file
* </pre>
* 
*/

public class LeaveUserTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = 4294247719992051739L;

    @Override
    public void notify(DelegateTask delegateTask) {
    	Arith ari = new Arith();
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName)){
             String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
             BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
             Map<String, Object> params = new HashMap<String, Object>();
             params.put("id", businessKey);
             UserLeave zf = baseService.get("from UserLeave where id=:id", params);
             String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
             
             if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
            	 if("bangongshi".equals(taskDefinitionKey)){
                	 zf.setLeaveStatus("20");
                 }else if ("fuboss".equals(taskDefinitionKey)) {
                	 zf.setLeaveStatus("30");
                 } else if ("boss".equals(taskDefinitionKey)) {
                	 zf.setLeaveStatus("40");
                	 //删除用户
                	 /*User employee = baseService.get(User.class, zf.getUserId());
                	 List<UserRole> r = baseService.findBySql("select * from tbl_user_role  where userID='" + zf.getUserId() + "'", UserRole.class);
                     for (UserRole u : r) {
                    	 baseService.delete(u);
                     }
                     baseService.delete(employee);*/
                	 User employee = baseService.get(User.class, zf.getUserId());
                	 employee.setAuditStatus("50");
                	 baseService.update(employee);
                 }
             }else if("false".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
            	 zf.setLeaveStatus("50");
            	 User u = baseService.get(User.class, zf.getUserId());
            	 u.setAuditStatus("10");
            	 baseService.update(u);
             }
             String suggestion = delegateTask.getVariable("suggestion").toString();
             if("bangongshi".equals(taskDefinitionKey)){
            	 zf.setLeaveView(suggestion);
             }
             baseService.save(zf);
             
             
        }

    }
    
}
