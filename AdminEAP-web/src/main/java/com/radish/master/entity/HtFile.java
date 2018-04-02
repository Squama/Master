package com.radish.master.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

@Entity
@Table(name="tbl_usercontractfile")
public class HtFile extends BaseEntity{
	@Header(name = "实际名称")
    @Column(name = "fileName")
    private String fileName;
    @Header(name = "保存的名称")
    @Column(name = "savedName")
    private String savedName;
    @Header(name = "存储地址")
    @Column(name = "filePath")
    private String filePath;
    @Header(name = "附件大小")
    @Column(name = "fileSize")
    private Long fileSize;

    @Header(name = "创建者id")
    @Column(name = "create_user_id")
    private String createUserId;

    /**
     * 业务表单ID
     */
    @Column(name = "form_ID")
    private String formId;

    public String getSavedName() {
        return savedName;
    }

    public void setSavedName(String savedName) {
        this.savedName = savedName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

}
