/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.entity.wechat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年6月25日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_attendance")
public class Attendance extends BaseEntity {

    private static final long serialVersionUID = 6855023005894812344L;

    @Header(name = "月份")
    @Column(name = "month")
    private String month;

    @Header(name = "姓名")
    @Column(name = "name")
    private String name;

    @Header(name = "账号")
    @Column(name = "account")
    private String account;

    @Header(name = "应打卡天数")
    @Column(name = "suppose")
    private String suppose;

    @Header(name = "正常打卡天数")
    @Column(name = "normal")
    private String normal;

    @Header(name = "异常打卡天数")
    @Column(name = "exception")
    private String exception;

    @Header(name = "早退")
    @Column(name = "early")
    private String early;

    @Header(name = "迟到")
    @Column(name = "late")
    private String late;

    @Header(name = "旷工")
    @Column(name = "completion")
    private String completion;

    @Header(name = "合计")
    @Column(name = "total")
    private String total;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSuppose() {
        return suppose;
    }

    public void setSuppose(String suppose) {
        this.suppose = suppose;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getEarly() {
        return early;
    }

    public void setEarly(String early) {
        this.early = early;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }

    public String getCompletion() {
        return completion;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
