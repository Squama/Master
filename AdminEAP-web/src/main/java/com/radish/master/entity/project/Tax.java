/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.entity.project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年11月15日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_tax")
public class Tax extends BaseEntity {

    private static final long serialVersionUID = 4307992817853724890L;

    @Header(name = "项目ID")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "项目名称")
    @Column(name = "project_name")
    private String projectName;

    @Header(name = "子项ID")
    @Column(name = "project_sub_id")
    private String projectSubID;

    @Header(name = "子项名称")
    @Column(name = "project_sub_name")
    private String projectSubName;

    @Header(name = "税金名称")
    @Column(name = "name")
    private String name;

    @Header(name = "金额")
    @Column(name = "amount")
    private String amount;

    @Header(name = "状态")
    @Column(name = "status")
    private String status;
    
    @Header(name = "是否记账")
    @Column(name = "isjz")
    private String isjz;
    
    @Header(name = "支付对象")
    @Column(name = "payObj")
    private String payObj;

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSubID() {
        return projectSubID;
    }

    public void setProjectSubID(String projectSubID) {
        this.projectSubID = projectSubID;
    }

    public String getProjectSubName() {
        return projectSubName;
    }

    public void setProjectSubName(String projectSubName) {
        this.projectSubName = projectSubName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getIsjz() {
		return isjz;
	}

	public void setIsjz(String isjz) {
		this.isjz = isjz;
	}

	public String getPayObj() {
		return payObj;
	}

	public void setPayObj(String payObj) {
		this.payObj = payObj;
	}

}
