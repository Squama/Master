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
* dongyan      2018年8月8日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_package_detail")
public class PackageDetail extends BaseEntity {

    private static final long serialVersionUID = -7246250637802697112L;

    @Header(name = "主表ID")
    @Column(name = "package_id")
    private String packageID;

    @Header(name = "包工包料名称")
    @Column(name = "name")
    private String name;

    @Header(name = "面积")
    @Column(name = "area")
    private String area;

    @Header(name = "单价")
    @Column(name = "price")
    private String price;

    @Header(name = "总价")
    @Column(name = "total")
    private String total;

    public String getPackageID() {
        return packageID;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
