package com.radish.master.entity.fixedassets;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//请修表

@Entity
@Table(name ="tbl_fixedmaintain")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class FixedMaintain implements Serializable {

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
    private String id;
    
    @Header(name = "编号")
    @Column(name = "number")
    private String number;
    
    @Header(name = "请修单位id")
    @Column(name = "qxdeptid")
    private String qxdeptid;
    
    @Header(name = "请修单位")
    @Column(name = "qxdept")
    private String qxdept;
    
    @Header(name = "填写时间")
    @Column(name = "createdate")
    private Date createdate;
    
    @Header(name = "申请人id")
    @Column(name = "sqrid")
    private String sqrid;
    
    @Header(name = "申请人名称")
    @Column(name = "sqr")
    private String sqr;
    
    @Header(name = "维修类型 10自修20外修")
    @Column(name = "wxtype")
    private String wxtype;
    
    @Header(name = "交修人id")
    @Column(name = "jxr")
    private String jxr;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "交修时间")
    @Column(name = "jxdate")
    private Date jxdate;
    
    @Header(name = "承修厂商")
    @Column(name = "cxcs")
    private String cxcs;
    
    @Header(name = "承修总金额")
    @Column(name = "cxje")
    private String cxje;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "交修时间")
    @Column(name = "cxjhdate")
    private Date cxjhdate;
    
    @Header(name = "承修逾期罚款")
    @Column(name = "cxfk")
    private String cxfk;
    
    @Header(name = "验收意见")
    @Column(name = "yscontent")
    private String yscontent;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "验收交货时间")
    @Column(name = "ysjhdate")
    private Date ysjhdate;
    
    @Header(name = "验收人名称")
    @Column(name = "ysr")
    private String ysr;
    
    @Header(name = "部门意见")
    @Column(name = "bmyj")
    private String bmyj;
    
    @Header(name = "部门审核人")
    @Column(name = "bmshr")
    private String bmshr;
    
    @Header(name = "部门意见")
    @Column(name = "cwyj")
    private String cwyj;
    
    @Header(name = "部门审核人")
    @Column(name = "cwshr")
    private String cwshr;
	
    @Header(name = "领导意见")
    @Column(name = "bossyj")
    private String bossyj;
    
    @Header(name = "审核领导")
    @Column(name = "boss")
    private String boss;
    
    @Header(name = "状态 10=新建20=部门审核30=部门驳回40=总经理审批50=总经理驳回60=检修70=部门接收检验80=完成")
    @Column(name = "status")
    private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getQxdeptid() {
		return qxdeptid;
	}

	public void setQxdeptid(String qxdeptid) {
		this.qxdeptid = qxdeptid;
	}

	public String getQxdept() {
		return qxdept;
	}

	public void setQxdept(String qxdept) {
		this.qxdept = qxdept;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
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

	public String getWxtype() {
		return wxtype;
	}

	public void setWxtype(String wxtype) {
		this.wxtype = wxtype;
	}

	public String getJxr() {
		return jxr;
	}

	public void setJxr(String jxr) {
		this.jxr = jxr;
	}

	public Date getJxdate() {
		return jxdate;
	}

	public void setJxdate(Date jxdate) {
		this.jxdate = jxdate;
	}

	public String getCxcs() {
		return cxcs;
	}

	public void setCxcs(String cxcs) {
		this.cxcs = cxcs;
	}

	public String getCxje() {
		return cxje;
	}

	public void setCxje(String cxje) {
		this.cxje = cxje;
	}

	public Date getCxjhdate() {
		return cxjhdate;
	}

	public void setCxjhdate(Date cxjhdate) {
		this.cxjhdate = cxjhdate;
	}

	public String getCxfk() {
		return cxfk;
	}

	public void setCxfk(String cxfk) {
		this.cxfk = cxfk;
	}

	public String getYscontent() {
		return yscontent;
	}

	public void setYscontent(String yscontent) {
		this.yscontent = yscontent;
	}

	public Date getYsjhdate() {
		return ysjhdate;
	}

	public void setYsjhdate(Date ysjhdate) {
		this.ysjhdate = ysjhdate;
	}

	public String getYsr() {
		return ysr;
	}

	public void setYsr(String ysr) {
		this.ysr = ysr;
	}

	public String getBmyj() {
		return bmyj;
	}

	public void setBmyj(String bmyj) {
		this.bmyj = bmyj;
	}

	public String getBmshr() {
		return bmshr;
	}

	public void setBmshr(String bmshr) {
		this.bmshr = bmshr;
	}

	public String getCwyj() {
		return cwyj;
	}

	public void setCwyj(String cwyj) {
		this.cwyj = cwyj;
	}

	public String getCwshr() {
		return cwshr;
	}

	public void setCwshr(String cwshr) {
		this.cwshr = cwshr;
	}

	public String getBossyj() {
		return bossyj;
	}

	public void setBossyj(String bossyj) {
		this.bossyj = bossyj;
	}

	public String getBoss() {
		return boss;
	}

	public void setBoss(String boss) {
		this.boss = boss;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
}
