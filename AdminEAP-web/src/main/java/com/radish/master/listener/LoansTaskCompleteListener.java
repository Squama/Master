package com.radish.master.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.volumePay.Loans;
import com.radish.master.system.SpringUtil;

public class LoansTaskCompleteListener implements TaskListener {
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
            
            Loans jk = baseService.get(Loans.class, businessKey);
            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	if ("auidt".equals(taskDefinitionKey)) {
            		jk.setStatus("50");
            		jk.setBossyj(suggestion);
                    jk.setBoss(SecurityUtil.getUser().getName());
            	}else if("cwfzr".equals(taskDefinitionKey)){
            		jk.setStatus("40");
            		jk.setCwshyj(suggestion);
                    jk.setCwshr(SecurityUtil.getUser().getName());
            	}
            	
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	if ("auidt".equals(taskDefinitionKey)) {
            		jk.setStatus("60");
            		jk.setBossyj(suggestion);
                    jk.setBoss(SecurityUtil.getUser().getName());
            	}else if("cwfzr".equals(taskDefinitionKey)){
            		jk.setStatus("100");
            		jk.setCwshyj(suggestion);
                    jk.setCwshr(SecurityUtil.getUser().getName());
            	}
            }
            
            baseService.update(jk);
        }
		
	}

}
