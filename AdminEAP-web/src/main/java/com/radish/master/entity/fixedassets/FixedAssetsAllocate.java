/**
 * 
 */
package com.radish.master.entity.fixedassets;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * @author tonyd
 *
 */
@Entity
@Table(name = "tbl_fixedassets_allocate")
public class FixedAssetsAllocate extends BaseEntity {

	private static final long serialVersionUID = -1227578279730907589L;

	@Header(name = "库存ID")
	@Column(name = "stk_id")
	private String stkID;

	@Header(name = "名称")
	@Column(name = "name")
	private String name;

	@Header(name = "英文名称")
	@Column(name = "english_name")
	private String englishName;

	@Header(name = "型号")
	@Column(name = "model")
	private String model;

	@Header(name = "来源库")
	@Column(name = "source_stock")
	private String sourceStock;

	@Header(name = "来源库管理员")
	@Column(name = "source_manager")
	private String sourceManager;

	@Header(name = "来源库管理员名字")
	@Column(name = "source_manager_name")
	private String sourceManagerName;

	@Header(name = "目标库")
	@Column(name = "target_stock")
	private String targetStock;

	@Header(name = "目标库管理员")
	@Column(name = "target_manager")
	private String targetManager;

	@Header(name = "目标库管理员名字")
	@Column(name = "target_manager_name")
	private String targetManagerName;

	@Header(name = "规格")
	@Column(name = "norm")
	private String norm;

	@Header(name = "单位")
	@Column(name = "unit")
	private String unit;

	@Header(name = "单价")
	@Column(name = "price")
	private String price;

	@Header(name = "数量")
	@Column(name = "quantity")
	private String quantity;

	@Header(name = "生产厂商")
	@Column(name = "vendor")
	private String vendor;

	/**
	 * 10=固定资产 20=器具、工具 30=办公用品
	 */
	@Header(name = "单据类型")
	@Column(name = "fa_type")
	private String faType;

	/**
	 * 10=新建 20=审核通过 30=审核通过（需归还） 40=归还申请 50=已归还 60=审核不通过
	 */
	@Header(name = "状态")
	@Column(name = "status")
	private String status;

	@Header(name = "运输车辆")
	@Column(name = "vehicle")
	private String vehicle;

	@Header(name = "司机")
	@Column(name = "driver")
	private String driver;

	@Header(name = "调拨人")
	@Column(name = "operator")
	private String operator;

	@Header(name = "编号")
	@Column(name = "number")
	private String number;
	
	@Header(name = "设备状况描述")
	@Column(name = "sbzk")
	private String sbzk;
	
	@Header(name = "调拨原因")
	@Column(name = "dbyy")
	private String dbyy;
	
	@Header(name = "备注")
	@Column(name = "note")
	private String note;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "开始时间")
    @Column(name = "startDate")
    private Date startDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "结束时间")
    @Column(name = "endDate")
    private Date endDate;
	
	public String getStkID() {
		return stkID;
	}

	public void setStkID(String stkID) {
		this.stkID = stkID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSourceStock() {
		return sourceStock;
	}

	public void setSourceStock(String sourceStock) {
		this.sourceStock = sourceStock;
	}

	public String getSourceManager() {
		return sourceManager;
	}

	public void setSourceManager(String sourceManager) {
		this.sourceManager = sourceManager;
	}

	public String getSourceManagerName() {
		return sourceManagerName;
	}

	public void setSourceManagerName(String sourceManagerName) {
		this.sourceManagerName = sourceManagerName;
	}

	public String getTargetStock() {
		return targetStock;
	}

	public void setTargetStock(String targetStock) {
		this.targetStock = targetStock;
	}

	public String getTargetManager() {
		return targetManager;
	}

	public void setTargetManager(String targetManager) {
		this.targetManager = targetManager;
	}

	public String getTargetManagerName() {
		return targetManagerName;
	}

	public void setTargetManagerName(String targetManagerName) {
		this.targetManagerName = targetManagerName;
	}

	public String getNorm() {
		return norm;
	}

	public void setNorm(String norm) {
		this.norm = norm;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getFaType() {
		return faType;
	}

	public void setFaType(String faType) {
		this.faType = faType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSbzk() {
		return sbzk;
	}

	public void setSbzk(String sbzk) {
		this.sbzk = sbzk;
	}

	public String getDbyy() {
		return dbyy;
	}

	public void setDbyy(String dbyy) {
		this.dbyy = dbyy;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
