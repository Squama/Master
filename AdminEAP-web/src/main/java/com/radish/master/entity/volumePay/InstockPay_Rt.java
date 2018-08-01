package com.radish.master.entity.volumePay;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//入库明细与供应商支付明细关系表

@Entity
@Table(name ="tbl_instock_pay")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class InstockPay_Rt implements Serializable {

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
    
    @Header(name = "入库明细Id")
    @Column(name = "instockDetId")
    private String instockDetId;

    
    @Header(name = "供应商支付明细ID")
    @Column(name = "payDetId")
    private String payDetId;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getInstockDetId() {
		return instockDetId;
	}


	public void setInstockDetId(String instockDetId) {
		this.instockDetId = instockDetId;
	}


	public String getPayDetId() {
		return payDetId;
	}


	public void setPayDetId(String payDetId) {
		this.payDetId = payDetId;
	}
    
    
    
}
