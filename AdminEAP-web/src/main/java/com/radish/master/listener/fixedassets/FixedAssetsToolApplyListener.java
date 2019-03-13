/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.listener.fixedassets;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.common.ActivitiSuggestion;
import com.radish.master.entity.fixedassets.FixedAssetsPur;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2019年3月13日    Create this file
* </pre>
* 
*/

public class FixedAssetsToolApplyListener implements TaskListener {

    private static final long serialVersionUID = -8426335481391123598L;

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

            FixedAssetsPur fixedAssetsPur = baseService.get(FixedAssetsPur.class, businessKey);

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
                fixedAssetsPur.setStatus("10");
                as.setApprove("false");
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                if ("xiangmujingli".equals(delegateTask.getTaskDefinitionKey())) {
                    fixedAssetsPur.setStatus("22");
                }else if ("zongjingli".equals(delegateTask.getTaskDefinitionKey())) {
                    fixedAssetsPur.setStatus("23");
                } else if ("caigouyuan".equals(delegateTask.getTaskDefinitionKey())) {
                    fixedAssetsPur.setStatus("24");
                }else if ("chengben".equals(delegateTask.getTaskDefinitionKey())) {
                    fixedAssetsPur.setStatus("40");
                }
            }

            baseService.save(fixedAssetsPur);
            as.setSuggestion(suggestion);
            as.setOperator(SecurityUtil.getUser().getName());
            as.setUpdateDateTime(new Date());
            baseService.save(as);

        }
        
    }

}
