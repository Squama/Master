/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.budget;

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
import com.radish.master.entity.project.Package;
import com.radish.master.entity.project.PackageDetail;
import com.radish.master.service.CommonService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年8月8日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/budget/pack")
public class PackageController {

    @Resource
    private CommonService commonService;
    
    @Resource
    private RuntimePageService runtimePageService;
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "budgetmanage/pack/list";
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request){
        request.setAttribute("packageID", id);
        return "budgetmanage/pack/detail";
    }
    
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        
        return "budgetmanage/pack/edit";
    }
    
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id, HttpServletRequest request){
        request.setAttribute("packageID", id);
        
        return "budgetmanage/pack/edit";
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(Package pack, HttpServletRequest request){
        pack.setStatus("10");
        pack.setUpdateDateTime(new Date());
        
        try {
            commonService.save(pack);
        } catch (Exception e) {
            return new Result(false,"该班组已在该项目下配置包工包料");
        }
        return new Result(true, pack);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/savedetail")
    @ResponseBody
    public Result saveDet(PackageDetail packageDetail, HttpServletRequest request){
        packageDetail.setCreateDateTime(new Date());
        commonService.save(packageDetail);
        return new Result(true, "success");
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/deletedetail")
    @ResponseBody
    public Result deleteDet(String id, HttpServletRequest request){
        PackageDetail packageDetail = commonService.get(PackageDetail.class, id);
        commonService.delete(packageDetail);
        return new Result(true, "success");
    }
    
    @RequestMapping(value="/getpackage")
    @ResponseBody
    public Result getPackage(String packageID){
        Package pack = commonService.get(Package.class, packageID);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("projectName", pack.getProjectName());
        map.put("teamName", pack.getTeamName());
        
        return new Result(true, map);
    }
    
    @RequestMapping(value="/getteam")
    @ResponseBody
    public Result getPoint(String projectID){
        return new Result(true, JSONArray.toJSONString(commonService.getTeamComboboxByProject(projectID)));
    }
    
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {
        Package pack = commonService.get(Package.class, id);
        pack.setStatus("20");
        pack.setUpdateDateTime(new Date());
        
        commonService.update(pack);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "申请包工包料预算，项目：" + pack.getProjectName() +",班组：" + pack.getTeamName();
        
        //businessKey
        String businessKey = pack.getId();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey("packBudget", name, variables,
                user.getId(), businessKey);
    }
    
}
