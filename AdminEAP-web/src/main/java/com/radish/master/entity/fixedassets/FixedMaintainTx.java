package com.radish.master.entity.fixedassets;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//请修明细表

@Entity
@Table(name ="tbl_fixedmaintain_tx")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class FixedMaintainTx implements Serializable {

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
    
    @Header(name = "请修id")
    @Column(name = "pid")
    private String pid;
    
    @Header(name = "资产id")
    @Column(name = "zcid")
    private String zcid;
    
    @Header(name = "资产名称")
    @Column(name = "name")
    private String name;
    
    @Header(name = "规格")
    @Column(name = "norm")
    private String norm;
    
    
    @Header(name = "使用部门id")
    @Column(name = "sybmid")
    private String sybmid;
    
    @Header(name = "使用部门")
    @Column(name = "sybm")
    private String sybm;
    
    @Header(name = "数量")
    @Column(name = "quantity")
    private String quantity;
    
    @Header(name = "损坏原因")
    @Column(name = "content")
    private String content;
    
    @Header(name = "承修金额")
    @Column(name = "cxje")
    private String cxje;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getZcid() {
		return zcid;
	}

	public void setZcid(String zcid) {
		this.zcid = zcid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNorm() {
		return norm;
	}

	public void setNorm(String norm) {
		this.norm = norm;
	}

	public String getSybmid() {
		return sybmid;
	}

	public void setSybmid(String sybmid) {
		this.sybmid = sybmid;
	}

	public String getSybm() {
		return sybm;
	}

	public void setSybm(String sybm) {
		this.sybm = sybm;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCxje() {
		return cxje;
	}

	public void setCxje(String cxje) {
		this.cxje = cxje;
	}
    
    
}
