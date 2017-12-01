package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

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
    
    @Header(name="预算总价")
    @Column(name="budget_amount")
    private String budgetAmount;
    
    @Header(name="消耗总价")
    @Column(name="consume_amount")
    private String consumeAmount;

    @Header(name="预算导入人员")
    @Column(name="operator")
    private String operator;

    @Header(name="导入时间")
    @Column(name="operate_time")
    private Date operateTime;

    @Header(name="测算提交人员")
    @Column(name="estimater")
    private String estimater;
    
    @Header(name="测算提交时间")
    @Column(name="estimate_time")
    private String estimateTime;
    
    @Header(name="测算审核人员")
    @Column(name="checker")
    private String checker;

    @Header(name="测算审核时间")
    @Column(name="check_time")
    private Date checkTime;

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

    public String getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(String budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public String getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(String consumeAmount) {
        this.consumeAmount = consumeAmount;
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

    public String getEstimater() {
        return estimater;
    }

    public void setEstimater(String estimater) {
        this.estimater = estimater;
    }

    public String getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(String estimateTime) {
        this.estimateTime = estimateTime;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
