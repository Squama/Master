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
* dongyan      2018年5月14日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_labor_sub")
public class LaborSub extends BaseEntity {

    private static final long serialVersionUID = 7138833085931816467L;

    @Header(name = "劳务合同ID")
    @Column(name = "labor_id")
    private String laborID;

    @Header(name = "劳务合同名称")
    @Column(name = "contract_name")
    private String contractName;

    @Header(name = "分项名称")
    @Column(name = "sub_name")
    private String subName;

    @Header(name = "分项总价")
    @Column(name = "sub_price")
    private String subPrice;

    @Header(name = "分项已消耗总价")
    @Column(name = "consume_price")
    private String consumePrice;

    @Header(name = "分项施工员ID")
    @Column(name = "construction_worker_id")
    private String constructionWorkerID;

    @Header(name = "分项施工员")
    @Column(name = "construction_worker_name")
    private String constructionWorkerName;

    @Header(name = "分项机械费")
    @Column(name = "mech_price")
    private String mechPrice;

    @Header(name = "分项人工费")
    @Column(name = "labour_price")
    private String labourPrice;

    @Header(name = "分项材料费")
    @Column(name = "mat_price")
    private String matPrice;

    @Header(name = "分项包工包料费用")
    @Column(name = "package_price")
    private String packagePrice;

    public String getLaborID() {
        return laborID;
    }

    public void setLaborID(String laborID) {
        this.laborID = laborID;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getSubPrice() {
        return subPrice;
    }

    public void setSubPrice(String subPrice) {
        this.subPrice = subPrice;
    }

    public String getConsumePrice() {
        return consumePrice;
    }

    public void setConsumePrice(String consumePrice) {
        this.consumePrice = consumePrice;
    }

    public String getConstructionWorkerID() {
        return constructionWorkerID;
    }

    public void setConstructionWorkerID(String constructionWorkerID) {
        this.constructionWorkerID = constructionWorkerID;
    }

    public String getConstructionWorkerName() {
        return constructionWorkerName;
    }

    public void setConstructionWorkerName(String constructionWorkerName) {
        this.constructionWorkerName = constructionWorkerName;
    }

    public String getMechPrice() {
        return mechPrice;
    }

    public void setMechPrice(String mechPrice) {
        this.mechPrice = mechPrice;
    }

    public String getLabourPrice() {
        return labourPrice;
    }

    public void setLabourPrice(String labourPrice) {
        this.labourPrice = labourPrice;
    }

    public String getMatPrice() {
        return matPrice;
    }

    public void setMatPrice(String matPrice) {
        this.matPrice = matPrice;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

}
