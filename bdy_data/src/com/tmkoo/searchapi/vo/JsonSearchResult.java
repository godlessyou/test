package com.tmkoo.searchapi.vo;

import java.util.List;

public class JsonSearchResult {
	private String ret;
	private String msg;
	private String remainCount;
	private String allRecords;
	private List<JsonTmInfoList> results;

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

	public String getAllRecords() {
		return allRecords;
	}

	public void setAllRecords(String allRecords) {
		this.allRecords = allRecords;
	}

	public List<JsonTmInfoList> getResults() {
		return results;
	}

	public void setResults(List<JsonTmInfoList> results) {
		this.results = results;
	}

}
