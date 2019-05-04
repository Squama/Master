package com.radish.master.entity.volumePay;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//收据表
@Entity
@Table(name ="tbl_pro_receipt")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class ProReceipt implements Serializable {

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

    @Header(name = "所属项目id")
    @Column(name = "proId")
    private String proId;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Header(name = "填开日期")
    @Column(name = "createDate")
    private Date createDate;
    
    @Header(name = "编号")
    @Column(name = "number")
    private String number;
    
    @Header(name = "交款人")
    @Column(name = "jkr")
    private String jkr;
    
    @Header(name = "内容及用途")
    @Column(name = "content")
    private String content;
    
    @Header(name = "交款方式")
    @Column(name = "type")
    private String type;
    
    @Header(name = "支付金额")
    @Column(name = "money")
    private String money;
    
    @Header(name = "备注")
    @Column(name = "bz")
    private String bz;
    
    @Header(name = "创建人id")
    @Column(name = "createId")
    private String createId;
    
    @Header(name = "是否记账")
    @Column(name = "isrz")
    private String isrz;
    
    @Header(name = "收据类型")
    @Column(name = "sjtype")
    private String sjtype;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getJkr() {
		return jkr;
	}

	public void setJkr(String jkr) {
		this.jkr = jkr;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getIsrz() {
		return isrz;
	}

	public void setIsrz(String isrz) {
		this.isrz = isrz;
	}

	public String getSjtype() {
		return sjtype;
	}

	public void setSjtype(String sjtype) {
		this.sjtype = sjtype;
	}
    
    
}
