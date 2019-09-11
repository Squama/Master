/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.controller;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetEstimate;
import com.radish.master.entity.Project;
import com.radish.master.entity.Purchase;
import com.radish.master.entity.PurchaseDet;
import com.radish.master.pojo.Options;
import com.radish.master.pojo.RowEdit;
import com.radish.master.service.PurchaseService;
import com.radish.master.service.project.TeamSalaryService;

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
@Controller
@RequestMapping("/purchase/apply")
public class PurchaseApplyController {

    @Resource
    private PurchaseService purchaseService;

    @Resource
    private TeamSalaryService teamSalaryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "purchase/apply/apply_list";
    }

    @RequestMapping(value = "/testT", method = RequestMethod.GET)
    public String testQ() {
        return "purchase/apply/test_quantity_list";
    }

    @RequestMapping(value = "/testA", method = RequestMethod.GET)
    public String testA() {
        return "purchase/apply/test_amount_list";
    }

    @RequestMapping(value = "/auditquantity/{id}", method = RequestMethod.GET)
    public String auditquantity(@PathVariable("id") String id, HttpServletRequest request) {

        return "purchase/activiti/quantity_audit";
    }

    @RequestMapping(value = "/auditamount/{id}", method = RequestMethod.GET)
    public String auditamount(@PathVariable("id") String id, HttpServletRequest request) {

        return "purchase/activiti/amount_audit";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(purchaseService.getProjectCombobox()));
        request.setAttribute("budgetOptions", JSONArray.toJSONString(purchaseService.getBudgetCombobox()));

        return "purchase/apply/apply_edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(String id, HttpServletRequest request) {
        request.setAttribute("purchaseID", id);

        return "purchase/apply/apply_edit";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editActiviti(@PathVariable("id") String id, HttpServletRequest request) {
        request.setAttribute("purchaseID", id);

        return "purchase/apply/apply_edit";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(String id, HttpServletRequest request) {
        request.setAttribute("purchaseID", id);

        return "purchase/apply/apply_detail";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id) {

        Purchase purchase = purchaseService.get(Purchase.class, id);
        if (!"10".equals(purchase.getStatus())) {
            return new Result(false);
        } else {
            List<PurchaseDet> list = purchaseService.getPurchaseDetList(id);

            for (PurchaseDet det : list) {
                purchaseService.delete(det);
            }

            purchaseService.delete(purchase);
        }
        return new Result(true);
    }

    @RequestMapping(value = "/getpurchase")
    @ResponseBody
    public Result getPurchase(String purchaseID) {
        Purchase purchase = purchaseService.get(Purchase.class, purchaseID);

        Project project = purchaseService.get(Project.class, purchase.getProjectID());
        Budget budget = purchaseService.getBudgetByNo(purchase.getBudgetNo());

        Map<String, String> map = new HashMap<String, String>();
        map.put("regions", JSONArray.toJSONString(purchaseService.getRegionComboboxByBudgetNo(purchase.getBudgetNo())));
        map.put("mats", JSONArray.toJSONString(purchaseService.getMatMap()));
        map.put("teams", JSONArray.toJSONString(purchaseService.getTeamComboboxByProject(purchase.getProjectID())));
        map.put("projectName", project.getProjectName());
        map.put("budgetName", budget.getBudgetName());
        map.put("budgetNo", budget.getBudgetNo());

        return new Result(true, map);
    }

    @RequestMapping(value = "/getbudgetop")
    @ResponseBody
    public Result getBudgetOp(String projectID) {
        return new Result(true, JSONArray.toJSONString(purchaseService.getBudgetComboboxByProject(projectID)));
    }

    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    public Result save(Purchase purchase, HttpServletRequest request) {
        if (StrUtil.isEmpty(purchase.getId())) {
            purchase.setOperator(SecurityUtil.getUserId());
            purchase.setOperateTime(new Date());
            purchase.setStatus("10");
            purchase.setUpdateDateTime(new Date());
            purchaseService.save(purchase);
        } else {
            Purchase oldPurchase = purchaseService.get(Purchase.class, purchase.getId());
            oldPurchase.setOperator(SecurityUtil.getUserId());
            oldPurchase.setOperateTime(new Date());
            purchaseService.update(oldPurchase);
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", purchase.getId());
        map.put("regions", JSONArray.toJSONString(purchaseService.getRegionComboboxByBudgetNo(purchase.getBudgetNo())));
        map.put("mats", JSONArray.toJSONString(purchaseService.getMatMap()));
        map.put("teams", JSONArray.toJSONString(purchaseService.getTeamComboboxByProject(purchase.getProjectID())));

        return new Result(true, map);
    }

    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/savedet")
    @ResponseBody
    public Result saveDet(PurchaseDet purchaseDet, HttpServletRequest request) {
        purchaseDet.setCreateDateTime(new Date());
        purchaseDet.setSurplusQuantity(purchaseDet.getQuantity());
        purchaseDet.setGuidancePrice("0");
        purchaseDet.setPrice("0");

        Purchase pur = purchaseService.get(Purchase.class, purchaseDet.getPurchaseID());
        String budgetNo = pur.getBudgetNo();
        String regionCode = purchaseDet.getRegionID();
        String matNumber = purchaseDet.getMatNumber();
        // 预算
        List<Options> budgetSum = purchaseService.findMapBySql(
                "SELECT CAST(sum(e.quantity) AS char) value, '' data FROM tbl_budget_tx tx,tbl_budget_estimate e where tx.id=e.budget_tx_id and tx.budget_no = ? and tx.region_code= ? AND e.mat_number=?",
                new Object[] { budgetNo, regionCode, matNumber }, new Type[] { StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE }, Options.class);

        // 已消耗
        List<Options> consumeSum = purchaseService.findMapBySql(
                "SELECT CAST(sum(pd.quantity) AS char) value, '' data FROM tbl_purchase p, tbl_purchase_det pd WHERE p.id = pd.purchase_id AND P.status NOT IN ('10', '20', '30') AND p.budget_no=? AND pd.region_id=? AND pd.mat_number=?",
                new Object[] { budgetNo, regionCode, matNumber }, new Type[] { StringType.INSTANCE, StringType.INSTANCE, StringType.INSTANCE }, Options.class);

        String result = "";

        if (!consumeSum.isEmpty()) {
            BigDecimal budgetMat = new BigDecimal(budgetSum.get(0).getValue() == null ? "0" : budgetSum.get(0).getValue());
            BigDecimal consumeMat = new BigDecimal(consumeSum.get(0).getValue() == null ? "0" : consumeSum.get(0).getValue());
            result = budgetMat.subtract(consumeMat).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
        } else {
            result = budgetSum.get(0).getValue();
        }

        purchaseDet.setRemain(result);
        purchaseService.save(purchaseDet);
        return new Result(true, "success");
    }

    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/deletedet")
    @ResponseBody
    public Result deleteDet(String id, HttpServletRequest request) {
        PurchaseDet purchaseDet = purchaseService.get(PurchaseDet.class, id);
        purchaseService.delete(purchaseDet);
        return new Result(true, "success");
    }

    @VerifyCSRFToken
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {
        return purchaseService.startPurchaseApplyFlow(purchaseService.get(Purchase.class, id), "purchaseApply");
    }

    @RequestMapping(value = "/getbyid")
    @ResponseBody
    private Result getDetByID(PurchaseDet purchaseDet, HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Purchase purchase = purchaseService.get(Purchase.class, purchaseDet.getPurchaseID());
        Project project = purchaseService.get(Project.class, purchase.getProjectID());
        Budget budget = purchaseService.getBudgetByNo(purchase.getBudgetNo());

        map.put("projectName", project.getProjectName());
        map.put("budgetName", budget.getBudgetName());
        map.put("purchaseName", purchase.getPurchaseName());
        map.put("purchaseID", purchase.getId());

        return new Result(true, map);
    }

    @RequestMapping(value = "/channeledit/{id}", method = RequestMethod.GET)
    public String channelEdit(@PathVariable("id") String id, HttpServletRequest request) {
        request.setAttribute("purchaseID", id);

        return "purchase/activiti/channel_edit";
    }

    @RequestMapping(value = "/guidanceprice/{id}", method = RequestMethod.GET)
    public String guidancePrice(@PathVariable("id") String id, HttpServletRequest request) {
        request.setAttribute("purchaseID", id);

        return "purchase/activiti/guidance_price";
    }

    @RequestMapping(value = "/rowedit", method = RequestMethod.POST)
    @ResponseBody
    public String singleEstimate(String action, HttpServletRequest request) throws Exception {
        String id = "";
        String field = "";
        String value = "";

        RowEdit re = new RowEdit();
        List<Object> list = new ArrayList<Object>();

        Enumeration<String> key = request.getParameterNames();
        while (key.hasMoreElements()) {
            String paramName = (String) key.nextElement();
            if ("action".equals(paramName)) {
                continue;
            }
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    int idIndexStart = paramName.indexOf("[");
                    int idIndexEnd = paramName.indexOf("]");
                    int fieldIndexStart = paramName.lastIndexOf("[");
                    int fieldIndexEnd = paramName.lastIndexOf("]");
                    id = paramName.substring(idIndexStart + 1, idIndexEnd);
                    field = paramName.substring(fieldIndexStart + 1, fieldIndexEnd);
                    value = paramValue;
                }
            }
        }

        PurchaseDet det = purchaseService.get(PurchaseDet.class, id);

        Method set = det.getClass().getMethod("set" + teamSalaryService.captureName(field), String.class);
        set.invoke(det, value);

        purchaseService.save(det);

        list.add(det);
        re.setData(list);
        return JSONArray.toJSONString(re);
    }

    @RequestMapping(value = "/choose/{id}", method = RequestMethod.GET)
    public String channelChoose(@PathVariable("id") String id, HttpServletRequest request) {
        request.setAttribute("purchaseDetID", id);
        PurchaseDet det = purchaseService.get(PurchaseDet.class, id);
        request.setAttribute("matID", det.getMatNumber());
        request.setAttribute("count", det.getQuantity());
        request.setAttribute("dispatchStatus", det.getDispatchStatus());

        return "purchase/activiti/choose_channel";
    }
    
    @RequestMapping(value = "/chooseshow/{id}", method = RequestMethod.GET)
    public String channelChooseShow(@PathVariable("id") String id, HttpServletRequest request) {
        request.setAttribute("purchaseDetID", id);
        PurchaseDet det = purchaseService.get(PurchaseDet.class, id);
        request.setAttribute("matID", det.getMatNumber());
        request.setAttribute("count", det.getQuantity());
        request.setAttribute("dispatchStatus", det.getDispatchStatus());

        return "purchase/activiti/choose_channel_show";
    }

    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/savechannel")
    @ResponseBody
    private Result saveChannel(PurchaseDet purchaseDet, HttpServletRequest request) {
        PurchaseDet purchaseDetOld = purchaseService.get(PurchaseDet.class, purchaseDet.getId());

        // budgetTxOld.setSupplier(budgetTx.getSupplier());
        // budgetTxOld.setPrice(budgetTx.getPrice());

        purchaseDetOld.setChannelID(purchaseDet.getChannelID());
        purchaseDetOld.setChannelName(purchaseDet.getChannelName());
        purchaseDetOld.setPrice(purchaseDet.getPrice());
        purchaseDetOld.setStockType("1");
        purchaseDetOld.setUpdateDateTime(new Date());

        purchaseService.save(purchaseDetOld);

        return new Result(true);
    }

    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/savechannelstock")
    @ResponseBody
    private Result saveChannelStock(String projectID, String id, String matID, String num, HttpServletRequest request) {
        purchaseService.executeDispatch(projectID, id, matID, num);

        return new Result(true);
    }

    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/cancelchannelstock")
    @ResponseBody
    private Result cancelChannelStock(String id, HttpServletRequest request) {
        purchaseService.cancelDispatch(id);

        return new Result(true);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validatechannel")
    @ResponseBody
    public Result validateChannel(String businessKey, HttpServletRequest request) {
        List<PurchaseDet> list = purchaseService.findMapBySql("select id from tbl_purchase_det where purchase_id = ? AND channel_id is null",
                new Object[] { businessKey }, new Type[] { StringType.INSTANCE }, PurchaseDet.class);
        if (!list.isEmpty()) {
            return new Result(false, "请为所有明细都添加渠道！");
        } else {
            return new Result(true, "校验通过");
        }

    }

    @RequestMapping(value = "/getregionmat")
    @ResponseBody
    public Result getRegionMat(String budgetNo, String regionID) {
        return new Result(true, JSONArray.toJSONString(purchaseService.getMatComboboxByRegion(budgetNo, regionID)));
    }

    @RequestMapping(value = "/handledistinctestimatedetail")
    public Result handledistinctestimatedetail(String id, HttpServletRequest request) {

        List<BudgetEstimate> list = purchaseService.getDistinctList();

        String budgetTxID = "";
        BudgetEstimate beTmp = null;

        for (BudgetEstimate be : list) {
            BudgetEstimate budgetEstimate = purchaseService.get(BudgetEstimate.class, be.getId());

            if (!be.getBudgetTxID().equals(budgetTxID)) {
                budgetTxID = be.getBudgetTxID();
                beTmp = budgetEstimate;
                continue;
            }

            BigDecimal q1 = new BigDecimal(beTmp.getQuantity());
            BigDecimal q2 = new BigDecimal(budgetEstimate.getQuantity());

            beTmp.setQuantity(q1.add(q2).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());

            purchaseService.update(beTmp);
            purchaseService.delete(budgetEstimate);
        }

        return new Result(true, "处理成功");
    }

}
