package com.radish.master.controller;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetEstimate;
import com.radish.master.entity.BudgetImport;
import com.radish.master.entity.BudgetTx;
import com.radish.master.entity.Project;
import com.radish.master.service.BudgetService;

@Controller
@RequestMapping("/budget")
public class BudgetController {
    
    private final static String budget_key = "budgetApprove";
    

    @Resource
    private BudgetService budgetService;
    
    @RequestMapping(method = RequestMethod.POST, value = "/getproject/{projectID}")
    @ResponseBody
    private Result getProject(@PathVariable("projectID") String projectID) {
        Project project = budgetService.get(Project.class, projectID);
        return new Result(true, project.getProjectName());
    }
    
    @RequestMapping(value="/toquantitydetail",method = RequestMethod.GET)
    public String toQuantityDetail(String budgetNo, HttpServletRequest request){
    	request.setAttribute("budgetNo", budgetNo);
    	
        return "budgetmanage/budgetactiviti/budget_quantity_list";
    }
    
    @RequestMapping(value="/toamountdetail",method = RequestMethod.GET)
    public String toAmountDetail(String budgetNo, HttpServletRequest request){
    	request.setAttribute("budgetNo", budgetNo);
    	
        return "budgetmanage/budgetactiviti/budget_amount_list";
    }
    
    /**
     * 
     * 新旧代码分界
     * *************************************************************************
     * 
     */
    
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



    @RequestMapping(method = RequestMethod.POST, value = "/getbyno")
    @ResponseBody
    private Budget getBudgetByNo(String budgetNo) {
        return budgetService.getBudgetByNo(budgetNo);
    }
    
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
    
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(Budget budget) {
        return budgetService.startFlow(this.getBudgetByNo(budget.getBudgetNo()),budget_key);
    }
    
    @RequestMapping(value="/channel/{id}",method ={RequestMethod.POST,RequestMethod.GET})
    public String channel(@PathVariable("id") String id,HttpServletRequest request) {
        BudgetTx budgetTx = budgetService.get(BudgetTx.class ,id);
        request.setAttribute("id", budgetTx.getId());
        //request.setAttribute("matID", budgetTx.getMatNumber());
        return "budgetmanage/budgetactiviti/budget_channel";
    }
    
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
    
    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        
        Budget budget = budgetService.getBudgetByNo(id);
        if(!"10".equals(budget.getStatus())){
            return new Result(false);
        }else{
            List<BudgetTx> txList = budgetService.getBudgetTxList(budget.getBudgetNo());
            
            for(BudgetTx tx : txList){
                budgetService.delete(tx);
            }
            
            List<BudgetImport> importList = budgetService.getBudgetImportList(budget.getBudgetNo());
            
            for(BudgetImport imp : importList){
                budgetService.delete(imp);
            }
            
            List<BudgetEstimate> estimateList = budgetService.getBudgetEstimateList(budget.getBudgetNo());
            
            for(BudgetEstimate est : estimateList){
                budgetService.delete(est);
            }
            
            budgetService.delete(budget);
        }
        return new Result(true);
    }
    

    

}
