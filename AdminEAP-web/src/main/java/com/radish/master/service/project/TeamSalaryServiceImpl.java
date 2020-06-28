/**
 * 
 */
package com.radish.master.service.project;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.common.ActivitiSuggestion;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.SalaryVolume;
import com.radish.master.entity.project.SocialSecurity;
import com.radish.master.entity.project.Worker;
import com.radish.master.pojo.Options;
import com.radish.master.pojo.SalarySubInfo;
import com.radish.master.system.TimeUtil;

@Service("teamSalaryService")
public class TeamSalaryServiceImpl extends BaseServiceImpl implements TeamSalaryService {

    @Resource
    private RuntimePageService runtimePageService;

    @Override
    public List<Options> getLaborComboboxByProject(String projectID) {
        return this.findMapBySql("select id value, contract_name data from tbl_labor where project_id=? AND Status='30'", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getTeamComboboxByProject(String projectID) {
        return this.findMapBySql("select id value, team_name data from tbl_project_team where project_id=? AND status = '10'", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }
    
    @Override
    public List<Options> getPointTeamComboboxByProject(String projectID) {
        return this.findMapBySql("select id value, team_name data from tbl_project_team where project_id=? AND status = '20'", new Object[] { projectID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getVolumeComboboxByProjectAndLabor(String projectID, String laborID) {
        return this.findMapBySql(
                "select id value, concat(concat(date_format(start_time, '%Y-%m-%d'),'至'),date_format(end_time, '%Y-%m-%d')) data from tbl_project_volume where project_id=? AND labor_id=? AND Status='70'",
                new Object[] { projectID, laborID }, new Type[] { StringType.INSTANCE, StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getUserComboboxByProjectAndLabor(String projectID, String laborID) {
        return this.findMapBySql(
                "SELECT UT.user_id value, UT.user_name data FROM tbl_user_team UT, tbl_labor L WHERE UT.team_id = L.construction_team_id AND L.project_id=? AND L.id=?",
                new Object[] { projectID, laborID }, new Type[] { StringType.INSTANCE, StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<Options> getUserComboboxByTeam(String teamID) {
        return this.findMapBySql("SELECT UT.user_id value, UT.user_name data FROM tbl_user_team UT WHERE UT.team_id =? ", new Object[] { teamID },
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public List<User> getManageMemberByProject(String projectID) {
        
        StringBuilder buffer = new StringBuilder();

        buffer.append("SELECT U.* ");
        buffer.append("FROM tbl_user U,tbl_user_team UT, tbl_project_team PT  ");
        buffer.append("WHERE U.id=UT.user_id AND UT.project_id = PT.project_id  ");
        buffer.append("AND UT.team_id=PT.id AND PT.status='30'  ");
        buffer.append("AND UT.project_id=:projectID ");
        buffer.append("Group BY U.ID ");

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("projectID", projectID);

        return this.findBySql(buffer.toString(), params, User.class);
    }
    
    @Override
    public List<Worker> getProMemberByProject(String projectID, String teamID) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("SELECT U.* ");
        buffer.append("FROM tbl_worker U,tbl_user_team UT, tbl_project_team PT  ");
        buffer.append("WHERE U.id=UT.user_id AND UT.project_id = PT.project_id  ");
        buffer.append("AND UT.team_id=PT.id AND PT.status='10'  ");
        buffer.append("AND UT.project_id=:projectID ");
        buffer.append("AND UT.team_id=:teamID ");

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("projectID", projectID);
        params.put("teamID", teamID);

        return this.findBySql(buffer.toString(), params, Worker.class);
    }
    
    @Override
    public List<Worker> getPointMemberByProject(String projectID, String teamID) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("SELECT U.* ");
        buffer.append("FROM tbl_worker U,tbl_user_team UT, tbl_project_team PT  ");
        buffer.append("WHERE U.id=UT.user_id AND UT.project_id = PT.project_id  ");
        buffer.append("AND UT.team_id=PT.id AND PT.status='20'  ");
        buffer.append("AND UT.project_id=:projectID ");
        buffer.append("AND UT.team_id=:teamID ");

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("projectID", projectID);
        params.put("teamID", teamID);

        return this.findBySql(buffer.toString(), params, Worker.class);
    }

    @Override
    public List<User> getOrganMember(String deptent){
        StringBuilder buffer = new StringBuilder();

        buffer.append("SELECT * FROM tbl_user WHERE id NOT IN( ");
        buffer.append("SELECT DISTINCT U.id FROM tbl_user U,tbl_user_team UT, tbl_project_team PT   ");
        buffer.append("WHERE U.id=UT.user_id AND UT.project_id = PT.project_id  AND UT.team_id=PT.id AND PT.status='30' ) ");
        buffer.append(" and audit_status <>'50' ");
        buffer.append(" and deptent ='"+deptent+"' ");
        return this.findBySql(buffer.toString(), User.class);
    }
    
    @Override
    public Result startTeamSalaryFlow(Salary salary, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        salary.setStatus("20");
        List<SalaryDetail> mxs = baseDao.find("from SalaryDetail where salaryID= '"+salary.getId()+"'");
        if(mxs.size()==0){
        	return new Result(false,"无工资明细，不能提交");
        }
        BigDecimal zje = BigDecimal.ZERO;
        for(SalaryDetail mx : mxs){
        	zje = zje.add(new BigDecimal(mx.getActual()));
        }
        salary.setApplySum(zje+"");
        this.update(salary);

        String name = "专业作业班组：" + salary.getTeamName() + "【工资单审核】";

        // businessKey
        String businessKey = salary.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        String suggestionHql = "from ActivitiSuggestion where businessKey=:businessKey AND taskNode=:taskNode";
        Map<String, Object> suggestionParams = new HashMap<>();
        suggestionParams.put("businessKey", businessKey);
        suggestionParams.put("taskNode", "caozuoyuan");
        ActivitiSuggestion as = this.get(suggestionHql, suggestionParams);

        if (as == null) {
            as = new ActivitiSuggestion();
            as.setCreateDateTime(new Date());
            as.setUpdateDateTime(new Date());
            as.setBusinessKey(businessKey);
            as.setTaskNode("caozuoyuan");
            as.setApprove("true");
        }

        as.setSuggestion("");
        as.setOperator(SecurityUtil.getUser().getName());
        as.setUpdateDateTime(new Date());
        this.save(as);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(), businessKey);
    }
    
    @Override
    public Result startPointSalaryFlow(Salary salary, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        salary.setStatus("20");
        List<SalaryDetail> mxs = baseDao.find("from SalaryDetail where salaryID= '"+salary.getId()+"'");
        if(mxs.size()==0){
        	return new Result(false,"无工资明细，不能提交");
        }
        BigDecimal zje = BigDecimal.ZERO;
        for(SalaryDetail mx : mxs){
        	zje = zje.add(new BigDecimal(mx.getActual()));
        }
        salary.setApplySum(zje+"");
        this.update(salary);

        String name = "点工班组：" + salary.getTeamName() + "【工资单审核】";

        // businessKey
        String businessKey = salary.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        String suggestionHql = "from ActivitiSuggestion where businessKey=:businessKey AND taskNode=:taskNode";
        Map<String, Object> suggestionParams = new HashMap<>();
        suggestionParams.put("businessKey", businessKey);
        suggestionParams.put("taskNode", "caozuoyuan");
        ActivitiSuggestion as = this.get(suggestionHql, suggestionParams);

        if (as == null) {
            as = new ActivitiSuggestion();
            as.setCreateDateTime(new Date());
            as.setUpdateDateTime(new Date());
            as.setBusinessKey(businessKey);
            as.setTaskNode("caozuoyuan");
            as.setApprove("true");
        }

        as.setSuggestion("");
        as.setOperator(SecurityUtil.getUser().getName());
        as.setUpdateDateTime(new Date());
        this.save(as);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(), businessKey);
    }

    @Override
    public Result startManagerSalaryFlow(Salary salary, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        salary.setStatus("20");
        List<SalaryDetail> mxs = baseDao.find("from SalaryDetail where salaryID= '"+salary.getId()+"'");
        if(mxs.size()==0){
        	return new Result(false,"无工资明细，不能提交");
        }
        BigDecimal zje = BigDecimal.ZERO;
        for(SalaryDetail mx : mxs){
        	zje = zje.add(new BigDecimal(mx.getActual()));
        }
        salary.setApplySum(zje+"");
        this.update(salary);

        String name = "项目：" + salary.getProjectName() + "【管理人员工资单审核】";

        // businessKey
        String businessKey = salary.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        String suggestionHql = "from ActivitiSuggestion where businessKey=:businessKey AND taskNode=:taskNode";
        Map<String, Object> suggestionParams = new HashMap<>();
        suggestionParams.put("businessKey", businessKey);
        suggestionParams.put("taskNode", "caozuoyuan");
        ActivitiSuggestion as = this.get(suggestionHql, suggestionParams);

        if (as == null) {
            as = new ActivitiSuggestion();
            as.setCreateDateTime(new Date());
            as.setUpdateDateTime(new Date());
            as.setBusinessKey(businessKey);
            as.setTaskNode("caozuoyuan");
            as.setApprove("true");
        }

        as.setSuggestion("");
        as.setOperator(SecurityUtil.getUser().getName());
        as.setUpdateDateTime(new Date());
        this.save(as);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(), businessKey);
    }
    
    @Override
    public Result startOrganSalaryFlow(Salary salary, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        salary.setStatus("20");
        List<SalaryDetail> mxs = baseDao.find("from SalaryDetail where salaryID= '"+salary.getId()+"'");
        if(mxs.size()==0){
        	return new Result(false,"无工资明细，不能提交");
        }
        BigDecimal zje = BigDecimal.ZERO;
        for(SalaryDetail mx : mxs){
        	zje = zje.add(new BigDecimal(mx.getActual()));
        }
        salary.setApplySum(zje+"");
        this.update(salary);

        String name = "【机关人员工资单审核】";

        // businessKey
        String businessKey = salary.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        String suggestionHql = "from ActivitiSuggestion where businessKey=:businessKey AND taskNode=:taskNode";
        Map<String, Object> suggestionParams = new HashMap<>();
        suggestionParams.put("businessKey", businessKey);
        suggestionParams.put("taskNode", "caozuoyuan");
        ActivitiSuggestion as = this.get(suggestionHql, suggestionParams);

        if (as == null) {
            as = new ActivitiSuggestion();
            as.setCreateDateTime(new Date());
            as.setUpdateDateTime(new Date());
            as.setBusinessKey(businessKey);
            as.setTaskNode("caozuoyuan");
            as.setApprove("true");
        }

        as.setSuggestion("");
        as.setOperator(SecurityUtil.getUser().getName());
        as.setUpdateDateTime(new Date());
        this.save(as);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(), businessKey);
    }
    
    @Override
    public Result startHodSalaryFlow(Salary salary, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        salary.setStatus("20");
        List<SalaryDetail> mxs = baseDao.find("from SalaryDetail where salaryID= '"+salary.getId()+"'");
        if(mxs.size()==0){
        	return new Result(false,"无工资明细，不能提交");
        }
        BigDecimal zje = BigDecimal.ZERO;
        for(SalaryDetail mx : mxs){
        	zje = zje.add(new BigDecimal(mx.getActual()));
        }
        salary.setApplySum(zje+"");
        this.update(salary);

        String name = "【门卫机修人员工资单审核】";

        // businessKey
        String businessKey = salary.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        String suggestionHql = "from ActivitiSuggestion where businessKey=:businessKey AND taskNode=:taskNode";
        Map<String, Object> suggestionParams = new HashMap<>();
        suggestionParams.put("businessKey", businessKey);
        suggestionParams.put("taskNode", "caozuoyuan");
        ActivitiSuggestion as = this.get(suggestionHql, suggestionParams);

        if (as == null) {
            as = new ActivitiSuggestion();
            as.setCreateDateTime(new Date());
            as.setUpdateDateTime(new Date());
            as.setBusinessKey(businessKey);
            as.setTaskNode("caozuoyuan");
            as.setApprove("true");
        }

        as.setSuggestion("");
        as.setOperator(SecurityUtil.getUser().getName());
        as.setUpdateDateTime(new Date());
        this.save(as);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(), businessKey);
    }
    @Override
    public Result startElseSalaryFlow(Salary salary, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        salary.setStatus("30");
        List<SalaryDetail> mxs = baseDao.find("from SalaryDetail where salaryID= '"+salary.getId()+"'");
        if(mxs.size()==0){
        	return new Result(false,"无工资明细，不能提交");
        }
        BigDecimal zje = BigDecimal.ZERO;
        for(SalaryDetail mx : mxs){
        	zje = zje.add(new BigDecimal(mx.getActual()));
        }
        salary.setApplySum(zje+"");
        this.update(salary);

        String name = "【其他类型（13薪、奖金、津贴）发放单审核】";

        // businessKey
        String businessKey = salary.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);
        variables.put("taskName", name);

        String suggestionHql = "from ActivitiSuggestion where businessKey=:businessKey AND taskNode=:taskNode";
        Map<String, Object> suggestionParams = new HashMap<>();
        suggestionParams.put("businessKey", businessKey);
        suggestionParams.put("taskNode", "caozuoyuan");
        ActivitiSuggestion as = this.get(suggestionHql, suggestionParams);

        if (as == null) {
            as = new ActivitiSuggestion();
            as.setCreateDateTime(new Date());
            as.setUpdateDateTime(new Date());
            as.setBusinessKey(businessKey);
            as.setTaskNode("caozuoyuan");
            as.setApprove("true");
        }

        as.setSuggestion("");
        as.setOperator(SecurityUtil.getUser().getName());
        as.setUpdateDateTime(new Date());
        this.save(as);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(), businessKey);
    }

    @Override
    public List<Salary> checkTimePeriod(String projectID, String startTimeStr, String endTimeStr, String salaryID) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("SELECT * FROM tbl_salary ");
        buffer.append("WHERE UNIX_TIMESTAMP(start_time) <= UNIX_TIMESTAMP(:endTime) ");
        buffer.append("AND UNIX_TIMESTAMP(end_time) >= UNIX_TIMESTAMP(:startTime) ");
        buffer.append("AND project_id=:projectID AND type='20' ");

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("endTime", endTimeStr);
        params.put("startTime", startTimeStr);
        params.put("projectID", projectID);

        if (!StrUtil.isEmpty(salaryID)) {
            buffer.append(" AND id <> :id");
            params.put("id", salaryID);
        }

        return this.findBySql(buffer.toString(), params, Salary.class);
    }
    
    @Override
    public List<Salary> checkHodTimePeriod(String projectID, String startTimeStr, String endTimeStr, String salaryID) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("SELECT * FROM tbl_salary ");
        buffer.append("WHERE UNIX_TIMESTAMP(start_time) <= UNIX_TIMESTAMP(:endTime) ");
        buffer.append("AND UNIX_TIMESTAMP(end_time) >= UNIX_TIMESTAMP(:startTime) ");
        buffer.append("AND project_id=:projectID AND type='50' ");

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("endTime", endTimeStr);
        params.put("startTime", startTimeStr);
        params.put("projectID", projectID);

        if (!StrUtil.isEmpty(salaryID)) {
            buffer.append(" AND id <> :id");
            params.put("id", salaryID);
        }

        return this.findBySql(buffer.toString(), params, Salary.class);
    }
    
    
    @Override
    public List<Salary> checkOrganTimePeriod(String startTimeStr, String endTimeStr, String salaryID,String deptent) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("SELECT * FROM tbl_salary ");
        buffer.append("WHERE UNIX_TIMESTAMP(start_time) <= UNIX_TIMESTAMP(:endTime) ");
        buffer.append("AND UNIX_TIMESTAMP(end_time) >= UNIX_TIMESTAMP(:startTime) ");
        buffer.append("AND type='40' ");
        buffer.append("AND deptent='"+deptent+"' ");

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("endTime", endTimeStr);
        params.put("startTime", startTimeStr);

        if (!StrUtil.isEmpty(salaryID)) {
            buffer.append(" AND id <> :id");
            params.put("id", salaryID);
        }

        return this.findBySql(buffer.toString(), params, Salary.class);
    }

    @Override
    public SalaryVolume getBySalaryAndVolume(String salaryID, String volumeID) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("salaryID", salaryID);
        params.put("volumeID", volumeID);
        return this.get("from SalaryVolume where salaryID=:salaryID AND volumeID=:volumeID ", params);
    }

    @Override
    public List<SalarySubInfo> getSubInfoList(String salaryID) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ls.sub_name subName, pv.start_time startTime, pv.end_time endTime ");
        sb.append("FROM tbl_salary s, tbl_salary_volume sv, tbl_project_volume pv, tbl_labor_sub ls ");
        sb.append("WHERE s.id = sv.salary_id AND sv.volume_id = pv.id AND pv.labor_sub_id = ls.id ");
        sb.append(" AND s.id=?");
        return this.findMapBySql(sb.toString(), new Object[] { salaryID }, new Type[] { StringType.INSTANCE }, SalarySubInfo.class);
    }
    
