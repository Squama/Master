package com.radish.master.entity;


import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tbl_stock")
public class Stock extends BaseEntity {

    private static final long serialVersionUID = -52018669648128138L;

    @Header(name = "预算ID")
    @Column(name = "budget_id")
    private String budget_id;

    @Header(name = "项目ID")
    @Column(name = "project_id")
    private String project_id;

    @Header(name = "物料ID")
    @Column(name = "mat_id")
    private String mat_id;

    @Header(name = "库存量")
    @Column(name = "stock_num")
    private Integer stock_num;

    @Header(name = "入库人员ID")
    @Column(name = "storage_person_id")
    private String storage_person_id;

    @Header(name = "入库时间")
    @Column(name = "storage_time")
    private Date storage_time;

    @Header(name = "操作类型")
    @Column(name = "usetype")
    private String usetype;

    @Header(name = "预留字段1")
    @Column(name = "reserve1")
    private String reserve1;

    @Header(name = "预留字段2")
    @Column(name = "reserve2")
    private String reserve2;

    @Header(name = "预留字段3")
    @Column(name = "reserve3")
    private String reserve3;



    public String getBudget_id() {
        return budget_id;
    }

    public void setBudget_id(String budget_id) {
        this.budget_id = budget_id;
    }

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

    public Integer getStock_num() {
        return stock_num;
    }

    public void setStock_num(Integer stock_num) {
        this.stock_num = stock_num;
    }

    public String getStorage_person_id() {
        return storage_person_id;
    }

    public void setStorage_person_id(String storage_person_id) {
        this.storage_person_id = storage_person_id;
    }

    public Date getStorage_time() {
        return storage_time;
    }

    public void setStorage_time(Date storage_time) {
        this.storage_time = storage_time;
    }

    public String getUsetype() {
        return usetype;
    }

    public void setUsetype(String usetype) {
        this.usetype = usetype;
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


}
