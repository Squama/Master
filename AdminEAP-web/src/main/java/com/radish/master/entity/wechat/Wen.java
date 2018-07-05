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
* dongyan      2018年7月5日    Create this file
 * </pre>
 * 
 */
@Entity
@Table(name = "tbl_wen")
public class Wen extends BaseEntity {

    private static final long serialVersionUID = -1272754979289656394L;

    @Header(name = "病理号")
    @Column(name = "bingli")
    private String bingli;

    @Header(name = "病案号")
    @Column(name = "bingan")
    private String bingan;

    @Header(name = "性别")
    @Column(name = "sex")
    private String sex;

    @Header(name = "年龄")
    @Column(name = "age")
    private String age;

    @Header(name = "送检日期")
    @Column(name = "getdate")
    private String getdate;

    @Header(name = "诊断日期")
    @Column(name = "zhenduan")
    private String zhenduan;

    @Header(name = "诊断结论")
    @Column(name = "jielun")
    private String jielun;

    @Header(name = "标本类型")
    @Column(name = "biaoben")
    private String biaoben;

    @Header(name = "报告类型")
    @Column(name = "baogao")
    private String baogao;

    @Header(name = "肿瘤大小数据")
    @Column(name = "shuju")
    private String shuju;

    @Header(name = "最大值")
    @Column(name = "zuida")
    private String zuida;

    public String getBingli() {
        return bingli;
    }

    public void setBingli(String bingli) {
        this.bingli = bingli;
    }

    public String getBingan() {
        return bingan;
    }

    public void setBingan(String bingan) {
        this.bingan = bingan;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGetdate() {
        return getdate;
    }

    public void setGetdate(String getdate) {
        this.getdate = getdate;
    }

    public String getZhenduan() {
        return zhenduan;
    }

    public void setZhenduan(String zhenduan) {
        this.zhenduan = zhenduan;
    }

    public String getJielun() {
        return jielun;
    }

    public void setJielun(String jielun) {
        this.jielun = jielun;
    }

    public String getBiaoben() {
        return biaoben;
    }

    public void setBiaoben(String biaoben) {
        this.biaoben = biaoben;
    }

    public String getBaogao() {
        return baogao;
    }

    public void setBaogao(String baogao) {
        this.baogao = baogao;
    }

    public String getShuju() {
        return shuju;
    }

    public void setShuju(String shuju) {
        this.shuju = shuju;
    }

    public String getZuida() {
        return zuida;
    }

    public void setZuida(String zuida) {
        this.zuida = zuida;
    }

}
