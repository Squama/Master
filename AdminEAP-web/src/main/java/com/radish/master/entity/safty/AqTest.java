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
@Table(name = "tbl_aqtest")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class AqTest implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "id", length = 64)
    protected String id;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "考核日期")
    @Column(name = "rq")
    private Date rq;
    
    @Header(name = "岗位")
    @Column(name = "station")
    private String station;
    
    @Header(name = "台账类型")
    @Column(name = "type")
    private String type;
    
    @Header(name = "考核人")
    @Column(name = "khr")
    private String khr;
    
    @Header(name = "被考核人")
    @Column(name = "bkhr")
    private String bkhr;
    
    @Header(name = "项目id")
    @Column(name = "projectId")
    private String projectId;
    
    @Header(name = "施工单位")
    @Column(name = "sgdw")
    private String sgdw;
    
    @Header(name = "项目部")
    @Column(name = "xmb")
    private String xmb;
    
    @Header(name = "项目经理")
    @Column(name = "xmjl")
    private String xmjl;
    
    
    @Column(name = "score")
    private String score;
    
    @Column(name = "deduction")
    private String deduction;
    
    @Column(name = "khdj")
    private String khdj;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getRq() {
		return rq;
	}

	public void setRq(Date rq) {
		this.rq = rq;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKhr() {
		return khr;
	}

	public void setKhr(String khr) {
		this.khr = khr;
	}

	public String getBkhr() {
		return bkhr;
	}

	public void setBkhr(String bkhr) {
		this.bkhr = bkhr;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getSgdw() {
		return sgdw;
	}

	public void setSgdw(String sgdw) {
		this.sgdw = sgdw;
	}

	public String getXmb() {
		return xmb;
	}

	public void setXmb(String xmb) {
		this.xmb = xmb;
	}

	public String getXmjl() {
		return xmjl;
	}

	public void setXmjl(String xmjl) {
		this.xmjl = xmjl;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getDeduction() {
		return deduction;
	}

	public void setDeduction(String deduction) {
		this.deduction = deduction;
	}

	public String getKhdj() {
		return khdj;
	}

	public void setKhdj(String khdj) {
		this.khdj = khdj;
	}
	
}
