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
 * @author 公司函件文件管理表
 * @创建时间 2018年4月27日 下午2:25:35
 * @return
 */
@Entity
@Table(name ="tbl_correspondenceDoc")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class CorrespondenceDoc implements Serializable {

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
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "发出日期")
    @Column(name = "sendDate")
    private Date sendDate;
    
    @Header(name = "函件内容")
    @Column(name = "remark")
    private String remark;
    
    @Header(name = "是否回复")
    @Column(name = "isReplay")
    private String isReplay;
    
    @Header(name = "回复内容")
    @Column(name = "replayText")
    private String replayText;
    
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
    
    @Header(name = "类型")
    @Column(name = "type")
    private String type;

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

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsReplay() {
		return isReplay;
	}

	public void setIsReplay(String isReplay) {
		this.isReplay = isReplay;
	}

	public String getReplayText() {
		return replayText;
	}

	public void setReplayText(String replayText) {
		this.replayText = replayText;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

}
