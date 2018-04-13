/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司  
 */
package com.radish.master.service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.BaseService;
import com.radish.master.entity.Labor;
import com.radish.master.entity.ProjectVolume;
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
    
    public Result startVolumeFlow(ProjectVolume projectVolume, String processDefinitionKey);
    
    public Result startLaborFlow(Labor labor, String processDefinitionKey);
}
