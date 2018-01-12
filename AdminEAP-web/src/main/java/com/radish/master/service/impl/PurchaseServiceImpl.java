/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Budget;
import com.radish.master.entity.Dispatch;
import com.radish.master.entity.DispatchDetail;
import com.radish.master.entity.Materiel;
import com.radish.master.entity.Purchase;
import com.radish.master.entity.PurchaseDet;
import com.radish.master.entity.StockChannel;
import com.radish.master.pojo.MatMap;
import com.radish.master.pojo.Options;
import com.radish.master.service.PurchaseService;
import com.radish.master.service.StockService;

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
@Service("purchaseService")
public class PurchaseServiceImpl extends BaseServiceImpl implements PurchaseService {
    
    @Resource
    private RuntimePageService runtimePageService;

    @Resource
    private StockService stockService;
    
    @Override
    public List<Options> getProjectCombobox() {
        return this.findMapBySql("select id value, project_name data from tbl_project", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    }

    @Override
    public List<Options> getBudgetCombobox() {
        return this.findMapBySql("select budget_no value, budget_name data from tbl_budget", new Object[]{}, new Type[]{StringType.INSTANCE}, Options.class);
    }

    @Override
    public List<Options> getBudgetComboboxByProject(String projectID) {
        return this.findMapBySql("select budget_no value, budget_name data from tbl_budget where project_id=? AND status=30", new Object[]{projectID}, new Type[]{StringType.INSTANCE}, Options.class);
    }

    @Override
    public List<Options> getRegionComboboxByBudgetNo(String budgetNo) {
        return this.findMapBySql("select region_code value, region_name data from tbl_budget_tx where budget_no=? and region_code is not null", new Object[]{budgetNo}, new Type[]{StringType.INSTANCE}, Options.class);
    }
    
    @Override
    public MatMap getMatComboboxByRegion(String budgetNo, String regionID) {
        List<Materiel> list = this.findMapBySql("select e.mat_number mat_number, e.mat_name mat_name, e.mat_standard mat_standard, e.unit unit from tbl_budget_tx tx,tbl_budget_estimate e where tx.id=e.budget_tx_id and tx.budget_no = ? and tx.region_code= ?", new Object[]{budgetNo, regionID}, new Type[]{StringType.INSTANCE,StringType.INSTANCE}, Materiel.class);
        Map<String, Materiel> map = new HashMap<String, Materiel>();
        for(Materiel mat : list){
            map.put(mat.getMat_number(), mat);
        }
        
        MatMap matMap = new MatMap();
        matMap.setMap(map);
        
        return matMap;
    }

    @Override
    public MatMap getMatMap() {
        List<Materiel> list = this.find("from Materiel where isValid = 1");
        Map<String, Materiel> map = new HashMap<String, Materiel>();
        for(Materiel mat : list){
            map.put(mat.getMat_number(), mat);
        }
        MatMap matMap = new MatMap();
        matMap.setMap(map);
        
        return matMap;
    }

    @Override
    public Budget getBudgetByNo(String budgetNo){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("budgetNo", budgetNo);
        return this.get("from Budget where budgetNo=:budgetNo", params);
    }

    @Override
    public Result startPurchaseApplyFlow(Purchase purchase, String processDefinitionKey) {
        User user = SecurityUtil.getUser();
        
        Budget budget = this.getBudgetByNo(purchase.getBudgetNo());
        
        purchase.setStatus("20");
        purchase.setUpdateDateTime(new Date());
        purchase.setPurchaseName("【" + user.getName() + "】申请" + budget.getBudgetName());
        
        this.update(purchase);
        
        //给流程起个名字
        String name = user.getName() + "申请预算用料：" + purchase.getBudgetNo() + "，所属项目：" + purchase.getProjectID();
        
        //businessKey
        String businessKey = purchase.getId();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("isAudit", "true");
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables,
                user.getId(), businessKey);
    }
    
    @Override
    public List<PurchaseDet> getPurchaseDetList(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return this.find("from PurchaseDet where purchaseID = :id", params);
    }

