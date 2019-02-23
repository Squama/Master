/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.entity.fixedassets;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2019年2月23日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_fixedassets_use")
public class FixedAssetsUse extends BaseEntity {

    private static final long serialVersionUID = 6551156975502374969L;

    @Header(name = "库存ID")
    @Column(name = "stk_id")
    private String stkID;

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

    @Header(name = "使用部门编号")
    @Column(name = "used_dept_id")
    private String usedDeptID;

    @Header(name = "使用部门")
    @Column(name = "used_dept_name")
    private String usedDeptName;

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

    /**
     * 10=固定资产 20=器具、工具 30=办公用品
     */
    @Header(name = "单据类型")
    @Column(name = "fa_type")
    private String faType;

    /**
     * 10=不需要 20=需要
     */
    @Header(name = "是否需要归还")
    @Column(name = "need_return")
    private String needReturn;

    /**
     * 10=新建 20=审核通过 30=审核通过（需归还） 40=归还申请 50=已归还 60=审核不通过
     */
    @Header(name = "状态")
    @Column(name = "status")
    private String status;

    @Header(name = "领用人")
    @Column(name = "operator")
    private String operator;

    @Column(name = "return_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    protected Date returnTime;

    public String getStkID() {
        return stkID;
    }

    public void setStkID(String stkID) {
        this.stkID = stkID;
    }

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

    public String getUsedDeptID() {
        return usedDeptID;
    }

    public void setUsedDeptID(String usedDeptID) {
        this.usedDeptID = usedDeptID;
    }

    public String getUsedDeptName() {
        return usedDeptName;
    }

    public void setUsedDeptName(String usedDeptName) {
        this.usedDeptName = usedDeptName;
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

    public String getFaType() {
        return faType;
    }

    public void setFaType(String faType) {
        this.faType = faType;
    }

    public String getNeedReturn() {
        return needReturn;
    }

    public void setNeedReturn(String needReturn) {
        this.needReturn = needReturn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

}
