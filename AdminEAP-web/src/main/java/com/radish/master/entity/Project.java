package com.radish.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

@Entity
@Table(name = "tbl_project")
public class Project extends BaseEntity {

	private static final long serialVersionUID = -3076809547362911507L;

	@Header(name = "项目编号")
	@Column(name = "project_code")
	private String projectCode;
	
	@Header(name = "项目名称")
	@Column(name = "project_name")
	private String project_name;
	
	@Header(name = "项目负责人")
	@Column(name = "project_manager")
	private String projectManager;
	
	@Header(name = "项目级别")
	@Column(name = "project_rank")
	private String projectRank;
	
	@Header(name = "安全文明文件")
	@Column(name = "safty_file")
	private String saftyFile;
	
	@Header(name = "施工合同")
	@Column(name = "construction_file")
	private String constructionFile;
	
	@Header(name = "招标文件")
	@Column(name = "bids_file")
	private String bidsFile;
	
	@Header(name = "中标文件")
	@Column(name = "bids_win_file")
	private String bidsWinFile;
	
	@Header(name = "中标通知文件")
	@Column(name = "bids_win_notice_file")
	private String bidsWinNoticeFile;
	
	@Header(name = "工程质量文件")
	@Column(name = "quality_file")
	private String qualityFile;
	
	@Header(name = "预算成本分析表")
	@Column(name = "cost_file")
	private String costFile;
	
	@Header(name = "工程进度文件")
	@Column(name = "schedule_file")
	private String scheduleFile;

	@Header(name = "项目状态")
	@Column(name = "status")
	private String status;

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public String getProjectRank() {
		return projectRank;
	}

	public void setProjectRank(String projectRank) {
		this.projectRank = projectRank;
	}

	public String getSaftyFile() {
		return saftyFile;
	}

	public void setSaftyFile(String saftyFile) {
		this.saftyFile = saftyFile;
	}

	public String getConstructionFile() {
		return constructionFile;
	}

	public void setConstructionFile(String constructionFile) {
		this.constructionFile = constructionFile;
	}

	public String getBidsFile() {
		return bidsFile;
	}

	public void setBidsFile(String bidsFile) {
		this.bidsFile = bidsFile;
	}

	public String getBidsWinFile() {
		return bidsWinFile;
	}

	public void setBidsWinFile(String bidsWinFile) {
		this.bidsWinFile = bidsWinFile;
	}

	public String getBidsWinNoticeFile() {
		return bidsWinNoticeFile;
	}

	public void setBidsWinNoticeFile(String bidsWinNoticeFile) {
		this.bidsWinNoticeFile = bidsWinNoticeFile;
	}

	public String getQualityFile() {
		return qualityFile;
	}

	public void setQualityFile(String qualityFile) {
		this.qualityFile = qualityFile;
	}

	public String getCostFile() {
		return costFile;
	}

	public void setCostFile(String costFile) {
		this.costFile = costFile;
	}

	public String getScheduleFile() {
		return scheduleFile;
	}

	public void setScheduleFile(String scheduleFile) {
		this.scheduleFile = scheduleFile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
