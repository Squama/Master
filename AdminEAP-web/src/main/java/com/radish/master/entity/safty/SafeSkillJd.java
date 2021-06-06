/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity.safty;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name = "tbl_SafeSkillJD")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class SafeSkillJd implements Serializable {


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
    
    @Header(name = "编号")
    @Column(name = "number")
    private String number;
    
    @Header(name = "类型 10公司 20项目")
    @Column(name = "type")
    private String type;
    
    @Header(name = "创建人")
    @Column(name = "create_name")
    private String create_name;

    @Header(name = "创建人id")
    @Column(name = "create_name_ID")
    private String create_name_ID;

    @Header(name = "创建时间")
    @Column(name = "create_time")
    private Date create_time;
    
    @Header(name = "提要")
    @Column(name = "ty")
    private String ty;
    
    @Header(name = "对象")
    @Column(name = "dx")
    private String dx;
    
    @Header(name = "具体位置")
    @Column(name = "jtwz")
    private String jtwz;
    
    @Header(name = "交底内容")
    @Column(name = "content")
    private String content;
    
    @Header(name = "审核人")
    @Column(name = "shname")
    private String shname;
    
    @Header(name = "交底人")
    @Column(name = "jdname")
    private String jdname;
    
    @Header(name = "'接受交底人")
    @Column(name = "jsjd")
    private String jsjd;
    
    @Header(name = "工种")
    @Column(name = "gz")
    private String gz;

    @Header(name = "部门负责人")
    @Column(name = "bmfzr")
    private String bmfzr;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "交底日期")
    @Column(name = "jdtime")
    private Date jdtime;
    
    @Header(name = "机械名称")
    @Column(name = "jxmc")
    private String jxmc;
    
    @Header(name = "机械编号")
    @Column(name = "jxbh")
    private String jxbh;
    
    @Header(name = "机械型号")
    @Column(name = "jxxh")
    private String jxxh;
    
    @Header(name = "分项类型")
    @Column(name = "fxlx")
    private String fxlx;
    
    
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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getTy() {
		return ty;
	}

	public void setTy(String ty) {
		this.ty = ty;
	}

	public String getDx() {
		return dx;
	}

	public void setDx(String dx) {
		this.dx = dx;
	}

	public String getJtwz() {
		return jtwz;
	}

	public void setJtwz(String jtwz) {
		this.jtwz = jtwz;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getShname() {
		return shname;
	}

	public void setShname(String shname) {
		this.shname = shname;
	}

	public String getJdname() {
		return jdname;
	}

	public void setJdname(String jdname) {
		this.jdname = jdname;
	}

	public String getJsjd() {
		return jsjd;
	}

	public void setJsjd(String jsjd) {
		this.jsjd = jsjd;
	}

	public String getGz() {
		return gz;
	}

	public void setGz(String gz) {
		this.gz = gz;
	}

	public String getBmfzr() {
		return bmfzr;
	}

	public void setBmfzr(String bmfzr) {
		this.bmfzr = bmfzr;
	}

	public Date getJdtime() {
		return jdtime;
	}

	public void setJdtime(Date jdtime) {
		this.jdtime = jdtime;
	}

	public String getJxmc() {
		return jxmc;
	}

	public void setJxmc(String jxmc) {
		this.jxmc = jxmc;
	}

	public String getJxbh() {
		return jxbh;
	}

	public void setJxbh(String jxbh) {
		this.jxbh = jxbh;
	}

	public String getJxxh() {
		return jxxh;
	}

	public void setJxxh(String jxxh) {
		this.jxxh = jxxh;
	}

	public String getFxlx() {
		return fxlx;
	}

	public void setFxlx(String fxlx) {
		this.fxlx = fxlx;
	}
    
    
}
