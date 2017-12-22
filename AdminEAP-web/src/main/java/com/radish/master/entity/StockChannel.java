package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_stock_channel")
public class StockChannel extends BaseEntity {

    private static final long serialVersionUID = -5596718551580398549L;

    @Header(name = "项目ID")
    @Column(name = "project_id")
    private String project_id;

    @Header(name = "物料ID")
    @Column(name = "mat_id")
    private String mat_id;

    @Header(name = "库存量")
    @Column(name = "stock_num")
    private Double stock_num;

    @Header(name = "渠道编号")
    @Column(name = "channel_id")
    private String channel_id;

    @Header(name = "物料单价")
    @Column(name = "price")
    private Double price;

    @Header(name = "预留字段1")
    @Column(name = "reserve1")
    private String reserve1;

    @Header(name = "预留字段2")
    @Column(name = "reserve2")
    private String reserve2;

    @Header(name = "预留字段3")
    @Column(name = "reserve3")
    private String reserve3;

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getMat_id() {
        return mat_id;
    }

    public void setMat_id(String mat_id) {
        this.mat_id = mat_id;
    }

    public Double getStock_num() {
        return stock_num;
    }

    public void setStock_num(Double stock_num) {
        this.stock_num = stock_num;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }
}
