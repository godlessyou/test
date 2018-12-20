package com.tmkoo.searchapi.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tmkoo.searchapi.common.Constants;
import com.tmkoo.searchapi.json.help.JSONArray;
import com.tmkoo.searchapi.json.help.JSONObject;
import com.tmkoo.searchapi.vo.AppDataStatus;
import com.tmkoo.searchapi.vo.TmDataCount;
import com.tmkoo.searchapi.vo.TmDataStatus;
import com.tmkoo.searchapi.vo.TradeMark;



@Component
// Spring Service Bean的标识.
//@Transactional

public class TmDataService extends TmDataUpdater {
	
	@Resource
	private ServiceUrlConfig serviceUrlConfig;
	
	@Resource
	private UpdateConfig updateConfig;

	//由于申请人太多，导致更新申请人商标数据的任务一直执行，
	//从凌晨1点开始，一直在执行更新，可能影响访问速度
	//所以，预先设定一个更新操作执行的最长时间，到时间之后，停止更新操作
	//默认值是10个小时
	private final long maxUpdateTime=3600 * 1000 * 10;
	
    //重复尝试的次数
	private int limitCount=1;
	
	private final ExecutorProcessPool pool = ExecutorProcessPool.getInstance();
	
	private static Logger logger = Logger.getLogger(TmDataService.class);
	
	

	public List<TradeMark> querySameTm(String tmName,String tmType) throws Exception {
		
			
		//设置API Key
		setKey();
				
		List<TradeMark> list=getTradeMarkData(tmName,tmType);
		
		return list;
	
	}
	
	
	
	public List<TmDataCount> getTmCount(String appName) throws Exception {
		List<String> appNames=new ArrayList<String>();
		if (appName != null && !appName.equals("")) {
			StringTokenizer tok = new StringTokenizer(appName, ",");
			while (tok.hasMoreTokens()) {
				String key = tok.nextToken();
				appNames.add(key);
			}			
		}	
			
		
		// 处理申请人的名字，将其中的中文括号改为英文括号
		appNames=processAppName(appNames);
		
		List<TmDataCount> list=new ArrayList<TmDataCount>();
		for (String name: appNames){		
			int tmCount=getTmCountByAppName(name);			
			TmDataCount tdc=new TmDataCount();
			tdc.setAppName(name);
			tdc.setTmCount(tmCount);			
			list.add(tdc);			
		}
		
		return list;
	
	}
	
	
	
