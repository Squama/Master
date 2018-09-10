/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "tbl_projectAccount_det")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class ProAccountDet implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;

    
    @Header(name = "所属项目帐id")
    @Column(name = "projectAccountId")
    private String projectAccountId;
    
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "账目时间")
    @Column(name = "createDate")
    private Date createDate;
    
    
    @Header(name = "摘要")
    @Column(name = "abstracts")
    private String abstracts;
    
    @Header(name = "账目类型")
    @Column(name = "zmtype")
    private String zmtype;//1收入、2支出
    
    @Header(name = "收入")
    @Column(name = "inMoney")
    private String inMoney;
    
    @Header(name = "支出")
    @Column(name = "outMoney")
    private String outMoney;
    
    @Header(name = "项目当前总额")
    @Column(name = "nowMoney")
    private String nowMoney;
    
    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;

    @Header(name = "审核员id")
    @Column(name = "auditId")
    private String auditId;
    
    @Header(name = "审核员姓名")
    @Column(name = "auditName")
    private String auditName;
    
    @Header(name = "记账员id")
    @Column(name = "accounterId")
    private String accounterId;
    
    @Header(name = "记账员姓名")
    @Column(name = "accounter")
    private String accounter;
    
    @Header(name = "创建人id")
    @Column(name = "createId")
    private String createId;
    
    @Header(name = "备用审核状态")
    @Column(name = "status")
    private String status;
    
    @Header(name = "审核意见")
    @Column(name = "descs")
    private String descs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectAccountId() {
		return projectAccountId;
	}

	public void setProjectAccountId(String projectAccountId) {
		this.projectAccountId = projectAccountId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getZmtype() {
		return zmtype;
	}

	public void setZmtype(String zmtype) {
		this.zmtype = zmtype;
	}

	public String getInMoney() {
		return inMoney;
	}

	public void setInMoney(String inMoney) {
		this.inMoney = inMoney;
	}

	public String getOutMoney() {
		return outMoney;
	}

	public void setOutMoney(String outMoney) {
		this.outMoney = outMoney;
	}

	public String getNowMoney() {
		return nowMoney;
	}

	public void setNowMoney(String nowMoney) {
		this.nowMoney = nowMoney;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public String getAccounterId() {
		return accounterId;
	}

	public void setAccounterId(String accounterId) {
		this.accounterId = accounterId;
	}

	public String getAccounter() {
		return accounter;
	}

	public void setAccounter(String accounter) {
		this.accounter = accounter;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}
    
	
}
