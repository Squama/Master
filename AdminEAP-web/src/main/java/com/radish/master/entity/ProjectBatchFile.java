/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司  
 */
package com.radish.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.base.entity.BaseEntity;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年11月2日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_project_batchfile")
public class ProjectBatchFile extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -360627417601064187L;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "operator")
    private String operator;

    @Column(name = "remark")
    private String remark;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
