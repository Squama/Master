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
public class BudgetCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 private String budget_no;
    private String project_name;
    
    private String project_sub_name;
    private String budget_name;
   
    private String project_id;
    private String project_sub_id;
    private Double drhj;
    private Double drrg;
    private Double drcl;
    private Double drjx;
    private Double drzc;
    private Double cshj;
    private Double csrg;
    private Double cscl;
    private Double csjx;
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getProject_sub_name() {
		return project_sub_name;
	}
	public void setProject_sub_name(String project_sub_name) {
		this.project_sub_name = project_sub_name;
	}
	public String getBudget_name() {
		return budget_name;
	}
	public void setBudget_name(String budget_name) {
		this.budget_name = budget_name;
	}
	public String getBudget_no() {
		return budget_no;
	}
	public void setBudget_no(String budget_no) {
		this.budget_no = budget_no;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getProject_sub_id() {
		return project_sub_id;
	}
	public void setProject_sub_id(String project_sub_id) {
		this.project_sub_id = project_sub_id;
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
    
}
