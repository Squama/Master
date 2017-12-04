/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年12月4日    Create this file
* </pre>
* 
*/
@Entity
@Table(name = "tbl_budget_import")
public class BudgetImport {
    
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "id", length = 36)
    private String id;
    
    private String quotaNo;
    
    private String quotaName;
    
    private String units;
    
    private String quantities;
    
    private String price;
    
    private String unitPrice;
    
    private String artificiality;
    
    private String materiels;
    
    private String mech;
    
    private String materielsUnitPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuotaNo() {
        return quotaNo;
    }

    public void setQuotaNo(String quotaNo) {
        this.quotaNo = quotaNo;
    }

    public String getQuotaName() {
        return quotaName;
    }

    public void setQuotaName(String quotaName) {
        this.quotaName = quotaName;
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
    
    
}
