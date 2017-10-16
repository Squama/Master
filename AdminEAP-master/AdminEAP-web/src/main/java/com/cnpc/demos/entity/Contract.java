package com.cnpc.demos.entity;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "tbl_contract")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Contract extends BaseEntity {

    private static final long serialVersionUID = 6093546087036436584L;
    @Header(name = "合同名称")
    @Column(name = "name")
    private String name;
    @Header(name = "合同编号")
    @Column(name = "contract_number")
    private String contractNumber;
    @Header(name = "甲方")
    @Column(name = "party_a")
    private String partyA;
    @Header(name = "乙方")
    @Column(name = "party_b")
    private String partyB;
    @Header(name = "合同金额")
    @Column(name = "contract_amount")
    private BigDecimal contractAmount;
    @Header(name = "合同地址")
    @Column(name = "file_url")
    private String fileUrl;
    @Header(name = "文件名称")
    @Column(name = "file_name")
    private String fileName;
    @Header(name = "状态")
    @Column(name = "status")
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public String getPartyB() {
        return partyB;
    }

    public void setPartyB(String partyB) {
        this.partyB = partyB;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
