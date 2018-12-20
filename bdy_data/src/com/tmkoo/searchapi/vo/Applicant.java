package com.tmkoo.searchapi.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Applicant {
	private Integer id;
	
	private Integer mainAppId;

	private String applicantName;

	private String applicantAddress;

	private String applicantEnName;

	private String applicantEnAddress;

	private String usertName;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date modifyTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName == null ? null : applicantName
				.trim();
	}

	public String getApplicantAddress() {
		return applicantAddress;
	}

	public void setApplicantAddress(String applicantAddress) {
		this.applicantAddress = applicantAddress == null ? null
				: applicantAddress.trim();
	}

	public String getApplicantEnName() {
		return applicantEnName;
	}

	public void setApplicantEnName(String applicantEnName) {
		this.applicantEnName = applicantEnName == null ? null : applicantEnName
				.trim();
	}

	public String getApplicantEnAddress() {
		return applicantEnAddress;
	}

	public void setApplicantEnAddress(String applicantEnAddress) {
		this.applicantEnAddress = applicantEnAddress == null ? null
				: applicantEnAddress.trim();
	}

	public String getUsertName() {
		return usertName;
	}

	public void setUsertName(String usertName) {
		this.usertName = usertName == null ? null : usertName.trim();
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getMainAppId() {
		return mainAppId;
	}

	public void setMainAppId(Integer mainAppId) {
		this.mainAppId = mainAppId;
	}

}