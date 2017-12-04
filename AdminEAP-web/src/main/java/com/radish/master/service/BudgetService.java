package com.radish.master.service;


import org.springframework.web.multipart.MultipartFile;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Budget;

public interface BudgetService extends BaseService {

    Budget getBudgetByNo(String batchNo);
    
    Boolean checkTxUnique(String projectID, String regionID, String matNumber);

    public Result startFlow(Budget budget, String processDefinitionKey);
    
    public Integer importExcel(MultipartFile file) throws Exception;
    
}
