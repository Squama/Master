/**
 * 
 */
package com.radish.master.listener.fixedassets;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.common.ActivitiSuggestion;
import com.radish.master.entity.fixedassets.FixedAssetsAllocate;
import com.radish.master.entity.fixedassets.FixedAssetsStk;
import com.radish.master.entity.fixedassets.FixedAssetsStkTx;
import com.radish.master.system.SpringUtil;

/**
 * @author tonyd
 *
 */
public class FixedAssetsAllocateCompleteListener implements TaskListener {

	private static final long serialVersionUID = -200919250195337005L;

	private static final String TRUE = "true";

    private static final String FALSE = "false";
	
	@Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            String suggestion = delegateTask.getVariable("suggestion").toString();
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");
            
            FixedAssetsAllocate fixedAssetsAllocate = baseService.get(FixedAssetsAllocate.class, businessKey);

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
            
            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	fixedAssetsAllocate.setStatus("10");
                as.setApprove("false");
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                if ("source".equals(delegateTask.getTaskDefinitionKey())) {
                	fixedAssetsAllocate.setStatus("30");
                }else if ("target".equals(delegateTask.getTaskDefinitionKey())) {
                	fixedAssetsAllocate.setStatus("40");
                } else if ("boss".equals(delegateTask.getTaskDefinitionKey())) {
                	fixedAssetsAllocate.setStatus("50");
                	//调库存
                	FixedAssetsStk sourceStk = baseService.get(FixedAssetsStk.class, fixedAssetsAllocate.getStkID());
                	if(Double.valueOf(fixedAssetsAllocate.getQuantity()) > Double.valueOf(sourceStk.getQuantityAvl())){
                		fixedAssetsAllocate.setStatus("60");
                    }else{
                    	BigDecimal allocate = new BigDecimal(fixedAssetsAllocate.getQuantity());
                    	BigDecimal quantity = new BigDecimal(sourceStk.getQuantity());
                    	BigDecimal quantityAvl = new BigDecimal(sourceStk.getQuantityAvl());
                    	
                    	String targetHql = "from FixedAssetsStk where belongedStock=:belongedStock AND name=:name AND norm=:norm AND unit=:unit";
                        Map<String, Object> targetParams = new HashMap<>();
                        targetParams.put("belongedStock", fixedAssetsAllocate.getTargetStock());
                        targetParams.put("name", fixedAssetsAllocate.getName());
                        targetParams.put("norm", fixedAssetsAllocate.getNorm());
                        targetParams.put("unit", fixedAssetsAllocate.getUnit());
                        List<FixedAssetsStk> targetList = baseService.find(targetHql, targetParams);
                        
                        sourceStk.setQuantity(quantity.subtract(allocate).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                        sourceStk.setQuantityAvl(quantityAvl.subtract(allocate).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                        
                        FixedAssetsStkTx sourceStkTx = new FixedAssetsStkTx();
                        sourceStkTx.setFixedAssetsID(sourceStk.getId());
                        sourceStkTx.setOperation("60");//调拨出库
                        sourceStkTx.setAmount(fixedAssetsAllocate.getQuantity());
                        sourceStkTx.setBalance(sourceStk.getQuantityAvl());
                        sourceStkTx.setPrice(sourceStk.getPrice());
                        sourceStkTx.setRemark("固定资产/工具调拨");
                        sourceStkTx.setOperator(SecurityUtil.getUser().getLoginName());
                        sourceStkTx.setOperateTime(new Date());
                        sourceStkTx.setSourceTxID(fixedAssetsAllocate.getId());
                        sourceStkTx.setNeedReturn("10");
                        
                        if(targetList.isEmpty()){
                        	FixedAssetsStk stk = new FixedAssetsStk();
                            stk.setName(sourceStk.getName());
                            stk.setEnglishName(sourceStk.getEnglishName());
                            stk.setModel(sourceStk.getModel());
                            stk.setBelongedStock(fixedAssetsAllocate.getTargetStock());
                            stk.setKeepedDeptID(sourceStk.getKeepedDeptID());
                            stk.setKeepedDeptName(sourceStk.getKeepedDeptName());
                            stk.setNorm(sourceStk.getNorm());
                            stk.setUnit(sourceStk.getUnit());
                            stk.setPrice(sourceStk.getPrice());
                            stk.setQuantity(fixedAssetsAllocate.getQuantity());
                            stk.setQuantityAvl(fixedAssetsAllocate.getQuantity());
                            stk.setVendor(sourceStk.getVendor());
                            stk.setFaType(sourceStk.getFaType());
                            
                            baseService.save(stk);
                            
                            FixedAssetsStkTx stkTx = new FixedAssetsStkTx();
                            stkTx.setFixedAssetsID(stk.getId());
                            stkTx.setOperation("70");//调拨入库
                            stkTx.setAmount(stk.getQuantity());
                            stkTx.setBalance(stk.getQuantityAvl());
                            stkTx.setPrice(stk.getPrice());
                            stkTx.setRemark("固定资产/工具调拨");
                            stkTx.setOperator(SecurityUtil.getUser().getLoginName());
                            stkTx.setOperateTime(new Date());
                            stkTx.setSourceTxID(fixedAssetsAllocate.getId());
                            stkTx.setNeedReturn("10");
                            
                            baseService.update(sourceStk);
                            baseService.save(stkTx);
                            baseService.save(sourceStkTx);
                            
                        }else{
                        	FixedAssetsStk stk = targetList.get(0);
                        	
                        	stk.setQuantity(quantity.add(allocate).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                        	stk.setQuantityAvl(quantityAvl.add(allocate).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                            
                            FixedAssetsStkTx stkTx = new FixedAssetsStkTx();
                            stkTx.setFixedAssetsID(stk.getId());
                            stkTx.setOperation("70");//调拨入库
                            stkTx.setAmount(fixedAssetsAllocate.getQuantity());
                            stkTx.setBalance(stk.getQuantityAvl());
                            stkTx.setPrice(stk.getPrice());
                            stkTx.setRemark("固定资产/工具调拨");
                            stkTx.setOperator(SecurityUtil.getUser().getLoginName());
                            stkTx.setOperateTime(new Date());
                            stkTx.setSourceTxID(fixedAssetsAllocate.getId());
                            
                            baseService.update(sourceStk);
                            baseService.update(stk);
                            baseService.save(stkTx);
                            baseService.save(sourceStkTx);
                        }
                    	
                    }
                	
                }
            }

            baseService.save(fixedAssetsAllocate);
            as.setSuggestion(suggestion);
            as.setOperator(SecurityUtil.getUser().getName());
            as.setUpdateDateTime(new Date());
            baseService.save(as);
        }
        
    }

}
