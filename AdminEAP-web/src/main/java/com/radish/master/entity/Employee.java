/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity;

import java.util.Date;

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
* dongyan      2017年10月17日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_employee")
public class Employee extends BaseEntity {

    private static final long serialVersionUID = -4535904106336743574L;

    @Header(name = "姓名")
    @Column(name = "name")
    private String name;

    @Header(name = "性别")
    @Column(name = "sex")
    private String sex;

    @Header(name = "出生日期")
    @Column(name = "birthday")
    private Date birthday;

    @Header(name = "民族")
    @Column(name = "nation")
    private String nation;

    @Header(name = "籍贯")
    @Column(name = "nativeplace")
    private String nativeplace;

    @Header(name = "政治面貌")
    @Column(name = "political")
    private String political;

    @Header(name = "婚姻状况")
    @Column(name = "marriage")
    private String marriage;

    @Header(name = "出生地")
    @Column(name = "birthplace")
    private String birthplace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getNativeplace() {
        return nativeplace;
    }

    public void setNativeplace(String nativeplace) {
        this.nativeplace = nativeplace;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }
    
}
