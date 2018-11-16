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
import com.radish.master.entity.project.Fee;
import com.radish.master.entity.project.MeasureConsume;
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

public class FeeTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = 2952485228544217646L;

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
             Fee fee = baseService.get("from Fee where id=:id", params);
             
             if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
                 fee.setStatus("30");
                 MeasureConsume mc = new MeasureConsume();
                 mc.setCreateDateTime(new Date());
                 mc.setConsumeName("规费上报审核消耗");
                 mc.setProjectID(fee.getProjectID());
                 mc.setProjectSubID(fee.getProjectSubID());
                 mc.setOperator(SecurityUtil.getUser().getName());
                 mc.setOperateTime(new Date());
                 mc.setType("rule");
                 mc.setAmount(fee.getAmount());
                 mc.setOp("-");
                 baseService.save(mc);
             }else{
                 fee.setStatus("10");
             }
             
             baseService.save(fee);
        }

    }

}
