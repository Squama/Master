package com.radish.master.entity.safty;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//职务职责表

@Entity
@Table(name ="tbl_aqjy")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Aqjy implements Serializable {

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
    @Column(name = "id", length = 64)
    private String id;
   
    @Header(name = "人员id")
    @Column(name = "workerid")
    private String workerid;
    
    @Header(name = "姓名")
    @Column(name = "name")
    private String name;

    @Header(name = "性别")
    @Column(name = "sex")
    private String sex;

    @Header(name = "出生年月")
    @Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Header(name = "工种")
    @Column(name = "work_type")
    private String workType;

    @Header(name = "家庭住址")
    @Column(name = "address")
    private String address;

    @Header(name = "进场时间")
    @Column(name = "jctime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date jctime;
    
    @Header(name = "文化程度")
    @Column(name = "whcd")
    private String whcd;
    
    @Header(name = "班组教育")
    @Column(name = "bzjy")
    private String bzjy;
    
    @Header(name = "班组教育人")
    @Column(name = "bzjyr")
    private String bzjyr;
    
    @Header(name = "班组教育时间")
    @Column(name = "bztime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bztime;
    
    @Header(name = "项目教育")
    @Column(name = "xmjy")
    private String xmjy;
    
    @Header(name = "项目教育人")
    @Column(name = "xmjyr")
    private String xmjyr;
    
    @Header(name = "项目教育时间")
    @Column(name = "xmtime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date xmtime;
    
    @Header(name = "公司教育")
    @Column(name = "gsjy")
    private String gsjy;
    
    @Header(name = "公司教育人")
    @Column(name = "gsjyr")
    private String gsjyr;
    
    @Header(name = "公司教育时间")
    @Column(name = "gstime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date gstime;
    
    @Header(name = "备注")
    @Column(name = "descs")
    private String descs;
    
    @Header(name = "状态 10-新增  20-班组教育  30-项目教育 40-公司教育 50-完成")
    @Column(name = "status")
    private String status;
    
    @Column(name = "create_name")
    private String create_name;
    
    @Column(name = "create_name_ID")
    private String create_name_ID;
    
    @Column(name = "create_time")
    private Date create_time;
    
    @Column(name = "jyid")
    private String jyid;
    @Column(name = "jyname")
    private String jyname;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWorkerid() {
		return workerid;
	}

	public void setWorkerid(String workerid) {
		this.workerid = workerid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getJctime() {
		return jctime;
	}

	public void setJctime(Date jctime) {
		this.jctime = jctime;
	}

	public String getWhcd() {
		return whcd;
	}

	public void setWhcd(String whcd) {
		this.whcd = whcd;
	}

	public String getBzjy() {
		return bzjy;
	}

	public void setBzjy(String bzjy) {
		this.bzjy = bzjy;
	}

	public String getBzjyr() {
		return bzjyr;
	}

	public void setBzjyr(String bzjyr) {
		this.bzjyr = bzjyr;
	}

	public Date getBztime() {
		return bztime;
	}

	public void setBztime(Date bztime) {
		this.bztime = bztime;
	}

	public String getXmjy() {
		return xmjy;
	}

	public void setXmjy(String xmjy) {
		this.xmjy = xmjy;
	}

	public String getXmjyr() {
		return xmjyr;
	}

	public void setXmjyr(String xmjyr) {
		this.xmjyr = xmjyr;
	}

	public Date getXmtime() {
		return xmtime;
	}

	public void setXmtime(Date xmtime) {
		this.xmtime = xmtime;
	}

	public String getGsjy() {
		return gsjy;
	}

	public void setGsjy(String gsjy) {
		this.gsjy = gsjy;
	}

	public String getGsjyr() {
		return gsjyr;
	}

	public void setGsjyr(String gsjyr) {
		this.gsjyr = gsjyr;
	}

	public Date getGstime() {
		return gstime;
	}

	public void setGstime(Date gstime) {
		this.gstime = gstime;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getJyid() {
		return jyid;
	}

	public void setJyid(String jyid) {
		this.jyid = jyid;
	}

	public String getJyname() {
		return jyname;
	}

	public void setJyname(String jyname) {
		this.jyname = jyname;
	}
    
    
}
