/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.controller.fixedassets;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.radish.master.service.CommonService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2019年1月11日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/fixedassets/purchase")
public class FixedAssetsPurchaseController {

    @Resource
    private CommonService commonService;
    
    /**
     * 固定资产start
     */
    
    @RequestMapping(value="/assets/list",method = RequestMethod.GET)
    public String assetsList(HttpServletRequest request){
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDeptCombobox()));
        return "fixedassets/purchase/assets_list";
    }
    
    /** 固定资产end */
    
    /** 器具、工具start */
    @RequestMapping(value="/tool/list",method = RequestMethod.GET)
    public String toolList(HttpServletRequest request){
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDeptCombobox()));
        return "fixedassets/purchase/tool_list";
    }
    
    /** 器具、工具end */
    
    /** 办公用品start */
    @RequestMapping(value="/office/list",method = RequestMethod.GET)
    public String officeList(HttpServletRequest request){
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDeptCombobox()));
        return "fixedassets/purchase/office_list";
    }
    
    /** 办公用品end */
    
}
