package com.radish.master.service;


import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetImport;
import com.radish.master.entity.BudgetTx;
import com.radish.master.entity.Materiel;
import com.radish.master.pojo.Options;

public interface BudgetService extends BaseService {

    Budget getBudgetByNo(String batchNo);
    
    Boolean checkTxUnique(String projectID, String regionID, String matNumber);

    public Result startFlow(Budget budget, String processDefinitionKey);
    
    /*
     * *************************************************************
     */
    
    public Integer importExcel(MultipartFile file, Budget budget) throws Exception;
    
    BudgetImport getGroupByNo(String group);
    
    List<Options> getProjectCombobox();
    
    List<BudgetImport> getBudgetImportList(String[] importIDs);
    
    BudgetTx getTxGroupByNo(String group);
    
    Map<String, Materiel> getMatMap();
    
    public Result startEstimateFlow(Budget budget, String processDefinitionKey);
}
