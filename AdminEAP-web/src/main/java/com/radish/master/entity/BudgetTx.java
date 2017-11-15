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
* dongyan      2017年11月14日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name="tbl_budget_tx")
public class BudgetTx extends BaseEntity {

    private static final long serialVersionUID = -2958304921341682100L;

    @Header(name = "预算编码")
    @Column(name = "budget_no")
    private String budgetNo;

    @Header(name = "所属项目")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "部位")
    @Column(name = "region_id")
    private String regionID;

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

    public String getBudgetNo() {
        return budgetNo;
    }

    public void setBudgetNo(String budgetNo) {
        this.budgetNo = budgetNo;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getRegionID() {
        return regionID;
    }

    public void setRegionID(String regionID) {
        this.regionID = regionID;
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
