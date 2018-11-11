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
 * @author 收发文人员查看表
 * @创建时间 2018年4月27日 下午2:25:35
 * @return
 */
@Entity
@Table(name ="tbl_writingslook")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class WritingsLook implements Serializable {

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

    
    @Header(name = "收发文内码")
    @Column(name = "writingid")
    private String writingid;
    
    @Header(name = "查看人员内码")
    @Column(name = "lookid")
    private String lookid;
    
    @Header(name = "查看人员")
    @Column(name = "lookname")
    private String lookname;
    
    @Header(name = "最新查看时间")
    @Column(name = "createDate")
    private Date createDate;
    
    @Header(name = "查看人员所在部门")
    @Column(name = "deptName")
    private String deptName;
    
    @Header(name = "是否查看")
    @Column(name = "islook")
    private String islook;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWritingid() {
		return writingid;
	}

	public void setWritingid(String writingid) {
		this.writingid = writingid;
	}

	public String getLookid() {
		return lookid;
	}

	public void setLookid(String lookid) {
		this.lookid = lookid;
	}

	public String getLookname() {
		return lookname;
	}

	public void setLookname(String lookname) {
		this.lookname = lookname;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getIslook() {
		return islook;
	}

	public void setIslook(String islook) {
		this.islook = islook;
	}

    
}
