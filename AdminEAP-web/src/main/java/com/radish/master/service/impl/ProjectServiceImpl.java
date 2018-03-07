/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司  
 */
package com.radish.master.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cnpc.framework.activiti.pojo.Constants;
import com.cnpc.framework.activiti.service.RuntimePageService;
import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.FileUtil;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.SecurityUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.Labor;
import com.radish.master.entity.Project;
import com.radish.master.entity.ProjectFileItem;
import com.radish.master.entity.ProjectVolume;
import com.radish.master.pojo.Options;
import com.radish.master.service.ProjectService;
import com.radish.master.system.FileHelper;

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
@Service("projectService")
public class ProjectServiceImpl extends BaseServiceImpl implements ProjectService {
    
    @Resource
    private RuntimePageService runtimePageService;
    
    private static Logger logger= LoggerFactory.getLogger(ProjectServiceImpl.class);
    

    /**
     * 创建文件
     *
     * @param file
     * @return
     */
    public File createFile(MultipartFile file) {

        String dirPath = PropertiesUtil.getValue("projectFilePath");
        return createFile(file, dirPath);
    }

    public File createFile(MultipartFile file, String dirPath) {

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String filePath = dirPath + "/" + (new Date().getTime()) + "_" + file.getOriginalFilename();
        File newFile = new File(filePath);
        try {
            InputStream ins = file.getInputStream();
            OutputStream os = new FileOutputStream(newFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile;
    }

    @Override
    public byte[] getImage(String path, String name) {
        try{
            
            //调接口写读文件
            return FileHelper.showImageFile(name, path);
        }
        catch(Exception e){
            logger.error("", e);
        }
        
        return new byte[0];
    }

    @Override
    public Result getManagerName(String id) {
        String name=this.redisDao.get("manager:"+id);
        if(StrUtil.isEmpty(name)) {
            User user=this.get(User.class,id);
            redisDao.save("org:"+id,user.getName());
            return new Result(true,user.getName(),"获取成功");
        }else{
            return new Result(true,name,"获取成功");
        }
    }

    @Override
    public String getFileField(String batchNo) {
        String result = "";
        
        if(StrUtil.isEmpty(batchNo)){
            return result;
        }
        
        String hql = "from ProjectFileItem where batchNo=:batchNo";
        Map<String, Object> params = new HashMap<>();
        params.put("batchNo", batchNo);
        List<ProjectFileItem> itemList = this.find(hql, params);
        if(itemList.isEmpty()){
            result = "";
        }else{
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<itemList.size();i++){
                sb.append(",");
                sb.append(itemList.get(i).getId());
            }
            result = sb.toString().substring(1);
        }
        return result;
    }

    @Override
    public Result deleteFileItem(String projectID, String fileField, String key) {
        ProjectFileItem item=this.get(ProjectFileItem.class, key);
        String batchNo = item.getBatchNo();
        FileUtil.delFile(item.getFilePath());
        this.delete(item);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("batchNo", batchNo);
        List<ProjectFileItem> list =  this.find("from ProjectFileItem where batchNo = :batchNo", params);
        
        if(list.isEmpty()){
            Project project = this.get(Project.class, projectID);
            String methodStr = "set"+fileField.toUpperCase().substring(0, 1)+fileField.substring(1);
            Method method;
            try {
                method = Project.class.getMethod(methodStr,new Class[]{String.class});
                method.invoke(project, new Object[]{null});
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                return new Result(false, e.getMessage());
            }
            this.update(project);
        }
        
        return new Result(true);
    }

    @Override
    public List<Options> getLaborComboboxByProject(String projectID) {
        return this.findMapBySql("select id value, contract_name data from tbl_labor where project_id=?", new Object[] {projectID},
                new Type[] { StringType.INSTANCE }, Options.class);
    }

    @Override
    public Result startVolumeFlow(ProjectVolume projectVolume, String processDefinitionKey) {
        User user = SecurityUtil.getUser();

        Labor labor = this.get(Labor.class, projectVolume.getLaborID());
        projectVolume.setStatus("20");

        this.update(projectVolume);

        String name = "项目：" + projectVolume.getProjectName() + "合同：" + labor.getContractName() + "工程量上报";

        // businessKey
        String businessKey = projectVolume.getId();

        // 配置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_APPLYUSER_NAME, user.getName());
        variables.put(Constants.VAR_BUSINESS_KEY, businessKey);

        // 启动流程
        return runtimePageService.startProcessInstanceByKey(processDefinitionKey, name, variables, user.getId(),
                businessKey);
    }

    
}
