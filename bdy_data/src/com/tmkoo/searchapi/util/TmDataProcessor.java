package com.tmkoo.searchapi.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;




import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tmkoo.searchapi.common.Constants;
import com.tmkoo.searchapi.common.Global;
import com.tmkoo.searchapi.vo.JsonSbGongGao;
import com.tmkoo.searchapi.vo.JsonSblc;
import com.tmkoo.searchapi.vo.JsonSysp;
import com.tmkoo.searchapi.vo.JsonTmGongGaoInfo;
import com.tmkoo.searchapi.vo.JsonTmInfo;
import com.tmkoo.searchapi.vo.TradeMark;
import com.tmkoo.searchapi.vo.TradeMarkCategory;
import com.tmkoo.searchapi.vo.TradeMarkProcess;


@Component
// Spring Service Bean的标识.
//@Transactional
public class TmDataProcessor extends TmDataGetor {
	@Resource
	private UpdateConfig updateConfig;

	private static Logger logger = Logger.getLogger(TmDataProcessor.class);
	//“暂无图”这张图片进行base64编码后形成的字符串
	//private static String zanwutu="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IokJhDEKDECCKyV0TUriQkWbITknI2j8KAK8Fo97d+TaIzZPGew9Sa19dshp2l2Ft5m8qzktjrn2/GobNNb00OlvayLuOWPlBs/jUmtyXUulWL3qlZyz7gV2nFAG54a/5AcP+838616yPDX/ACA4f95v51r0AFFFFABRRRQAUUUUAFZuv/8AIDuvoP5itKs3X/8AkB3X0H8xQBxdhFZTO/225aBQAVITdk/gKv8A2LQv+gpL/wB+j/hVCwntbd3a6tftCkAKN+3Bq9/aWkf9Af8A8jGgBfsWhf8AQUl/79H/AApktpoqxOU1ORnCkqpiPJ/Knf2lpH/QH/8AIxpH1DSWjYJpOHKkKTKeKAE8Nf8AIch/3W/ka7quG8MIza1GQpIVWJI6AY7/AJ1sa3rt1pt8IIUhZCgbLqc/zoAreMJ/mtrcHoC5H6D+RrQ8LweVo4kI5lcv/T+lcpeXdzq16rugMjAIqRg/pmu/s4BbWUEHeNAv40Acd4o/5DTf9c1rq9I/5BFp/wBclrlfFKMur7ipAaNSD2NJbeJr21tooEjgKxqFBKnOPfmgDt64PWv+Rhn/AN9f5Cug0LWbnVJpUmSJQiggoDnP4mud107deuG64ZTj8BQBP4nWNdWJVGRyoLEjhvcEf54qMR3BUEa3CBjgec4x+lSX2uwaiVNzpysVyFIlIOPqBVT7Xp//AEDP/JhqALHlXH/Qcg/7/P8A4VJqqsujWAe4Wc75MyKxbP5iqf2vT/8AoGf+TDU28v1uLaC3itxDFEWIG/dkn6/SgDrfDX/IDh/3m/nWvWV4cjZNEgDqQSWIB4OCa1aACiiigAooooAKKKKACkwGGCMg9jS0UAM8qP8AuL+VHlR/3F/Kn0UAM8qP+4v5UeVH/cX8qfRQAgVV6KBn0FLRRQAUUUUAIVVhgqCPcU3yo/7i/lT6KAECKvRQM+gpjQQuxLRISepKjJ/SpKKAIvs0H/PCP/vgUfZoP+eEf/fAqWigCL7NB/zwj/74FKLaAHIhjBHcIKkooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAPSiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAP/9k=";
	
//	private static int wutuHashValue=StringUtils.getStringHash(zanwutu); 
	
	//对上述字符串取hash的结果是2031750866	
	private static int wutuHashValue=2031750866;
	
	//处理返回消息
	protected void processMsg(StringBuilder message, StringBuilder errMessage, String msg){
			
		String errkey = "exception-";
		if (msg != null) {
			int pos = msg.indexOf(errkey);
			if (pos > -1) {
				if (errMessage.length()==0) {
					errMessage = errMessage.append(msg);
				} else {
					int len = errkey.length();
					msg = msg.substring(len + 1);
					errMessage =  errMessage.append("<p></p>" + msg);
				}
			} else {
				if (message.length()==0) {
					message = message.append("success-"+msg);
				}else{
					message =  message.append("<p></p>" + msg);
				}				
			}
		}
		
	}
	
	
	
