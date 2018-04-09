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
@Table(name = "tbl_mech_consume")
public class MechConsume extends BaseEntity {

    private static final long serialVersionUID = 4567447724809764615L;

    @Header(name = "项目编码")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "项目名称")
    @Column(name = "project_name")
    private String projectName;

    @Header(name = "机械条目编码")
    @Column(name = "mech_id")
    private String mechID;

    @Header(name = "机械条目名称")
    @Column(name = "mech_name")
    private String mechName;

    @Header(name = "消耗项名称")
    @Column(name = "name")
    private String name;

    @Header(name = "操作员")
    @Column(name = "operator")
    private String operator;

    @Header(name = "操作员ID")
    @Column(name = "operator_id")
    private String operatorID;

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

    public String getMechID() {
        return mechID;
    }

    public void setMechID(String mechID) {
        this.mechID = mechID;
    }

    public String getMechName() {
        return mechName;
    }

    public void setMechName(String mechName) {
        this.mechName = mechName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
