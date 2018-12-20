package com.tmkoo.searchapi.vo;

import java.util.List;

public class JsonTmGongGaoInfo {
	// 客户Id
	private Integer custId;

	// 商标注册号
	private String regNumber;
		
	// 商标公告
	private List<JsonSbGongGao> gonggaos;
	
	//本天内，本接口的剩余允许调用次数
	private String remainCount;
	
	//如果ret=1，会有相应的错误信息提示
	private String msg;
	
	//返回码。0-成功  1-失败
	private String ret;

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	

	public List<JsonSbGongGao> getGonggaos() {
		return gonggaos;
	}

	public void setGonggaos(List<JsonSbGongGao> gonggaos) {
		this.gonggaos = gonggaos;
	}
	
	
	
	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getRemainCount() {
		return remainCount;
	}

	public void setRemainCount(String remainCount) {
		this.remainCount = remainCount;
	}

}
