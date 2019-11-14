/**
 * 版权所有 (c) 2017，中金支付有限公司  
 */
package com.radish.master.pojo;

import javax.persistence.Entity;

/**
* 类说明
* 
* <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2017年12月5日    Create this file
* </pre>
* 
*/
@Entity
public class AllVolume {
    
    private Double final_mech;
    
    private Double final_labour;
    
    private Double final_mat;
    
    private Double final_debit;
    
    private Double final_sub;

	public Double getFinal_mech() {
		return final_mech;
	}

	public void setFinal_mech(Double final_mech) {
		this.final_mech = final_mech;
	}

	public Double getFinal_labour() {
		return final_labour;
	}

	public void setFinal_labour(Double final_labour) {
		this.final_labour = final_labour;
	}

	public Double getFinal_mat() {
		return final_mat;
	}

	public void setFinal_mat(Double final_mat) {
		this.final_mat = final_mat;
	}

	public Double getFinal_debit() {
		return final_debit;
	}

	public void setFinal_debit(Double final_debit) {
		this.final_debit = final_debit;
	}

	public Double getFinal_sub() {
		return final_sub;
	}

	public void setFinal_sub(Double final_sub) {
		this.final_sub = final_sub;
	}

    
}
