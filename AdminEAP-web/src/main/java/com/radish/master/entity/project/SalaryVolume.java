/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.entity.project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2018年5月14日    Create this file
* </pre>
* 
*/
@Entity
@Table(name = "tbl_salary_volume")
public class SalaryVolume extends BaseEntity{

    private static final long serialVersionUID = -248429658275517836L;

    @Header(name = "工资单ID")
    @Column(name = "salary_id")
    private String salaryID;
    
    @Header(name = "工程量清单ID")
    @Column(name = "volume_id")
    private String volumeID;

    public String getSalaryID() {
        return salaryID;
    }

    public void setSalaryID(String salaryID) {
        this.salaryID = salaryID;
    }

    public String getVolumeID() {
        return volumeID;
    }

    public void setVolumeID(String volumeID) {
        this.volumeID = volumeID;
    }
    
}
