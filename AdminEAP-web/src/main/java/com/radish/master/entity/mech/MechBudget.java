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
@Table(name = "tbl_mech_budget")
public class MechBudget extends BaseEntity {

    private static final long serialVersionUID = 773238830889768811L;

    @Header(name = "所属项目编码")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "所属项目名称")
    @Column(name = "project_name")
    private String projectName;

    @Header(name = "编码")
    @Column(name = "code")
    private String code;

    @Header(name = "名称")
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
