/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.budget;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.radish.master.entity.ProAccount;
import com.radish.master.entity.ProAccountDet;
import com.radish.master.entity.Project;
import com.radish.master.entity.project.Fee;
import com.radish.master.entity.project.FeeDetail;
import com.radish.master.entity.project.Tax;
import com.radish.master.service.CommonService;
import com.radish.master.system.Arith;
import com.radish.master.system.TimeUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年11月16日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/tax")
public class TaxController {

    @Resource
    private CommonService commonService;
    
    @Resource
    private RuntimePageService runtimePageService;
    
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    private String list(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "budgetmanage/tax/list";
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request){
        request.setAttribute("taxID", id);
        return "budgetmanage/tax/detail";
    }
    
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "budgetmanage/tax/edit";
    }
    
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id, HttpServletRequest request){
        request.setAttribute("taxID", id);
        return "budgetmanage/tax/edit";
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(Tax tax, HttpServletRequest request){
        tax.setStatus("10");
        tax.setUpdateDateTime(new Date());
        tax.setName(SecurityUtil.getUser().getName() + "申报税金消耗" + TimeUtil.getCurrentTime());
        
        try {
            commonService.save(tax);
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系管理员");
        }
        return new Result(true, tax);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    public Tax getMechInfo(String id){
        return commonService.get(Tax.class, id);
    }
    
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {
        Tax tax = commonService.get(Tax.class, id);
        tax.setStatus("20");
        tax.setUpdateDateTime(new Date());
        
        commonService.update(tax);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "税金消耗申请，项目：" + tax.getProjectName() +",子项：" + tax.getProjectSubName();
        
        //businessKey
        String businessKey = tax.getId();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey("taxBudget", name, variables,
                user.getId(), businessKey);
    }
    
    //---------------------------------------财务支付部分
    @RequestMapping("/doPrint")
    private String doPrint(HttpServletRequest request) {
        request.setAttribute("taxID",request.getParameter("id"));
        request.setAttribute("zdr", SecurityUtil.getUser().getId());
        return "budgetmanage/tax/payDetail";
    }
    @RequestMapping(value = "/changePayObj", method = RequestMethod.POST)
    @ResponseBody
    public Result startManage(Tax fee) {
    	Tax f = commonService.get(Tax.class, fee.getId());
         f.setPayObj(fee.getPayObj());
         commonService.update(f);
         return new Result(true);
    }
    @RequestMapping("/dojz")
	@ResponseBody
	public Result dojz(HttpServletRequest request){
		Arith arith = new Arith();
		String id = request.getParameter("id");
		Tax fee = commonService.get(Tax.class, id);
		
		String xmid = "";
		String content = fee.getProjectSubName()+"税金消耗";
		
		List<ProAccount> xmz = commonService.find(" from ProAccount where projectId='"+fee.getProjectID()+"'");
		User u = SecurityUtil.getUser();
		//账目明细
		ProAccountDet mx = new ProAccountDet();
		mx.setCreateDate(new Date());
		mx.setAbstracts(content);
		mx.setZmtype("2");
		mx.setOutMoney(fee.getAmount());
		mx.setAccounter(u.getName());
		mx.setAccounterId(u.getId());
		//mx.setAuditName(u.getName());
		//mx.setAuditId(u.getId());
		mx.setStatus("10");
		if(xmz.size()<=0){//无账本，先生成账本
			ProAccount zb = new ProAccount();
			zb.setProjectId(xmid);
			if("1".equals(xmid)){
				zb.setAccountName("公司账本");
			}else{
				Project p = commonService.get(Project.class, fee.getProjectID());
				zb.setAccountName(p.getProjectName());
			}
			
			commonService.save(zb);
			zb.setAllMoney(arith.sub(0.0,Double.valueOf(fee.getAmount()))+"");
			mx.setProjectAccountId(zb.getId());
			commonService.save(mx);
			commonService.update(zb);
		}else{
			ProAccount zb = xmz.get(0);
			zb.setAllMoney(arith.sub(Double.valueOf(zb.getAllMoney()),Double.valueOf(fee.getAmount()))+"");
			mx.setProjectAccountId(zb.getId());
			commonService.save(mx);
			commonService.update(zb);
		}
		fee.setIsjz("1");
		commonService.update(fee);
		Result r = new Result();
		return r;
	}
}
