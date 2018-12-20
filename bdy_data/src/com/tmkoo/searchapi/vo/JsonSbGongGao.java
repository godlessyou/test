package com.tmkoo.searchapi.vo;

import java.util.Date;

public class JsonSbGongGao {
	//公告日期
	private Date ggDate;	
	//公告类型
	private String ggName;
	//公告期号	
	private String ggQihao;
	//第几页
	private String ggPage;
	
	//用于构造查看公告图片的url
	private String vcode;
	
	
	public Date getGgDate() {
		return ggDate;
	}
	public void setGgDate(Date ggDate) {
		this.ggDate = ggDate;
	}
	
	public String getGgName() {
		return ggName;
	}
	public void setGgName(String ggName) {
		this.ggName = ggName;
	}
	
	public String getGgQihao() {
		return ggQihao;
	}
	public void setGgQihao(String ggQihao) {
		this.ggQihao = ggQihao;
	}
	
	public String getGgPage() {
		return ggPage;
	}
	public void setGgPage(String ggPage) {
		this.ggPage = ggPage;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	

}
