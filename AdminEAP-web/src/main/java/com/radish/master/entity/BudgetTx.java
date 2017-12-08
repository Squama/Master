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

    @Header(name="部位编号")
    @Column(name="region_code")
    private String regionCode;
    
    @Header(name="部位名称")
    @Column(name="region_name")
    private String regionName;
    
    @Header(name="单位")
    @Column(name="units")
    private String units;
    
    @Header(name="工程量")
    @Column(name="quantities")
    private String quantities;
    
    @Header(name="单价")
    @Column(name="price")
    private String price;
    
    @Header(name="合价")
    @Column(name="unit_price")
    private String unitPrice;
    
    @Header(name="合价（人工）")
    @Column(name="artificiality")
    private String artificiality;
    
    @Header(name="合价（材料）")
    @Column(name="materiels")
    private String materiels;

    @Header(name="合价（机械）")
    @Column(name="mech")
    private String mech;
    
    @Header(name="主材合价")
    @Column(name="materiels_unit_price")
    private String materielsUnitPrice;
    
    @Header(name="分组")
    @Column(name="quota_group")
    private String quotaGroup;
    
    @Header(name="项目")
    @Column(name="project_id")
    private String projectID;
    
    @Header(name="预算")
    @Column(name="budget_no")
    private String budgetNo;
    
    @Header(name="拆分合并标志")
    @Column(name="order_no")
    private String orderNo;
    
    @Header(name="行高标志")
    @Column(name="col")
    private String col;

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getQuantities() {
        return quantities;
    }

    public void setQuantities(String quantities) {
        this.quantities = quantities;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getArtificiality() {
        return artificiality;
    }

    public void setArtificiality(String artificiality) {
        this.artificiality = artificiality;
    }

    public String getMateriels() {
        return materiels;
    }

    public void setMateriels(String materiels) {
        this.materiels = materiels;
    }

    public String getMech() {
        return mech;
    }

    public void setMech(String mech) {
        this.mech = mech;
    }

    public String getMaterielsUnitPrice() {
        return materielsUnitPrice;
    }

    public void setMaterielsUnitPrice(String materielsUnitPrice) {
        this.materielsUnitPrice = materielsUnitPrice;
    }

    public String getQuotaGroup() {
        return quotaGroup;
    }

    public void setQuotaGroup(String quotaGroup) {
        this.quotaGroup = quotaGroup;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

}
