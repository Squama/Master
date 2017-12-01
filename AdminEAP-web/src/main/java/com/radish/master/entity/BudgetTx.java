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

    @Header(name = "部位ID")
    @Column(name = "region_id")
    private String regionID;
    
    @Header(name = "部位名称")
    @Column(name = "region_name")
    private String regionName;
    
    @Header(name = "部位特征描述")
    @Column(name = "region_desc")
    private String regionDesc;
    
    @Header(name = "计量单位")
    @Column(name = "units")
    private String units;
    
    @Header(name = "工程量")
    @Column(name = "quantities")
    private String quantities;
    
    @Header(name = "综合单价")
    @Column(name = "comprehensive_price")
    private String comprehensivePrice;
    
    @Header(name = "合价")
    @Column(name = "unit_price")
    private String unitPrice;
    
    @Header(name = "暂估价")
    @Column(name = "provisional_price")
    private String provisionalPrice;

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

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionDesc() {
        return regionDesc;
    }

    public void setRegionDesc(String regionDesc) {
        this.regionDesc = regionDesc;
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

    public String getComprehensivePrice() {
        return comprehensivePrice;
    }

    public void setComprehensivePrice(String comprehensivePrice) {
        this.comprehensivePrice = comprehensivePrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProvisionalPrice() {
        return provisionalPrice;
    }

    public void setProvisionalPrice(String provisionalPrice) {
        this.provisionalPrice = provisionalPrice;
    }
    
}
