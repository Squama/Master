/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.entity.project;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年11月8日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_worker")
public class Worker extends BaseEntity {

    private static final long serialVersionUID = -1279976377205210105L;

    @Header(name = "姓名")
    @Column(name = "name")
    private String name;

    @Header(name = "性别")
    @Column(name = "sex")
    private String sex;

    @Header(name = "出生年月")
    @Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Header(name = "年龄")
    @Column(name = "age")
    private String age;

    @Header(name = "民族")
    @Column(name = "ethnic")
    private String ethnic;

    @Header(name = "政治面貌")
    @Column(name = "political")
    private String political;

    @Header(name = "工种")
    @Column(name = "work_type")
    private String workType;

    @Header(name = "岗位年限")
    @Column(name = "working_year")
    private String workingYear;

    @Header(name = "婚姻状况")
    @Column(name = "marital")
    private String marital;

    @Header(name = "健康状况")
    @Column(name = "health_status")
    private String healthStatus;

    @Header(name = "有无病史")
    @Column(name = "medical_his")
    private String medicalHis;

    @Header(name = "身份证号码")
    @Column(name = "identification_number")
    private String identificationNumber;

    @Header(name = "电话号码")
    @Column(name = "mobile")
    private String mobile;

    @Header(name = "家庭住址")
    @Column(name = "address")
    private String address;

    @Header(name = "紧急联络人姓名")
    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Header(name = "紧急联络人关系")
    @Column(name = "emergency_contact_relation")
    private String emergencyContactRelation;

    @Header(name = "紧急联络人电话号码")
    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @Header(name = "头像")
    @Column(name = "avatar")
    private String avatar;
    
    @Header(name = "进场时间")
    @Column(name = "jctime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date jctime;
    
    @Header(name = "文化程度")
    @Column(name = "whcd")
    private String whcd;

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkingYear() {
        return workingYear;
    }

    public void setWorkingYear(String workingYear) {
        this.workingYear = workingYear;
    }

    public String getMarital() {
        return marital;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getMedicalHis() {
        return medicalHis;
    }

    public void setMedicalHis(String medicalHis) {
        this.medicalHis = medicalHis;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyContactRelation() {
        return emergencyContactRelation;
    }

    public void setEmergencyContactRelation(String emergencyContactRelation) {
        this.emergencyContactRelation = emergencyContactRelation;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

	public Date getJctime() {
		return jctime;
	}

	public void setJctime(Date jctime) {
		this.jctime = jctime;
	}

	public String getWhcd() {
		return whcd;
	}

	public void setWhcd(String whcd) {
		this.whcd = whcd;
	}

}
