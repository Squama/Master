package com.radish.master.listener.fixedassets;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.fixedassets.FixedMaintain;
import com.radish.master.entity.volumePay.Loans;
import com.radish.master.system.SpringUtil;

public class FixedMaintainTaskCompleteListener implements TaskListener {
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
            User u = SecurityUtil.getUser();
            FixedMaintain jk = baseService.get(FixedMaintain.class, businessKey);
            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	if ("boss".equals(taskDefinitionKey)) {
            		jk.setStatus("50");
            		jk.setBossyj(suggestion);
                    jk.setBoss(u.getLoginName());
            	}
            	
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	if ("boss".equals(taskDefinitionKey)) {
            		jk.setStatus("60");
            		jk.setBossyj(suggestion);
                    jk.setBoss(u.getLoginName());
            	}
            }
            if("sj".equals(taskDefinitionKey)){
        		jk.setStatus("70");
        		jk.setCwshr(u.getLoginName());
        	}
            baseService.update(jk);
        }
		
	}

}
