/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity;


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
 * 类说明  出库单表
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
@Table(name = "tbl_outstock")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Outstock {

    private static final long serialVersionUID = 3309603144734703924L;
    
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "id", length = 64)
    protected String id;
    
  @Header(name = "所属项目id")
  @Column(name = "projectId")
  private String projectId;
  
  @Header(name = "申请班组id")
  @Column(name = "teamCode")
  private String teamCode;

  @Header(name = "出库单名称")
  @Column(name = "outstock_name")
  private String outstock_name;

  @Header(name = "出库单编号")
  @Column(name = "number")
  private String number;

  @Header(name = "10-新建(待出库)\r\n20-已出库")
  @Column(name = "status")
  private String status;
  
  @Header(name = "创建日期")
  @Column(name = "create_date_time")
  private Date createDate;
  
  @Header(name = "修改日期")
  @Column(name = "update_date_time")
  private Date updateDate;
  
  @Header(name = "出库日期")
  @Column(name = "outdate")
  private Date outdate;

  @Header(name = "负责人id")
  @Column(name = "fz_id")
  private String fz_id;

  @Header(name = "库管员id")
  @Column(name = "kg_id")
  private String kg_id;
  
  @Header(name = "领用人id")
  @Column(name = "sl_id")
  private String sl_id;
  
  @Header(name = "承包人id")
  @Column(name = "cb_id")
  private String cb_id;
  
  @Header(name = "承包人姓名")
  @Column(name = "cbName")
  private String cbName;
  
  @Header(name = "方便序列取值")
  @Column(name = "xh")
  private Integer xh;
  
  @Header(name = "预算使用部位")
  @Column(name = "sybw")
  private String sybw;
  
  @Header(name = "预算明细id")
  @Column(name = "budgetTxId")
  private String budgetTxId;

  @Header(name = "创建人id")
  @Column(name = "creatId")
  private String creatId;
  
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


public String getTeamCode() {
	return teamCode;
}

public void setTeamCode(String teamCode) {
	this.teamCode = teamCode;
}

public String getOutstock_name() {
	return outstock_name;
}

public void setOutstock_name(String outstock_name) {
	this.outstock_name = outstock_name;
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

public Date getUpdateDate() {
	return updateDate;
}

public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
}

public Date getOutdate() {
	return outdate;
}

public void setOutdate(Date outdate) {
	this.outdate = outdate;
}

public String getFz_id() {
	return fz_id;
}

public void setFz_id(String fz_id) {
	this.fz_id = fz_id;
}

public String getKg_id() {
	return kg_id;
}

public void setKg_id(String kg_id) {
	this.kg_id = kg_id;
}

public String getSl_id() {
	return sl_id;
}

public void setSl_id(String sl_id) {
	this.sl_id = sl_id;
}

public String getCb_id() {
	return cb_id;
}

public void setCb_id(String cb_id) {
	this.cb_id = cb_id;
}

public Integer getXh() {
	return xh;
}

public void setXh(Integer xh) {
	this.xh = xh;
}

public String getSybw() {
	return sybw;
}

public void setSybw(String sybw) {
	this.sybw = sybw;
}

public String getBudgetTxId() {
	return budgetTxId;
}

public void setBudgetTxId(String budgetTxId) {
	this.budgetTxId = budgetTxId;
}

public String getCreatId() {
	return creatId;
}

public void setCreatId(String creatId) {
	this.creatId = creatId;
}

public String getCbName() {
	return cbName;
}

public void setCbName(String cbName) {
	this.cbName = cbName;
}
  
  
}
