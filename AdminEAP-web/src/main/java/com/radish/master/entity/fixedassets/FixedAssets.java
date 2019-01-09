/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.entity.fixedassets;

import javax.persistence.Column;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2019年1月9日    Create this file
 * </pre>
 * 
 */

public class FixedAssets extends BaseEntity {

    private static final long serialVersionUID = 8787799691593939611L;

    @Header(name = "名称")
    @Column(name = "name")
    private String name;

    @Header(name = "英文名称")
    @Column(name = "english_name")
    private String englishName;

    @Header(name = "型号")
    @Column(name = "model")
    private String model;

    @Header(name = "所属库存")
    @Column(name = "belonged_stock")
    private String belongedStock;

    @Header(name = "保管部门")
    @Column(name = "keeped_dept")
    private String keepedDept;

    @Header(name = "规格")
    @Column(name = "norm")
    private String norm;

    @Header(name = "单位")
    @Column(name = "unit")
    private String unit;

    @Header(name = "单价")
    @Column(name = "price")
    private String price;

    @Header(name = "数量")
    @Column(name = "quantity")
    private String quantity;

    @Header(name = "生产厂商")
    @Column(name = "vendor")
    private String vendor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBelongedStock() {
        return belongedStock;
    }

    public void setBelongedStock(String belongedStock) {
        this.belongedStock = belongedStock;
    }

    public String getKeepedDept() {
        return keepedDept;
    }

    public void setKeepedDept(String keepedDept) {
        this.keepedDept = keepedDept;
    }

    public String getNorm() {
        return norm;
    }

    public void setNorm(String norm) {
        this.norm = norm;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

}
