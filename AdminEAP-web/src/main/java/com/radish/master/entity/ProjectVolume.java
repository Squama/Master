/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.entity;

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
    private String startTime;

    @Header(name = "施工结束时间")
    @Column(name = "end_time")
    private String endTime;

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
     * 10-提交审核 20-质量员 30-安全员 40-分管施工员 50-施工负责人 60-预算负责人 70-总负责人 80-财务负责人 90-已完成
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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
