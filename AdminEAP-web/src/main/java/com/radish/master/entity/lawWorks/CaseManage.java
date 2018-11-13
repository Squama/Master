package com.radish.master.entity.lawWorks;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author 案件管理
 * @创建时间 2018年11月11日 
 * @return
 */
@Entity
@Table(name ="tbl_casemanage")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class CaseManage implements Serializable {

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
    private String id;

    @Header(name = "名称")
    @Column(name = "name")
    private String name;
    
    @Header(name = "案件类型 刑事10 民事20 劳动仲裁30")
    @Column(name = "caseType")
    private String caseType;
    
    @Header(name = "案件状态 案件准备10 已立案20 进行中30 执行中40 已结案50")
    @Column(name = "caseStatus")
    private String caseStatus;
    
    @Header(name = "创建人")
    @Column(name = "create_name")
    private String create_name;
    
    @Header(name = "创建人id")
    @Column(name = "create_name_ID")
    private String create_name_ID;
    
    @Header(name = "创建日期")
    @Column(name = "create_time")
    private Date create_time;
    
    @Header(name = "受理法院")
    @Column(name = "court")
    private String court;
    
    @Header(name = "法官")
    @Column(name = "judge")
    private String judge;
    
    @Header(name = "法官电话")
    @Column(name = "judgeTel")
    private String judgeTel;
    
    @Header(name = "诉讼要求")
    @Column(name = "descs")
    private String descs;
    
    @Header(name = "标的额")
    @Column(name = "money")
    private String money;
    
    @Header(name = "原告")
    @Column(name = "ygs")
    private String ygs;
    
    @Header(name = "原告电话")
    @Column(name = "ygTels")
    private String ygTels;
    
    @Header(name = "被告")
    @Column(name = "bgs")
    private String bgs;
    
    @Header(name = "被告电话")
    @Column(name = "bgTels")
    private String bgTels;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "立案时间")
    @Column(name = "LAtime")
    private Date latime;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "开庭时间")
    @Column(name = "KTtime")
    private Date kttime;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "举证期限时间")
    @Column(name = "JZQXtime")
    private Date jzqxtime;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "判决送达时间")
    @Column(name = "PJSDtime")
    private Date pjsdtime;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "裁定送达时间")
    @Column(name = "CDSDtime")
    private Date cdsdtime;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "异议截止时间")
    @Column(name = "YYtime")
    private Date yytime;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "查封到期时间")
    @Column(name = "CFtime")
    private Date cftime;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "冻结到期时间")
    @Column(name = "DJtime")
    private Date djtime;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "扣押到期时间")
    @Column(name = "KYtime")
    private Date kytime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
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

	public String getCourt() {
		return court;
	}

	public void setCourt(String court) {
		this.court = court;
	}

	public String getJudge() {
		return judge;
	}

	public void setJudge(String judge) {
		this.judge = judge;
	}

	public String getJudgeTel() {
		return judgeTel;
	}

	public void setJudgeTel(String judgeTel) {
		this.judgeTel = judgeTel;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getYgs() {
		return ygs;
	}

	public void setYgs(String ygs) {
		this.ygs = ygs;
	}

	public String getYgTels() {
		return ygTels;
	}

	public void setYgTels(String ygTels) {
		this.ygTels = ygTels;
	}

	public String getBgs() {
		return bgs;
	}

	public void setBgs(String bgs) {
		this.bgs = bgs;
	}

	public String getBgTels() {
		return bgTels;
	}

	public void setBgTels(String bgTels) {
		this.bgTels = bgTels;
	}

	public Date getLatime() {
		return latime;
	}

	public void setLatime(Date latime) {
		this.latime = latime;
	}

	public Date getKttime() {
		return kttime;
	}

	public void setKttime(Date kttime) {
		this.kttime = kttime;
	}

	public Date getJzqxtime() {
		return jzqxtime;
	}

	public void setJzqxtime(Date jzqxtime) {
		this.jzqxtime = jzqxtime;
	}

	public Date getPjsdtime() {
		return pjsdtime;
	}

	public void setPjsdtime(Date pjsdtime) {
		this.pjsdtime = pjsdtime;
	}

	public Date getCdsdtime() {
		return cdsdtime;
	}

	public void setCdsdtime(Date cdsdtime) {
		this.cdsdtime = cdsdtime;
	}

	public Date getYytime() {
		return yytime;
	}

	public void setYytime(Date yytime) {
		this.yytime = yytime;
	}

	public Date getCftime() {
		return cftime;
	}

	public void setCftime(Date cftime) {
		this.cftime = cftime;
	}

	public Date getDjtime() {
		return djtime;
	}

	public void setDjtime(Date djtime) {
		this.djtime = djtime;
	}

	public Date getKytime() {
		return kytime;
	}

	public void setKytime(Date kytime) {
		this.kytime = kytime;
	}

}
