/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
@Table(name = "tbl_dutycheck")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class DutyCheck implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;
    
    @Header(name = "名字")
    @Column(name = "name")
    private String name;

    @Header(name = "考核人名字")
    @Column(name = "check_name")
    private String check_name;
    
    @Header(name = "职务")
    @Column(name = "duties")
    private String duties;
    
    @Header(name = "单位名称")
    @Column(name = "unitName")
    private String unitName;

    @Header(name = "所在工程内码")
    @Column(name = "project_id")
    private String project_id;

    @Header(name = "所在工程")
    @Column(name = "project_name")
    private String project_name;
    
    @Header(name = "有效标识")
    @Column(name = "isValid")
    private String isValid;
    
    @Header(name = "考核时间")
    @Column(name = "check_time")
    private Date check_time;
    
    @Header(name = "考核结果")
    @Column(name = "check_result")
    private String check_result;
    
    @Header(name = "奖惩")
    @Column(name = "jc")
    private String jc;
    
    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;
    
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCheck_name() {
		return check_name;
	}

	public void setCheck_name(String check_name) {
		this.check_name = check_name;
	}

	public String getDuties() {
		return duties;
	}

	public void setDuties(String duties) {
		this.duties = duties;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public Date getCheck_time() {
		return check_time;
	}

	public void setCheck_time(Date check_time) {
		this.check_time = check_time;
	}

	public String getCheck_result() {
		return check_result;
	}

	public void setCheck_result(String check_result) {
		this.check_result = check_result;
	}

	public String getJc() {
		return jc;
	}

	public void setJc(String jc) {
		this.jc = jc;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	


}
