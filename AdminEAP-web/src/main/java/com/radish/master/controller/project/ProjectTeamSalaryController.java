/**
 * 
 */
package com.radish.master.controller.project;

import java.lang.reflect.Method;
import java.math.BigDecimal;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cnpc.framework.annotation.RefreshCSRFToken;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.MeasureVolume;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.SalaryVolume;
import com.radish.master.entity.project.Worker;
import com.radish.master.pojo.RowEdit;
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
    @RequestMapping(method = RequestMethod.POST, value = "/getGcls")
    @ResponseBody
    public Result getGcls(String id) {
    	Result r = new Result();
    	List<SalaryVolume> gxs = teamSalaryService.find(" from SalaryVolume where salary_id='"+id+"'");
    	if(gxs.size()>0){
    		int i=1;
    		Salary s = teamSalaryService.get(Salary.class, id);
    		List<Map<String,String>> sjs = new ArrayList<Map<String,String>>();
        	if("10".equals(s.getType())){//班组工资
        		for(SalaryVolume gx : gxs){
        			Map<String,String> m = new HashMap<String, String>();
        			ProjectVolume gcl = teamSalaryService.get(ProjectVolume.class, gx.getVolumeID());
        			m.put("id", gcl.getId());
        			m.put("name", "工程量"+i);
        			m.put("lx", "10");
        			i++;
        			sjs.add(m);
        		}
        		r.setSuccess(true);
        		r.setData(sjs);
        	}else if("30".equals(s.getType())){//点工班组
        		for(SalaryVolume gx : gxs){
	        		Map<String,String> m = new HashMap<String, String>();
	    			MeasureVolume gcl = teamSalaryService.get(MeasureVolume.class, gx.getVolumeID());
	    			m.put("id", gcl.getId());
	    			m.put("name", "工程量"+i);
	    			m.put("lx", "30");
	    			i++;
	    			sjs.add(m);
        		}
        		r.setSuccess(true);
        		r.setData(sjs);
        	}else{
        		r.setSuccess(false);
        	}
    	}else{
    		r.setSuccess(false);
    	}
    	
        return r;
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
            salary.setCostSum("0");
            try {
                teamSalaryService.save(salary);
            } catch (Exception e) {
                return new Result(false);
            }
            total = salary.getTotal();
            
            // 直接录入全部明细
            List<Worker> managerList = teamSalaryService.getProMemberByProject(salary.getProjectID(), salary.getTeamID());
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
            
        	String hqlSalary = "from SalaryDetail where salaryID=:salaryID";
            Map<String, Object> paramsSalary = new HashMap<>();
            paramsSalary.put("salaryID", salary.getId());
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
        
        BigDecimal payable = new BigDecimal(detail.getPayable());
        
        if("payable".equals(field)){
            String taxRateArea = PropertiesUtil.getValue("tax.member");
            BigDecimal taxRate = new BigDecimal("0");
            BigDecimal taxDeductionNum = new BigDecimal("0");
            
            BigDecimal taxRank = new BigDecimal(detail.getPayable()).multiply(new BigDecimal("0.8"));
            
            String[] traArrays = taxRateArea.split(",");
            for (int i = 0; i < traArrays.length; i++) {
                //解析每个税率,查找税率区间
                String[] traArray = traArrays[i].split("_");
                if(Double.valueOf(traArray[0]) < taxRank.doubleValue() && taxRank.doubleValue() <= Double.valueOf(traArray[1])){
                    taxRate = new BigDecimal(traArray[2]).multiply(new BigDecimal("0.01"));
                    taxDeductionNum = new BigDecimal(traArray[3]);
                    break;
                }
            }
            
            String tax = taxRank.multiply(taxRate).subtract(taxDeductionNum).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
            detail.setTax(tax);
        }
        
        BigDecimal loan = new BigDecimal(detail.getLoan());
        
        detail.setActual(payable.subtract(loan).subtract(new BigDecimal(detail.getTax())).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());

        teamSalaryService.save(detail);
        
        
        if(!"attendance".equals(field)){
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
        
        String hqlVolume = "from SalaryVolume where salaryID=:salaryID";
        Map<String, Object> paramsVolume = new HashMap<>();
        paramsVolume.put("salaryID", id);
        List<SalaryVolume> volumeList = teamSalaryService.find(hqlVolume, paramsVolume);
        
        try {
            Salary salary = teamSalaryService.get(Salary.class, id);
            teamSalaryService.delete(salary);
            teamSalaryService.batchDelete(volumeList);
            teamSalaryService.batchDelete(detailList);
        } catch (Exception e) {
            return new Result(false);
        }
        return new Result(true);
    }

    @RefreshCSRFToken
    @RequestMapping(value = "/step2", method = RequestMethod.POST)
    public String step2(Salary salary, HttpServletRequest request) {
        request.setAttribute("id", salary.getId());
        request.setAttribute("total", salary.getTotal());
        request.setAttribute("changedTeamID", salary.getTeamID());
        request.setAttribute("pointTeams", JSONArray.toJSONString(teamSalaryService.getPointTeamComboboxByProject(salary.getProjectID())));
        return "projectmanage/team/salary/step2";
    }

    
    /*@RequestMapping(value = "/savedetail")
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
        return new Result(true);
    }
    
    @RequestMapping(value = "/changebanzu", method = RequestMethod.POST)
    @ResponseBody
    public Result changeBanzu(String salaryID, String teamID, String teamName) {

        String hql = "from SalaryDetail where salaryID=:salaryID";
        Map<String, Object> params = new HashMap<>();
        params.put("salaryID", salaryID);
        List<SalaryDetail> detailListOld = teamSalaryService.find(hql, params);
        
        try {
            teamSalaryService.batchDelete(detailListOld);
        } catch (Exception e) {
            return new Result(false);
        }
        
        Salary salary = teamSalaryService.get(Salary.class, salaryID);
        
        // 直接录入全部明细
        List<Worker> managerList = teamSalaryService.getPointMemberByProject(salary.getProjectID(), teamID);
        List<SalaryDetail> detailList = new ArrayList<SalaryDetail>();
        for (Worker user : managerList) {
            SalaryDetail detail = new SalaryDetail();
            detail.setCreateDateTime(new Date());
            detail.setUpdateDateTime(new Date());

            detail.setSalaryID(salaryID);
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
        
        try {
            teamSalaryService.batchSave(detailList);
            
            String hqlSalary = "from SalaryDetail where salaryID=:salaryID";
            Map<String, Object> paramsSalary = new HashMap<>();
            paramsSalary.put("salaryID", salary.getId());
            List<SalaryDetail> detailLists = teamSalaryService.find(hqlSalary, paramsSalary);
            BigDecimal a = new BigDecimal("0");
            for (SalaryDetail sd : detailLists) {
                BigDecimal actual = new BigDecimal(sd.getActual());

                a = a.add(actual);
            }
            salary.setApplySum(a.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
            salary.setTeamID(teamID);
            salary.setTeamName(teamName);
            teamSalaryService.update(salary);
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
