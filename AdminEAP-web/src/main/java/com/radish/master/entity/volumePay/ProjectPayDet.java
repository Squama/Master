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
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
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
    
    
}
