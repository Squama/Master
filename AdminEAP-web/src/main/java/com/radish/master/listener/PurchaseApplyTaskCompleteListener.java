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
import com.radish.master.entity.PurchaseApplyAudit;
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
* dongyan      2018年1月4日    Create this file
* </pre>
* 
*/

public class PurchaseApplyTaskCompleteListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName)){
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
            Purchase purchase = baseService.get(Purchase.class, businessKey);
            
            //TODO计算金额超限
            purchase.setStatus("35");
            delegateTask.setVariable("isAmountAudit", "true");
            
            if("false".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
                //终止怎么处理？
                purchase.setStatus("10");
            }else if("true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
                delegateTask.setVariable("isAmountAudit", "false");
                purchase.setStatus("40");
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("id", purchase.getId());
                List<PurchaseDet> detList = baseService.find("from PurchaseDet where purchaseID = :id", params);
                
                BigDecimal sum = new BigDecimal("0");
                
                for(PurchaseDet det : detList){
                    BigDecimal price = new BigDecimal(det.getPrice());
                    BigDecimal quantity = new BigDecimal(det.getQuantity());
                    
                    sum = sum.add(quantity.multiply(price));
                    
                }
                
                purchase.setApplyAmount(sum.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                
                //判断金额超限
                PurchaseService purchaseService = (PurchaseService)SpringUtil.getObject("purchaseActServer");
                List<PurchaseApplyAudit> list = purchaseService.getAmountList(businessKey);
                String result = "false";
                for(int i=0;i<list.size();i++){
                    
                    BigDecimal budgetCount = new BigDecimal(list.get(i).getBudget()==null?"0":list.get(i).getBudget());
                    BigDecimal costCount = new BigDecimal(list.get(i).getCost()==null?"0":list.get(i).getCost());
                    BigDecimal applyCount = new BigDecimal(list.get(i).getApply());
                    
                    if(applyCount.add(costCount).compareTo(budgetCount) != -1){
                        result = "true";
                        purchase.setStatus("35");
                        break;
                    }
                    
                }
                delegateTask.setVariable("isAmountAudit", result);
                //不超限就更新审核标志false并更新调度表20
                if(result == "false"){
                    // 更新调度表到20
                    Dispatch dispatch = purchaseService.getDispatchByProAndPur("402880e860c947ea0160ca0239670000", purchase.getProjectID(), purchase.getId());
                    if(dispatch != null){
                    	dispatch.setStatus("20");
                        purchaseService.save(dispatch);
                    }
                }
            }
            baseService.save(purchase);
            
        }

    }

}
