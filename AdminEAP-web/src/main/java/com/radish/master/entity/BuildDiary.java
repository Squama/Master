/**
 * 版权所有 (c) 2017，周庆博和他的朋友们有限公司
 */
package com.radish.master.entity;

import com.cnpc.framework.annotation.Header;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "tbl_builddiary")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class BuildDiary implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * 主键ID自动生成策略
     */
    @Id
    @GenericGenerator(name = "id", strategy = "uuid")
    @GeneratedValue(generator = "id")
    @Column(name = "ID", length = 64)
    protected String id;

    
    @Header(name = "项目内码")
    @Column(name = "xmid")
    private String xmid;
    
    @Header(name = "人员内码")
    @Column(name = "userid")
    private String userid;
    
    @Header(name = "天气")
    @Column(name = "weather")
    private String weather;
    
    @Header(name = "气温")
    @Column(name = "airTemp")
    private String airTemp;
    
    @Header(name = "风力")
    @Column(name = "wind")
    private String wind;
    
    @Header(name = "风向")
    @Column(name = "windFx")
    private String windFx;
    
    @Header(name = "施工内容")
    @Column(name = "sgnr")
    private String sgnr;
    
    @Header(name = "验收人")
    @Column(name = "ysName")
    private String ysName;
    
    @Header(name = "验收存在问题")
    @Column(name = "yswt")
    private String yswt;
    
    @Header(name = "试块制作组数")
    @Column(name = "sk_count")
    private String sk_count;
    
    @Header(name = "试块制作强度")
    @Column(name = "skqd")
    private String skqd;
    
    @Header(name = "试块制作部位")
    @Column(name = "skbw")
    private String skbw;
    
    @Header(name = "自检存在问题")
    @Column(name = "zjwt")
    private String zjwt;
    
    @Header(name = "自检改进措施")
    @Column(name = "zjgjcs")
    private String zjgjcs;
    
    @Header(name = "检查记录")
    @Column(name = "jcjl")
    private String jcjl;
    
    @Header(name = "事故记录")
    @Column(name = "sgjl")
    private String sgjl;
    
    @Header(name = "进场水泥数")
    @Column(name = "jcsn_count")
    private String jcsn_count;
    
    @Header(name = "水泥部位")
    @Column(name = "jcsnbw")
    private String jcsnbw;
    
    @Header(name = "水泥标号")
    @Column(name = "jcsnbh")
    private String jcsnbh;
    
    @Header(name = "水泥产地")
    @Column(name = "jcsncd")
    private String jcsncd;
    
    @Header(name = "停工原因")
    @Column(name = "tgyy")
    private String tgyy;
    
    @Header(name = "设计变更情况")
    @Column(name = "sjbgqk")
    private String sjbgqk;
    
    @Header(name = "土方回填质量情况")
    @Column(name = "tfhtqk")
    private String tfhtqk;
    
    @Header(name = "放线抄平记录")
    @Column(name = "fxcpjl")
    private String fxcpjl;
    
    @Header(name = "技术交底记录")
    @Column(name = "jsjdjl")
    private String jsjdjl;
    
    @Header(name = "养护情况记录")
    @Column(name = "yhqkjl")
    private String yhqkjl;
    
    @Header(name = "试块送压记录")
    @Column(name = "sksyjl")
    private String sksyjl;
    
    @Header(name = "试块送压部位及强度")
    @Column(name = "sksybwqd")
    private String sksybwqd;
    
    @Header(name = "试压报告")
    @Column(name = "sksybg")
    private String sksybg;
    
    @Header(name = "丢失或损失记录")
    @Column(name = "dsjl")
    private String dsjl;
    
    @Header(name = "违纪记录")
    @Column(name = "wjjl")
    private String wjjl;
    
    @Header(name = "会议记录")
    @Column(name = "hyjl")
    private String hyjl;
    
    @Header(name = "机械损坏记录")
    @Column(name = "jxshjl")
    private String jxshjl;
    
    @Header(name = "材料进场情况")
    @Column(name = "cljcjl")
    private String cljcjl;
    
    @Header(name = "日志日期")
    @Column(name = "rzdate")
    private Date rzdate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXmid() {
		return xmid;
	}

	public void setXmid(String xmid) {
		this.xmid = xmid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getAirTemp() {
		return airTemp;
	}

	public void setAirTemp(String airTemp) {
		this.airTemp = airTemp;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getWindFx() {
		return windFx;
	}

	public void setWindFx(String windFx) {
		this.windFx = windFx;
	}

	public String getSgnr() {
		return sgnr;
	}

	public void setSgnr(String sgnr) {
		this.sgnr = sgnr;
	}

	public String getYsName() {
		return ysName;
	}

	public void setYsName(String ysName) {
		this.ysName = ysName;
	}

	public String getYswt() {
		return yswt;
	}

	public void setYswt(String yswt) {
		this.yswt = yswt;
	}

	public String getSk_count() {
		return sk_count;
	}

	public void setSk_count(String sk_count) {
		this.sk_count = sk_count;
	}

	public String getSkqd() {
		return skqd;
	}

	public void setSkqd(String skqd) {
		this.skqd = skqd;
	}

	public String getSkbw() {
		return skbw;
	}

	public void setSkbw(String skbw) {
		this.skbw = skbw;
	}

	public String getZjwt() {
		return zjwt;
	}

	public void setZjwt(String zjwt) {
		this.zjwt = zjwt;
	}

	public String getZjgjcs() {
		return zjgjcs;
	}

	public void setZjgjcs(String zjgjcs) {
		this.zjgjcs = zjgjcs;
	}

	public String getJcjl() {
		return jcjl;
	}

	public void setJcjl(String jcjl) {
		this.jcjl = jcjl;
	}

	public String getSgjl() {
		return sgjl;
	}

	public void setSgjl(String sgjl) {
		this.sgjl = sgjl;
	}

	public String getJcsn_count() {
		return jcsn_count;
	}

	public void setJcsn_count(String jcsn_count) {
		this.jcsn_count = jcsn_count;
	}

	public String getJcsnbw() {
		return jcsnbw;
	}

	public void setJcsnbw(String jcsnbw) {
		this.jcsnbw = jcsnbw;
	}

	public String getJcsnbh() {
		return jcsnbh;
	}

	public void setJcsnbh(String jcsnbh) {
		this.jcsnbh = jcsnbh;
	}

	public String getJcsncd() {
		return jcsncd;
	}

	public void setJcsncd(String jcsncd) {
		this.jcsncd = jcsncd;
	}

	public String getTgyy() {
		return tgyy;
	}

	public void setTgyy(String tgyy) {
		this.tgyy = tgyy;
	}

	public String getSjbgqk() {
		return sjbgqk;
	}

	public void setSjbgqk(String sjbgqk) {
		this.sjbgqk = sjbgqk;
	}

	public String getTfhtqk() {
		return tfhtqk;
	}

	public void setTfhtqk(String tfhtqk) {
		this.tfhtqk = tfhtqk;
	}

	public String getFxcpjl() {
		return fxcpjl;
	}

	public void setFxcpjl(String fxcpjl) {
		this.fxcpjl = fxcpjl;
	}

	public String getJsjdjl() {
		return jsjdjl;
	}

	public void setJsjdjl(String jsjdjl) {
		this.jsjdjl = jsjdjl;
	}

	public String getYhqkjl() {
		return yhqkjl;
	}

	public void setYhqkjl(String yhqkjl) {
		this.yhqkjl = yhqkjl;
	}

	public String getSksyjl() {
		return sksyjl;
	}

	public void setSksyjl(String sksyjl) {
		this.sksyjl = sksyjl;
	}

	public String getSksybwqd() {
		return sksybwqd;
	}

	public void setSksybwqd(String sksybwqd) {
		this.sksybwqd = sksybwqd;
	}

	public String getSksybg() {
		return sksybg;
	}

	public void setSksybg(String sksybg) {
		this.sksybg = sksybg;
	}

	public String getDsjl() {
		return dsjl;
	}

	public void setDsjl(String dsjl) {
		this.dsjl = dsjl;
	}

	public String getWjjl() {
		return wjjl;
	}

	public void setWjjl(String wjjl) {
		this.wjjl = wjjl;
	}

	public String getHyjl() {
		return hyjl;
	}

	public void setHyjl(String hyjl) {
		this.hyjl = hyjl;
	}

	public String getJxshjl() {
		return jxshjl;
	}

	public void setJxshjl(String jxshjl) {
		this.jxshjl = jxshjl;
	}

	public String getCljcjl() {
		return cljcjl;
	}

	public void setCljcjl(String cljcjl) {
		this.cljcjl = cljcjl;
	}

	public Date getRzdate() {
		return rzdate;
	}

	public void setRzdate(Date rzdate) {
		this.rzdate = rzdate;
	}
    
    
}
