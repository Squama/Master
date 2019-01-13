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


@Entity
@Table(name = "tbl_builddiary_safe")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class SafeBuildDiary implements Serializable {


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

    
    @Header(name = "项目内码")
    @Column(name = "xmid")
    private String xmid;
    
    @Header(name = "人员内码")
    @Column(name = "userid")
    private String userid;
    
    @Header(name = "天气")
    @Column(name = "weather")
    private String weather;
    
    @Header(name = "气温")
    @Column(name = "airTemp")
    private String airTemp;
    
    @Header(name = "施工内容")
    @Column(name = "sgnr")
    private String sgnr;
   
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "日志日期")
    @Column(name = "rzdate")
    private Date rzdate;

    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;
    
    @Header(name = "项目名称")
    @Column(name = "xmmc")
    private String xmmc;
    
    @Header(name = "人员名称")
    @Column(name = "rymc")
    private String rymc;
    
    @Header(name = "岗位")
    @Column(name = "jobname")
    private String jobname;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXmid() {
		return xmid;
	}

	public void setXmid(String xmid) {
		this.xmid = xmid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getAirTemp() {
		return airTemp;
	}

	public void setAirTemp(String airTemp) {
		this.airTemp = airTemp;
	}

	public String getSgnr() {
		return sgnr;
	}

	public void setSgnr(String sgnr) {
		this.sgnr = sgnr;
	}

	public Date getRzdate() {
		return rzdate;
	}

	public void setRzdate(Date rzdate) {
		this.rzdate = rzdate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getRymc() {
		return rymc;
	}

	public void setRymc(String rymc) {
		this.rymc = rymc;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
  
    
    
}
