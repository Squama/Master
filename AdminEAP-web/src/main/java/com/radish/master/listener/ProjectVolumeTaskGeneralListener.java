/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.listener;


import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.Purchase;
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
            Purchase purchase = baseService.get(Purchase.class, businessKey);
            baseService.save(purchase);

            ProjectVolume pv = baseService.get(ProjectVolume.class, businessKey);
            System.out.println(delegateTask.getId());

            if (FALSE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                if ("zhiliang".equals(delegateTask.getId())) {
                    delegateTask.setVariable(ZHILIANG, FALSE);
                } else if ("shigong".equals(delegateTask.getId())) {
                    delegateTask.setVariable(SHIGONG, FALSE);
                } else if ("anquan".equals(delegateTask.getId())) {
                    delegateTask.setVariable(ANQUAN, FALSE);
                }
            } else if (TRUE.equalsIgnoreCase(delegateTask.getVariable("approved").toString())) {
                if ("zhiliang".equals(delegateTask.getId())) {
                    delegateTask.setVariable(ZHILIANG, TRUE);
                } else if ("shigong".equals(delegateTask.getId())) {
                    delegateTask.setVariable(SHIGONG, TRUE);
                } else if ("anquan".equals(delegateTask.getId())) {
                    delegateTask.setVariable(ANQUAN, TRUE);
                }
                if (TRUE.equals(delegateTask.getVariable(ZHILIANG).toString())
                        && TRUE.equals(delegateTask.getVariable(SHIGONG).toString())
                        && TRUE.equals(delegateTask.getVariable(ANQUAN).toString())) {
                    pv.setStatus("30");
                }
            }else{
                if ("fuzeren".equals(delegateTask.getId())) {
                    pv.setStatus("40");
                } else if ("yusuan".equals(delegateTask.getId())) {
                    pv.setStatus("50");
                } else if ("boss".equals(delegateTask.getId())) {
                    pv.setStatus("60");
                }else if ("account".equals(delegateTask.getId())) {
                    pv.setStatus("70");
                }
            }

            baseService.save(purchase);

        }

    }

}
