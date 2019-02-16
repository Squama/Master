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
* dongyan      2019年2月16日    Create this file
 * </pre>
 * 
 */

public class AssetsApplyVO {

    private String purID;

    private String faType;

    private String assetsStkID;

    private String num;

    public String getPurID() {
        return purID;
    }

    public void setPurID(String purID) {
        this.purID = purID;
    }

    public String getFaType() {
        return faType;
    }

    public void setFaType(String faType) {
        this.faType = faType;
    }

    public String getAssetsStkID() {
        return assetsStkID;
    }

    public void setAssetsStkID(String assetsStkID) {
        this.assetsStkID = assetsStkID;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

}
