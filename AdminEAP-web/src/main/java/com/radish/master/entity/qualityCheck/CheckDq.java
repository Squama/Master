/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity.qualityCheck;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* wangzhihao      2017年10月28日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_checkDq")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class CheckDq implements Serializable {


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
    
    @Header(name = "项目id")
    @Column(name = "proid")
    private String proid;
    
    @Header(name = "所属项目")
    @Column(name = "proname")
    private String proname;
    
    @Header(name = "状态")
    @Column(name = "status")
    private String status;
    
    @Header(name = "创建人")
    @Column(name = "create_name")
    private String create_name;

    @Header(name = "创建人id")
    @Column(name = "create_name_ID")
    private String create_name_ID;

    @Header(name = "创建时间")
    @Column(name = "create_time")
    private Date create_time;
    
    @Header(name = "检查内容")
    @Column(name = "checkCont")
    private String checkCont;

    @Header(name = "交接记录")
    @Column(name = "jjjl")
    private String jjjl;

    @Header(name = "上道工序负责人")
    @Column(name = "sdfzr")
    private String sdfzr;
    
    @Header(name = "下道工序负责人")
    @Column(name = "xdfzr")
    private String xdfzr;
    
    @Header(name = "形象进度")
    @Column(name = "xxjd")
    private String xxjd;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "检查时间")
    @Column(name = "checktime")
    private Date checktime;
    
    @Header(name = "整改期限")
    @Column(name = "zgqx")
    private String zgqx;
    
    @Header(name = "整改检查内容")
    @Column(name = "zgjcnr")
    private String zgjcnr;
    
    @Header(name = "需整改内容")
    @Column(name = "xzgnr")
    private String xzgnr;
    
    @Header(name = "检查部门")
    @Column(name = "jcbm")
    private String jcbm;
    
    @Header(name = "检查人")
    @Column(name = "jcr")
    private String jcr;
    
    @Header(name = "受检部门负责人")
    @Column(name = "sjbmr")
    private String sjbmr;
    
    @Header(name = "受检部门负责人电话")
    @Column(name = "sjbmrdh")
    private String sjbmrdh;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "回复日期")
    @Column(name = "hfrq")
    private Date hfrq;
    
    @Header(name = "已整改内容")
    @Column(name = "yzgnr")
    private String yzgnr;
    
    @Header(name = "回复人")
    @Column(name = "hfr")
    private String hfr;
    
    @Header(name = "复查情况")
    @Column(name = "fcqk")
    private String fcqk;
    
    @Header(name = "复查部门")
    @Column(name = "fcbm")
    private String fcbm;
    
    @Header(name = "复查人")
    @Column(name = "fcr")
    private String fcr;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "复查日期")
    @Column(name = "fcrq")
    private Date fcrq;
    
    @Header(name = "受复查人")
    @Column(name = "sfcr")
    private String sfcr;
    
    @Header(name = "本月评价")
    @Column(name = "bypj")
    private String bypj;
    
    @Header(name = "综合评分")
    @Column(name = "zhpf")
    private String zhpf;
    
    @Header(name = "是否罚款")
    @Column(name = "isfk")
    private String isfk;

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

	public String getProid() {
		return proid;
	}

	public void setProid(String proid) {
		this.proid = proid;
	}

	public String getProname() {
		return proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreate_name() {
		return create_name;
	}

	public void setCreate_name(String create_name) {
		this.create_name = create_name;
	}

	public String getCreate_name_ID() {
		return create_name_ID;
	}

	public void setCreate_name_ID(String create_name_ID) {
		this.create_name_ID = create_name_ID;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getCheckCont() {
		return checkCont;
	}

	public void setCheckCont(String checkCont) {
		this.checkCont = checkCont;
	}

	public String getJjjl() {
		return jjjl;
	}

	public void setJjjl(String jjjl) {
		this.jjjl = jjjl;
	}

	public String getSdfzr() {
		return sdfzr;
	}

	public void setSdfzr(String sdfzr) {
		this.sdfzr = sdfzr;
	}

	public String getXdfzr() {
		return xdfzr;
	}

	public void setXdfzr(String xdfzr) {
		this.xdfzr = xdfzr;
	}

	public String getXxjd() {
		return xxjd;
	}

	public void setXxjd(String xxjd) {
		this.xxjd = xxjd;
	}

	public Date getChecktime() {
		return checktime;
	}

	public void setChecktime(Date checktime) {
		this.checktime = checktime;
	}

	public String getZgqx() {
		return zgqx;
	}

	public void setZgqx(String zgqx) {
		this.zgqx = zgqx;
	}

	public String getZgjcnr() {
		return zgjcnr;
	}

	public void setZgjcnr(String zgjcnr) {
		this.zgjcnr = zgjcnr;
	}

	public String getXzgnr() {
		return xzgnr;
	}

	public void setXzgnr(String xzgnr) {
		this.xzgnr = xzgnr;
	}

	public String getJcbm() {
		return jcbm;
	}

	public void setJcbm(String jcbm) {
		this.jcbm = jcbm;
	}

	public String getJcr() {
		return jcr;
	}

	public void setJcr(String jcr) {
		this.jcr = jcr;
	}

	public String getSjbmr() {
		return sjbmr;
	}

	public void setSjbmr(String sjbmr) {
		this.sjbmr = sjbmr;
	}

	public String getSjbmrdh() {
		return sjbmrdh;
	}

	public void setSjbmrdh(String sjbmrdh) {
		this.sjbmrdh = sjbmrdh;
	}

	public Date getHfrq() {
		return hfrq;
	}

	public void setHfrq(Date hfrq) {
		this.hfrq = hfrq;
	}

	public String getYzgnr() {
		return yzgnr;
	}

	public void setYzgnr(String yzgnr) {
		this.yzgnr = yzgnr;
	}

	public String getHfr() {
		return hfr;
	}

	public void setHfr(String hfr) {
		this.hfr = hfr;
	}

	public String getFcqk() {
		return fcqk;
	}

	public void setFcqk(String fcqk) {
		this.fcqk = fcqk;
	}

	public String getFcbm() {
		return fcbm;
	}

	public void setFcbm(String fcbm) {
		this.fcbm = fcbm;
	}

	public String getFcr() {
		return fcr;
	}

	public void setFcr(String fcr) {
		this.fcr = fcr;
	}

	public Date getFcrq() {
		return fcrq;
	}

	public void setFcrq(Date fcrq) {
		this.fcrq = fcrq;
	}

	public String getSfcr() {
		return sfcr;
	}

	public void setSfcr(String sfcr) {
		this.sfcr = sfcr;
	}

	public String getBypj() {
		return bypj;
	}

	public void setBypj(String bypj) {
		this.bypj = bypj;
	}

	public String getZhpf() {
		return zhpf;
	}

	public void setZhpf(String zhpf) {
		this.zhpf = zhpf;
	}

	public String getIsfk() {
		return isfk;
	}

	public void setIsfk(String isfk) {
		this.isfk = isfk;
	}
    
    
}
