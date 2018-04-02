/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.entity;

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
* dongyan      2017年12月1日    Create this file
* </pre>
* 
*/
@Entity
@Table(name="tbl_budget_estimate")
public class BudgetEstimate extends BaseEntity {

    private static final long serialVersionUID = -2888885202838810412L;

    @Header(name = "预算明细编号")
    @Column(name = "budget_tx_id") 
    private String budgetTxID;
    
    @Header(name = "项目编号")
    @Column(name = "project_id") 
    private String projectID;
    
    @Header(name = "预算编号")
    @Column(name = "budget_no") 
    private String budgetNo;
    
    @Header(name = "物料编号")
    @Column(name = "mat_number")  
    private String matNumber;

    @Header(name = "物料名称")
    @Column(name = "mat_name")
    private String matName;

    @Header(name = "物料规格")
    @Column(name = "mat_standard")
    private String matStandard;

    @Header(name = "单位")
    @Column(name = "unit")
    private String unit;

    @Header(name = "数量")
    @Column(name = "quantity")
    private String quantity;
    
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
