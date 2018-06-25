/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity.qualityCheck;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
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
@Table(name = "tbl_videoFile")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class VideoFile implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;


    @Header(name = "创建人")
    @Column(name = "create_name")
    private String create_name;

    @Header(name = "创建人id")
    @Column(name = "create_name_ID")
    private String create_name_ID;

    @Header(name = "创建时间")
    @Column(name = "create_time")
    private Date create_time;

    @Header(name = "修改人")
    @Column(name = "update_name")
    private String update_name;
    
    @Header(name = "修改人id")
    @Column(name = "update_name_ID")
    private String update_name_ID;

    @Header(name = "修改时间")
    @Column(name = "update_time")
    private Date update_time;

    
    @Header(name = "预留字段1")
    @Column(name = "reserve1")
    private String reserve1;

    @Header(name = "预留字段1")
    @Column(name = "type")
    private String type;

    @Header(name = "项目id")
    @Column(name = "proid")
    private String proid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUpdate_name() {
		return update_name;
	}

	public void setUpdate_name(String update_name) {
		this.update_name = update_name;
	}

	public String getUpdate_name_ID() {
		return update_name_ID;
	}

	public void setUpdate_name_ID(String update_name_ID) {
		this.update_name_ID = update_name_ID;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProid() {
		return proid;
	}

	public void setProid(String proid) {
		this.proid = proid;
	}


}
