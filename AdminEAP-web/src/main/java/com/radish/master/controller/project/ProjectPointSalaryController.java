/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.project;

import java.util.ArrayList;
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
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.Worker;
import com.radish.master.service.CommonService;
import com.radish.master.service.project.TeamSalaryService;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年11月19日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/project/point/salary")
public class ProjectPointSalaryController {

    @Resource
    private CommonService commonService;

    @Resource
    private TeamSalaryService teamSalaryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "projectmanage/team/salary/point/list";
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("id", id);
        return "projectmanage/team/salary/point/detail";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "projectmanage/team/salary/point/step1";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "projectmanage/team/salary/point/step1";
    }
    
    @RequestMapping(value = "/getteam")
    @ResponseBody
    public Result getLabor(String projectID) {
        return new Result(true, JSONArray.toJSONString(teamSalaryService.getPointTeamComboboxByProject(projectID)));
    }

    @RequestMapping(value = "/savesalary")
    @ResponseBody
    public Result saveSalary(Salary salary, HttpServletRequest request) {
        String total = "";
        if (StrUtil.isEmpty(salary.getId())) {
            salary.setCreateDateTime(new Date());
            salary.setUpdateDateTime(new Date());
            salary.setStatus("10");
            salary.setType("30");
            salary.setTotal("0");
            salary.setApplySum("0");
            try {
                teamSalaryService.save(salary);
            } catch (Exception e) {
                return new Result(false);
            }
            total = salary.getTotal();
            
            // 直接录入全部明细
            List<Worker> managerList = teamSalaryService.getPointMemberByProject(salary.getProjectID());
            List<SalaryDetail> detailList = new ArrayList<SalaryDetail>();
            for (Worker user : managerList) {
                SalaryDetail detail = new SalaryDetail();
                detail.setCreateDateTime(new Date());
                detail.setUpdateDateTime(new Date());

                detail.setSalaryID(salary.getId());
                detail.setUserID(user.getId());
                detail.setName(user.getName());
                detail.setMobile(user.getMobile());
                detail.setIdentificationNumber(user.getIdentificationNumber());
                detail.setWorkType(user.getWorkType());

                detail.setAttendance("0");
                detail.setPayable("0");
                detail.setLoan("0");
                detail.setMedical("0");
                detail.setSocial("0");
                detail.setTax("0");
                detail.setActual("0");
                detail.setRemark("");

                detailList.add(detail);

            }
            teamSalaryService.batchSave(detailList);
        } else {
            Salary oldSalary = teamSalaryService.get(Salary.class, salary.getId());
            oldSalary.setProjectID(salary.getProjectID());
            oldSalary.setTeamID(salary.getTeamID());
            oldSalary.setTeamName(salary.getTeamName());
            oldSalary.setUpdateDateTime(new Date());
            teamSalaryService.save(oldSalary);

            total = oldSalary.getTotal();
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", salary.getId());
        map.put("projectID", salary.getProjectID());
        map.put("teamID", salary.getTeamID());
        map.put("total", total);
        return new Result(true, map);
    }

    @RequestMapping(value = "/step2", method = RequestMethod.POST)
    public String step2(Salary salary, HttpServletRequest request) {
        request.setAttribute("id", salary.getId());
        request.setAttribute("total", salary.getTotal());
        return "projectmanage/team/salary/point/step2";
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {

        Salary salary = teamSalaryService.get(Salary.class, id);

        return teamSalaryService.startPointSalaryFlow(salary, "pointSalary");
    }
    
}
