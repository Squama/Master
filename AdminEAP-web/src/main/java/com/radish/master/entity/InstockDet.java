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
 * 类说明  入库单明细表
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
@Table(name = "tbl_instock_det")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class InstockDet {

    private static final long serialVersionUID = 3309603144734703924L;
    
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "id", length = 64)
    protected String id;
    
  @Header(name = "所属入库单id")
  @Column(name = "instockId")
  private String instockId;

  @Header(name = "采购明细id")
  @Column(name = "pDetId")
  private String pDetId;
  
  @Header(name = "采购渠道id")
  @Column(name = "channelId")
  private String channelId;
  
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
  
  @Header(name = "入库量")
  @Column(name = "rkl")
  private String rkl;
  
  @Header(name = "价格")
  @Column(name = "price")
  private String price;

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getInstockId() {
	return instockId;
}

public void setInstockId(String instockId) {
	this.instockId = instockId;
}

public String getpDetId() {
	return pDetId;
}

public void setpDetId(String pDetId) {
	this.pDetId = pDetId;
}

public String getChannelId() {
	return channelId;
}

public void setChannelId(String channelId) {
	this.channelId = channelId;
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

public String getRkl() {
	return rkl;
}

public void setRkl(String rkl) {
	this.rkl = rkl;
}

public String getPrice() {
	return price;
}

public void setPrice(String price) {
	this.price = price;
}

  
  
}
