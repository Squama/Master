package com.radish.master.entity.volumePay;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//借款表

@Entity
@Table(name ="tbl_loans")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Loans implements Serializable {

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
    
    @Header(name = "借款来源 10-项目借款、20-公司借款")
    @Column(name = "type")
    private String type;
    
    @Header(name = "编号")
    @Column(name = "number")
    private String number;
    
    @Header(name = "借款人id")
    @Column(name = "loanerid")
    private String loanerid;
    
    @Header(name = "借款人名称")
    @Column(name = "loanname")
    private String loanname;
    
    @Header(name = "借款时间")
    @Column(name = "startdate")
    private Date startdate;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "预计还款时间")
    @Column(name = "enddate")
    private Date enddate;
    
    @Header(name = "状态 10-新建,20-部门审核,30-部门驳回,40-领导审核,50-领导驳回,60-财务放款,70-完成,80-已还款")
    @Column(name = "status")
    private String status;
    
    @Header(name = "借款金额")
    @Column(name = "money")
    private String money;
    
    @Header(name = "是否记账")
    @Column(name = "isjz")
    private String isjz;
    
    @Header(name = "部门意见")
    @Column(name = "bmyj")
    private String bmyj;
    
    @Header(name = "部门审核人")
    @Column(name = "bmshr")
    private String bmshr;
    
    @Header(name = "领导意见")
    @Column(name = "bossyj")
    private String bossyj;
    
    @Header(name = "审核领导")
    @Column(name = "boss")
    private String boss;
    
    @Header(name = "是否还款")
    @Column(name = "ishk")
    private String ishk;
    
    @Header(name = "事由")
    @Column(name = "content")
    private String content;
    
    @Header(name = "部门")
    @Column(name = "dept")
    private String dept;
    
    @Header(name = "proid")
    @Column(name = "proid")
    private String proid;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getLoanerid() {
		return loanerid;
	}

	public void setLoanerid(String loanerid) {
		this.loanerid = loanerid;
	}

	public String getLoanname() {
		return loanname;
	}

	public void setLoanname(String loanname) {
		this.loanname = loanname;
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

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getIsjz() {
		return isjz;
	}

	public void setIsjz(String isjz) {
		this.isjz = isjz;
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

	public String getIshk() {
		return ishk;
	}

	public void setIshk(String ishk) {
		this.ishk = ishk;
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

	public String getProid() {
		return proid;
	}

	public void setProid(String proid) {
		this.proid = proid;
	}

	
    
    
}
