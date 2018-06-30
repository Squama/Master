/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity.skillManage;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Blob;
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
@Table(name = "tbl_QCproject")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class QCproject implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;

    @Header(name = "项目id")
    @Column(name = "proid")
    private String proid;
    
    @Header(name = "所属项目")
    @Column(name = "proname")
    private String proname;
    
    @Header(name = "创建人")
    @Column(name = "create_name")
    private String create_name;

    @Header(name = "创建人id")
    @Column(name = "create_name_ID")
    private String create_name_ID;

    @Header(name = "创建时间")
    @Column(name = "create_time")
    private Date create_time;
    
    @Header(name = "目的")
    @Column(name = "md")
    private String md;
    
    @Header(name = "工程概况")
    @Column(name = "gcgk")
    private String gcgk;
    
    @Header(name = "小组概况")
    @Column(name = "xzgk")
    private String xzgk;
    
    @Header(name = "小组成员")
    @Column(name = "xzcy")
    private String xzcy;
    
    @Header(name = "小组计划")
    @Column(name = "xzjh")
    private String xzjh;
    
    @Header(name = "选题理由")
    @Column(name = "xtly")
    private String xtly;
    
    @Header(name = "现状调查")
    @Column(name = "xzdc")
    private String xzdc;
    
    @Header(name = "论证")
    @Column(name = "lz")
    private String lz;
    
    @Header(name = "原因分析")
    @Column(name = "yyfx")
    private String yyfx;
    
    @Header(name = "要因确认")
    @Column(name = "yyqr")
    private String yyqr;
    
    @Header(name = "对策")
    @Column(name = "dc")
    private String dc;
    
    @Header(name = "对策实施情况")
    @Column(name = "dcssqk")
    private String dcssqk;
    
    @Header(name = "结果")
    @Column(name = "jg")
    private String jg;
    
    @Header(name = "巩固措施")
    @Column(name = "ggcs")
    private String ggcs;
    
    @Header(name = "总结")
    @Column(name = "zj")
    private String zj;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getMd() {
		return md;
	}

	public void setMd(String md) {
		this.md = md;
	}

	public String getGcgk() {
		return gcgk;
	}

	public void setGcgk(String gcgk) {
		this.gcgk = gcgk;
	}

	public String getXzgk() {
		return xzgk;
	}

	public void setXzgk(String xzgk) {
		this.xzgk = xzgk;
	}

	public String getXzcy() {
		return xzcy;
	}

	public void setXzcy(String xzcy) {
		this.xzcy = xzcy;
	}

	public String getXzjh() {
		return xzjh;
	}

	public void setXzjh(String xzjh) {
		this.xzjh = xzjh;
	}

	public String getXtly() {
		return xtly;
	}

	public void setXtly(String xtly) {
		this.xtly = xtly;
	}

	public String getXzdc() {
		return xzdc;
	}

	public void setXzdc(String xzdc) {
		this.xzdc = xzdc;
	}

	public String getLz() {
		return lz;
	}

	public void setLz(String lz) {
		this.lz = lz;
	}

	public String getYyfx() {
		return yyfx;
	}

	public void setYyfx(String yyfx) {
		this.yyfx = yyfx;
	}

	public String getYyqr() {
		return yyqr;
	}

	public void setYyqr(String yyqr) {
		this.yyqr = yyqr;
	}

	public String getDc() {
		return dc;
	}

	public void setDc(String dc) {
		this.dc = dc;
	}

	public String getDcssqk() {
		return dcssqk;
	}

	public void setDcssqk(String dcssqk) {
		this.dcssqk = dcssqk;
	}

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}

	public String getGgcs() {
		return ggcs;
	}

	public void setGgcs(String ggcs) {
		this.ggcs = ggcs;
	}

	public String getZj() {
		return zj;
	}

	public void setZj(String zj) {
		this.zj = zj;
	}

    
}
