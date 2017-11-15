package com.radish.master.controller;


import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.ContractService;
import com.radish.master.entity.Budget;
import com.radish.master.service.BudgetService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    @Resource
    private ContractService contractService;

    @Resource
    private BudgetService budgetService;

    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(){
        return "budgetmanage/budget/budget_edit";
    }



    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    private Result saveBudget(Budget tbl_budget, HttpServletRequest request) {
        if(tbl_budget.getId()==null) {
            //tbl_budget.setisApprover(0);
            //tbl_budget.setisEdit(0);
            budgetService.save(tbl_budget);
        }else {
            budgetService.update(tbl_budget);
        }
        return new Result(true);
    }

}
