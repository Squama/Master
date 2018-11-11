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
 * @author 收发文
 * @创建时间 2018年4月27日 下午2:25:35
 * @return
 */
@Entity
@Table(name ="tbl_writings")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Writings implements Serializable {

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

    
    @Header(name = "编号")
    @Column(name = "number")
    private String number;
    
    @Header(name = "名称")
    @Column(name = "name")
    private String name;
    
    @Header(name = "创建人")
    @Column(name = "create_name")
    private String create_name;
    
    @Header(name = "创建人id")
    @Column(name = "create_name_ID")
    private String create_name_ID;
    
    @Header(name = "创建日期")
    @Column(name = "create_time")
    private Date create_time;
    
    @Header(name = "概述")
    @Column(name = "descs")
    private String descs;
    
    @Header(name = "是否已发送")
    @Column(name = "issend")
    private String issend;
    
    @Header(name = "是否有效")
    @Column(name = "isvalid")
    private String isvalid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public String getIssend() {
		return issend;
	}

	public void setIssend(String issend) {
		this.issend = issend;
	}

	public String getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}
    
    

}
