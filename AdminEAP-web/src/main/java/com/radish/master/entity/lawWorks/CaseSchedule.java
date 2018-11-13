package com.radish.master.entity.lawWorks;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author 案件进度
 * @创建时间 2018年11月11日 
 * @return
 */
@Entity
@Table(name ="tbl_caseSchedule")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class CaseSchedule implements Serializable {

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
    @Column(name = "ID", length = 64)
    private String id;
    
    @Header(name = "案件id")
    @Column(name = "caseid")
    private String caseid;

    @Header(name = "进展情况")
    @Column(name = "jzqk")
    private String jzqk;
    
    @Header(name = "节点计划")
    @Column(name = "plan")
    private String plan;
    
    @Header(name = "备注")
    @Column(name = "descs")
    private String descs;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "完成时间")
    @Column(name = "finalTime")
    private Date finalTime;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "进度时间")
    @Column(name = "scheTime")
    private Date scheTime;
    
    @Header(name = "进度状态")
    @Column(name = "shceStatus")
    private String shceStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public String getJzqk() {
		return jzqk;
	}

	public void setJzqk(String jzqk) {
		this.jzqk = jzqk;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public Date getFinalTime() {
		return finalTime;
	}

	public void setFinalTime(Date finalTime) {
		this.finalTime = finalTime;
	}

	public Date getScheTime() {
		return scheTime;
	}

	public void setScheTime(Date scheTime) {
		this.scheTime = scheTime;
	}

	public String getShceStatus() {
		return shceStatus;
	}

	public void setShceStatus(String shceStatus) {
		this.shceStatus = shceStatus;
	}
    
    
}
