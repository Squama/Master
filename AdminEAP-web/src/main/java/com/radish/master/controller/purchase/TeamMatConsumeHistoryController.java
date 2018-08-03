/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.purchase;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.radish.master.entity.Purchase;
import com.radish.master.entity.PurchaseDet;
import com.radish.master.service.CommonService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年8月3日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/purchase/team")
public class TeamMatConsumeHistoryController {
    
    @Resource
    private CommonService commonService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "purchase/teamconsume/team_consume_list";
    }
    
    @RequestMapping(value="/history/{id}",method = RequestMethod.GET)
    public String channelChoose(@PathVariable("id") String id, HttpServletRequest request){
        
        PurchaseDet det = commonService.get(PurchaseDet.class, id);
        Purchase purchase = commonService.get(Purchase.class, det.getPurchaseID());
        
        request.setAttribute("budgetNo", purchase.getBudgetNo());
        request.setAttribute("teamCode", det.getTeamCode());
        
        return "purchase/teamconsume/team_consume_history";
    }
    
}
