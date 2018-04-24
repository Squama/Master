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

    @Header(name = "申报机械费")
    @Column(name = "apply_mech")
    private String applyMech;

    @Header(name = "申报人工费")
    @Column(name = "apply_labour")
    private String applyLabour;

    @Header(name = "申报材料费")
    @Column(name = "apply_mat")
    private String applyMat;

    @Header(name = "申报扣款")
    @Column(name = "apply_debit")
    private String applyDebit;

    @Header(name = "申报小计")
    @Column(name = "apply_sub")
    private String applySub;

    @Header(name = "经营科审核机械费")
    @Column(name = "business_mech")
    private String businessMech;

    @Header(name = "经营科审核人工费")
    @Column(name = "business_labour")
    private String businessLabour;

    @Header(name = "经营科审核材料费")
    @Column(name = "business_mat")
    private String businessMat;

    @Header(name = "经营科审核扣款")
    @Column(name = "business_debit")
    private String businessDebit;

    @Header(name = "经营科审核小计")
    @Column(name = "business_sub")
    private String businessSub;

    @Header(name = "财务审核机械费")
    @Column(name = "final_mech")
    private String finalMech;

    @Header(name = "财务审核人工费")
    @Column(name = "final_labour")
    private String finalLabour;

    @Header(name = "财务审核材料费")
    @Column(name = "final_mat")
    private String finalMat;

    @Header(name = "财务审核扣款")
    @Column(name = "final_debit")
    private String finalDebit;

    @Header(name = "财务审核小计")
    @Column(name = "final_sub")
    private String finalSub;

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

    public String getApplyMech() {
        return applyMech;
    }

    public void setApplyMech(String applyMech) {
        this.applyMech = applyMech;
    }

    public String getApplyLabour() {
        return applyLabour;
    }

    public void setApplyLabour(String applyLabour) {
        this.applyLabour = applyLabour;
    }

    public String getApplyMat() {
        return applyMat;
    }

    public void setApplyMat(String applyMat) {
        this.applyMat = applyMat;
    }

    public String getApplyDebit() {
        return applyDebit;
    }

    public void setApplyDebit(String applyDebit) {
        this.applyDebit = applyDebit;
    }

    public String getApplySub() {
        return applySub;
    }

    public void setApplySub(String applySub) {
        this.applySub = applySub;
    }

    public String getBusinessMech() {
        return businessMech;
    }

    public void setBusinessMech(String businessMech) {
        this.businessMech = businessMech;
    }

    public String getBusinessLabour() {
        return businessLabour;
    }

    public void setBusinessLabour(String businessLabour) {
        this.businessLabour = businessLabour;
    }

    public String getBusinessMat() {
        return businessMat;
    }

    public void setBusinessMat(String businessMat) {
        this.businessMat = businessMat;
    }

    public String getBusinessDebit() {
        return businessDebit;
    }

    public void setBusinessDebit(String businessDebit) {
        this.businessDebit = businessDebit;
    }

    public String getBusinessSub() {
        return businessSub;
    }

    public void setBusinessSub(String businessSub) {
        this.businessSub = businessSub;
    }

    public String getFinalMech() {
        return finalMech;
    }

    public void setFinalMech(String finalMech) {
        this.finalMech = finalMech;
    }

    public String getFinalLabour() {
        return finalLabour;
    }

    public void setFinalLabour(String finalLabour) {
        this.finalLabour = finalLabour;
    }

    public String getFinalMat() {
        return finalMat;
    }

    public void setFinalMat(String finalMat) {
        this.finalMat = finalMat;
    }

    public String getFinalDebit() {
        return finalDebit;
    }

    public void setFinalDebit(String finalDebit) {
        this.finalDebit = finalDebit;
    }

    public String getFinalSub() {
        return finalSub;
    }

    public void setFinalSub(String finalSub) {
        this.finalSub = finalSub;
    }

}
