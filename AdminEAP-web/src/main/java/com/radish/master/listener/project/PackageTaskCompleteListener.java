/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener.project;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.radish.master.entity.project.Package;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年8月9日    Create this file
* </pre>
* 
*/

public class PackageTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = 1989439159566998280L;

    /**
     * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName)){
             String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
             BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
             Map<String, Object> params = new HashMap<String, Object>();
             params.put("id", businessKey);
             Package pack = baseService.get("from Package where id=:id", params);
             
             if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
                 pack.setStatus("30");
             }else{
                 pack.setStatus("10");
             }
             
             baseService.save(pack);
        }

    }

}
