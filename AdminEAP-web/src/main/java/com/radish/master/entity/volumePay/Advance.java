package com.radish.master.entity.volumePay;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//预付款

@Entity
@Table(name ="tbl_advance")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Advance implements Serializable {

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

	@Header(name = "采购明细内码")
    @Column(name = "purdetid")
    private String purdetid;
	
	@Header(name = "支付明细内码")
    @Column(name = "paydetid")
    private String paydetid;
	
	@Header(name = "供应商名称")
    @Column(name = "channelName")
    private String channelName;
	
	@Header(name = "采购金额")
    @Column(name = "cgmoney")
    private String cgmoney;
	
	@Header(name = "金额")
    @Column(name = "money")
    private String money;
    
    @Header(name = "申请金额")
    @Column(name = "sqmoney")
    private String sqmoney;
    
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "支付时间")
    @Column(name = "paytime")
    private Date paytime;
	
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
    
    @Header(name = "物料名称")
    @Column(name = "mat")
    private String mat;
    
    @Header(name = "规格")
    @Column(name = "matStandard")
    private String matStandard;
    
    @Header(name = "是否抵扣")
    @Column(name = "isdk")
    private String isdk;
    
    @Header(name = "状态 10=新建,20=财务审核,30=老总审核,40=完成,50=审核驳回")
    @Column(name = "status")
    private String status;
    
    @Header(name = "是否记账")
    @Column(name = "isjz")
    private String isjz;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPurdetid() {
		return purdetid;
	}

	public void setPurdetid(String purdetid) {
		this.purdetid = purdetid;
	}

	public String getPaydetid() {
		return paydetid;
	}

	public void setPaydetid(String paydetid) {
		this.paydetid = paydetid;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
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

	public String getMat() {
		return mat;
	}

	public void setMat(String mat) {
		this.mat = mat;
	}

	public String getMatStandard() {
		return matStandard;
	}

	public void setMatStandard(String matStandard) {
		this.matStandard = matStandard;
	}

	public String getIsdk() {
		return isdk;
	}

	public void setIsdk(String isdk) {
		this.isdk = isdk;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsjz() {
		return isjz;
	}

	public void setIsjz(String isjz) {
		this.isjz = isjz;
	}

	public String getSqmoney() {
		return sqmoney;
	}

	public void setSqmoney(String sqmoney) {
		this.sqmoney = sqmoney;
	}

	public String getCgmoney() {
		return cgmoney;
	}

	public void setCgmoney(String cgmoney) {
		this.cgmoney = cgmoney;
	}
    
    
}
