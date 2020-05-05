/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.controller.workmanage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.project.SocialSecurity;
import com.radish.master.service.CommonService;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2019年5月13日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/social")
public class SocialSecurityController {

    @Resource
    private CommonService commonService;
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "workmanage/socialsecurity/list";
    }
    
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("regionCodes", JSONArray.toJSONString(commonService.getRegionCode()));
        return "workmanage/socialsecurity/edit";
    }
    
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id, HttpServletRequest request){
        request.setAttribute("regionCodes", JSONArray.toJSONString(commonService.getRegionCode()));
        request.setAttribute("id", id);
        return "workmanage/socialsecurity/edit";
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(SocialSecurity socialSecurity, HttpServletRequest request){
        socialSecurity.setUpdateDateTime(new Date());
        
        if(Double.valueOf(socialSecurity.getRadix()) > Double.valueOf(socialSecurity.getAvg())){
            return new Result(false,"社保基数不可大于上年平均工资 !");
        }
        
        try {
            if (StrUtil.isEmpty(socialSecurity.getId())) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.valueOf(socialSecurity.getYear()));
                cal.set(Calendar.MONTH, 1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                
                String startDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
                
                cal.set(Calendar.MONTH, 12);
                cal.set(Calendar.DAY_OF_MONTH, 31);
                
                String endDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
                
                socialSecurity.setStartTime(startDate);
                socialSecurity.setEndTime(endDate);
                
                commonService.save(socialSecurity);
            }else{
                SocialSecurity oldSocialSecurity = commonService.get(SocialSecurity.class, socialSecurity.getId());
                
                SpringUtil.copyPropertiesIgnoreNull(socialSecurity, oldSocialSecurity);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.valueOf(socialSecurity.getYear()));
                cal.set(Calendar.MONTH, 1);
                cal.set(Calendar.DAY_OF_MONTH, 0);
                
                String startDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
                
                cal.set(Calendar.MONTH, 11);
                cal.set(Calendar.DAY_OF_MONTH, 31);
                
                String endDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
                
                oldSocialSecurity.setStartTime(startDate);
                oldSocialSecurity.setEndTime(endDate);
                
                commonService.update(oldSocialSecurity);
            }
            
        } catch (Exception e) {
            return new Result(false,"保存失败！社保年限不可重复");
        }
        return new Result(true, socialSecurity);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    public SocialSecurity getMechInfo(String id){
        return commonService.get(SocialSecurity.class, id);
    }
}
