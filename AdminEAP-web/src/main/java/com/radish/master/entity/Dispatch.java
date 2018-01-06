/**
 * 
 */
package com.radish.master.entity;

import javax.persistence.Column;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * @author tonyd
 *
 */
public class Dispatch extends BaseEntity{

	private static final long serialVersionUID = -6144122188053470798L;

	@Header(name="采购单编号")
    @Column(name="purchase_id")
	private String purchaseID;
	
	@Header(name="项目ID")
    @Column(name="project_id")
	private String projectID;
	
	@Header(name="调度单状态")
    @Column(name="status")
	private String status;

	public String getPurchaseID() {
		return purchaseID;
	}

	public void setPurchaseID(String purchaseID) {
		this.purchaseID = purchaseID;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
