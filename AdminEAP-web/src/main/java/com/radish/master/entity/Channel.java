/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity;


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
 * 类说明
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
@Table(name = "tbl_channel")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Channel {

    private static final long serialVersionUID = 3309603144734703924L;
    
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "id", length = 36)
    protected String id;
    
  @Header(name = "所属物料内码")
  @Column(name = "mat_ID")
  private String mat_ID;

  @Header(name = "物料名称")
  @Column(name = "mat_name")
  private String mat_name;
  
  @Header(name = "有效标志")
  @Column(name = "isValid")
  private String isValid;

  @Header(name = "供应商")
  @Column(name = "supplier")
  private String supplier;

  @Header(name = "单价")
  @Column(name = "price")
  private Double price;
  
  @Header(name = "所在地")
  @Column(name = "area")
  private String area;
  
  public String getArea() {
	return area;
}

public void setArea(String area) {
	this.area = area;
}

@Header(name = "创建人姓名")
  @Column(name = "create_name")
  private String create_name;

  @Header(name = "创建人ID")
  @Column(name = "create_name_id")
  private String create_name_id;

  @Header(name = "创建时间")
  @Column(name = "create_time")
  private Date create_time;

  @Header(name = "更新人姓名")
  @Column(name = "update_name")
  private String update_name;

  @Header(name = "更新人ID")
  @Column(name = "update_name_id")
  private String update_name_id;

  @Header(name = "更新时间")
  @Column(name = "update_time")
  private Date update_time;

  @Header(name = "预留字段1")
  @Column(name = "reserve1")
  private String reserve1;

  @Header(name = "预留字段2")
  @Column(name = "reserve2")
  private String reserve2;

  @Header(name = "预留字段3")
  @Column(name = "reserve3")
  private String reserve3;

  public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
  public String getMat_ID() {
	return mat_ID;
}

public void setMat_ID(String mat_ID) {
	this.mat_ID = mat_ID;
}

public String getMat_name() {
	return mat_name;
}

public void setMat_name(String mat_name) {
	this.mat_name = mat_name;
}



  public String getIsValid() {
	return isValid;
}

public void setIsValid(String isValid) {
	this.isValid = isValid;
}

public String getSupplier() {
    return supplier;
  }

  public void setSupplier(String supplier) {
    this.supplier = supplier;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getCreate_name() {
    return create_name;
  }

  public void setCreate_name(String create_name) {
    this.create_name = create_name;
  }

  public String getCreate_name_id() {
    return create_name_id;
  }

  public void setCreate_name_id(String create_name_id) {
    this.create_name_id = create_name_id;
  }

  public Date getCreate_time() {
    return create_time;
  }

  public void setCreate_time(Date create_time) {
    this.create_time = create_time;
  }

  public String getUpdate_name() {
    return update_name;
  }

  public void setUpdate_name(String update_name) {
    this.update_name = update_name;
  }

  public String getUpdate_name_id() {
    return update_name_id;
  }

  public void setUpdate_name_id(String update_name_id) {
    this.update_name_id = update_name_id;
  }

  public Date getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(Date update_time) {
    this.update_time = update_time;
  }

  public String getReserve1() {
    return reserve1;
  }

  public void setReserve1(String reserve1) {
    this.reserve1 = reserve1;
  }

  public String getReserve2() {
    return reserve2;
  }

  public void setReserve2(String reserve2) {
    this.reserve2 = reserve2;
  }

  public String getReserve3() {
    return reserve3;
  }

  public void setReserve3(String reserve3) {
    this.reserve3 = reserve3;
  }
}
