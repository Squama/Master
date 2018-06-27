
package com.cnpc.framework.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.rmi.ConnectException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cnpc.framework.base.pojo.AccessToken;


/**
 * 类说明
 * 
 * <pre>
 * Modify Information:
 * Author         Date         Description
 * ============ =========== ============================
 * dongyan       2018-3-28    Create this file
 * 
 * </pre>
 */

public final class WeChatUtil {

    
    private static final String CORP_ID = PropertiesUtil.getValue("CORPID");
    private static final String GET_TOKEN_URL = PropertiesUtil.getValue("GETTOKEN_URL");
    
    private WeChatUtil(){
        
    }

    /**
     * 获取access_token
     * 
     * @param appid
     *            凭证
     * @param appsecret
     *            密钥
     * @return
     * @throws CodeException
     */
    public static AccessToken getAccessToken(String sceret) throws CodeException {
        AccessToken accessToken = null;
        
        String requestUrl = GET_TOKEN_URL.replace("CORPID", CORP_ID).replace("SECRET", sceret);
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
            } catch (JSONException e) {
                // 获取token失败
                throw new CodeException("1001", "获取Token失败");
            }
        }
        return accessToken;
    }

    /**
     * 发起https请求并获取结果
     * 
     * @param requestUrl
     *            请求地址
     * @param requestMethod
     *            请求方式（GET、POST）
     * @param outputStr
     *            提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     * @throws CodeException
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) throws CodeException {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            // SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            // httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            httpUrlConn.disconnect();
            jsonObject = JsonUtil.str2JSONObject(buffer.toString());
        } catch (ConnectException ce) {
            throw new CodeException("2001", ce.getMessage());
        } catch (Exception e) {
            throw new CodeException("9001", "内部错误");
        }
        return jsonObject;
    }
    
}
