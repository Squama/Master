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
public class BudgetCountDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String budget_no;
	private String budget_name;
	private String region_code;
	private String region_name;
	private String units;
	private String quantities; 
    private Double drhj;
    private Double drrg;
    private Double drcl;
    private Double drjx;
    private Double drzc;
    private Double cshj;
    private Double csrg;
    private Double cscl;
    private Double csjx;
    private String quota_group;
    private String order_no;
	public String getBudget_no() {
		return budget_no;
	}
	public void setBudget_no(String budget_no) {
		this.budget_no = budget_no;
	}
	public String getBudget_name() {
		return budget_name;
	}
	public void setBudget_name(String budget_name) {
		this.budget_name = budget_name;
	}
	public String getRegion_code() {
		return region_code;
	}
	public void setRegion_code(String region_code) {
		this.region_code = region_code;
	}
	public String getRegion_name() {
		return region_name;
	}
	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getQuantities() {
		return quantities;
	}
	public void setQuantities(String quantities) {
		this.quantities = quantities;
	}
	public Double getDrhj() {
		return drhj;
	}
	public void setDrhj(Double drhj) {
		this.drhj = drhj;
	}
	public Double getDrrg() {
		return drrg;
	}
	public void setDrrg(Double drrg) {
		this.drrg = drrg;
	}
	public Double getDrcl() {
		return drcl;
	}
	public void setDrcl(Double drcl) {
		this.drcl = drcl;
	}
	public Double getDrjx() {
		return drjx;
	}
	public void setDrjx(Double drjx) {
		this.drjx = drjx;
	}
	public Double getDrzc() {
		return drzc;
	}
	public void setDrzc(Double drzc) {
		this.drzc = drzc;
	}
	public Double getCshj() {
		return cshj;
	}
	public void setCshj(Double cshj) {
		this.cshj = cshj;
	}
	public Double getCsrg() {
		return csrg;
	}
	public void setCsrg(Double csrg) {
		this.csrg = csrg;
	}
	public Double getCscl() {
		return cscl;
	}
	public void setCscl(Double cscl) {
		this.cscl = cscl;
	}
	public Double getCsjx() {
		return csjx;
	}
	public void setCsjx(Double csjx) {
		this.csjx = csjx;
	}
	public String getQuota_group() {
		return quota_group;
	}
	public void setQuota_group(String quota_group) {
		this.quota_group = quota_group;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
    
}
