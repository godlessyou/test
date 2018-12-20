package com.tmkoo.searchapi.vo;

import java.util.List;

public class TmDataStatus {

	
	int totalTmCount;
	
	int updateTmCount;
	
	
	List<AppDataStatus> appDataStatusList;
	

	public List<AppDataStatus> getAppDataStatusList() {
		return appDataStatusList;
	}

	public void setAppDataStatusList(List<AppDataStatus> appDataStatusList) {
		this.appDataStatusList = appDataStatusList;
	}

	public int getTotalTmCount() {
		return totalTmCount;
	}

	public void setTotalTmCount(int totalTmCount) {
		this.totalTmCount = totalTmCount;
	}

	public int getUpdateTmCount() {
		return updateTmCount;
	}

	public void setUpdateTmCount(int updateTmCount) {
		this.updateTmCount = updateTmCount;
	}


	
	
	
	
}
