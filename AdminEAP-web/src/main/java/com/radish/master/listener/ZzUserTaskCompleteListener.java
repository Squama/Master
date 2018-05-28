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

public class ZzUserTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = 4294247719992051739L;

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName)){
             String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
             BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
             Map<String, Object> params = new HashMap<String, Object>();
             params.put("id", businessKey);
             User u = baseService.get(User.class, businessKey);
             String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
             String suggestion = delegateTask.getVariable("suggestion").toString();
             if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
            	 if("bangongshi".equals(taskDefinitionKey)){
                	 u.setZzStatus("30");
                	 u.setZzView(suggestion);
                 }else if ("fuboss".equals(taskDefinitionKey)) {
                	 u.setZzStatus("40");
                 } else if ("boss".equals(taskDefinitionKey)) {
                	 u.setZzStatus("50");
                 }
             }else if("false".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
            	 u.setZzStatus("60");
            	 u.setZzFaildDesc(suggestion);
             }
            
             
             baseService.update(u);
             
             
        }

    }
    
}
