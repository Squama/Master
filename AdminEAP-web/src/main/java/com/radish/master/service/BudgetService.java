package com.radish.master.service;


import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Budget;

public interface BudgetService extends BaseService {

    Budget getBudgetByID(String ID);

}
