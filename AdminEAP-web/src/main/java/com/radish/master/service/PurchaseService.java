/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.service;

import java.util.List;

import com.cnpc.framework.base.pojo.PageInfo;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.query.entity.QueryCondition;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetEstimate;
import com.radish.master.entity.Dispatch;
import com.radish.master.entity.DispatchDetail;
import com.radish.master.entity.Purchase;
import com.radish.master.entity.PurchaseApplyAudit;
import com.radish.master.entity.PurchaseDet;
import com.radish.master.pojo.MatMap;
import com.radish.master.pojo.Options;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年12月22日    Create this file
* </pre>
* 
*/

public interface PurchaseService extends BaseService{

    List<Options> getProjectCombobox();
    
    List<Options> getBudgetCombobox();
    
    List<Options> getTeamComboboxByProject(String projectID);
    
    List<Options> getBudgetComboboxByProject(String projectID);
    
    List<Options> getRegionComboboxByBudgetNo(String budgetNo);
    
    MatMap getMatComboboxByRegion(String budgetNo, String regionID);
    
    MatMap getMatMap();
    
    Budget getBudgetByNo(String batchNo);
    
    public Result startPurchaseApplyFlow(Purchase purchase, String processDefinitionKey);
    
    public List<PurchaseDet> getPurchaseDetList(String id);
    
    Dispatch getDispatchByProAndPur(String sourceProjectID, String targetProjectID, String purchaseID);
    
    public List<DispatchDetail> getDispatchDetailList(String dispatchID, String purchaseDetID);
    
    public PurchaseDet getPurchaseDetOri(String purchaseID, String regionID, String MatNumber);
    
    public void executeDispatch(String projectID, String id, String matID, String num);
    
    public void cancelDispatch(String id);
    
    List<PurchaseApplyAudit> getQuantityAuditList(QueryCondition condition, PageInfo pageInfo);
    
    List<PurchaseApplyAudit> getAmountAuditList(QueryCondition condition, PageInfo pageInfo);
    
    public List<PurchaseApplyAudit> getAmountListMap(String purchaseID);
    
    public List<PurchaseApplyAudit> getQuantityListMap(String purchaseID);
    
    public List<PurchaseApplyAudit> getAmountList(String purchaseID);
    
    public List<PurchaseApplyAudit> getQuantityList(String purchaseID);
    
    public List<BudgetEstimate> getDistinctList();
}
