package com.radish.master.entity.safty;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 安全教育附件
 * @author wzh
 * @创建时间 2019年5月8日 下午4:57:27
 * @return
 */
@Entity
@Table(name ="tbl_aqjyfj")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Aqjyfj implements Serializable {

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

    
    @Header(name = "教育id")
    @Column(name = "jyid")
    private String jyid;
    
    
    @Header(name = "来源")
    @Column(name = "ly")
    private String ly;
    
    @Header(name = "文件类型")
    @Column(name = "fjtype")
    private String fjtype;
    
    @Header(name = "文件类型名称")
    @Column(name = "fjtypename")
    private String fjtypename;
    
    @Header(name = "创建人")
    @Column(name = "create_name")
    private String create_name;
    
    @Header(name = "创建人id")
    @Column(name = "create_name_ID")
    private String create_name_ID;
    
    @Header(name = "创建日期")
    @Column(name = "create_time")
    private Date create_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJyid() {
		return jyid;
	}

	public void setJyid(String jyid) {
		this.jyid = jyid;
	}

	public String getLy() {
		return ly;
	}

	public void setLy(String ly) {
		this.ly = ly;
	}

	public String getFjtype() {
		return fjtype;
	}

	public void setFjtype(String fjtype) {
		this.fjtype = fjtype;
	}

	public String getFjtypename() {
		return fjtypename;
	}

	public void setFjtypename(String fjtypename) {
		this.fjtypename = fjtypename;
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
    
    

	
    
    

}
