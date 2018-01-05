/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.system;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年1月4日    Create this file
* </pre>
* 
*/

public class SpringUtil implements ApplicationContextAware {

    /** 
     * 当前IOC 
     *  
     */  
    private static ApplicationContext applicationContext;  

    /** 
     * * 设置当前上下文环境，此方法由spring自动装配 
     *  
     */  
    @Override  
    public void setApplicationContext(ApplicationContext arg0)  
            throws BeansException {  
        applicationContext = arg0;  
    }  

    /** 
     * 从当前IOC获取bean 
     * 
     * @param id 
     * bean的id 
     * @return 
     *
     */  
    public static Object getObject(String id) {  
        Object object = null;  
        object = applicationContext.getBean(id);  
        return object;  
    } 

}
