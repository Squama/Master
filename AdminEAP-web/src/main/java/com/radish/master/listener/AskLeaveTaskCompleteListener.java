package com.radish.master.listener;

import java.math.BigDecimal;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.AskLeave;
import com.radish.master.entity.volumePay.Loans;
import com.radish.master.system.SpringUtil;

public class AskLeaveTaskCompleteListener implements TaskListener {
	 private static final long serialVersionUID = -665720800903137257L;

	    private static final String TRUE = "true";

	    private static final String FALSE = "false";
	@Override
	public void notify(DelegateTask delegateTask) {
		String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
        	String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String suggestion = delegateTask.getVariable("suggestion").toString();
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            
            AskLeave jk = baseService.get(AskLeave.class, businessKey);
            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	if ("boss".equals(taskDefinitionKey)) {
            		jk.setStatus("70");
            		jk.setBossyj(suggestion);
                    jk.setBoss(SecurityUtil.getUser().getName());
            	}else if("bgs".equals(taskDefinitionKey)){
            		jk.setStatus("50");
            		jk.setBgsyj(suggestion);
                    jk.setBgsshr(SecurityUtil.getUser().getName());
            	}
            	
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	if ("boss".equals(taskDefinitionKey)) {
            		jk.setStatus("80");
            		jk.setBossyj(suggestion);
                    jk.setBoss(SecurityUtil.getUser().getName());
            	}else if("bgs".equals(taskDefinitionKey)){
            		if(new BigDecimal("1").compareTo(new BigDecimal(jk.getAlldays()))<0){//大于一天
            			jk.setStatus("60");
            		}else{
            			jk.setStatus("80");
            		}
            		jk.setBgsyj(suggestion);
                    jk.setBgsshr(SecurityUtil.getUser().getName());
            	}
            }
            
            baseService.update(jk);
        }
		
	}

}
