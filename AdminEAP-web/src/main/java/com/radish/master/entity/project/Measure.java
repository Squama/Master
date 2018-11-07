/**
 * 
 */
package com.radish.master.entity.project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * @author tonyd
 *
 */
@Entity
@Table(name = "tbl_measure")
public class Measure extends BaseEntity {

    private static final long serialVersionUID = 4805661305199812852L;

    @Header(name = "名称")
    @Column(name = "name")
    private String name;

    @Header(name = "所属项目编码")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "所属项目名称")
    @Column(name = "project_name")
    private String projectName;

    @Header(name = "所属项目子项编码")
    @Column(name = "project_sub_id")
    private String projectSubID;

    @Header(name = "所属项目子项名称")
    @Column(name = "project_sub_name")
    private String projectSubName;

    @Header(name = "安全文明施工费")
    @Column(name = "construct")
    private String construct;

    @Header(name = "总价措施项目费")
    @Column(name = "issue")
    private String issue;

    @Header(name = "管理费")
    @Column(name = "manage")
    private String manage;

    @Header(name = "规费")
    @Column(name = "rule")
    private String rule;

    @Header(name = "税金")
    @Column(name = "tax")
    private String tax;

    @Header(name = "状态")
    @Column(name = "status")
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getConstruct() {
        return construct;
    }

    public void setConstruct(String construct) {
        this.construct = construct;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getManage() {
        return manage;
    }

    public void setManage(String manage) {
        this.manage = manage;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
