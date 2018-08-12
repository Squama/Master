package com.radish.master.entity.doCount;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

//库存判断统计表

@Entity
@Table(name ="tbl_stockchecks_mat")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class StockChecksMat implements Serializable {

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
    
    
    @Header(name = "库存盘点内码")
    @Column(name = "pid")
    private String pid;
    
    @Header(name = "物料名称")
    @Column(name = "matName")
    private String matName;
    
    @Header(name = "物料编号")
    @Column(name = "matNumber")
    private String matNumber;
    
    @Header(name = "规格")
    @Column(name = "standard")
    private String standard;
    
    @Header(name = "单位")
    @Column(name = "unit")
    private String unit;
    
    
    @Header(name = "附注")
    @Column(name = "fz")
    private String fz;
    
    @Header(name = "明细数量")
    @Column(name = "sl")
    private String sl;

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

	public String getMatName() {
		return matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}

	public String getMatNumber() {
		return matNumber;
	}

	public void setMatNumber(String matNumber) {
		this.matNumber = matNumber;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getFz() {
		return fz;
	}

	public void setFz(String fz) {
		this.fz = fz;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}
    
    
    
}
