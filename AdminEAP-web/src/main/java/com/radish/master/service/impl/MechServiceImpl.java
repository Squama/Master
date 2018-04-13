/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.mech.MechBudget;
import com.radish.master.entity.mech.MechConsume;
import com.radish.master.pojo.Options;
import com.radish.master.service.MechService;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月9日    Create this file
 * </pre>
 * 
 */
@Service("mechService")
public class MechServiceImpl extends BaseServiceImpl implements MechService {
    
    @Resource
    private RuntimePageService runtimePageService;

    @Override
    public List<Options> getMechComboboxByProject(String projectID) {
        return this.findMapBySql("select id value, name data from tbl_mech_budget where project_id=? AND status=30", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getConsumeCombobox(String mechID) {
        return this.findMapBySql("select id value, name data from tbl_mech_budget_detail where entry_id=?", new Object[] { mechID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }
    
    @Override
    public Result startBudgetFlow(MechBudget mechBudget, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        mechBudget.setStatus("20");

        this.update(mechBudget);

        String name = "机械预算：" + mechBudget.getProjectName() + "【审核】";

        // businessKey
        String businessKey = mechBudget.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(),
                businessKey);
    }
    
    @Override
    public Result startConsumeFlow(MechConsume mechConsume, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        mechConsume.setStatus("20");

        this.update(mechConsume);

        String name = "机械预算：" + mechConsume.getProjectName() + "【工程量上报】";

        // businessKey
        String businessKey = mechConsume.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(),
                businessKey);
    }

}
