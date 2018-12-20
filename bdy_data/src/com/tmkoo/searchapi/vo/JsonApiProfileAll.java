package com.tmkoo.searchapi.vo;
import java.util.List;

public class JsonApiProfileAll {
	private String ret;
	private String msg;
	private String priceCategory; 
	private String restCount;
	private String validDate; 
	private List<JsonApiTodayUseDetail> todayUserDetails;//今天各个接口使用情况
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
	public String getPriceCategory() {
		return priceCategory;
	}
	public void setPriceCategory(String priceCategory) {
		this.priceCategory = priceCategory;
	}
	public String getRestCount() {
		return restCount;
	}
	public void setRestCount(String restCount) {
		this.restCount = restCount;
	}
	public String getValidDate() {
		return validDate;
	}
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	public List<JsonApiTodayUseDetail> getTodayUserDetails() {
		return todayUserDetails;
	}
	public void setTodayUserDetails(List<JsonApiTodayUseDetail> todayUserDetails) {
		this.todayUserDetails = todayUserDetails;
	}
}
