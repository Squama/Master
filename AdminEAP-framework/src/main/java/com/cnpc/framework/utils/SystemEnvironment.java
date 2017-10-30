/**
 * 版权所有 (c) 2016，周庆博和他的朋友们有限公司 
 */
package com.cnpc.framework.utils;

import org.springframework.context.support.AbstractApplicationContext;

/**
 * 类说明：配置系统环境
 * 
 * <pre>
 * Modify Information:
 * Author        Date          Description
 * ============ =========== ============================
 * dongyan      2017-10-30   Create this file
 * </pre>
 * 
 */

public final class SystemEnvironment {

    public static String APP_VERSION;

    public static AbstractApplicationContext APPLICATION_CONTEXT;

    // 用户的默认登录密码
    public static String UPLOAD_PATH;

    private SystemEnvironment() {

    }
}
