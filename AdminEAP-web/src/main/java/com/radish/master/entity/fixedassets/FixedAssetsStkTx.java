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
* dongyan      2019年1月9日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_fixedassets_stk_tx")
public class FixedAssetsStkTx extends BaseEntity {

    private static final long serialVersionUID = 2329499931060350521L;

    @Header(name = "固定资产主表ID")
    @Column(name = "fixed_assets_id")
    private String fixedAssetsID;

    @Header(name = "操作类型")
    @Column(name = "operation")
    private String operation;

    @Header(name = "发生额")
    @Column(name = "amount")
    private String amount;

    @Header(name = "余额")
    @Column(name = "balance")
    private String balance;

    @Header(name = "入库单价")
    @Column(name = "price")
    private String price;

    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;

    @Header(name = "操作员")
    @Column(name = "operator")
    private String operator;

    @Header(name = "操作时间")
    @Column(name = "operate_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private Date operateTime;

    @Header(name = "来源ID")
    @Column(name = "source_tx_id")
    private String sourceTxID;

    /**
     * 10=不需要 20=需要
     */
    @Header(name = "是否需要归还")
    @Column(name = "need_return")
    private String needReturn;

    public String getFixedAssetsID() {
        return fixedAssetsID;
    }

    public void setFixedAssetsID(String fixedAssetsID) {
        this.fixedAssetsID = fixedAssetsID;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getSourceTxID() {
        return sourceTxID;
    }

    public void setSourceTxID(String sourceTxID) {
        this.sourceTxID = sourceTxID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNeedReturn() {
        return needReturn;
    }

    public void setNeedReturn(String needReturn) {
        this.needReturn = needReturn;
    }

}
