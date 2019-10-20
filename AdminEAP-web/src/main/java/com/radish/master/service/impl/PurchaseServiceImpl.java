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
import org.springframework.stereotype.Service;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.PageInfo;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.query.entity.QueryCondition;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetEstimate;
import com.radish.master.entity.Dispatch;
import com.radish.master.entity.DispatchDetail;
import com.radish.master.entity.Materiel;
import com.radish.master.entity.Project;
import com.radish.master.entity.Purchase;
import com.radish.master.entity.PurchaseApplyAudit;
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
        return this.findMapBySql("select id value, project_name data from tbl_project", new Object[] {}, new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getBudgetCombobox() {
        return this.findMapBySql("select budget_no value, budget_name data from tbl_budget", new Object[] {}, new Type[] { StringType.INSTANCE },
                Options.class);
    }

    @Override
    public List<Options> getTeamComboboxByProject(String projectID) {
        return this.findMapBySql("select team_code value, team_name data from tbl_project_team where project_id=? AND status = 10", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getBudgetComboboxByProject(String projectID) {
        return this.findMapBySql("select budget_no value, budget_name data from tbl_budget where project_id=? AND status=30", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getRegionComboboxByBudgetNo(String budgetNo) {
        return this.findMapBySql(
                "select tx.region_code value, tx.region_name data from tbl_budget_tx tx " + "where tx.budget_no = ? and tx.region_code is not null "
                        + "AND tx.id in(select budget_tx_id FROM tbl_budget_estimate where budget_no = ?)",
                new Object[] { budgetNo, budgetNo }, new Type[] { StringType.INSTANCE, StringType.INSTANCE }, Options.class);
    }

    @Override
    public MatMap getMatComboboxByRegion(String budgetNo, String regionID) {
        List<Materiel> list = this.findMapBySql(
                "select e.mat_number mat_number, e.mat_name mat_name, e.mat_standard mat_standard, e.unit unit from tbl_budget_tx tx,tbl_budget_estimate e where tx.id=e.budget_tx_id and tx.budget_no = ? and tx.region_code= ?",
                new Object[] { budgetNo, regionID }, new Type[] { StringType.INSTANCE, StringType.INSTANCE }, Materiel.class);
        Map<String, Materiel> map = new HashMap<String, Materiel>();
        for (Materiel mat : list) {
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
        for (Materiel mat : list) {
            map.put(mat.getMat_number(), mat);
        }
        MatMap matMap = new MatMap();
        matMap.setMap(map);

        return matMap;
    }

    @Override
    public Budget getBudgetByNo(String budgetNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("budgetNo", budgetNo);
        return this.get("from Budget where budgetNo=:budgetNo", params);
    }

    @Override
    public Result startPurchaseApplyFlow(Purchase purchase, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        Budget budget = this.getBudgetByNo(purchase.getBudgetNo());
        Project project = this.get(Project.class, purchase.getProjectID());

        purchase.setStatus("20");
        purchase.setUpdateDateTime(new Date());
        purchase.setPurchaseName("【" + user.getName() + "】申请" + budget.getBudgetName());

        this.update(purchase);

        // 给流程起个名字
        String name = user.getName() + "申请预算【" + budget.getBudgetName() + "】用料，所属项目：" + project.getProjectName();

        // businessKey
        String businessKey = purchase.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);
        // 数量超限判断
        List<PurchaseApplyAudit> list = this.getQuantityList(businessKey);
        String result = "false";
        for (int i = 0; i < list.size(); i++) {

            BigDecimal budgetCount = new BigDecimal(list.get(i).getBudget() == null ? "0" : list.get(i).getBudget());
            BigDecimal costCount = new BigDecimal(list.get(i).getCost() == null ? "0" : list.get(i).getCost());
            BigDecimal applyCount = new BigDecimal(list.get(i).getApply());

            if (applyCount.add(costCount).compareTo(budgetCount) != -1) {
                result = "true";
                break;
            }

        }
        variables.put("isAudit", result);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(), businessKey);
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
    public PurchaseDet getPurchaseDetOri(String purchaseID, String regionID, String MatNumber) {
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
        Purchase purchase = this.get(Purchase.class, purchaseDetOld.getPurchaseID());

        Dispatch dispatch = this.getDispatchByProAndPur(projectID, purchase.getProjectID(), purchaseDetOld.getPurchaseID());
        if (dispatch == null) {
            dispatch = new Dispatch();
            dispatch.setSourceProjectID(projectID);
            dispatch.setTargetProjectID(purchase.getProjectID());
            dispatch.setPurchaseID(purchaseDetOld.getPurchaseID());
            dispatch.setStatus("10");

            this.save(dispatch);
        }

        BigDecimal sum = new BigDecimal("0");

        for (StockChannel channel : list) {
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

        if (purchaseDetOld.getQuantity() == Double.parseDouble(num)) {
            purchaseDetOld.setChannelID(projectID);
            purchaseDetOld.setChannelName("总库调度");
            purchaseDetOld.setPrice(sum.divide(new BigDecimal(num), 2, RoundingMode.HALF_UP).toPlainString());
            purchaseDetOld.setSurplusQuantity(purchaseDetOld.getQuantity());
            purchaseDetOld.setStockType("2");
            this.save(purchaseDetOld);
        } else {
            BigDecimal quantity = new BigDecimal(purchaseDetOld.getQuantity());
            purchaseDetOld.setQuantity(quantity.subtract(new BigDecimal(num)).doubleValue());
            purchaseDetOld.setSurplusQuantity(purchaseDetOld.getQuantity());
            purchaseDetOld.setStockType("1");

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
        // 获取原调度信息
        PurchaseDet purchaseDetOld = this.get(PurchaseDet.class, id);
        Purchase purchase = this.get(Purchase.class, purchaseDetOld.getPurchaseID());

        Dispatch dispatch = this.getDispatchByProAndPur("4028828e61d54c2a0161e4f72db3010c", purchase.getProjectID(), purchaseDetOld.getPurchaseID());

        List<DispatchDetail> detailList = this.getDispatchDetailList(dispatch.getId(), purchaseDetOld.getId());

        List<StockChannel> list = new ArrayList<StockChannel>();

        for (DispatchDetail detail : detailList) {
            StockChannel channel = new StockChannel();
            channel.setChannel_id(detail.getChannelID());
            channel.setProject_id(dispatch.getSourceProjectID());
            channel.setMat_id(purchaseDetOld.getMatNumber());
            channel.setFrozen_num(detail.getQuantity());
        }

        if (stockService.thawStockChannel(list)) {
            // 加回原来的
            PurchaseDet detOriginal = this.getPurchaseDetOri(purchaseDetOld.getPurchaseID(), purchaseDetOld.getRegionID(), purchaseDetOld.getMatNumber());
            BigDecimal numCancel = new BigDecimal(purchaseDetOld.getQuantity());
            detOriginal.setQuantity(numCancel.add(new BigDecimal(detOriginal.getQuantity())).doubleValue());
            detOriginal.setSurplusQuantity(detOriginal.getQuantity());

            this.update(detOriginal);
            // 删掉现在的

            this.delete(purchaseDetOld);

        }
    }

    @Override
    public List<PurchaseApplyAudit> getQuantityAuditList(QueryCondition condition, PageInfo pageInfo) {

        String id = null;
        if (condition != null) {
            id = condition.getConditionMap().get("id").toString();
        }

        return this.getQuantityListMap(id);
    }

    @Override
    public List<PurchaseApplyAudit> getAmountAuditList(QueryCondition condition, PageInfo pageInfo) {
        String id = null;
        if (condition != null) {
            id = condition.getConditionMap().get("id").toString();
        }

        return this.getAmountListMap(id);
    }

    @Override
    public List<PurchaseApplyAudit> getQuantityListMap(String purchaseID) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT PUR.id, PUR.budget_no, ORI.region_name, PUR.mat_number, PUR.mat_name, PUR.mat_standard, ORI.budget, COST.cost, PUR.apply FROM ");
        sb.append("(SELECT PD.id,P.budget_no, PD.region_id, PD.mat_number, PD.mat_name, PD.mat_standard, SUM(PD.quantity) apply ");
        sb.append("FROM tbl_purchase P, tbl_purchase_det PD ");
        sb.append("WHERE PD.purchase_id = P.id AND P.id = ? ");
        sb.append("GROUP BY P.budget_no, PD.mat_number, PD.mat_name, PD.mat_standard, PD.region_id) PUR ");
        sb.append("LEFT JOIN");
        sb.append("(SELECT BT.budget_no, BT.region_code, BT.region_name, BE.mat_number, BE.mat_name, BE.mat_standard, BE.quantity budget ");
        sb.append("FROM tbl_budget_tx BT, tbl_budget_estimate BE ");
        sb.append("WHERE BT.id = BE.budget_tx_id) ORI ");
        sb.append("ON PUR.budget_no = ORI.budget_no AND PUR.region_id = ORI.region_code AND PUR.mat_number = ORI.mat_number ");
        sb.append("LEFT JOIN");
        sb.append("(SELECT P.budget_no, PD.region_id, PD.mat_number, PD.mat_name, PD.mat_standard, SUM(PD.quantity) cost ");
        sb.append("FROM tbl_purchase P, tbl_purchase_det PD ");
        sb.append("WHERE PD.purchase_id = P.id AND P.status NOT IN ('10', '20', '30') AND P.id != ? ");
        sb.append("GROUP BY P.budget_no, PD.mat_number, PD.mat_name, PD.mat_standard, PD.region_id) COST ");
        sb.append("ON COST.budget_no = ORI.budget_no AND COST.region_id = ORI.region_code AND COST.mat_number = ORI.mat_number ");
        sb.append("ORDER BY PUR.region_id ");

        /*
         * SELECT PUR.id, PUR.budget_no, ORI.region_name, PUR.mat_number,
         * PUR.mat_name, PUR.mat_standard, ORI.budget, COST.cost, PUR.apply FROM
         * (SELECT PD.id,P.budget_no, PD.region_id, PD.mat_number, PD.mat_name,
         * PD.mat_standard, SUM(PD.quantity) apply FROM tbl_purchase P,
         * tbl_purchase_det PD WHERE PD.purchase_id = P.id AND P.id =
         * '40280c9460e9746f0160e97f17880035' GROUP BY P.budget_no,
         * PD.mat_number, PD.mat_name, PD.mat_standard, PD.region_id) PUR LEFT
         * JOIN (SELECT BT.budget_no, BT.region_code, BT.region_name,
         * BE.mat_number, BE.mat_name, BE.mat_standard, BE.quantity budget FROM
         * tbl_budget_tx BT, tbl_budget_estimate BE WHERE BT.id =
         * BE.budget_tx_id) ORI ON PUR.budget_no = ORI.budget_no AND
         * PUR.region_id = ORI.region_code AND PUR.mat_number = ORI.mat_number
         * LEFT JOIN (SELECT P.budget_no, PD.region_id, PD.mat_number,
         * PD.mat_name, PD.mat_standard, SUM(PD.quantity) cost FROM tbl_purchase
         * P, tbl_purchase_det PD WHERE PD.purchase_id = P.id AND P.id !=
         * '40280c9460e9746f0160e97f17880035' GROUP BY P.budget_no,
         * PD.mat_number, PD.mat_name, PD.mat_standard, PD.region_id) COST ON
         * COST.budget_no = ORI.budget_no AND COST.region_id = ORI.region_code
         * AND COST.mat_number = ORI.mat_number ORDER BY PUR.region_id
         */

        return this.findMapBySql(sb.toString(), new Object[] { purchaseID, purchaseID }, new Type[] { StringType.INSTANCE, StringType.INSTANCE }, null);
    }

    @Override
    public List<PurchaseApplyAudit> getAmountListMap(String purchaseID) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT PUR.id, PUR.budget_no, ORI.region_name, ORI.budget, COST.cost, PUR.apply FROM ");
        sb.append("(SELECT PD.id,P.budget_no, PD.region_id, SUM(PD.quantity*PD.price) apply ");
        sb.append("FROM tbl_purchase P, tbl_purchase_det PD ");
        sb.append("WHERE PD.purchase_id = P.id AND P.id = ? ");
        sb.append("GROUP BY P.budget_no, PD.region_id) PUR ");
        sb.append("LEFT JOIN");
        sb.append("(SELECT budget_no, region_code, region_name, unit_price AS budget ");
        sb.append("FROM tbl_budget_tx) ORI ");
        sb.append("ON PUR.budget_no = ORI.budget_no AND PUR.region_id = ORI.region_code  ");
        sb.append("LEFT JOIN");
        sb.append("(SELECT PD.id,P.budget_no, PD.region_id, SUM(PD.quantity*PD.price) cost  ");
        sb.append("FROM tbl_purchase P, tbl_purchase_det PD ");
        sb.append("WHERE PD.purchase_id = P.id AND P.status NOT IN ('10', '20', '30') AND P.id != ? ");
        sb.append("GROUP BY P.budget_no, PD.region_id) COST ");
        sb.append("ON COST.budget_no = ORI.budget_no AND COST.region_id = ORI.region_code ");
        sb.append("ORDER BY PUR.region_id ");

        // TODO Auto-generated method stub
        /*
         * SELECT PUR.id, PUR.budget_no, ORI.region_name, ORI.budget, COST.cost,
         * PUR.apply FROM (SELECT PD.id,P.budget_no, PD.region_id,
         * SUM(PD.quantity*PD.price) apply FROM tbl_purchase P, tbl_purchase_det
         * PD WHERE PD.purchase_id = P.id AND P.id =
         * '40280c9460e9746f0160e97f17880035' GROUP BY P.budget_no,
         * PD.region_id) PUR LEFT JOIN (SELECT budget_no, region_code,
         * region_name, unit_price AS budget FROM tbl_budget_tx) ORI ON
         * PUR.budget_no = ORI.budget_no AND PUR.region_id = ORI.region_code
         * LEFT JOIN (SELECT PD.id,P.budget_no, PD.region_id,
         * SUM(PD.quantity*PD.price) cost FROM tbl_purchase P, tbl_purchase_det
         * PD WHERE PD.purchase_id = P.id AND P.id !=
         * '40280c9460e9746f0160e97f17880035' GROUP BY P.budget_no,
         * PD.region_id) COST ON COST.budget_no = ORI.budget_no AND
         * COST.region_id = ORI.region_code ORDER BY PUR.region_id
         */

        return this.findMapBySql(sb.toString(), new Object[] { purchaseID, purchaseID }, new Type[] { StringType.INSTANCE, StringType.INSTANCE }, null);
    }

    @Override
    public List<PurchaseApplyAudit> getQuantityList(String purchaseID) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT PUR.id, PUR.budget_no, ORI.region_name, PUR.mat_number, PUR.mat_name, PUR.mat_standard, ORI.budget, COST.cost, PUR.apply FROM ");
        sb.append("(SELECT PD.id,P.budget_no, PD.region_id, PD.mat_number, PD.mat_name, PD.mat_standard, SUM(PD.quantity) apply ");
        sb.append("FROM tbl_purchase P, tbl_purchase_det PD ");
        sb.append("WHERE PD.purchase_id = P.id AND P.id = :purchaseID ");
        sb.append("GROUP BY P.budget_no, PD.mat_number, PD.mat_name, PD.mat_standard, PD.region_id) PUR ");
        sb.append("LEFT JOIN");
        sb.append("(SELECT BT.budget_no, BT.region_code, BT.region_name, BE.mat_number, BE.mat_name, BE.mat_standard, BE.quantity budget ");
        sb.append("FROM tbl_budget_tx BT, tbl_budget_estimate BE ");
        sb.append("WHERE BT.id = BE.budget_tx_id) ORI ");
        sb.append("ON PUR.budget_no = ORI.budget_no AND PUR.region_id = ORI.region_code AND PUR.mat_number = ORI.mat_number ");
        sb.append("LEFT JOIN");
        sb.append("(SELECT P.budget_no, PD.region_id, PD.mat_number, PD.mat_name, PD.mat_standard, SUM(PD.quantity) cost ");
        sb.append("FROM tbl_purchase P, tbl_purchase_det PD ");
        sb.append("WHERE PD.purchase_id = P.id AND P.status NOT IN ('10', '20', '30') AND P.id != :purchaseID2 ");
        sb.append("GROUP BY P.budget_no, PD.mat_number, PD.mat_name, PD.mat_standard, PD.region_id) COST ");
        sb.append("ON COST.budget_no = ORI.budget_no AND COST.region_id = ORI.region_code AND COST.mat_number = ORI.mat_number ");
        sb.append("ORDER BY PUR.region_id ");

        Map<String, Object> params = new HashMap<>();
        params.put("purchaseID", purchaseID);
        params.put("purchaseID2", purchaseID);
        return this.findBySql(sb.toString(), params, PurchaseApplyAudit.class);

    }

    @Override
    public List<PurchaseApplyAudit> getAmountList(String purchaseID) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT PUR.id, PUR.budget_no, ORI.region_name, PUR.mat_number, PUR.mat_name, PUR.mat_standard, ORI.budget, COST.cost, PUR.apply FROM ");
        sb.append("(SELECT PD.id,P.budget_no, PD.region_id, PD.mat_number, PD.mat_name, PD.mat_standard, SUM(PD.quantity*PD.price) apply ");
        sb.append("FROM tbl_purchase P, tbl_purchase_det PD ");
        sb.append("WHERE PD.purchase_id = P.id AND P.id = :purchaseID ");
        sb.append("GROUP BY P.budget_no, PD.mat_number, PD.mat_name, PD.mat_standard, PD.region_id) PUR ");
        sb.append("LEFT JOIN");
        sb.append("(SELECT BT.budget_no, BT.region_code, BT.region_name, BE.mat_number, BE.mat_name, BE.mat_standard, BE.quantity*BE.budget_price budget ");
        sb.append("FROM tbl_budget_tx BT, tbl_budget_estimate BE ");
        sb.append("WHERE BT.id = BE.budget_tx_id) ORI ");
        sb.append("ON PUR.budget_no = ORI.budget_no AND PUR.region_id = ORI.region_code AND PUR.mat_number = ORI.mat_number ");
        sb.append("LEFT JOIN");
        sb.append("(SELECT P.budget_no, PD.region_id, PD.mat_number, PD.mat_name, PD.mat_standard, SUM(PD.quantity*PD.price) cost ");
        sb.append("FROM tbl_purchase P, tbl_purchase_det PD ");
        sb.append("WHERE PD.purchase_id = P.id AND P.status NOT IN ('10', '20', '30') AND P.id != :purchaseID2 ");
        sb.append("GROUP BY P.budget_no, PD.mat_number, PD.mat_name, PD.mat_standard, PD.region_id) COST ");
        sb.append("ON COST.budget_no = ORI.budget_no AND COST.region_id = ORI.region_code AND COST.mat_number = ORI.mat_number ");
        sb.append("ORDER BY PUR.region_id ");

        Map<String, Object> params = new HashMap<>();
        params.put("purchaseID", purchaseID);
        params.put("purchaseID2", purchaseID);
        return this.findBySql(sb.toString(), params, PurchaseApplyAudit.class);
    }

    @Override
    public List<BudgetEstimate> getDistinctList() {
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT ");
        sb.append(" b.*");
        sb.append(" FROM ");
        sb.append("  tbl_budget_estimate b ");
        sb.append(" RIGHT JOIN ");
        sb.append(" ( ");
        sb.append(" SELECT be.mat_number,be.budget_tx_id,count(1) countNum ");
        sb.append(" from tbl_budget_estimate be ");
        sb.append(" WHERE be.budget_tx_id IN (SELECT id from tbl_budget_tx) ");
        sb.append(" GROUP BY be.mat_number,be.budget_tx_id ");
        sb.append(" HAVING count(1) > 1 ");
        sb.append(" ) a ");
        sb.append(" ON b.budget_tx_id = a.budget_tx_id ");
        sb.append(" AND b.mat_number = a.mat_number ");
        sb.append(" ORDER BY b.mat_number,b.budget_tx_id ");
        Map<String, Object> params = new HashMap<>();
        
        return this.findBySql(sb.toString(), params, BudgetEstimate.class);
    }
}
