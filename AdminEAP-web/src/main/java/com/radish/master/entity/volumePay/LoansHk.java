package com.radish.master.entity.volumePay;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//借款还款
@Entity
@Table(name ="tbl_loansHk")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class LoansHk implements Serializable {

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
    @Column(name = "id", length = 64)
    protected String id;

	@Header(name = "借款id")
    @Column(name = "loansid")
    private String loansid;
	
	@Header(name = "收据内码")
    @Column(name = "receiptid")
    private String receiptid;
	
	@Header(name = "金额")
    @Column(name = "money")
    private String money;
    
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "还款时间")
    @Column(name = "hktime")
    private Date hktime;
	
	@Header(name = "申请人id")
    @Column(name = "sqrid")
    private String sqrid;
    
    @Header(name = "申请人")
    @Column(name = "sqr")
    private String sqr;
    
    @Header(name = "申请时间")
    @Column(name = "sqtime")
    private Date sqtime;
    
    @Header(name = "申请原因")
    @Column(name = "sqyy")
    private String sqyy;
    
    @Header(name = "是否记账")
    @Column(name = "isjz")
    private String isjz;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoansid() {
		return loansid;
	}

	public void setLoansid(String loansid) {
		this.loansid = loansid;
	}

	public String getReceiptid() {
		return receiptid;
	}

	public void setReceiptid(String receiptid) {
		this.receiptid = receiptid;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public Date getHktime() {
		return hktime;
	}

	public void setHktime(Date hktime) {
		this.hktime = hktime;
	}

	public String getSqrid() {
		return sqrid;
	}

	public void setSqrid(String sqrid) {
		this.sqrid = sqrid;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public Date getSqtime() {
		return sqtime;
	}

	public void setSqtime(Date sqtime) {
		this.sqtime = sqtime;
	}

	public String getSqyy() {
		return sqyy;
	}

	public void setSqyy(String sqyy) {
		this.sqyy = sqyy;
	}

	public String getIsjz() {
		return isjz;
	}

	public void setIsjz(String isjz) {
		this.isjz = isjz;
	}

    
}
