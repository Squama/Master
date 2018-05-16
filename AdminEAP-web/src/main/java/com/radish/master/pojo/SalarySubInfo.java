/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.pojo;

import java.util.Date;

import javax.persistence.Entity;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年5月16日    Create this file
 * </pre>
 * 
 */
@Entity
public class SalarySubInfo {

    private String subName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
