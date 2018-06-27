
package com.cnpc.framework.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;

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

public class MyX509TrustManager implements TrustManager {

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      //zhege fangfa shi kongde 
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      //zhege fangfa shi kongde 
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[1];
    }

}
