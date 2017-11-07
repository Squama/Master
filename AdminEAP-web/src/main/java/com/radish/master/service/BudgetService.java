package com.radish.master.service;


import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Tbl_budget;

public interface BudgetService extends BaseService {

    Tbl_budget getBudgetByID(String ID);

}
