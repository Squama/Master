/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司  
 */
package com.radish.master.system;

import java.io.File;
import java.io.FileInputStream;

import com.cnpc.framework.utils.StrUtil;


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

public class FileHelper {

    /*
     * 读文件
     */
    public static byte[] showImageFile(String name, String path) throws CodeException {
        if (StrUtil.isEmpty(path)) {
            return new byte[0];
        }
        
        if (StrUtil.isEmpty(name)){
            return new byte[0];
        }
         
        try {
            String filePath = path;
            File file = new File(filePath);
            if (file.exists() || file.isFile()) {
                FileInputStream fis = new FileInputStream(file);
                return IoUtil.read(fis, 1024);
            }
        } catch (Exception e) {
            String errorCode = "2001";
            String errorMessage = "系统内部错误";
            throw new CodeException(errorCode, errorMessage);
        }

        return new byte[0];
    }

}
