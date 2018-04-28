package com.radish.master.entity.files;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author 工程量变更签证文件管理表
 * @创建时间 2018年4月27日 下午2:25:35
 * @return
 */
@Entity
@Table(name ="tbl_changeVisaDoc")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class ChangeVisaDoc implements Serializable {

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
    protected String id;

    @Header(name = "所属项目id")
    @Column(name = "projectId")
    private String projectId;
    
    @Header(name = "编号")
    @Column(name = "number")
    private String number;
    
    @Header(name = "变更类型")
    @Column(name = "ctype")
    private String ctype;
    
    @Header(name = "施工部位及名称")
    @Column(name = "cname")
    private String cname;
    
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "签证日期")
    @Column(name = "visaDate")
    private Date visaDate;
    
    @Header(name = "签证内容")
    @Column(name = "remark")
    private String remark;
    
    @Header(name = "签证原因")
    @Column(name = "reason")
    private String reason;
    
    @Header(name = "附件ID")
    @Column(name = "fileId")
    private String fileId;
    
    
    @Header(name = "创建人id")
    @Column(name = "createId")
    private String createId;
    
    @Header(name = "创建日期")
    @Column(name = "createDate")
    private Date createDate;
    
    @Header(name = "修改人id")
    @Column(name = "editId")
    private String editId;
    
    @Header(name = "修改日期")
    @Column(name = "editDate")
    private Date editDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Date getVisaDate() {
		return visaDate;
	}

	public void setVisaDate(Date visaDate) {
		this.visaDate = visaDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
    

}