	//合并tmGroup;
	protected String  processTmGroup(String tmType, String tmGroup, String tmGroup2){
		
		if (tmGroup==null || tmGroup.equals("")){			
			return tmGroup2;
		}
		
		int tmTypeLen=tmType.length();
		if (tmTypeLen==1){
			tmType="0"+tmType;
			tmTypeLen=2;
		}
		
		List<String> tmGroupList2 = Arrays.asList(tmGroup2.split(";"));
		List<String> tmGroupList = Arrays.asList(tmGroup.split(";"));
		
		List<String> arrayList2 = new ArrayList<String>(tmGroupList2);
		
		Iterator<String> it = arrayList2.iterator();
		while (it.hasNext()) {
			String group=it.next();	
			if (group==null || group.equals("")){
				it.remove();
				continue;
			}
			int len=group.length();
			if (len>tmTypeLen){
				String s=group.substring(0,tmTypeLen);
				if (s.equals(tmType)){
					it.remove();
				}
			}			
		}
		
		
		for (String group: tmGroupList){
			int len=group.length();
			if (len<4){
				continue;
			}
			if (!arrayList2.contains(group)){
				arrayList2.add(group);
			}
		}
		
		try{
			//当数据如：0301;0301,0306;0305;0306;0501;1605;1606;
			//其中有这种数据：0301,0306，将导致StringUtils.listSort执行排序时出现异常：
			//java.lang.NumberFormatException: For input string: "0301,0306"
			//因此，对此进行处理，直接忽略这个异常
			//排序
			StringUtils.listSort(arrayList2);
		}catch(Exception e){
			logger.info(e.getMessage()+ ", tmGroup2: "+tmGroup2 + ", tmGroup:"+tmGroup);
		}
		
		String[] b = arrayList2.toArray(new String[arrayList2.size()]);
		String tmGroupResult= String.join(";", b);
		if(tmGroupResult!=null && !tmGroupResult.endsWith(";")){
			tmGroupResult=tmGroupResult+";";
		}
		
		return tmGroupResult;
		
	}


