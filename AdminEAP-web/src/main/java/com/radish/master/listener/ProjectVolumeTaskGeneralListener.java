/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.common.ActivitiSuggestion;
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

public class ProjectVolumeTaskGeneralListener implements TaskListener {

    private static final long serialVersionUID = -665720800903137257L;

    private static final String TRUE = "true";

    private static final String FALSE = "false";

    private static final String ZHILIANG = "zhiliangApproved";

    private static final String SHIGONG = "shigongApproved";

    private static final String ANQUAN = "anquanApproved";

    private static final Map<String, String> TASKMAP = new HashMap<String, String>() {
        {
            put("zhiliang", "质量员");
            put("shigong", "施工员");
            put("anquan", "安全员");
            put("fuzeren", "项目负责人");
            put("yusuan", "经营科");
            put("boss", "分公司主管");
            put("account", "财务人员");
        }
    };

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
            String suggestion = delegateTask.getVariable("suggestion").toString();
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");

            ProjectVolume pv = baseService.get(ProjectVolume.class, businessKey);

            String suggestionHql = "from ActivitiSuggestion where businessKey=:businessKey AND taskNode=:taskNode";
            Map<String, Object> suggestionParams = new HashMap<>();
            suggestionParams.put("businessKey", businessKey);
            suggestionParams.put("taskNode", taskDefinitionKey);
            ActivitiSuggestion as = baseService.get(suggestionHql, suggestionParams);

            if (as == null) {
                as = new ActivitiSuggestion();
                as.setCreateDateTime(new Date());
                as.setUpdateDateTime(new Date());
                as.setBusinessKey(businessKey);
                as.setTaskNode(taskDefinitionKey);
                as.setApprove("true");
            }

            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                if ("zhiliang".equals(taskDefinitionKey)) {
                    delegateTask.setVariable(ZHILIANG, FALSE);
                } else if ("shigong".equals(taskDefinitionKey)) {
                    delegateTask.setVariable(SHIGONG, FALSE);
                } else if ("anquan".equals(taskDefinitionKey)) {
                    delegateTask.setVariable(ANQUAN, FALSE);
                }
                as.setApprove("false");
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                if ("zhiliang".equals(taskDefinitionKey)) {
                    delegateTask.setVariable(ZHILIANG, TRUE);
                } else if ("shigong".equals(taskDefinitionKey)) {
                    delegateTask.setVariable(SHIGONG, TRUE);
                } else if ("anquan".equals(taskDefinitionKey)) {
                    delegateTask.setVariable(ANQUAN, TRUE);
                }
                if (delegateTask.getVariable(ZHILIANG) != null && delegateTask.getVariable(SHIGONG) != null && delegateTask.getVariable(ANQUAN) != null
                        && TRUE.equals(delegateTask.getVariable(ZHILIANG).toString()) && TRUE.equals(delegateTask.getVariable(SHIGONG).toString())
                        && TRUE.equals(delegateTask.getVariable(ANQUAN).toString())) {
                    pv.setStatus("30");
                }
            }

            if ("fuzeren".equals(taskDefinitionKey)) {
                pv.setStatus("40");
            } else if ("yusuan".equals(taskDefinitionKey)) {
                pv.setStatus("50");
            } else if ("boss".equals(taskDefinitionKey)) {
                pv.setStatus("60");
            } else if ("account".equals(taskDefinitionKey)) {
                // 在流程审核完成时，统计该合同下的各次工程量上报金额，记录到合同的消耗字段中。 即tbl_labor新加的字段
                /*
                String hql = "from ProjectVolume where laborID=:laborID AND status=:status";
                Map<String, Object> params = new HashMap<>();
                params.put("laborID", pv.getLaborID());
                params.put("status", "70");
                List<ProjectVolume> pvList = baseService.find(hql, params);
                BigDecimal sum = new BigDecimal("0");
                for (ProjectVolume proV : pvList) {
                    BigDecimal consume = new BigDecimal(proV.getFinalSub());

                    sum = sum.add(consume);
                }

                BigDecimal thisVolume = new BigDecimal(pv.getFinalSub());
                sum = sum.add(thisVolume);
                 
                
                labor.setConsumePrice(sum.setScale(2,
                BigDecimal.ROUND_DOWN).toPlainString());
                */
                
                Labor labor = baseService.get(Labor.class, pv.getLaborID());

                BigDecimal oldLabour = new BigDecimal(labor.getLabourConsume() == null? "0" : labor.getLabourConsume());
                BigDecimal oldMat = new BigDecimal(labor.getMatConsume() == null? "0" : labor.getMatConsume());
                BigDecimal oldMech = new BigDecimal(labor.getMechConsume() == null? "0" : labor.getMechConsume());
                BigDecimal oldPack = new BigDecimal(labor.getPackageConsume() == null? "0" : labor.getPackageConsume());
                
                BigDecimal thisLabour = new BigDecimal(pv.getFinalLabour());
                BigDecimal thisMat = new BigDecimal(pv.getFinalMat());
                BigDecimal thisMech = new BigDecimal(pv.getFinalMech());
                BigDecimal thisPack = new BigDecimal(pv.getFinalPack());
                
                labor.setLabourConsume(oldLabour.add(thisLabour).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                labor.setMatConsume(oldMat.add(thisMat).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                labor.setMechConsume(oldMech.add(thisMech).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                labor.setPackageConsume(oldPack.add(thisPack).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                
                
                baseService.save(labor);

                pv.setStatus("70");
            }

            baseService.save(pv);
            as.setSuggestion(suggestion);
            as.setOperator(SecurityUtil.getUser().getName());
            as.setUpdateDateTime(new Date());
            baseService.save(as);

        }

    }

}
