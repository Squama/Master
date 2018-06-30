/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener.qualityCheck;

import java.util.Date;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.qualityCheck.CheckDq;
import com.radish.master.entity.skillManage.PlanApprove;
import com.radish.master.system.SpringUtil;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年3月8日    Create this file
 * </pre>
 * 
 */

public class PlanAppTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = -665720800903137257L;
    private static final String TRUE = "true";

    private static final String FALSE = "false";
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");

            PlanApprove pv = baseService.get(PlanApprove.class, businessKey);
            
            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	 String suggestion = delegateTask.getVariable("suggestion").toString();
            	 pv.setStatus("40");
            	 pv.setBhdesc(suggestion);
            }else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	pv.setStatus("30");
            	pv.setBhdesc("");
            }
            
            baseService.update(pv);
           

        }

    }
}
