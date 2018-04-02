/**
 * 版权所有 (c) 2018，中金支付有限公司  
 */
package com.radish.master.entity.wechat;

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
* dongyan      2018年4月2日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_wechat_config")
public class WechatConfig extends BaseEntity {

    private static final long serialVersionUID = -184525400456927354L;

    @Header(name="应用ID")
    @Column(name="agent_id")
    private String agentID;

    @Header(name="应用名称")
    @Column(name="agent_name")
    private String agentName;

    @Header(name="密钥")
    @Column(name="secret")
    private String secret;

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

}
