package com.radish.master.entity.volumePay;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
	 * 工程量清单支付
	 * @author wangzhihao
	 * @创建时间 2018年5月2日 下午8:36:45
	 * @return
	 */
@Entity
@Table(name ="tbl_volumePay")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class VolumePay implements Serializable {

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

    @Header(name = "所属工程量id")
    @Column(name = "volumeId")
    private String volumeId;
    
    @Header(name = "支付类型")
    @Column(name = "payType")
    private String payType;
    
    @Header(name = "支付方式")
    @Column(name = "payWay")
    private String payWay;
    
    @Header(name = "支付金额")
    @Column(name = "payMoney")
    private String payMoney;
    
    @Header(name = "部门")
    @Column(name = "department")
    private String department;
    
    @Header(name = "内容及用途")
    @Column(name = "content")
    private String content;
    
    @Header(name = "创建人id")
    @Column(name = "createId")
    private String createId;
    
    @Header(name = "创建日期")
    @Column(name = "createDate")
    private Date createDate;
    
    @Header(name = "负责人id")
    @Column(name = "fzrId")
    private String fzrId;
    
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
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
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

	public String getFzrId() {
		return fzrId;
	}

	public void setFzrId(String fzrId) {
		this.fzrId = fzrId;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
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

	

}
