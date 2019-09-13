/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.cnpc.framework.annotation.Header;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年12月4日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_budget_import")
public class BudgetImport {

	@Id
	@GenericGenerator(name = "id", strategy = "uuid")
	@GeneratedValue(generator = "id")
	@Column(name = "id", length = 36)
	private String id;

	@Header(name = "定额编码")
	@Column(name = "quota_no")
	private String quotaNo;

	@Header(name = "子目名称")
	@Column(name = "quota_name")
	private String quotaName;

	@Header(name = "单位")
	@Column(name = "units")
	private String units;

	@Header(name = "工程量")
	@Column(name = "quantities")
	private String quantities;

	@Header(name = "单价")
	@Column(name = "price")
	private String price;

	@Header(name = "人工单价")
	@Column(name = "labour_single")
	private String labourSingle;

	@Header(name = "材料单价")
	@Column(name = "mat_single")
	private String matSingle;

	@Header(name = "机械单价")
	@Column(name = "mech_single")
	private String mechSingle;

	@Header(name = "主材单价")
	@Column(name = "main_single")
	private String mainSingle;

	@Header(name = "合价")
	@Column(name = "unit_price")
	private String unitPrice;

	@Header(name = "合价（人工）")
	@Column(name = "artificiality")
	private String artificiality;

	@Header(name = "合价（材料）")
	@Column(name = "materiels")
	private String materiels;

	@Header(name = "合价（机械）")
	@Column(name = "mech")
	private String mech;

	@Header(name = "主材合价")
	@Column(name = "materiels_unit_price")
	private String materielsUnitPrice;

	@Header(name = "分组")
	@Column(name = "quota_group")
	private String quotaGroup;

	@Header(name = "项目")
	@Column(name = "project_id")
	private String projectID;

	@Header(name = "预算")
	@Column(name = "budget_no")
	private String budgetNo;

	@Header(name = "拆分合并标志")
	@Column(name = "order_no")
	private String orderNo;

	@Header(name = "行高标志")
	@Column(name = "col")
	private String col;

	@Header(name = "是否为分组")
	@Column(name = "is_group")
	private String isGroup;

	@Header(name = "是否标记为包工包料")
	@Column(name = "is_pack")
	private String isPack;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuotaNo() {
		return quotaNo;
	}

	public void setQuotaNo(String quotaNo) {
		this.quotaNo = quotaNo;
	}

	public String getQuotaName() {
		return quotaName;
	}

	public void setQuotaName(String quotaName) {
		this.quotaName = quotaName;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getArtificiality() {
		return artificiality;
	}

	public void setArtificiality(String artificiality) {
		this.artificiality = artificiality;
	}

	public String getMateriels() {
		return materiels;
	}

	public void setMateriels(String materiels) {
		this.materiels = materiels;
	}

	public String getMech() {
		return mech;
	}

	public void setMech(String mech) {
		this.mech = mech;
	}

	public String getMaterielsUnitPrice() {
		return materielsUnitPrice;
	}

	public void setMaterielsUnitPrice(String materielsUnitPrice) {
		this.materielsUnitPrice = materielsUnitPrice;
	}

	public String getQuotaGroup() {
		return quotaGroup;
	}

	public void setQuotaGroup(String quotaGroup) {
		this.quotaGroup = quotaGroup;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public String getBudgetNo() {
		return budgetNo;
	}

	public void setBudgetNo(String budgetNo) {
		this.budgetNo = budgetNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public String getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}

	public String getIsPack() {
		return isPack;
	}

	public void setIsPack(String isPack) {
		this.isPack = isPack;
	}

	public String getLabourSingle() {
		return labourSingle;
	}

	public void setLabourSingle(String labourSingle) {
		this.labourSingle = labourSingle;
	}

	public String getMatSingle() {
		return matSingle;
	}

	public void setMatSingle(String matSingle) {
		this.matSingle = matSingle;
	}

	public String getMechSingle() {
		return mechSingle;
	}

	public void setMechSingle(String mechSingle) {
		this.mechSingle = mechSingle;
	}

	public String getMainSingle() {
		return mainSingle;
	}

	public void setMainSingle(String mainSingle) {
		this.mainSingle = mainSingle;
	}

}
