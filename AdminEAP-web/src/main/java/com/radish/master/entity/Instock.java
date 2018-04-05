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
@Table(name = "tbl_instock")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Instock {

    private static final long serialVersionUID = 3309603144734703924L;
    
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "id", length = 64)
    protected String id;
    
  @Header(name = "所属项目id")
  @Column(name = "projectId")
  private String projectId;

  @Header(name = "采购单id")
  @Column(name = "purchaseId")
  private String purchaseId;
  
  @Header(name = "入库单名称")
  @Column(name = "instock_name")
  private String instock_name;

  @Header(name = "入库单编号")
  @Column(name = "number")
  private String number;

  @Header(name = "10-新建(待核对)\r\n20-已核对(待入库)\r\n30-已入库")
  @Column(name = "status")
  private String status;
  
  @Header(name = "创建日期")
  @Column(name = "create_date_time")
  private Date createDate;
  
  @Header(name = "修改日期")
  @Column(name = "update_date_time")
  private Date updateDate;
  
  @Header(name = "入库日期")
  @Column(name = "indate")
  private Date indate;

  @Header(name = "交货人id")
  @Column(name = "buyer_id")
  private String buyer_id;

  @Header(name = "验收人id")
  @Column(name = "checker_id")
  private String checker_id;
  
  @Header(name = "入库人id")
  @Column(name = "iner_id")
  private String iner_id;
  
  @Header(name = "方便序列取值")
  @Column(name = "xh")
  private Integer xh;

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

public String getPurchaseId() {
	return purchaseId;
}

public void setPurchaseId(String purchaseId) {
	this.purchaseId = purchaseId;
}

public String getInstock_name() {
	return instock_name;
}

public void setInstock_name(String instock_name) {
	this.instock_name = instock_name;
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

public Date getIndate() {
	return indate;
}

public void setIndate(Date indate) {
	this.indate = indate;
}

public String getBuyer_id() {
	return buyer_id;
}

public void setBuyer_id(String buyer_id) {
	this.buyer_id = buyer_id;
}

public String getChecker_id() {
	return checker_id;
}

public void setChecker_id(String checker_id) {
	this.checker_id = checker_id;
}

public String getIner_id() {
	return iner_id;
}

public void setIner_id(String iner_id) {
	this.iner_id = iner_id;
}

public Integer getXh() {
	return xh;
}

public void setXh(Integer xh) {
	this.xh = xh;
}
  
  
}
