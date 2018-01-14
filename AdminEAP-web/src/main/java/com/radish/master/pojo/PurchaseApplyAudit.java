/**
 * 
 */
package com.radish.master.pojo;

import java.io.Serializable;

/**
 * @author tonyd
 *
 */
public class PurchaseApplyAudit implements Serializable{

	private static final long serialVersionUID = -804124931262764039L;
	
	private String id;
	private String budget_no;
	private String region_name;
	private String mat_number;
	private String mat_name;
	private String mat_standard;
	private String budget;
	private String cost;
	private String apply;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBudget_no() {
		return budget_no;
	}

	public void setBudget_no(String budget_no) {
		this.budget_no = budget_no;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public String getMat_number() {
		return mat_number;
	}

	public void setMat_number(String mat_number) {
		this.mat_number = mat_number;
	}

	public String getMat_name() {
		return mat_name;
	}

	public void setMat_name(String mat_name) {
		this.mat_name = mat_name;
	}

	public String getMat_standard() {
		return mat_standard;
	}

	public void setMat_standard(String mat_standard) {
		this.mat_standard = mat_standard;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getApply() {
		return apply;
	}

	public void setApply(String apply) {
		this.apply = apply;
	}
}
