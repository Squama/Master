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
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Labor;
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

public class ProjectPayDetTaskCompleteListener implements TaskListener {

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

            ProjectPayDet mx = baseService.get(ProjectPayDet.class, businessKey);
    		ProjectPay zf = baseService.get(ProjectPay.class, mx.getProjectPayId());

            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	mx.setStatus("40");
            	mx.setBhdesc(suggestion);
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            	mx.setStatus("30");
            	mx.setBhdesc("");
            	baseService.update(mx);
            	boolean is = true;
            	List<ProjectPayDet> mxs = baseService.find(" from ProjectPayDet where projectPayId='"+mx.getProjectPayId()+"'");
            	
            	for(ProjectPayDet m :mxs){
            		if(m.getId().equals(businessKey)){
            			continue;
            		}
            		if(!"30".equals(m.getStatus())){//还有未完成状态的明细
            			is = false;
            		}
            	}
            	if(is){//全部完成
            		zf.setStatus("50");
            	}else{//未完成
            		zf.setStatus("40");
            	}
            	baseService.update(zf);
            }

           
            

        }

    }

}