    @Override
    public boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    // 首字母大写
    @Override
    public String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    @Override
    public List<SocialSecurity> getSocialSecurity(String year) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("year", year);
        return this.find("from SocialSecurity where year=:year", params);
    }

    @Override
    public void handleSocialSalaryDetail(SocialSecurity socialSecurity, SalaryDetail detail, Date startDate) {
        Map<Double, Double> map = new HashMap<>();
        map.put(0.00, Double.valueOf(socialSecurity.getRadix()));
        map.put(Double.valueOf(socialSecurity.getRadix()),Double.valueOf(socialSecurity.getAvg()));
        map.put(Double.valueOf(socialSecurity.getAvg()), 99999999.99);
        BigDecimal thd = new BigDecimal(socialSecurity.getAvg());
        thd = thd.multiply(new BigDecimal("3"));
        //三倍平均工资
        Double thdAvg = thd.doubleValue();
        String calRadix = "0";
        
        Iterator<Map.Entry<Double, Double>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Double, Double> entry = it.next();
            if (entry.getKey() <= Double.valueOf(detail.getBasicSalary()) && map.get(entry.getKey()) > Double.valueOf(detail.getBasicSalary())) {
                if(entry.getKey() == 0.00){
                    calRadix = socialSecurity.getRadix();
                }else if(map.get(entry.getKey()) == 99999999.99){
                    if(Double.valueOf(detail.getBasicSalary()) > thdAvg){
                        calRadix = thd.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
                    }else{
                        calRadix = detail.getBasicSalary();
                    }
                }else{
                    calRadix = detail.getBasicSalary();
                }
            }
        }
        
        BigDecimal basic = new BigDecimal(calRadix);
        BigDecimal yanglao = new BigDecimal(socialSecurity.getYanglao()).multiply(new BigDecimal("0.01"));
        BigDecimal yanglaoCorp = new BigDecimal(socialSecurity.getYanglaoCorp()).multiply(new BigDecimal("0.01"));
        BigDecimal yiliao = new BigDecimal(socialSecurity.getYiliao()).multiply(new BigDecimal("0.01"));
        BigDecimal yiliaoCorp = new BigDecimal(socialSecurity.getYiliaoCorp()).multiply(new BigDecimal("0.01"));
        BigDecimal shiye = new BigDecimal(socialSecurity.getShiye()).multiply(new BigDecimal("0.01"));
        BigDecimal shiyeCorp = new BigDecimal(socialSecurity.getShiyeCorp()).multiply(new BigDecimal("0.01"));
        BigDecimal gongshang = new BigDecimal(socialSecurity.getGongshang()).multiply(new BigDecimal("0.01"));
        BigDecimal shengyu = new BigDecimal(socialSecurity.getShengyu()).multiply(new BigDecimal("0.01"));
        BigDecimal gongjijin = new BigDecimal(socialSecurity.getGongjijin()).multiply(new BigDecimal("0.01"));

        
        BigDecimal yanglaoP = basic.multiply(yanglao);//养老个人缴纳
        BigDecimal yanglaoC = basic.multiply(yanglaoCorp);//养老公司缴纳
        BigDecimal yiliaoP = basic.multiply(yiliao);//医疗个人缴纳
        BigDecimal yiliaoC = basic.multiply(yiliaoCorp);//医疗公司缴纳
        BigDecimal shiyeP = basic.multiply(shiye);//失业个人缴纳
        BigDecimal shiyeC = basic.multiply(shiyeCorp);//失业公司缴纳
        BigDecimal gongshangC = basic.multiply(gongshang);//工伤公司缴纳
        BigDecimal shengyuC = basic.multiply(shengyu);//生育公司缴纳
        BigDecimal gongjijinP = basic.multiply(gongjijin);//公积金个人缴纳
        BigDecimal gongjijinC = basic.multiply(gongjijin);//公积金公司缴纳
        
        detail.setYanglao(yanglaoP.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setYanglaoCorp(yanglaoC.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setMedical(yiliaoP.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setMedicalCorp(yiliaoC.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setShiye(shiyeP.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setShiyeCorp(shiyeC.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setGongshangCorp(gongshangC.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setShengyuCorp(shengyuC.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setPrf(gongjijinP.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setPrfCorp(gongjijinC.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        
        //原社保字段 =失业+养老
        detail.setSocial(shiyeP.add(yanglaoP).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        
        BigDecimal salarySoFar = new BigDecimal(detail.getBasicSalary());
        
        salarySoFar = salarySoFar.subtract(yanglaoP).subtract(yiliaoP).subtract(shiyeP).subtract(gongjijinP);
        
        //获取总工资和已交税款
        BigDecimal sumSalary = new BigDecimal("0");
        BigDecimal sumTax = new BigDecimal("0");
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userID", detail.getUserID());
        params.put("ssYear", socialSecurity.getYear());
        List<SalaryDetail> list =  this.find("from SalaryDetail where userID =:userID AND ssYear =:ssYear", params);
        for(SalaryDetail his : list){
            sumSalary = sumSalary.add(new BigDecimal(his.getPayable()).add(new BigDecimal(his.getTax())));
            sumTax = sumTax.add(new BigDecimal(his.getTax()));
        }
        
        BigDecimal sumMonth = new BigDecimal(TimeUtil.getFormattedTime2(startDate).substring(4, 6));
        BigDecimal starter = new BigDecimal(PropertiesUtil.getValue("tax.manager.starter"));
        BigDecimal deduction = new BigDecimal(detail.getDeduction());
        
        String taxRateArea = PropertiesUtil.getValue("tax.manager");
        BigDecimal taxRate = new BigDecimal("0");
        BigDecimal taxDeductionNum = new BigDecimal("0");
        
        BigDecimal taxRank = sumSalary.subtract(sumMonth.multiply(starter.add(deduction))).add(salarySoFar);
        
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
        
        String tax = taxRank.multiply(taxRate).subtract(taxDeductionNum).subtract(sumTax).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
        
        detail.setTax(tax);
        detail.setPayable(detail.getBasicSalary());
        detail.setActual(salarySoFar.subtract(new BigDecimal(tax)).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        
    }

    @Override
    public void handleSocialSalaryDetailPayable(SocialSecurity socialSecurity, SalaryDetail detail, Date startDate) {
        Map<Double, Double> map = new HashMap<>();
        map.put(0.00, Double.valueOf(socialSecurity.getRadix()));
        map.put(Double.valueOf(socialSecurity.getRadix()),Double.valueOf(socialSecurity.getAvg()));
        map.put(Double.valueOf(socialSecurity.getAvg()), 99999999.99);
        BigDecimal thd = new BigDecimal(socialSecurity.getAvg());
        thd = thd.multiply(new BigDecimal("3"));
        //三倍平均工资
        Double thdAvg = thd.doubleValue();
        String calRadix = "0";
        
        Iterator<Map.Entry<Double, Double>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Double, Double> entry = it.next();
            if (entry.getKey() <= Double.valueOf(detail.getPayable()) && map.get(entry.getKey()) > Double.valueOf(detail.getPayable())) {
                if(entry.getKey() == 0.00){
                    calRadix = socialSecurity.getRadix();
                }else if(map.get(entry.getKey()) == 99999999.99){
                    if(Double.valueOf(detail.getBasicSalary()) > thdAvg){
                        calRadix = thd.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
                    }else{
                        calRadix = detail.getPayable();
                    }
                }else{
                    calRadix = detail.getPayable();
                }
            }
        }
        
        BigDecimal basic = new BigDecimal(calRadix);
        BigDecimal yanglao = new BigDecimal(socialSecurity.getYanglao()).multiply(new BigDecimal("0.01"));
        BigDecimal yanglaoCorp = new BigDecimal(socialSecurity.getYanglaoCorp()).multiply(new BigDecimal("0.01"));
        BigDecimal yiliao = new BigDecimal(socialSecurity.getYiliao()).multiply(new BigDecimal("0.01"));
        BigDecimal yiliaoCorp = new BigDecimal(socialSecurity.getYiliaoCorp()).multiply(new BigDecimal("0.01"));
        BigDecimal shiye = new BigDecimal(socialSecurity.getShiye()).multiply(new BigDecimal("0.01"));
        BigDecimal shiyeCorp = new BigDecimal(socialSecurity.getShiyeCorp()).multiply(new BigDecimal("0.01"));
        BigDecimal gongshang = new BigDecimal(socialSecurity.getGongshang()).multiply(new BigDecimal("0.01"));
        BigDecimal shengyu = new BigDecimal(socialSecurity.getShengyu()).multiply(new BigDecimal("0.01"));
        BigDecimal gongjijin = new BigDecimal(socialSecurity.getGongjijin()).multiply(new BigDecimal("0.01"));

        
        BigDecimal yanglaoP = basic.multiply(yanglao);//养老个人缴纳
        BigDecimal yanglaoC = basic.multiply(yanglaoCorp);//养老公司缴纳
        BigDecimal yiliaoP = basic.multiply(yiliao);//医疗个人缴纳
        BigDecimal yiliaoC = basic.multiply(yiliaoCorp);//医疗公司缴纳
        BigDecimal shiyeP = basic.multiply(shiye);//失业个人缴纳
        BigDecimal shiyeC = basic.multiply(shiyeCorp);//失业公司缴纳
        BigDecimal gongshangC = basic.multiply(gongshang);//工伤公司缴纳
        BigDecimal shengyuC = basic.multiply(shengyu);//生育公司缴纳
        BigDecimal gongjijinP = basic.multiply(gongjijin);//公积金个人缴纳
        BigDecimal gongjijinC = basic.multiply(gongjijin);//公积金公司缴纳
        
        detail.setYanglao(yanglaoP.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setYanglaoCorp(yanglaoC.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setMedical(yiliaoP.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setMedicalCorp(yiliaoC.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setShiye(shiyeP.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setShiyeCorp(shiyeC.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setGongshangCorp(gongshangC.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setShengyuCorp(shengyuC.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setPrf(gongjijinP.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        detail.setPrfCorp(gongjijinC.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        
        //原社保字段 =失业+养老
        detail.setSocial(shiyeP.add(yanglaoP).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        
        BigDecimal salarySoFar = new BigDecimal(detail.getPayable());
        
        salarySoFar = salarySoFar.subtract(yanglaoP).subtract(yiliaoP).subtract(shiyeP).subtract(gongjijinP);
        
        //获取总工资和已交税款
        BigDecimal sumSalary = new BigDecimal("0");
        BigDecimal sumTax = new BigDecimal("0");
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userID", detail.getUserID());
        params.put("ssYear", socialSecurity.getYear());
        params.put("id", detail.getId());
        List<SalaryDetail> list =  this.find("from SalaryDetail where userID =:userID AND ssYear =:ssYear AND id <>:id", params);
        for(SalaryDetail his : list){
            sumSalary = sumSalary.add(new BigDecimal(his.getPayable()).add(new BigDecimal(his.getTax())));
            sumTax = sumTax.add(new BigDecimal(his.getTax()));
        }
        
        BigDecimal sumMonth = new BigDecimal(TimeUtil.getFormattedTime2(startDate).substring(4, 6));
        BigDecimal starter = new BigDecimal(PropertiesUtil.getValue("tax.manager.starter"));
        BigDecimal deduction = new BigDecimal(detail.getDeduction());
        
        String taxRateArea = PropertiesUtil.getValue("tax.manager");
        BigDecimal taxRate = new BigDecimal("0");
        BigDecimal taxDeductionNum = new BigDecimal("0");
        
        BigDecimal taxRank = sumSalary.subtract(sumMonth.multiply(starter.add(deduction))).add(salarySoFar);
        
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
        
        String tax = taxRank.multiply(taxRate).subtract(taxDeductionNum).subtract(sumTax).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
        
        detail.setTax(tax);
        detail.setActual(salarySoFar.subtract(new BigDecimal(tax)).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        
    }
    
    @Override
    public Salary getBiggestSalary(String projectID, String type) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("SELECT * FROM tbl_salary ");
        buffer.append("WHERE project_id=:projectID AND type=:type ORDER BY start_time desc");

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("type", type);
        params.put("projectID", projectID);

        List<Salary> list = this.findBySql(buffer.toString(), params, Salary.class);
        if(list.isEmpty()){
            return null;
        }
        
        return list.get(0);
    }

    @Override
    public Salary getBiggestSalary(String type) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("SELECT * FROM tbl_salary ");
        buffer.append("WHERE type=:type ORDER BY start_time desc");

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("type", type);

        List<Salary> list = this.findBySql(buffer.toString(), params, Salary.class);
        if(list.isEmpty()){
            return null;
        }
        
        return list.get(0);
    }
    @Override
    public Salary getBiggestSalaryJg(String type,String deptent) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("SELECT * FROM tbl_salary ");
        buffer.append("WHERE type=:type and deptent=:deptent ORDER BY start_time desc");

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("type", type);
        params.put("deptent", deptent);

        List<Salary> list = this.findBySql(buffer.toString(), params, Salary.class);
        if(list.isEmpty()){
            return null;
        }
        
        return list.get(0);
    }
    
}
