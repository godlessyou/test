package com.tmkoo.searchapi.vo;

import java.util.List;

import com.tmkoo.searchapi.util.AES;
import com.tmkoo.searchapi.util.DES;
import com.tmkoo.searchapi.util.MyLevenshtein;
import com.tmkoo.searchapi.util.Pinyin;

public class JsonTmInfoList {
	   private String regNo;//"5232144",   //注册号
	   private String intCls;//"9",    //国际分类
	   private String tmImg;//"http;////www.tmkoo.com/pic.php?zch=121b99cebccde101&s=0",  //商标小图片
	   private String tmName;//"万龙腾飞;MONLONG",   //商标名称
	   private String applicantCn;//"陈健光",  //申请人中文名
	   private String appDate;//"2006-03-22",  //申请日期
	   private String currentStatus;//"商标已注册"   //当前流程状态
	   private String announcementIssue;//	初审公告期号
	   private String announcementDate;//	初审公告日期 
	   private String regIssue;//:"1336",//注册公告期号
	   private String regDate;//:"2012-11-21",//注册公告日期 
	   private String agent;//:"无锡创名商标事务所有限公司",
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
		public String getTmImg() {
			return tmImg;
		}
		public void setTmImg(String tmImg) {
			this.tmImg = tmImg;
		}
		public String getTmName() {
			return tmName;
		}
		public void setTmName(String tmName) {
			this.tmName = tmName;
		}
		public String getApplicantCn() {
			return applicantCn;
		}
		public void setApplicantCn(String applicantCn) {
			this.applicantCn = applicantCn;
		}
		public String getAppDate() {
			return appDate;
		}
		public void setAppDate(String appDate) {
			this.appDate = appDate;
		}
		public String getCurrentStatus() {
			return currentStatus;
		}
		public void setCurrentStatus(String currentStatus) {
			this.currentStatus = currentStatus;
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
		public String getAgent() {
			return agent;
		}
		public void setAgent(String agent) {
			this.agent = agent;
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
		
		//===用于在查询结果列表排序用========================
		//===================
		/**
		 *  第一步初步筛查
		 * @return
		 */
		public Long getPpd(String searchKey){
			if(this.tmName == null || this.tmName.trim().equals("") || searchKey==null || searchKey.trim().length() == 0){
				return 0L;
			}
			String newSbwzLs = tmName.replaceAll("及图形", "").replaceAll("图形", "").replaceAll("及图", "");
			if(newSbwzLs.trim().equals("")){
				return 0L;
			} 
			return new Long(computerJsd(tmName.trim(),searchKey)); 
		}
		
		/**
		 * 第二步筛查
		 * @return
		 */
		public Long getJsd(String searchKey){
			//1、先把本商标名，去除 及图 
			String newSbwzLs = tmName.replaceAll("及图形", "").replaceAll("图形", "").replaceAll("及图", "");
			if(newSbwzLs.trim().equals("")){
				return 0L;
			}
			return new Long(computerJsd(tmName,searchKey));
		}
		 
		private int computerJsd(String tmName,String key){
			if(tmName.trim().equals(key.trim())){
				//1、关键词 相同
				return 100;
			}
			else if(tmName.trim().indexOf(key.trim()) >=0){
				//2、包含关键词
				if(tmName.trim().length() - key.trim().length() >=11){
					return 60;
				}
				else {
					return 59+(11-(tmName.trim().length() - key.trim().length()))*4 -(tmName.trim().indexOf(key.trim()));
					//59+(11-长度差)*4-位置号
				} 
			}
			else if(haveSub(tmName.trim(),key.trim())){
				//3、包含部分关键词
				if(tmName.trim().length() == key.trim().length()){
					//读音是否相同
					if(duyinSame(tmName.trim(),key.trim())){
						return 50;
					}
					else{
						return 40;
					}
				}
				else {
					 return MyLevenshtein.similar(tmName.trim(),key.trim());
				}  
			}
			else{
				return 0;
			}
			
			
		}
		private Boolean haveSub(String tmName,String key){
			for(int i=0;i<key.length();i++){
				if(tmName.indexOf(key.substring(i, i+1))>=0){
					return true;
				}
			}
			return false;
		}
		private Boolean duyinSame(String tmName,String key){
			List<String > pinyinSbwzLs= Pinyin.execute(tmName);
			List<String > pinyinKey= Pinyin.execute(key);
			for(String s:pinyinSbwzLs){
				for(String k:pinyinKey){
					if(s.equals(k)){
						return true;
					}
				}
			}
			return false;
		}
		public String getRegNoEn() {
			try {
				return AES.encryptString(regNo);
			} catch (Exception e) {
				e.printStackTrace();
				return regNo;
			}
		} 
		public String getIntClsEn() {
			try {
				return AES.encryptString(intCls);
			} catch (Exception e) {
				e.printStackTrace();
				return intCls;
			}
		} 
}
