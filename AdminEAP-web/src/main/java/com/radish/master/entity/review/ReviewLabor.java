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
@Table(name = "tbl_reviewLabor")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class ReviewLabor{

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

  @Header(name = "10新建/20综合评审/30副经理评审/40总经理评审/50经营科总结/60完成")
  @Column(name = "status")
  private String status;
  
  @Header(name = "创建日期")
  @Column(name = "create_date_time")
  private Date createDate;
  
  @Header(name = "参与合同谈判人")
  @Column(name = "reviewName")
  private String reviewName;

  @Header(name = "起草人")
  @Column(name = "creatName")
  private String creatName;

  @Header(name = "建设单位")
  @Column(name = "builder")
  private String builder;
  
  @Header(name = "承包单位")
  @Column(name = "contracter")
  private String contracter;
  
  @Header(name = "垫资额度要求")
  @Column(name = "dzedyq")
  private String dzedyq;
  
  @Header(name = "工程质量目标要求")
  @Column(name = "zlyq")
  private String zlyq;
  
  @Header(name = "返还时间节点")
  @Column(name = "fhjd")
  private String fhjd;
  
  @Header(name = "甲供材")
  @Column(name = "jgc")
  private String jgc;
  
  @Header(name = "付款方式")
  @Column(name = "payWay")
  private String payWay;
  
  @Header(name = "付款时间节点")
  @Column(name = "payTime")
  private String payTime;
  
  @Header(name = "配合费")
  @Column(name = "phMoney")
  private String phMoney;
  
  @Header(name = "定案时间节点")
  @Column(name = "dajd")
  private String dajd;
  
  @Header(name = "解决途径")
  @Column(name = "jjtj")
  private String jjtj;
  
  @Header(name = "发包方应给")
  @Column(name = "getToJf")
  private String getToJf;
  
  @Header(name = "税务要求")
  @Column(name = "swyq")
  private String swyq;
  
  
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
  
  @Header(name = "合同编号")
  @Column(name = "htNumber")
  private String htNumber;

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getProjectId() {
	return projectId;
}

public void setProjectId(String projectId) {
	this.projectId = projectId;
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

public String getReviewName() {
	return reviewName;
}

public void setReviewName(String reviewName) {
	this.reviewName = reviewName;
}

public String getCreatName() {
	return creatName;
}

public void setCreatName(String creatName) {
	this.creatName = creatName;
}

public String getBuilder() {
	return builder;
}

public void setBuilder(String builder) {
	this.builder = builder;
}

public String getContracter() {
	return contracter;
}

public void setContracter(String contracter) {
	this.contracter = contracter;
}

public String getDzedyq() {
	return dzedyq;
}

public void setDzedyq(String dzedyq) {
	this.dzedyq = dzedyq;
}

public String getZlyq() {
	return zlyq;
}

public void setZlyq(String zlyq) {
	this.zlyq = zlyq;
}

public String getFhjd() {
	return fhjd;
}

public void setFhjd(String fhjd) {
	this.fhjd = fhjd;
}

public String getJgc() {
	return jgc;
}

public void setJgc(String jgc) {
	this.jgc = jgc;
}

public String getPayWay() {
	return payWay;
}

public void setPayWay(String payWay) {
	this.payWay = payWay;
}

public String getPayTime() {
	return payTime;
}

public void setPayTime(String payTime) {
	this.payTime = payTime;
}

public String getPhMoney() {
	return phMoney;
}

public void setPhMoney(String phMoney) {
	this.phMoney = phMoney;
}

public String getDajd() {
	return dajd;
}

public void setDajd(String dajd) {
	this.dajd = dajd;
}

public String getJjtj() {
	return jjtj;
}

public void setJjtj(String jjtj) {
	this.jjtj = jjtj;
}

public String getGetToJf() {
	return getToJf;
}

public void setGetToJf(String getToJf) {
	this.getToJf = getToJf;
}

public String getSwyq() {
	return swyq;
}

public void setSwyq(String swyq) {
	this.swyq = swyq;
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

public String getHtNumber() {
	return htNumber;
}

public void setHtNumber(String htNumber) {
	this.htNumber = htNumber;
}

  
}
