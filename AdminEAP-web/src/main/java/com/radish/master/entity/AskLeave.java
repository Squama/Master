package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//请假表

@Entity
@Table(name ="tbl_askleave")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class AskLeave implements Serializable {

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
    @Column(name = "id", length = 64)
    private String id;
    
    @Header(name = "项目或部门内码")
    @Column(name = "pid")
    private String pid;
 
    @Header(name = "请假人id")
    @Column(name = "askid")
    private String askid;
    
    @Header(name = "请假人名称")
    @Column(name = "askame")
    private String askame;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "开始时间")
    @Column(name = "startdate")
    private Date startdate;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "结束时间")
    @Column(name = "enddate")
    private Date enddate;
    
    @Header(name = "状态 10-新建,20-部门审核,30-部门驳回,40-办公室审核,50-办公室驳回,60-领导审核,70-领导驳回，80-完成")
    @Column(name = "status")
    private String status;
    
    @Header(name = "总天数")
    @Column(name = "alldays")
    private String alldays;
    
    @Header(name = "部门意见")
    @Column(name = "bmyj")
    private String bmyj;
    
    @Header(name = "部门审核人")
    @Column(name = "bmshr")
    private String bmshr;
    

    @Header(name = "部门意见")
    @Column(name = "bgsyj")
    private String bgsyj;
    
    @Header(name = "部门审核人")
    @Column(name = "bgsshr")
    private String bgsshr;
    
    @Header(name = "领导意见")
    @Column(name = "bossyj")
    private String bossyj;
    
    @Header(name = "审核领导")
    @Column(name = "boss")
    private String boss;
    
    @Header(name = "事由")
    @Column(name = "content")
    private String content;
    
    @Header(name = "部门")
    @Column(name = "dept")
    private String dept;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getAskid() {
		return askid;
	}

	public void setAskid(String askid) {
		this.askid = askid;
	}

	public String getAskame() {
		return askame;
	}

	public void setAskame(String askame) {
		this.askame = askame;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAlldays() {
		return alldays;
	}

	public void setAlldays(String alldays) {
		this.alldays = alldays;
	}

	public String getBmyj() {
		return bmyj;
	}

	public void setBmyj(String bmyj) {
		this.bmyj = bmyj;
	}

	public String getBmshr() {
		return bmshr;
	}

	public void setBmshr(String bmshr) {
		this.bmshr = bmshr;
	}

	public String getBgsyj() {
		return bgsyj;
	}

	public void setBgsyj(String bgsyj) {
		this.bgsyj = bgsyj;
	}

	public String getBgsshr() {
		return bgsshr;
	}

	public void setBgsshr(String bgsshr) {
		this.bgsshr = bgsshr;
	}

	public String getBossyj() {
		return bossyj;
	}

	public void setBossyj(String bossyj) {
		this.bossyj = bossyj;
	}

	public String getBoss() {
		return boss;
	}

	public void setBoss(String boss) {
		this.boss = boss;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}
    

	
    
    
}
