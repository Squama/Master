/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.pojo.Result;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetImport;
import com.radish.master.entity.BudgetTx;
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
        Budget budget = budgetService.getBudgetByNo(budgetNo);
        
        request.setAttribute("budget", JSONArray.toJSONString(budget));
        request.setAttribute("sourceUrl", sourceUrl);
        
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
    
}
