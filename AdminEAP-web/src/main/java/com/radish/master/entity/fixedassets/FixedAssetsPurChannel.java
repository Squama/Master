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
@Table(name = "tbl_fixedassets_pur_channel")
public class FixedAssetsPurChannel extends BaseEntity {

    private static final long serialVersionUID = -890515830889132019L;

    @Header(name = "明细ID")
    @Column(name = "pur_tx_id")
    private String purTxID;

    @Header(name = "渠道名称")
    @Column(name = "channel_name")
    private String channelName;

    @Header(name = "渠道价格")
    @Column(name = "price")
    private String price;

    public String getPurTxID() {
        return purTxID;
    }

    public void setPurTxID(String purTxID) {
        this.purTxID = purTxID;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
