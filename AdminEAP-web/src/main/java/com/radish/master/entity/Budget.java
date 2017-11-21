package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_budget")
public class Budget extends BaseEntity {

    private static final long serialVersionUID = 5201916964951209848L;

    @Header(name="预算编码")
    @Column(name="budget_no")
    private String budgetNo;

    @Header(name="预算信息名称")
    @Column(name="budget_name")
    private String budgetName;

    @Header(name="所属项目")
    @Column(name="project_id")
    private String projectID;

    @Header(name="预算制单人员")
    @Column(name="operator")
    private String operator;

    @Header(name="制单时间")
    @Column(name="operate_time")
    private Date operateTime;

    @Header(name="审核人员")
    @Column(name="checker")
    private String checker;

    @Header(name="审核时间")
    @Column(name="check_time")
    private Date checkTime;

    @Header(name="供应渠道编辑人")
    @Column(name="supplier_editer")
    private String supplierEditer;

    @Header(name="编辑时间")
    @Column(name="edit_time")
    private Date editTime;

    @Header(name="状态")
    @Column(name="status")
    private String status;

    public String getBudgetNo() {
        return budgetNo;
    }

    public void setBudgetNo(String budgetNo) {
        this.budgetNo = budgetNo;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getSupplierEditer() {
        return supplierEditer;
    }

    public void setSupplierEditer(String supplierEditer) {
        this.supplierEditer = supplierEditer;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
