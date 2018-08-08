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
@Table(name ="tbl_inandout_det_jc")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class InAndOutDetJc implements Serializable {

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
    
    @Header(name = "结存数量")
    @Column(name = "jcsl")
    private String jcsl;
    
    @Header(name = "结存金额")
    @Column(name = "jcje")
    private String jcje;

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

	
    
}
