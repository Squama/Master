/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.project;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
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
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.project.ProjectTeam;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.SocialSecurity;
import com.radish.master.pojo.RowEdit;
import com.radish.master.service.CommonService;
import com.radish.master.service.project.TeamSalaryService;
import com.radish.master.system.SpringUtil;
import com.radish.master.system.TimeUtil;

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
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (salary.getStartTime().compareTo(salary.getEndTime()) != -1) {
            return new Result(false, "开始时间必须小于结束时间");
        }

        String startTime = myFormat.format(salary.getStartTime()) + " 00:00:00";
        String endTime = myFormat.format(salary.getEndTime()) + " 23:59:59";

        List<Salary> list = teamSalaryService.checkTimePeriod(salary.getProjectID(), startTime, endTime, salary.getId());
        if (!list.isEmpty()) {
            return new Result(false, "工资时间段不可重叠");
        }
        
        Salary limitSalary = teamSalaryService.getBiggestSalary(salary.getProjectID(), "20");
        if(null!=limitSalary&&limitSalary.getStartTime().getTime() > salary.getStartTime().getTime()){
            return new Result(false, "工资起始时间不可小于"+myFormat.format(limitSalary.getStartTime()));
        }
        
        if (StrUtil.isEmpty(salary.getId())) {
            salary.setCreateDateTime(new Date());
            salary.setUpdateDateTime(new Date());
            salary.setStatus("10");
            salary.setType("20");
            salary.setApplySum("0");
            salary.setCostSum("0");
            salary.setTotal("无");
            try {
                teamSalaryService.save(salary);
            } catch (Exception e) {
                return new Result(false);
            }
            String year = startTime.substring(0, 4);
            SocialSecurity socialSecurity = teamSalaryService.getSocialSecurity(year);
            
            // 直接录入全部明细
            List<User> managerList = teamSalaryService.getManageMemberByProject(salary.getProjectID());
            List<SalaryDetail> detailList = new ArrayList<SalaryDetail>();
            for (User user : managerList) {
                SalaryDetail detail = new SalaryDetail();
                detail.setCreateDateTime(new Date());
                detail.setUpdateDateTime(new Date());

                detail.setSalaryID(salary.getId());
                detail.setUserID(user.getId());
                detail.setName(user.getName());
                detail.setMobile(user.getMobile());
                detail.setIdentificationNumber(user.getIdentificationNumber());
                detail.setWorkType(user.getWorkType());
                if(StrUtil.isEmpty(user.getBasicSalary())){
                    detail.setBasicSalary("0");
                }else{
                    detail.setBasicSalary(user.getBasicSalary());
                }
                if(StrUtil.isEmpty(user.getDeduction())){
                    detail.setDeduction("0");
                }else{
                    detail.setDeduction(user.getDeduction());
                }
                detail.setSsYear(year);
                
                detail.setAttendance("0");
                detail.setLoan("0");
                
                if("0".equals(detail.getBasicSalary()) || socialSecurity == null){
                    detail.setYanglao("0");
                    detail.setYanglaoCorp("0");
                    detail.setMedical("0");
                    detail.setMedicalCorp("0");
                    detail.setShiye("0");
                    detail.setShiyeCorp("0");
                    detail.setGongshangCorp("0");
                    detail.setShengyuCorp("0");
                    detail.setPrf("0");
                    detail.setPrfCorp("0");
                    detail.setSocial("0");
                    detail.setTax("0");
                    detail.setPayable("0");
                    detail.setActual("0");
                }else{
                    teamSalaryService.handleSocialSalaryDetail(socialSecurity, detail, salary.getStartTime());
                    
                }

                detail.setRemark("");

                detailList.add(detail);

            }
            for(SalaryDetail detail :detailList){
            	teamSalaryService.saveOrUpdate(detail);
            }
            //teamSalaryService.batchSave(detailList);
            	String hqlSalary = "from SalaryDetail where salaryID=:salaryID";
                Map<String, Object> paramsSalary = new HashMap<>();
                paramsSalary.put("salaryID",salary.getId());
                List<SalaryDetail> detailLists = teamSalaryService.find(hqlSalary, paramsSalary);
                BigDecimal a = new BigDecimal("0");
                for (SalaryDetail sd : detailLists) {
                    BigDecimal actual = new BigDecimal(sd.getActual());

                    a = a.add(actual);
                }
                salary.setApplySum(a.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
                teamSalaryService.update(salary);
        } else {
            Salary oldSalary = teamSalaryService.get(Salary.class, salary.getId());
            if(!startTime.substring(0, 4).equals(TimeUtil.getFormattedDate(oldSalary.getStartTime()).substring(0, 4))){
                return new Result(false, "不能跨社保年份");
            }
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
        return "projectmanage/team/salary/manager/step2";
    }

    @RequestMapping(value = "/rowedit", method = RequestMethod.POST)
    @ResponseBody
    public String singleEstimate(String action, HttpServletRequest request) throws Exception {
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
        if(!"remark".equals(field)&&!"workType".equals(field)){
        	if (!teamSalaryService.isNumber(value)) {
                list.add(new SalaryDetail());
                re.setData(list);
                return JSONArray.toJSONString(re);
            }
        }
        SalaryDetail detail = teamSalaryService.get(SalaryDetail.class, id);

        Method set = detail.getClass().getMethod("set" + teamSalaryService.captureName(field), String.class);
        set.invoke(detail, value);
        
        if("payable".equals(field)){
        	Salary salary = teamSalaryService.get(Salary.class, detail.getSalaryID());
        	if("20".equals(salary.getType()) || "40".equals(salary.getType()) || "50".equals(salary.getType())){
        		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        		String startTime = myFormat.format(salary.getStartTime()) + " 00:00:00";
        		String year = startTime.substring(0, 4);
        		SocialSecurity socialSecurity = teamSalaryService.getSocialSecurity(year);
        		teamSalaryService.handleSocialSalaryDetailPayable(socialSecurity, detail, salary.getStartTime());
        		teamSalaryService.save(detail);
        	}
        }
        
        if(!"actual".equals(field)){
        	BigDecimal loan = new BigDecimal(detail.getLoan());
            BigDecimal actual = new BigDecimal(detail.getActual());
            detail.setActual(actual.subtract(loan).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
            teamSalaryService.save(detail);
        }
        
        if("actual".equals(field)){
            String hqlSalary = "from SalaryDetail where salaryID=:salaryID";
            Map<String, Object> paramsSalary = new HashMap<>();
            paramsSalary.put("salaryID", detail.getSalaryID());
            List<SalaryDetail> detailList = teamSalaryService.find(hqlSalary, paramsSalary);
            BigDecimal a = new BigDecimal("0");
            BigDecimal b = new BigDecimal("0");
            for (SalaryDetail sd : detailList) {
                BigDecimal actual = new BigDecimal(sd.getActual());
                BigDecimal cost = new BigDecimal(sd.getPayable());
                
                a = a.add(actual);
                b = b.add(cost);
            }
            Salary salary = teamSalaryService.get(Salary.class, detail.getSalaryID());
            salary.setApplySum(a.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
            salary.setCostSum(b.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
            teamSalaryService.save(salary);
        }
        
        list.add(detail);
        re.setData(list);
        return JSONArray.toJSONString(re);
    }

    /*@RequestMapping(value = "/savedetail")
    @ResponseBody
    public Result saveDetail(SalaryDetail detail, String total, HttpServletRequest request) {

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
    }*/

    @RequestMapping(value = "/deletedetail/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteDetail(@PathVariable("id") String id) {

        SalaryDetail detail = teamSalaryService.get(SalaryDetail.class, id);
        try {
            teamSalaryService.delete(detail);
        } catch (Exception e) {
            return new Result(false);
        }
        //重新计算总值
        Salary salary = teamSalaryService.get(Salary.class, detail.getSalaryID());
        String hqlSalary = "from SalaryDetail where salaryID=:salaryID";
        Map<String, Object> paramsSalary = new HashMap<>();
        paramsSalary.put("salaryID",detail.getSalaryID());
        List<SalaryDetail> detailLists = teamSalaryService.find(hqlSalary, paramsSalary);
        BigDecimal a = new BigDecimal("0");
        for (SalaryDetail sd : detailLists) {
            BigDecimal actual = new BigDecimal(sd.getActual());

            a = a.add(actual);
        }
        salary.setApplySum(a.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        teamSalaryService.update(salary);
        
        
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
