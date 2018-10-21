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
import com.radish.master.entity.volumePay.Reimburse;
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

public class ReimburseTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = -665720800903137257L;
    
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");

            Reimburse bx = baseService.get(Reimburse.class, businessKey);
            String suggestion = delegateTask.getVariable("suggestion").toString();
            if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
            	if ("cw".equals(taskDefinitionKey)) {
            		bx.setStatus("100");
            		bx.setShcw(SecurityUtil.getUser().getId());
            		bx.setShcwyj(suggestion);
                } else if ("boss".equals(taskDefinitionKey)) {
                	bx.setStatus("80");
                	bx.setBoss(SecurityUtil.getUser().getId());
                	bx.setBossyj(suggestion);
                }else if ("cwfzr".equals(taskDefinitionKey)){
                	bx.setStatus("60");
                	bx.setShcwfzr(SecurityUtil.getUser().getId());
                	bx.setShcwfzryj(suggestion);
                }
            }else{
            	if ("cw".equals(taskDefinitionKey)) {
            		bx.setStatus("50");
            		bx.setShcw(SecurityUtil.getUser().getId());
            		bx.setShcwyj(suggestion);
                } else if ("boss".equals(taskDefinitionKey)) {
                	bx.setStatus("70");
                	bx.setBoss(SecurityUtil.getUser().getId());
                	bx.setBossyj(suggestion);
                }else if ("cwfzr".equals(taskDefinitionKey)) {
                	bx.setStatus("110");
                	bx.setShcwfzr(SecurityUtil.getUser().getId());
                	bx.setShcwfzryj(suggestion);
                }
            }
            
            baseService.update(bx);
           

        }

    }
}
