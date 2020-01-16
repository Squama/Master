/**
 * 
 */
package com.radish.master.controller.fixedassets;

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
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.fixedassets.FixedAssetsAllocate;
import com.radish.master.entity.fixedassets.FixedAssetsStk;
import com.radish.master.service.CommonService;
import com.radish.master.service.fixedassets.FixedAssetsService;
import com.radish.master.system.SpringUtil;

/**
 * @author tonyd
 *
 */
@Controller
@RequestMapping("/fixedassets/allocate")
public class FixedAssetsAllocateController {
	
	@Resource
    private CommonService commonService;
	
	@Resource
    private FixedAssetsService fixedAssetsService;

	@Resource
    private RuntimePageService runtimePageService;
	
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String assetsList(HttpServletRequest request){
    	request.setAttribute("userName", SecurityUtil.getUser().getName());
        return "fixedassets/allocate/list";
    }
    
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String assetsAdd(HttpServletRequest request){
    	request.setAttribute("number", System.currentTimeMillis()+"");//先用时间戳
        request.setAttribute("operatorName", SecurityUtil.getUser().getName());
        request.setAttribute("stkOptions", JSONArray.toJSONString(fixedAssetsService.getAllocateAssetsCombobox()));
//        request.setAttribute("deptOptions", JSONArray.toJSONString(fixedAssetsService.getDeptNameCombobox()));
        request.setAttribute("deptOptions", JSONArray.toJSONString(fixedAssetsService.getDeptNameComboboxByXm()));
        request.setAttribute("userOptions", JSONArray.toJSONString(commonService.getUserCombobox()));
        
        return "fixedassets/allocate/edit";
    }
    
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String assetsEdit(String id, HttpServletRequest request){
        request.setAttribute("id", id);
        request.setAttribute("operatorName", SecurityUtil.getUser().getName());
        request.setAttribute("stkOptions", JSONArray.toJSONString(fixedAssetsService.getAllocateAssetsCombobox()));
//        request.setAttribute("deptOptions", JSONArray.toJSONString(fixedAssetsService.getDeptNameCombobox()));
        request.setAttribute("deptOptions", JSONArray.toJSONString(fixedAssetsService.getDeptNameComboboxByXm()));
        request.setAttribute("userOptions", JSONArray.toJSONString(commonService.getUserCombobox()));
        
        return "fixedassets/allocate/edit";
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request){
        request.setAttribute("id", id);
        
        return "fixedassets/allocate/detail";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getsourcestk")
    @ResponseBody
    public Result getStkDet(String stkID){
//    	return new Result(true, JSONArray.toJSONString(fixedAssetsService.getAllocateStkExitsCombobox(stkID)));
        return new Result(true, JSONArray.toJSONString(fixedAssetsService.getAllocateStkExitsComboboxByXm(stkID)));
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(FixedAssetsAllocate faa, HttpServletRequest request){
    	//时间判断
    	if(faa.getStartDate()!=null&&faa.getEndDate()!=null){
    		if(faa.getStartDate().after(faa.getEndDate())){
    			return new Result(false,"结束日期不能早于开始日期");
    		}
    	}
    	faa.setStatus("10");
        
        FixedAssetsStk stk = commonService.get(FixedAssetsStk.class, faa.getStkID());
        faa.setName(stk.getName());
        faa.setEnglishName(stk.getEnglishName());
        faa.setModel(stk.getModel());
        faa.setNorm(stk.getNorm());
        faa.setUnit(stk.getUnit());
        faa.setPrice(stk.getPrice());
        faa.setVendor(stk.getVendor());
        faa.setFaType(stk.getFaType());
        
        if(Double.valueOf(faa.getQuantity()) > Double.valueOf(stk.getQuantityAvl())){
        	return new Result(false,"保存失败，调拨数量大于可用数量！可用数量："+stk.getQuantityAvl());
        }
        
        try {
            if(StrUtil.isEmpty(faa.getId())){
            	faa.setUpdateDateTime(new Date());
            	faa.setCreateDateTime(new Date());
                commonService.save(faa);
            }else{
                FixedAssetsAllocate oldFixedAssetsAllocate = commonService.get(FixedAssetsAllocate.class, faa.getId());

				SpringUtil.copyPropertiesIgnoreNull(faa, oldFixedAssetsAllocate);

				oldFixedAssetsAllocate.setUpdateDateTime(new Date());

				commonService.update(oldFixedAssetsAllocate);
            }
            
        } catch (Exception e) {
            return new Result(false,"保存失败，请联系系统管理员！");
        }
        return new Result(true, faa);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/getallocate")
    @ResponseBody
    public FixedAssetsAllocate getAllocate(String id){
    	FixedAssetsAllocate fixedAssetsAllocate = commonService.get(FixedAssetsAllocate.class, id);
        return fixedAssetsAllocate;
    }
    
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {
    	FixedAssetsAllocate fixedAssetsAllocate = commonService.get(FixedAssetsAllocate.class, id);
    	fixedAssetsAllocate.setStatus("20");
    	fixedAssetsAllocate.setUpdateDateTime(new Date());
        
        commonService.update(fixedAssetsAllocate);
        
        //给流程起个名字
        User user = SecurityUtil.getUser();
        String name = user.getName() + "申请固定资产工具调拨";
        
        //businessKey
        String businessKey = fixedAssetsAllocate.getId();
        
        //配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);
        
        
        //启动流程
        return runtimePageService.startProcessInstanceByKey("fixedassetsAllocate", name, variables,
                user.getId(), businessKey);
    }
    @RequestMapping(method = RequestMethod.POST, value="/delete")
    @ResponseBody
    public Result delete(String id){
    	FixedAssetsAllocate oldFixedAssetsAllocate = commonService.get(FixedAssetsAllocate.class, id);
    	commonService.delete(oldFixedAssetsAllocate);
    	 return new Result(true,"删除成功");
    }
    
}
