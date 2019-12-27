/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.controller.budget;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.BudgetEstimate;
import com.radish.master.entity.BudgetTx;
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
    
    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    private Result saveBudgetEstimate(BudgetEstimate budgetEstimate, HttpServletRequest request) {
        
        List<BudgetEstimate> list = budgetService.getBudgetEstimateCount(budgetEstimate.getBudgetTxID());
        if(list.size() >= 20){
            return new Result(false, "材料测算保存失败！请检查是否超过20条！");
        }
        
        budgetEstimate.setType("2");
        
        budgetService.save(budgetEstimate);
        
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/savelabour")
    @ResponseBody
    private Result saveBudgetLabour(BudgetLabour budgetLabour, HttpServletRequest request) {
        
        budgetLabour.setType("2");
        budgetService.save(budgetLabour);
        return new Result(true);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/savemech")
    @ResponseBody
    private Result saveBudgetMech(BudgetMech budgetMech, HttpServletRequest request) {
        
        budgetMech.setType("2");
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
    
    private String getUnitPrice(BudgetTx bt){
        BigDecimal result = new BigDecimal("0");
        BigDecimal labour = new BigDecimal(StrUtil.isEmpty(bt.getArtificiality())?"0":bt.getArtificiality() );
        BigDecimal mat = new BigDecimal(StrUtil.isEmpty(bt.getMateriels())?"0":bt.getMateriels());
        BigDecimal mech = new BigDecimal(StrUtil.isEmpty(bt.getMech())?"0":bt.getMech());
        
        return result.add(labour.add(mat.add(mech))).toPlainString();
    }
    
    private void updateGroupInfo(String quotaGroup, String budgetNo){
        List<BudgetTx> list = budgetService.getBudgetTxListByGroup(budgetNo, quotaGroup);
        
        //合价
        BigDecimal unitPriceSum = new BigDecimal("0");
        //人工
        BigDecimal artificialitySum = new BigDecimal("0");
        //机械
        BigDecimal mechSum = new BigDecimal("0");
        //材料
        BigDecimal materielsSum = new BigDecimal("0");
        
        BudgetTx groupBudgetTx = null;
        
        for(BudgetTx tx : list){
            if("1".equals(tx.getIsGroup())){
                groupBudgetTx = tx;
                continue;
            }
            unitPriceSum = unitPriceSum.add(new BigDecimal(StrUtil.isEmpty(tx.getUnitPrice())?"0":tx.getUnitPrice()));
            artificialitySum = artificialitySum.add(new BigDecimal(StrUtil.isEmpty(tx.getArtificiality())?"0":tx.getArtificiality()));
            mechSum = mechSum.add(new BigDecimal(StrUtil.isEmpty(tx.getMech())?"0":tx.getMech()));
            materielsSum = materielsSum.add(new BigDecimal(StrUtil.isEmpty(tx.getMateriels())?"0":tx.getMateriels()));
        }
        
        if(groupBudgetTx != null){
            groupBudgetTx.setUnitPrice(unitPriceSum.toPlainString());
            groupBudgetTx.setArtificiality(artificialitySum.toPlainString());
            groupBudgetTx.setMech(mechSum.toPlainString());
            groupBudgetTx.setMateriels(materielsSum.toPlainString());
            
            budgetService.update(groupBudgetTx);
        }
        
    }
}
