/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.project;

import java.util.Date;
import java.util.HashMap;
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
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Labor;
import com.radish.master.service.BudgetService;
import com.radish.master.service.ProjectService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年3月5日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/labor")
public class LaborController {
    
    @Resource
    private ProjectService projectService;
    
    @Resource
    private BudgetService budgetService;

    private static final String projectFilePath=PropertiesUtil.getValue("projectFilePath");
    
    public static Map fileIconMap=new HashMap();
    
    static {
        fileIconMap.put("doc" ,"<i class='fa fa-file-word-o text-primary'></i>");
        fileIconMap.put("docx","<i class='fa fa-file-word-o text-primary'></i>");
        fileIconMap.put("xls" ,"<i class='fa fa-file-excel-o text-success'></i>");
        fileIconMap.put("xlsx","<i class='fa fa-file-excel-o text-success'></i>");
        fileIconMap.put("ppt" ,"<i class='fa fa-file-powerpoint-o text-danger'></i>");
        fileIconMap.put("pptx","<i class='fa fa-file-powerpoint-o text-danger'></i>");
        fileIconMap.put("jpg" ,"<i class='fa fa-file-photo-o text-warning'></i>");
        fileIconMap.put("pdf" ,"<i class='fa fa-file-pdf-o text-danger'></i>");
        fileIconMap.put("zip" ,"<i class='fa fa-file-archive-o text-muted'></i>");
        fileIconMap.put("rar" ,"<i class='fa fa-file-archive-o text-muted'></i>");
        fileIconMap.put("default" ,"<i class='fa fa-file-o'></i>");
    }
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "projectmanage/labor/labor_list";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        
        return "projectmanage/labor/labor_add";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/edit/{id}",method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/labor/labor_edit";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/uploadfile",method = RequestMethod.POST)
    public String step2(Labor labor, HttpServletRequest request){
        request.setAttribute("id", labor.getId());
        return "projectmanage/labor/labor_add_step2";
    }
    
    @RefreshCSRFToken
    @RequestMapping(value="/edituploadfile",method = RequestMethod.POST)
    public String editstep2(Labor labor, HttpServletRequest request){
        request.setAttribute("id", labor.getId());
        return "projectmanage/labor/labor_edit_step2";
    }
    
    @VerifyCSRFToken
    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(Labor labor, HttpServletRequest request){
        
        
        if(StrUtil.isEmpty(labor.getId())){
            labor.setCreateDateTime(new Date());
            labor.setContractPrice("0");
            projectService.save(labor);
        }else{
            Labor oldLabor = projectService.get(Labor.class, labor.getId());
            oldLabor.setProjectName(labor.getProjectName());
            oldLabor.setConstructionManager(labor.getConstructionManager());
            oldLabor.setUpdateDateTime(new Date());
            projectService.update(oldLabor);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", labor.getId());
        return new Result(true, map);
    }
    
}
