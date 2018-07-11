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
* dongyan      2018年4月19日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_project_team")
public class ProjectTeam extends BaseEntity {

    private static final long serialVersionUID = 6617000819411298749L;

    @Header(name = "项目编号")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "项目名称")
    @Column(name = "project_name")
    private String projectName;

    @Header(name = "班组编码")
    @Column(name = "team_code")
    private String teamCode;

    @Header(name = "班组名称")
    @Column(name = "team_name")
    private String teamName;
    
    /**
     * 10-普通班组，20-失效班组，30-管理人员班组
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

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
