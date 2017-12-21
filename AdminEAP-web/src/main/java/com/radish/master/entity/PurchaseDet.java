package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "tbl_purchase_det")
public class PurchaseDet extends BaseEntity {

    private static final long serialVersionUID = 5365926552161649689L;

    @Header(name = "采购单ID")
    @Column(name = "purchase_id")
    private String purchase_id;

    @Header(name = "物料ID")
    @Column(name = "mat_id")
    private String mat_id;

    @Header(name = "渠道名")
    @Column(name = "channel_name")
    private String channel_name;

    @Header(name = "渠道ID")
    @Column(name = "channel_id")
    private String channel_id;

    @Header(name = "单价")
    @Column(name = "price")
    private String price;

    @Header(name = "采购量")
    @Column(name = "quantity")
    private Double quantity;

    @Header(name = "剩余采购量")
    @Column(name = "surplus_quantity")
    private Double surplus_quantity;

    @Header(name = "采购类型")
    @Column(name = "stock_type")
    private String stock_type;

    public String getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(String purchase_id) {
        this.purchase_id = purchase_id;
    }

    public String getMat_id() {
        return mat_id;
    }

    public void setMat_id(String mat_id) {
        this.mat_id = mat_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
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

    public Double getSurplus_quantity() {
        return surplus_quantity;
    }

    public void setSurplus_quantity(Double surplus_quantity) {
        this.surplus_quantity = surplus_quantity;
    }

    public String getStock_type() {
        return stock_type;
    }

    public void setStock_type(String stock_type) {
        this.stock_type = stock_type;
    }
}
