/**
 * 
 */
package com.radish.master.service.project;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.common.ActivitiSuggestion;
import com.radish.master.entity.project.Salary;
import com.radish.master.pojo.Options;

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
    public List<Options> getManageUserComboboxByProject(String projectID) {
        return this.findMapBySql(
                "SELECT UT.user_id value, UT.user_name data FROM tbl_user_team UT, tbl_project_team PT WHERE UT.project_id = PT.project_id AND PT.status='30' AND UT.project_id=?",
                new Object[] { projectID }, new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public Result startTeamSalaryFlow(Salary salary, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        salary.setStatus("20");

        this.update(salary);

        String name = "劳务合同：" + salary.getContractName() + "【工资单审核】";

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
        
        if(as == null){
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
        
        if(as == null){
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

        if(!StrUtil.isEmpty(salaryID)){
            buffer.append(" AND id <> :id");
            params.put("id", salaryID);
        }
        
        return this.findBySql(buffer.toString(), params, Salary.class);
    }

}
