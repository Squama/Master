/**
 * 
 */
package com.radish.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * @author tonyd
 *
 */
@Entity
@Table(name = "tbl_dispatch")
public class Dispatch extends BaseEntity{

	private static final long serialVersionUID = -6144122188053470798L;

	@Header(name="采购单编号")
    @Column(name="purchase_id")
	private String purchaseID;
	
	@Header(name="物料源库项目ID")
    @Column(name="source_project_id")
	private String sourceProjectID;


	@Header(name="目标库项目ID")
	@Column(name="target_project_id")
	private String targetProjectID;

	@Header(name="调度单状态")
    @Column(name="status")
	private String status;

	public String getPurchaseID() {
		return purchaseID;
	}

	public void setPurchaseID(String purchaseID) {
		this.purchaseID = purchaseID;
	}

	public String getSourceProjectID() {
		return sourceProjectID;
	}

	public void setSourceProjectID(String sourceProjectID) {
		this.sourceProjectID = sourceProjectID;
	}

	public String getTargetProjectID() {
		return targetProjectID;
	}

	public void setTargetProjectID(String targetProjectID) {
		this.targetProjectID = targetProjectID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