	// 处理商标的专有权期限
	protected void processPrivateDate(JsonTmInfo jsonTmInfo, TradeMark tm) {

		String publicDate = jsonTmInfo.getPrivateDate();
		if (publicDate != null) {
			int pos = publicDate.indexOf("至");
			if (pos > -1) {
				String startDate = publicDate.substring(0, pos);
				if (startDate != null && !startDate.equals("")) {
					Date date = DateTool.StringToDate(startDate);
					tm.setValidStartDate(date);
				}
				int len = publicDate.length();
				if (len > pos + 1) {
					String endDate = publicDate.substring(pos + 1, len);
					if (endDate != null && !endDate.equals("")) {
						Date date = DateTool.StringToDate(endDate);
						tm.setValidEndDate(date);
					}
				}
			}
		}else{
			String regNumber=tm.getRegNumber();
			if (regNumber.startsWith("G")){
				Date regDate=tm.getRegNoticeDate();				
				if(regDate!=null){
					
					//初始：将专用权有效期起始日设置为商标注册日
					Date validStartDate=regDate;			
					try{					
						int count=0;																
						Date now=new Date();	
						//计算当前时间可能有n个10年（ n>=0）
						if(now.getTime()>validStartDate.getTime()){
							int result=DateTool.yearsBetween(validStartDate, now);						
							count=result/10;	
						}
						
						//因此，将专用权有效期起始日设置为商标注册日加上n个10年
						validStartDate=DateTool.getDateAfterYear(validStartDate, 10*count);
						
						//将专用权有效期截至日设置为起始日加上10年
						Date date=DateTool.getDateAfterYear(validStartDate, 10);			
											
						String dateString=DateTool.getDate(date);
						Date validEndDate=DateTool.StringToDate(dateString);
						
						tm.setValidEndDate(validEndDate);	
						tm.setValidStartDate(validStartDate);
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	

	// 处理商标流程
	protected List<TradeMarkProcess> processTmFlow(JsonTmInfo jsonTmInfo) {

		// 设置商标流程
		List<JsonSblc> flows = jsonTmInfo.getFlow();
		List<TradeMarkProcess> tmPs = new ArrayList<TradeMarkProcess>();
		for (JsonSblc flow : flows) {
			TradeMarkProcess tradeMarkProcess = new TradeMarkProcess();
			String flowDate = flow.getFlowDate();
			if (flowDate != null && !flowDate.equals("")) {
				Date date = DateTool.StringToDate(flowDate);
				tradeMarkProcess.setStatusDate(date);
			}
			tradeMarkProcess.setStatus(flow.getFlowName());
			tmPs.add(tradeMarkProcess);
		}

		return tmPs;
	}
	
	// 查看商标图片是否存在
	protected boolean imageExist(String imageFile) throws Exception{

		boolean exist = false;	
				
		File localFile = new File(imageFile);
		if (localFile.exists()) {
			exist = true; 
		}	
		return exist;
	}
	

	// 处理商标图片
	protected boolean ifNeedUpdateImage(String imageFile) throws Exception{
		boolean needUpdate = false;
							
		//一般情况下，该目录下已有该注册号对应的图片文件，就不需要再更新该商标的图片，
		//但如果之前商标图片是“暂无图”这张图片，那么需要更新该商标的图片。
		//判断该商标的图片是否是“暂无图”这张图片的方法是：
		//将当前图片进行base64编码形成字符串，然后获取该字符串的hash值，
		//与 事先使用“暂无图”进行base64编码形成字符串再取得的hash值进行比较，
        //如果二者相等，则认为当前商标的图片是“暂无图”这张图片			
		String base64Image = ImageUtils.encodeImgageToBase64(imageFile);
		int hashValue = StringUtils.getStringHash(base64Image); 
		
		if(hashValue==wutuHashValue){
			needUpdate = true;
		}
	
        return needUpdate;
		
	}
	
	
	// 获取图片的64位编码后的字符串
	protected String processTmImage(String imgFileUrl,
			String regNumber, String imagePath, String tempDir) throws Exception{
		
		if(imgFileUrl!=null && !imgFileUrl.equals("")){
			getTmImage(imgFileUrl,regNumber,imagePath);
		}
		
		String photo = getBase64Image(regNumber, imagePath, tempDir);
		
		return photo;
	}
		
	
	// 从API接口获取图片，并保存到本地
	protected void getTmImage(String imgFileUrl,
			String regNumber, String imagePath) throws Exception{	
		
		// 处理商标图片
		String fileName = regNumber + ".jpg";
		String imageFile = imagePath + File.separator + fileName;
		
		File localFile = new File(imageFile);
		if (!localFile.exists()) {		
			/*
		      tmImg:"121b99cebccde101", //商标图片名
		              需要小图(50*50)，http://tmpic.tmkoo.com/121b99cebccde101-s
	                        需要中图(100*100)，http://tmpic.tmkoo.com/121b99cebccde101-m
	                        需要大图，http://tmpic.tmkoo.com/121b99cebccde101
	          */
			String url = "http://tmpic.tmkoo.com/" + imgFileUrl+"-m";
			ImageUtils.getImageFromUrl(url, imageFile);
		}
	}
	
	
	
	// 处理商标图片
	protected String getBase64Image(
			String regNumber, String imagePath, String tempDir) throws Exception{

		String photo = null;
		// 处理商标图片
		String fileName = regNumber + ".jpg";
//		String fileName2 = regNumber + "_1.jpg";
		String sourceFile = imagePath + File.separator + fileName;
//		String tempFile = imagePath + File.separator + fileName2;
	
		File file2 = new File(sourceFile);
		if (!file2.exists()) {
			logger.info("注册号为：" + regNumber + "的商标图样不存在，用暂无图替代！+ file path: "
					+ imagePath);	
			photo="default";
//			String base64Image =Constants.zanwutu;
//			photo = "data:image/png;base64," + base64Image;	
			
		} else {
			
			String usedFile = sourceFile;
			
			//由于现在获取的图片已经是100*100的最小尺寸图片，因此无需再进行压缩
			/*
			String destFile = tempDir + File.separator + fileName;			
			File file3 = new File(destFile);
			if (!file3.exists()) {
				// 对图片进行另存为处理，减小文件尺寸
				boolean success = ImageUtils.compressImage(sourceFile, tempFile, destFile);				
				if (success) {
					usedFile = destFile;
				}
			}else{
				usedFile = destFile;
			}
			*/
			
			
			String base64Image = ImageUtils.encodeImgageToBase64(usedFile);
			photo = "data:image/png;base64," + base64Image;
			
		}
		
		return photo;
	}
	
	
	

	// 获取商品/服务数据
	protected List<TradeMarkCategory> getTradeMarkCategoryList(JsonTmInfo jsonTmInfo,
			TradeMark tm, String regNumber, String tradeMarkType)
			throws Exception {
		
		List<TradeMarkCategory> tradeMarkCategoryList = new ArrayList<TradeMarkCategory>();
		
		int no = 0;
		
		Integer tmId=tm.getTmId();
		Integer tmType = new Integer(tradeMarkType);
		// 商品和服务
		List<JsonSysp> goods = jsonTmInfo.getGoods();		
		
		for (JsonSysp service : goods) {
			no++;		
			TradeMarkCategory tradeMarkCategory = new TradeMarkCategory();
			
			tradeMarkCategory.setTmId(tmId);
			tradeMarkCategory.setRegNumber(regNumber);			
			tradeMarkCategory.setTmType(tmType);		

			tradeMarkCategory.setNo(no);
			String group = service.getGoodsCode();
			if (group != null && !group.equals("")) {
				tradeMarkCategory.setTmGroup(group);
			}

			String name = service.getGoodsName();
			if (name != null && !name.equals("")) {
				tradeMarkCategory.setName(name);
			}
			
			
			// 保存类似群与商品/服务的对应关系
			tradeMarkCategoryList.add(tradeMarkCategory);
		}
		
		// 按照tmgroup进行排序
		sortByTmGroup(tradeMarkCategoryList, tm);
		
		return tradeMarkCategoryList;
	}
	

	
	
	// 更新商品/服务数据
	protected void updateTmGoods(JsonTmInfo jsonTmInfo,
			TradeMark tm, String regNumber, String tradeMarkType, List<TradeMarkCategory> tcList)
			throws Exception {
		
		List<TradeMarkCategory> tradeMarkCategoryList = getTradeMarkCategoryList(jsonTmInfo, tm, regNumber,tradeMarkType);
		
		Integer tmType = new Integer(tradeMarkType);	
		// 查找数据库中是否已经有该注册号的商品和服务数据
//		List<TradeMarkCategory> list= getTradeMarkCategory(regNumber,tmType);
		
		List<TradeMarkCategory> list = new ArrayList<TradeMarkCategory>();
		
		if(tcList!=null){
			for (TradeMarkCategory tradeMarkCategory : tcList) {
				String regNum = tradeMarkCategory.getRegNumber();
				Integer tType = tradeMarkCategory.getTmType();
				if (regNum!=null && regNum.equals(regNumber)){
					if (tType!=null && tType.intValue()==tmType.intValue()){
						list.add(tradeMarkCategory);
					}
				}
			}
		}
		
		if (list==null || list.size()==0){
			insertTradeMarkCategoryTable(tradeMarkCategoryList);
			return;
		}
        
		boolean deleteFlag=false;
		// 判断数据库中的数据与本次获取的数据是否完全相同，
		// 如果有不同的，将删除表中的数据，
		// 而使用本地获取的数据（排序后的数据），以保证数据中的no属性是按照tmgroup进行顺序的结果
		for(TradeMarkCategory tradeMarkCategory: tradeMarkCategoryList){
			String group1=tradeMarkCategory.getTmGroup();
			String name1=tradeMarkCategory.getName();
			boolean hasSameOne=false;
			for(TradeMarkCategory tmc: list){
				String group2=tmc.getTmGroup();
				String name2=tmc.getName();
				if (group1 != null && group2 != null && group1.equals(group2)) {					
					if (name1 != null && name2 != null && name1.equals(name2)) {
						hasSameOne=true;
						break;
					}					
				}				
			}		
			
			if (!hasSameOne){
				deleteFlag=true;
				break;
			}
			
		}
		
		if (deleteFlag){
			
			//删除表中的数据，插入排序后的数据
			deleteTradeMarkCategory(regNumber,tmType);
			insertTradeMarkCategoryTable(tradeMarkCategoryList);
		}
	
	}
	
	
	private void sortByTmGroup(List<TradeMarkCategory> tradeMarkCategoryList, TradeMark tm){
		String tmGroup = "";
		String tmCategory = "";
		List<String> tmGroupList = new ArrayList<String>();	
		
	    // 获取tmgroup
		for(TradeMarkCategory tmc:tradeMarkCategoryList){			
			
			String name=tmc.getName();
			if (name==null){
				continue;
			}
			if (tmCategory.equals("")) {
				tmCategory = tmCategory + name;
			} else {
				if (tmCategory.indexOf(name) < 0) {
					tmCategory = tmCategory + ";" + name;
				}
			}
			
			String group=tmc.getTmGroup();
			if (group==null || group.equals("")){
				continue;
			}
			if (!tmGroupList.contains(group)){			
				if (tmGroup.equals("")) {
					tmGroup = tmGroup + group;
				} else {
					if (tmGroup.indexOf(group) < 0) {
						tmGroup = tmGroup + ";" + group;
					}
				}			
				tmGroupList.add(group);
			}			
		}
		
//		List<TradeMarkCategory> tmcList = new ArrayList<TradeMarkCategory>();
		
		//从官网以及路标等网站可以看到，商品服务信息列表，都是按照tmgroup进行排序，其中
		//序号是指同一个tmgroup下的各个服务名称的排序结果。
		//所以，需要按照tmgroup对上述查询结果进行排序，重新设置序号的值。
		for(String group1: tmGroupList){
			int number=1;
			for(TradeMarkCategory tmc:tradeMarkCategoryList){			
				String group2=tmc.getTmGroup();			
				if (group2==null){
					tmc.setNo(number);					
					number++;	
					continue;
				}
				if (!group1.equals(group2)){
					continue;
				}
				tmc.setNo(number);
//				tmcList.add(tmc);
				number++;							
			}
		}
		
		// 类似群
		if (tmGroup!=null && !tmGroup.equals("")){
			if (!tmGroup.endsWith(";")){
				tmGroup=tmGroup+";";
			}
		}
		tm.setTmGroup(tmGroup);

		// 商品和服务描述信息
		tm.setTmCategory(tmCategory);

		
//		return tmcList;
	
	}

	
	
	// 获取商标状态
	protected String getTmStatus(String status, String approvalNumber) {

		// 处理商标状态，将获取的商标状态归类为5种状态
		// logger.info("获取的商标状态：" + status);
		if (status != null && !status.equals("")) {
			boolean find = false;
			Iterator<Entry<String, String>> iter = Constants.tmstatus
					.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
						.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				if (value.indexOf(status) > -1) {
					return key;					
				}
			}
			if (!find) {
				if (approvalNumber != null && !approvalNumber.equals("")) {
					String value = "已初审";
					return value;
				} else {
					String value = "待审中";
					return value;
				}
			}

		} else {
			String value = "待审中";
			return value;
		}
		
		return null;
	}
	
	
	// 处理商标申请人中文名称
	protected void processAppName(String applicantCn, TradeMark tm) throws Exception {

		if (applicantCn!=null && !applicantCn.equals("")){				
			int pos=applicantCn.indexOf(";");					
			if(pos>-1){
				String applicantName=applicantCn.substring(0,pos);	
				tm.setApplicantName(applicantName);	
			}
			else{
				/*
				// 对于申请人名称不准确的，进行矫正
				String applicantName=getCorrectAppName(applicantCn);				
				
				if (applicantName!=null){
					tm.setApplicantName(applicantName);	
				}else{
					tm.setApplicantName(applicantCn);	
				}
				*/
				
				tm.setApplicantName(applicantCn);	
			}
		}
	}
	
	

	public List<String> processAppName(List<String> appNames){
		
		List<String> newAppNames = new ArrayList<String>();
		int size=appNames.size();
		for (int i=0;i<size;i++) {
			
			String appName=appNames.get(i);
			
			// 将申请人中的中文括号，替换为英文括号，
			// 否则将无法查找到该申请人的商标
			
			if (appName.indexOf("（")>-1){
				appName=appName.replaceAll("（", "(");
			}
			if (appName.indexOf("）")>-1){
				appName=appName.replaceAll("）", ")");
			}
			
			newAppNames.add(appName);
			
		}
		
		return newAppNames;
	}
	
	
	
	
	// 处理商标状态
	protected void processTmStatus(String status, String approvalNumber,
			TradeMark tm) {

		String result=getTmStatus(status, approvalNumber);
		
		tm.setStatus(result);
	}

	
	
	
	
	// 从公告中获取商标状态
	protected boolean getTmStatusFromGongGao(String regNumber, TradeMark tm) throws Exception {
        boolean result = false;	
        
    	// 从数据库中获取该商标注册号的公告
		List<JsonSbGongGao> ggList = getTradeMarkGongGao(regNumber);
		

		if(ggList==null || ggList.size()==0){
			return result;
		}
		Iterator<JsonSbGongGao> it = ggList.iterator();
		while (it.hasNext()) {
			JsonSbGongGao gg = it.next();
			String ggName = gg.getGgName();

			// 处理商标状态，将获取的商标状态归类为5种状态
			if (ggName== null || ggName.equals("")) {
				continue;
			}

			Iterator<Entry<String, String>> iter = Constants.gonggaotatus
					.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
						.next();
				String key = (String) entry.getKey();
				String gonggaoName = (String) entry.getValue();
				
				StringTokenizer tok = new StringTokenizer(gonggaoName, ",");
				while (tok.hasMoreTokens()) {
					String value = tok.nextToken();
					String name = value.trim();
					if (ggName.indexOf(name) > -1) {
						tm.setStatus(key);
						result=true;							
						break;
					}
				}
				if (result){
					break;
				}					
				
			}
			
		}
		
		return result;

	}
	
	

	
	// 处理商标的公告数据
	protected int processGongGaoDate(String regNumber,
			JsonTmGongGaoInfo jsonTmGongGaoInfo) throws Exception {

		int count=0;
		// 查看数据库中是否已经有该商标注册号的公告
		List<JsonSbGongGao> gonggaos = getTradeMarkGongGao(regNumber);
		
		List<JsonSbGongGao> ggList = jsonTmGongGaoInfo.getGonggaos();
		
		if (gonggaos == null || gonggaos.size() == 0) {
			// 如果数据库中不存在该商标注册号的公告，那么将本次获取的公告插入数据库
			insertTradeMarkGongGaoTable(regNumber,ggList);
			
			count=ggList.size();

		} else {


			// 处理获取的数据， 只保留数据库中不存在的数据
			Iterator<JsonSbGongGao> it = ggList.iterator();
			while (it.hasNext()) {
				JsonSbGongGao gg = it.next();
				String ggName = gg.getGgName();
				Date ggDate = gg.getGgDate();

				boolean hasSameOne = false;
				for (JsonSbGongGao gonggao : gonggaos) {
					String gonggaoName = gonggao.getGgName();
					Date gonggaoDate = gonggao.getGgDate();
					if (ggName != null && gonggaoName != null
							&& ggName.equals(gonggaoName)) {
						if (ggDate != null && gonggaoDate != null
								&& ggDate.equals(gonggaoDate)) {
							hasSameOne = true;
							break;
						}
					}
				}
				if (hasSameOne) {
					it.remove();
				}
			}

			count=ggList.size();
			if (count > 0) {
				
				// 将数据库中不能存在的公告数据插入数据库
				insertTradeMarkGongGaoTable(regNumber,ggList);
			}

		}
		
		return count;

		
	}
	
	
	
	// 更新商标的流程数据
	protected void updateTmProcessData(String regNumber, List<TradeMarkProcess> tpList, List<TradeMarkProcess> tmPs) throws Exception {

		// 从数据库获取该商标的流程数据
		//List<TradeMarkProcess> tradeMarkProcessList = getTradeMarkProcessList(tmId);
		
		List<TradeMarkProcess> tradeMarkProcessList =new ArrayList<TradeMarkProcess>();
		if(tpList!=null){
			for (TradeMarkProcess tradeMarkProcess : tpList) {
				String regNum = tradeMarkProcess.getRegNumber();
				if (regNum!=null && regNum.equals(regNumber)){
					tradeMarkProcessList.add(tradeMarkProcess);
				}
			}
		}

		if (tradeMarkProcessList == null || tradeMarkProcessList.size() == 0) {
			// 如果数据库中没有该商标的流程数据，那么插入
			insertTradeMarkProcessTable(tmPs);

		} else {
			
			if (tmPs==null ){
				return;				
			}
			int size1=tradeMarkProcessList.size();
			int size2=tmPs.size();
			
			boolean deleteFlag= false;
			
			if (size1!=size2){
			//	deleteFlag=true;
			}else{
						
				// 判断数据库中的数据与本次获取的数据是否完全相同，
				// 如果有不同的，将删除表中的数据，
				// 而使用本次获取的数据，以减少误差。
	            // 场景：数据库中有一条status为“商标注册申请等待驳回通知发文”的数据
				// 本次获取的数据中有一条status为“商标注册申请驳回通知发文”，二者时间相同，
				// 唯独status有细小差别，实际含义是相同的。如果不删除原有数据，而插入当前获取的数据
				// 将使数据数据库中存在相近的流程数据
				Iterator<TradeMarkProcess> it = tmPs.iterator();
				while (it.hasNext()) {
					TradeMarkProcess tmp = it.next();
					String status = tmp.getStatus();
					Date statusdate = tmp.getStatusDate();
	
					boolean hasSameOne = false;
					for (TradeMarkProcess tradeMarkProcess : tradeMarkProcessList) {
						String pStatus = tradeMarkProcess.getStatus();
						Date pStatusdate = tradeMarkProcess.getStatusDate();
						if (status != null && pStatus != null
								&& status.equals(pStatus)) {
							if (statusdate == null && pStatusdate == null){
								hasSameOne = true;
								break;
							}
							if (statusdate != null && pStatusdate != null
									&& statusdate.equals(pStatusdate)) {
								hasSameOne = true;
								break;
							}
							
						}
					}
					if (!hasSameOne) {
						deleteFlag=true;
						break;
					}
				}	
			}
			
			if (deleteFlag) {
				// 删除原有数据
				deleteTradeMarkProcess(regNumber);
				
				// 插入当前获取的数据
				insertTradeMarkProcessTable(tmPs);
			}			
			

		
		}

	}
	
	

	
	// 判断是否需要更新数据库中的商标相关数据
	protected String ifNeedUpdate(String appName, Integer tmId, String opt) throws Exception {
		
		int updateInterval=0;
		String interval=null;
		String keyWord="";
		if (updateConfig==null){
			updateConfig= new UpdateConfig();
		}
		if(opt.equals(Constants.tmOpt)){
			interval=updateConfig.getTm_interval();
			keyWord="商标";
		}else if(opt.equals(Constants.gonggaoOpt)){
			interval=updateConfig.getGonggao_interval();
			keyWord="商标公告";
		}		
		if(interval!=null && !interval.equals("")){
			if (StringUtils.isNum(interval)){			
				updateInterval=Integer.parseInt(interval);				
			}
		}
		
		if (updateInterval<1){
			logger.info("商标数据的更新时间间隔小于1天是不正确的，需要修改application.properties中的配置。");
			return null;
		}
		
		String message =null;
		Date optDate = null;
		if(tmId!=null){			
			//判断指定的商标是否需要更新
			optDate = getTmDetailUpdateDate(tmId);			
		}else{			
			//判断某个客户的商标数据是否需要更新
			Map<Integer, Date> updateRecord=getTradeMarkUpdateRecord(appName, opt);
			Iterator<Entry<Integer, Date>> iter = updateRecord
					.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<Integer, Date> entry = (Map.Entry<Integer, Date>) iter
						.next();
				 optDate  = entry.getValue();
				 break;
			}
		}
		
		if (optDate != null) {
			Date needUpdateDay = DateTool.getDateAfter(optDate,
					updateInterval);
			Date now = new Date();
			if (needUpdateDay.after(now)) {
				String destDate=DateTool.getDate(needUpdateDay);
				if (appName!=null){	
					message= appName;					
				}else{
					message= "序号为"+tmId;
				}
				message=message+"的" + keyWord + "数据最近一次更新时间是" + optDate + "，"
						+ destDate + "之后再更新";
//				logger.info(message);				
			}
		}
		
		return message;		
	}
	
		

	// 处理命令行参数
	protected List<String> processParameter(String[] args) {

		int len = args.length;
		List<String> appNames = new ArrayList<String>();
		for (int i = 1; i < len; i++) {
			String s = args[i];
			if (s.indexOf("#") > -1) {
				s = s.replaceAll("#", " ");
			}
			appNames.add(s);

		}
		return appNames;
	}

	

	protected boolean compareString(TradeMark tradeMark, TradeMark tm) {
		
		// 计算hash值
		int hashValue = getStringHash(tradeMark);	
		
		int hashValue2 = getStringHash(tm);

		if (hashValue != hashValue2) {
			return false;
		}

		return true;

	}
	
	
	
	// 获取数据库中该商标的属性相加构成一个字符串，获取该字符串的hash值
	protected int getStringHash(TradeMark tradeMark) {

		String data ="";		
		
		Date appdate = tradeMark.getAppDate();
		if(appdate!=null){
			String date=DateTool.getDate(appdate);
			data=StringUtils.addString(data, date);
		}
		String approveNumber = tradeMark.getApprovalNumber();
		data=StringUtils.addString(data, approveNumber);
		
		String regnoticeNumber = tradeMark.getRegnoticeNumber();
		data=StringUtils.addString(data, regnoticeNumber);
		
		Date validStart = tradeMark.getValidEndDate();
		if(validStart!=null){
			String date=DateTool.getDate(validStart);
			data=StringUtils.addString(data, date);
		}
		
		Date validEnd = tradeMark.getValidEndDate();
		if(validEnd!=null){
			String date=DateTool.getDate(validEnd);
			data=StringUtils.addString(data, date);
		}
		
		Date gjRegDate = tradeMark.getGjzcrq();
		if (gjRegDate != null) {
			String date = DateTool.getDate(gjRegDate);
			data = StringUtils.addString(data, date);
		}

		Date hqzdDate = tradeMark.getHqzdrq();
		if (hqzdDate != null) {
			String date = DateTool.getDate(hqzdDate);
			data = StringUtils.addString(data, date);
		}

		String priorDate = tradeMark.getYxqrq();
	

		
		String agent = tradeMark.getAgent();
		data=StringUtils.addString(data, agent);
		
		String applicantName = tradeMark.getApplicantName();
		data=StringUtils.addString(data, applicantName);
		
//		String tmcategory=tradeMark.getTmCategory();
//		data=StringUtils.addString(data, tmcategory);
		
		String status=tradeMark.getStatus();
		data=StringUtils.addString(data, status);
							
		// 计算字符串的hash值
		int hashValue = StringUtils.getStringHash(data); 
		
		return hashValue;

	}
	
	
    protected void setKey(){
    	if (Global.webProperties.API_KEY==null || !Global.webProperties.API_KEY.equals("QIJIAN_380109332")){
			Global.webProperties.API_KEY = "QIJIAN_380109332";
			Global.webProperties.API_PASSWORD = "SMQicSvJNB";
		}
//    	if (Global.webProperties.API_KEY==null || !Global.webProperties.API_KEY.equals("A_xMyN1pk2")){
//			Global.webProperties.API_KEY = "A_xMyN1pk2";
//			Global.webProperties.API_PASSWORD = "CyQBfeyWUs";
//		}
    }
    
    
	public static void processApplicant(List<String>  appNames) throws Exception{
		String condition = "";
		
		// 获取applicant表中的已经存在的申请人信息
		Map<Integer, String> oldApplicants = getApplicantInfo(null,
				condition);
		
		Iterator<Entry<Integer, String>> iter = oldApplicants.entrySet()
				.iterator();
		while (iter.hasNext()) {
			boolean hasSameOne = false;
			Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) iter
					.next();
			int index = 0;
			
			String appName=entry.getValue();		
			
			for (String appName2: appNames){
				
				if (appName!=null && appName2!=null && appName.equals(appName2)){
					hasSameOne = true;
					break;
				}
				
				index++;
			}			
			// 针对数据库中已经存在的申请人，不需要再插入到数据库。
			if (hasSameOne) {
				appNames.remove(index);					
			}
		}
	}

	
	
	// 从商标流程中获取商标状态
	protected boolean getTmStatusFromLiucheng(List<TradeMarkProcess> tmPs, TradeMark tm) throws Exception {
       
		boolean result=false;

		if(tmPs==null || tmPs.size()==0){
			return result;
		}
		
		boolean find = false;
		for(TradeMarkProcess tp:tmPs){
			String status=tp.getStatus();			
			Iterator<Entry<String, String>> iter = Constants.liuchengstatus
					.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
						.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				if (value.indexOf(status) > -1) {
					find=true;
					tm.setStatus(key);
					return true;
				}
			}
			
		}
		
		return result;
	

	}
	
}
