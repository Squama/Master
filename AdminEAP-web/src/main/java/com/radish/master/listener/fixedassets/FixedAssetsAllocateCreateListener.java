/**
 * 
 */
package com.radish.master.listener.fixedassets;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.fixedassets.FixedAssetsAllocate;
import com.radish.master.system.SpringUtil;

/**
 * @author tonyd
 *
 */
public class FixedAssetsAllocateCreateListener implements TaskListener {

	private static final long serialVersionUID = -5401355470678177086L;

	@Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        
        if (EVENTNAME_CREATE.equals(eventName)) {
        	String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");
            
            FixedAssetsAllocate fixedAssetsAllocate = baseService.get(FixedAssetsAllocate.class, businessKey);
            
            if ("source".equals(taskDefinitionKey)) {
            	delegateTask.setAssignee(fixedAssetsAllocate.getSourceManager());
            }else if ("target".equals(taskDefinitionKey)) {
            	delegateTask.setAssignee(fixedAssetsAllocate.getTargetManager());
            } 
            
        }
        
    }

}
