/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.controller.project;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import com.cnpc.framework.base.entity.Dict;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.SocialSecurity;
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
* dongyan      2018年11月19日    Create this file
* </pre>
* 
*/
@Controller
@RequestMapping("/project/organ/salary")
public class OrganSalaryController {
    @Resource
    private CommonService commonService;

    @Resource
    private TeamSalaryService teamSalaryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        return "projectmanage/team/salary/organ/list";
    }

    @RequestMapping(value = "/detailouter/{id}", method = RequestMethod.GET)
    public String detailOuter(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("id", id);
        return "projectmanage/team/salary/organ/detail";
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("id", id);
        return "projectmanage/team/salary/organ/detail";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(HttpServletRequest request) {
        return "projectmanage/team/salary/organ/step1";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        return "projectmanage/team/salary/organ/step1";
    }

    @RequestMapping(value = "/savesalary")
    @ResponseBody
    public Result saveSalary(Salary salary, HttpServletRequest request) {
    	String deptent = request.getParameter("deptent");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (salary.getStartTime().compareTo(salary.getEndTime()) != -1) {
            return new Result(false, "开始时间必须小于结束时间");
        }

        String startTime = myFormat.format(salary.getStartTime()) + " 00:00:00";
        String endTime = myFormat.format(salary.getEndTime()) + " 23:59:59";

        
        if (StrUtil.isEmpty(salary.getId())) {
        	 List<Salary> list = teamSalaryService.checkOrganTimePeriod(startTime, endTime, salary.getId(),deptent);
             if (!list.isEmpty()) {
                 return new Result(false, "工资时间段不可重叠");
             }
             
             Salary limitSalary = teamSalaryService.getBiggestSalaryJg("40",deptent);
             if(limitSalary!=null&&limitSalary.getStartTime().getTime() > salary.getStartTime().getTime()){
                 return new Result(false, "工资起始时间不可小于"+myFormat.format(limitSalary.getStartTime()));
             }
        	String year = startTime.substring(0, 4);
            List<SocialSecurity> socialSecuritys = teamSalaryService.getSocialSecurity(year);
            if(socialSecuritys==null||socialSecuritys.size()==0){
            	return new Result(false, "请维护"+year+"年度社保费率");
            }
            Map<String,SocialSecurity> fls = new HashMap<String, SocialSecurity>();
            for(SocialSecurity fl :socialSecuritys){
            	fls.put(fl.getRegionCode(), fl);
            }
        	//---------------存入工资发放分部
        	Dict d = commonService.get(Dict.class, deptent);
        	salary.setDeptent(deptent);
        	salary.setDeptentname(d.getName());
        	//---------------
            salary.setCreateDateTime(new Date());
            salary.setUpdateDateTime(new Date());
            salary.setStatus("10");
            salary.setType("40");
            salary.setApplySum("0");
            salary.setCostSum("0");
            salary.setTotal("无");
            try {
                teamSalaryService.save(salary);
            } catch (Exception e) {
                return new Result(false);
            }
            // 直接录入全部明细
            List<User> managerList = teamSalaryService.getOrganMember(deptent);
            List<SalaryDetail> detailList = new ArrayList<SalaryDetail>();
            for (User user : managerList) {
            	SocialSecurity socialSecurity = fls.get(user.getRegionCode());
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
                
                if("0".equals(detail.getBasicSalary())  || socialSecurity == null || user.getRegionCode()==null){
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
                   // detail.setPayable("0");
                    detail.setActual("0");
                }else{
                    teamSalaryService.handleSocialSalaryDetail(socialSecurity, detail, salary.getStartTime());
                }
                detail.setRemark("");

                detailList.add(detail);
            }
            teamSalaryService.batchSave(detailList);
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
        return "projectmanage/team/salary/organ/step2";
    }


    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public Result start(String id) {

        Salary salary = teamSalaryService.get(Salary.class, id);

        return teamSalaryService.startOrganSalaryFlow(salary, "organSalary");
    }
}
