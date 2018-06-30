/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity.skillManage;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* wangzhihao      2017年10月28日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_planApprove")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class PlanApprove implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;

    @Header(name = "项目id")
    @Column(name = "proid")
    private String proid;
    
    @Header(name = "所属项目")
    @Column(name = "proname")
    private String proname;
    
    @Header(name = "状态 10新建 20总公审批 30 完成 40 驳回")
    @Column(name = "status")
    private String status;
    
    @Header(name = "类型 10施工组织设计 20专项施工方案")
    @Column(name = "type")
    private String type;
    
    @Header(name = "创建人")
    @Column(name = "create_name")
    private String create_name;

    @Header(name = "创建人id")
    @Column(name = "create_name_ID")
    private String create_name_ID;

    @Header(name = "创建时间")
    @Column(name = "create_time")
    private Date create_time;
    
   
    @Header(name = "驳回原因")
    @Column(name = "bhdesc")
    private String bhdesc;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getProid() {
		return proid;
	}


	public void setProid(String proid) {
		this.proid = proid;
	}


	public String getProname() {
		return proname;
	}


	public void setProname(String proname) {
		this.proname = proname;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getCreate_name() {
		return create_name;
	}


	public void setCreate_name(String create_name) {
		this.create_name = create_name;
	}


	public String getCreate_name_ID() {
		return create_name_ID;
	}


	public void setCreate_name_ID(String create_name_ID) {
		this.create_name_ID = create_name_ID;
	}


	public Date getCreate_time() {
		return create_time;
	}


	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


	public String getBhdesc() {
		return bhdesc;
	}


	public void setBhdesc(String bhdesc) {
		this.bhdesc = bhdesc;
	}

	
    
}
