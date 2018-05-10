package com.radish.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="tbl_job_dept")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class JobDeptRt {
	private static final long serialVersionUID = 3309603144734703924L;
	 	@Id
	    @GenericGenerator(name = "id", strategy = "uuid")
	    @GeneratedValue(generator = "id")
	    @Column(name = "id", length = 64)
	    private String id;
    @Header(name = "部门id")
    @Column(name = "deptId")
    private String deptId;
    @Header(name = "职务id")
    @Column(name = "jobId")
    private String jobId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}
