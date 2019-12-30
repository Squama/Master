/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.controller.budget;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.BudgetEstimate;
import com.radish.master.entity.BudgetImport;
import com.radish.master.entity.project.BudgetLabour;
import com.radish.master.entity.project.BudgetMech;
import com.radish.master.service.BudgetService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2019年12月26日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/budget/estimate/addition")
public class BudgetEstimateAdditionController {

    @Resource
    private BudgetService budgetService;
    @Resource
    private BaseService baseService;
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "budgetmanage/budgetaddition/list";
    }
    
    @RequestMapping(value="/toestimate",method = RequestMethod.POST)
    public String toEstimate(String budgetNo, String sourceUrl, HttpServletRequest request){
        
        request.setAttribute("budgetNo", budgetNo);
        request.setAttribute("sourceUrl", sourceUrl);
        request.setAttribute("materiels", JSONArray.toJSONString(budgetService.getMatMap()));
        
        return "budgetmanage/budgetaddition/toadd";
    }
    
    @RequestMapping(value="/singleaddition",method = RequestMethod.GET)
    public String singleEstimate(HttpServletRequest request, String id){
        request.setAttribute("budgetTxID", id);
        
        
        return "budgetmanage/budgetaddition/single_addition";
    }
    
    @RequestMapping(value="/toaddition",method = RequestMethod.GET)
    public String toAddition(String tabName, String id, String quantity, String consume, HttpServletRequest request){
        
        request.setAttribute("tabName", tabName);
        request.setAttribute("id", id);
        
        request.setAttribute("quantity", quantity);
        request.setAttribute("consume", consume);
        
        return "budgetmanage/budgetaddition/addition";
    }
    
    @RequestMapping(value="/change",method = RequestMethod.POST)
    @ResponseBody
    public Result change(String tabName, String id, String direction, String addition, String quantity, String consume, HttpServletRequest request){
        
        if("2".equals(direction)){
            BigDecimal q = new BigDecimal(quantity);
            BigDecimal c = new BigDecimal(consume);
            BigDecimal a = new BigDecimal(addition);
            
            if(q.subtract(c).compareTo(a) < 0){
                return new Result(false, "扣减预算后不能为负数");
            }
            
            addition = "-" + addition;
        }else{
            addition = "+" + addition;
        }
        
        
        
        if("tx".equals(tabName)){
            return addTx(id, addition);
        }
        
        if("labour".equals(tabName)){
            return addLabour(id, addition);
        }
        
        if("mech".equals(tabName)){
            return addMech(id, addition);
        }
        
        return new Result(true);
    }
    
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String budgetNo) {
        return budgetService.startAdditionFlow(budgetService.getBudgetByNo(budgetNo),"additionApprove");
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/auditlist/{budgetNo}")
    private String toAudit(@PathVariable("budgetNo") String budgetNo, HttpServletRequest request) {
        request.setAttribute("budgetNo", budgetNo);
        
        List<BudgetImport> list = budgetService.getBudgetImportList(budgetNo);
        
        BigDecimal sumUnit = new BigDecimal("0");
        BigDecimal sumLabour = new BigDecimal("0");
        BigDecimal sumMat = new BigDecimal("0");
        BigDecimal sumMech = new BigDecimal("0");
        
        for(BudgetImport bi : list){
            if(!"1".equals(bi.getIsGroup())){
                sumUnit = sumUnit.add(new BigDecimal(StrUtil.isEmpty(bi.getUnitPrice())?"0":bi.getUnitPrice()));
                sumLabour = sumLabour.add(new BigDecimal(StrUtil.isEmpty(bi.getArtificiality())?"0":bi.getArtificiality()));
                sumMat = sumMat.add(new BigDecimal(StrUtil.isEmpty(bi.getMateriels())?"0":bi.getMateriels()));
                sumMech = sumMech.add(new BigDecimal(StrUtil.isEmpty(bi.getMech())?"0":bi.getMech()));
            }
        }
        
        request.setAttribute("sumUnit", sumUnit.toPlainString());
        request.setAttribute("sumLabour", sumLabour.toPlainString());
        request.setAttribute("sumMat", sumMat.toPlainString());
        request.setAttribute("sumMech", sumMech.toPlainString());
        
        return "budgetmanage/budgetaddition/audit_list";
    }
    
    @RequestMapping(value="/singleadditionquery",method = RequestMethod.GET)
    public String singleEstimateQuery(HttpServletRequest request, String id){
        request.setAttribute("budgetTxID", id);
        
        return "budgetmanage/budgetaddition/single_addition_query";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    private Result saveBudgetEstimate(BudgetEstimate budgetEstimate, HttpServletRequest request) {
        
        List<BudgetEstimate> list = budgetService.getBudgetEstimateCount(budgetEstimate.getBudgetTxID());
        if(list.size() >= 20){
            return new Result(false, "材料测算保存失败！请检查是否超过20条！");
        }
        
        budgetEstimate.setQuantity("0");
        budgetEstimate.setType("2");
        budgetEstimate.setAdditionQuantity("+" + budgetEstimate.getAdditionQuantity());
        
        budgetService.save(budgetEstimate);
        
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/savelabour")
    @ResponseBody
    private Result saveBudgetLabour(BudgetLabour budgetLabour, HttpServletRequest request) {
        
        budgetLabour.setLabourQuantity("0");
        budgetLabour.setType("2");
        budgetLabour.setAdditionQuantity("+" + budgetLabour.getAdditionQuantity());
        
        budgetService.save(budgetLabour);
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/savemech")
    @ResponseBody
    private Result saveBudgetMech(BudgetMech budgetMech, HttpServletRequest request) {
        
        budgetMech.setMechQuantity("0");
        budgetMech.setType("2");
        budgetMech.setAdditionQuantity("+" + budgetMech.getAdditionQuantity());
        
        budgetService.save(budgetMech);
        
        return new Result(true);
    }
    
    private Result addTx(String id, String addition){
        BudgetEstimate be = budgetService.get(BudgetEstimate.class, id);
        
        be.setType("2");
        be.setAdditionQuantity(addition);
        
        budgetService.update(be);
        return new Result(true);
    }
    
    private Result addLabour(String id, String addition){
        BudgetLabour be = budgetService.get(BudgetLabour.class, id);
        
        be.setType("2");
        be.setAdditionQuantity(addition);
        
        budgetService.update(be);
        return new Result(true);
    }
    
    private Result addMech(String id, String addition){
        BudgetMech be = budgetService.get(BudgetMech.class, id);
        
        be.setType("2");
        be.setAdditionQuantity(addition);
        
        budgetService.update(be);
        return new Result(true);
    }
}
