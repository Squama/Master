package com.radish.master.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * JsonUtil
 * 
 * <pre>
 * Modify Information:
 * Author       Date        Description
 * ============ =========== ============================
 * dongyan      2018-03-28  Create this file
 * </pre>
 * 
 */
public class JsonUtil {

    /**
     * json串转换为fastJson对象
     * @param json
     * @return
     */
    public static JSONObject str2JSONObject(String json) {
        return JSON.parseObject(json);
    }
    
}
