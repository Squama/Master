package com.radish.master.entity.volumePay;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//报销明细表

@Entity
@Table(name ="tbl_reimburseDet")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class ReimburseDet implements Serializable {

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
    
    @Header(name = "报销内码")
    @Column(name = "reimburseId")
    private String reimburseId;
    
    
    @Header(name = "发票编号")
    @Column(name = "fpnumber")
    private String fpnumber;
    
    @Header(name = "发票类型")
    @Column(name = "fptype")
    private String fptype;
    
    @Header(name = "发票金额")
    @Column(name = "fpmoney")
    private String fpmoney;
    
    
    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getReimburseId() {
		return reimburseId;
	}


	public void setReimburseId(String reimburseId) {
		this.reimburseId = reimburseId;
	}



	public String getFpnumber() {
		return fpnumber;
	}


	public void setFpnumber(String fpnumber) {
		this.fpnumber = fpnumber;
	}


	public String getFptype() {
		return fptype;
	}


	public void setFptype(String fptype) {
		this.fptype = fptype;
	}


	public String getFpmoney() {
		return fpmoney;
	}


	public void setFpmoney(String fpmoney) {
		this.fpmoney = fpmoney;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
   
    
    
}
