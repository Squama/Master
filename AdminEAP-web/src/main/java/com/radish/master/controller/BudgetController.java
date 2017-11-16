package com.radish.master.controller;


import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetTx;
import com.radish.master.service.BudgetService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    @Resource
    private BudgetService budgetService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "budgetmanage/budget/budget_list";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.POST)
    public String edit(String budgetNo,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("budgetNo", budgetNo);
        return "budgetmanage/budget/budget_edit";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(){
        return "budgetmanage/budget/budget_edit";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getproject")
    @ResponseBody
    private Result getProject(Budget budget, HttpServletRequest request) {
        return new Result(true);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getbyno")
    @ResponseBody
    private Budget getBudgetByNo(String budgetNo) {
        return budgetService.getBudgetByNo(budgetNo);
    }
    
    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    private Result saveBudget(Budget budget, HttpServletRequest request) {
        if(budget.getId()==null) {
            budget.setOperator(SecurityUtil.getUserId());
            budget.setOperateTime(new Date());
            budget.setStatus("10");
            budgetService.save(budget);
        }else {
            budgetService.update(budget);
        }
        return new Result(true, budget);
    }
    
    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/savetx")
    @ResponseBody
    private Result saveBudgetTx(BudgetTx budgetTx, HttpServletRequest request) {
        if(budgetService.checkTxUnique(budgetTx.getProjectID(), budgetTx.getRegionID(), budgetTx.getMatNumber())){
            budgetService.save(budgetTx);
            return new Result(true);
        }else{
            return new Result(false);
        }
        
    }

}
