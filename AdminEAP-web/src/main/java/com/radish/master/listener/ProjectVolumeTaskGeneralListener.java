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
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
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

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if (EVENTNAME_COMPLETE.equals(eventName)) {
            String businessKey = delegateTask.getVariable(Constants.VAR_BUSINESS_KEY).toString();
            BaseService baseService = (BaseService) SpringUtil.getObject("baseActServer");

            ProjectVolume pv = baseService.get(ProjectVolume.class, businessKey);

            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                if ("zhiliang".equals(delegateTask.getTaskDefinitionKey())) {
                    delegateTask.setVariable(ZHILIANG, FALSE);
                } else if ("shigong".equals(delegateTask.getTaskDefinitionKey())) {
                    delegateTask.setVariable(SHIGONG, FALSE);
                } else if ("anquan".equals(delegateTask.getTaskDefinitionKey())) {
                    delegateTask.setVariable(ANQUAN, FALSE);
                }
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                if ("zhiliang".equals(delegateTask.getTaskDefinitionKey())) {
                    delegateTask.setVariable(ZHILIANG, TRUE);
                } else if ("shigong".equals(delegateTask.getTaskDefinitionKey())) {
                    delegateTask.setVariable(SHIGONG, TRUE);
                } else if ("anquan".equals(delegateTask.getTaskDefinitionKey())) {
                    delegateTask.setVariable(ANQUAN, TRUE);
                }
                if (delegateTask.getVariable(ZHILIANG) != null && delegateTask.getVariable(SHIGONG) != null && delegateTask.getVariable(ANQUAN) != null
                        && TRUE.equals(delegateTask.getVariable(ZHILIANG).toString()) && TRUE.equals(delegateTask.getVariable(SHIGONG).toString())
                        && TRUE.equals(delegateTask.getVariable(ANQUAN).toString())) {
                    pv.setStatus("30");
                }
            }
            
            if ("fuzeren".equals(delegateTask.getTaskDefinitionKey())) {
                pv.setStatus("40");
            } else if ("yusuan".equals(delegateTask.getTaskDefinitionKey())) {
                pv.setStatus("50");
            } else if ("boss".equals(delegateTask.getTaskDefinitionKey())) {
                pv.setStatus("60");
            } else if ("account".equals(delegateTask.getTaskDefinitionKey())) {
                //在流程审核完成时，统计该合同下的各次工程量上报金额，记录到合同的消耗字段中。 即tbl_labor新加的字段
            	
            	String hql = "from ProjectVolume where laborID=:laborID AND status=:status";
                Map<String, Object> params = new HashMap<>();
                params.put("laborID", pv.getLaborID());
                params.put("status", "70");
            	List<ProjectVolume> pvList = baseService.find(hql, params);
            	BigDecimal sum = new BigDecimal("0");
            	for(ProjectVolume proV : pvList){
            		BigDecimal consume = new BigDecimal(proV.getEngineerMoney());
                    
                    sum = sum.add(consume);
            	}
            	
            	BigDecimal thisVolume = new BigDecimal(pv.getEngineerMoney());
            	sum = sum.add(thisVolume);
            	
            	Labor labor = baseService.get(Labor.class, pv.getLaborID());
            	labor.setConsumePrice(sum.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
            	
            	baseService.save(labor);
            	
                pv.setStatus("70");
            }

            baseService.save(pv);

        }

    }

}
