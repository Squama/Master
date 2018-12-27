/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity.workmanage;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 类说明
 * 车辆表
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* wangzhihao      2017年10月28日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_car")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Car implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;

    
    @Header(name = "编号")
    @Column(name = "number")
    private String number;
    
    @Header(name = "司机")
    @Column(name = "driver")
    private String driver;
    
    @Header(name = "车辆型号")
    @Column(name = "clxh")
    private String clxh;
    
   
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "行车证年检日期")
    @Column(name = "njtime")
    private Date njtime;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "保险日期")
    @Column(name = "bxtime")
    private Date bxtime;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "道路运输证年审日期")
    @Column(name = "ysznstime")
    private Date ysznstime;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "尾气检测日期")
    @Column(name = "qcwqtime")
    private Date qcwqtime;
    
    @Header(name = "备注")
    @Column(name = "descs")
    private String descs;
    
    @Header(name = "是否有效")
    @Column(name = "isvalid")
    private String isvalid;
    
    @Header(name = "车辆使用状态")
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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getClxh() {
		return clxh;
	}

	public void setClxh(String clxh) {
		this.clxh = clxh;
	}

	public Date getNjtime() {
		return njtime;
	}

	public void setNjtime(Date njtime) {
		this.njtime = njtime;
	}

	public Date getBxtime() {
		return bxtime;
	}

	public void setBxtime(Date bxtime) {
		this.bxtime = bxtime;
	}

	public Date getYsznstime() {
		return ysznstime;
	}

	public void setYsznstime(Date ysznstime) {
		this.ysznstime = ysznstime;
	}

	public Date getQcwqtime() {
		return qcwqtime;
	}

	public void setQcwqtime(Date qcwqtime) {
		this.qcwqtime = qcwqtime;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public String getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
    
}
