package com.radish.master.service.impl;


import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.radish.master.entity.Tbl_budget;
import com.radish.master.service.BudgetService;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("budgitService")
public class BudgitServiceImpl extends BaseServiceImpl implements BudgetService {

    @Override
    public Tbl_budget getBudgetByID(String id){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return this.get("from budget where budget_ID=:id", params);
    }
}
