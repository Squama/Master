/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity.project;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * 类说明  荣誉文件
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
@Table(name = "tbl_projectHonour")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class ProjectHonour implements Serializable {


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
    @Column(name = "projectid")
    private String projectid;
    
    @Header(name = "名称")
    @Column(name = "name")
    private String name;
    
    @Header(name = "年度")
    @Column(name = "year")
    private String year;
    
    
    @Header(name = "级别")
    @Column(name = "level")
    private String level;

    @Header(name = "类型 10-质量荣誉表，20-安全荣誉表，30-其他")
    @Column(name = "type")
    private String type;
    
    @Column(name = "descs")
    private String descs;

    @Column(name = "fileids")
    private String fileids;

    @Column(name = "photoids")
    private String photoids;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectid() {
		return projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public String getFileids() {
		return fileids;
	}

	public void setFileids(String fileids) {
		this.fileids = fileids;
	}

	public String getPhotoids() {
		return photoids;
	}

	public void setPhotoids(String photoids) {
		this.photoids = photoids;
	}

}
