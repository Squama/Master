/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司  
 */
package com.radish.master.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.radish.master.entity.Project;
import com.radish.master.entity.ProjectFileItem;
import com.radish.master.service.ProjectService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年11月13日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/projectfile")
public class ProjectFileController {

    @Resource
    private ProjectService projectService;
    
    private static final String RESPONSEURL = "projectmanage/projectfile/project_file";
    
    private static final String ITEMCODE = "itemCode";
    
    private static final String ITEMNAME = "itemName";
    
    private static final String ITEMCOLUMN = "itemColumn";
    
    @RequestMapping(value="/projectdetailfile", method = RequestMethod.GET)
    public String projectdetailfile(String id, String columnName, HttpServletRequest request) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Project project = projectService.get(Project.class, id);
        String name = columnName.substring(0,1).toUpperCase()+columnName.substring(1);
        Method m = project.getClass().getMethod("get"+name);
        String value = (String) m.invoke(project);
        
        List<ProjectFileItem> fileList=new ArrayList<>();
        DetachedCriteria criteria = DetachedCriteria.forClass(ProjectFileItem.class);
        criteria.add(Restrictions.eq("batchNo", value));
        criteria.addOrder(Order.asc("createDateTime"));
        fileList = projectService.findByCriteria(criteria);
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<fileList.size();i++){
            sb.append(",");
            sb.append(fileList.get(i).getId());
        }
        value = sb.toString().substring(1);
        
        request.setAttribute("fields", value);
        
        return "projectmanage/project/project_query_file";
    }
    
    @RequestMapping(value="/safty",method = RequestMethod.GET)
    public String safty(HttpServletRequest request){
        request.setAttribute(ITEMCOLUMN, "safty_file");
        request.setAttribute(ITEMCODE,"saftyFile");
        request.setAttribute(ITEMNAME, "安全文明文件");
        return RESPONSEURL;
    }
    
    @RequestMapping(value="/construction",method = RequestMethod.GET)
    public String construction(HttpServletRequest request){
        request.setAttribute(ITEMCOLUMN, "construction_file");
        request.setAttribute(ITEMCODE,"constructionFile");
        request.setAttribute(ITEMNAME, "施工合同");
        return RESPONSEURL;
    }
    
    @RequestMapping(value="/bids",method = RequestMethod.GET)
    public String bids(HttpServletRequest request){
        request.setAttribute(ITEMCOLUMN, "bids_file");
        request.setAttribute(ITEMCODE,"bidsFile");
        request.setAttribute(ITEMNAME, "招标文件");
        return RESPONSEURL;
    }
    
    @RequestMapping(value="/bidswin",method = RequestMethod.GET)
    public String bidsWin(HttpServletRequest request){
        request.setAttribute(ITEMCOLUMN, "bids_win_file");
        request.setAttribute(ITEMCODE,"bidsWinFile");
        request.setAttribute(ITEMNAME, "中标文件");
        return RESPONSEURL;
    }
    
    @RequestMapping(value="/bidswinnotice",method = RequestMethod.GET)
    public String bidsWinNotice(HttpServletRequest request){
        request.setAttribute(ITEMCOLUMN, "bids_win_notice_file");
        request.setAttribute(ITEMCODE,"bidsWinNoticeFile");
        request.setAttribute(ITEMNAME, "中标通知文件");
        return RESPONSEURL;
    }
    
    @RequestMapping(value="/quality",method = RequestMethod.GET)
    public String quality(HttpServletRequest request){
        request.setAttribute(ITEMCOLUMN, "quality_file");
        request.setAttribute(ITEMCODE,"qualityFile");
        request.setAttribute(ITEMNAME, "工程质量文件");
        return RESPONSEURL;
    }
    
    @RequestMapping(value="/cost",method = RequestMethod.GET)
    public String cost(HttpServletRequest request){
        request.setAttribute(ITEMCOLUMN, "cost_file");
        request.setAttribute(ITEMCODE,"costFile");
        request.setAttribute(ITEMNAME, "预算成本分析表");
        return RESPONSEURL;
    }
    
    @RequestMapping(value="/schedule",method = RequestMethod.GET)
    public String schedule(HttpServletRequest request){
        request.setAttribute(ITEMCOLUMN, "schedule_file");
        request.setAttribute(ITEMCODE,"scheduleFile");
        request.setAttribute(ITEMNAME, "工程进度文件");
        return RESPONSEURL;
    }
    
}
