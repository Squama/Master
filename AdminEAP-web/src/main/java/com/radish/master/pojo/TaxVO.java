/**
 * 版权所有 (c) 2019，中金支付有限公司  
 */
package com.radish.master.pojo;

/**
 * 类说明
 * 
 * <pre>
* Modify Information:
* Author        Date          Description
* ============ =========== ============================
* dongyan      2019年5月13日    Create this file
 * </pre>
 * 
 */

public class TaxVO {

    private String starter;

    private String ender;

    private String rate;

    private String deduction;

    public String getStarter() {
        return starter;
    }

    public void setStarter(String starter) {
        this.starter = starter;
    }

    public String getEnder() {
        return ender;
    }

    public void setEnder(String ender) {
        this.ender = ender;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

}
