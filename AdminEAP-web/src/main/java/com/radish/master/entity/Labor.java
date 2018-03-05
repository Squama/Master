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
    
}
