/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.controller.fixedassets;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.radish.master.pojo.RowEdit;
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
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDepartmentCombobox()));
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
        stkTx.setOperator(SecurityUtil.getUser().getLoginName());
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
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDepartmentCombobox()));
        return "fixedassets/purchase/tool_list";
    }
    
    @RequestMapping(value = "/tool/acceptance", method = RequestMethod.POST)
    @ResponseBody
    public Result toolAcceptance(String purID, HttpServletRequest request) throws Exception {
        
        FixedAssetsPur pur = commonService.get(FixedAssetsPur.class, purID);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("purID", purID);
        List<FixedAssetsPurTx> list = commonService.find("from FixedAssetsPurTx where purID = :purID", params);
        
        if(list.isEmpty()){
            return new Result(false, "采购单不合规，请联系系统管理员"); 
        }
        
        for(FixedAssetsPurTx purTx : list){
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
                stk.setFaType("20");
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
            stkTx.setRemark("器具、工具一键入库");
            stkTx.setOperator(SecurityUtil.getUser().getLoginName());
            stkTx.setOperateTime(new Date());
            stkTx.setSourceTxID(purTx.getId());
            
            commonService.save(stkTx);
        }
        
        pur.setStatus("60");
        commonService.save(pur);
        
        return new Result(true, "一键入库成功");
    }
    
    @RequestMapping(value="/tool/partaccept",method = RequestMethod.GET)
    public String toolPart(String id, HttpServletRequest request){
        request.setAttribute("purID", id);
        
        return "fixedassets/purchase/part_acceptance";
    }
    
    /** 器具、工具end */
    
    /** 办公用品start */
    @RequestMapping(value="/office/list",method = RequestMethod.GET)
    public String officeList(HttpServletRequest request){
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDepartmentCombobox()));
        return "fixedassets/purchase/office_list";
    }
    
    @RequestMapping(value = "/office/acceptance", method = RequestMethod.POST)
    @ResponseBody
    public Result officeAcceptance(String purID, HttpServletRequest request) throws Exception {
        
        FixedAssetsPur pur = commonService.get(FixedAssetsPur.class, purID);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("purID", purID);
        List<FixedAssetsPurTx> list = commonService.find("from FixedAssetsPurTx where purID = :purID", params);
        
        if(list.isEmpty()){
            return new Result(false, "采购单不合规，请联系系统管理员"); 
        }
        
        for(FixedAssetsPurTx purTx : list){
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
                stk.setFaType("30");
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
            stkTx.setRemark("办公用品一键入库");
            stkTx.setOperator(SecurityUtil.getUser().getLoginName());
            stkTx.setOperateTime(new Date());
            stkTx.setSourceTxID(purTx.getId());
            
            commonService.save(stkTx);
        }
        
        pur.setStatus("60");
        commonService.save(pur);
        
        return new Result(true, "一键入库成功");
    }
    
    
    @RequestMapping(value="/office/partaccept",method = RequestMethod.GET)
    public String officePart(String id, HttpServletRequest request){
        request.setAttribute("purID", id);
        
        return "fixedassets/purchase/part_acceptance";
    }
    /** 办公用品end */
    
    @RequestMapping(value = "/rowedit", method = RequestMethod.POST)
    @ResponseBody
    public String doPartStk(String action, HttpServletRequest request) throws Exception {
        String id = "";
        String field = "";
        String value = "";
        
        RowEdit re = new RowEdit();
        List<Object> list = new ArrayList<Object>();
        
        Enumeration<String> key = request.getParameterNames();
        while (key.hasMoreElements()) {
            String paramName = (String) key.nextElement();
            if ("action".equals(paramName)) {
                continue;
            }
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    int idIndexStart = paramName.indexOf("[");
                    int idIndexEnd = paramName.indexOf("]");
                    int fieldIndexStart = paramName.lastIndexOf("[");
                    int fieldIndexEnd = paramName.lastIndexOf("]");
                    id = paramName.substring(idIndexStart + 1, idIndexEnd);
                    field = paramName.substring(fieldIndexStart + 1, fieldIndexEnd);
                    value = paramValue;
                }
            }
        }

        if (!isNumber(value)) {
            list.add(new FixedAssetsPurTx());
            re.setData(list);
            return JSONArray.toJSONString(re);
        }
        
        FixedAssetsPurTx purTx = commonService.get(FixedAssetsPurTx.class, id);
        FixedAssetsPur pur = commonService.get(FixedAssetsPur.class, purTx.getPurID());
        
        BigDecimal quantity = new BigDecimal(purTx.getQuantity());
        BigDecimal quantityStk = new BigDecimal(purTx.getQuantityStk()==null?"0":purTx.getQuantityStk());
        BigDecimal incrementPur = new BigDecimal(value);
        
        if(quantityStk.add(incrementPur).compareTo(quantity) == 1){
            list.add(new FixedAssetsPurTx());
            re.setData(list);
            return JSONArray.toJSONString(re);
        }
        
        purTx.setQuantityStk(quantityStk.add(incrementPur).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        
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
            stk.setFaType(pur.getFaType());
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
        stkTx.setRemark("部分入库");
        stkTx.setOperator(SecurityUtil.getUser().getLoginName());
        stkTx.setOperateTime(new Date());
        stkTx.setSourceTxID(purTx.getId());
        
        commonService.save(stkTx);
        commonService.save(purTx);
        
        list.add(purTx);
        re.setData(list);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("purID", purTx.getPurID());
        List<FixedAssetsPurTx> purTxList = commonService.find("from FixedAssetsPurTx where purID = :purID", params);
        boolean allIN = true;
        for(FixedAssetsPurTx tx : purTxList){
            BigDecimal qstk = new BigDecimal(tx.getQuantityStk()==null?"0":tx.getQuantityStk());
            BigDecimal qua = new BigDecimal(tx.getQuantity());
            if(qstk.compareTo(qua) == -1){
                allIN = false;
            }
        }
        
        if(allIN){
            pur.setStatus("60");
        }else{
            pur.setStatus("50");
        }
        
        commonService.save(pur);
        
        return JSONArray.toJSONString(re);
    }
    
    private boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }
    
}
