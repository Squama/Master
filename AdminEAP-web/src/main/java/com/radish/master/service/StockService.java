package com.radish.master.service;


import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Project;


public interface StockService extends BaseService {

    Boolean saveHistory(String project_ID,String mat_ID,int stockChangeNum,String useTpye,String stockSource,String remark);
    Boolean stockChange(String budget_ID,String mat_ID,int stockChangeNum,int changeType);
    Project getProjectByBudget(String budget_ID);
}
