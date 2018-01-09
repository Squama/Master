/**
 * 
 */
package com.radish.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * @author tonyd
 *
 */
@Entity
@Table(name = "tbl_dispatch_detail")
public class DispatchDetail extends BaseEntity{

	private static final long serialVersionUID = -4047955577096808803L;

	/**
	 * 用于取消调度
	 */
	@Header(name="采购明细编号")
    @Column(name="purchase_det_id")
	private String purchaseDetID;
	
	@Header(name="调度单编号")
    @Column(name="dispatch_id")
	private String dispatchID;
	
	@Header(name="渠道ID")
    @Column(name="channel_id")
	private String channelID;
	
	@Header(name="单价")
    @Column(name="price")
	private double price;
	
	@Header(name="物料编号")
    @Column(name="mat_number")
	private String matNumber;
	
	@Header(name="调度数量")
    @Column(name="quantity")
	private double quantity;

	public String getDispatchID() {
		return dispatchID;
	}

	public void setDispatchID(String dispatchID) {
		this.dispatchID = dispatchID;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getMatNumber() {
		return matNumber;
	}

	public void setMatNumber(String matNumber) {
		this.matNumber = matNumber;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

    public String getPurchaseDetID() {
        return purchaseDetID;
    }

    public void setPurchaseDetID(String purchaseDetID) {
        this.purchaseDetID = purchaseDetID;
    }
}
