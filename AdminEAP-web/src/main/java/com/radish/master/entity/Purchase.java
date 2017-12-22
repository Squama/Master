/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.entity;

import java.util.Date;

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
* dongyan      2017年12月22日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_purchase")
public class Purchase extends BaseEntity {

    private static final long serialVersionUID = -8751095541988265367L;

    @Header(name = "项目编号")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "预算编号")
    @Column(name = "budget_no")
    private String budgetNo;

    @Header(name = "申请金额")
    @Column(name = "apply_amount")
    private String applyAmount;

    @Header(name = "已采购金额")
    @Column(name = "purchase_amount")
    private String purchaseAmount;

    @Header(name = "申请人")
    @Column(name = "operator")
    private String operator;

    @Header(name = "申请时间")
    @Column(name = "operate_time")
    private Date operateTime;

    @Header(name = "渠道编辑人")
    @Column(name = "channelor")
    private String channelor;

    @Header(name = "编辑时间")
    @Column(name = "channel_time")
    private Date channelTime;

    @Header(name = "审核人")
    @Column(name = "checker")
    private String checker;

    @Header(name = "审核时间")
    @Column(name = "check_time")
    private Date checkTime;

    /**
     * 10-新建 20-待编辑渠道 30-待审核 40-待采购 50-部分入库 60-已完成
     */
    @Header(name = "状态")
    @Column(name = "status")
    private String status;

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getBudgetNo() {
        return budgetNo;
    }

    public void setBudgetNo(String budgetNo) {
        this.budgetNo = budgetNo;
    }

    public String getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(String applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(String purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
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

    public String getChannelor() {
        return channelor;
    }

    public void setChannelor(String channelor) {
        this.channelor = channelor;
    }

    public Date getChannelTime() {
        return channelTime;
    }

    public void setChannelTime(Date channelTime) {
        this.channelTime = channelTime;
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
