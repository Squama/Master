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
* dongyan      2019年2月12日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_fixedassets_pur_tx")
public class FixedAssetsPurTx extends BaseEntity {

    private static final long serialVersionUID = 1133208699894833264L;

    @Header(name = "与库存对应")
    @Column(name = "stk_id")
    private String stkID;

    @Header(name = "主表主键")
    @Column(name = "pur_id")
    private String purID;

    @Header(name = "名称")
    @Column(name = "name")
    private String name;

    @Header(name = "英文名称")
    @Column(name = "english_name")
    private String englishName;

    @Header(name = "型号")
    @Column(name = "model")
    private String model;

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

    @Header(name = "渠道ID")
    @Column(name = "channel_id")
    private String channelID;

    @Header(name = "渠道名称")
    @Column(name = "channel_name")
    private String channelName;

    /**
     * 10=固定资产 20=器具、工具 30=办公用品
     */
    @Header(name = "单据类型")
    @Column(name = "fa_type")
    private String faType;

    public String getPurID() {
        return purID;
    }

    public void setPurID(String purID) {
        this.purID = purID;
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

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getStkID() {
        return stkID;
    }

    public void setStkID(String stkID) {
        this.stkID = stkID;
    }

    public String getFaType() {
        return faType;
    }

    public void setFaType(String faType) {
        this.faType = faType;
    }

}
