package com.radish.master.entity.doCount;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//材料出入库统计表

@Entity
@Table(name ="tbl_inandout")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class InAndOut implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "id", length = 64)
    private String id;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "开始时间")
    @Column(name = "startdate")
    private Date startdate;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "结束时间")
    @Column(name = "enddate")
    private Date enddate;
    
    @Header(name = "所属项目id")
    @Column(name = "projectId")
    private String projectId;
    
    @Header(name = "物料ID")
    @Column(name = "matId")
    private String matId;
    
    @Header(name = "状态")
    @Column(name = "status")
    private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getMatId() {
		return matId;
	}

	public void setMatId(String matId) {
		this.matId = matId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
    
}
