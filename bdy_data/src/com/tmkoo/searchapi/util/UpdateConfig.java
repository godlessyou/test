package com.tmkoo.searchapi.util;

import org.springframework.stereotype.Component;


@Component
public class UpdateConfig {
	// 公告的更新周期
	private String gonggao_interval="30";
	// 商标的更新周期
	private String tm_interval="30";

	public String getGonggao_interval() {
		return gonggao_interval;
	}

	public void setGonggao_interval(String gonggao_interval) {
		this.gonggao_interval = gonggao_interval;
	}

	public String getTm_interval() {
		return tm_interval;
	}

	public void setTm_interval(String tm_interval) {
		this.tm_interval = tm_interval;
	}
}