	public  List<TmDataCount>  getCaseCount(String url) throws Exception {
			
		List<TmDataCount> list = new ArrayList<TmDataCount>();
		String jsonString = GraspUtil.getText(url);
		JSONObject json= new JSONObject(jsonString); 
		JSONArray jsonArray=json.getJSONArray("data");  
	  
	    for(int i=0;i<jsonArray.length();i++){  
	        JSONObject jObj=(JSONObject) jsonArray.get(i); 
	        TmDataCount tmDataCount=new TmDataCount();
	        String appName=jObj.get("appName").toString();	
	        tmDataCount.setAppName(appName);		        
	        String caseCountStr=jObj.get("caseCount").toString();
	        if (caseCountStr!=null && !caseCountStr.equals("")){
	        	Integer caseCount=new Integer(caseCountStr);
	        	int count=caseCount.intValue();
//	        	tmDataCount.setCaseCount(count);
	        }		        
	        list.add(tmDataCount);		        
	    }
	    
	    return list;
	    
	}
			
		
	
	
	// 更新商标数据（包含对公告数据的更新）
	private String updateTmAndGg( List<String> appNames, StringBuilder message, StringBuilder errMessage) throws Exception {
		boolean needUpdateTm=true;
		boolean needUpdateGg=true;
		boolean needUpdateTmDeTail=true;	
		
		logger.info("call updateTmAndGg");
		
		
		if (updateConfig==null){
			updateConfig= new UpdateConfig();
		}
		
		String	ggInterval=updateConfig.getGonggao_interval();
		
		String	tmInterval=updateConfig.getTm_interval();
		

		List<String> needUpdateGgNames = new ArrayList<String>();
		List<String> needUpdateTmNames = new ArrayList<String>();
		if (updateAll){
			// 获取过商标，并且距离上次获取公告的时间间隔符合规则的申请人，即: 需要更新商标公告的申请人
			needUpdateGgNames = getAppNameHasTm(ggInterval);
		
			// 获取过商标，并且距离上次获取商标的时间间隔符合规则的申请人，即: 需要更新商标详情的申请人
		    needUpdateTmNames = getAppNameHasTm(tmInterval);
		}
		
		
		long startTime=System.currentTimeMillis(); //更新商标的任务开始的时间
		
		for (String appName : appNames) {
			
			long endTime=System.currentTimeMillis(); //上一个申请人的商标数据更新完成时间
			
			long duration= endTime-startTime; //更新任务持续执行的时间
			
			if (duration>maxUpdateTime){
				logger.info("Too long time for update trademark , stop it now!");
				break;
			}
			
			String newAppName=processAppName(appName);
			
			int count=1;
			
			if(updateAll && needUpdateTm){
				int remainSearchCount=searchUtil.getRemainSearchCount();
				// 自动更新，保留150次调用申请人商标列表接口的机会给日常的更新使用
				if (remainSearchCount<151){
					logger.info("停止调用search接口，因为可用的接口调用次数为" + remainSearchCount);
					
					needUpdateTm=false;
				}
			}
			
			while(needUpdateTm){	
				List<TradeMark> tmList = null;
				
				if (count==1){
					tmList = getTradeMarkList(newAppName);
				}
				
				// 获取商标数据，并插入数据库标
				boolean finished=updateTm(tmList, appName, message, errMessage);
				if (finished){	
					
					//将刚刚获取的申请人加入需要更新公告和更新商标详情的申请人列表
					if (!needUpdateGgNames.contains(appName)){
						needUpdateGgNames.add(appName);
					}
					if (!needUpdateTmNames.contains(appName)){
						needUpdateTmNames.add(appName);
					}
					break;
				}
				
				if (errMessage.indexOf("符合条件的商标列表接口的查询次数配额不足")>-1){
					needUpdateTm = false;
					break;
				}	
				
				count++;	
				if (count>limitCount){					
					break;
				}
				
				if (count>1){
					if (errMessage.indexOf("服务器503错误")>-1){
						Thread.sleep(60*1000);	
					}else{
						Thread.sleep(30*1000);	
					}
				}
	
			}
			
				
			if(updateAll && needUpdateGg){
				int remaiGgCount=searchUtil.getRemainGgCount();
				// 自动更新，保留10000次调用申请人商标公告接口的机会给日常的更新使用
				if (remaiGgCount < 10001){
					logger.info("停止调用tm-gonggao-list接口，因为可用的接口调用次数为" + remaiGgCount);
					
					needUpdateGg=false;
				}
			}
			if (needUpdateGgNames.contains(appName)){						
				count=1;
				while(needUpdateGg){	
					//更新商标公告		
					boolean finished=updateTmGg(appName,message, errMessage);
					if (finished){	
						break;
					}
									
					if (errMessage.indexOf("商标公告接口查询次数配额不足")>-1){
						needUpdateGg = false;
						break;
					}
					
					count++;	
					if (count>limitCount){					
						break;
					}
					if (count>1){
						if (errMessage.indexOf("服务器503错误")>-1){
							Thread.sleep(60*1000);	
						}else{
							Thread.sleep(30*1000);	
						}						
					}
				}
				
			}
		
			
			// 如果从来没有获取过该申请人的商标列表
			// 那么该申请人的商标为空，也就没有商标注册号，也就无需要获取商标详细信息的接口（需要商标注册号）。			
			if (!needUpdateTmNames.contains(appName)){
				continue;
			}
			
			
			if (updateAll && needUpdateTmDeTail){
				int remainInfoCount=searchUtil.getRemainInfoCount();
				// 自动更新申请人商标时，保留10000次调用申请人商标详情接口的机会给日常的更新使用
				if (remainInfoCount < 10001){
					logger.info("停止调用info接口，因为可用的接口调用次数为" + remainInfoCount);
					break;
				}
			}
			
			List<TradeMark> tmList2 =  getTradeMarkList(newAppName);
			if (tmList2!=null && tmList2.size()>0){
				count=1;
				while(needUpdateTmDeTail){
					// 更新商标详细数据	
					boolean finished=updateTmDetail(appName, tmList2, message, errMessage);
					if (finished){	
						break;
					}
					
					if (errMessage.indexOf("商标详情接口查询次数配额不足")>-1){
						needUpdateTmDeTail = false;
						break;
					}
					
					
					count++;
					if (count>limitCount){				
						break;
					}
					if (count>1){
						if (errMessage.indexOf("服务器503错误")>-1){
							Thread.sleep(60*1000);	
						}else{
							Thread.sleep(30*1000);	
						}	
						
					}
				}
			
			}
			
			
			
		}
		
				
		String result= message + "<tr></tr>" + errMessage;
		
        return result;		

	}
	

