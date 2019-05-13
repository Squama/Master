/**
 * 
 */
package com.radish.master.entity.project;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * @author tonyd
 *
 */
@Entity
@Table(name = "tbl_salary")
public class Salary extends BaseEntity {

    private static final long serialVersionUID = -3672976343399467185L;

    @Header(name = "所属项目编码")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "所属项目名称")
    @Column(name = "project_name")
    private String projectName;

    @Header(name = "班组ID")
    @Column(name = "team_id")
    private String teamID;

    @Header(name = "班组名称")
    @Column(name = "team_name")
    private String teamName;

    @Header(name = "开始时间")
    @Column(name = "start_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @Header(name = "结束时间")
    @Column(name = "end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    // 10-专业作业班组班组 20-管理人员30-点工班组40-机关人员 50-门卫机修工资表
    @Header(name = "工资单类型")
    @Column(name = "type")
    private String type;

    @Header(name = "总额")
    @Column(name = "total")
    private String total;

    @Header(name = "申报总额")
    @Column(name = "apply_sum")
    private String applySum;

    /*
     * 10-new 20-负责人审批 30-办公室主任审批 40-总经理审批 50-财务审批 60-已完成 70-审核不通过
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

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getApplySum() {
        return applySum;
    }

    public void setApplySum(String applySum) {
        this.applySum = applySum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
