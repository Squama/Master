package com.radish.master.service.impl;


import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Budget;
import com.radish.master.service.BudgetService;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

@Service("budgetService")
public class BudgetServiceImpl extends BaseServiceImpl implements BudgetService {
    
    @Resource
    private RuntimePageService runtimePageService;

    @Override
    public Budget getBudgetByNo(String budgetNo){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("budgetNo", budgetNo);
        return this.get("from Budget where budgetNo=:budgetNo", params);
    }

    @Override
    public Boolean checkTxUnique(String projectID, String regionID, String matNumber) {
        String hql = "from BudgetTx where projectID=:projectID AND regionID=:regionID AND matNumber=:matNumber";
        Map<String, Object> params = new HashMap<>();
        params.put("projectID", projectID);
        params.put("regionID", regionID);
        params.put("matNumber", matNumber);
        
        return this.get(hql, params) == null;
    }

    @Override
    public Result startFlow(Budget budget, String processDefinitionKey) {
        budget.setStatus("20");
        budget.setUpdateDateTime(new Date());
        
        this.update(budget);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "申请预算：" + budget.getBudgetName() + "，所属项目：" + budget.getProjectID();
        
        //businessKey
        String businessKey = budget.getBudgetNo();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, budget.getBudgetNo());
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables,
                user.getId(), businessKey);
    }
}
