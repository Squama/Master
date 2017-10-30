/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.material.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

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
@Table(name = "Purchase_history")
public class Purchase_history  extends BaseEntity{


  private static final long serialVersionUID = 3147202737225081323L;


  @Header(name = "物料编号")
  @Column(name = "mat_number")
  private String mat_number;

  @Header(name = "物料名称")
  @Column(name = "mat_name")
  private String mat_name;

  @Header(name = "物料供应商")
  @Column(name = "supplier")
  private String supplier;

  @Header(name = "单价")
  @Column(name = "price")
  private Double price;

  @Header(name = "采购人员姓名")
  @Column(name = "purchase_name")
  private String purchase_name;

  @Header(name = "采购人员ID")
  @Column(name = "purchase_name_id")
  private String purchase_name_id;

  @Header(name = "采购时间")
  @Column(name = "purchase_time")
  private java.sql.Date purchase_time;

  @Header(name = "所属项目")
  @Column(name = "project_name")
  private String project_name;

  @Header(name = "所属项目ID")
  @Column(name = "project_id")
  private String project_id;

  @Header(name = "预留字段1")
  @Column(name = "reserve1")
  private String reserve1;

  @Header(name = "预留字段2")
  @Column(name = "reserve2")
  private String reserve2;

  @Header(name = "预留字段3")
  @Column(name = "reserve3")
  private String reserve3;



  public String getMat_number() {
    return mat_number;
  }

  public void setMat_number(String mat_number) {
    this.mat_number = mat_number;
  }

  public String getMat_name() {
    return mat_name;
  }

  public void setMat_name(String mat_name) {
    this.mat_name = mat_name;
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

  public String getPurchase_name() {
    return purchase_name;
  }

  public void setPurchase_name(String purchase_name) {
    this.purchase_name = purchase_name;
  }

  public String getPurchase_name_id() {
    return purchase_name_id;
  }

  public void setPurchase_name_id(String purchase_name_id) {
    this.purchase_name_id = purchase_name_id;
  }

  public java.sql.Date getPurchase_time() {
    return purchase_time;
  }

  public void setPurchase_time(java.sql.Date purchase_time) {
    this.purchase_time = purchase_time;
  }

  public String getProject_name() {
    return project_name;
  }

  public void setProject_name(String project_name) {
    this.project_name = project_name;
  }

  public String getProject_id() {
    return project_id;
  }

  public void setProject_id(String project_id) {
    this.project_id = project_id;
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
