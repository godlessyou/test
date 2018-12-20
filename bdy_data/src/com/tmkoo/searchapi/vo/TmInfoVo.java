package com.tmkoo.searchapi.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public class TmInfoVo {
	private Long id;
	private String regNo;
	private Long intCls;
	private String tmName;
	private String appDate;
	private String applicantCn;
	private String idCardNo;
	private String addressCn;
	private String applicantOther1;
	private String applicantOther2;
	private String applicantEn;
	private String addressEn;
	private String announcementIssue;
	private String announcementDate;
	private String regIssue;
	private String regDate;
	private String privateDateStart;
	private String privateDateEnd;
	private String agent;
	private String category;// 普通商标  特殊商标  集体商标  证明商标
	private String hqzdrq;
	private String gjzcrq;
	private String yxqrq;
	private String color; 
 
	List<JsonSysp> sysps=new ArrayList<JsonSysp>();
	List<JsonSblc> sblcs =new ArrayList<JsonSblc>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRegNo() {
		return regNo;
	}
	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}
	public Long getIntCls() {
		return intCls;
	}
	public void setIntCls(Long intCls) {
		this.intCls = intCls;
	}
	public String getTmName() {
		return tmName;
	}
	public void setTmName(String tmName) {
		this.tmName = tmName;
	}
	public String getAppDate() {
		return appDate;
	}
	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}
	public String getApplicantCn() {
		return applicantCn;
	}
	public void setApplicantCn(String applicantCn) {
		this.applicantCn = applicantCn;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getAddressCn() {
		return addressCn;
	}
	public void setAddressCn(String addressCn) {
		this.addressCn = addressCn;
	}
	public String getApplicantOther1() {
		return applicantOther1;
	}
	public void setApplicantOther1(String applicantOther1) {
		this.applicantOther1 = applicantOther1;
	}
	public String getApplicantOther2() {
		return applicantOther2;
	}
	public void setApplicantOther2(String applicantOther2) {
		this.applicantOther2 = applicantOther2;
	}
	public String getApplicantEn() {
		return applicantEn;
	}
	public void setApplicantEn(String applicantEn) {
		this.applicantEn = applicantEn;
	}
	public String getAddressEn() {
		return addressEn;
	}
	public void setAddressEn(String addressEn) {
		this.addressEn = addressEn;
	}
	public String getAnnouncementIssue() {
		return announcementIssue;
	}
	public void setAnnouncementIssue(String announcementIssue) {
		this.announcementIssue = announcementIssue;
	}
	public String getAnnouncementDate() {
		return announcementDate;
	}
	public void setAnnouncementDate(String announcementDate) {
		this.announcementDate = announcementDate;
	}
	public String getRegIssue() {
		return regIssue;
	}
	public void setRegIssue(String regIssue) {
		this.regIssue = regIssue;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getPrivateDateStart() {
		return privateDateStart;
	}
	public void setPrivateDateStart(String privateDateStart) {
		this.privateDateStart = privateDateStart;
	}
	public String getPrivateDateEnd() {
		return privateDateEnd;
	}
	public void setPrivateDateEnd(String privateDateEnd) {
		this.privateDateEnd = privateDateEnd;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getHqzdrq() {
		return hqzdrq;
	}
	public void setHqzdrq(String hqzdrq) {
		this.hqzdrq = hqzdrq;
	}
	public String getGjzcrq() {
		return gjzcrq;
	}
	public void setGjzcrq(String gjzcrq) {
		this.gjzcrq = gjzcrq;
	}
	public String getYxqrq() {
		return yxqrq;
	}
	public void setYxqrq(String yxqrq) {
		this.yxqrq = yxqrq;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	} 
	public List<JsonSysp> getSysps() {
		return sysps;
	}
	public void setSysps(List<JsonSysp> sysps) {
		this.sysps = sysps;
	}
	public List<JsonSblc> getSblcs() {
		return sblcs;
	}
	public void setSblcs(List<JsonSblc> sblcs) {
		this.sblcs = sblcs;
	}
}
