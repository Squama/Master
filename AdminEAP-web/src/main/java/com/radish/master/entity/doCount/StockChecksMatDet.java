package com.radish.master.entity.doCount;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//材料出入库统计明细结存

@Entity
@Table(name ="tbl_stockchecks_matdet")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class StockChecksMatDet implements Serializable {

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
    
    
    @Header(name = "材料出入库统计明细表内码")
    @Column(name = "pid")
    private String pid;
    
    @Header(name = "期初数量")
    @Column(name = "qcsl")
    private String qcsl;
    
    @Header(name = "期初金额")
    @Column(name = "qcje")
    private String qcje;
    
    @Header(name = "入库数量")
    @Column(name = "rksl")
    private String rksl;
    
    @Header(name = "入库金额")
    @Column(name = "rkje")
    private String rkje;
    
    @Header(name = "出库数量")
    @Column(name = "cksl")
    private String cksl;
    
    @Header(name = "出库金额")
    @Column(name = "ckje")
    private String ckje;
    
    
    @Header(name = "结存数量")
    @Column(name = "jcsl")
    private String jcsl;
    
    @Header(name = "结存金额")
    @Column(name = "jcje")
    private String jcje;
    
    @Header(name = "附注")
    @Column(name = "fz")
    private String fz;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getJcsl() {
		return jcsl;
	}

	public void setJcsl(String jcsl) {
		this.jcsl = jcsl;
	}

	public String getJcje() {
		return jcje;
	}

	public void setJcje(String jcje) {
		this.jcje = jcje;
	}

	public String getQcsl() {
		return qcsl;
	}

	public void setQcsl(String qcsl) {
		this.qcsl = qcsl;
	}

	public String getQcje() {
		return qcje;
	}

	public void setQcje(String qcje) {
		this.qcje = qcje;
	}

	public String getRksl() {
		return rksl;
	}

	public void setRksl(String rksl) {
		this.rksl = rksl;
	}

	public String getRkje() {
		return rkje;
	}

	public void setRkje(String rkje) {
		this.rkje = rkje;
	}

	public String getCksl() {
		return cksl;
	}

	public void setCksl(String cksl) {
		this.cksl = cksl;
	}

	public String getCkje() {
		return ckje;
	}

	public void setCkje(String ckje) {
		this.ckje = ckje;
	}

	public String getFz() {
		return fz;
	}

	public void setFz(String fz) {
		this.fz = fz;
	}

	
    
}
