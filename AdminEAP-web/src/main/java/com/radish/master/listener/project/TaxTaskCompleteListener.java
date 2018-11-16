/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener.project;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.project.MeasureConsume;
import com.radish.master.entity.project.Tax;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年11月15日    Create this file
* </pre>
* 
*/

public class TaxTaskCompleteListener implements TaskListener{

    private static final long serialVersionUID = 2338230965848382749L;

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
             Tax tax = baseService.get("from Tax where id=:id", params);
             
             if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
                 tax.setStatus("30");
                 MeasureConsume mc = new MeasureConsume();
                 mc.setCreateDateTime(new Date());
                 mc.setConsumeName(tax.getName());
                 mc.setProjectID(tax.getProjectID());
                 mc.setProjectSubID(tax.getProjectSubID());
                 mc.setOperator(SecurityUtil.getUser().getName());
                 mc.setOperateTime(new Date());
                 mc.setType("tax");
                 mc.setAmount(tax.getAmount());
                 mc.setOp("-");
                 baseService.save(mc);
             }else{
                 tax.setStatus("10");
             }
             
             baseService.save(tax);
        }

    }
    
}