	// 更新商标数据（包含对公告数据的更新）
	private String updateTradeMark(List<String> appNames, StringBuilder message, StringBuilder errMessage) throws Exception {
		
		List<TradeMark> tmList = getCustomerTmList();	
		for (String appName : appNames) {			
			updateTm(tmList, appName,message, errMessage);			
		}
		
			
		for (String appName : appNames) {			
			
			updateTmDetail(appName,tmList, message, errMessage);		
		}
		String result = message + "<tr></tr>" + errMessage;
		
        return result;		

	}
	
	
	private boolean updateTmGg(String appName, StringBuilder message, StringBuilder errMessage) throws Exception {
		 
		String msg=null;		
		
		String opt=Constants.gonggaoOpt;
		
		// 判断是否需要从API接口获取该客户的商标数据
        // 以避免不必要的接口调用（浪费调用接口的次数，降低了更新数据的效率）
		String result=ifNeedUpdate(appName, null, opt);	
		if (result==null){	
			//从API接口获取商标数据，将新增的商标数据插入数据库
			msg = updateGongGao(appName);
			processMsg(message,errMessage,msg);		
			if(msg.indexOf("exception")>-1){
				return false;
			}
		}			
      
		return true;

	}
	
	
	
	
	public TradeMark searchTradeMark(String regNumber) throws Exception {			
		
		//设置API Key
		setKey();
		
		TradeMark tm = searchTradeMarkInfo(regNumber);
			
	    return tm;	
      

	}
	
	
	public TradeMark queryTradeMark(String regNumber, String tmType) throws Exception {			
		
		//设置API Key
		setKey();
		
		TradeMark tm = queryTradeMarkInfo(regNumber,tmType);
			
	    return tm;	
      

	}
	
	
	
	
	private boolean updateTm(List<TradeMark>tmList, String appName, StringBuilder message, StringBuilder errMessage) throws Exception {
				 
		String msg=null;		
		
		String opt=Constants.tmOpt;
		
		// 判断是否需要从API接口获取该客户的商标数据
        // 以避免不必要的接口调用（浪费调用接口的次数，降低了更新数据的效率）
//		String result=ifNeedUpdate(appName, null, opt);	
//		if (result==null){	
			//从API接口获取商标数据，将新增的商标数据插入数据库
			msg = insertTradeMarkData(tmList, appName);
			processMsg(message,errMessage,msg);	
			if(msg.indexOf("exception")>-1){
				return false;
			}
//		}			
      
		return true;

	}
	
	
	
