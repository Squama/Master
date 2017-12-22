package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "tbl_purchase_det")
public class PurchaseDet extends BaseEntity {

    private static final long serialVersionUID = 5365926552161649689L;

    @Header(name = "采购单ID")
    @Column(name = "purchase_id")
    private String purchaseID;

    @Header(name = "物料ID")
    @Column(name = "mat_id")
    private String matID;

    @Header(name = "渠道名")
    @Column(name = "channel_name")
    private String channelName;

    @Header(name = "渠道ID")
    @Column(name = "channel_id")
    private String channelID;

    @Header(name = "单价")
    @Column(name = "price")
    private String price;

    @Header(name = "采购量")
    @Column(name = "quantity")
    private Double quantity;

    @Header(name = "剩余采购量")
    @Column(name = "surplus_quantity")
    private Double surplusQuantity;

    @Header(name = "采购类型")
    @Column(name = "stock_type")
    private String stockType;

    public String getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(String purchaseID) {
        this.purchaseID = purchaseID;
    }

    public String getMatID() {
        return matID;
    }

    public void setMatID(String matID) {
        this.matID = matID;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getSurplusQuantity() {
        return surplusQuantity;
    }

    public void setSurplusQuantity(Double surplusQuantity) {
        this.surplusQuantity = surplusQuantity;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }
    
}
