package com.radish.master.controller;


import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetTx;
import com.radish.master.service.BudgetService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/budget")
public class BudgetController {
    
    private final static String budget_key = "budgetApprove";
    

    @Resource
    private BudgetService budgetService;
    
    @RequestMapping(value="/toimport",method = RequestMethod.GET)
    public String toImport(){
        return "budgetmanage/budget/budget_import";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/import")
    @ResponseBody
    public Result importExcel(@RequestParam(value = "budgetfileupload", required = false) MultipartFile budgetfileupload){
        try{
            budgetService.importExcel(budgetfileupload);
        }catch (Exception e) {
            return new Result(false,"导入失败");
        }
        
        return new Result(true);
    }

    
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "budgetmanage/budget/budget_list";
    }
    
    @RequestMapping(value="/listquery",method = RequestMethod.GET)
    public String listquery(){
        return "budgetmanage/budget/budget_list_query";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.POST)
    public String edit(String budgetNo,HttpServletRequest request) {
        request.setAttribute("budgetNo", budgetNo);
        return "budgetmanage/budget/budget_edit";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/detail/{batchNo}",method ={RequestMethod.POST,RequestMethod.GET})
    public String detail(@PathVariable("batchNo") String budgetNo,HttpServletRequest request) {
        request.setAttribute("budgetNo", budgetNo);
        return "budgetmanage/budget/budget_detail";
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
        /*if(budgetService.checkTxUnique(budgetTx.getProjectID(), budgetTx.getRegionID(), budgetTx.getMatNumber())){
            budgetService.save(budgetTx);
            return new Result(true);
        }else{
            return new Result(false);
        }*/
        return new Result(true);
    }
    
    @VerifyCSRFToken
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(Budget budget) {
        return budgetService.startFlow(this.getBudgetByNo(budget.getBudgetNo()),budget_key);
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/channel/{id}",method ={RequestMethod.POST,RequestMethod.GET})
    public String channel(@PathVariable("id") String id,HttpServletRequest request) {
        BudgetTx budgetTx = budgetService.get(BudgetTx.class ,id);
        request.setAttribute("id", budgetTx.getId());
        //request.setAttribute("matID", budgetTx.getMatNumber());
        return "budgetmanage/budgetactiviti/budget_channel";
    }
    
    @VerifyCSRFToken
    @RequestMapping(method = RequestMethod.POST, value = "/savechannel")
    @ResponseBody
    private Result saveChannel(BudgetTx budgetTx, HttpServletRequest request) {
        BudgetTx budgetTxOld = budgetService.get(BudgetTx.class, budgetTx.getId());
        
        //budgetTxOld.setSupplier(budgetTx.getSupplier());
        //budgetTxOld.setPrice(budgetTx.getPrice());
        budgetTxOld.setUpdateDateTime(new Date());
        
        budgetService.save(budgetTxOld);
        
        return new Result(true);
    }
    

    

}