	private boolean updateTmDetail(String appName, List<TradeMark> tmList, StringBuilder message, StringBuilder errMessage) throws Exception {
				 
		String msg=null;		
		
		String opt=Constants.tmOpt;
				
//		Integer updateId=null;
//		//判断某个客户的商标数据是否需要更新
//		Map<Integer, Date> updateRecord=getTradeMarkUpdateRecord(appName, opt);
//		Iterator<Entry<Integer, Date>> iter = updateRecord
//				.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry<Integer, Date> entry = (Map.Entry<Integer, Date>) iter
//					.next();
//			updateId  = entry.getKey();
//			break;
//		}
//		
//		String status=getTradeMarkUpdateStatus(appName, opt);
//		if (status==null || !status.equals(Constants.updateFinish)){	
			// 更新商标详细数据		
			msg = updateTradeMarkDetail(appName, tmList);					
			processMsg(message,errMessage,msg);	
			if(msg.indexOf("exception")>-1){
				return false;
			}
//		}
		
		return true;
	}
	
	
	
	
	// 按照客户Id、客户名称和申请人查找商标数据/商标公告数据，并更新数据库
	private String updateData(List<String> appNames, String opt) {
		
		//设置API Key
		setKey();		
		
		StringBuilder message = new StringBuilder();
		
		StringBuilder errMessage = new StringBuilder();
		
		String result="";
		
		// 处理申请人的名字，将其中的中文括号改为英文括号
//		List<String> newAppNames=processAppName(appNames);
				
		try {
			
			if (opt==null || opt.equals(Constants.tmAndggOpt)){
				//更新商标和公告数据
				logger.info("start update trademark and gonggao data");
				result = updateTmAndGg(appNames, message, errMessage);
			}				
			else if(opt.equals(Constants.tmOpt)){		
				//更新商标数据		
				result = updateTradeMark(appNames, message, errMessage);
			}else if(opt.equals(Constants.gonggaoOpt)){			   
				// 更新该客户的所有商标公告数据
				// 判断是否需要从API接口获取该客户的商标公告数据
		        // 以避免不必要的接口调用（浪费调用接口的次数，降低了更新数据的效率）
				for (String appName : appNames) {
					result=ifNeedUpdate(appName, null, opt);	
					if (result==null){	
						String msg = updateGongGao(appName);
						processMsg(message,errMessage, msg);
					}
				}
				
			}
						
			
		} catch (Exception e) {
			e.printStackTrace();
			String msg=e.getMessage();
			if (errMessage.equals("")) {
				errMessage = errMessage.append("exception-" + msg);
			}else{
				errMessage = errMessage.append("<p></p>" + msg);
			}				
		}		
		
		result = message + "<tr></tr>" + errMessage;
		
		return result;
	}
	
	
	

