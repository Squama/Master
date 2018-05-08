/**
 * 
 */
package com.radish.master.service.project;

import java.util.List;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.project.Salary;
import com.radish.master.pojo.Options;

public interface TeamSalaryService extends BaseService{

	List<Options> getLaborComboboxByProject(String projectID);
	
	List<Options> getVolumeComboboxByProjectAndLabor(String projectID, String laborID);
	
	List<Options> getUserComboboxByProjectAndLabor(String projectID, String laborID);
	
	List<Options> getManageUserComboboxByProject(String projectID);
	
	public Result startTeamSalaryFlow(Salary salary, String processDefinitionKey);
	
	public Result startManagerSalaryFlow(Salary salary, String processDefinitionKey);
	
	List<Salary> checkTimePeriod(String projectID, String startTimeStr, String endTimeStr, String salaryID);
}
