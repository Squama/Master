/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.service;

import java.util.List;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Budget;
import com.radish.master.entity.Dispatch;
import com.radish.master.entity.DispatchDetail;
import com.radish.master.entity.Purchase;
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
    
    List<Options> getBudgetComboboxByProject(String projectID);
    
    List<Options> getRegionComboboxByBudgetNo(String budgetNo);
    
    MatMap getMatMap();
    
    Budget getBudgetByNo(String batchNo);
    
    public Result startPurchaseApplyFlow(Purchase purchase, String processDefinitionKey);
    
    public List<PurchaseDet> getPurchaseDetList(String id);
    
    Dispatch getDispatchByProAndPur(String sourceProjectID, String targetProjectID, String purchaseID);
    
    public List<DispatchDetail> getDispatchDetailList(String dispatchID, String purchaseDetID);
    
    public PurchaseDet getPurchaseDetOri(String purchaseID, String regionID, String MatNumber);
    
    public void executeDispatch(String projectID, String id, String matID, String num);
    
    public void cancelDispatch(String id);
}
