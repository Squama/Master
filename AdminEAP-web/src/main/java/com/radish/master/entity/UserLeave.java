package com.radish.master.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tbl_userLeave")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class UserLeave extends BaseEntity {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Header(name = "User表的Id")
    @Column(name = "userId")
    private String userId;
    
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
    
    @Header(name = "部门名称")
    @Column(name = "deptName")
    private String deptName;
    
    @Header(name = "工号")
    @Column(name = "jobNumber")
    private String jobNumber;
    
    @Header(name = "年龄")
    @Column(name = "age")
    private String age;
    
    @Header(name = "政治面貌")
    @Column(name = "political")
    private String political;
    
    @Header(name = "职称")
    @Column(name = "someTitle")
    private String someTitle;
    
    @Header(name = "毕业学院")
    @Column(name = "school")
    private String school;
    
    @Header(name = "专业")
    @Column(name = "professional")
    private String professional;
    
    @Header(name = "血型")
    @Column(name = "bloodType")
    private String bloodType;
    
    @Header(name = "银行账户")
    @Column(name = "bankCount")
    private String bankCount;
    
    @Header(name = "各类文件Id")
    @Column(name = "fileId")
    private String fileId;
    
    @Header(name = "入职时间")
    @Column(name = "hireDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date hireDate;
    
    @Header(name = "转正状态")
    @Column(name = "zzStatus")
    private String zzStatus;
    
    @Header(name = "离职附件id")
    @Column(name = "leaveFileId")
    private String leaveFileId;
    
    @Header(name = "离职时间")
    @Column(name = "leaveTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date leaveTime;
    
    @Header(name = "离职原因")
    @Column(name = "leaveReason")
    private String leaveReason;
    
    @Header(name = "离职意见")
    @Column(name = "leaveView")
    private String leaveView;
    
    @Header(name = "离职审核状态")
    @Column(name = "leaveStatus")
    private String leaveStatus;

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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPolitical() {
		return political;
	}

	public void setPolitical(String political) {
		this.political = political;
	}

	public String getSomeTitle() {
		return someTitle;
	}

	public void setSomeTitle(String someTitle) {
		this.someTitle = someTitle;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getBankCount() {
		return bankCount;
	}

	public void setBankCount(String bankCount) {
		this.bankCount = bankCount;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public String getZzStatus() {
		return zzStatus;
	}

	public void setZzStatus(String zzStatus) {
		this.zzStatus = zzStatus;
	}

	public String getLeaveFileId() {
		return leaveFileId;
	}

	public void setLeaveFileId(String leaveFileId) {
		this.leaveFileId = leaveFileId;
	}

	public Date getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(Date leaveTime) {
		this.leaveTime = leaveTime;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public String getLeaveView() {
		return leaveView;
	}

	public void setLeaveView(String leaveView) {
		this.leaveView = leaveView;
	}

	public String getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
