/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.radish.master.entity.Budget;
import com.radish.master.service.BudgetService;

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
    
    @RequestMapping(value="/doestimate",method = RequestMethod.POST)
    public String doEstimate(String budgetNo, String sourceUrl, HttpServletRequest request){
        Budget budget = budgetService.getBudgetByNo(budgetNo);
        
        request.setAttribute("budget", JSONArray.toJSONString(budget));
        request.setAttribute("sourceUrl", sourceUrl);
        
        return "budgetmanage/budgetestimate/budget_estimate";
    }
    
}
