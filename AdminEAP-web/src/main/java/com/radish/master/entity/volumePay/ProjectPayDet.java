package com.radish.master.entity.volumePay;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//付款明细表

@Entity
@Table(name ="tbl_projectPay_det")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class ProjectPayDet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * 主键ID自动生成策略
     */
    @Id
    @Column(name = "id", length = 64)
    private String id;

    
    @Header(name = "付款ID")
    @Column(name = "projectPayId")
    private String projectPayId;
    
    @Header(name = "供应商Id")
    @Column(name = "channelId")
    private String channelId;
    
    @Header(name = "供应商名称")
    @Column(name = "channelName")
    private String channelName;
    
    @Header(name = "期初")
    @Column(name = "qc")
    private String qc;
    
    @Header(name = "期初")
    @Column(name = "bq")
    private String bq;
    
    @Header(name = "期初")
    @Column(name = "fk")
    private String fk;
    
    @Header(name = "期初")
    @Column(name = "qm")
    private String qm;
    
    @Header(name = "备注")
    @Column(name = "content")
    private String content;
    
    @Header(name = "状态")
    @Column(name = "status")
    private String status;
    
    @Header(name = "驳回原因")
    @Column(name = "bhdesc")
    private String bhdesc;
    
    @Header(name = "付款方式")
    @Column(name = "fkfs")
    private String fkfs;

    @Header(name = "用途")
    @Column(name = "useFact")
    private String useFact;
    
    @Header(name = "审核人")
    @Column(name = "shr")
    private String shr;
    
    @Header(name = "附件数量")
    @Column(name = "fjsl")
    private String fjsl;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectPayId() {
		return projectPayId;
	}

	public void setProjectPayId(String projectPayId) {
		this.projectPayId = projectPayId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getQc() {
		return qc;
	}

	public void setQc(String qc) {
		this.qc = qc;
	}

	public String getBq() {
		return bq;
	}

	public void setBq(String bq) {
		this.bq = bq;
	}

	public String getFk() {
		return fk;
	}

	public void setFk(String fk) {
		this.fk = fk;
	}

	public String getQm() {
		return qm;
	}

	public void setQm(String qm) {
		this.qm = qm;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBhdesc() {
		return bhdesc;
	}

	public void setBhdesc(String bhdesc) {
		this.bhdesc = bhdesc;
	}

	public String getFkfs() {
		return fkfs;
	}

	public void setFkfs(String fkfs) {
		this.fkfs = fkfs;
	}

	public String getUseFact() {
		return useFact;
	}

	public void setUseFact(String useFact) {
		this.useFact = useFact;
	}

	public String getShr() {
		return shr;
	}

	public void setShr(String shr) {
		this.shr = shr;
	}

	public String getFjsl() {
		return fjsl;
	}

	public void setFjsl(String fjsl) {
		this.fjsl = fjsl;
	}
    
    
}
