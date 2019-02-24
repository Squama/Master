/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener;

import java.util.Date;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.qualityCheck.CheckDq;
import com.radish.master.entity.safty.AqCheck;
import com.radish.master.entity.safty.Aqjy;
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

public class AqjyTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = -665720800903137257L;
    
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");

            Aqjy pv = baseService.get(Aqjy.class, businessKey);
            
           
            if ("bz".equals(taskDefinitionKey)) {
                pv.setStatus("30");
                pv.setBzjyr(SecurityUtil.getUser().getName());
                pv.setBztime(new Date());
            } else if ("xm".equals(taskDefinitionKey)) {
            	pv.setStatus("40");
                pv.setXmjyr(SecurityUtil.getUser().getName());
                pv.setXmtime(new Date());
            }else if ("gs".equals(taskDefinitionKey)) {
            	pv.setStatus("50");
                pv.setGsjyr(SecurityUtil.getUser().getName());
                pv.setGstime(new Date());
            }
            baseService.update(pv);
           

        }

    }
}
