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
@Table(name = "tbl_checkFkd")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class CheckFkd implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "id", length = 64)
    protected String id;
    
    @Header(name = "所属检查内码")
    @Column(name = "checkDqId")
    private String checkDqId;
    
    @Header(name = "编号")
    @Column(name = "number")
    private String number;
    
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
    
    @Header(name = "违规事实")
    @Column(name = "wgCont")
    private String wgCont;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "违规时间")
    @Column(name = "wgtime")
    private Date wgtime;

    @Header(name = "违规班组")
    @Column(name = "wgbz")
    private String wgbz;
    
    @Header(name = "违规地点")
    @Column(name = "wgdd")
    private String wgdd;
    
    @Header(name = "罚款金额")
    @Column(name = "fkje")
    private String fkje;
    
    @Header(name = "违规班组id")
    @Column(name = "wgbzid")
    private String wgbzid;
    

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCheckDqId() {
		return checkDqId;
	}

	public void setCheckDqId(String checkDqId) {
		this.checkDqId = checkDqId;
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

	public String getWgCont() {
		return wgCont;
	}

	public void setWgCont(String wgCont) {
		this.wgCont = wgCont;
	}

	public Date getWgtime() {
		return wgtime;
	}

	public void setWgtime(Date wgtime) {
		this.wgtime = wgtime;
	}

	public String getWgbz() {
		return wgbz;
	}

	public void setWgbz(String wgbz) {
		this.wgbz = wgbz;
	}

	public String getWgdd() {
		return wgdd;
	}

	public void setWgdd(String wgdd) {
		this.wgdd = wgdd;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getWgbzid() {
		return wgbzid;
	}

	public void setWgbzid(String wgbzid) {
		this.wgbzid = wgbzid;
	}
    
    
   
    
}
