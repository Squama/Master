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
public class FixedAssetsPur extends BaseEntity{

    private static final long serialVersionUID = -8219170334393570630L;

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
    @Column(name = "use")
    private String use;

    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;

    @Header(name = "采购合同编号")
    @Column(name = "file_id")
    private String fileID;

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

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
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

}
