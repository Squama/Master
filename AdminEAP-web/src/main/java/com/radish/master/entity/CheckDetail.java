/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name = "tbl_checkdetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class CheckDetail implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;
    
    @Header(name = "考核表ID")
    @Column(name = "dutycheck_ID")
    private String dutycheck_ID;
    
    @Header(name = "扣分1")
    @Column(name = "col1")
    private Double col1;
    
    @Header(name = "分数1")
    @Column(name = "sol1")
    private Double sol1;
    
    @Header(name = "扣分2")
    @Column(name = "col2")
    private Double col2;
    
    @Header(name = "分数2")
    @Column(name = "sol2")
    private Double sol2;
    
    @Header(name = "扣分3")
    @Column(name = "col3")
    private Double col3;
    
    @Header(name = "分数3")
    @Column(name = "sol3")
    private Double sol3;
    
    @Header(name = "扣分4")
    @Column(name = "col4")
    private Double col4;
    
    @Header(name = "分数4")
    @Column(name = "sol4")
    private Double sol4;
    
    @Header(name = "扣分5")
    @Column(name = "col5")
    private Double col5;
    
    @Header(name = "分数5")
    @Column(name = "sol5")
    private Double sol5;
    
    @Header(name = "分数6")
    @Column(name = "sol6")
    private Double sol6;
    
    @Header(name = "扣分6")
    @Column(name = "col6")
    private Double col6;
    
    @Header(name = "分数7")
    @Column(name = "sol7")
    private Double sol7;
    
    @Header(name = "扣分7")
    @Column(name = "col7")
    private Double col7;
    
    @Header(name = "分数8")
    @Column(name = "sol8")
    private Double sol8;
    
    @Header(name = "扣分8")
    @Column(name = "col8")
    private Double col8;
    
   
    
    @Header(name = "总扣分")
    @Column(name = "deduction")
    private Double deduction;
    
    @Header(name = "总得分")
    @Column(name = "score")
    private Double score;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDutycheck_ID() {
		return dutycheck_ID;
	}

	public void setDutycheck_ID(String dutycheck_ID) {
		this.dutycheck_ID = dutycheck_ID;
	}

	public Double getCol1() {
		return col1;
	}

	public void setCol1(Double col1) {
		this.col1 = col1;
	}

	public Double getSol1() {
		return sol1;
	}

	public void setSol1(Double sol1) {
		this.sol1 = sol1;
	}

	public Double getCol2() {
		return col2;
	}

	public void setCol2(Double col2) {
		this.col2 = col2;
	}

	public Double getSol2() {
		return sol2;
	}

	public void setSol2(Double sol2) {
		this.sol2 = sol2;
	}

	public Double getCol3() {
		return col3;
	}

	public void setCol3(Double col3) {
		this.col3 = col3;
	}

	public Double getSol3() {
		return sol3;
	}

	public void setSol3(Double sol3) {
		this.sol3 = sol3;
	}

	public Double getCol4() {
		return col4;
	}

	public void setCol4(Double col4) {
		this.col4 = col4;
	}

	public Double getSol4() {
		return sol4;
	}

	public void setSol4(Double sol4) {
		this.sol4 = sol4;
	}

	public Double getCol5() {
		return col5;
	}

	public void setCol5(Double col5) {
		this.col5 = col5;
	}

	public Double getSol5() {
		return sol5;
	}

	public void setSol5(Double sol5) {
		this.sol5 = sol5;
	}

	public Double getSol6() {
		return sol6;
	}

	public void setSol6(Double sol6) {
		this.sol6 = sol6;
	}

	public Double getCol6() {
		return col6;
	}

	public void setCol6(Double col6) {
		this.col6 = col6;
	}

	public Double getSol7() {
		return sol7;
	}

	public void setSol7(Double sol7) {
		this.sol7 = sol7;
	}

	public Double getCol7() {
		return col7;
	}

	public void setCol7(Double col7) {
		this.col7 = col7;
	}

	public Double getSol8() {
		return sol8;
	}

	public void setSol8(Double sol8) {
		this.sol8 = sol8;
	}

	public Double getCol8() {
		return col8;
	}

	public void setCol8(Double col8) {
		this.col8 = col8;
	}

	public Double getDeduction() {
		return deduction;
	}

	public void setDeduction(Double deduction) {
		this.deduction = deduction;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
	

	


}
