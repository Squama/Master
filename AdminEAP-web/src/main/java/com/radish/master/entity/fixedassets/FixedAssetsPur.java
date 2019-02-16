/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.entity.fixedassets;

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
* dongyan      2019年2月12日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_fixedassets_pur")
public class FixedAssetsPur extends BaseEntity {

    private static final long serialVersionUID = -8219170334393570630L;

    @Header(name = "请购单名称")
    @Column(name = "name")
    private String name;

    @Header(name = "保管部门ID")
    @Column(name = "dept_id")
    private String deptID;

    @Header(name = "保管部门名称")
    @Column(name = "dept_name")
    private String deptName;

    @Header(name = "请购编号")
    @Column(name = "pur_no")
    private String purNo;

    @Header(name = "所属库存")
    @Column(name = "stock")
    private String stock;

    @Header(name = "购买原因")
    @Column(name = "reason")
    private String reason;

    @Header(name = "购买用途")
    @Column(name = "purpose")
    private String purpose;

    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;

    @Header(name = "采购合同编号")
    @Column(name = "file_id")
    private String fileID;

    @Header(name = "状态")
    @Column(name = "status")
    private String status;

    @Header(name = "申请人")
    @Column(name = "applyer")
    private String applyer;

    /**
     * 10=固定资产 20=器具、工具 30=办公用品
     */
    @Header(name = "单据类型")
    @Column(name = "fa_type")
    private String faType;

    public String getDeptID() {
        return deptID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPurNo() {
        return purNo;
    }

    public void setPurNo(String purNo) {
        this.purNo = purNo;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public String getFaType() {
        return faType;
    }

    public void setFaType(String faType) {
        this.faType = faType;
    }

}
