/**
 * 版权所有 (c) 2019，中金支付有限公司  
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
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetEstimate;
import com.radish.master.entity.project.BudgetLabour;
import com.radish.master.entity.project.BudgetMech;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2019年12月30日    Create this file
* </pre>
* 
*/

public class AdditionTaskCompleteListener implements TaskListener{

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if(EVENTNAME_COMPLETE.equals(eventName) && "true".equalsIgnoreCase(delegateTask.getVariable("approved").toString())){
             String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
             BaseService baseService = (BaseService)SpringUtil.getObject("baseActServer");
             Map<String, Object> params = new HashMap<String, Object>();
             params.put("budgetNo", businessKey);
             Budget budget = baseService.get("from Budget where budgetNo=:budgetNo", params);
             
             //处理所有变更量
             
             Map<String, Object> paramsDet = new HashMap<String, Object>();
             paramsDet.put("budgetNo", businessKey);
             
             //材料
             
             List<BudgetEstimate> matList =  baseService.find("from BudgetEstimate where budgetNo = :budgetNo AND Type = '2'", params);
             
             for(BudgetEstimate mat : matList){
                 mat.setType("1");
                 String addition = mat.getAdditionQuantity();
                 if(addition.contains("+")){
                     addition = addition.substring(1, addition.length());
                 }
                 BigDecimal stock = new BigDecimal(mat.getQuantity());
                 BigDecimal add = new BigDecimal(addition);
                 
                 mat.setQuantity(stock.add(add).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
             }
             
             if(!matList.isEmpty()){
                 baseService.batchUpdate(matList);
             }
             
             //人力
             
             List<BudgetLabour> labourList =  baseService.find("from BudgetLabour where budgetNo = :budgetNo AND Type = '2'", params);
             
             for(BudgetLabour labour : labourList){
                 labour.setType("1");
                 String addition = labour.getAdditionQuantity();
                 if(addition.contains("+")){
                     addition = addition.substring(1, addition.length());
                 }
                 BigDecimal stock = new BigDecimal(labour.getLabourQuantity());
                 BigDecimal add = new BigDecimal(addition);
                 
                 labour.setLabourQuantity(stock.add(add).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
             }
             
             if(!labourList.isEmpty()){
                 baseService.batchUpdate(labourList);
             }
             
             //机械
             
             List<BudgetMech> mechList =  baseService.find("from BudgetMech where budgetNo = :budgetNo AND Type = '2'", params);
             
             for(BudgetMech mech : mechList){
                 mech.setType("1");
                 String addition = mech.getAdditionQuantity();
                 if(addition.contains("+")){
                     addition = addition.substring(1, addition.length());
                 }
                 BigDecimal stock = new BigDecimal(mech.getMechQuantity());
                 BigDecimal add = new BigDecimal(addition);
                 
                 mech.setMechQuantity(stock.add(add).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
             }
             
             if(!mechList.isEmpty()){
                 baseService.batchUpdate(mechList);
             }
             
             budget.setStatus("30");
             baseService.update(budget);
        }
    }
    
}
