package com.tmkoo.searchapi.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tmkoo.searchapi.common.Constants;
import com.tmkoo.searchapi.vo.Applicant;
import com.tmkoo.searchapi.vo.JsonTmGongGaoInfo;
import com.tmkoo.searchapi.vo.JsonTmInfo;
import com.tmkoo.searchapi.vo.JsonTmInfoList;
import com.tmkoo.searchapi.vo.TradeMark;
import com.tmkoo.searchapi.vo.TradeMarkCategory;
import com.tmkoo.searchapi.vo.TradeMarkProcess;

@Component
// Spring Service Bean的标识.
// @Transactional
public class TmDataUpdater extends TmDataProcessor {

	// 标志位，当值为true：代表自动更新，即：更新所有符合条件的申请人的商标
	public boolean updateAll = false;

	public SearchUtil searchUtil = new SearchUtil();

	public static String exceptionMsg = "exception-";

	private static String sysMsg1 = "超过了最大40000次的限制";

	private static String sysMsg2 = "商标详情接口的查询次数配额不足";

	private static String sysMsg3 = "api.tmkoo.com";

	private static Logger logger = Logger.getLogger(TmDataUpdater.class);

	private final static String[] zimuList = { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };

	protected int getTmCountByAppName(String appName) throws Exception {

		int total = 0;

		// 从外部接口获取该申请人的商标列表
		List<JsonTmInfoList> JsonTmInfoList = getTradeMarkList(appName,
				searchUtil);

		if (JsonTmInfoList != null) {
			total = JsonTmInfoList.size();
		}

		return total;
	}

