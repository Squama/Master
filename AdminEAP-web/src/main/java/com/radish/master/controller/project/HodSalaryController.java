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

import org.hibernate.type.Type;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.pojo.MatInAndOut;
import com.radish.master.pojo.RowEdit;
import com.radish.master.service.CommonService;
import com.radish.master.service.project.TeamSalaryService;
import com.radish.master.system.SpringUtil;

/**
* 类说明 门卫 机修人员工资
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
@RequestMapping("/project/hod/salary")
public class HodSalaryController {
    @Resource
    private CommonService commonService;

    @Resource
    private TeamSalaryService teamSalaryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
    	request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "projectmanage/team/salary/hod/list";
    }

    @RequestMapping(value = "/detailouter/{id}", method = RequestMethod.GET)
    public String detailOuter(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("id", id);
        return "projectmanage/team/salary/hod/detail";
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("id", id);
        return "projectmanage/team/salary/hod/detail";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(HttpServletRequest request) {
    	request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        return "projectmanage/team/salary/hod/step1";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id, HttpServletRequest request) {
    	request.setAttribute("projectOptions", JSONArray.toJSONString(commonService.getProjectCombobox()));
        request.setAttribute("id", id);
        return "projectmanage/team/salary/hod/step1";
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

        List<Salary> list = teamSalaryService.checkHodTimePeriod(salary.getProjectID(),startTime, endTime, salary.getId());
        if (!list.isEmpty()) {
            return new Result(false, "工资时间段不可重叠");
        }
        if (StrUtil.isEmpty(salary.getId())) {
            salary.setCreateDateTime(new Date());
            salary.setUpdateDateTime(new Date());
            salary.setStatus("10");
            salary.setType("50");
            salary.setApplySum("0");
            salary.setTotal("无");
            try {
                teamSalaryService.save(salary);
            } catch (Exception e) {
                return new Result(false);
            }
            // 直接录入全部明细
            StringBuilder buffer = new StringBuilder();

            buffer.append("SELECT u.* FROM tbl_user u  where exists(select st.* from v_mwjx st where st.id = u.id) ");
            List<User> managerList = teamSalaryService.findBySql(buffer.toString(), User.class);
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
                if(user.getBasicSalary()==null){
                	detail.setBasicSalary("0");
                }else{
                	detail.setBasicSalary(user.getBasicSalary());
                }
                

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
            SpringUtil.copyPropertiesIgnoreNull(salary, oldSalary);
            oldSalary.setUpdateDateTime(new Date());
            teamSalaryService.save(oldSalary);
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", salary.getId());
        map.put("projectID", salary.getProjectID());
        return new Result(true, map);
    }

    @RequestMapping(value = "/step2", method = RequestMethod.POST)
    public String step2(Salary salary, HttpServletRequest request) {
        request.setAttribute("id", salary.getId());
        return "projectmanage/team/salary/hod/step2";
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

        if (!teamSalaryService.isNumber(value)) {
            list.add(new SalaryDetail());
            re.setData(list);
            return JSONArray.toJSONString(re);
        }

        SalaryDetail detail = teamSalaryService.get(SalaryDetail.class, id);

        Method set = detail.getClass().getMethod("set" + teamSalaryService.captureName(field), String.class);
        set.invoke(detail, value);

        teamSalaryService.save(detail);
        
        
        if("actual".equals(field)){
            String hqlSalary = "from SalaryDetail where salaryID=:salaryID";
            Map<String, Object> paramsSalary = new HashMap<>();
            paramsSalary.put("salaryID", detail.getSalaryID());
            List<SalaryDetail> detailList = teamSalaryService.find(hqlSalary, paramsSalary);
            BigDecimal a = new BigDecimal("0");
            for (SalaryDetail sd : detailList) {
                BigDecimal actual = new BigDecimal(sd.getActual());

                a = a.add(actual);
            }
            Salary salary = teamSalaryService.get(Salary.class, detail.getSalaryID());
            salary.setApplySum(a.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
            teamSalaryService.save(salary);
        }
        
        list.add(detail);
        re.setData(list);
        return JSONArray.toJSONString(re);
    }


    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {

        Salary salary = teamSalaryService.get(Salary.class, id);

        return teamSalaryService.startHodSalaryFlow(salary, "hodSalary");
    }
}
