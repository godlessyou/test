package com.tmkoo.searchapi.vo;
import java.util.List;

public class JsonTmInfo {
	private String ret;
	private String msg;
	private String remainCount; 
	private String id;
	private String tmImg;
	private String regNo;
	private String intCls;
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
	private String privateDate;
	private String agent;
	private String category;
	private String hqzdrq;//后期指定日期
	private String gjzcrq;//国际注册日期
	private String yxqrq;//优先权日期
	private String color;//指定颜色
	private List<JsonSysp> goods;
	private List<JsonSblc>  flow;
	
	private String currentStatus;//"商标已注册"   //当前流程状态

	public String getRet() {
		return ret;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getTmImg() {
		return tmImg;
	}

	public void setTmImg(String tmImg) {
		this.tmImg = tmImg;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getIntCls() {
		return intCls;
	}

	public void setIntCls(String intCls) {
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
		return (idCardNo == null || idCardNo.equals(""))?"0":idCardNo;
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

	public String getPrivateDate() {
		return privateDate;
	}

	public void setPrivateDate(String privateDate) {
		this.privateDate = privateDate;
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

	public List<JsonSysp> getGoods() {
		return goods;
	}

	public void setGoods(List<JsonSysp> goods) {
		this.goods = goods;
	}

	public List<JsonSblc> getFlow() {
		return flow;
	}

	public void setFlow(List<JsonSblc> flow) {
		this.flow = flow;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	 
}
