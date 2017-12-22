/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Purchase;
import com.radish.master.service.PurchaseService;

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

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "purchase/apply/apply_list";
    }
    
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(purchaseService.getProjectCombobox()));
        request.setAttribute("budgetOptions", JSONArray.toJSONString(purchaseService.getBudgetCombobox()));
        
        return "purchase/apply/apply_edit";
    }
    
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id, HttpServletRequest request){
        request.setAttribute("id", id);
        
        return "purchase/apply/apply_edit";
    }
    
    @RequestMapping(value="/getbudget")
    @ResponseBody
    public Result save(String projectID){
        return new Result(true, JSONArray.toJSONString(purchaseService.getBudgetComboboxByProject(projectID)));
    }
    
    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(Purchase purchase, HttpServletRequest request){
        
        
        if(StrUtil.isEmpty(purchase.getId())){
            purchase.setOperator(SecurityUtil.getUserId());
            purchase.setOperateTime(new Date());
            purchase.setStatus("10");
            purchase.setUpdateDateTime(new Date());
            purchaseService.save(purchase);
        }else{
            Purchase oldPurchase = purchaseService.get(Purchase.class, purchase.getId());
            oldPurchase.setOperator(SecurityUtil.getUserId());
            oldPurchase.setOperateTime(new Date());
            purchaseService.update(oldPurchase);
        }
        
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", purchase.getId());
        return new Result(true, map);
    }
    
}
