/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司  
 */
package com.radish.master.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cnpc.framework.base.entity.User;
import com.cnpc.framework.base.pojo.Result;
import com.cnpc.framework.base.service.impl.BaseServiceImpl;
import com.cnpc.framework.utils.PropertiesUtil;
import com.cnpc.framework.utils.StrUtil;
import com.radish.master.entity.ProjectFileItem;
import com.radish.master.pojo.ProjectDetailVO;
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

    
}
