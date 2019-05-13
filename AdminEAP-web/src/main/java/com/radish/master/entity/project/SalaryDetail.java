/**
 * 
 */
package com.radish.master.entity.project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * @author tonyd
 *
 */
@Entity
@Table(name = "tbl_salary_detail")
public class SalaryDetail extends BaseEntity {

    private static final long serialVersionUID = -8491294641659609096L;

    @Header(name = "所属工资单")
    @Column(name = "salary_id")
    private String salaryID;

    @Header(name = "姓名")
    @Column(name = "user_id")
    private String userID;

    @Header(name = "姓名")
    @Column(name = "name")
    private String name;

    @Header(name = "手机")
    @Column(name = "mobile")
    private String mobile;

    @Header(name = "身份证号")
    @Column(name = "identification_number")
    private String identificationNumber;

    @Header(name = "工种")
    @Column(name = "work_type")
    private String workType;

    @Header(name = "基础工资")
    @Column(name = "basic_salary")
    private String basicSalary;

    @Header(name = "出勤天数")
    @Column(name = "attendance")
    private String attendance;

    @Header(name = "应付工资")
    @Column(name = "payable")
    private String payable;

    @Header(name = "借款")
    @Column(name = "loan")
    private String loan;

    @Header(name = "医保")
    @Column(name = "medical")
    private String medical;

    @Header(name = "社保")
    @Column(name = "social")
    private String social;

    @Header(name = "公积金")
    @Column(name = "prf")
    private String prf;

    @Header(name = "个税")
    @Column(name = "tax")
    private String tax;

    @Header(name = "实发工资")
    @Column(name = "actual")
    private String actual;

    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;

    @Header(name = "附加扣除项")
    @Column(name = "deduction")
    private String deduction;

    @Header(name = "医保公司缴纳")
    @Column(name = "medical_corp")
    private String medicalCorp;

    @Header(name = "养老个人缴纳")
    @Column(name = "yanglao")
    private String yanglao;

    @Header(name = "养老公司缴纳")
    @Column(name = "yanglao_corp")
    private String yanglaoCorp;

    @Header(name = "失业个人缴纳")
    @Column(name = "shiye")
    private String shiye;

    @Header(name = "失业公司缴纳")
    @Column(name = "shiye_corp")
    private String shiyeCorp;

    @Header(name = "工伤公司缴纳")
    @Column(name = "gongshang_corp")
    private String gongshangCorp;

    @Header(name = "生育公司缴纳")
    @Column(name = "shengyu_corp")
    private String shengyuCorp;

    @Header(name = "公积金公司缴纳")
    @Column(name = "prf_corp")
    private String prfCorp;

    @Header(name = "所属社保年份")
    @Column(name = "ss_year")
    private String ssYear;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSalaryID() {
        return salaryID;
    }

    public void setSalaryID(String salaryID) {
        this.salaryID = salaryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
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

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getPayable() {
        return payable;
    }

    public void setPayable(String payable) {
        this.payable = payable;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(String medical) {
        this.medical = medical;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getPrf() {
        return prf;
    }

    public void setPrf(String prf) {
        this.prf = prf;
    }

    public String getMedicalCorp() {
        return medicalCorp;
    }

    public void setMedicalCorp(String medicalCorp) {
        this.medicalCorp = medicalCorp;
    }

    public String getYanglao() {
        return yanglao;
    }

    public void setYanglao(String yanglao) {
        this.yanglao = yanglao;
    }

    public String getYanglaoCorp() {
        return yanglaoCorp;
    }

    public void setYanglaoCorp(String yanglaoCorp) {
        this.yanglaoCorp = yanglaoCorp;
    }

    public String getShiye() {
        return shiye;
    }

    public void setShiye(String shiye) {
        this.shiye = shiye;
    }

    public String getShiyeCorp() {
        return shiyeCorp;
    }

    public void setShiyeCorp(String shiyeCorp) {
        this.shiyeCorp = shiyeCorp;
    }

    public String getGongshangCorp() {
        return gongshangCorp;
    }

    public void setGongshangCorp(String gongshangCorp) {
        this.gongshangCorp = gongshangCorp;
    }

    public String getShengyuCorp() {
        return shengyuCorp;
    }

    public void setShengyuCorp(String shengyuCorp) {
        this.shengyuCorp = shengyuCorp;
    }

    public String getPrfCorp() {
        return prfCorp;
    }

    public void setPrfCorp(String prfCorp) {
        this.prfCorp = prfCorp;
    }

    public String getSsYear() {
        return ssYear;
    }

    public void setSsYear(String ssYear) {
        this.ssYear = ssYear;
    }

}
