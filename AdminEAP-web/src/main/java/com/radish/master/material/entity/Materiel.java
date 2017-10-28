/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.material.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* wangzhihao      2017年10月28日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_materiel")
public class Materiel {

    private static final long serialVersionUID = -4535904106336743574L;
    /**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;

    
    @Header(name = "物料编号")
    @Column(name = "mat_number")
    private String mat_number;

    @Header(name = "物料规格")
    @Column(name = "mat_standard")
    private String mat_standard;
    
    @Header(name = "物料名称")
    @Column(name = "mat_name")
    private String mat_name;
    
    @Header(name = "单位")
    @Column(name = "unit")
    private Date unit;

    @Header(name = "有效标识")
    @Column(name = "isValid")
    private String isValid;

    @Header(name = "创建人")
    @Column(name = "create_name")
    private String create_name;

    @Header(name = "创建人id")
    @Column(name = "create_name_ID")
    private String create_name_ID;

    @Header(name = "创建时间")
    @Column(name = "create_time")
    private String create_time;

    @Header(name = "修改人")
    @Column(name = "update_name")
    private String update_name;
    
    @Header(name = "修改人id")
    @Column(name = "update_name_ID")
    private String update_name_ID;

    @Header(name = "修改时间")
    @Column(name = "update_time")
    private String update_time;

    @Header(name = "上级节点ID")
    @Column(name = "parent_ID")
    private String parent_ID;

    @Header(name = "类型")
    @Column(name = "type")
    private String type;
    
    @Header(name = "预留字段1")
    @Column(name = "reserve1")
    private String reserve1;

    @Header(name = "预留字段1")
    @Column(name = "reserve2")
    private String reserve2;

    @Header(name = "预留字段1")
    @Column(name = "reserve3")
    private String reserve3;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMat_number() {
		return mat_number;
	}

	public void setMat_number(String mat_number) {
		this.mat_number = mat_number;
	}

	public String getMat_standard() {
		return mat_standard;
	}

	public void setMat_standard(String mat_standard) {
		this.mat_standard = mat_standard;
	}

	public Date getUnit() {
		return unit;
	}

	public void setUnit(Date unit) {
		this.unit = unit;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getCreate_name() {
		return create_name;
	}

	public void setCreate_name(String create_name) {
		this.create_name = create_name;
	}

	public String getCreate_name_ID() {
		return create_name_ID;
	}

	public void setCreate_name_ID(String create_name_ID) {
		this.create_name_ID = create_name_ID;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_name() {
		return update_name;
	}

	public void setUpdate_name(String update_name) {
		this.update_name = update_name;
	}

	public String getUpdate_name_ID() {
		return update_name_ID;
	}

	public void setUpdate_name_ID(String update_name_ID) {
		this.update_name_ID = update_name_ID;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getParent_ID() {
		return parent_ID;
	}

	public void setParent_ID(String parent_ID) {
		this.parent_ID = parent_ID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getMat_name() {
		return mat_name;
	}

	public void setMat_name(String mat_name) {
		this.mat_name = mat_name;
	}
    
   
    
}
