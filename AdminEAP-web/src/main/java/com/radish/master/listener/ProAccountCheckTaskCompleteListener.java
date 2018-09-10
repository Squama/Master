/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProAccountDet;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.common.ActivitiSuggestion;
import com.radish.master.entity.volumePay.ProjectPay;
import com.radish.master.entity.volumePay.ProjectPayDet;
import com.radish.master.system.SpringUtil;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年3月8日    Create this file
 * </pre>
 * 
 */

public class ProAccountCheckTaskCompleteListener implements TaskListener {

    private static final long serialVersionUID = -665720800903137257L;

    private static final String TRUE = "true";

    private static final String FALSE = "false";


    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String suggestion = delegateTask.getVariable("suggestion").toString();
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");

            ProAccountDet mx = baseService.get(ProAccountDet.class, businessKey);
            User u = SecurityUtil.getUser();
            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	mx.setStatus("40");
            	mx.setDescs(suggestion);
            	mx.setAuditId(u.getId());
            	mx.setAuditName(u.getName());
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	mx.setStatus("30");
            	mx.setDescs(suggestion);
            	mx.setAuditId(u.getId());
            	mx.setAuditName(u.getName());
            }

            baseService.update(mx);
            

        }

    }

}
