/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener.reviews;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.alibaba.fastjson.JSONObject;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.entity.TaskWechat;
import com.cnpc.framework.base.entity.WechatConfig;
import com.cnpc.framework.base.pojo.AccessToken;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.CodeException;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.WeChatUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.common.ActivitiReview;
import com.radish.master.entity.common.ActivitiSuggestion;
import com.radish.master.entity.review.ReviewMat;
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

public class ReviewMatTaskGeneralListener implements TaskListener {

    private static final long serialVersionUID = -665720800903137257L;
    
    private static final Map<String, String> TASKMAP = new HashMap<String, String>(){{
        put("jinying", "经营部门");
        put("gongchen", "工程部门");
        put("bangong", "办公部门");
        put("caiwu", "财务部门");
        put("fawu", "法务部门");
        put("fuBoss", "副总经理");
        put("boss", "总经理");
        put("jinyingF", "经营科");
    }};
    private static final String TRUE = "true";

    private static final String FALSE = "false";
    
    private static final String JINYING = "jinyingApproved";

    private static final String GONGCHEN = "gongchenApproved";

    private static final String BANGONG = "bangongApproved";
    
    private static final String CAIWU = "caiwuApproved";
    
    private static final String FAWU = "fawuApproved";
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            String suggestion = delegateTask.getVariable("suggestion").toString();
            String risk = delegateTask.getVariable("risk").toString();
            
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");

            ReviewMat pv = baseService.get(ReviewMat.class, businessKey);
            
            String suggestionHql = "from ActivitiReview where businessKey=:businessKey AND taskNode=:taskNode";
            Map<String, Object> suggestionParams = new HashMap<>();
            suggestionParams.put("businessKey", businessKey);
            suggestionParams.put("taskNode", taskDefinitionKey);
            ActivitiReview as = baseService.get(suggestionHql, suggestionParams);
            
            if(as == null){
                as = new ActivitiReview();
                as.setCreateDateTime(new Date());
                as.setUpdateDateTime(new Date());
                as.setBusinessKey(businessKey);
                as.setTaskNode(taskDefinitionKey);
                as.setApprove("true");
            }
            /*if ("jinying".equals(taskDefinitionKey)) {
                delegateTask.setVariable(JINYING, TRUE);
            } else if ("gongchen".equals(taskDefinitionKey)) {
                delegateTask.setVariable(GONGCHEN, TRUE);
            } else if ("bangong".equals(taskDefinitionKey)) {
                delegateTask.setVariable(BANGONG, TRUE);
            }else if ("caiwu".equals(taskDefinitionKey)) {
                delegateTask.setVariable(CAIWU, TRUE);
            }else if ("fawu".equals(taskDefinitionKey)) {
                delegateTask.setVariable(FAWU, TRUE);
            }
            
            if (delegateTask.getVariable(JINYING) != null 
            		&& delegateTask.getVariable(GONGCHEN) != null 
            		&& delegateTask.getVariable(BANGONG) != null
    				&& delegateTask.getVariable(CAIWU) != null 
            		&& delegateTask.getVariable(FAWU) != null
                    && TRUE.equals(delegateTask.getVariable(JINYING).toString()) 
                    && TRUE.equals(delegateTask.getVariable(GONGCHEN).toString())
                    && TRUE.equals(delegateTask.getVariable(BANGONG).toString())
                    && TRUE.equals(delegateTask.getVariable(CAIWU).toString())
                    && TRUE.equals(delegateTask.getVariable(FAWU).toString())) {
                pv.setStatus("30");
            }*/
            as.setSuggestion(suggestion);
            as.setRisk(risk);
            as.setOperator(SecurityUtil.getUser().getName());
            as.setUpdateDateTime(new Date());
            if(!"jinyingF".equals(taskDefinitionKey)){
            	baseService.save(as);
            }else{
            	String conclusion = delegateTask.getVariable("conclusion").toString();
            	String manage = delegateTask.getVariable("manage").toString();
            	String joinName = delegateTask.getVariable("joinName").toString();
            	String remark =delegateTask.getVariable("remark").toString();
            	pv.setConclusion(conclusion);
            	pv.setManage(manage);
            	pv.setJoinName(joinName);
            	pv.setRemark(remark);
            	pv.setRisk(risk);
            }
            
            if ("fuBoss".equals(taskDefinitionKey)) {
                if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            		pv.setStatus("70");
            		pv.setBoyy(suggestion);
            	}else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            		pv.setStatus("40");
            	}
            } else if ("boss".equals(taskDefinitionKey)) {
                if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            		pv.setStatus("70");
            		pv.setBoyy(suggestion);
            	}else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            		pv.setStatus("60");
            	}
            } else if ("jinyingF".equals(taskDefinitionKey)) {
            	if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            		pv.setStatus("70");
            		pv.setBoyy(delegateTask.getVariable("conclusion").toString());
            	}else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
            		pv.setStatus("30");
            	}
                
            }

            
            
            baseService.update(pv);
           

        }

    }
}
