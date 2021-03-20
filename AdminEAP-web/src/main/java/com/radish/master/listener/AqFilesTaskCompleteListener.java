package com.radish.master.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.common.ActivitiSuggestion;
import com.radish.master.entity.project.Notice;
import com.radish.master.entity.safty.CheckRecordAQ;
import com.radish.master.system.SpringUtil;

public class AqFilesTaskCompleteListener implements TaskListener{


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
             BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
             
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
             as.setSuggestion(suggestion);
             as.setOperator(SecurityUtil.getUser().getName());
             as.setUpdateDateTime(new Date());
             baseService.save(as);
             
             Map<String, Object> params = new HashMap<String, Object>();
             params.put("id", businessKey);
             CheckRecordAQ zf = baseService.get("from CheckRecordAQ where id=:id", params);
             if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	 zf.setStatus("40");
            	 zf.setReserve2(suggestion);
             }else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	 zf.setStatus("30");
             }
             baseService.update(zf);
             
             
             
        }

    }
}
