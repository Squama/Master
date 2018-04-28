package com.cnpc.framework.base.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tbl_user")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class User extends BaseEntity {

    private static final long serialVersionUID = 6093546087036436583L;

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

    @Header(name = "登录名")
    @Column(name = "login_name")
    private String loginName;

    @Header(name = "密码")
    @Column(name = "password")
    private String password;

    @Header(name = "邮箱")
    @Column(name = "email", length = 50)
    private String email;

    @Header(name = "座机")
    @Column(name = "telphone")
    private String telphone;

    @Header(name = "手机")
    @Column(name = "mobile")
    private String mobile;

    @Header(name = "QQ")
    @Column(name = "qq")
    private String qq;

    @Header(name = "微信")
    @Column(name = "wechat")
    private String wechat;

    @Header(name = "是否开启账号")
    @Column(name = "open_account", length = 5)
    private String openAccount;

    @Header(name = "超级管理员")
    @Column(name = "isSuperAdmin")
    private String isSuperAdmin;

    @Header(name = "部门ID")
    @Column(name = "dept_id")
    private String deptId;

    @Header(name = "职位ID")
    @Column(name = "job_id")
    private String jobId;

    @Header(name = "审核状态")
    @Column(name = "audit_status")
    private String auditStatus;

    @Header(name = "身份证号")
    @Column(name = "identification_number")
    private String identificationNumber;

    @Header(name = "民族")
    @Column(name = "ethnic")
    private String ethnic;

    @Header(name = "学历")
    @Column(name = "education")
    private String education;

    @Header(name = "家庭住址")
    @Column(name = "address")
    private String address;

    @Header(name = "紧急联系人电话")
    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @Header(name = "紧急联系人")
    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Header(name = "工种")
    @Column(name = "work_type")
    private String workType;

    @Header(name = "基础工资")
    @Column(name = "basic_salary")
    private String basicSalary;

    @Transient
    private String avatarId;

    @Transient
    private int isSelected;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

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

    public String getLoginName() {

        return loginName;
    }

    public void setLoginName(String loginName) {

        this.loginName = loginName;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getTelphone() {

        return telphone;
    }

    public void setTelphone(String telphone) {

        this.telphone = telphone;
    }

    public String getMobile() {

        return mobile;
    }

    public void setMobile(String mobile) {

        this.mobile = mobile;
    }

    public String getQq() {

        return qq;
    }

    public void setQq(String qq) {

        this.qq = qq;
    }

    public String getWechat() {

        return wechat;
    }

    public void setWechat(String wechat) {

        this.wechat = wechat;
    }

    public String getOpenAccount() {

        return openAccount;
    }

    public void setOpenAccount(String openAccount) {

        this.openAccount = openAccount;
    }

    public String getIsSuperAdmin() {

        return isSuperAdmin;
    }

    public void setIsSuperAdmin(String isSuperAdmin) {

        this.isSuperAdmin = isSuperAdmin;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(String basicSalary) {
        this.basicSalary = basicSalary;
    }

}
