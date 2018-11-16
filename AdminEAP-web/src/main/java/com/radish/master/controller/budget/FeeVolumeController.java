/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.budget;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.project.Fee;
import com.radish.master.entity.project.FeeDetail;
import com.radish.master.service.CommonService;
import com.radish.master.system.TimeUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年11月15日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/project/feevolume")
public class FeeVolumeController {

    @Resource
    private CommonService commonService;
    
    @Resource
    private RuntimePageService runtimePageService;
    
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    private String list(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "budgetmanage/fee/fee_report_list";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/querylist")
    private String queryList(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "budgetmanage/fee/fee_query_list";
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request){
        request.setAttribute("feeID", id);
        return "budgetmanage/fee/detail";
    }
    
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        request.setAttribute("feeOptions", JSONArray.toJSONString(commonService.getFeeCombobox()));
        return "budgetmanage/fee/edit";
    }
    
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id, HttpServletRequest request){
        request.setAttribute("feeID", id);
        request.setAttribute("feeOptions", JSONArray.toJSONString(commonService.getFeeCombobox()));
        return "budgetmanage/fee/edit";
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(Fee fee, HttpServletRequest request){
        fee.setStatus("10");
        fee.setUpdateDateTime(new Date());
        fee.setAmount("0");
        fee.setName(SecurityUtil.getUser().getName() + "上报规费" + TimeUtil.getCurrentTime());
        
        try {
            commonService.save(fee);
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系管理员");
        }
        return new Result(true, fee);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/savedetail")
    @ResponseBody
    public Result saveDet(FeeDetail feeDetail, HttpServletRequest request){
        feeDetail.setCreateDateTime(new Date());
        commonService.save(feeDetail);
        
        Fee fee = commonService.get(Fee.class, feeDetail.getFeeID());
        BigDecimal oldValue = new BigDecimal(fee.getAmount());
        BigDecimal thisValue = new BigDecimal(feeDetail.getPrice());
        fee.setAmount(oldValue.add(thisValue).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        commonService.save(fee);
        
        return new Result(true, "success");
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/deletedetail")
    @ResponseBody
    public Result deleteDet(String id, HttpServletRequest request){
        FeeDetail feeDetail = commonService.get(FeeDetail.class, id);
        commonService.delete(feeDetail);
        return new Result(true, "success");
    }
    
    @RequestMapping(value="/getfee")
    @ResponseBody
    public Result getPackage(String feeID){
        Fee fee = commonService.get(Fee.class, feeID);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("projectName", fee.getProjectName());
        map.put("projectSubName", fee.getProjectSubName());
        
        return new Result(true, map);
    }
    
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {
        Fee fee = commonService.get(Fee.class, id);
        fee.setStatus("20");
        fee.setUpdateDateTime(new Date());
        
        commonService.update(fee);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "上报规费，项目：" + fee.getProjectName() +",子项：" + fee.getProjectSubName();
        
        //businessKey
        String businessKey = fee.getId();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey("feeBudget", name, variables,
                user.getId(), businessKey);
    }
    
}
