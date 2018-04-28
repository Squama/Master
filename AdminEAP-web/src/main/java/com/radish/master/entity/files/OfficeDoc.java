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
 * @author 办公文件管理表
 * @创建时间 2018年4月27日 下午2:25:35
 * @return
 */
@Entity
@Table(name ="tbl_officeDoc")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class OfficeDoc implements Serializable {

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

    
    @Header(name = "文件类型")
    @Column(name = "ctype")
    private String ctype;
    
    @Header(name = "文件名称")
    @Column(name = "cname")
    private String cname;
    
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "最后更新时间")
    @Column(name = "endDate")
    private Date endDate;
    
    
    
    @Header(name = "附件ID")
    @Column(name = "fileId")
    private String fileId;
    
    
    @Header(name = "创建人id")
    @Column(name = "createId")
    private String createId;
    
    @Header(name = "创建人日期")
    @Column(name = "createDate")
    private Date createDate;
    
    @Header(name = "备注")
    @Column(name = "remark")
    private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
    
}
