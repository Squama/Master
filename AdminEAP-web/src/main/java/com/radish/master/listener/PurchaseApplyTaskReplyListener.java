/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Purchase;
import com.radish.master.system.SpringUtil;

/**
* 退回重新编辑
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年1月4日    Create this file
* </pre>
* 
*/

public class PurchaseApplyTaskReplyListener implements TaskListener {

    private static final long serialVersionUID = -5449229131938290086L;

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName)){
             String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
             BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
             //数量超预算审核不通过
             if("false".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
                 Purchase purchase = baseService.get(Purchase.class, businessKey);
                 purchase.setStatus("10");
                 baseService.save(purchase);
             }else{
                 //审核通过
                 Purchase purchase = baseService.get(Purchase.class, businessKey);
                 purchase.setStatus("25");
                 baseService.save(purchase);
             }
        }
    }

}
