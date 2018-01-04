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

    @Header(name = "部位ID")
    @Column(name = "region_id")
    private String regionID;
    
    @Header(name = "部位名称")
    @Column(name = "region_name")
    private String regionName;
    
    @Header(name = "物料ID")
    @Column(name = "mat_number")
    private String matNumber;
    
    @Header(name = "物料名称")
    @Column(name = "mat_name")
    private String matName;
    
    @Header(name = "物料规格")
    @Column(name = "mat_standard")
    private String matStandard;
    
    @Header(name = "物料单位")
    @Column(name = "unit")
    private String unit;

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

    @Header(name = "存储状态")
    @Column(name = "status")
    private String status;

    public String getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(String purchaseID) {
        this.purchaseID = purchaseID;
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

	public String getMatName() {
		return matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}

	public String getMatNumber() {
        return matNumber;
    }

    public void setMatNumber(String matNumber) {
        this.matNumber = matNumber;
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

    public String getMatStandard() {
        return matStandard;
    }

    public void setMatStandard(String matStandard) {
        this.matStandard = matStandard;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}