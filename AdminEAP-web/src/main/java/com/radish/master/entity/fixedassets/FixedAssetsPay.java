package com.radish.master.entity.fixedassets;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name ="tbl_fixedAssetsPay")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class FixedAssetsPay implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;

    @Header(name = "所属资产id")
    @Column(name = "purId")
    private String purId;
    
    @Header(name = "支付类型/10固定/20器具/30办公")
    @Column(name = "payType")
    private String payType;
    
    @Header(name = "支付方式/10现金/20转账/30电汇")
    @Column(name = "payWay")
    private String payWay;
    
    @Header(name = "支付金额")
    @Column(name = "payMoney")
    private String payMoney;
    
    
    @Header(name = "内容及用途")
    @Column(name = "content")
    private String content;
    
    @Header(name = "创建人id")
    @Column(name = "createId")
    private String createId;
    
    @Header(name = "创建日期")
    @Column(name = "createDate")
    private Date createDate;
    
    
    @Header(name = "支付状态")
    @Column(name = "status")
    private String status;

    @Header(name = "驳回原因")
    @Column(name = "rebutReason")
    private String rebutReason;
    
    @Header(name = "是否记账")
    @Column(name = "isjz")
    private String isjz;
    
    @Header(name = "收款方")
    @Column(name = "skf")
    private String skf;
    
    @Header(name = "账目id")
    @Column(name = "accountId")
    private String accountId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPurId() {
		return purId;
	}

	public void setPurId(String purId) {
		this.purId = purId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRebutReason() {
		return rebutReason;
	}

	public void setRebutReason(String rebutReason) {
		this.rebutReason = rebutReason;
	}

	public String getIsjz() {
		return isjz;
	}

	public void setIsjz(String isjz) {
		this.isjz = isjz;
	}

	public String getSkf() {
		return skf;
	}

	public void setSkf(String skf) {
		this.skf = skf;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
    

	

}