	@Override
	public Dispatch getDispatchByProAndPur(String sourceProjectID, String targetProjectID, String purchaseID) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("sourceProjectID", sourceProjectID);
        params.put("targetProjectID", targetProjectID);
        params.put("purchaseID", purchaseID);
        return this.get("from Dispatch where sourceProjectID=:sourceProjectID AND targetProjectID=:targetProjectID AND purchaseID=:purchaseID", params);
	}
	
    @Override
    public List<DispatchDetail> getDispatchDetailList(String dispatchID, String purchaseDetID) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dispatchID", dispatchID);
        params.put("purchaseDetID", purchaseDetID);
        return this.find("from DispatchDetail where dispatchID = :dispatchID AND purchaseDetID = : purchaseDetID", params);
    }
    
    @Override
    public PurchaseDet getPurchaseDetOri(String purchaseID, String regionID, String MatNumber){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("purchaseID", purchaseID);
        params.put("regionID", regionID);
        params.put("MatNumber", MatNumber);
        return this.get("from PurchaseDet where purchaseID=:purchaseID AND regionID=:regionID AND MatNumber=:MatNumber AND stockType <> '2'", params);
    }
    

	@Override
	public void executeDispatch(String projectID, String id, String matID, String num) {
		List<StockChannel> list = stockService.getStockChannelFrozenList(projectID, matID, Double.parseDouble(num));
    	
    	PurchaseDet purchaseDetOld = this.get(PurchaseDet.class, id);
    	Purchase purchase = this.get(Purchase.class,purchaseDetOld.getPurchaseID());
    	
    	Dispatch dispatch = this.getDispatchByProAndPur(projectID, purchase.getProjectID(), purchaseDetOld.getPurchaseID());
    	if(dispatch == null){
    		dispatch = new Dispatch();
    		dispatch.setSourceProjectID(projectID);
    		dispatch.setTargetProjectID(purchase.getProjectID());
        	dispatch.setPurchaseID(purchaseDetOld.getPurchaseID());
        	dispatch.setStatus("10");
        	
        	this.save(dispatch);
    	}
    	
    	BigDecimal sum = new BigDecimal("0");
    	
    	for(StockChannel channel : list){
    		DispatchDetail detail = new DispatchDetail();
    		detail.setChannelID(channel.getChannel_id());
    		detail.setDispatchID(dispatch.getId());
    		detail.setMatNumber(matID);
    		detail.setPrice(channel.getPrice());
    		detail.setQuantity(channel.getFrozen_num());
    		detail.setPurchaseDetID(purchaseDetOld.getId());
    		BigDecimal price = new BigDecimal(channel.getPrice());
    		BigDecimal quantity = new BigDecimal(channel.getFrozen_num());
    		sum = sum.add(price.multiply(quantity));
    		this.save(detail);
    	}
    	
    	
    	if(purchaseDetOld.getQuantity() == Double.parseDouble(num)){
    		purchaseDetOld.setChannelID(projectID);
    		purchaseDetOld.setChannelName("总库调度");
    		purchaseDetOld.setPrice(sum.divide(new BigDecimal(num), 2, RoundingMode.HALF_UP).toPlainString());
    		purchaseDetOld.setSurplusQuantity(purchaseDetOld.getQuantity());
    		this.save(purchaseDetOld);
    	}else{
    		BigDecimal quantity = new BigDecimal(purchaseDetOld.getQuantity());
    		purchaseDetOld.setQuantity(quantity.subtract(new BigDecimal(num)).doubleValue());
    		purchaseDetOld.setSurplusQuantity(purchaseDetOld.getQuantity());
    		purchaseDetOld.setDispatchStatus("1");
    		
    		this.update(purchaseDetOld);
    		
    		PurchaseDet purchaseDet = new PurchaseDet();
    		purchaseDet.setRegionID(purchaseDetOld.getRegionID());
    		purchaseDet.setRegionName(purchaseDetOld.getRegionName());
    		purchaseDet.setChannelName(purchaseDetOld.getChannelName());
    		purchaseDet.setMatNumber(purchaseDetOld.getMatNumber());
    		purchaseDet.setMatName(purchaseDetOld.getMatName());
    		purchaseDet.setPurchaseID(purchaseDetOld.getPurchaseID());
    		purchaseDet.setMatStandard(purchaseDetOld.getMatStandard());
    		purchaseDet.setUnit(purchaseDetOld.getUnit());
    		purchaseDet.setCreateDateTime(new Date());
    		purchaseDet.setChannelID(projectID);
    		purchaseDet.setChannelName("总库调度");
    		purchaseDet.setPrice(sum.divide(new BigDecimal(num), 2, RoundingMode.HALF_UP).toPlainString());
    		purchaseDet.setQuantity(Double.parseDouble(num));
    		purchaseDet.setSurplusQuantity(purchaseDet.getQuantity());
    		purchaseDet.setStockType("2");
    		
    		this.save(purchaseDet);
    	}
		
	}
	
	@Override
    public void cancelDispatch(String id) {
	    //获取原调度信息
	    PurchaseDet purchaseDetOld = this.get(PurchaseDet.class, id);
        Purchase purchase = this.get(Purchase.class,purchaseDetOld.getPurchaseID());
        
        Dispatch dispatch = this.getDispatchByProAndPur(purchase.getProjectID(), purchase.getProjectID(), purchaseDetOld.getPurchaseID());
        
        List<DispatchDetail> detailList = this.getDispatchDetailList(dispatch.getId(), purchaseDetOld.getId());
        
        List<StockChannel> list = new ArrayList<StockChannel>();
        
        for(DispatchDetail detail : detailList){
            StockChannel channel = new StockChannel();
            channel.setChannel_id(detail.getChannelID());
            channel.setProject_id(dispatch.getSourceProjectID());
            channel.setMat_id(purchaseDetOld.getMatNumber());
            channel.setFrozen_num(detail.getQuantity());
        }
	    
        if(stockService.thawStockChannel(list)){
            //加回原来的
            PurchaseDet detOriginal = this.getPurchaseDetOri(purchaseDetOld.getPurchaseID(), purchaseDetOld.getRegionID(), purchaseDetOld.getMatNumber());
            BigDecimal numCancel = new BigDecimal(purchaseDetOld.getQuantity());
            detOriginal.setQuantity(numCancel.add(new BigDecimal(detOriginal.getQuantity())).doubleValue());
            detOriginal.setSurplusQuantity(detOriginal.getQuantity());
            
            this.update(detOriginal);
            //删掉现在的
            
            this.delete(purchaseDetOld);
            
        }
    }
}
