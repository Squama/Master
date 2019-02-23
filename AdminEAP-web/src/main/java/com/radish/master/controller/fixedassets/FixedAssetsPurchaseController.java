/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.controller.fixedassets;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.fixedassets.FixedAssetsPur;
import com.radish.master.entity.fixedassets.FixedAssetsPurTx;
import com.radish.master.entity.fixedassets.FixedAssetsStk;
import com.radish.master.entity.fixedassets.FixedAssetsStkTx;
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
    
    @RequestMapping(value = "/assets/acceptance", method = RequestMethod.POST)
    @ResponseBody
    public Result singleEstimate(String purID, HttpServletRequest request) throws Exception {
        
        FixedAssetsPur pur = commonService.get(FixedAssetsPur.class, purID);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("purID", purID);
        List<FixedAssetsPurTx> list = commonService.find("from FixedAssetsPurTx where purID = :purID", params);
        
        if(list.isEmpty() || list.size() > 1){
            return new Result(false, "采购单不合规，请联系系统管理员"); 
        }
        
        FixedAssetsPurTx purTx = list.get(0);
        FixedAssetsStk stk;
        if(StrUtil.isEmpty(purTx.getStkID())){
            stk = new FixedAssetsStk();
            stk.setName(purTx.getName());
            stk.setEnglishName(purTx.getEnglishName());
            stk.setModel(purTx.getModel());
            stk.setBelongedStock("总库");
            stk.setKeepedDeptID(pur.getDeptID());
            stk.setKeepedDeptName(pur.getDeptName());
            stk.setNorm(purTx.getNorm());
            stk.setUnit(purTx.getUnit());
            stk.setPrice(purTx.getPrice());
            stk.setQuantity(purTx.getQuantity());
            stk.setQuantityAvl(purTx.getQuantity());
            stk.setVendor(purTx.getVendor());
            stk.setFaType("10");
        }else{
            stk = commonService.get(purTx.getStkID());
            BigDecimal oldQuantity = new BigDecimal(stk.getQuantity());
            BigDecimal oldQuantityAvl = new BigDecimal(stk.getQuantityAvl());
            BigDecimal increment = new BigDecimal(purTx.getQuantity());
            
            stk.setQuantity(oldQuantity.add(increment).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
            stk.setQuantityAvl(oldQuantityAvl.add(increment).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        }
        
        commonService.save(stk);
        
        FixedAssetsStkTx stkTx = new FixedAssetsStkTx();
        stkTx.setFixedAssetsID(stk.getId());
        stkTx.setOperation("10");//入库
        stkTx.setAmount(purTx.getQuantity());
        stkTx.setBalance(stk.getQuantityAvl());
        stkTx.setPrice(purTx.getPrice());
        stkTx.setRemark("固定资产验收入库");
        stkTx.setOperator(SecurityUtil.getUser().getName());
        stkTx.setOperateTime(new Date());
        stkTx.setSourceTxID(purTx.getId());
        
        pur.setStatus("60");
        
        commonService.save(stkTx);
        commonService.save(pur);
        
        return new Result(true, "验收入库成功");
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
