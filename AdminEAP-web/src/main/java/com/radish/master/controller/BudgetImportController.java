/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.pojo.Result;
import com.radish.master.entity.Budget;
import com.radish.master.entity.BudgetImport;
import com.radish.master.service.BudgetService;
import com.radish.master.system.HanyuPinyinHelper;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年12月5日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/budgetimport")
public class BudgetImportController {

    @Resource
    private BudgetService budgetService;
    
    @RequestMapping(value="/toimport",method = RequestMethod.GET)
    public String toImport(HttpServletRequest request){
        
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        
        return "budgetmanage/budget/budget_import";
    }
    
    @RequestMapping("/checkUnique")
    @ResponseBody
    public Map checkUnique(String projectID, String budgetName, String fieldValue, String id) {

        Map<String, Boolean> map = new HashMap<String, Boolean>();
        try {
            Budget budget = budgetService.getBudgetByProjectAndName(projectID, budgetName);
            map.put("valid", budget==null);
            return map;
        } catch (Exception ex) {
            map.put("valid", false);
            return map;
        }
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/import")
    @ResponseBody
    public Result importExcel(@RequestParam(value = "budgetfileupload", required = false) MultipartFile budgetfileupload, Budget budget){
        try{
            budgetService.importExcel(budgetfileupload, budget);
        }catch (Exception e) {
            return new Result(false,"导入失败");
        }
        
        return new Result(true,budgetfileupload.getOriginalFilename());
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/getgroup/{group}")
    @ResponseBody
    private Result getGroup(@PathVariable("group") String group, HttpServletRequest request) {
        BudgetImport bi = budgetService.getGroupByNo(group);
        return new Result(true, bi);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/genno/{budgetName}")
    @ResponseBody
    private Result genNo(@PathVariable("budgetName") String budgetName, HttpServletRequest request) {
        String timeString = new SimpleDateFormat("yyMMddHHmm").format(Calendar.getInstance().getTime());
        String py = HanyuPinyinHelper.toHanyuPinyin(budgetName) + timeString;
        return new Result(true, py);
    }
    
    
}
