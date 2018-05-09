/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener.project;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.common.ActivitiSuggestion;
import com.radish.master.entity.project.Salary;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年5月8日    Create this file
* </pre>
* 
*/

public class ManagerSalaryTaskCompleteListener implements TaskListener{
    private static final long serialVersionUID = 8633012790450639220L;

    private static final String TRUE = "true";

    private static final String FALSE = "false";

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            String suggestion = delegateTask.getVariable("suggestion").toString();
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");

            Salary salary = baseService.get(Salary.class, businessKey);

            String suggestionHql = "from ActivitiSuggestion where businessKey=:businessKey AND taskNode=:taskNode";
            Map<String, Object> suggestionParams = new HashMap<>();
            suggestionParams.put("businessKey", businessKey);
            suggestionParams.put("taskNode", taskDefinitionKey);
            ActivitiSuggestion as = baseService.get(suggestionHql, suggestionParams);
            
            if(as == null){
                as = new ActivitiSuggestion();
                as.setCreateDateTime(new Date());
                as.setUpdateDateTime(new Date());
                as.setBusinessKey(businessKey);
                as.setTaskNode(taskDefinitionKey);
                as.setApprove("true");
            }
            
            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                salary.setStatus("70");
                as.setApprove("false");
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                if ("fuzeren".equals(delegateTask.getTaskDefinitionKey())) {
                    salary.setStatus("30");
                } else if ("bangongshi".equals(delegateTask.getTaskDefinitionKey())) {
                    salary.setStatus("40");
                } else if ("boss".equals(delegateTask.getTaskDefinitionKey())) {
                    salary.setStatus("50");
                } else if ("account".equals(delegateTask.getTaskDefinitionKey())) {
                    salary.setStatus("60");
                }
            }

            baseService.save(salary);
            as.setSuggestion(suggestion);
            as.setOperator(SecurityUtil.getUser().getName());
            as.setUpdateDateTime(new Date());
            baseService.save(as);

        }
        
    }
}
