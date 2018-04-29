/**
 * 
 */
package com.radish.master.service.project;

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
import com.radish.master.entity.project.Salary;
import com.radish.master.pojo.Options;

@Service("teamSalaryService")
public class TeamSalaryServiceImpl extends BaseServiceImpl implements TeamSalaryService {

	@Resource
	private RuntimePageService runtimePageService;

	@Override
	public List<Options> getLaborComboboxByProject(String projectID) {
		return this.findMapBySql(
				"select id value, contract_name data from tbl_labor where project_id=? AND Status='30'",
				new Object[] { projectID }, new Type[] { StringType.INSTANCE }, Options.class);
	}

	@Override
	public List<Options> getVolumeComboboxByProjectAndLabor(String projectID, String laborID) {
		return this.findMapBySql(
				"select id value, concat(concat(date_format(start_time, '%Y-%m-%d'),'至'),date_format(end_time, '%Y-%m-%d')) data from tbl_project_volume where project_id=? AND labor_id=? AND Status='70'",
				new Object[] { projectID, laborID }, new Type[] { StringType.INSTANCE, StringType.INSTANCE },
				Options.class);
	}

	@Override
	public List<Options> getUserComboboxByProjectAndLabor(String projectID, String laborID) {
		return this.findMapBySql(
				"SELECT UT.user_id value, UT.user_name data FROM tbl_user_team UT, tbl_labor L WHERE UT.team_id = L.construction_team_id AND L.project_id=? AND L.id=?",
				new Object[] { projectID, laborID }, new Type[] { StringType.INSTANCE, StringType.INSTANCE }, Options.class);
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

		// 启动流程
		return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(),
				businessKey);
	}

}
