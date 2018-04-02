/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年3月5日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_project_volume")
public class ProjectVolume extends BaseEntity {

    private static final long serialVersionUID = 5693322660504020063L;

    @Header(name = "项目ID")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "项目名称")
    @Column(name = "project_name")
    private String projectName;

    @Header(name = "施工开始时间")
    @Column(name = "start_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @Header(name = "施工结束时间")
    @Column(name = "end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @Header(name = "劳务合同ID")
    @Column(name = "labor_id")
    private String laborID;

    @Header(name = "本次应付工程款")
    @Column(name = "engineer_money")
    private String engineerMoney;

    @Header(name = "工程量")
    @Column(name = "volume")
    private String volume;

    /**
     * 10-提交审核 20-综合审核 30-施工负责人 40-预算负责人 50-总负责人 60-财务负责人 70-已完成
     */
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLaborID() {
        return laborID;
    }

    public void setLaborID(String laborID) {
        this.laborID = laborID;
    }

    public String getEngineerMoney() {
        return engineerMoney;
    }

    public void setEngineerMoney(String engineerMoney) {
        this.engineerMoney = engineerMoney;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
