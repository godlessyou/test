package com.tmkoo.searchapi.vo;

import java.util.Date;
import java.util.List;

public class TradeMark {
    private Integer tmId;

    private String regNumber;

    private String tmType;

    private String tmGroup;

    private String tmName;

    private String applicantName;

    private String applicantAddress;

    private String applicantEnName;

    private String applicantEnAddress;

    private String gtApplicantName;

    private String gtApplicantAddress;

    private String status;

    private Date appDate;

    private String approvalNumber;

    private Date approvalDate;

    private String regnoticeNumber;

    private Date regNoticeDate;

    private Date validStartDate;

    private Date validEndDate;

    private String tmCategory;

    private String agent;

    private String classify;

    private String tmImage;
    
    private String imgFileUrl;
    
    private String imgFilePath;
    
    
    private String tmStatus;
    
    private Date hqzdrq;//后期指定日期
	private Date gjzcrq;//国际注册日期
	private String yxqrq;//优先权日期
    

	List<TradeMarkCategory> tradeMarkCategoryList;
	

	public List<TradeMarkCategory> getTradeMarkCategoryList() {
		return tradeMarkCategoryList;
	}

	public void setTradeMarkCategoryList(
			List<TradeMarkCategory> tradeMarkCategoryList) {
		this.tradeMarkCategoryList = tradeMarkCategoryList;
	}

	public Integer getTmId() {
        return tmId;
    }

    public void setTmId(Integer tmId) {
        this.tmId = tmId;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber == null ? null : regNumber.trim();
    }

    public String getTmType() {
        return tmType;
    }

    public void setTmType(String tmType) {
        this.tmType = tmType == null ? null : tmType.trim();
    }

    public String getTmGroup() {
        return tmGroup;
    }

    public void setTmGroup(String tmGroup) {
        this.tmGroup = tmGroup == null ? null : tmGroup.trim();
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName == null ? null : tmName.trim();
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName == null ? null : applicantName.trim();
    }

    public String getApplicantAddress() {
        return applicantAddress;
    }

    public void setApplicantAddress(String applicantAddress) {
        this.applicantAddress = applicantAddress == null ? null : applicantAddress.trim();
    }

    public String getApplicantEnName() {
        return applicantEnName;
    }

    public void setApplicantEnName(String applicantEnName) {
        this.applicantEnName = applicantEnName == null ? null : applicantEnName.trim();
    }

    public String getApplicantEnAddress() {
        return applicantEnAddress;
    }

    public void setApplicantEnAddress(String applicantEnAddress) {
        this.applicantEnAddress = applicantEnAddress == null ? null : applicantEnAddress.trim();
    }

    public String getGtApplicantName() {
        return gtApplicantName;
    }

    public void setGtApplicantName(String gtApplicantName) {
        this.gtApplicantName = gtApplicantName == null ? null : gtApplicantName.trim();
    }

    public String getGtApplicantAddress() {
        return gtApplicantAddress;
    }

    public void setGtApplicantAddress(String gtApplicantAddress) {
        this.gtApplicantAddress = gtApplicantAddress == null ? null : gtApplicantAddress.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getAppDate() {
        return appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public String getApprovalNumber() {
        return approvalNumber;
    }

    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber == null ? null : approvalNumber.trim();
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getRegnoticeNumber() {
        return regnoticeNumber;
    }

    public void setRegnoticeNumber(String regnoticeNumber) {
        this.regnoticeNumber = regnoticeNumber == null ? null : regnoticeNumber.trim();
    }

    public Date getRegNoticeDate() {
        return regNoticeDate;
    }

    public void setRegNoticeDate(Date regNoticeDate) {
        this.regNoticeDate = regNoticeDate;
    }

    public Date getValidStartDate() {
        return validStartDate;
    }

    public void setValidStartDate(Date validStartDate) {
        this.validStartDate = validStartDate;
    }

    public Date getValidEndDate() {
        return validEndDate;
    }

    public void setValidEndDate(Date validEndDate) {
        this.validEndDate = validEndDate;
    }

    public String getTmCategory() {
        return tmCategory;
    }

    public void setTmCategory(String tmCategory) {
        this.tmCategory = tmCategory == null ? null : tmCategory.trim();
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent == null ? null : agent.trim();
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify == null ? null : classify.trim();
    }

    public String getTmImage() {
        return tmImage;
    }

    public void setTmImage(String tmImage) {
        this.tmImage = tmImage == null ? null : tmImage.trim();
    }

	public String getImgFileUrl() {
		return imgFileUrl;
	}

	public void setImgFileUrl(String imgFileUrl) {
		this.imgFileUrl = imgFileUrl;
	}

	public String getImgFilePath() {
		return imgFilePath;
	}

	public void setImgFilePath(String imgFilePath) {
		this.imgFilePath = imgFilePath;
	}

	public String getTmStatus() {
		return tmStatus;
	}

	public void setTmStatus(String tmStatus) {
		this.tmStatus = tmStatus;
	}

	public Date getHqzdrq() {
		return hqzdrq;
	}

	public void setHqzdrq(Date hqzdrq) {
		this.hqzdrq = hqzdrq;
	}

	public Date getGjzcrq() {
		return gjzcrq;
	}

	public void setGjzcrq(Date gjzcrq) {
		this.gjzcrq = gjzcrq;
	}

	public String getYxqrq() {
		return yxqrq;
	}

	public void setYxqrq(String yxqrq) {
		this.yxqrq = yxqrq;
	}
	
	
}