/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity.review;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类说明  入库单表
 *
 * <pre>
 * Modify Information:
 * Author        Date          Description
 * ============ =========== ============================
 * zhouqb      2017年10月28日    Create this file
 * </pre>
 *
 */
@Entity
@Table(name = "tbl_reviewBid")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class ReviewBid{

    private static final long serialVersionUID = 3309603144734703924L;
    
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "id", length = 64)
    protected String id;
    @Header(name = "所属项目id")
    @Column(name = "projectId")
    private String projectId;
    
  @Header(name = "项目名称")
  @Column(name = "projectName")
  private String projectName;

  @Header(name = "编号")
  @Column(name = "number")
  private String number;
  
  @Header(name = "招标文件编号")
  @Column(name = "bidNumber")
  private String bidNumber;

  @Header(name = "10新建/20综合评审/30副经理评审/40总经理评审/50经营科总结/60完成")
  @Column(name = "status")
  private String status;
  
  @Header(name = "创建日期")
  @Column(name = "create_date_time")
  private Date createDate;
  
  @Header(name = "招标方式")
  @Column(name = "bidWay")
  private String bidWay;
  
  @Header(name = "建设单位")
  @Column(name = "builder")
  private String builder;
  
  @Header(name = "招标单位")
  @Column(name = "bider")
  private String bider;
  
  @Header(name = "资金来源")
  @Column(name = "moneySource")
  private String moneySource;
  
  @Header(name = "工程地点")
  @Column(name = "buildAddress")
  private String buildAddress;
  
  @Header(name = "工程规模")
  @Column(name = "buildSize")
  private String buildSize;
  
  @Header(name = "建设周期")
  @Column(name = "buildTime")
  private String buildTime;
  
  @Header(name = "结论")
  @Column(name = "conclusion")
  private String conclusion;
  
  @Header(name = "风险")
  @Column(name = "risk")
  private String risk;
  
  @Header(name = "事后处理")
  @Column(name = "manage")
  private String manage;
  
  @Header(name = "参与人员")
  @Column(name = "joinName")
  private String joinName;

  @Header(name = "备注")
  @Column(name = "remark")
  private String remark;

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getProjectName() {
	return projectName;
}

public void setProjectName(String projectName) {
	this.projectName = projectName;
}

public String getNumber() {
	return number;
}

public void setNumber(String number) {
	this.number = number;
}

public String getBidNumber() {
	return bidNumber;
}

public void setBidNumber(String bidNumber) {
	this.bidNumber = bidNumber;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public Date getCreateDate() {
	return createDate;
}

public void setCreateDate(Date createDate) {
	this.createDate = createDate;
}

public String getBidWay() {
	return bidWay;
}

public void setBidWay(String bidWay) {
	this.bidWay = bidWay;
}

public String getBuilder() {
	return builder;
}

public void setBuilder(String builder) {
	this.builder = builder;
}

public String getBider() {
	return bider;
}

public void setBider(String bider) {
	this.bider = bider;
}

public String getMoneySource() {
	return moneySource;
}

public void setMoneySource(String moneySource) {
	this.moneySource = moneySource;
}

public String getBuildAddress() {
	return buildAddress;
}

public void setBuildAddress(String buildAddress) {
	this.buildAddress = buildAddress;
}

public String getBuildSize() {
	return buildSize;
}

public void setBuildSize(String buildSize) {
	this.buildSize = buildSize;
}

public String getBuildTime() {
	return buildTime;
}

public void setBuildTime(String buildTime) {
	this.buildTime = buildTime;
}

public String getConclusion() {
	return conclusion;
}

public void setConclusion(String conclusion) {
	this.conclusion = conclusion;
}

public String getRisk() {
	return risk;
}

public void setRisk(String risk) {
	this.risk = risk;
}

public String getManage() {
	return manage;
}

public void setManage(String manage) {
	this.manage = manage;
}

public String getJoinName() {
	return joinName;
}

public void setJoinName(String joinName) {
	this.joinName = joinName;
}

public String getRemark() {
	return remark;
}

public void setRemark(String remark) {
	this.remark = remark;
}

public String getProjectId() {
	return projectId;
}

public void setProjectId(String projectId) {
	this.projectId = projectId;
}

}
