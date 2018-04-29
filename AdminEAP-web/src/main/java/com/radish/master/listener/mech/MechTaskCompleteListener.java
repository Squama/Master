/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener.mech;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.mech.MechBudget;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月9日    Create this file
* </pre>
* 
*/

public class MechTaskCompleteListener implements TaskListener{

    private static final long serialVersionUID = 7383477718722575983L;

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName)){
             String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
             BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
             Map<String, Object> params = new HashMap<String, Object>();
             params.put("id", businessKey);
             MechBudget mechBudget = baseService.get("from MechBudget where id=:id", params);
             
             if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
                 mechBudget.setStatus("30");
             }else if("false".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
                 mechBudget.setStatus("40");
             }
             
             baseService.save(mechBudget);
        }

    }

}
