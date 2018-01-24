/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Dispatch;
import com.radish.master.entity.Purchase;
import com.radish.master.entity.PurchaseDet;
import com.radish.master.service.PurchaseService;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年1月9日    Create this file
* </pre>
* 
*/

public class PurchaseApplyTaskAmountAuditListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName)){
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
            Purchase purchase = baseService.get(Purchase.class, businessKey);
            
            if("false".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
                //渠道再编辑
                purchase.setStatus("35");
            }else if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
                purchase.setStatus("40");
                //更新调度表到20
                PurchaseService purchaseService = (PurchaseService)SpringUtil.getObject("purchaseActServer");
                Dispatch dispatch = purchaseService.getDispatchByProAndPur("402880e860c947ea0160ca0239670000", purchase.getProjectID(), purchase.getId());
                if(dispatch != null){
                    dispatch.setStatus("20");
                    purchaseService.save(dispatch);
                }
            }
            baseService.save(purchase);
        }

    }
}
