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
@Table(name = "tbl_proEditLettersLook")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class LettersLook implements Serializable {


	private static final long serialVersionUID = 7064265855870717810L;
	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String ID;

    
    @Header(name = "查看人内码")
    @Column(name = "userid")
    private String userid;


    @Header(name = "姓名")
    @Column(name = "username")
    private String username;

    @Header(name = "函件id")
    @Column(name = "lettersid")
    private String lettersid;

    @Header(name = "查看时间")
    @Column(name = "lookTime")
    private Date lookTime;

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLettersid() {
		return lettersid;
	}

	public void setLettersid(String lettersid) {
		this.lettersid = lettersid;
	}

	public Date getLookTime() {
		return lookTime;
	}

	public void setLookTime(Date lookTime) {
		this.lookTime = lookTime;
	}


}
