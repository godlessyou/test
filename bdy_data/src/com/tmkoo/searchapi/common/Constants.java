package com.tmkoo.searchapi.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Constants {
	
	public static String zanwutu="/9j/4AAQSkZJRgABAQEAkACQAAD/4QBaRXhpZgAATU0AKgAAAAgABQMBAAUAAAABAAAASgMDAAEAAAABAAAAAFEQAAEAAAABAQAAAFERAAQAAAABAAAAAFESAAQAAAABAAAAAAAAAAAAAYagAACxj//bAEMAAgEBAgEBAgICAgICAgIDBQMDAwMDBgQEAwUHBgcHBwYHBwgJCwkICAoIBwcKDQoKCwwMDAwHCQ4PDQwOCwwMDP/bAEMBAgICAwMDBgMDBgwIBwgMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDP/AABEIADIAMgMBIgACEQEDEQH/xAAfAAABBQEBAQEBAQAAAAAAAAAAAQIDBAUGBwgJCgv/xAC1EAACAQMDAgQDBQUEBAAAAX0BAgMABBEFEiExQQYTUWEHInEUMoGRoQgjQrHBFVLR8CQzYnKCCQoWFxgZGiUmJygpKjQ1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4eLj5OXm5+jp6vHy8/T19vf4+fr/xAAfAQADAQEBAQEBAQEBAAAAAAAAAQIDBAUGBwgJCgv/xAC1EQACAQIEBAMEBwUEBAABAncAAQIDEQQFITEGEkFRB2FxEyIygQgUQpGhscEJIzNS8BVictEKFiQ04SXxFxgZGiYnKCkqNTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqCg4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2dri4+Tl5ufo6ery8/T19vf4+fr/2gAMAwEAAhEDEQA/AP38ooozQAUUZooAKKKKACiiigD5v+LX7VfjLwF8frbRY/Ct0+lCc6faKtvqL2+svJC04mM8enSkGFYSpig8zJnLFtsTYX4VftG+PfiD8aLfSbjT0j0c3+ouotNGg8uaytrqW0EjXMmp+ZxJsJ22gYkfKpXk+f8A7Y114V8M/tHQ6fdeEvhjHI2mx62+oappVh9qu7iWS4gbe9xqVgsgCqCATKQ2GwCqkbf7GvxRh8S/HCz8P28HheSz0TwrLFaT6WYTJBEtzAPKY2+q36NuJ3FpCshIzk5NTT1t8/nYK3u6Ly/Fr9D2D9pTxZ45+G3gPXfFHhzU/Cq2Wh6c92NP1HRJ7ma4kQEkeel5Eqg8D/VnHPJzx0XgrS/HVrqSzeIPEHhXUrFozmDT/D1xYzBzjB8x72ZcDnI2c+orxv41eBvhz+0H8RZPCPh/wr4R1zxFNfRTeKtch0yCRtFto5QZEluguftUvlmJY928AuzAKvOL8Crj4OfDO202xn8P+Cbj4gL4rvNNt7Gy0+ybWrN21KdYpCnEsccUQVt/8MagrngFxCX5bmh8c/2r/Gfw/k8TSaTqXghLqz14aHoujX2lytcag3kW0ryyXJvoY4o18/5nZAq/ICcsKveDf2kfiBqlxoV5f/2Kum33itPDF1BL4d+yyO22QySQTxapdROEdChOCNwYdVIrlvH2t+G7nUfinpepeOtK0TUh4hkaLQ7zxdD4fjvhJp9knmyv5byOFAk2o6tA54kRgOMLwJrXho2Xwtt7HxtHqGtyeJ7GK48Op4htL6G1WIXW2aG3hublYBsdVIgkSHAXEMXCKo7R/wC3fxtcU9L/AD/C59sA5FFA6UVQwooooAaqhSSMDccnHc9KdRRQBR0zQrPRZrp7OztrSS+nNzcmGFYzcSkBTI5A+ZiFUZPOFHpRqeg2ettbfbLO1u/sU63NuZ4Vk8iVQQsiZHysAThhyMmr1FABRRRQAUUUUAFFFFABRRRQAUUUUAf/2Q==";
	
	//更新商标详细信息完成
	public static final String updateFinish="finish";
	
	//更新商标和公告操作
	public static final String tmAndggOpt="tmandgg";
	
	//更新商标的操作
	public static final String tmOpt="tm";
	
	//更新商标公告的操作
	public static final String gonggaoOpt="gonggao";

	public static final Map<String, String> gonggaotatus = new HashMap<String, String>();
	static {			
		gonggaotatus.put("已注册", "注册公告,注册商标续展公告,注册人名义及地址变更公告,转让/移转公告,转让公告,更正公告,备案公告,驳回复审注册公告排版完成,变更商标申请人/注册人名义/地址完成");
		gonggaotatus.put("已销亡", "撤销公告,未续展商标注销公告,无效公告,注册申请撤回公告");	
		gonggaotatus.put("已初审", "初步审定公告");	
	}

	public static final Map<String, String> tmstatus = new HashMap<String, String>();
	static {			
		tmstatus.put("已注册", "商标已注册,商标注册申请完成,等待注册证发文,商标注册申请注册公告排版完成,商标续展中,商标续展完成,商标转让完成,商标使用许可备案完成,驳回复审注册公告排版完成,变更商标申请人/注册人名义/地址完成,商标变更完成,许可合同备案完成");
		tmstatus.put("已销亡", "商标无效,注册申请部分驳回,排版送达公告(关于撤销连续三年未使用商标的决定)");
		tmstatus.put("已驳回", "等待驳回通知发文,驳回复审评审实审裁文发文,驳回复审评审实审裁文等待实审裁文发文");
		tmstatus.put("待审中", "商标注册申请等待受理中,驳回复审中");
//		tmstatus.put("已初审", "");		
	}
	
	public static final Map<String, String> liuchengstatus = new HashMap<String, String>();
	static {			
		liuchengstatus.put("已注册", "商标已注册,商标注册申请完成,等待注册证发文,商标注册申请注册公告排版完成,商标续展中,商标续展完成,商标转让完成,商标使用许可备案完成,驳回复审注册公告排版完成,变更商标申请人/注册人名义/地址完成,商标变更完成,许可合同备案完成");
		liuchengstatus.put("已销亡", "商标无效,注册申请部分驳回,排版送达公告(关于撤销连续三年未使用商标的决定)");
		liuchengstatus.put("已驳回", "等待驳回通知发文,商标注册申请驳回通知发文,打印驳回或部分驳回通知书,驳回复审评审实审裁文发文,驳回复审评审实审裁文等待实审裁文发文");
		liuchengstatus.put("待审中", "商标注册申请等待受理中,驳回复审中,商标异议申请中,撤销三年不使用待审中");
//		tmstatus.put("已初审", "");		
	}
	

	
	public static final String base64ImageStr ="data:image/png;base64,";
	
	public static final String imageDir = "downloadimage";
	
	public static final String uploadDir = "upload";
	
	public static final String exportDir = "export";
	
	public static final String casefileDir = "mydoc";
	
	public static final String app_prefix = "biaodeyi";

//	public static final String catalina_home = System.getProperty("catalina.home","C:/temp");
	
	public static final String catalina_home = "C:/tmimage";
	
	public static final String ftpConfigFile = "ftp.properties";

	public static String app_dir = catalina_home+ "/"+ app_prefix;

	public static String image_dir = app_dir + "/"+ imageDir;
	
	public static String upload_dir = app_dir + "/" + uploadDir;
	
	public static final String export_dir = app_dir + "/" + exportDir;
	
	public static final String casefile_dir = app_dir + "/"	+ casefileDir;
	
	public static String[] illegalCharacter={"\\", "/", ":", "*", "?", "\"", "<", ">", "|", "\r\n"};
	
	//分页
	public static final Integer LIMIT_NUM = 10;

	public static final String FILE_SEP = System.getProperty("file.separator");

	public static final String USER_HOME = System.getProperty("user.home")
			+ FILE_SEP;

	private static boolean initFlag = false;
	
	public static boolean isInitFlag() {
		return initFlag;
	}
	
	public static void init(String fileUrl) {		
		
		if (!initFlag){	
			if (fileUrl!=null && !fileUrl.equals("")){
				app_dir=fileUrl;
				image_dir = app_dir + "/"+ imageDir;
				upload_dir = app_dir + "/" + uploadDir;
			}			
			initFlag=true;
		}
	}

}
