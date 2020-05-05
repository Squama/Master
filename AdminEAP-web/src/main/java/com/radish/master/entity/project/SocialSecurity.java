/**
 * 
 */
package com.radish.master.entity.project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.framework.annotation.Header;
import com.cnpc.framework.base.entity.BaseEntity;

/**
 * @author tonyd
 *
 */
@Entity
@Table(name = "tbl_social_security")
public class SocialSecurity extends BaseEntity {

    private static final long serialVersionUID = 1894253821019964551L;

    @Header(name = "社保年")
    @Column(name = "year")
    private String year;

    @Header(name = "社保基数")
    @Column(name = "radix")
    private String radix;

    @Header(name = "公积金基数")
    @Column(name = "prf")
    private String prf;

    @Header(name = "上一年度平均工资")
    @Column(name = "avg")
    private String avg;

    @Header(name = "养老费率")
    @Column(name = "yanglao")
    private String yanglao;

    @Header(name = "医疗费率")
    @Column(name = "yiliao")
    private String yiliao;

    @Header(name = "失业费率")
    @Column(name = "shiye")
    private String shiye;

    @Header(name = "养老企业费率")
    @Column(name = "yanglao_corp")
    private String yanglaoCorp;

    @Header(name = "医疗企业费率")
    @Column(name = "yiliao_corp")
    private String yiliaoCorp;

    @Header(name = "失业企业费率")
    @Column(name = "shiye_corp")
    private String shiyeCorp;

    @Header(name = "工伤费率")
    @Column(name = "gongshang")
    private String gongshang;

    @Header(name = "生育费率")
    @Column(name = "shengyu")
    private String shengyu;

    @Header(name = "公积金费率")
    @Column(name = "gongjijin")
    private String gongjijin;

    @Header(name = "社保年起始时间")
    @Column(name = "start_time")
    private String startTime;

    @Header(name = "社保年截止时间")
    @Column(name = "end_time")
    private String endTime;
    
    @Header(name = "所属地")
    @Column(name = "regionCode")
    private String regionCode;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRadix() {
        return radix;
    }

    public void setRadix(String radix) {
        this.radix = radix;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getYanglao() {
        return yanglao;
    }

    public void setYanglao(String yanglao) {
        this.yanglao = yanglao;
    }

    public String getYiliao() {
        return yiliao;
    }

    public void setYiliao(String yiliao) {
        this.yiliao = yiliao;
    }

    public String getShiye() {
        return shiye;
    }

    public void setShiye(String shiye) {
        this.shiye = shiye;
    }

    public String getGongshang() {
        return gongshang;
    }

    public void setGongshang(String gongshang) {
        this.gongshang = gongshang;
    }

    public String getShengyu() {
        return shengyu;
    }

    public void setShengyu(String shengyu) {
        this.shengyu = shengyu;
    }

    public String getGongjijin() {
        return gongjijin;
    }

    public void setGongjijin(String gongjijin) {
        this.gongjijin = gongjijin;
    }

    public String getPrf() {
        return prf;
    }

    public void setPrf(String prf) {
        this.prf = prf;
    }

    public String getYanglaoCorp() {
        return yanglaoCorp;
    }

    public void setYanglaoCorp(String yanglaoCorp) {
        this.yanglaoCorp = yanglaoCorp;
    }

    public String getYiliaoCorp() {
        return yiliaoCorp;
    }

    public void setYiliaoCorp(String yiliaoCorp) {
        this.yiliaoCorp = yiliaoCorp;
    }

    public String getShiyeCorp() {
        return shiyeCorp;
    }

    public void setShiyeCorp(String shiyeCorp) {
        this.shiyeCorp = shiyeCorp;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

}
