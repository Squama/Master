/**
 * 
 */
package com.radish.master.service.project;

import java.util.Date;
import java.util.List;

import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.project.Salary;
import com.radish.master.entity.project.SalaryDetail;
import com.radish.master.entity.project.SalaryVolume;
import com.radish.master.entity.project.SocialSecurity;
import com.radish.master.entity.project.Worker;
import com.radish.master.pojo.Options;
import com.radish.master.pojo.SalarySubInfo;

public interface TeamSalaryService extends BaseService {

    List<Options> getLaborComboboxByProject(String projectID);

    List<Options> getVolumeComboboxByProjectAndLabor(String projectID, String laborID);

    List<Options> getUserComboboxByProjectAndLabor(String projectID, String laborID);

    List<User> getManageMemberByProject(String projectID);
    
    List<Worker> getProMemberByProject(String projectID, String teamID);
    
    List<Worker> getPointMemberByProject(String projectID, String teamID);
    
    List<User> getOrganMember(String deptent);

    List<Options> getUserComboboxByTeam(String teamID);
    
    List<Options> getTeamComboboxByProject(String projectID);
    
    List<Options> getPointTeamComboboxByProject(String projectID);

    Result startTeamSalaryFlow(Salary salary, String processDefinitionKey);
    
    Result startPointSalaryFlow(Salary salary, String processDefinitionKey);

    Result startManagerSalaryFlow(Salary salary, String processDefinitionKey);
    
    Result startOrganSalaryFlow(Salary salary, String processDefinitionKey);
    
    Result startHodSalaryFlow(Salary salary, String processDefinitionKey);

    List<Salary> checkTimePeriod(String projectID, String startTimeStr, String endTimeStr, String salaryID);
    
    List<Salary> checkHodTimePeriod(String projectID, String startTimeStr, String endTimeStr, String salaryID);
    
    List<Salary> checkOrganTimePeriod(String startTimeStr, String endTimeStr, String salaryID,String deptent);

    SalaryVolume getBySalaryAndVolume(String salaryID, String volumeID);

    List<SalarySubInfo> getSubInfoList(String salaryID);
    
    boolean isNumber(String str);
    
    String captureName(String name);
    
    SocialSecurity getSocialSecurity(String year);
    
    void handleSocialSalaryDetail(SocialSecurity socialSecurity, SalaryDetail detail, Date startDate);
    
    Salary getBiggestSalary(String projectID, String type);
    
    Salary getBiggestSalary(String type);
    
    Salary getBiggestSalaryJg(String type,String deptent);
    
}
