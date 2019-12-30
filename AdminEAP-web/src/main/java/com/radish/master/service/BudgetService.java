package com.radish.master.service;


import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cnpc.framework.base.pojo.PageInfo;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.query.entity.QueryCondition;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetEstimate;
import com.radish.master.entity.BudgetImport;
import com.radish.master.entity.BudgetTx;
import com.radish.master.entity.Materiel;
import com.radish.master.entity.PurchaseApplyAudit;
import com.radish.master.entity.project.BudgetLabour;
import com.radish.master.entity.project.BudgetMech;
import com.radish.master.pojo.Options;

public interface BudgetService extends BaseService {

    Budget getBudgetByNo(String batchNo);
    
    Budget getBudgetByProjectAndName(String projectID, String budgetName);
    
    Boolean checkTxUnique(String projectID, String regionID, String matNumber);

    public Result startFlow(Budget budget, String processDefinitionKey);
    
    List<PurchaseApplyAudit> getQuantityAuditList(QueryCondition condition, PageInfo pageInfo);
    
    List<PurchaseApplyAudit> getAmountAuditList(QueryCondition condition, PageInfo pageInfo);
    
    public List<PurchaseApplyAudit> getAmountListMap(String purchaseID);
    
    public List<PurchaseApplyAudit> getQuantityListMap(String purchaseID);
    
    /*
     * *************************************************************
     */
    
    public Integer importExcel(MultipartFile file, Budget budget) throws Exception;
    
    BudgetImport getGroupByNo(String group);
    
    BudgetImport getGroupByGroupAndNo(String group, String budgetNo);
    
    List<Options> getProjectCombobox();
    
    List<BudgetImport> getBudgetImportList(String[] importIDs);
    
    BudgetTx getTxGroupByNo(String group);
    
    BudgetTx getTxGroupByGroupAndNo(String group, String budgetNo);
    
    BudgetTx checkPack(String budgetNo);
    
    Materiel getEstimateImportMat(String name, String standard, String unit);
    
    Map<String, Materiel> getMatMap();
    
    public Result startEstimateFlow(Budget budget, String processDefinitionKey);
    
    public Result startAdditionFlow(Budget budget, String processDefinitionKey);
    
    public List<BudgetTx> getBudgetTxList(String budgetNo);
    
    public List<BudgetImport> getBudgetImportList(String budgetNo);
    
    public List<BudgetEstimate> getBudgetEstimateList(String budgetNo);
    
    public List<BudgetEstimate> getBudgetEstimateCount(String budgetTxID);
    
    public List<BudgetImport> getBudgetImportListByOrderNo(String orderNo);
    
    public List<BudgetTx> getBudgetTxListByGroup(String budgetNo, String group);
    
    public List<BudgetLabour> getBudgetLabourCount(String budgetTxID);
    
    public List<BudgetMech> getBudgetMechCount(String budgetTxID);
}
