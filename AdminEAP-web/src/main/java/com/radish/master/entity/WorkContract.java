/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "tbl_usercontract")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class WorkContract implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;

    
    @Header(name = "签约人内码")
    @Column(name = "newUserId")
    private String newUserId;
    
    @Header(name = "负责签约人内码")
    @Column(name = "oldUserId")
    private String oldUserId;
    
    @Header(name = "合同文件表内码")
    @Column(name = "fileId")
    private String fileId;
    
    @Header(name = "合同编号")
    @Column(name = "contract_number")
    private String contract_number;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "开始时间")
    @Column(name = "startDate")
    private Date startDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "结束时间")
    @Column(name = "endDate")
    private Date endDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "签约时间")
    @Column(name = "qyDate")
    private Date qyDate;
    
    @Header(name = "附件路径")
    @Column(name = "file_path")
    private String file_path;
    
    @Header(name = "附件名")
    @Column(name = "file_name")
    private String file_name;
    
    @Header(name = "特殊条款")
    @Column(name = "items")
    private String items;
    
    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;

    
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNewUserId() {
		return newUserId;
	}

	public void setNewUserId(String newUserId) {
		this.newUserId = newUserId;
	}

	public String getOldUserId() {
		return oldUserId;
	}

	public void setOldUserId(String oldUserId) {
		this.oldUserId = oldUserId;
	}

	public String getContract_number() {
		return contract_number;
	}

	public void setContract_number(String contract_number) {
		this.contract_number = contract_number;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getQyDate() {
		return qyDate;
	}

	public void setQyDate(Date qyDate) {
		this.qyDate = qyDate;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
    
}
