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

public class CheckDqTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = -665720800903137257L;
    
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");

            CheckDq pv = baseService.get(CheckDq.class, businessKey);
            
           
            if ("xmb".equals(taskDefinitionKey)) {
                pv.setStatus("30");
                pv.setHfrq(new Date());
                pv.setHfr(SecurityUtil.getUser().getName());
            } else if ("gcbfc".equals(taskDefinitionKey)) {
                pv.setStatus("40");
            }
            baseService.update(pv);
           

        }

    }
}
