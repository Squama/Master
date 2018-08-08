package com.radish.master.pojo;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//材料出入库调度视图

@Entity
public class MatInAndOut implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String projectId;
	private Date rq;
    private String number;
    private String matNumber;
    private String matName;
    private String matStandard;
    private String unit;
    private String rkl;
    private String price;
    private String type;
    private String bz;
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Date getRq() {
		return rq;
	}
	public void setRq(Date rq) {
		this.rq = rq;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMatNumber() {
		return matNumber;
	}
	public void setMatNumber(String matNumber) {
		this.matNumber = matNumber;
	}
	public String getMatName() {
		return matName;
	}
	public void setMatName(String matName) {
		this.matName = matName;
	}
	public String getMatStandard() {
		return matStandard;
	}
	public void setMatStandard(String matStandard) {
		this.matStandard = matStandard;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getRkl() {
		return rkl;
	}
	public void setRkl(String rkl) {
		this.rkl = rkl;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
    
    
}
