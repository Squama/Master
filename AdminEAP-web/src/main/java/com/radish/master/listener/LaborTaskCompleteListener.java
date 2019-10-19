/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.common.ActivitiReview;
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

public class LaborTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = 4294247719992051739L;
    private static final String TRUE = "true";

    private static final String FALSE = "false";

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName)){
        	String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            String suggestion = delegateTask.getVariable("suggestion").toString();
            String risk = delegateTask.getVariable("risk").toString();
            
             BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
             Map<String, Object> params = new HashMap<String, Object>();
             params.put("id", businessKey);
             Labor labor = baseService.get("from Labor where id=:id", params);
             
             
             String suggestionHql = "from ActivitiReview where businessKey=:businessKey AND taskNode=:taskNode";
             Map<String, Object> suggestionParams = new HashMap<>();
             suggestionParams.put("businessKey", businessKey);
             suggestionParams.put("taskNode", taskDefinitionKey);
             ActivitiReview as = baseService.get(suggestionHql, suggestionParams);
             
             if(as == null){
                 as = new ActivitiReview();
                 as.setCreateDateTime(new Date());
                 as.setUpdateDateTime(new Date());
                 as.setBusinessKey(businessKey);
                 as.setTaskNode(taskDefinitionKey);
                 as.setApprove("true");
             }
             as.setSuggestion(suggestion);
             as.setRisk(risk);
             as.setOperator(SecurityUtil.getUser().getName());
             as.setUpdateDateTime(new Date());
             baseService.save(as);
             //10=新建,20=审核中,30=审核通过,40=审核不通过,50=综合评审,60=项目经理总结,70=总经理评审
             if ("boss".equals(taskDefinitionKey)) {
                 if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                	 labor.setStatus("40");
             		labor.setBoyy(suggestion);
             	}else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
             		labor.setStatus("30");
             	}
             } else if ("jinyingF".equals(taskDefinitionKey)) {
             	if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
             		labor.setStatus("40");
             		labor.setBoyy(delegateTask.getVariable("conclusion").toString());
             	}else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
             		labor.setStatus("70");
             	}
                 
             }
             baseService.save(labor);
        }

    }
    
}
