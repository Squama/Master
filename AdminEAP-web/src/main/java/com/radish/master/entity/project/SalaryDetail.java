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
	private String medical_corp;

	@Header(name = "养老个人缴纳")
	@Column(name = "yanglao")
	private String yanglao;

	@Header(name = "养老公司缴纳")
	@Column(name = "yanglao_corp")
	private String yanglao_corp;

	@Header(name = "失业个人缴纳")
	@Column(name = "shiye")
	private String shiye;

	@Header(name = "失业公司缴纳")
	@Column(name = "shiye_corp")
	private String shiye_corp;

	@Header(name = "工伤公司缴纳")
	@Column(name = "gongshang_corp")
	private String gongshang_corp;

	@Header(name = "生育公司缴纳")
	@Column(name = "shengyu_corp")
	private String shengyu_corp;

	@Header(name = "公积金公司缴纳")
	@Column(name = "prf_corp")
	private String prf_corp;

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

	public String getMedical_corp() {
		return medical_corp;
	}

	public void setMedical_corp(String medical_corp) {
		this.medical_corp = medical_corp;
	}

	public String getYanglao() {
		return yanglao;
	}

	public void setYanglao(String yanglao) {
		this.yanglao = yanglao;
	}

	public String getYanglao_corp() {
		return yanglao_corp;
	}

	public void setYanglao_corp(String yanglao_corp) {
		this.yanglao_corp = yanglao_corp;
	}

	public String getShiye() {
		return shiye;
	}

	public void setShiye(String shiye) {
		this.shiye = shiye;
	}

	public String getShiye_corp() {
		return shiye_corp;
	}

	public void setShiye_corp(String shiye_corp) {
		this.shiye_corp = shiye_corp;
	}

	public String getGongshang_corp() {
		return gongshang_corp;
	}

	public void setGongshang_corp(String gongshang_corp) {
		this.gongshang_corp = gongshang_corp;
	}

	public String getShengyu_corp() {
		return shengyu_corp;
	}

	public void setShengyu_corp(String shengyu_corp) {
		this.shengyu_corp = shengyu_corp;
	}

	public String getPrf_corp() {
		return prf_corp;
	}

	public void setPrf_corp(String prf_corp) {
		this.prf_corp = prf_corp;
	}

}
