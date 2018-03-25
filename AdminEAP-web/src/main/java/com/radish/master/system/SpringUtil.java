/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.system;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
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
    
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

}
