package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tbl_stock_history")
public class StockHistory extends BaseEntity {


    private static final long serialVersionUID = -137060175090754566L;

    @Header(name = "原项目库ID")
    @Column(name = "project_id")
    private String project_id;


    @Header(name = "物料编号")
    @Column(name = "mat_id")
    private String mat_id;

    @Header(name = "变更数量")
    @Column(name = "stock_change_num")
    private Double stock_change_num;

    @Header(name = "操作类型")
    @Column(name = "usetpye")
    private String usetpye;

    @Header(name = "库存操作数据源")
    @Column(name = "operation_bill_ID")
    private String operation_bill_ID;

    @Header(name = "操作人员ID")
    @Column(name = "operation_person_id")
    private String operation_person_id;

    @Header(name = "操作时间")
    @Column(name = "operation_time")
    private Date operation_time;

    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;

    @Header(name = "预留字段1")
    @Column(name = "reserve1")
    private String reserve1;
    @Header(name = "预留字段2")
    @Column(name = "reserve2")
    private String reserve2;
    @Header(name = "预留字段3")
    @Column(name = "reserve3")
    private String reserve3;


    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }


    public String getMat_id() {
        return mat_id;
    }

    public void setMat_id(String mat_id) {
        this.mat_id = mat_id;
    }

    public Double getStock_change_num() {
        return stock_change_num;
    }

    public void setStock_change_num(Double stock_change_num) {
        this.stock_change_num = stock_change_num;
    }

    public String getUsetpye() {
        return usetpye;
    }

    public void setUsetpye(String usetpye) {
        this.usetpye = usetpye;
    }

    public String getOperation_person_id() {
        return operation_person_id;
    }

    public void setOperation_person_id(String operation_person_id) {
        this.operation_person_id = operation_person_id;
    }

    public Date getOperation_time() {
        return operation_time;
    }

    public void setOperation_time(Date operation_time) {
        this.operation_time = operation_time;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperation_bill_ID() {
        return operation_bill_ID;
    }

    public void setOperation_bill_ID(String operation_bill_ID) {
        this.operation_bill_ID = operation_bill_ID;
    }
}
