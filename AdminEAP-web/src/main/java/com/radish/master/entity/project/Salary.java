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

	@Header(name = "成本总额")
	@Column(name = "cost_sum")
	private String costSum;

	/*
	 * 10-new 20-负责人审批 30-办公室主任审批 40-总经理审批 50-财务审批 60-已完成 70-审核不通过
	 */
	@Header(name = "状态")
	@Column(name = "status")
	private String status;

	@Header(name = "是否记账")
	@Column(name = "isjz")
	private String isjz;

	@Header(name = "税金支付状态")
	@Column(name = "sjstatus")
	private String sjstatus;

	@Header(name = "税金是否记账")
	@Column(name = "sjisjz")
	private String sjisjz;
	
	/**
	 * 机关人员工资表需区分公司分部----分部值（DICT表的ENTS）
	 * @author wzh
	 * @创建时间 2019年7月10日 上午10:34:28
	 * @return
	 */
	@Header(name = "直属分部")
	@Column(name = "deptent")
	private String deptent;
	
	@Header(name = "直属分部名称")
	@Column(name = "deptentname")
	private String deptentname;

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

	public String getIsjz() {
		return isjz;
	}

	public void setIsjz(String isjz) {
		this.isjz = isjz;
	}

	public String getSjstatus() {
		return sjstatus;
	}

	public void setSjstatus(String sjstatus) {
		this.sjstatus = sjstatus;
	}

	public String getSjisjz() {
		return sjisjz;
	}

	public void setSjisjz(String sjisjz) {
		this.sjisjz = sjisjz;
	}

	public String getCostSum() {
		return costSum;
	}

	public void setCostSum(String costSum) {
		this.costSum = costSum;
	}

	public String getDeptent() {
		return deptent;
	}

	public void setDeptent(String deptent) {
		this.deptent = deptent;
	}

	public String getDeptentname() {
		return deptentname;
	}

	public void setDeptentname(String deptentname) {
		this.deptentname = deptentname;
	}

}
