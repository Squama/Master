/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.activiti.pojo.ActivityVo;
import com.cnpc.framework.activiti.pojo.TaskVo;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.activiti.service.TaskPageService;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.PageInfo;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.cnpc.framework.query.entity.QueryCondition;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.project.LaborSub;
import com.radish.master.entity.project.ProjectTeam;
import com.radish.master.entity.qualityCheck.CheckFkd;
import com.radish.master.pojo.Options;
import com.radish.master.service.BudgetService;
import com.radish.master.service.ProjectService;
import com.radish.master.system.Arith;
import com.radish.master.system.SpringUtil;

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
@RequestMapping("/projectvolume")
public class ProjectVolumeController {
    
    @Resource
    private ProjectService projectService;
    
    @Resource
    private BudgetService budgetService;
    
    @Resource
    private TaskPageService taskPageService;
    
    @Resource
    private RuntimePageService runtimePageService;
    
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(HttpServletRequest request){
    	request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        return "projectmanage/volume/volume_list";
    }
    
    @RequestMapping(value="/detaillist",method = RequestMethod.GET)
    public String detailList(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        List<ProjectTeam> htbzs = projectService.find(" from ProjectTeam where 1=1 ");
        request.setAttribute("htbzs", JSONArray.toJSONString(htbzs));
        return "projectmanage/volume/detail_list";
    }
    
    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request){
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        String sblx = request.getParameter("sblx");
        if(sblx!=null){
        	if("10".equals(sblx)){
        		return "projectmanage/volumeRg/volume_edit";
        	}else if("30".equals(sblx)){
        		return "projectmanage/volumeJx/volume_edit";
        	}
        }
        return "projectmanage/volume/volume_edit";
    }
    
    @RequestMapping(value="/edit/{id}",method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        
        String sblx = request.getParameter("sblx");
        if(sblx!=null){
        	if("10".equals(sblx)){
        		return "projectmanage/volumeRg/volume_edit";
        	}else if("30".equals(sblx)){
        		return "projectmanage/volumeJx/volume_edit";
        	}
        }
        
        return "projectmanage/volume/volume_edit";
    }
    
    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        ProjectVolume sb = projectService.get(ProjectVolume.class, id);
        Labor ht = projectService.get(Labor.class, sb.getLaborID());
        request.setAttribute("bzid", ht.getConstructionTeamID());
        String sblx = sb.getSblx();
        if(sblx!=null){
        	if("10".equals(sblx)){
        		return "projectmanage/volumeRg/volume_detail";
        	}else if("30".equals(sblx)){
        		return "projectmanage/volumeJx/volume_detail";
        	}
        }
        return "projectmanage/volume/volume_detail";
    }
    
    @RequestMapping(value="/detailbusiness/{id}",method = RequestMethod.GET)
    public String detailBusiness(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        ProjectVolume sb = projectService.get(ProjectVolume.class, id);
        Labor ht = projectService.get(Labor.class, sb.getLaborID());
        request.setAttribute("bzid", ht.getConstructionTeamID());
        String sblx = sb.getSblx();
        if(sblx!=null){
        	if("10".equals(sblx)){
        		return "projectmanage/volumeRg/volume_detail_business";
        	}else if("30".equals(sblx)){
        		return "projectmanage/volumeJx/volume_detail_business";
        	}
        }
        return "projectmanage/volume/volume_detail_business";
    }
    
    @RequestMapping(value="/detailfinal/{id}",method = RequestMethod.GET)
    public String detailFinal(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        ProjectVolume sb = projectService.get(ProjectVolume.class, id);
        Labor ht = projectService.get(Labor.class, sb.getLaborID());
        request.setAttribute("bzid", ht.getConstructionTeamID());
        String sblx = sb.getSblx();
        if(sblx!=null){
        	if("10".equals(sblx)){
        		return "projectmanage/volumeRg/volume_detail_final";
        	}else if("30".equals(sblx)){
        		return "projectmanage/volumeJx/volume_detail_final";
        	}
        }
        return "projectmanage/volume/volume_detail_final";
    }
    
    @RequestMapping(value="/detailouter/{id}",method = RequestMethod.GET)
    public String detailOuter(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(budgetService.getProjectCombobox()));
        ProjectVolume sb = projectService.get(ProjectVolume.class, id);
        String zt = sb.getStatus();
        if("50".equals(zt)||"60".equals(zt)){//显示上报、经营科金额框
        	request.setAttribute("sbxs", "2");
        }else if("70".equals(zt)){//显示全部金额框
        	request.setAttribute("sbxs", "3");
        }else{//只显示上报金额框
        	request.setAttribute("sbxs", "1");
        }
        String sblx = sb.getSblx();
        if(sblx!=null){
        	if("10".equals(sblx)){
        		return "projectmanage/volumeRg/detail_outer";
        	}else if("30".equals(sblx)){
        		return "projectmanage/volumeJx/detail_outer";
        	}
        }
        return "projectmanage/volume/detail_outer";
    }
    
    @RequestMapping(value="/check",method = RequestMethod.POST)
    @ResponseBody
    public Result check(String businessKey){
        ProjectVolume pv = projectService.get(ProjectVolume.class, businessKey);
        
        if("20".equals(pv.getStatus())){
            return new Result(false,"质量员和施工员尚未全部同意！！");
        }else{
            return new Result(true);
        }
    }
    
    @RequestMapping(value="/checkconstworker",method = RequestMethod.POST)
    @ResponseBody
    public Result checkConstWorker(String userID, String userName){
        User currentUser = SecurityUtil.getUser();
        
        if(!userID.equals(currentUser.getId())){
            return new Result(false,"本分项只可由施工员【"+userName+"】进行上报！");
        }else{
            return new Result(true);
        }
    }
    
    @RequestMapping(value="/getlabor")
    @ResponseBody
    public Result getLabor(String projectID,String sblx){
    	String sql = "select id value, contract_name data from tbl_labor where project_id=? AND Status='30'";
    	//机械上报取机械合同
    	if(sblx!=null){
    		if("30".equals(sblx)){
    			sql += " AND htlx = '30' ";
    		}else if("10".equals(sblx)){
    			sql += " AND htlx = '10' ";
    		}
    		
    	}else{
    		sql +=" AND (htlx='20' or htlx is null)  ";
    	}
    	List<Options> hts = projectService.findMapBySql(
    			sql,
    			new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    	
        return new Result(true, JSONArray.toJSONString(hts));
    }
    
    @RequestMapping(value="/getlaborsub")
    @ResponseBody
    public Result getLaborSub(String laborID){
        return new Result(true, JSONArray.toJSONString(projectService.getLaborSubComboboxByLabor(laborID)));
    }
    
    @RequestMapping(value="/getprojectsub")
    @ResponseBody
    public Result getProjectSub(String projectID){
        return new Result(true, JSONArray.toJSONString(projectService.getProjectSubCombobox(projectID)));
    }
    
    @RequestMapping(value="/getpack")
    @ResponseBody
    public Result getPack(String laborID){
        Labor labor = projectService.get(Labor.class, laborID);
        return new Result(true, JSONArray.toJSONString(projectService.getPackCombobox(labor.getProjectID(), labor.getConstructionTeamID())));
    }
    
    @RequestMapping(value="/getpingxing")
    @ResponseBody
    public Result getPX(String id){
    	
    	List<Map<String, Object>> conditions = new ArrayList<Map<String, Object>>();
    	Map<String, String> map = new HashMap<String, String>();
    	String zhiliang = "info";
    	String zhiliangsugg = "info";
    	String anquan = "info";
    	String anquansugg = "info";
    	String shigong = "info";
    	String shigongsugg = "info";
    	
    	Map<String, Object> condition = new HashMap<String, Object>();
    	condition.put("key", "businessKey");
    	condition.put("value", id);
    	
    	conditions.add(condition);
    	
    	QueryCondition q = new QueryCondition();
    	q.setConditions(conditions);
    	
    	List<TaskVo> taskVOList = taskPageService.getTaskToDoList(q, new PageInfo());
    	if(taskVOList.size() > 0){
        	List<Map<String, Object>> conditionsR = new ArrayList<Map<String, Object>>();
        	
        	Map<String, Object> conditionR = new HashMap<String, Object>();
        	conditionR.put("key", "processInstanceId");
        	conditionR.put("value", taskVOList.get(0).getProcessInstanceId());
        	
        	conditionsR.add(conditionR);
        	
        	QueryCondition qc = new QueryCondition();
        	qc.setConditions(conditionsR);
        	
    		List<ActivityVo> list = runtimePageService.getActivityList(qc, new PageInfo());
    		for(ActivityVo vo : list){
    			if(vo.getActivityState() == "0"){
    				if("zhiliang".equals(vo.getActivityId())){
        				zhiliang=vo.getApproved();
        				zhiliangsugg=vo.getSuggestion();
        			}else if("anquan".equals(vo.getActivityId())){
        				anquan=vo.getApproved();
        				anquansugg=vo.getSuggestion();
        			}else if("shigong".equals(vo.getActivityId())){
        				shigong=vo.getApproved();
        				shigongsugg=vo.getSuggestion();
        			}
    			}
    		}
    	}
    	map.put("zhiliang", zhiliang);
    	map.put("zhiliangsugg", zhiliangsugg);
    	map.put("anquan", anquan);
    	map.put("anquansugg", anquansugg);
    	map.put("shigong", shigong);
    	map.put("shigongsugg", shigongsugg);
    	
        return new Result(true, map);
    }
    
    @RequestMapping(value="/getlaborinfo")
    @ResponseBody
    public Labor getLaborInfo(String laborID){
        return projectService.get(Labor.class, laborID);
    }
    
    @RequestMapping(value="/getlaborsubinfo")
    @ResponseBody
    public LaborSub getLaborSubInfo(String laborSubID){
        return projectService.get(LaborSub.class, laborSubID);
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/delete")
    @ResponseBody
    public Result delete(ProjectVolume projectVolume, HttpServletRequest request){
    	String id = request.getParameter("id");
    	ProjectVolume pv = projectService.get(ProjectVolume.class, id);
    	projectService.delete(pv);
    	return new Result();
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/save")
    @ResponseBody
    public Result save(ProjectVolume projectVolume, HttpServletRequest request){
        SimpleDateFormat myFormat=new SimpleDateFormat("yyyy-MM-dd");
        if(projectVolume.getStartTime().compareTo(projectVolume.getEndTime()) != -1){
            return new Result(false, "开始时间必须小于结束时间");
        }
        
        String startTime = myFormat.format(projectVolume.getStartTime()) + " 00:00:00";
        String endTime = myFormat.format(projectVolume.getEndTime()) + " 23:59:59";
        
        /*Calendar calendar = Calendar.getInstance();
        calendar.clear();
        
        calendar.set(projectVolume.getStartTime().getYear()+1900, projectVolume.getStartTime().getMonth()+1, projectVolume.getStartTime().getDay(), 0, 0, 0);
        String startTime = String.valueOf(calendar.getTimeInMillis());
        
        calendar.clear();
        calendar.set(projectVolume.getEndTime().getYear()+1900, projectVolume.getEndTime().getMonth()+1, projectVolume.getEndTime().getDay(), 23, 59, 59);
        String endTime = String.valueOf(calendar.getTimeInMillis());*/
        
        
        List<ProjectVolume> list = projectService.checkTimePeriod(projectVolume.getProjectID(), projectVolume.getLaborID(), projectVolume.getProjectSubID(), startTime, endTime, projectVolume.getId(),projectVolume.getSblx());
        if(!list.isEmpty()){
            return new Result(false, "上报时间段不可重叠");
        }
        
        if(StrUtil.isEmpty(projectVolume.getId())){
            projectVolume.setCreateDateTime(new Date());
            projectVolume.setCreateTime(new Date());
            projectVolume.setStatus("10");
            /*projectVolume.setBusinessMech(projectVolume.getApplyMech());
            projectVolume.setBusinessLabour(projectVolume.getApplyLabour());
            projectVolume.setBusinessMat(projectVolume.getApplyMat());
            projectVolume.setBusinessDebit(projectVolume.getApplyDebit());
            projectVolume.setBusinessDebitjx(projectVolume.getApplyDebitjx());
            projectVolume.setBusinessDebitcl(projectVolume.getApplyDebitcl());
            projectVolume.setBusinessPack(projectVolume.getApplyPack());
            projectVolume.setBusinessSub(projectVolume.getApplySub());
            projectVolume.setFinalMech(projectVolume.getApplyMech());
            projectVolume.setFinalLabour(projectVolume.getApplyLabour());
            projectVolume.setFinalMat(projectVolume.getApplyMat());
            projectVolume.setFinalDebit(projectVolume.getApplyDebit());
            projectVolume.setFinalDebitjx(projectVolume.getApplyDebitjx());
            projectVolume.setFinalDebitcl(projectVolume.getApplyDebitcl());
            projectVolume.setFinalPack(projectVolume.getApplyPack());
            projectVolume.setFinalSub(projectVolume.getApplySub());*/
            projectVolume.setBusinessMech("0");
            projectVolume.setBusinessLabour("0");
            projectVolume.setBusinessMat("0");
            projectVolume.setBusinessDebit("0");
            projectVolume.setBusinessDebitjx("0");
            projectVolume.setBusinessDebitcl("0");
            projectVolume.setBusinessPack("0");
            projectVolume.setBusinessSub("0");
            projectVolume.setFinalMech("0");
            projectVolume.setFinalLabour("0");
            projectVolume.setFinalMat("0");
            projectVolume.setFinalDebit("0");
            projectVolume.setFinalDebitjx("0");
            projectVolume.setFinalDebitcl("0");
            projectVolume.setFinalPack("0");
            projectVolume.setFinalSub("0");
            projectService.save(projectVolume);
        }else{
            ProjectVolume oldProjectVolume = projectService.get(ProjectVolume.class, projectVolume.getId());
            /*oldProjectVolume.setProjectID(projectVolume.getProjectID());
            oldProjectVolume.setProjectName(projectVolume.getProjectName());
            oldProjectVolume.setStartTime(projectVolume.getStartTime());
            oldProjectVolume.setEndTime(projectVolume.getEndTime());
            oldProjectVolume.setLaborID(projectVolume.getLaborID());
            oldProjectVolume.setVolume(projectVolume.getVolume());
            oldProjectVolume.setApplyMech(projectVolume.getApplyMech());
            oldProjectVolume.setApplyLabour(projectVolume.getApplyLabour());
            oldProjectVolume.setApplyMat(projectVolume.getApplyMat());
            oldProjectVolume.setApplyDebit(projectVolume.getApplyDebit());
            oldProjectVolume.setApplyPack(projectVolume.getApplyPack());
            oldProjectVolume.setApplySub(projectVolume.getApplySub());
            oldProjectVolume.setBusinessMech(projectVolume.getApplyMech());
            oldProjectVolume.setBusinessLabour(projectVolume.getApplyLabour());
            oldProjectVolume.setBusinessMat(projectVolume.getApplyMat());
            oldProjectVolume.setBusinessDebit(projectVolume.getApplyDebit());
            oldProjectVolume.setBusinessPack(projectVolume.getApplyPack());
            oldProjectVolume.setBusinessSub(projectVolume.getApplySub());
            oldProjectVolume.setFinalMech(projectVolume.getApplyMech());
            oldProjectVolume.setFinalLabour(projectVolume.getApplyLabour());
            oldProjectVolume.setFinalMat(projectVolume.getApplyMat());
            oldProjectVolume.setFinalDebit(projectVolume.getApplyDebit());
            oldProjectVolume.setFinalPack(projectVolume.getApplyPack());
            oldProjectVolume.setFinalSub(projectVolume.getApplySub());*/
            
            SpringUtil.copyPropertiesIgnoreNull(projectVolume, oldProjectVolume);
            
            oldProjectVolume.setUpdateDateTime(new Date());
            oldProjectVolume.setCreateTime(new Date());
            
            projectService.update(oldProjectVolume);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", projectVolume.getId());
        return new Result(true, map);
        
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/savebusiness")
    @ResponseBody
    public Result saveBusiness(ProjectVolume projectVolume, HttpServletRequest request){
        
        ProjectVolume oldProjectVolume = projectService.get(ProjectVolume.class, projectVolume.getId());
        oldProjectVolume.setBusinessMech(projectVolume.getBusinessMech());
        oldProjectVolume.setBusinessLabour(projectVolume.getBusinessLabour());
        oldProjectVolume.setBusinessMat(projectVolume.getBusinessMat());
        oldProjectVolume.setBusinessDebit(projectVolume.getBusinessDebit());
        
        oldProjectVolume.setBusinessDebitjx(projectVolume.getBusinessDebitjx());
        oldProjectVolume.setBusinessDebitcl(projectVolume.getBusinessDebitcl());
        
        oldProjectVolume.setBusinessPack(projectVolume.getBusinessPack());
        oldProjectVolume.setBusinessSub(projectVolume.getBusinessSub());
        oldProjectVolume.setUpdateDateTime(new Date());
        
        try{
            projectService.update(oldProjectVolume); 
        }catch (Exception e) {
            return new Result(false);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", projectVolume.getId());
        return new Result(true, map);
        
    }
    
    @RequestMapping(method = RequestMethod.POST, value="/savefinal")
    @ResponseBody
    public Result saveFinal(ProjectVolume projectVolume, HttpServletRequest request){
        
        ProjectVolume oldProjectVolume = projectService.get(ProjectVolume.class, projectVolume.getId());
        oldProjectVolume.setFinalMech(projectVolume.getFinalMech());
        oldProjectVolume.setFinalLabour(projectVolume.getFinalLabour());
        oldProjectVolume.setFinalMat(projectVolume.getFinalMat());
        oldProjectVolume.setFinalDebit(projectVolume.getFinalDebit());
        
        oldProjectVolume.setFinalDebitjx(projectVolume.getFinalDebitjx());
        oldProjectVolume.setFinalDebitcl(projectVolume.getFinalDebitcl());
        
        oldProjectVolume.setFinalPack(projectVolume.getFinalPack());
        oldProjectVolume.setFinalSub(projectVolume.getFinalSub());
        oldProjectVolume.setUpdateDateTime(new Date());
        
        try{
            projectService.update(oldProjectVolume); 
        }catch (Exception e) {
            return new Result(false);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", projectVolume.getId());
        return new Result(true, map);
        
    }
    
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {
        return projectService.startVolumeFlow(projectService.get(ProjectVolume.class, id),"projectVolume");
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private ProjectVolume getProject(String id) {
        return projectService.get(ProjectVolume.class, id);
    }
    
    @RequestMapping("/lookfkd")
    public String lookfkd(HttpServletRequest request){
    	String id = request.getParameter("id");//工程量id
    	ProjectVolume gcl = projectService.get(ProjectVolume.class, id);
    	if(gcl!=null){
    		if(gcl.getLaborID()!=null){
    			Labor ht = projectService.get(Labor.class, gcl.getLaborID());//劳务合同
    			if(ht!=null){
    				request.setAttribute("bzid", ht.getConstructionTeamID());//拿到班组id
    			}else{
    				request.setAttribute("bzid", "1");
    			}
    		}else{
    			request.setAttribute("bzid", "1");
    		}
    	}else{
    		request.setAttribute("bzid", "1");
    	}
    	request.setAttribute("gclid", id);
    	return "projectmanage/volume/lookfkd";
    }
    //确认执行罚款单操作   改罚款单执行状态 并且增加工程量罚款金额
    @RequestMapping("/doqrfkd")
    @ResponseBody
    public Result doqrfkd(HttpServletRequest request){
    	Result r = new Result();
    	String fkid = request.getParameter("fkid");
    	String gclid = request.getParameter("gclid");
    	CheckFkd fk = projectService.get(CheckFkd.class, fkid);
    	//如果确认类型==1 直接返回
    	if(fk.getIsqr()!=null&&"1".equals(fk.getIsqr())){
    		return r;
    	}
    	
    	fk.setIsqr("1");
    	fk.setGclid(gclid);
    	projectService.update(fk);
    	ProjectVolume gcl = projectService.get(ProjectVolume.class, gclid);
    	Arith ari = new Arith();
    	if(fk.getFkje()==null||Double.valueOf(fk.getFkje())==0){
    		return r;
    	}
    	Double fkje = Double.valueOf(fk.getFkje());
    	//机械上报扣在机械扣款 其他扣在人工扣款
    	String sblx = gcl.getSblx();
    	if(sblx!=null&&"30".equals(sblx)){
    		//拿到经营科扣款和财务扣款  扣款相加
        	Double jyk = 0.0;
        	Double cw = 0.0;
        	if(gcl.getBusinessDebitjx()!=null){
        		jyk = Double.valueOf(gcl.getBusinessDebitjx());
        	}
        	if(gcl.getFinalDebitjx()!=null){
        		cw =  Double.valueOf(gcl.getFinalDebitjx());
        	}
        	jyk = ari.add(jyk, fkje);
        	gcl.setBusinessDebitjx(jyk.toString());
        	cw = ari.add(cw, fkje);
        	gcl.setFinalDebitjx(cw.toString());
        	//重算经营科小计和财务小计   小计-扣款
        	Double jyks = 0.0;
        	Double cws = 0.0;
        	if(gcl.getBusinessSub()!=null){
        		jyks = Double.valueOf(gcl.getBusinessSub());
        	}
        	if(gcl.getFinalSub()!=null){
        		cws =  Double.valueOf(gcl.getFinalSub());
        	}
        	jyks = ari.sub(jyks, fkje);
        	gcl.setBusinessSub(jyks.toString());
        	cws = ari.sub(cws, fkje);
        	gcl.setFinalSub(cws.toString());
    	}else{
    		//拿到经营科扣款和财务扣款  扣款相加
        	Double jyk = 0.0;
        	Double cw = 0.0;
        	if(gcl.getBusinessDebit()!=null){
        		jyk = Double.valueOf(gcl.getBusinessDebit());
        	}
        	if(gcl.getFinalDebit()!=null){
        		cw =  Double.valueOf(gcl.getFinalDebit());
        	}
        	jyk = ari.add(jyk, fkje);
        	gcl.setBusinessDebit(jyk.toString());
        	cw = ari.add(cw, fkje);
        	gcl.setFinalDebit(cw.toString());
        	//重算经营科小计和财务小计   小计-扣款
        	Double jyks = 0.0;
        	Double cws = 0.0;
        	if(gcl.getBusinessSub()!=null){
        		jyks = Double.valueOf(gcl.getBusinessSub());
        	}
        	if(gcl.getFinalSub()!=null){
        		cws =  Double.valueOf(gcl.getFinalSub());
        	}
        	jyks = ari.sub(jyks, fkje);
        	gcl.setBusinessSub(jyks.toString());
        	cws = ari.sub(cws, fkje);
        	gcl.setFinalSub(cws.toString());
    	}
    	
    	
    	projectService.update(gcl);
    	return r;
    }
    @RequestMapping("/doqxfkd")
    @ResponseBody
    public Result doqxfkd(HttpServletRequest request){
    	Result r = new Result();
    	String fkid = request.getParameter("fkid");
    	String gclid = request.getParameter("gclid");
    	CheckFkd fk = projectService.get(CheckFkd.class, fkid);
    	//如果确认类型==1 直接返回
    	if("0".equals(fk.getIsqr())){
    		return r;
    	}
    	
    	fk.setIsqr("0");
    	fk.setGclid("");
    	projectService.update(fk);
    	ProjectVolume gcl = projectService.get(ProjectVolume.class, gclid);
    	Arith ari = new Arith();
    	if(fk.getFkje()==null||Double.valueOf(fk.getFkje())==0){
    		return r;
    	}
    	Double fkje = Double.valueOf(fk.getFkje());
    	//机械上报扣在机械扣款 其他扣在人工扣款
    	String sblx = gcl.getSblx();
    	if(sblx!=null&&"30".equals(sblx)){
    		//拿到经营科扣款和财务扣款  扣款相加
        	Double jyk = 0.0;
        	Double cw = 0.0;
        	if(gcl.getBusinessDebitjx()!=null){
        		jyk = Double.valueOf(gcl.getBusinessDebitjx());
        	}
        	if(gcl.getFinalDebitjx()!=null){
        		cw =  Double.valueOf(gcl.getFinalDebitjx());
        	}
        	jyk = ari.sub(jyk, fkje);
        	gcl.setBusinessDebitjx(jyk.toString());
        	cw = ari.sub(cw, fkje);
        	gcl.setFinalDebitjx(cw.toString());
        	//重算经营科小计和财务小计   小计-扣款
        	Double jyks = 0.0;
        	Double cws = 0.0;
        	if(gcl.getBusinessSub()!=null){
        		jyks = Double.valueOf(gcl.getBusinessSub());
        	}
        	if(gcl.getFinalSub()!=null){
        		cws =  Double.valueOf(gcl.getFinalSub());
        	}
        	jyks = ari.add(jyks, fkje);
        	gcl.setBusinessSub(jyks.toString());
        	cws = ari.add(cws, fkje);
        	gcl.setFinalSub(cws.toString());
    	}else{
    		//拿到经营科扣款和财务扣款  扣款相加
        	Double jyk = 0.0;
        	Double cw = 0.0;
        	if(gcl.getBusinessDebit()!=null){
        		jyk = Double.valueOf(gcl.getBusinessDebit());
        	}
        	if(gcl.getFinalDebit()!=null){
        		cw =  Double.valueOf(gcl.getFinalDebit());
        	}
        	jyk = ari.sub(jyk, fkje);
        	gcl.setBusinessDebit(jyk.toString());
        	cw = ari.sub(cw, fkje);
        	gcl.setFinalDebit(cw.toString());
        	//重算经营科小计和财务小计   小计-扣款
        	Double jyks = 0.0;
        	Double cws = 0.0;
        	if(gcl.getBusinessSub()!=null){
        		jyks = Double.valueOf(gcl.getBusinessSub());
        	}
        	if(gcl.getFinalSub()!=null){
        		cws =  Double.valueOf(gcl.getFinalSub());
        	}
        	jyks = ari.add(jyks, fkje);
        	gcl.setBusinessSub(jyks.toString());
        	cws = ari.add(cws, fkje);
        	gcl.setFinalSub(cws.toString());
    	}
    	
    	
    	
    	projectService.update(gcl);
    	return r;
    }
}
