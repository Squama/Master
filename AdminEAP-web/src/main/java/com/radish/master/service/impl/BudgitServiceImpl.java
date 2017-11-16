package com.radish.master.service.impl;


import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.radish.master.entity.Budget;
import com.radish.master.service.BudgetService;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("budgitService")
public class BudgitServiceImpl extends BaseServiceImpl implements BudgetService {

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
}
