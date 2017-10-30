/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司   
 */
package com.cnpc.framework.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cnpc.framework.utils.SystemEnvironment;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年10月30日    Create this file
* </pre>
* 
*/

public class WebappListener implements ServletContextListener{
    
    protected ServletContext servletContext;

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        this.servletContext = servletContextEvent.getServletContext();
        initEnvironment(this.servletContext);
        
    }
    
    private static void initEnvironment(ServletContext servletContext){
        SystemEnvironment.UPLOAD_PATH = servletContext.getInitParameter("uploadConfigLocation");
    }
    
}
