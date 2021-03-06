/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.entity.fixedassets;

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
* dongyan      2019年1月9日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_fixedassets_stk")
public class FixedAssetsStk extends BaseEntity {

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

    /**
     * 默认全是总库 0=总库 1=待定，其它
     */
    @Header(name = "所属库存")
    @Column(name = "belonged_stock")
    private String belongedStock;

    @Header(name = "保管部门编号")
    @Column(name = "keeped_dept_id")
    private String keepedDeptID;

    @Header(name = "保管部门")
    @Column(name = "keeped_dept_name")
    private String keepedDeptName;

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

    @Header(name = "可用数量")
    @Column(name = "quantity_avl")
    private String quantityAvl;

    @Header(name = "生产厂商")
    @Column(name = "vendor")
    private String vendor;

    /**
     * 10=固定资产 20=器具、工具 30=办公用品
     */
    @Header(name = "单据类型")
    @Column(name = "fa_type")
    private String faType;

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

    public String getKeepedDeptID() {
        return keepedDeptID;
    }

    public void setKeepedDeptID(String keepedDeptID) {
        this.keepedDeptID = keepedDeptID;
    }

    public String getKeepedDeptName() {
        return keepedDeptName;
    }

    public void setKeepedDeptName(String keepedDeptName) {
        this.keepedDeptName = keepedDeptName;
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

    public String getQuantityAvl() {
        return quantityAvl;
    }

    public void setQuantityAvl(String quantityAvl) {
        this.quantityAvl = quantityAvl;
    }

    public String getFaType() {
        return faType;
    }

    public void setFaType(String faType) {
        this.faType = faType;
    }

}
