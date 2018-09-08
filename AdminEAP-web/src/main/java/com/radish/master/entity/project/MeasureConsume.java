/**
 * 
 */
package com.radish.master.entity.project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * @author tonyd
 *
 */
@Entity
@Table(name = "tbl_measure_consume")
public class MeasureConsume extends BaseEntity {

	private static final long serialVersionUID = -1015154647867433617L;

	@Header(name = "所属项目编码")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "所属项目子项编码")
    @Column(name = "project_sub_id")
    private String projectSubID;

    @Header(name = "安全施工费")
    @Column(name = "safe")
    private String safe;

    @Header(name = "文明施工费")
    @Column(name = "civil")
    private String civil;
    
    @Header(name = "环境保护费")
    @Column(name = "envir")
    private String envir;

    @Header(name = "临设费")
    @Column(name = "temp")
    private String temp;
    
    @Header(name = "夜间施工增加")
    @Column(name = "night")
    private String night;

    @Header(name = "二次搬运费")
    @Column(name = "twice")
    private String twice;
    
    @Header(name = "冬雨季施工费")
    @Column(name = "winter")
    private String winter;

    @Header(name = "已完工程及设备保护费")
    @Column(name = "protect")
    private String protect;
    
    @Header(name = "工程定位复测费")
    @Column(name = "retest")
    private String retest;

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public String getProjectSubID() {
		return projectSubID;
	}

	public void setProjectSubID(String projectSubID) {
		this.projectSubID = projectSubID;
	}

	public String getSafe() {
		return safe;
	}

	public void setSafe(String safe) {
		this.safe = safe;
	}

	public String getCivil() {
		return civil;
	}

	public void setCivil(String civil) {
		this.civil = civil;
	}

	public String getEnvir() {
		return envir;
	}

	public void setEnvir(String envir) {
		this.envir = envir;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getNight() {
		return night;
	}

	public void setNight(String night) {
		this.night = night;
	}

	public String getTwice() {
		return twice;
	}

	public void setTwice(String twice) {
		this.twice = twice;
	}

	public String getWinter() {
		return winter;
	}

	public void setWinter(String winter) {
		this.winter = winter;
	}

	public String getProtect() {
		return protect;
	}

	public void setProtect(String protect) {
		this.protect = protect;
	}

	public String getRetest() {
		return retest;
	}

	public void setRetest(String retest) {
		this.retest = retest;
	}
	
}
