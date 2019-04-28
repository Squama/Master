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
@Table(name = "tbl_labor")
public class Labor extends BaseEntity {

    private static final long serialVersionUID = 6192545125376999377L;

    @Header(name = "项目ID")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "项目名称")
    @Column(name = "project_name")
    private String projectName;

    @Header(name = "施工班组ID")
    @Column(name = "construction_team_id")
    private String constructionTeamID;

    @Header(name = "施工班组")
    @Column(name = "construction_team")
    private String constructionTeam;

    @Header(name = "施工负责人")
    @Column(name = "construction_manager")
    private String constructionManager;

    @Header(name = "施工负责人ID")
    @Column(name = "construction_manager_id")
    private String constructionManagerID;

    @Header(name = "合同总价")
    @Column(name = "contract_price")
    private String contractPrice;

    @Header(name = "合同ID")
    @Column(name = "contract_file")
    private String contractFile;

    @Header(name = "合同名称")
    @Column(name = "contract_name")
    private String contractName;

    @Header(name = "状态")
    @Column(name = "status")
    private String status;

    @Header(name = "承包人")
    @Column(name = "contractor")
    private String contractor;

    @Header(name = "机械费")
    @Column(name = "mech_price")
    private String mechPrice;

    @Header(name = "人工费单价")
    @Column(name = "labour_sin")
    private String labourSin;
    
    @Header(name = "人工费数量")
    @Column(name = "labour_quantity")
    private String labourQuantity;
    
    @Header(name = "人工费单位")
    @Column(name = "labour_unit")
    private String labourUnit;
    
    @Header(name = "人工费")
    @Column(name = "labour_price")
    private String labourPrice;

    @Header(name = "材料费")
    @Column(name = "mat_price")
    private String matPrice;

    @Header(name = "包工包料费用")
    @Column(name = "package_price")
    private String packagePrice;

    @Header(name = "机械费消耗")
    @Column(name = "mech_consume")
    private String mechConsume;

    @Header(name = "人工费消耗")
    @Column(name = "labour_consume")
    private String labourConsume;

    @Header(name = "材料费消耗")
    @Column(name = "mat_consume")
    private String matConsume;

    @Header(name = "包工包料费用消耗")
    @Column(name = "package_consume")
    private String packageConsume;

    @Header(name = "甲方")
    @Column(name = "gander")
    private String gander;

    @Header(name = "乙方")
    @Column(name = "goose")
    private String goose;

    @Header(name = "劳务内容")
    @Column(name = "service_content")
    private String serviceContent;

    @Header(name = "承包方式")
    @Column(name = "contract_mode")
    private String contractMode;

    @Header(name = "质量目标")
    @Column(name = "quality_target")
    private String qualityTarget;

    @Header(name = "付款方式")
    @Column(name = "payment_mode")
    private String paymentMode;

    @Header(name = "质保金约定")
    @Column(name = "guarantee_money")
    private String guaranteeMoney;

    @Header(name = "纠纷处理")
    @Column(name = "issue")
    private String issue;
    
    @Header(name = "合同类型")
    @Column(name = "htlx")
    private String htlx;

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

    public String getConstructionTeam() {
        return constructionTeam;
    }

    public void setConstructionTeam(String constructionTeam) {
        this.constructionTeam = constructionTeam;
    }

    public String getConstructionManager() {
        return constructionManager;
    }

    public void setConstructionManager(String constructionManager) {
        this.constructionManager = constructionManager;
    }

    public String getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(String contractPrice) {
        this.contractPrice = contractPrice;
    }

    public String getContractFile() {
        return contractFile;
    }

    public void setContractFile(String contractFile) {
        this.contractFile = contractFile;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getConstructionManagerID() {
        return constructionManagerID;
    }

    public void setConstructionManagerID(String constructionManagerID) {
        this.constructionManagerID = constructionManagerID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConstructionTeamID() {
        return constructionTeamID;
    }

    public void setConstructionTeamID(String constructionTeamID) {
        this.constructionTeamID = constructionTeamID;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getMechPrice() {
        return mechPrice;
    }

    public void setMechPrice(String mechPrice) {
        this.mechPrice = mechPrice;
    }

    public String getLabourPrice() {
        return labourPrice;
    }

    public void setLabourPrice(String labourPrice) {
        this.labourPrice = labourPrice;
    }

    public String getMatPrice() {
        return matPrice;
    }

    public void setMatPrice(String matPrice) {
        this.matPrice = matPrice;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getMechConsume() {
        return mechConsume;
    }

    public void setMechConsume(String mechConsume) {
        this.mechConsume = mechConsume;
    }

    public String getLabourConsume() {
        return labourConsume;
    }

    public void setLabourConsume(String labourConsume) {
        this.labourConsume = labourConsume;
    }

    public String getMatConsume() {
        return matConsume;
    }

    public void setMatConsume(String matConsume) {
        this.matConsume = matConsume;
    }

    public String getPackageConsume() {
        return packageConsume;
    }

    public void setPackageConsume(String packageConsume) {
        this.packageConsume = packageConsume;
    }

    public String getGander() {
        return gander;
    }

    public void setGander(String gander) {
        this.gander = gander;
    }

    public String getGoose() {
        return goose;
    }

    public void setGoose(String goose) {
        this.goose = goose;
    }

    public String getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent;
    }

    public String getContractMode() {
        return contractMode;
    }

    public void setContractMode(String contractMode) {
        this.contractMode = contractMode;
    }

    public String getQualityTarget() {
        return qualityTarget;
    }

    public void setQualityTarget(String qualityTarget) {
        this.qualityTarget = qualityTarget;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getGuaranteeMoney() {
        return guaranteeMoney;
    }

    public void setGuaranteeMoney(String guaranteeMoney) {
        this.guaranteeMoney = guaranteeMoney;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

	public String getLabourSin() {
		return labourSin;
	}

	public void setLabourSin(String labourSin) {
		this.labourSin = labourSin;
	}

	public String getLabourQuantity() {
		return labourQuantity;
	}

	public void setLabourQuantity(String labourQuantity) {
		this.labourQuantity = labourQuantity;
	}

	public String getLabourUnit() {
		return labourUnit;
	}

	public void setLabourUnit(String labourUnit) {
		this.labourUnit = labourUnit;
	}

	public String getHtlx() {
		return htlx;
	}

	public void setHtlx(String htlx) {
		this.htlx = htlx;
	}

}
