/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.controller.fixedassets;

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
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.Org;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.SecurityUtil;
import com.radish.master.entity.fixedassets.FixedAssetsPur;
import com.radish.master.entity.fixedassets.FixedAssetsPurTx;
import com.radish.master.entity.fixedassets.FixedAssetsStk;
import com.radish.master.pojo.AssetsApplyVO;
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
@RequestMapping("/fixedassets/apply")
public class FixedAssetsApplyController {
    
    @Resource
    private CommonService commonService;
    
    @Resource
    private RuntimePageService runtimePageService;

    /**
     * 固定资产start
     */
    
    @RequestMapping(value="/assets/list",method = RequestMethod.GET)
    public String assetsList(HttpServletRequest request){
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDeptCombobox()));
        return "fixedassets/apply/assets_list";
    }
    
    /** 固定资产end */
    
    @RequestMapping(value="/tool/list",method = RequestMethod.GET)
    public String toolList(HttpServletRequest request){
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDeptCombobox()));
        return "fixedassets/apply/tool_list";
    }
    
    @RequestMapping(value="/office/list",method = RequestMethod.GET)
    public String officeList(HttpServletRequest request){
        request.setAttribute("deptOptions", JSONArray.toJSONString(commonService.getDeptCombobox()));
        return "fixedassets/apply/office_list";
    }
    
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String assetsAdd(HttpServletRequest request){
        String deptID = SecurityUtil.getUser().getDeptId();
        Org org = commonService.get(Org.class, deptID);
        if(org == null){
            request.setAttribute("deptID", "nondept");
            request.setAttribute("deptName", "nondept");
        }else{
            request.setAttribute("deptID", deptID);
            request.setAttribute("deptName", org.getName());
        }
        
        return "fixedassets/apply/edit";
    }
    
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String assetsEdit(String id,String faType, HttpServletRequest request){
        request.setAttribute("purID", id);
        request.setAttribute("faType", faType);
        /*String deptID = SecurityUtil.getUser().getDeptId();
        Org org = commonService.get(Org.class, deptID);
        if(org == null){
            request.setAttribute("deptID", "nondept");
            request.setAttribute("deptName", "nondept");
        }else{
            request.setAttribute("deptID", deptID);
            request.setAttribute("deptName", org.getName());
        }*/
        
        return "fixedassets/apply/edit";
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request){
        request.setAttribute("purID", id);
        
        return "fixedassets/apply/detail";
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(FixedAssetsPur fap, HttpServletRequest request){
        fap.setStatus("10");
        fap.setUpdateDateTime(new Date());
        fap.setCreateDateTime(new Date());
        fap.setApplyer(SecurityUtil.getUser().getName());
        
        try {
            commonService.save(fap);
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系系统管理员！");
        }
        return new Result(true, fap);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/deletedetail")
    @ResponseBody
    public Result deleteDet(String id, HttpServletRequest request){
        FixedAssetsPurTx tx = commonService.get(FixedAssetsPurTx.class, id);
        commonService.delete(tx);
        return new Result(true, "success");
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/recentstk/save")
    @ResponseBody
    public Result recentstkSave(AssetsApplyVO vo, HttpServletRequest request){
        
        if("10".equals(vo.getFaType())){
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("purID", vo.getPurID());
            List<FixedAssetsPurTx> list = commonService.find("from FixedAssetsPurTx where purID = :purID", params);
            if(!list.isEmpty()){
                return new Result(false,"固定资产只能有一条明细");
            }
        }
        
        FixedAssetsStk stk = commonService.get(FixedAssetsStk.class, vo.getAssetsStkID());
        
        FixedAssetsPurTx tx = new FixedAssetsPurTx();
        tx.setStkID(vo.getAssetsStkID());
        tx.setPurID(vo.getPurID());
        tx.setName(stk.getName());
        tx.setEnglishName(stk.getEnglishName());
        tx.setModel(stk.getModel());
        tx.setNorm(stk.getNorm());
        tx.setUnit(stk.getUnit());
        tx.setPrice(stk.getPrice());
        tx.setQuantity(vo.getNum());
        tx.setVendor(stk.getVendor());
        tx.setUpdateDateTime(new Date());
        tx.setCreateDateTime(new Date());
        
        try {
            commonService.save(tx);
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系系统管理员！");
        }
        return new Result(true, tx);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/newstk/save")
    @ResponseBody
    public Result newSave(FixedAssetsPurTx tx, HttpServletRequest request){
        
        if("10".equals(tx.getFaType())){
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("purID", tx.getPurID());
            List<FixedAssetsPurTx> list = commonService.find("from FixedAssetsPurTx where purID = :purID", params);
            if(!list.isEmpty()){
                return new Result(false,"固定资产只能有一条明细");
            }
        }
        
        tx.setUpdateDateTime(new Date());
        tx.setCreateDateTime(new Date());
        
        try {
            commonService.save(tx);
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系系统管理员！");
        }
        return new Result(true, tx);
    }
    
    @RequestMapping(value="/addstock",method = RequestMethod.GET)
    public String addStock(String faType, String purID, HttpServletRequest request){
        
        request.setAttribute("faType", faType);
        request.setAttribute("purID", purID);
        request.setAttribute("stkOptions", JSONArray.toJSONString(commonService.getAssetsCombobox(faType)));
        
        return "fixedassets/apply/recent_stk";
    }
    
    @RequestMapping(value="/newstock",method = RequestMethod.GET)
    public String newStock(String faType, String purID, HttpServletRequest request){
        
        request.setAttribute("faType", faType);
        request.setAttribute("purID", purID);
        
        return "fixedassets/apply/new_stk";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getstkdet")
    @ResponseBody
    public FixedAssetsStk getStkDet(String stkID){
        return commonService.get(FixedAssetsStk.class, stkID);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getpur")
    @ResponseBody
    public FixedAssetsPur getPur(String purID){
        return commonService.get(FixedAssetsPur.class, purID);
    }
    
}
