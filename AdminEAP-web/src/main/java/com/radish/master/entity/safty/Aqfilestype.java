/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity.safty;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 类说明  入库单表
 *
 * <pre>
 * Modify Information:
 * Author        Date          Description
 * ============ =========== ============================
 * zhouqb      2017年10月28日    Create this file
 * </pre>
 *
 */
@Entity
@Table(name = "tbl_aqfilestype")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class Aqfilestype{

    private static final long serialVersionUID = 3309603144734703924L;
    
    @Id
    @Column(name = "id", length = 36)
    protected String id;
    @Column(name = "names")
    private String names;

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getNames() {
	return names;
}

public void setNames(String names) {
	this.names = names;
}

  
}
