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

    @Header(name = "创建时间")
    @Column(name = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @Header(name = "劳务合同ID")
    @Column(name = "labor_id")
    private String laborID;

    @Header(name = "项目子项ID")
    @Column(name = "project_sub_id")
    private String projectSubID;

    @Header(name = "包工包料ID")
    @Column(name = "package_detail_id")
    private String packageDetailID;

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

    @Header(name = "申报包工包料")
    @Column(name = "apply_pack")
    private String applyPack;

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

    @Header(name = "经营科审核包工包料")
    @Column(name = "business_pack")
    private String businessPack;

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

    @Header(name = "财务审核包工包料")
    @Column(name = "final_pack")
    private String finalPack;

    @Header(name = "财务审核小计")
    @Column(name = "final_sub")
    private String finalSub;

    @Header(name = "人工费支付状态")
    @Column(name = "labourStatus")
    private String labourStatus;

    @Header(name = "材料费支付状态")
    @Column(name = "matStatus")
    private String matStatus;

    @Header(name = "机械费支付状态")
    @Column(name = "mechStatus")
    private String mechStatus;

    @Header(name = "包工包料支付状态")
    @Column(name = "packStatus")
    private String packStatus;
    
    @Header(name = "总价措施项目费类别")
    @Column(name = "measure_type")
    private String measureType;
    
    @Header(name = "费用金额")
    @Column(name = "measure_amount")
    private String measureAmount;
    
    @Header(name = "点工班组")
    @Column(name = "measure_team")
    private String measureTeam;
    
    
    @Header(name = "上报类型 10=包清工,20=包工包料,30=机械")
    @Column(name = "sblx")
    private String sblx;
    
    @Header(name = "申报材料扣款")
    @Column(name = "apply_debitcl")
    private String applyDebitcl;
    
    @Header(name = "申报机械扣款")
    @Column(name = "apply_debitjx")
    private String applyDebitjx;
    
    @Header(name = "经营科材料扣款")
    @Column(name = "business_debitcl")
    private String businessDebitcl;
    
    @Header(name = "经营科材料扣款")
    @Column(name = "business_debitjx")
    private String businessDebitjx;
    
    @Header(name = "财务审材料扣款")
    @Column(name = "final_debitcl")
    private String finalDebitcl;
    
    @Header(name = "财务审机械扣款")
    @Column(name = "final_debitjx")
    private String finalDebitjx;
    
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLabourStatus() {
        return labourStatus;
    }

    public void setLabourStatus(String labourStatus) {
        this.labourStatus = labourStatus;
    }

    public String getMatStatus() {
        return matStatus;
    }

    public void setMatStatus(String matStatus) {
        this.matStatus = matStatus;
    }

    public String getMechStatus() {
        return mechStatus;
    }

    public void setMechStatus(String mechStatus) {
        this.mechStatus = mechStatus;
    }

    public String getProjectSubID() {
        return projectSubID;
    }

    public void setProjectSubID(String projectSubID) {
        this.projectSubID = projectSubID;
    }

    public String getApplyPack() {
        return applyPack;
    }

    public void setApplyPack(String applyPack) {
        this.applyPack = applyPack;
    }

    public String getBusinessPack() {
        return businessPack;
    }

    public void setBusinessPack(String businessPack) {
        this.businessPack = businessPack;
    }

    public String getFinalPack() {
        return finalPack;
    }

    public void setFinalPack(String finalPack) {
        this.finalPack = finalPack;
    }

    public String getPackStatus() {
        return packStatus;
    }

    public void setPackStatus(String packStatus) {
        this.packStatus = packStatus;
    }

    public String getPackageDetailID() {
        return packageDetailID;
    }

    public void setPackageDetailID(String packageDetailID) {
        this.packageDetailID = packageDetailID;
    }

	public String getMeasureType() {
		return measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	public String getMeasureAmount() {
		return measureAmount;
	}

	public void setMeasureAmount(String measureAmount) {
		this.measureAmount = measureAmount;
	}

	public String getMeasureTeam() {
		return measureTeam;
	}

	public void setMeasureTeam(String measureTeam) {
		this.measureTeam = measureTeam;
	}

	public String getSblx() {
		return sblx;
	}

	public void setSblx(String sblx) {
		this.sblx = sblx;
	}

	public String getApplyDebitcl() {
		return applyDebitcl;
	}

	public void setApplyDebitcl(String applyDebitcl) {
		this.applyDebitcl = applyDebitcl;
	}

	public String getApplyDebitjx() {
		return applyDebitjx;
	}

	public void setApplyDebitjx(String applyDebitjx) {
		this.applyDebitjx = applyDebitjx;
	}

	public String getBusinessDebitcl() {
		return businessDebitcl;
	}

	public void setBusinessDebitcl(String businessDebitcl) {
		this.businessDebitcl = businessDebitcl;
	}

	public String getBusinessDebitjx() {
		return businessDebitjx;
	}

	public void setBusinessDebitjx(String businessDebitjx) {
		this.businessDebitjx = businessDebitjx;
	}

	public String getFinalDebitcl() {
		return finalDebitcl;
	}

	public void setFinalDebitcl(String finalDebitcl) {
		this.finalDebitcl = finalDebitcl;
	}

	public String getFinalDebitjx() {
		return finalDebitjx;
	}

	public void setFinalDebitjx(String finalDebitjx) {
		this.finalDebitjx = finalDebitjx;
	}

}
