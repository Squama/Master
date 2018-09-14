package com.radish.master.entity.volumePay;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//报销表

@Entity
@Table(name ="tbl_reimburse")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Reimburse implements Serializable {

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
    
    @Header(name = "部门内码")
    @Column(name = "pid")
    private String pid;
    
    @Header(name = "报销款来源 10-项目款、20-公司款")
    @Column(name = "type")
    private String type;
    
    @Header(name = "编号")
    @Column(name = "number")
    private String number;
    
    @Header(name = "报销人id")
    @Column(name = "reerid")
    private String reerid;
    
    @Header(name = "报销人名称")
    @Column(name = "reername")
    private String reername;
    
    @Header(name = "报销时间")
    @Column(name = "bxdate")
    private Date bxdate;
    
    @Header(name = "状态 10-新建,20-部门审核,30-部门驳回,40-财务核对,50-财务驳回,60-领导审核,70-领导驳回,80-财务放款,90-完成")
    @Column(name = "status")
    private String status;
    
    @Header(name = "报销金额")
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
    
    @Header(name = "领导意见")
    @Column(name = "shcwyj")
    private String shcwyj;
    
    @Header(name = "审核领导")
    @Column(name = "shcw")
    private String shcw;
    
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

	public String getReerid() {
		return reerid;
	}

	public void setReerid(String reerid) {
		this.reerid = reerid;
	}

	public String getReername() {
		return reername;
	}

	public void setReername(String reername) {
		this.reername = reername;
	}

	public Date getBxdate() {
		return bxdate;
	}

	public void setBxdate(Date bxdate) {
		this.bxdate = bxdate;
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

	public String getShcwyj() {
		return shcwyj;
	}

	public void setShcwyj(String shcwyj) {
		this.shcwyj = shcwyj;
	}

	public String getShcw() {
		return shcw;
	}

	public void setShcw(String shcw) {
		this.shcw = shcw;
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
