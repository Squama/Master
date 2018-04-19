package com.radish.master.entity.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

@Entity
@Table(name = "tbl_job_role")
public class JobRole extends BaseEntity {

	private static final long serialVersionUID = -529782182811911804L;

	@Header(name = "角色ID")
	@Column(name = "role_id")
	private String roleID;
	
	@Header(name = "角色名称")
	@Column(name = "role_name")
	private String roleName;

	@Header(name = "职务ID")
	@Column(name = "job_id")
	private String jobID;
	
	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roldID) {
		this.roleID = roldID;
	}

	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
