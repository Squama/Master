/**
 * 
 */
package com.radish.master.entity.project;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * @author tonyd
 *
 */
@Entity
@Table(name = "tbl_measure_consume")
public class MeasureConsume extends BaseEntity {

    private static final long serialVersionUID = -1015154647867433617L;

    @Header(name = "所属项目编码")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "所属项目子项编码")
    @Column(name = "project_sub_id")
    private String projectSubID;

    @Header(name = "消耗类型")
    @Column(name = "type")
    private String type;

    @Header(name = "消耗金额")
    @Column(name = "amount")
    private String amount;

    @Header(name = "操作")
    @Column(name = "op")
    private String op;

    @Header(name = "变更人")
    @Column(name = "operator")
    private String operator;

    @Column(name = "operate_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private Date operateTime;

    @Header(name = "变更说明")
    @Column(name = "consume_name")
    private String consumeName;

    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectSubID() {
        return projectSubID;
    }

    public void setProjectSubID(String projectSubID) {
        this.projectSubID = projectSubID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getConsumeName() {
        return consumeName;
    }

    public void setConsumeName(String consumeName) {
        this.consumeName = consumeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

}
