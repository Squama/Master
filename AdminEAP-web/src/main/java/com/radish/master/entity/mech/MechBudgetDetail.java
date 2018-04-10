/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.entity.mech;

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
* dongyan      2018年4月8日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_mech_budget_detail")
public class MechBudgetDetail extends BaseEntity {

    private static final long serialVersionUID = 1272162618122289897L;

    @Header(name = "所属项目编码")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "所属项目名称")
    @Column(name = "project_name")
    private String projectName;

    @Header(name = "条目编码")
    @Column(name = "entry_id")
    private String entryID;

    @Header(name = "编码")
    @Column(name = "code")
    private String code;

    @Header(name = "类别")
    @Column(name = "type")
    private String type;

    @Header(name = "名称")
    @Column(name = "name")
    private String name;

    @Header(name = "规格型号")
    @Column(name = "modal")
    private String modal;

    @Header(name = "单位")
    @Column(name = "unit")
    private String unit;

    @Header(name = "机械台班")
    @Column(name = "quantity")
    private String quantity;

    @Header(name = "预算价")
    @Column(name = "budget_price")
    private String budgetPrice;

    @Header(name = "状态")
    @Column(name = "status")
    private String status;

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

    public String getEntryID() {
        return entryID;
    }

    public void setEntryID(String entryID) {
        this.entryID = entryID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModal() {
        return modal;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBudgetPrice() {
        return budgetPrice;
    }

    public void setBudgetPrice(String budgetPrice) {
        this.budgetPrice = budgetPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
