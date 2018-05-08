/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.project;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.project.ProjectTeam;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.service.CommonService;
import com.radish.master.service.project.TeamSalaryService;
import com.radish.master.system.SpringUtil;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年5月7日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/project/manager/salary")
public class ProjectManagerSalaryController {

    @Resource
    private CommonService commonService;

    @Resource
    private TeamSalaryService teamSalaryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "projectmanage/team/salary/manager/list";
    }
    
    @RequestMapping(value = "/detailouter/{id}", method = RequestMethod.GET)
    public String detailOuter(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("id", id);
        return "projectmanage/team/salary/manager/detail";
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("id", id);
        return "projectmanage/team/salary/manager/detail";
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    public Salary getSalaryInfo(String id) {
        return teamSalaryService.get(Salary.class, id);
    }

    @RequestMapping(value = "/getinfo")
    @ResponseBody
    public User getUser(String userID) {
        return teamSalaryService.get(User.class, userID);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "projectmanage/team/salary/manager/step1";
    }
    
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "projectmanage/team/salary/manager/step1";
    }
    
    @RequestMapping(value = "/savesalary")
    @ResponseBody
    public Result saveSalary(Salary salary, HttpServletRequest request) {
        SimpleDateFormat myFormat=new SimpleDateFormat("yyyy-MM-dd");
        if(salary.getStartTime().compareTo(salary.getEndTime()) != -1){
            return new Result(false, "开始时间必须小于结束时间");
        }
        
        String startTime = myFormat.format(salary.getStartTime()) + " 00:00:00";
        String endTime = myFormat.format(salary.getEndTime()) + " 23:59:59";
        
        
        List<Salary> list = teamSalaryService.checkTimePeriod(salary.getProjectID(), startTime, endTime, salary.getId());
        if(!list.isEmpty()){
            return new Result(false, "工资时间段不可重叠");
        }
        if (StrUtil.isEmpty(salary.getId())) {
            salary.setCreateDateTime(new Date());
            salary.setUpdateDateTime(new Date());
            salary.setStatus("10");
            salary.setType("20");
            salary.setApplySum("0");
            try {
                teamSalaryService.save(salary);
            } catch (Exception e) {
                return new Result(false);
            }
        } else {
            Salary oldSalary = teamSalaryService.get(Salary.class, salary.getId());
            SpringUtil.copyPropertiesIgnoreNull(salary, oldSalary);
            oldSalary.setUpdateDateTime(new Date());
            teamSalaryService.save(oldSalary);
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", salary.getId());
        map.put("projectID", salary.getProjectID());
        return new Result(true, map);
    }
    
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id) {

        String hql = "from SalaryDetail where salaryID=:salaryID";
        Map<String, Object> params = new HashMap<>();
        params.put("salaryID", id);
        List<SalaryDetail> detailList = teamSalaryService.find(hql, params);
        if (detailList.isEmpty()) {
            try {
                Salary salary = teamSalaryService.get(Salary.class, id);
                teamSalaryService.delete(salary);
            } catch (Exception e) {
                return new Result(false);
            }
            return new Result(true);
        } else {
            return new Result(false);
        }
    }

    @RefreshCSRFToken
    @RequestMapping(value = "/step2", method = RequestMethod.POST)
    public String step2(Salary salary, HttpServletRequest request) {
        request.setAttribute("id", salary.getId());
        request.setAttribute("userOptions", JSONArray.toJSONString(
                teamSalaryService.getManageUserComboboxByProject(salary.getProjectID())));
        return "projectmanage/team/salary/manager/step2";
    }

    @VerifyCSRFToken
    @RequestMapping(value = "/savedetail")
    @ResponseBody
    public Result saveDetail(SalaryDetail detail, String total, HttpServletRequest request) {

        String hqlSalary = "from SalaryDetail where salaryID=:salaryID";
        Map<String, Object> paramsSalary = new HashMap<>();
        paramsSalary.put("salaryID", detail.getSalaryID());
        List<SalaryDetail> detailList = teamSalaryService.find(hqlSalary, paramsSalary);
        BigDecimal a = new BigDecimal("0");
        for(SalaryDetail sd : detailList){
            BigDecimal actual = new BigDecimal(sd.getActual());
            
            a = a.add(actual);
        }
        BigDecimal d = new BigDecimal(detail.getActual());

        detail.setCreateDateTime(new Date());
        detail.setUpdateDateTime(new Date());
        try {
            teamSalaryService.save(detail);
        } catch (Exception e) {
            return new Result(false, "请检查人员是否重复");
        }

        Salary salary = teamSalaryService.get(Salary.class, detail.getSalaryID());
        salary.setApplySum(d.add(a).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        teamSalaryService.save(salary);
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", detail.getId());
        return new Result(true, map);
    }

    @RequestMapping(value = "/deletedetail/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteDetail(@PathVariable("id") String id) {

        SalaryDetail detail = teamSalaryService.get(SalaryDetail.class, id);
        try {
            teamSalaryService.delete(detail);
        } catch (Exception e) {
            return new Result(false);
        }
        return new Result(true);
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {

        Salary salary = teamSalaryService.get(Salary.class, id);

        return teamSalaryService.startManagerSalaryFlow(salary, "managerSalary");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/check")
    @ResponseBody
    public Result validateChannel(String id) {
        Salary salary = teamSalaryService.get(Salary.class, id);

        String hqlPT = "from ProjectTeam where projectID=:projectID AND status='30'";
        Map<String, Object> paramsPT = new HashMap<>();
        paramsPT.put("projectID", salary.getProjectID());
        ProjectTeam projectTeam = teamSalaryService.get(hqlPT, paramsPT);
        
        String hql = "from UserTeam where teamID=:teamID";
        Map<String, Object> params = new HashMap<>();
        params.put("teamID", projectTeam.getId());
        List<SalaryDetail> memberList = teamSalaryService.find(hql, params);

        String hqlSalary = "from SalaryDetail where salaryID=:salaryID";
        Map<String, Object> paramsSalary = new HashMap<>();
        paramsSalary.put("salaryID", id);
        List<SalaryDetail> detailList = teamSalaryService.find(hqlSalary, paramsSalary);

        if (memberList.size() != detailList.size()) {
            return new Result(false);
        } else {
            return new Result(true);
        }
    }
}
