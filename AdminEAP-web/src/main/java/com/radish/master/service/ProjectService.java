/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司  
 */
package com.radish.master.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Labor;
import com.radish.master.entity.MeasureVolume;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.entity.project.ConstructionPlan;
import com.radish.master.pojo.Options;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年11月2日    Create this file
* </pre>
* 
*/

public interface ProjectService extends BaseService{

    /**
     * 创建文件
     *
     * @param file
     * @return
     */
    public File createFile(MultipartFile file);


    /**
     * 创建文件
     *
     * @param file    文件
     * @param dirPath 文件存储路径
     * @return
     */
    public File createFile(MultipartFile file, String dirPath);
    
    /**
     * 预览图
     * @param imageID
     * @return
     */
    public byte[] getImage(String path, String name);
    
    public Result getManagerName(String id);
    
    public String getFileField(String batchNo);
    
    public Result deleteFileItem(String projectID, String fileField, String key);
    
    List<Options> getLaborComboboxByProject(String projectID);
    
    List<Options> getLaborSubComboboxByLabor(String laborID);
    
    List<Options> getProjectSubCombobox(String projectID);
    
    List<Options> getPackCombobox(String projectID, String laborID);
    
    public Result startVolumeFlow(ProjectVolume projectVolume, String processDefinitionKey);
    
    public Result startMeasureVolumeFlow(MeasureVolume measureVolume, String processDefinitionKey);
    
    public Result startLaborFlow(Labor labor, String processDefinitionKey);
    
    public Result startPlanFlow(ConstructionPlan plan, String processDefinitionKey);
    
    List<Options> getTeamComboboxByProject(String projectID);
    
    List<Options> getPointTeamComboboxByProject(String projectID);
    
    List<Options> getTeamManagerComboboxByProject(String projectID);
    
    List<Options> getTeamMemberNonManagerComboboxByTeam(String teamID);
    
    List<Options> getMemberTeamComboboxByProject(String projectID);
    
    List<Options> getMemberTeamComboboxByProjectAndStatus(String projectID, int status);
    
    List<Options> getUserCombobox();
    
    List<Options> getWorkerCombobox();
    
    Map<String, String> getUserTeamCombobox();
    
    List<ProjectVolume> checkTimePeriod(String projectID, String laborID, String laborSubID, String startTimeStr, String endTimeStr, String volumeID,String sblx);
    
    List<MeasureVolume> checkMeasureTimePeriod(String projectID, String measureType, String projectSubID, String startTimeStr, String endTimeStr, String volumeID);
}
