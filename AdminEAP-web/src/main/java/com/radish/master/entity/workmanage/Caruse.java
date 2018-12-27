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
 * 车辆使用表
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* wangzhihao      2017年10月28日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_caruse")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Caruse implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;

    
    @Header(name = "车辆内码")
    @Column(name = "carid")
    private String carid;
    
    @Header(name = "车牌号")
    @Column(name = "number")
    private String number;
    
    @Header(name = "司机id")
    @Column(name = "driverid")
    private String driverid;
    
    @Header(name = "司机")
    @Column(name = "driver")
    private String driver;
    
   
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "出车时间")
    @Column(name = "cctime")
    private Date cctime;
    
    @Header(name = "出车小时数")
    @Column(name = "cchours")
    private String cchours;
    
    @Header(name = "目的地")
    @Column(name = "mdd")
    private String mdd;
    
    @Header(name = "出车原因")
    @Column(name = "ccyy")
    private String ccyy;
    
    @Header(name = "出车前里程数")
    @Column(name = "cclcs")
    private String cclcs;
    
    @Header(name = "部门id")
    @Column(name = "deptid")
    private String deptid;
    
    @Header(name = "部门名称")
    @Column(name = "deptname")
    private String deptname;
    
    @Header(name = "随行人员")
    @Column(name = "sxry")
    private String sxry;
    
    @Header(name = "备注")
    @Column(name = "descs")
    private String descs;
    
    @Header(name = "审核状态")
    @Column(name = "status")
    private String status;
    
    @Header(name = "审核原因")
    @Column(name = "bhyy")
    private String bhyy;
    
    @Header(name = "审核人")
    @Column(name = "shr")
    private String shr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCarid() {
		return carid;
	}

	public void setCarid(String carid) {
		this.carid = carid;
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


	public Date getCctime() {
		return cctime;
	}

	public void setCctime(Date cctime) {
		this.cctime = cctime;
	}

	public String getCchours() {
		return cchours;
	}

	public void setCchours(String cchours) {
		this.cchours = cchours;
	}

	public String getMdd() {
		return mdd;
	}

	public void setMdd(String mdd) {
		this.mdd = mdd;
	}

	public String getCcyy() {
		return ccyy;
	}

	public void setCcyy(String ccyy) {
		this.ccyy = ccyy;
	}

	public String getCclcs() {
		return cclcs;
	}

	public void setCclcs(String cclcs) {
		this.cclcs = cclcs;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getSxry() {
		return sxry;
	}

	public void setSxry(String sxry) {
		this.sxry = sxry;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBhyy() {
		return bhyy;
	}

	public void setBhyy(String bhyy) {
		this.bhyy = bhyy;
	}

	public String getDriverid() {
		return driverid;
	}

	public void setDriverid(String driverid) {
		this.driverid = driverid;
	}

	public String getShr() {
		return shr;
	}

	public void setShr(String shr) {
		this.shr = shr;
	}

	
    
    
    
}
