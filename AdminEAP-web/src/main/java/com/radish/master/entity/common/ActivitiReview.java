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
@Table(name = "tbl_activiti_review")
public class ActivitiReview extends BaseEntity {

    private static final long serialVersionUID = -4253541714439144619L;

    @Header(name = "业务相关表主键")
    @Column(name = "business_key")
    private String businessKey;

    @Header(name = "业务流节点名称")
    @Column(name = "task_node")
    private String taskNode;

    @Header(name = "审批结果")
    @Column(name = "approve")
    private String approve;

    @Header(name = "审批意见")
    @Column(name = "suggestion")
    private String suggestion;
    
    @Header(name = "风险")
    @Column(name = "risk")
    private String risk;
    
    @Header(name = "操作人")
    @Column(name = "operator")
    private String operator;
    
    

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getTaskNode() {
        return taskNode;
    }

    public void setTaskNode(String taskNode) {
        this.taskNode = taskNode;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

}
