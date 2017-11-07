package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_budget")
public class Tbl_budget extends BaseEntity {

    private static final Long serialVersionUID = 8933803158142753625L;

    @Header(name = "预算ID")
    @Column(name = "budget_id")
    private String budget_id;

    @Header(name = "预算清单名")
    @Column(name = "budget_name")
    private String budget_name;

    @Header(name = "所属项目ID")
    @Column(name = "project_id")
    private String project_id;

    @Header(name = "预算人员ID")
    @Column(name = "budget_people_id")
    private String budget_people_id;

    @Header(name = "审核人ID")
    @Column(name = "approver_id")
    private String approver_id;

    @Header(name = "审核标志")
    @Column(name = "isApprover")
    private Integer isApprover;

    @Header(name = "编辑人ID")
    @Column(name = "edit_people_id")
    private String edit_people_id;

    @Header(name = "编辑标志")
    @Column(name = "isEdit")
    private Integer isEdit;

    @Header(name = "预留字段1")
    @Column(name = "reserve1")
    private String reserve1;

    @Header(name = "预留字段1")
    @Column(name = "reserve2")
    private String reserve2;

    @Header(name = "预留字段1")
    @Column(name = "reserve3")
    private String reserve3;


    public String getBudget_id() {
        return budget_id;
    }

    public void setBudget_id(String budget_id) {
        this.budget_id = budget_id;
    }

    public String getBudget_name() {
        return budget_name;
    }

    public void setBudget_name(String budget_name) {
        this.budget_name = budget_name;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getBudget_people_id() {
        return budget_people_id;
    }

    public void setBudget_people_id(String budget_people_id) {
        this.budget_people_id = budget_people_id;
    }

    public String getApprover_id() {
        return approver_id;
    }

    public void setApprover_id(String approver_id) {
        this.approver_id = approver_id;
    }

    public Integer getisApprover() {
        return isApprover;
    }

    public void setisApprover(Integer isApprover) {
        this.isApprover = isApprover;
    }

    public String getEdit_people_id() {
        return edit_people_id;
    }

    public void setEdit_people_id(String edit_people_id) {
        this.edit_people_id = edit_people_id;
    }

    public Integer getisEdit() {
        return isEdit;
    }

    public void setisEdit(Integer isEdit) {
        this.isEdit = isEdit;
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
