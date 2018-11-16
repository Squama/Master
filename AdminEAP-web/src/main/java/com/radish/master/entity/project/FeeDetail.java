/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.entity.project;

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
* dongyan      2018年11月15日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_fee_detail")
public class FeeDetail extends BaseEntity {

    private static final long serialVersionUID = 7619886395790747235L;

    @Header(name = "主表ID")
    @Column(name = "fee_id")
    private String feeID;

    @Header(name = "名称")
    @Column(name = "name")
    private String name;

    @Header(name = "价格")
    @Column(name = "price")
    private String price;

    @Header(name = "说明")
    @Column(name = "remark")
    private String remark;

    public String getFeeID() {
        return feeID;
    }

    public void setFeeID(String feeID) {
        this.feeID = feeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
