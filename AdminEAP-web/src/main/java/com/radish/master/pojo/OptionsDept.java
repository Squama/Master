/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.pojo;

import javax.persistence.Entity;

/**
* 类说明
*部门id  部门 父部门
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年12月5日    Create this file
* </pre>
* 
*/
@Entity
public class OptionsDept {
    
    private String value;
    
    private String data;
    
    private String pdata;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

	public String getPdata() {
		return pdata;
	}

	public void setPdata(String pdata) {
		this.pdata = pdata;
	}
    
    
}
