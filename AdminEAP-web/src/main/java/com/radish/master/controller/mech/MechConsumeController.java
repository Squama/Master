/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.mech;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.mech.MechConsume;
import com.radish.master.entity.mech.MechConsumeDetail;
import com.radish.master.service.BudgetService;
import com.radish.master.service.MechService;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年4月9日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/mech/consume")
public class MechConsumeController {

    @Resource
    private MechService mechService;
    
    @Resource
    private BudgetService budgetService;
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "mech/consume/list";
    }
    
    @RequestMapping(value="/detailouter/{id}",method = RequestMethod.GET)
    public String detailOuter(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        return "mech/consume/detail";
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        return "mech/consume/activiti_detail";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    public MechConsume getMechInfo(String id){
        return mechService.get(MechConsume.class, id);
    }
    
    @RequestMapping(value="/getmech")
    @ResponseBody
    public Result getMech(String projectID){
        return new Result(true, JSONArray.toJSONString(mechService.getMechComboboxByProject(projectID)));
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        
        return "mech/consume/step1";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/edit/{id}",method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id,HttpServletRequest request) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "mech/consume/step1";
    }
    
    @VerifyCSRFToken
    @RequestMapping(value="/savemech")
    @ResponseBody
    public Result saveMech(MechConsume mech, HttpServletRequest request){
        if(StrUtil.isEmpty(mech.getId())){
            mech.setCreateDateTime(new Date());
            mech.setUpdateDateTime(new Date());
            mech.setStatus("10");
            mech.setOperator(SecurityUtil.getUser().getName());
            mech.setOperatorID(SecurityUtil.getUserId());
            mechService.save(mech);
        }else{
            MechConsume oldMech = mechService.get(MechConsume.class, mech.getId());
            SpringUtil.copyPropertiesIgnoreNull(mech, oldMech);
            oldMech.setUpdateDateTime(new Date());
            mech.setOperator(SecurityUtil.getUser().getName());
            mech.setOperatorID(SecurityUtil.getUserId());
            mechService.save(oldMech);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", mech.getId());
        map.put("projectID", mech.getProjectID());
        map.put("projectName", mech.getProjectName());
        map.put("mechID", mech.getMechID());
        return new Result(true, map);
    }
    
    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        
        String hql = "from MechConsumeDetail where entryID=:entryID";
        Map<String, Object> params = new HashMap<>();
        params.put("entryID", id);
        List<MechConsumeDetail> detailList = mechService.find(hql, params);
        if(detailList.isEmpty()){
            try {
                MechConsume mech = mechService.get(MechConsume.class, id);
                mechService.delete(mech);
            } catch (Exception e) {
                return new Result(false);
            }
            return new Result(true);
        }else{
            return new Result(false);
        }
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/step2",method = RequestMethod.POST)
    public String step2(MechConsume mech, HttpServletRequest request){
        request.setAttribute("id", mech.getId());
        request.setAttribute("projectID", mech.getProjectID());
        request.setAttribute("projectName", mech.getProjectName());
        request.setAttribute("consumeOptions", JSONArray.toJSONString(mechService.getConsumeCombobox(mech.getMechID())));
        return "mech/consume/step2";
    }
    
    @VerifyCSRFToken
    @RequestMapping(value="/savedetail")
    @ResponseBody
    public Result saveDetail(MechConsumeDetail detail, HttpServletRequest request){
        detail.setCreateDateTime(new Date());
        detail.setUpdateDateTime(new Date());
        try {
            mechService.save(detail);
        } catch (Exception e) {
            return new Result(false);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", detail.getId());
        return new Result(true, map);
    }
    
    @RequestMapping(value="/deletedetail/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result deleteDetail(@PathVariable("id") String id){
        
        MechConsumeDetail detail = mechService.get(MechConsumeDetail.class, id);
        try {
            mechService.delete(detail);
        } catch (Exception e) {
            return new Result(false);
        }
        return new Result(true);
    }
    
    @VerifyCSRFToken
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {
        return mechService.startConsumeFlow(mechService.get(MechConsume.class, id),"mech");
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/check")
    @ResponseBody
    public Result validateChannel(String businessKey) {
    	MechConsume mc = mechService.get(MechConsume.class, businessKey);
        
        if("20".equals(mc.getStatus())){
            return new Result(false,"质量员、安全员和施工员尚未全部同意！！");
        }else{
            return new Result(true);
        }
    }
    
}
