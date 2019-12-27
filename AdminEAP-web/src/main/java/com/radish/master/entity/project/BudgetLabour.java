/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.entity.project;

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
* dongyan      2018年5月25日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_budget_labour")
public class BudgetLabour extends BaseEntity {

    private static final long serialVersionUID = -3714495444403163102L;

    @Header(name = "预算明细编号")
    @Column(name = "budget_tx_id")
    private String budgetTxID;

    @Header(name = "项目编号")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "预算编号")
    @Column(name = "budget_no")
    private String budgetNo;

    @Header(name = "人工费名称")
    @Column(name = "labour_name")
    private String labourName;

    @Header(name = "市场估计价")
    @Column(name = "forecast_price")
    private String forecastPrice;

    @Header(name = "消耗量")
    @Column(name = "labour_quantity")
    private String labourQuantity;

    @Header(name = "类型")
    @Column(name = "type")
    private String type;

    @Header(name = "变更量")
    @Column(name = "addition_quantity")
    private String additionQuantity;

    public String getBudgetTxID() {
        return budgetTxID;
    }

    public void setBudgetTxID(String budgetTxID) {
        this.budgetTxID = budgetTxID;
    }

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

    public String getLabourName() {
        return labourName;
    }

    public void setLabourName(String labourName) {
        this.labourName = labourName;
    }

    public String getForecastPrice() {
        return forecastPrice;
    }

    public void setForecastPrice(String forecastPrice) {
        this.forecastPrice = forecastPrice;
    }

    public String getLabourQuantity() {
        return labourQuantity;
    }

    public void setLabourQuantity(String labourQuantity) {
        this.labourQuantity = labourQuantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdditionQuantity() {
        return additionQuantity;
    }

    public void setAdditionQuantity(String additionQuantity) {
        this.additionQuantity = additionQuantity;
    }

}