	// 更新某个客户的申请人的商标数据
	public String updateTmDataOfApps(List<String> appNames, String opt) {
		
		
		String message = null;	
				
		try {
			
//			if (custId==null || custId.intValue()==0){
//				if (custName!=null && !custName.equals("")){
//					//根据客户名称获取客户Id
//					custId = getCustId(custName);
//				}
//			}
//			
//			if (custId == null || custId.intValue() == 0) {
//				message = "系统不存在该客户，请系统管理员先在系统中添加该客户";
//				return exceptionMsg + message;
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
			return exceptionMsg + message;
		}
		
		
		try {
			
			/*
			if (appNames!=null && appNames.size()>0){
				List<String> newAppNames = new ArrayList<String>();
				
				for(String name:appNames){
					newAppNames.add(name);
				}
				
				// 过滤掉数据库已有的申请人
				processApplicant(newAppNames);
				
				if (newAppNames!=null && newAppNames.size()>0){
					// 将新的申请人插入数据库
//					insertApplicantTable(newAppNames);
				}
			}
			*/
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		message=updateData(appNames, opt);		
		
		
		return message;
	}
	
	
	
	/*
	// 更新集合中多个客户的商标数据
	public String updateMultiCustData(List<String>  custNames, String opt) {
		
		//设置API Key
		setKey();
		
		String message = "";		
		Integer custId = null;
		List<String> appNames = null;		
			
		for(String custName: custNames){
			
			try {
				
				//根据客户名称获取客户Id
				custId = getCustId(custName);
				if (custId == null || custId.intValue() == 0) {
					message = "系统不存在该客户，请系统管理员先在系统中添加该客户";
					return exceptionMsg + message;
				}
								
				//获取客户的所有申请人
				appNames = getAppNames(custId);
				
				if (appNames == null || appNames.size()== 0) {
					message = "系统中缺少该客户的申请人，请系统管理员先在系统中添加该客户的申请人";
					return exceptionMsg + message;
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				message = e.getMessage();
				return exceptionMsg + message;
			}
			
			
			//更新商标数据	
			String msg=updateData(custId, appNames, opt);		
			message=message+msg;
		}
				
		
		return message;
	}
	*/
	
	
	// 更新客户的所有申请人的商标数据
	public String updateTmDataOfOneCust(String appName, String opt) {
			
		updateAll=false;
		
		//设置API Key
		setKey();
		
		String message = "";		
	
		List<String> appNames = null;						
		try {
			
//			if (custId==null || custId.intValue()==0){
//				if (custName!=null && !custName.equals("")){
//					//根据客户名称获取客户Id
//					custId = getCustId(custName);
//				}
//			}
//			
//			logger.info("custId: "+custId);
//			
//			if (custId == null || custId.intValue() == 0) {
//				message = "系统不存在该客户，请系统管理员先在系统中添加该客户";
//				return exceptionMsg + message;
//			}
							
			//获取客户的所有申请人
//			appNames = getAppNames(custId);
			appNames.add(appName);
						
			if (appNames == null || appNames.size()== 0) {
				message = "没有获取到申请人信息，停止更新申请人的商标数据";
				logger.info(message);		
				return exceptionMsg + message;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			message = e.getMessage();
			return exceptionMsg + message;
		}
		
		logger.info("start update data ");
		//更新商标数据	
		String msg=updateData(appNames, opt);		
		message=message+msg;
	
		logger.info("end update data ");		
		
		return message;
	}
		
	
	
	// 更新系统中所有客户的商标数据
	public String updateAllCustData() {
		
		logger.info("start update trademark data");
				
		//设置API Key
		setKey();
		
		//每天定时执行自动更新操作时，必须重新设置剩余调用次数，
		//否则，其中的调用次数仍然是昨天的最后调用次数
		//比如：remainSearchCount代表的是search接口的剩余可用调用次数
		//如果不初始化，那么当该值达到最小限额（10）之后，
		//即使到了第二天，再次调用该接口，本来该值应该设置为最大可用次数（2000）
		//由于没有再次初始化SearchUtil，导致该值仍然是10
		//导致不再执行更新流程
		searchUtil.init();
		
		String opt=Constants.tmAndggOpt;
		
		String message = "";		
		
//		List<Integer> custIdList=null;
//		try {
//			custIdList = getCustomerId();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}	
//		if (custIdList==null){
//			message = "系统中不存在客户，请系统管理员先在系统中添加客户";
//			return exceptionMsg + message;
//		}
		
		
		// 标志位，当值为true：代表自动更新，即：更新所有符合条件的申请人的商标
		updateAll=true;
		
		
//		int count=1;
//		boolean needUpdate=true;
				
//		while(needUpdate){
//			for(Integer custId: custIdList){			
				List<String> appNames = null;
//				List<String> appNamesFromTm = null;
				try {
					
//					if (custId == null || custId.intValue() == 0) {
//						message =  "custId为"+custId+"的客户不存在，请系统管理员先在系统中添加该客户";
//						continue;
//					}
					
					//由于腾讯科技（深圳）有限公司的商标数量达到18000多个，占用调用接口的次数
					//因此自动更新时，不再更新腾讯的商标。					
//					Integer txCustId = getCustId("腾讯科技（深圳）有限公司");
//					if (txCustId.intValue()==custId.intValue()){
//						continue;
//					}
					
									
					//获取客户的所有申请人
//					appNames = getAppNames(custId);
					if (updateConfig==null){
						updateConfig= new UpdateConfig();
					}
					
					String	interval=updateConfig.getTm_interval();
					
//					appNames = getAppNames(interval);
					
					appNames = getAppNameList(interval);
					
					
					/*
					appNamesFromTm = getAppNameFromTm(custId);					
					if (appNamesFromTm!=null){
						for(String name:appNamesFromTm){
							boolean sameOne=false;
							if (appNames!=null){
								for(String appName:appNames){
									if(name!=null && appName!=null && name.equals(appName)){
										sameOne=true;
										break;
									}
								}
								if (!sameOne){
									appNames.add(name);
								}
							}
						}
					}					
					*/
					
				
				} catch (Exception e) {
					e.printStackTrace();
					if (message==null || message.equals("")){
						message = e.getMessage();
					}else{
						message = message+e.getMessage();
					}
					return exceptionMsg + message;
				}
				
				if (appNames != null && appNames.size()> 0) {
					//更新商标数据	
					String msg=updateData(appNames, opt);		
					message=message+msg;
				}else{
					message = "没有获取到申请人信息，停止更新申请人的商标数据";
					logger.info(message);		
				}
//			}
			
//			if (message.indexOf(exceptionMsg)<0){
//				break;
//			}
			
//			count++;	
//			if (count>limitCount){
//				needUpdate = false;
//				break;
//			}
//			
//			if (count>1){				
//				try {					
//					//等待30分钟后，再执行
//					Thread.sleep(1800*1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}	
//			}
//		
//		}
				
		
		return message;
	}
			

	

	// 在独立的线程中，更新某个客户的申请人的商标数据
	/**
	 * @param customerId
	 * @param custName
	 * @param appName
	 * @return
	 * @throws Exception
	 */
	public String updateTmDataByThread(String appName, String tokenID) throws Exception{
		
		String condition=null;
		List<String> appNames=new ArrayList<String>();
		if (appName != null && !appName.equals("")) {
			StringTokenizer tok = new StringTokenizer(appName, ",");
			while (tok.hasMoreTokens()) {
				String key = tok.nextToken();
				appNames.add(key);
				if (condition==null){
					condition="'"+key+"'";
				}else{
					condition=condition+","+"'"+key+"'";
				}
			}			
		}	
		
		//根据给定的逻辑申请人，获取所有申请人信息
		appNames=selectApplicant(condition);
		
		if (appNames==null || appNames.size()==0){
			String msg="申请人名称不正确，请检查";
			return msg;
		}
		
		condition=null;
		for(String name: appNames){
			if (condition==null){
				condition="'"+name+"'";
			}else{
				condition=condition+","+"'"+name+"'";
			}
		}
		
		boolean isUpdate=isUpdated(condition);		
		if (isUpdate){
			String msg="该潜在用户的数据已经导入过";
//			return msg;
		}
		
		
		
//		Integer custId=null;		
		//获取 custId
//		if (customerId!=null && !customerId.equals("")){
//			if(StringUtils.isNum(customerId)){
//				custId=new Integer(customerId);
//			}
//		}else{			
//			if (custName!=null && !custName.equals("")){
//				//根据客户名称获取客户Id
//				custId = getCustId(custName);
//			}	
//			if (custId==null || custId.intValue()==0){
//				String msg="系统中不存在客户："+ custName;
//				return msg;
//			}
//		}
			
		
		
		
		String opt=Constants.tmAndggOpt;	
		
				
		
//		MyThread mt=new MyThread();
//		mt.tmDataService=this;
//		mt.custId=custId;
//		mt.custName=custName;
//		mt.appNames=appNames;
//		mt.opt=opt;		
		
//		String message=updateTmDataOfOneCust(custId, custName, opt);	
		
//		new Thread(mt).start();
		
//		mt.start();
		
			
		
		ExcuteTask task=new ExcuteTask();
//		XuzhanService xuzhanService=new XuzhanService();
//		task.setCustId(custId);
//		task.setCustName(custName);
		task.setAppNames(appNames);
		task.setOpt(opt);
		task.setTmDataService(this);
		
		String url=null;
				
		if (serviceUrlConfig!=null && tokenID!=null){
			 url=serviceUrlConfig.getBdyserviceUrl()+"/trademark/update?tokenID="+ tokenID;	
		}	
		
		if(url!=null){
			task.setUrl(url);		
		}
		
		pool.execute(task);
		
		
		return null;
	}
	
	

	// 在独立的线程中，更新某个客户的申请人的商标数据
	public List<AppDataStatus> getTmDataProcess(String appName) throws Exception{
		
		
//		Integer custId=null;		
		//获取 custId
//		if (customerId!=null && !customerId.equals("")){
//			if(StringUtils.isNum(customerId)){
//				custId=new Integer(customerId);
//			}
//		}
		
//		Date date = geTmDataUpdateDate(custId);
		
//		TmDataStatus tmDataStatus=getCustomerTmStatus(appName);
		
		List<AppDataStatus> appDataStatusList=getApplicantTmStatus(appName);	
		
//		tmDataStatus.setAppDataStatusList(appDataStatusList);
		
		return appDataStatusList;
	}


	
	public static void main(String[] args) {
		try {			
			
			TmDataService tmDataService = new TmDataService();
			
		
			String regNumber="17188084";
			String tmType="9,28";
			tmDataService.queryTradeMark(regNumber, tmType);
			/*
			String tmName="无限极萃雅";
			
			String tmType="1";
			
			List<TradeMark> list=tmDataService.querySameTm(tmName,tmType);
			
			if (list!=null && list.size()>0){
				for(TradeMark tm:list){
					String regNumber=tm.getRegNumber();
					System.out.print("regNumber="+regNumber);
				}
			}*/
			
//			String appName="BONPOINT";
//			String tokenID=null;
//			tmDataService.updateTmDataByThread(appName,tokenID);
			
//			String regNumber="15932254";
//			
//			TradeMark tm=tmDataService.searchTradeMark(regNumber);
						
//			tmDataService.updateAllCustData();
			
//			System.out.print("call updateAllCustData again /r/n");
//			
//			tmDataService.updateAllCustData();

			// Global.webProperties.API_KEY="A_z86HPnzA";
			// Global.webProperties.API_PASSWORD="aA8n4kzruf";
//			Global.webProperties.API_KEY = "QIJIAN_380109332";
//			Global.webProperties.API_PASSWORD = "SMQicSvJNB";
			
			/*
			// 目前参数是商标数据对应的操作和custId，custName（必须输入）
			if (args == null || args.length < 2) {
				logger.info("必须输入需要执行的操作（可能的值包括：gonggao：表示更新公告数据，tm：表示更新商标数据），以及custName，另外，可以输入多个appName，中间用空格分隔");
				System.exit(1);
			}

			// 第1个参数是操作，可能的值包括：gonggao：表示更新公告数据，tm：表示更新商标数据
			
			String opt = args[0];			
			
			
			// 第2个参数是客户名称，即：custName。作为过滤条件，只向数据库插入该客户的商品和服务数据
//			String custName = args[1];
			
			String custName = null;
			
			
			// 从第3个参数开始的参数是申请人名称
			List<String> appNames = tmDataService.processParameter(args);
			if (appNames != null && appNames.size() > 0) {
				tmDataService.updateTmDataOfApps(custName, appNames, opt);
			} else {				
				tmDataService.updateTmDataOfOneCust(custName, opt);				
			}
			*/
			System.out.print("update trademark finished.");
//			System.exit(1);	

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	/*
	class MyThread implements Runnable{
		String opt=null;
		Integer custId=0;
		String custName=null;
		List<String> appNames=null;
		String message=null;
		
//		TmDataService tmDataService=null;
		
		public void run(){
			if (appNames!=null && appNames.size()>0){
				message=updateTmDataOfApps(custId, custName, appNames, opt);	
			}else{	
				System.out.println("call updateTmDataOfOneCust");
				message=updateTmDataOfOneCust(custId, custName, opt);	
			}						
		}		
	}
	
	
	
	class MyThread2 extends Thread {
		String opt=null;
		Integer custId=0;
		String custName=null;
		List<String> appNames=null;
		String message=null;
		
		TmDataService tmDataService=null;
		public void run() {
			if (appNames!=null && appNames.size()>0){
				message=updateTmDataOfApps(custId, custName, appNames, opt);	
			}else{	
				logger.info("call updateTmDataOfOneCust");
				message=updateTmDataOfOneCust(custId, custName, opt);	
			}	
		}
	}
	
	*/
	
	

	
	
}



