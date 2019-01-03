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
 * 印章使用表
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* wangzhihao      2017年10月28日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_sealuse")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Sealuse implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;

    
    @Header(name = "印章内码")
    @Column(name = "sealid")
    private String sealid;
    
    @Header(name = "印章名称")
    @Column(name = "name")
    private String name;
    
    @Header(name = "申请人id")
    @Column(name = "sqrid")
    private String sqrid;
    
    @Header(name = "申请人")
    @Column(name = "sqr")
    private String sqr;
    
   
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "出车时间")
    @Column(name = "sqtime")
    private Date sqtime;
    
    
    @Header(name = "申请原因")
    @Column(name = "sqyy")
    private String sqyy;
    
    @Header(name = "数量")
    @Column(name = "sl")
    private String sl;
    
    @Header(name = "公司名称或项目名称")
    @Column(name = "gsmc")
    private String gsmc;
    
    @Header(name = "部门id")
    @Column(name = "deptid")
    private String deptid;
    
    @Header(name = "部门名称")
    @Column(name = "deptname")
    private String deptname;
    
    @Header(name = "备注")
    @Column(name = "descs")
    private String descs;
    
    @Header(name = "审核状态")
    @Column(name = "status")
    private String status;
    
    @Header(name = "审核原因")
    @Column(name = "bmbhyy")
    private String bmbhyy;
    
    @Header(name = "审核人")
    @Column(name = "bmshr")
    private String bmshr;
    
    @Header(name = "审核原因")
    @Column(name = "bgsbhyy")
    private String bgsbhyy;
    
    @Header(name = "审核人")
    @Column(name = "bgsshr")
    private String bgsshr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSealid() {
		return sealid;
	}

	public void setSealid(String sealid) {
		this.sealid = sealid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getGsmc() {
		return gsmc;
	}

	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
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

	public String getBmbhyy() {
		return bmbhyy;
	}

	public void setBmbhyy(String bmbhyy) {
		this.bmbhyy = bmbhyy;
	}

	public String getBmshr() {
		return bmshr;
	}

	public void setBmshr(String bmshr) {
		this.bmshr = bmshr;
	}

	public String getBgsbhyy() {
		return bgsbhyy;
	}

	public void setBgsbhyy(String bgsbhyy) {
		this.bgsbhyy = bgsbhyy;
	}

	public String getBgsshr() {
		return bgsshr;
	}

	public void setBgsshr(String bgsshr) {
		this.bgsshr = bgsshr;
	}

    
    
    
}
