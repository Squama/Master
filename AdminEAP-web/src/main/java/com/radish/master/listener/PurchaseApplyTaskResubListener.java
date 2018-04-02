/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener;

import java.math.BigDecimal;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Purchase;
import com.radish.master.entity.PurchaseApplyAudit;
import com.radish.master.service.PurchaseService;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年1月4日    Create this file
* </pre>
* 
*/

public class PurchaseApplyTaskResubListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName)){
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
            Purchase purchase = baseService.get(Purchase.class, businessKey);
            if("false".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
                //此处放弃之后，是不是应该把申请单数据删除，并加回申请数量？
                purchase.setStatus("10");
            }else if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
                purchase.setStatus("20");
                //判断是否数量超限，更新变量
                PurchaseService purchaseService = (PurchaseService)SpringUtil.getObject("purchaseActServer");
                List<PurchaseApplyAudit> list = purchaseService.getQuantityList(businessKey);
                String result = "false";
                for(int i=0;i<list.size();i++){
                    
                    BigDecimal budgetCount = new BigDecimal(list.get(i).getBudget()==null?"0":list.get(i).getBudget());
                    BigDecimal costCount = new BigDecimal(list.get(i).getCost()==null?"0":list.get(i).getCost());
                    BigDecimal applyCount = new BigDecimal(list.get(i).getApply());
                    
                    if(applyCount.add(costCount).compareTo(budgetCount) != -1){
                        result = "true";
                        break;
                    }
                    
                }
                delegateTask.setVariable("isAudit", result);
            }
            
            baseService.save(purchase);
             
        }

    }

}
