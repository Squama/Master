package com.radish.master.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

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
	private String projectName;
	
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
	
	@Header(name = "建筑面积")
	@Column(name = "jzmj")
	private String jzmj;
	
	@Header(name = "建筑结构")
	@Column(name = "jzjg")
	private String jzjg;
	
	@Header(name = "层数")
	@Column(name = "cs")
	private String cs;
	
	@Header(name = "开工时间")
	@Column(name = "kgsj")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date kgsj;
	
	@Header(name = "竣工时间")
	@Column(name = "jgsj")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date jgsj;
	
	@Header(name = "总造价")
	@Column(name = "zzj")
	private String zzj;
	
	@Header(name = "工程地点")
	@Column(name = "gcdd")
	private String gcdd;
	
	@Header(name = "工程状态")
	@Column(name = "gczt")
	private String gczt;

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

	public String getJzmj() {
		return jzmj;
	}

	public void setJzmj(String jzmj) {
		this.jzmj = jzmj;
	}

	public String getJzjg() {
		return jzjg;
	}

	public void setJzjg(String jzjg) {
		this.jzjg = jzjg;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public Date getKgsj() {
		return kgsj;
	}

	public void setKgsj(Date kgsj) {
		this.kgsj = kgsj;
	}

	public Date getJgsj() {
		return jgsj;
	}

	public void setJgsj(Date jgsj) {
		this.jgsj = jgsj;
	}

	public String getZzj() {
		return zzj;
	}

	public void setZzj(String zzj) {
		this.zzj = zzj;
	}

	public String getGcdd() {
		return gcdd;
	}

	public void setGcdd(String gcdd) {
		this.gcdd = gcdd;
	}

	public String getGczt() {
		return gczt;
	}

	public void setGczt(String gczt) {
		this.gczt = gczt;
	}
	
}
