package com.radish.master.entity.lawWorks;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author 案件相关人员  10- 原告 20-被告
 * @创建时间 2018年11月11日 
 * @return
 */
@Entity
@Table(name ="tbl_casepeople")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class CasePeople implements Serializable {

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
    private String id;
    
    @Header(name = "案件id")
    @Column(name = "caseid")
    private String caseid;

    @Header(name = "名称")
    @Column(name = "name")
    private String name;
    
    @Header(name = "人员类型 10-原告，20-被告")
    @Column(name = "pType")
    private String pType;
    
    @Header(name = "电话")
    @Column(name = "Tel")
    private String tel;
    
    @Header(name = "证件号")
    @Column(name = "idcode")
    private String idcode;
    
    @Header(name = "地址")
    @Column(name = "address")
    private String address;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getpType() {
		return pType;
	}

	public void setpType(String pType) {
		this.pType = pType;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getIdcode() {
		return idcode;
	}

	public void setIdcode(String idcode) {
		this.idcode = idcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
    
    
}