	// 从API接口获取商标数据，并插入到数据库中
	protected List<TradeMark> getTradeMarkData(String tmName, String tmType) {

		List<TradeMark> list = new ArrayList<TradeMark>();

		try {

			// 从外部接口获取该申请人的商标列表
			List<JsonTmInfoList> JsonTmInfoList = getTradeMarkByName(tmName,
					tmType, searchUtil);

			if (JsonTmInfoList == null) {
				return list;
			}

			for (JsonTmInfoList jsonTmInfoList : JsonTmInfoList) {
				// 商标注册号
				String regNumber = jsonTmInfoList.getRegNo();
				// 商标国际分类号
				String tradeMarkType = jsonTmInfoList.getIntCls();
				// 商标名称
				String tradeMarkName = jsonTmInfoList.getTmName();
				// 申请人名称
				String applicantName=jsonTmInfoList.getApplicantCn();
				
			
				// 检查商标注册号和商标国际分类号是否为空
				// if (regNumber == null || regNumber.trim().equals("")
				// || tradeMarkType == null || tradeMarkType.trim().equals(""))
				// {
				// continue;
				// }

				// 申请号为空的商标有可能是已经有申请号，但还没有注册完成的商标，这样的商标数据也是需要的。
				if (tradeMarkType == null || tradeMarkType.trim().equals("")) {
					continue;
				}

				// 因为调用接口是模糊查询，有可能查出与输入的参数中的商标名称不完全相同的数据
				// 所以，去掉这些商标，返回完全相同的上i包
				if (tradeMarkName == null
						|| !tradeMarkName.trim().equals(tmName)) {
					continue;
				}

				// 将查询结果保存到TradeMark对象
				TradeMark tm = new TradeMark();

				// 商标注册号
				tm.setRegNumber(regNumber);

				// 商标国际分类号
				tm.setTmType(tradeMarkType);
				
				// 申请人名称
				tm.setApplicantName(applicantName);

				list.add(tm);
			}

			// 对数据更新操作进行记录
			//
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	
	
	// 从API接口获取商标数据
	protected TradeMark searchTradeMarkInfo(String regNumber) {
		// 将查询结果保存到TradeMark对象
		try {
	
			// 从外部接口获取该申请人的商标列表
			List<JsonTmInfoList> list = getTradeMarkListByRegNumber(regNumber,
					searchUtil);		

			if (list == null || list.size()==0) {				
				return null;
			}
			
			for(JsonTmInfoList jsonTmInfoList: list){
				
				TradeMark tm = new TradeMark();					
								
				// 商标国际分类号
				String tmType = jsonTmInfoList.getIntCls();	
				
				// 商标状态
				String status = jsonTmInfoList.getCurrentStatus();
				
				// 检查商标国际分类号是否为空
				if (tmType == null || tmType.trim().equals("")) {
					continue;
				}
				
				
				// 商标注册号
				tm.setRegNumber(regNumber);
				
				// 商标国际分类号
				tm.setTmType(tmType);		
				
				JsonTmInfo jsonTmInfo = getTmDetail(regNumber, tmType, searchUtil);				
				
				if (jsonTmInfo!=null){
					
					// 商标名称					
					tm.setTmName(jsonTmInfo.getTmName());

					// 处理获取到的其它商标数据
					processTmInfo(tm, jsonTmInfo, status);
					
				}else{
					
					// 商标名称				
					tm.setTmName(jsonTmInfoList.getTmName());
			
					// 申请人
					String applicantCn = jsonTmInfoList.getApplicantCn();		
						
					// 初审公告期号
					String approvalNumber = jsonTmInfoList.getAnnouncementIssue();
						
					// 代理人
					tm.setAgent(jsonTmInfoList.getAgent());
		
					// 处理商标的申请人
					processAppName(applicantCn, tm);
		
					
					// 初审公告日期
					String approvalDate = jsonTmInfoList.getAnnouncementDate();
					if (approvalDate != null && !approvalDate.equals("")) {
						Date date = DateTool.StringToDate(approvalDate);
						tm.setApprovalDate(date);
					}
		
					tm.setApprovalNumber(approvalNumber);
		
					// 申请日期
					String appDate = jsonTmInfoList.getAppDate();
					if (appDate != null && !appDate.equals("")) {
						Date date = DateTool.StringToDate(appDate);
						tm.setAppDate(date);
					}
		
					// 注册公告日期
					String regDate = jsonTmInfoList.getRegDate();
					if (regDate != null && !regDate.equals("")) {
						Date date = DateTool.StringToDate(regDate);
						tm.setRegNoticeDate(date);
					}
		
					// 注册公告期号
					tm.setRegnoticeNumber(jsonTmInfoList.getRegIssue());		
					
				}
				
				return tm;
			}			

		} catch (Exception e) {
			e.printStackTrace();			
		}

		
		return null;
	}
	
	
	
	// 从API接口获取商标数据
	protected TradeMark queryTradeMarkInfo(String regNumber, String tmType2) {
	
		
		String tempDir = "";
		String imagePath = Constants.image_dir  ;
		//创建临时目录
		FileUtil.createFolderIfNotExists(imagePath);
				
		List<TradeMark> tmList=new ArrayList<TradeMark>();
		try {
			
			// 检查商标注册号和商标国际分类号是否为空
			if (regNumber == null || regNumber.trim().equals("")
					|| tmType2 == null || tmType2.trim().equals("")) {
				return null;
			}
			
			
			// 从外部接口获取该商标的数据
			List<JsonTmInfoList> list = getTradeMarkListByRegNumber(regNumber,
					searchUtil);		

			if (list == null || list.size()==0) {				
				return null;
			}
			
			JsonTmInfoList jsonTmInfoList=list.get(0);		
			// 商标状态
			String status = jsonTmInfoList.getCurrentStatus();
			
			// 商标图片名，用来从接口下载图片
			String imgFileUrl = jsonTmInfoList.getTmImg();	
						
			int number=0;
			StringTokenizer idtok3 = new StringTokenizer(tmType2, ",");					
			while (idtok3.hasMoreTokens()) {
								
				TradeMark tm = new TradeMark();	
				// 商标注册号
				tm.setRegNumber(regNumber);	
			
				String tmType= idtok3.nextToken();
	
				JsonTmInfo jsonTmInfo = null;
				
				int tryTime=3;	
				
				while(tryTime>0){
					try{
						jsonTmInfo = getTmDetail(regNumber, tmType, searchUtil);
						if (jsonTmInfo == null) {
							//由于商标注册号不正确，导致获取的商标数据为空
		                    //使用修改后的商标注册号获取数据
							int exceptionCount = getTmInfoWithOtherRegNumber(jsonTmInfo,
									regNumber, tmType, 0);
						}
					} catch (Exception e) {			
						String message=e.getMessage();
						if (message.indexOf("Connection timed out")>-1){
							logger.info(message);
						}else{
							throw e;
						}	
					} finally{						
						tryTime--;					
					}					
				}

				//如果仍然无法获取的商标数据，继续处理下一个商标
				if (jsonTmInfo == null) {
					return null;
				}
				
				String fileName = regNumber + ".jpg";
				String filePath = imagePath + "/" + fileName;

				// 检查本地目录是否存在商标图样
				boolean imageExist = imageExist(filePath);							
				int beginIndex=Constants.image_dir.length();
				String imgFilePath=filePath.substring(beginIndex);

				// 判断是否需要更新商标图样相关数据
				boolean needUpdateImage = false;
				if (!imageExist) {
					needUpdateImage=true;
				}
				
				// 处理商标图片数据				
				if (needUpdateImage) {
					if (imgFileUrl == null || imgFileUrl.equals("")) {
						imgFileUrl = jsonTmInfo.getTmImg();
					}
					try {
						String photo = getPhoto(tm, imageExist, imagePath, tempDir,
								imgFilePath, imgFileUrl);
//							if (photo == null) {
//								needUpdateImage = false;
//							}
					} catch (Exception e) {
						needUpdateImage = false;
						e.printStackTrace();
					}
				}
				
				
				
				List<TradeMarkCategory> tradeMarkCategoryList =null;
				
				if (number==0){
					// 处理商标名称
					String tmName = jsonTmInfo.getTmName();
					tm.setTmName(tmName);

					// 处理获取到的其它商标数据
					processTmInfo(tm, jsonTmInfo, status);
					
					// 商品/服务数据
					tradeMarkCategoryList = getTradeMarkCategoryList(jsonTmInfo, tm, regNumber, tmType);
			
					tm.setTradeMarkCategoryList(tradeMarkCategoryList);
				
				}else{
					// 商品/服务数据
					tradeMarkCategoryList = getTradeMarkCategoryList(jsonTmInfo, tm, regNumber, tmType);
					tm.setTradeMarkCategoryList(tradeMarkCategoryList);
				}			
			
				tm.setTmType(tmType2);				
				
				//该商品/描述信息已经插入到商品/服务表，在商标表中可以不用再保存一份，所以，设置为null
				tm.setTmCategory(null);		
				
				tmList.add(tm);
			
				number++;
			}

			TradeMark trademark= new TradeMark();
			
			int count=0;
			for(TradeMark tm:tmList){
				if(count==0){
					trademark=tm;
				}else{
					List<TradeMarkCategory> tmcList=trademark.getTradeMarkCategoryList();
					List<TradeMarkCategory> tmcList2=tm.getTradeMarkCategoryList();
					
					if (tmcList2!=null){
						if (tmcList!=null){
							tmcList.addAll(tmcList2);							
						}else{
							tm.setTradeMarkCategoryList(tmcList2);
						}
					}
				
					String tmGroup=trademark.getTmGroup();
					String tmGroup2=tm.getTmGroup();
					if (tmGroup2!=null && !tmGroup2.equals("")){
						if (tmGroup!=null && !tmGroup.equals("")){							
							if (!tmGroup.endsWith(";")){
								tmGroup=tmGroup+";";
							}
							tmGroup=tmGroup+tmGroup2;
						}else{
							tmGroup=tmGroup2;
						}					    
					    trademark.setTmGroup(tmGroup);
					}
				}				
				count++;				
			}
			
			return trademark;

		} catch (Exception e) {			
			e.printStackTrace();			
			
		}

		
		return null;
		
	}
		
		
		

	// 从API接口获取商标数据，并插入到数据库中
	protected String insertTradeMarkData(List<TradeMark> tmList, String appName) {

		String message = "success";

		String opt = Constants.tmOpt;

		int total = 0;

		try {

			String newAppName = processAppName(appName);

			// 从外部接口获取该申请人的商标列表
			List<JsonTmInfoList> JsonTmInfoList = getTradeMarkList(newAppName,
					searchUtil);

			// 获取数据库中该客户的所有商标的注册号和国际分类号，这个地方需要根据上面调用的外部接口来确定采用哪种方式的实现

			// 第一种方式：当调用外部的search接口，它是按照模糊查询的方式来查找， 例如：
			// 按照申请人“勐海茶厂”从接口获取该申请人的商标列表时，结果返回了367个商标，包括：
			// 勐海茶厂， 勐海茶厂（普通台伙）， 勐海茶厂（普通合伙）的商标数据。
			// 这种情况下，需要调用getCustomerTmList获取数据库中该客户的所有申请人的商标，
			// 在查重逻辑中，才能实现对勐海茶厂（普通台伙）， 勐海茶厂（普通合伙）的商标的检查，避免重复插入数据。

			// List<TradeMark> tmList = getCustomerTmList();

			// 第二种方式：当调用外部的sqr-tm-list接口，它是按照精确查询的方式来查找
			// 这种情况下，可以调用getTradeMarkList，只从数据库获取当前申请人的商标
			// List<TradeMark> tmList = getTradeMarkList(custId, appName);

			if (JsonTmInfoList == null) {
				message = message + "-从API接口获取不到该申请人的商标数据";
				return message;
			}

			for (JsonTmInfoList jsonTmInfoList : JsonTmInfoList) {
				// 商标注册号
				String regNumber = jsonTmInfoList.getRegNo();
				// 商标国际分类号
				String tmType = jsonTmInfoList.getIntCls();
				// 商标状态
				String status = jsonTmInfoList.getCurrentStatus();

				// 申请人
				String applicantCn = jsonTmInfoList.getApplicantCn();

				// 下载图片所用的url中的图片位置
				String imageFileUrl = jsonTmInfoList.getTmImg();

				// 初审公告期号
				String approvalNumber = jsonTmInfoList.getAnnouncementIssue();

				// 检查商标注册号和商标国际分类号是否为空
				if (regNumber == null || regNumber.trim().equals("")
						|| tmType == null || tmType.trim().equals("")) {
					continue;
				}

				// 因为调用接口是模糊查询，有可能查出与申请人表中的申请人不完全相同的数据
				// 所以，需要接口返回的数据中的申请人是否与申请人表中的申请人完全相同
				if (applicantCn == null
						|| !applicantCn.trim().equals(newAppName)) {
					continue;
				}

				// 查看数据库中是否已经插入了相同注册号，并且国际分类号也相同的商标
				boolean hasSameTradeMark = false;

				Integer tmId = null;
				for (TradeMark tm : tmList) {
					String regNum = tm.getRegNumber();
					String tmType2 = tm.getTmType();
					tmId = tm.getTmId();
					if (regNum != null && regNum.equals(regNumber)) {						
						StringTokenizer idtok3 = new StringTokenizer(tmType2, ",");					
						while (idtok3.hasMoreTokens()) {
							String type= idtok3.nextToken();
							if (type != null && type.equals(tmType)) {
								hasSameTradeMark = true;
								break;
							}
						}
						
					}
					if (hasSameTradeMark) {
						break;
					}
				}

				if (hasSameTradeMark) {

					// 获取商标状态
					String tmStatus = getTmStatus(status, approvalNumber);

					// 更新商标状态和下载图片所用的url中的图片位置
					updateTradeMarkStatus(tmId, tmStatus, imageFileUrl);
					continue;
				}

				// 将查询结果保存到TradeMark对象
				TradeMark tm = new TradeMark();

				// // 客户Id
				// tm.setCustid(custId);

				// 商标注册号
				tm.setRegNumber(regNumber);

				// 商标国际分类号
				tm.setTmType(jsonTmInfoList.getIntCls());

				// 代理人
				tm.setAgent(jsonTmInfoList.getAgent());

				// 处理商标的申请人
				processAppName(applicantCn, tm);

				// 商标文件名
				tm.setImgFileUrl(imageFileUrl);

				// 初审公告日期
				String approvalDate = jsonTmInfoList.getAnnouncementDate();
				if (approvalDate != null && !approvalDate.equals("")) {
					Date date = DateTool.StringToDate(approvalDate);
					tm.setApprovalDate(date);
				}

				tm.setApprovalNumber(approvalNumber);

				// 申请日期
				String appDate = jsonTmInfoList.getAppDate();
				if (appDate != null && !appDate.equals("")) {
					Date date = DateTool.StringToDate(appDate);
					tm.setAppDate(date);
				}

				// 注册公告日期
				String regDate = jsonTmInfoList.getRegDate();
				if (regDate != null && !regDate.equals("")) {
					Date date = DateTool.StringToDate(regDate);
					tm.setRegNoticeDate(date);
				}

				// 注册公告期号
				tm.setRegnoticeNumber(jsonTmInfoList.getRegIssue());

				// 商标名称
				String tmName = jsonTmInfoList.getTmName();
				// logger.info("tmName: " + tmName);
				if (appName != null && appName.equals("北京君策九州科技有限公司")) {
					if (tmName == null) {
						continue;
					} else {
						if (!tmName.equals("慧管佳") && !tmName.equals("IPGO")
								&& !tmName.equals("知产狗")) {
							continue;
						}
					}
				}
				tm.setTmName(tmName);

				// 处理商标状态
				processTmStatus(status, approvalNumber, tm);

				// 将商标插入数据库
				insertTradeMarkTable(tm);

				total++;

			}

			// 对数据更新操作进行记录
			// insertTradeMarkUpdateTable(appName, opt);
			String status = null;
			updateTmUpdateRecord(appName, status, opt);

		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
			return exceptionMsg + message;
		}

		logger.info("申请人： " + appName + "的 " + total + " 条商标数据已经保存到数据库");

		message = "申请人： " + appName + "的 " + total + " 条商标数据已经保存到数据库";

		return message;
	}
	
	
	
	
	//处理申请人信息
	private void processApplicantInfo(TradeMark tm, List<Applicant> applicantList){
		
		//从商标数据中提取申请人
		String applicantName=tm.getApplicantName();
		String applicantEnName=tm.getApplicantEnName();
		String applicantAddress=tm.getApplicantAddress();
		String applicantEnAddress=tm.getApplicantEnAddress();
				
		if (applicantName==null || applicantName.equals("")){
			return;
		}
		
		boolean hasSameOne=false;
		for(Applicant app:applicantList){
			String aName=app.getApplicantName();
			String aEnName=app.getApplicantEnName();
//			String aAddress=app.getApplicantAddress();
//			String aEnAddress=app.getApplicantEnAddress();
			
			if (aName!=null && aName.equalsIgnoreCase(applicantName)){
				
				if ((applicantEnName==null || applicantEnName.equals("")) && (aEnName==null || aEnName.equals(""))){
					hasSameOne=true;
					break;
				}
				if (aEnName!=null && applicantEnName!=null && aEnName.equalsIgnoreCase(applicantEnName)){
					hasSameOne=true;
					break;
				}
//				if (aEnName==null || aEnName.equals("")){
//					app.setApplicantEnName(applicantEnName);
//				}
//				if (aAddress==null || aAddress.equals("")){
//					app.setApplicantAddress(applicantAddress);
//				}
//				if (aEnAddress==null || aEnAddress.equals("")){
//					app.setApplicantEnAddress(applicantEnAddress);
//				}
				
//				hasSameOne=true;
//				break;								
			}	
		}
		if (!hasSameOne){
			Applicant applicant=new Applicant();
			applicant.setApplicantName(applicantName);
			applicant.setApplicantEnName(applicantEnName);
			applicant.setApplicantAddress(applicantAddress);
			applicant.setApplicantEnAddress(applicantEnAddress);		
			applicantList.add(applicant);
		}
	}
	
	
	
	
	
	

	// 更新数据库中的商标数据
	// 第一步：从数据库中获取商标数据
	// 第二步：按照商标号从API接口获取商标的详细数据，并插入到数据库中
	protected String updateTradeMarkDetail(String appName,
			List<TradeMark> tmList) {

		String message = "success";

		String opt = Constants.tmOpt;
		
		List<Applicant> applicantList=new ArrayList<Applicant>();

		int total = 0;
		String tempDir = "";
		// 因为之前已经获取过商标的图片，并且用的目录名是替换了英文括号的申请人
		// 为了避免重复下载图片，这里仍然沿用英文括号
		String newAppName = processAppName(appName);
		String dirName=newAppName;
		//替换目录名不允许的/\\\\:*?<>|等非法的字符
		dirName=StringUtils.replaceChar(dirName);
		
		
		String imagePath = Constants.image_dir + "/" + dirName;
		//创建临时目录
		FileUtil.createFolderIfNotExists(imagePath);
		
		// 由于获取的图片是100*100的，不需要进行图片缩小的操作，所以不需要临时目录
		// String tempDir = imagePath + "/" + "temp";
		// FileUtil.createFolderIfNotExists(tempDir);
		
		String currentRegNumber=null;
		try {

			int exceptionCount = 0;
			boolean finished = true;
			
			List<String> regNumberList=getNumberList(tmList);
			
			List<TradeMarkProcess> tpList=getTmProcessList(regNumberList);
			
			List<TradeMarkCategory> tcList=getTmCategoryList(regNumberList);
			
			List<String> regNumList=new ArrayList<String>();
			
			for (TradeMark tradeMark : tmList) {					
				
				if (updateAll) {
					int remainInfoCount = searchUtil.getRemainInfoCount();
					// 自动更新申请人商标时，保留10000次调用申请人商标详情接口的机会给日常的更新使用
					if (remainInfoCount < 10001) {
						finished = false;
						break;
					}
				}
				
				// 商标注册号
				String regNumber = tradeMark.getRegNumber();
				
				currentRegNumber=regNumber;
				
				// 商标国际分类号
				String tmType2 = tradeMark.getTmType();
				// 商标状态
				String status = tradeMark.getStatus();
				// 商标编号
				Integer tmId = tradeMark.getTmId();
				// 商标图片名，用来从接口下载图片
				String imgFileUrl = tradeMark.getImgFileUrl();
				
				String tmGroup2=tradeMark.getTmGroup();

				// 检查商标注册号和商标国际分类号是否为空
				if (regNumber == null || regNumber.trim().equals("")
						|| tmType2 == null || tmType2.trim().equals("")) {
					continue;
				}
				
				
				StringTokenizer idtok3 = new StringTokenizer(tmType2, ",");					
				while (idtok3.hasMoreTokens()) {
					String tmType= idtok3.nextToken();

					boolean tmTypeError = false;
					if (tmType != null && !tmType.equals("")) {
						if (!StringUtils.isNum(tmType)) {
							logger.info("商标的国际分类号不是数字");
							tmTypeError = true;
						}
					}
					
					// 将获取到商标数据进行处理后，保存到TradeMark对象
					TradeMark tm = new TradeMark();
					// 设置商标的id
					tm.setTmId(tmId);
					// 商标注册号
					tm.setRegNumber(regNumber);
					
					String fileName = regNumber + ".jpg";
					String filePath = imagePath + "/" + fileName;
	
					// 检查本地目录是否存在商标图样
					boolean imageExist = imageExist(filePath);
								
					int beginIndex=Constants.image_dir.length();				
					
					String imgFilePath=filePath.substring(beginIndex);
	
					// 判断是否需要更新商标图样相关数据
					boolean needUpdateImage = ifNeedUpdateImage(regNumber,
							imageExist);
	
					// 判断是否需要从API接口获取该客户的商标数据详情				
					boolean updateTm=ifUpdateTm(appName, tmId, opt,
							needUpdateImage, imageExist, tm, imgFileUrl, regNumber,
							imagePath,  tempDir, imgFilePath);
					if (!updateTm){
						continue;
					}
					
					JsonTmInfo jsonTmInfo = null;
					try {
						jsonTmInfo = getTmDetail(regNumber, tmType, searchUtil);
					} catch (Exception ex) {
						exceptionCount++;
						
						String errMsg = ex.getMessage();
						if (errMsg.indexOf("标库网目前还不存在注册号") > -1) {
							logger.info("get data error, current RegNumber: "+ currentRegNumber + ", error message: "+ errMsg);
							// continue;
						} else if (errMsg.indexOf(sysMsg1) > -1
								|| errMsg.indexOf(sysMsg2) > -1) {
							throw ex;
						} else if (errMsg.indexOf(sysMsg3) > -1) {
//							if (exceptionCount > 1000) {
//								throw ex;
//							} else {
								logger.info("get data error, current RegNumber: "+ currentRegNumber + ", error message: "+ errMsg);
								ex.printStackTrace();
								continue;
//							}
						} else {
							logger.info("get data error, current RegNumber: "+ currentRegNumber + ", error message: "+ errMsg);
							ex.printStackTrace();
							continue;
						}
					}
	
					if (jsonTmInfo == null) {
						//由于商标注册号不正确，导致获取的商标数据为空
	                    //使用修改后的商标注册号获取数据
						exceptionCount = getTmInfoWithOtherRegNumber(jsonTmInfo,
								regNumber, tmType, exceptionCount);
					}
	
					//如果仍然无法获取的商标数据，继续处理下一个商标
					if (jsonTmInfo == null) {
						continue;
					}
				
	
					if (tmTypeError) {
						// 使用从接口获取的商标国际分类号重新设置tmType
						String intCls = jsonTmInfo.getIntCls();						
						tm.setTmType(intCls);
						
						tmType2=tmType2.replaceFirst(tmType, intCls);
						tradeMark.setTmType(tmType2);
					}else{
						tm.setTmType(tmType);
					}
					
					
					
					// 处理商标名称
					boolean contiueFlag = processTmName(tm, jsonTmInfo, appName);
					if (contiueFlag){
						continue;
					}
	
					// 处理获取到的其它商标数据
					processTmInfo(tm, jsonTmInfo, status);
					
					// 设置商标流程
					List<TradeMarkProcess> tmPs = processTmFlow(jsonTmInfo);
					
					// 更新数据库中的商品/服务数据
					updateTmGoods(jsonTmInfo, tm, regNumber, tmType, tcList);
									
					
					// 更新商标流程和状态
					processTmProcess(tm, tpList, tmPs, status);
					
				
					// 处理商标图片数据
					String photo = null;
					
					if (needUpdateImage) {
						if (imgFileUrl == null || imgFileUrl.equals("")) {
							imgFileUrl = jsonTmInfo.getTmImg();
						}
						try {
							photo = getPhoto(tm, imageExist, imagePath, tempDir,
									imgFilePath, imgFileUrl);
//							if (photo == null) {
//								needUpdateImage = false;
//							}
						} catch (Exception e) {
							needUpdateImage = false;
							e.printStackTrace();
						}
					}
					
					
					
					//处理申请人信息
					processApplicantInfo(tm, applicantList);
					
					//更新修改日期用
					if(!regNumList.contains(regNumber)){
						regNumList.add(regNumber);
					}
	
					// 如果本次从接口获取的商标详细数据与数据库中的不同，执行商标数据更新
					if (!compareString(tradeMark, tm)) {
						try {
							//使用合并后的tmType和 tmGroup
							//合并tmGroup;
							if (tmGroup2!=null && !tmGroup2.equals("")){
								String tmGroup=tm.getTmGroup();					
								String tmGroupResult=processTmGroup(tmType, tmGroup, tmGroup2);
								tmGroup2=tmGroupResult;
								tm.setTmGroup(tmGroupResult);								
							}
							tm.setTmType(tmType2);
							
							//该商品/描述信息已经插入到商品/服务表，在商标表中可以不用再保存一份，所以，设置为null
							tm.setTmCategory(null);
							
							updateTradeMarkTable(tm, needUpdateImage);
							total++;
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
	
						// 更新本次同步数据的日期
//						updateTradeMarkModifyData(tmId);
	
						// 只需要更新商标图片
						if (needUpdateImage) {
							if (photo!=null && photo.equals("default")){
//								String base64Image =Constants.zanwutu;
//								String photoDefault = "data:image/png;base64," + base64Image;
								String photoDefault=null;
								updateTradeMarkImage(tmId, photoDefault, null);
							}else{
								updateTradeMarkImage(tmId, photo, imgFilePath);
							}
							
						}
					}
				
				}
				
			}
			
			
			updateTMModifyData(regNumList);
			
			if (applicantList!=null && applicantList.size()>0){
				//将商标中提取出来的申请人插入数据库					
				updateApplicantInfo(appName, applicantList);
			}

			if (finished) {
				// 设置商标详细数据更新的状态为完成
				String status = Constants.updateFinish;
				updateTmUpdateRecord(appName, status, opt);				
			}
			
			//将具有相同商标注册号的记录进行合并
			processDataOfSameRegNumber(regNumberList);

		} catch (Exception e) {			
			e.printStackTrace();
		
			//将商标中提取出来的申请人插入数据库	
			try {
				updateApplicantInfo(appName, applicantList);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return exceptionMsg + message;
			
		}

		logger.info("申请人： " + appName + "的 " + total + " 条商标数据已经更新");

		message = "申请人： " + appName + "的 " + total + " 条商标数据已经更新";
		return message;
	}

	
	
	
	private boolean ifUpdateTm(String appName, Integer tmId, String opt, boolean needUpdateImage, boolean imageExist, TradeMark tm, String imgFileUrl, String regNumber,
			String imagePath, String tempDir, String imgFilePath) throws Exception {

		boolean updateTm = true;
		// 判断是否需要从API接口获取该客户的商标数据详情
		// 只有需要更新的商标，才调用API接口去获取商标的详细数据
		// 以避免不必要的接口调用（浪费调用接口的次数，降低了更新数据的效率）
		String result = ifNeedUpdate(appName, tmId, opt);
		if (result != null) {
			if (needUpdateImage) {				
				String photo=null;
				try {
					photo = getPhoto(tm, imageExist, imagePath, tempDir,
							imgFilePath, imgFileUrl);
					if (photo != null) {
						if (photo.equals("default")){
							imgFilePath=null;
						}						
					}
					// 更新商标图片
					updateTradeMarkImage(tmId, photo,imgFilePath);
					
				} catch (Exception e) {
					needUpdateImage = false;
					e.printStackTrace();
				}
			}
			updateTm = false;			
		}
		return updateTm;
	}

		
	
	
	private boolean ifNeedUpdateImage(String regNumber, boolean imageExist)
			throws Exception {
		boolean needUpdateImage = false;
		// 如果本地不存在图片，或者图片内容为“暂无图”，需要更新商标的图片
		if (!imageExist) {
			needUpdateImage = true;
		} else {
			// needUpdateImage = ifNeedUpdateImage(imageFile);
		}

		if (!needUpdateImage) {
			// 如果数据库中商标的图片为null，需要更新商标的图片
			boolean imageIsNull = tmImageIsNull(regNumber);
			if (imageIsNull) {
				needUpdateImage = true;
			} else {
				// 如果数据库中的imgFilePath属性是否为空
				boolean imgFilePathIsNull = imgFilePathIsNull(regNumber);
				if (imgFilePathIsNull) {
					if (imageExist) {
						needUpdateImage = true;
					}
				}
			}
		}

		return needUpdateImage;
	}
	
	

	
	private int getTmInfoWithOtherRegNumber(JsonTmInfo jsonTmInfo,
			String regNumber, String tmType, int exceptionCount)
			throws Exception {

		for (String zimu : zimuList) {
			// 例如：17602218A
			if (regNumber.endsWith(zimu)) {
				String partRegNumber = regNumber.substring(0,
						regNumber.length() - 1);
				try {
					jsonTmInfo = getTmDetail(partRegNumber, tmType, searchUtil);
				} catch (Exception ex) {
					exceptionCount++;

					String errMsg = ex.getMessage();
					if (errMsg.indexOf("标库网目前还不存在注册号") > -1) {
						logger.info("get data error, current regNumber:" + partRegNumber +", error message: "+errMsg);			
						continue;
					} else if (errMsg.indexOf(sysMsg1) > -1
							|| errMsg.indexOf(sysMsg2) > -1) {
						throw ex;
					} else if (errMsg.indexOf(sysMsg3) > -1) {
//						if (exceptionCount > 1000) {
//							logger.info("exceptionCount >1000, stop update");							
//							throw ex;
//						} else {
							logger.info("get data error, current regNumber:" + partRegNumber +", error message: "+errMsg);						
							ex.printStackTrace();
							continue;
//						}
					} else {
						logger.info("get data error, current regNumber:" + partRegNumber +", error message: "+errMsg);			
						ex.printStackTrace();
						continue;
					}
				}
				break;
			}
		}

		return exceptionCount;
	}
	
	

	
	private boolean processTmName(TradeMark tm, JsonTmInfo jsonTmInfo, String appName)
			throws Exception {
		
		boolean contiueFlag=false;
		
		// 商标名称
		String tmName = jsonTmInfo.getTmName();
		// logger.info("tmName: " + tmName);
		if (appName != null && appName.equals("北京君策九州科技有限公司")) {
			if (tmName == null) {
				contiueFlag=true;
				return contiueFlag;
			} else {
				if (!tmName.equals("慧管佳") && !tmName.equals("IPGO")
						&& !tmName.equals("知产狗")) {
					contiueFlag=true;
					return contiueFlag;
				}
			}
		}
		
		tm.setTmName(tmName);
		
		return contiueFlag;
	}
	
	
	
	
	private void processTmInfo(TradeMark tm, JsonTmInfo jsonTmInfo, String status)
			throws Exception {
		

		//added, 2018-08-22
		//后期指定日期
		String hgzdrq=jsonTmInfo.getHqzdrq();
		if (hgzdrq != null && !hgzdrq.equals("")) {
			Date date = DateTool.StringToDate(hgzdrq);
			tm.setHqzdrq(date);
			
		}
		
		//国际注册日期
		String gjzcrq=jsonTmInfo.getGjzcrq();
		if (gjzcrq != null && !gjzcrq.equals("")) {
			Date date = DateTool.StringToDate(gjzcrq);
			tm.setGjzcrq(date);
			
		}
		
		//优先权日期	
		String yxqrq=jsonTmInfo.getYxqrq();		
		tm.setYxqrq(yxqrq); 	
		
		// 代理人
		tm.setAgent(jsonTmInfo.getAgent());

		// 申请人
		String applicantCn = jsonTmInfo.getApplicantCn();		
		
		processAppName(applicantCn, tm);		

		// 商标文件名
		tm.setImgFileUrl(jsonTmInfo.getTmImg());

		// 初审公告日期
		String approvalDate = jsonTmInfo.getAnnouncementDate();
		if (approvalDate != null && !approvalDate.equals("")) {
			Date date = DateTool.StringToDate(approvalDate);
			tm.setApprovalDate(date);
		}

		// 初审公告期号
		String approvalNumber = jsonTmInfo.getAnnouncementIssue();
		tm.setApprovalNumber(approvalNumber);

		// 申请日期
		String appDate = jsonTmInfo.getAppDate();
		if (appDate != null && !appDate.equals("")) {
			Date date = DateTool.StringToDate(appDate);
			tm.setAppDate(date);
		}

		// 注册公告日期
		String regDate = jsonTmInfo.getRegDate();
		if (regDate != null && !regDate.equals("")) {
			Date date = DateTool.StringToDate(regDate);
			tm.setRegNoticeDate(date);
		}

		// 注册公告期号
		tm.setRegnoticeNumber(jsonTmInfo.getRegIssue());

		// 申请人地址
		tm.setApplicantAddress(jsonTmInfo.getAddressCn());

		// 申请人英文名称
		tm.setApplicantEnName(jsonTmInfo.getApplicantEn());

		// 申请人英文地址
		tm.setApplicantEnAddress(jsonTmInfo.getAddressEn());

		// 共同申请人
		tm.setGtApplicantName(jsonTmInfo.getApplicantOther1());

		// 商标类型，通常该值为 一般
		tm.setClassify(jsonTmInfo.getCategory());

		// 商标专有权期限
		processPrivateDate(jsonTmInfo, tm);

	}
	
	
	
	private void processTmProcess(TradeMark tm, List<TradeMarkProcess> tpList, List<TradeMarkProcess> tmPs, String status)
			throws Exception {		

		String regNumber = tm.getRegNumber();
		Integer tmId=tm.getTmId();

		// 更新数据库中的商标流程数据
		if (tmPs != null && tmPs.size() > 0) {
//			tm.setTmId(tmId);
			if (tmId == 0) {
				logger.info("can not get tmid from trademark table ");
				// System.exit(1);
			} else {
				for (TradeMarkProcess tmProcess : tmPs) {
					tmProcess.setTmId(tmId);
					tmProcess.setRegNumber(regNumber);
				}
				updateTmProcessData(regNumber, tpList, tmPs);
			}
		}

		// 如果有公告数据，可以根据公告数据设置商标状态
		boolean statusSet = getTmStatusFromGongGao(regNumber, tm);

		// 如果无法通过公告设置商标状态，从商标流程中获取商标状态
		if (!statusSet) {
			// 按照流程信息的日期降序排列，将最新的流程信息排在前面
			UseComparator.sort(tmPs, "statusDate", "desc");
			statusSet = getTmStatusFromLiucheng(tmPs, tm);
		}

		// 如果无法从商标流程中获取商标状态，那么使用接口获取的status
		if (!statusSet) {
			if (status != null && !status.equals("")) {
				tm.setStatus(status);
			}
		}
		
		
		//added, 2018-08-22
		String tmStatus=tm.getStatus();
		if (tmStatus.equals("已初审") || tmStatus.equals("待审中")){
			tmStatus="申请中";
		}else if (tmStatus.equals("已销亡") || tmStatus.equals("已驳回")){
			tmStatus="已无效";
		}		
		tm.setTmStatus(tmStatus);

	}
	
	
	

	private String getPhoto(TradeMark tm, boolean imageExist, String imagePath,
			String tempDir, String imgFilePath, String imgFileUrl)
			throws Exception {
		String photo = null;
		String regNumber = tm.getRegNumber();

		// 如果本地存在图片，设置imgFilePath属性。
		if (imageExist) {
			tm.setImgFilePath(imgFilePath);
		} else {
			// 如果本地没有图片，将从API接口获取
			if (imgFileUrl != null && !imgFileUrl.equals("")) {
				getTmImage(imgFileUrl, regNumber, imagePath);
				tm.setImgFilePath(imgFilePath);
			}
		}
		// 获取图片的base64编码，如果将来不需要图片base64编码，可以屏蔽下列几行
		/*
		photo = getBase64Image(regNumber, imagePath, tempDir);
		if (photo != null) {
			if (photo.equals("default")){
				String base64Image =Constants.zanwutu;
				String photoDefault = "data:image/png;base64," + base64Image;	
				tm.setTmImage(photoDefault);
			}else{
				tm.setTmImage(photo);
			}
		}
		*/
		return photo;
	}

	
	
	
	// 只更新数据库中的商标公告
	protected String updateGongGao(String appName) {
		String opt = Constants.gonggaoOpt;
		String message = "success";
		// 设置API Key
		setKey();

		int total = 0;

		boolean finished = true;
		
		String currentRegNumber=null;

		try {

			String newAppName = processAppName(appName);

			List<String> regNumberList = getRegNumber(newAppName);
			for (String regNumber : regNumberList) {
				
				currentRegNumber=regNumber;

				if (regNumber == null || regNumber.equals("")) {
					continue;
				}

				// 测试只获取某一个注册号的公告
				// if (!regNumber.equals("20224577")){
				// continue;
				// }

				if (updateAll) {
					int remaiGgCount = searchUtil.getRemainGgCount();
					// 自动更新，保留10000次调用申请人商标公告接口的机会给日常的更新使用
					if (remaiGgCount < 10001) {
						finished = false;
						break;
					}
				}
				// 根据商标注册号，从API接口获取商标公告
				
				JsonTmGongGaoInfo jsonTmGongGaoInfo = null;
				try {
					jsonTmGongGaoInfo = getGongGao(regNumber,
							searchUtil, appName);
				} catch (Exception ex) {					
					String errMsg = ex.getMessage();
					if (errMsg.indexOf("标库网目前还不存在注册号") > -1) {
						logger.info(errMsg);
						continue;
					} else if (errMsg.indexOf(sysMsg1) > -1
							|| errMsg.indexOf(sysMsg2) > -1) {
						throw ex;
					} else if (errMsg.indexOf(sysMsg3) > -1) {
						logger.info("get data error, current RegNumber: "+ currentRegNumber + ", error message: "+ errMsg);
						ex.printStackTrace();
						continue;
					} else {
						logger.info("get data error, current RegNumber: "+ currentRegNumber + ", error message: "+ errMsg);
						ex.printStackTrace();
						continue;
					}
				}

				if (jsonTmGongGaoInfo != null) {

					// Integer id = jsonTmGongGaoInfo.getCustId();
					// // 如果没有获取到数据，jsonTmGongGaoInfo的custId属性值为null
					// if (id == null) {
					// continue;
					// }

					// 处理商标的公告数据
					int count = processGongGaoDate(regNumber, jsonTmGongGaoInfo);
					total = total + count;
				}

			}

			if (finished && regNumberList.size() > 0) {
				// 对数据更新操作进行记录
				String status = Constants.updateFinish;
				updateTmUpdateRecord(appName, status, opt);
			}

		} catch (Exception e) {			
			e.printStackTrace();
			message = e.getMessage();
			return exceptionMsg + message;

		}

		logger.info("申请人： " + appName + "的 " + total + " 条商标公告数据已经保存到数据库");

		message = "申请人： " + appName + "的 " + total + " 条商标公告数据已经保存到数据库";

		return message;
	}

	// 将某个客户的商标图片进行缩小处理后，更新数据库中的图片
	protected void updateImage(String appName) throws Exception {

		String imagePath = Constants.image_dir + "/" + appName;

		String temDir = imagePath + "/" + "temp";
		FileUtil.createFolderIfNotExists(temDir);

		// Integer cId = new Integer(custId);

		List<Map<String, Integer>> tmList = getTradeMarkInfo(appName);

		for (Map<String, Integer> map : tmList) {

			String regNumber = null;
			Integer tmId = null;

			Iterator<Entry<String, Integer>> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iter
						.next();
				regNumber = (String) entry.getKey();
				tmId = (Integer) entry.getValue();
				break;
			}

			String photo = getBase64Image(regNumber, imagePath, temDir);
			
			String fileName = regNumber + ".jpg";
			String imgFilePath = imagePath + "/" + fileName;
			
			if (photo!=null){
				if (photo.equals("default")){
					imgFilePath=null;
				}
				updateTradeMarkImage(tmId, photo, imgFilePath);
			}
		}
	}

	public static void main(String[] args) {
		try {

			TmDataUpdater tmDataUpdater = new TmDataUpdater();

			String custIds = args[0];
			String appName = args[1];

			Integer updateId = 0;

			Integer custId = new Integer(custIds);

			// 设置API Key
			tmDataUpdater.setKey();

			// List<TradeMark> tmList = tmDataUpdater.getCustomerTmList(custId);

			tmDataUpdater.updateGongGao(appName);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String processAppName(String appName) {

		// 将申请人中的中文括号，替换为英文括号，
		// 否则将无法查找到该申请人的商标
		String newName = appName;

		if (newName.indexOf("（") > -1) {
			newName = newName.replaceAll("（", "(");
		}
		if (newName.indexOf("）") > -1) {
			newName = newName.replaceAll("）", ")");
		}

		return newName;
	}
	
	
	
	protected List<String>  getNumberList(List<TradeMark> tms){
		List<String> regNumberList=new ArrayList<String>();
		for (TradeMark trademark : tms) {			
			String regNumber=trademark.getRegNumber();		
			if (regNumber==null || regNumber.equals("")){
				continue;
			}
			if (!regNumberList.contains(regNumber)){
				regNumberList.add(regNumber);
			}
		}		
		
		return regNumberList;
	}

}
