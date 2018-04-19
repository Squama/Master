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
 * 类说明  出库单明细表
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
@Table(name = "tbl_outstock_det")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class OutstockDet {

    private static final long serialVersionUID = 3309603144734703924L;
    
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "id", length = 64)
    protected String id;
    
  @Header(name = "所属出库单id")
  @Column(name = "outstockId")
  private String outstockId;

  @Header(name = "库存id")
  @Column(name = "stockId")
  private String stockId;
  
  @Header(name = "库存渠道id")
  @Column(name = "sChannelId")
  private String sChannelId;
  
  @Header(name = "物料编号")
  @Column(name = "matNumber")
  private String matNumber;

  @Header(name = "物料名称")
  @Column(name = "matName")
  private String matName;

  @Header(name = "物料规格")
  @Column(name = "matStandard")
  private String matStandard;
  
  @Header(name = "物料单位")
  @Column(name = "unit")
  private String unit;
  
  @Header(name = "出库量")
  @Column(name = "ckl")
  private String ckl;
  
  @Header(name = "价格")
  @Column(name = "price")
  private String price;
  
  @Header(name = "备注")
  @Column(name = "remark")
  private String remark;

  
public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getOutstockId() {
	return outstockId;
}

public void setOutstockId(String outstockId) {
	this.outstockId = outstockId;
}

public String getStockId() {
	return stockId;
}

public void setStockId(String stockId) {
	this.stockId = stockId;
}

public String getsChannelId() {
	return sChannelId;
}

public void setsChannelId(String sChannelId) {
	this.sChannelId = sChannelId;
}

public String getMatNumber() {
	return matNumber;
}

public void setMatNumber(String matNumber) {
	this.matNumber = matNumber;
}

public String getMatName() {
	return matName;
}

public void setMatName(String matName) {
	this.matName = matName;
}

public String getMatStandard() {
	return matStandard;
}

public void setMatStandard(String matStandard) {
	this.matStandard = matStandard;
}

public String getUnit() {
	return unit;
}

public void setUnit(String unit) {
	this.unit = unit;
}

public String getCkl() {
	return ckl;
}

public void setCkl(String ckl) {
	this.ckl = ckl;
}

public String getPrice() {
	return price;
}

public void setPrice(String price) {
	this.price = price;
}

public String getRemark() {
	return remark;
}

public void setRemark(String remark) {
	this.remark = remark;
}

  
}
