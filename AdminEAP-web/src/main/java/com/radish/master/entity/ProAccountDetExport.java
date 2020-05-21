/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "tbl_projectAccount_det")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class ProAccountDetExport implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    protected String id;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "账目时间")
    @Column(name = "createDate")
    private Date createDate;
    
    
    @Header(name = "摘要")
    @Column(name = "abstracts")
    private String abstracts;
    
    
    @Header(name = "收入")
    @Column(name = "inMoney")
    private String inMoney;
    
    @Header(name = "支出")
    @Column(name = "outMoney")
    private String outMoney;
    
    
    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;

    
    @Header(name = "备用审核状态")
    @Column(name = "status")
    private String status;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public String getAbstracts() {
		return abstracts;
	}


	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}


	public String getInMoney() {
		return inMoney;
	}


	public void setInMoney(String inMoney) {
		this.inMoney = inMoney;
	}


	public String getOutMoney() {
		return outMoney;
	}


	public void setOutMoney(String outMoney) {
		this.outMoney = outMoney;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
    
	
}
