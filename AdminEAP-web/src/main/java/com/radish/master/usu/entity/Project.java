package com.radish.master.usu.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

@Entity
@Table(name = "tbl_project")
public class Project extends BaseEntity {


  private static final Long serialVersionUID = 4876847951849490118L;

  @Header(name = "项目名")
  @Column(name = "project_name")
  private String project_name;

  @Header(name = "项目级别")
  @Column(name = "project_level")
  private int project_level;

  @Header(name = "负责人员工编号")
  @Column(name = "charge_id")
  private String charge_id;

  @Header(name = "负责人员姓名")
  @Column(name = "charge_name")
  private String charge_name;

  @Header(name = "项目状态")
  @Column(name = "status")
  private int status;


  public String getProject_name() {
    return project_name;
  }

  public void setProject_name(String project_name) {
    this.project_name = project_name;
  }

  public int getProject_level() {
    return project_level;
  }

  public void setProject_level(int project_level) {
    this.project_level = project_level;
  }

  public String getCharge_id() {
    return charge_id;
  }

  public void setCharge_id(String charge_id) {
    this.charge_id = charge_id;
  }

  public String getCharge_name() {
    return charge_name;
  }

  public void setCharge_name(String charge_name) {
    this.charge_name = charge_name;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
