/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.cnpc.framework.base.service.BaseService;

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
    
    
}
