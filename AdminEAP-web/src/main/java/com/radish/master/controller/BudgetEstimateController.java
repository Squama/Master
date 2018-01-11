/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetEstimate;
import com.radish.master.entity.BudgetImport;
import com.radish.master.entity.BudgetTx;
import com.radish.master.entity.Project;
import com.radish.master.pojo.RowEdit;
import com.radish.master.service.BudgetService;
import com.radish.master.system.CodeException;
import com.radish.master.system.GUID;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年12月6日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/budget/estimate")
public class BudgetEstimateController {

    @Resource
    private BudgetService budgetService;
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "budgetmanage/budgetestimate/budget_estimate_list";
    }
    
    @RequestMapping(value="/toestimate",method = RequestMethod.POST)
    public String toEstimate(String budgetNo, String sourceUrl, HttpServletRequest request){
        //Budget budget = budgetService.getBudgetByNo(budgetNo);
        
        request.setAttribute("budgetNo", budgetNo);
        request.setAttribute("sourceUrl", sourceUrl);
        request.setAttribute("materiels", JSONArray.toJSONString(budgetService.getMatMap()));
        
        return "budgetmanage/budgetestimate/budget_estimate";
    }
    
    @RequestMapping(value="/doestimate",method = RequestMethod.POST)
    @ResponseBody
    public Result doEstimate(String budgetNo, String[] choose, HttpServletRequest request) throws CodeException{
        List<BudgetImport> importList = budgetService.getBudgetImportList(choose);
        
        List<BudgetTx> txList = new ArrayList<BudgetTx>();
        
        BudgetTx btGroup = null;
        
        for(BudgetImport bi : importList){
            BudgetTx bt = new BudgetTx();
            BeanUtils.copyProperties(bi,bt);
            
            bt.setId(null);
            bt.setRegionCode(bi.getQuotaNo());
            bt.setRegionName(bi.getQuotaName());
            bt.setCreateDateTime(new Date());
            
            bi.setOrderNo(GUID.genTxNo(16));
            bi.setCol(null);
            
            bt.setOrderNo(bi.getOrderNo());
            bt.setCol(bi.getCol());
            
            txList.add(bt);
            
            if(btGroup == null || !btGroup.getQuotaGroup().equalsIgnoreCase(bt.getQuotaGroup())){
                btGroup = new BudgetTx();
                BudgetImport biGroup = budgetService.getGroupByNo(bi.getQuotaGroup());
                BeanUtils.copyProperties(biGroup,btGroup);
                btGroup.setId(null);
                btGroup.setRegionCode(biGroup.getQuotaNo());
                btGroup.setRegionName(biGroup.getQuotaName());
                
                txList.add(btGroup);
            }

        }
        
        budgetService.batchUpdate(importList);
        budgetService.batchSave(txList);
        
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/getgroup/{group}")
    @ResponseBody
    private Result getGroup(@PathVariable("group") String group, HttpServletRequest request) {
        BudgetTx bt = budgetService.getTxGroupByNo(group);
        return new Result(true, bt);
    }
    
    @RequestMapping(value="/rowedit",method = RequestMethod.POST)
    @ResponseBody
    public String singleEstimate(String action, HttpServletRequest request){
        request.setAttribute("id", action);
        Enumeration<String> key = request.getParameterNames();  
        while(key.hasMoreElements()){  
            System.out.println(key.nextElement());  
        } 
        RowEdit re = new RowEdit();
        List<Object> list = new ArrayList<Object>();
        list.add(budgetService.get(BudgetTx.class,"40280cac604dc2d501604dc4ef9c0003"));
        re.setData(list);
        System.out.println(JSONArray.toJSONString(re));
        return JSONArray.toJSONString(re);
    }
    
    @RequestMapping(value="/singleestimate",method = RequestMethod.GET)
    public String singleEstimate(HttpServletRequest request, String id){
        request.setAttribute("budgetTxID", id);
        
        return "budgetmanage/budgetestimate/single_estimate";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getbytxid")
    @ResponseBody
    private Result getBudgetByNo(String budgetTxID) {
        BudgetTx tx = budgetService.get(BudgetTx.class,budgetTxID);
        
        Project project = budgetService.get(Project.class, tx.getProjectID());
        
        Budget budget = budgetService.getBudgetByNo(tx.getBudgetNo());
        
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("budgetTx", tx);
        map.put("projectName", project.getProjectName());
        map.put("budgetName", budget.getBudgetName());
        
        return new Result(true, map);
    }
    
    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    private Result saveBudgetEstimate(BudgetEstimate budgetEstimate, HttpServletRequest request) {
        budgetService.save(budgetEstimate);
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    @ResponseBody
    private Result deleteBudgetEstimate(String id, HttpServletRequest request) {
        BudgetEstimate budgetEstimate = budgetService.get(BudgetEstimate.class, id);
        budgetService.delete(budgetEstimate);
        return new Result(true);
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value="/auditlist/{budgetNo}")
    private String toAudit(@PathVariable("budgetNo") String budgetNo, HttpServletRequest request) {
        request.setAttribute("budgetNo", budgetNo);
        return "budgetmanage/budgetactiviti/audit_list";
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value="/backtoedit/{budgetNo}")
    private String toEditAgain(@PathVariable("budgetNo") String budgetNo, HttpServletRequest request) {
        request.setAttribute("budgetNo", budgetNo);
        request.setAttribute("materiels", JSONArray.toJSONString(budgetService.getMatMap()));
        
        return "budgetmanage/budgetestimate/budget_estimate";
    }
    
    @VerifyCSRFToken
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String budgetNo) {
        return budgetService.startEstimateFlow(budgetService.getBudgetByNo(budgetNo),"estimateApprove");
    }
    
    @RequestMapping(value="/comparedetail",method = RequestMethod.GET)
    public String compareDetail(){
        return "budgetmanage/budgetestimate/budget_estimate_list";
    }
    
}
