/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.entity.common;

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
* dongyan      2018年4月18日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_task_wechat")
public class TaskWechat extends BaseEntity {

    private static final long serialVersionUID = 6571888576391692804L;

    @Header(name = "流程key")
    @Column(name = "process_definition_key")
    private String processDefinitionKey;

    @Header(name = "微信配置主键")
    @Column(name = "wechat_config_id")
    private String wechatConfigID;

    @Header(name = "发往部门编号")
    @Column(name = "to_party")
    private String toParty;

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinationKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getWechatConfigID() {
        return wechatConfigID;
    }

    public void setWechatConfigID(String wechatConfigID) {
        this.wechatConfigID = wechatConfigID;
    }

    public String getToParty() {
        return toParty;
    }

    public void setToParty(String toParty) {
        this.toParty = toParty;
    }

}
