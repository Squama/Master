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
* dongyan      2018年7月9日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_project_sub")
public class ProjectSub extends BaseEntity {

    private static final long serialVersionUID = -2483246370998831964L;

    @Header(name = "项目ID")
    @Column(name = "project_id")
    private String projectID;

    @Header(name = "子项名称")
    @Column(name = "sub_name")
    private String subName;

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

}
