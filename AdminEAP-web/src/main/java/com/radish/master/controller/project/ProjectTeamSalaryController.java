/**
 * 
 */
package com.radish.master.controller.project;

import java.math.BigDecimal;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.annotation.VerifyCSRFToken;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.SalaryVolume;
import com.radish.master.pojo.SalaryChooseVO;
import com.radish.master.service.CommonService;
import com.radish.master.service.project.TeamSalaryService;

@Controller
@RequestMapping("/project/team/salary")
public class ProjectTeamSalaryController {

    @Resource
    private CommonService commonService;

    @Resource
    private TeamSalaryService teamSalaryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "projectmanage/team/salary/list";
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("id", id);
        /* String subInfo = "";
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<SalarySubInfo> list = teamSalaryService.getSubInfoList(id);
        for (SalarySubInfo sub : list) {
            subInfo = subInfo + "分项名称：" + sub.getSubName() + "，";
            subInfo = subInfo + "开始时间：" + myFormat.format(sub.getStartTime()) + "，";
            subInfo = subInfo + "结束时间：" + myFormat.format(sub.getEndTime());
            subInfo = subInfo + "\\n";
        }
        request.setAttribute("subInfo", subInfo);*/
        return "projectmanage/team/salary/detail";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    public Salary getSalaryInfo(String id) {
        return teamSalaryService.get(Salary.class, id);
    }

    @RequestMapping(value = "/getteam")
    @ResponseBody
    public Result getLabor(String projectID) {
        return new Result(true, JSONArray.toJSONString(teamSalaryService.getTeamComboboxByProject(projectID)));
    }

    @RequestMapping(value = "/getvolume")
    @ResponseBody
    public Result getVolume(String projectID, String laborID) {
        return new Result(true, JSONArray.toJSONString(teamSalaryService.getVolumeComboboxByProjectAndLabor(projectID, laborID)));
    }

    @RequestMapping(value = "/getuser")
    @ResponseBody
    public Result getUser(String projectID, String laborID) {
        return new Result(true, JSONArray.toJSONString(teamSalaryService.getUserComboboxByProjectAndLabor(projectID, laborID)));
    }

    @RequestMapping(value = "/getinfo")
    @ResponseBody
    public User getUser(String userID) {
        return teamSalaryService.get(User.class, userID);
    }

    @RefreshCSRFToken
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(HttpServletRequest request) {
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "projectmanage/team/salary/step1";
    }

    @RefreshCSRFToken
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "projectmanage/team/salary/step1";
    }

    @RequestMapping(value = "/savesalary")
    @ResponseBody
    public Result saveSalary(Salary salary, HttpServletRequest request) {
        String total = "";
        if (StrUtil.isEmpty(salary.getId())) {
            salary.setCreateDateTime(new Date());
            salary.setUpdateDateTime(new Date());
            salary.setStatus("10");
            salary.setType("10");
            salary.setTotal("0");
            salary.setApplySum("0");
            try {
                teamSalaryService.save(salary);
            } catch (Exception e) {
                return new Result(false);
            }
            total = salary.getTotal();
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

    @RequestMapping(value = "/choose", method = RequestMethod.POST)
    @ResponseBody
    public Result choose(String volumeList) {

        List<SalaryChooseVO> scs = JSON.parseArray(volumeList, SalaryChooseVO.class);
        Salary salary = null;
        BigDecimal sum = new BigDecimal("0");
        for (SalaryChooseVO vo : scs) {
            if (salary == null) {
                salary = teamSalaryService.get(Salary.class, vo.getSalaryID());
                sum = new BigDecimal(salary.getTotal());
            }
            SalaryVolume sv = new SalaryVolume();
            sv.setSalaryID(vo.getSalaryID());
            sv.setVolumeID(vo.getVolumeID());
            teamSalaryService.save(sv);

            sum = sum.add(new BigDecimal(vo.getLabour()));
        }
        if (salary != null) {
            salary.setTotal(sum.toPlainString());
            teamSalaryService.update(salary);
        }
        return new Result(true, sum.toPlainString());
    }

    @RequestMapping(value = "/unchoose", method = RequestMethod.POST)
    @ResponseBody
    public Result unChoose(String volumeList) {

        List<SalaryChooseVO> scs = JSON.parseArray(volumeList, SalaryChooseVO.class);
        Salary salary = null;
        BigDecimal sum = new BigDecimal("0");
        for (SalaryChooseVO vo : scs) {
            if (salary == null) {
                salary = teamSalaryService.get(Salary.class, vo.getSalaryID());
                sum = new BigDecimal(salary.getTotal());
            }
            SalaryVolume sv = teamSalaryService.getBySalaryAndVolume(vo.getSalaryID(), vo.getVolumeID());
            teamSalaryService.delete(sv);

            sum = sum.subtract(new BigDecimal(vo.getLabour()));
        }
        if (salary != null) {
            salary.setTotal(sum.toPlainString());
            teamSalaryService.update(salary);
        }
        return new Result(true, sum.toPlainString());
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
        request.setAttribute("total", salary.getTotal());
        request.setAttribute("userOptions", JSONArray.toJSONString(teamSalaryService.getUserComboboxByTeam(salary.getTeamID())));
        return "projectmanage/team/salary/step2";
    }

    @VerifyCSRFToken
    @RequestMapping(value = "/savedetail")
    @ResponseBody
    public Result saveDetail(SalaryDetail detail, String total, HttpServletRequest request) {

        BigDecimal t = new BigDecimal(total);
        String hqlSalary = "from SalaryDetail where salaryID=:salaryID";
        Map<String, Object> paramsSalary = new HashMap<>();
        paramsSalary.put("salaryID", detail.getSalaryID());
        List<SalaryDetail> detailList = teamSalaryService.find(hqlSalary, paramsSalary);
        BigDecimal a = new BigDecimal("0");
        for (SalaryDetail sd : detailList) {
            BigDecimal actual = new BigDecimal(sd.getActual());

            a = a.add(actual);
        }
        BigDecimal d = new BigDecimal(detail.getActual());
        if (d.add(a).compareTo(t) == 1) {
            return new Result(false, "实发工资超出人工费，请核查");
        }

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

        return teamSalaryService.startTeamSalaryFlow(salary, "teamSalary");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/check")
    @ResponseBody
    public Result validateChannel(String id) {
        Salary salary = teamSalaryService.get(Salary.class, id);

        String hql = "from UserTeam where teamID=:teamID";
        Map<String, Object> params = new HashMap<>();
        params.put("teamID", salary.getTeamID());
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
