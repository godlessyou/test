package com.tmkoo.searchapi.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AppDataStatus {

	String appName;
	
	int totalCount;
	
	int updateCount;
	
	Date modifyDate;
	
	public int getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	
	
	//@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	public Date getModifyDate() {
		return modifyDate;
	}
	
	
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	
	public String getAppName() {
		return appName;
	}
	
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	
	public int getUpdateCount() {
		return updateCount;
	}
	
	
	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}
	
}
