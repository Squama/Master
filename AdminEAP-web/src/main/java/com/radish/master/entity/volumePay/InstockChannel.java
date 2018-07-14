package com.radish.master.entity.volumePay;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//入库供应商视图

@Entity
@Table(name ="v_instockChannel")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class InstockChannel implements Serializable {

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
    @Column(name = "mxid", length = 64)
    protected String mxid;
    
    @Header(name = "入库id")
    @Column(name = "rkid")
    protected String rkid;

    @Header(name = "入库时间")
    @Column(name = "indate")
    private Date indate;
    
    @Header(name = "所属项目id")
    @Column(name = "projectId")
    private String projectId;
    
    @Header(name = "单价")
    @Column(name = "price")
    private String price;
    
    @Header(name = "入库量")
    @Column(name = "rkl")
    private String rkl;
    
    @Header(name = "总价")
    @Column(name = "zj")
    private String zj;
    
    @Header(name = "供应商名称")
    @Column(name = "supplier")
    private String supplier;
    
    @Header(name = "供应商id")
    @Column(name = "qdid")
    private String qdid;

    
	public String getMxid() {
		return mxid;
	}

	public void setMxid(String mxid) {
		this.mxid = mxid;
	}

	public String getRkid() {
		return rkid;
	}

	public void setRkid(String rkid) {
		this.rkid = rkid;
	}

	public Date getIndate() {
		return indate;
	}

	public void setIndate(Date indate) {
		this.indate = indate;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRkl() {
		return rkl;
	}

	public void setRkl(String rkl) {
		this.rkl = rkl;
	}

	public String getZj() {
		return zj;
	}

	public void setZj(String zj) {
		this.zj = zj;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getQdid() {
		return qdid;
	}

	public void setQdid(String qdid) {
		this.qdid = qdid;
	}
    
    
}
