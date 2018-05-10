/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.pojo;

import javax.persistence.Entity;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年12月5日    Create this file
* </pre>
* 
*/
@Entity
public class OptionsNew {
    
    private Object value;
    
    private String data;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
}
