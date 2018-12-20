package com.tmkoo.searchapi.web.front;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmkoo.searchapi.common.Constants;
import com.tmkoo.searchapi.common.Global;



import com.tmkoo.searchapi.common.Globals;
import com.tmkoo.searchapi.util.AuthenticationService;
import com.tmkoo.searchapi.util.StringUtils;
import com.tmkoo.searchapi.util.TmDataService;
import com.tmkoo.searchapi.vo.AppDataStatus;
import com.tmkoo.searchapi.vo.TmDataCount;
import com.tmkoo.searchapi.vo.TmDataStatus;
import com.tmkoo.searchapi.vo.TradeMark;
import com.tmkoo.searchapi.common.ReturnInfo;


/**
 * 搜索
 * 
 * @author tmkoo
 */
@Controller
@RequestMapping(value = "interface/trademark")
public class TradeMarkController {


	
	@Resource
	private AuthenticationService authenticationService;
	
	@Resource
	private TmDataService tmDataService;

	/**
	 * 搜索初始化，进入过渡页，给用户提示等待5秒。
	 * @param searchType
	 * @param searchKey
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST )
	public String list(@RequestParam("custName") String custName, 
			@RequestParam(value = "appName") String appName,
			Model model,
			ServletRequest request) {
		 
		model.addAttribute("custName", custName);
		model.addAttribute("appName", appName);  

		return "trademark/loading";
	}
	
	
	/**
	 * 获取官网商标数据	
	 */
	@RequestMapping(value = "searchtmdata" , produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object searchTmData(@RequestParam(value = "appName", required = false) String appName,
			@RequestParam(value = "tokenID", required=false) String tokenID,
			HttpServletRequest request
	) {	
		
		// 如果请求参数中没有tokenID，返回提示用户重新登录的信息
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(tokenID);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}

	    rtnInfo =new ReturnInfo();
	    
		if(Global.webProperties.API_KEY==null || Global.webProperties.API_KEY.trim().equals("")){
			String message="Api key has not set";//管理员还未配置API呢
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(message);
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);			
			return  rtnInfo;
		}
				
		// 申请人参数检查
		if (appName == null || appName.equals("")) {			
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("require parameter appName");
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_PARAM_INVALID);
			return rtnInfo;
			
		}
		
		
		try {				
				 String msg=tmDataService.updateTmDataByThread(appName, tokenID);
				 if (msg!=null){
					 rtnInfo.setSuccess(false);
					 rtnInfo.setMessage(msg);
				 }else{
					 rtnInfo.setSuccess(true);
					 rtnInfo.setMessage("finish");
				 }
				
		} catch (Exception e) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);	
			return rtnInfo; 
		}	
		
		return rtnInfo; 
	}
	
	
	
	/**
	 * 获取申请人的商标总数	
	 */
	@RequestMapping(value = "querytmcount" , produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object queryTmCount(@RequestParam(value = "appName", required = false) String appName,
			@RequestParam(value = "tokenID", required=false) String tokenID,
			HttpServletRequest request
	) {	
		
		// 如果请求参数中没有tokenID，返回提示用户重新登录的信息
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(tokenID);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}		
		if(Global.webProperties.API_KEY==null || Global.webProperties.API_KEY.trim().equals("")){
			String message="Api key has not set";//管理员还未配置API呢
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(message);
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);			
			return  rtnInfo;
		}	

				
		
		try {
			
			//获取申请人的商标数量
			List<TmDataCount> list=tmDataService.getTmCount(appName);
			rtnInfo.setSuccess(true);			
			rtnInfo.setData(list);
			rtnInfo.setMessage("finish");
			
		} catch (Exception e) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);	
			return rtnInfo; 
		}	
				
		
		
		return rtnInfo; 
	}
	
	
	
	
	
	/**
	 * 获取官网商标数据更新情况	
	 */
	@RequestMapping(value = "tmdataprogress" , produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object tmDataProgress(@RequestParam(value="appName", required=false) String appName, @RequestParam(value="custName", required=false) String custName, 
			@RequestParam(value = "tokenID", required=false) String tokenID,
			HttpServletRequest request
	) {	
		
		// 如果请求参数中没有tokenID，返回提示用户重新登录的信息
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(tokenID);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}				
		
		try {
			
			List<AppDataStatus> appDataStatusList =tmDataService.getTmDataProcess(appName);
			Long total=(long)0;
			if (appDataStatusList!=null && appDataStatusList.size()>0){
				total=(long)appDataStatusList.size();
			}
			
			rtnInfo.setData(appDataStatusList);	
			rtnInfo.setTotal(total);
			rtnInfo.setMessage("finished");
			rtnInfo.setSuccess(true);
			
		} catch (Exception e) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage("error："+e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);	
			return rtnInfo; 
		}	
			
		return rtnInfo; 
	}
	
	
	
 
	/**
	 * Ajax真正的搜索，返回搜索结果编号，将搜索结果存入SESSION。
	 * @param searchType
	 * @param searchKey
	 * @return
	 */
	@RequestMapping(value = "dosearch")
	@ResponseBody
	public String dosearch(@RequestParam(value = "appName") String appName,
			HttpServletRequest request
	) {		

		
		if(Global.webProperties.API_KEY==null || Global.webProperties.API_KEY.trim().equals("")){
			return "NOTSET";//管理员还未配置API呢
		}
	
		
		boolean updateAllApp=false;
		List<String> appNames=new ArrayList<String>();
		if (appName != null && !appName.equals("")) {	
			if(appName.equalsIgnoreCase("all")){
				updateAllApp=true;
			}else{
				StringTokenizer tok = new StringTokenizer(appName, "\r\n");
				while (tok.hasMoreTokens()) {
					String key = tok.nextToken();
					appNames.add(key);
				}
			}
		}
		
		String opt=Constants.tmAndggOpt;
		String message= null;
		
		if (!updateAllApp){
			message=tmDataService.updateTmDataOfApps(appNames, opt);	
		}else{
			
			if (appName.equalsIgnoreCase("all")){
				message=tmDataService.updateAllCustData();	
			}
//			else{	
//				message = tmDataService.updateTmDataOfOneCust(custName, opt);		
//			}
		}
		
		
		String msg=message;
//		String success="success-";		
//		String exception="exception-";
//		if (message!=null){
//			if (message.indexOf(success)>-1){
//				int len=success.length();
//				int pos=message.indexOf(success);
//				 msg=message.substring(pos+len);			
//			}else if(message.indexOf(exception)>-1){				
//				int len=exception.length();
//				int pos=message.indexOf(exception);
//				msg=message.substring(pos+len);				
//			}
//		}
		
		return "SEARCHRESULT:"+msg; 
	}
	
	

	/**
	 * 获取具有相同名称的商标的集合	
	 */
	@RequestMapping(value = "querySameTm" , produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object querySameTm(@RequestParam(value = "tmName", required = false) String tmName,
			@RequestParam(value = "tmType", required = false) String tmType,			
			@RequestParam(value = "tokenID", required=false) String tokenID,
			HttpServletRequest request
	) {	
		
		// 如果请求参数中没有tokenID，返回提示用户重新登录的信息
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(tokenID);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}		
		if(Global.webProperties.API_KEY==null || Global.webProperties.API_KEY.trim().equals("")){
			String message="Api key has not set";//管理员还未配置API呢
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(message);
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);			
			return  rtnInfo;
		}	
	
		
		try {
			
			if (tmName==null || tmName.equals("")){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("参数tmName不能为空");
				rtnInfo.setMessageType(-1);
			}
			
			int len=tmName.length();
			if (len<2){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("不支持对长度小于2的商标文字进行查询");
				rtnInfo.setMessageType(-1);
			}
			if (tmName.equals("图形")){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("不支持对商标文字为图形的进行查询");
				rtnInfo.setMessageType(-1);
			}
			
			if (tmType==null || tmType.equals("")){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("参数tmType不能为空");
				rtnInfo.setMessageType(-1);
			}
			
//			int len2=tmType.length();
//			if (len2>2){
//				rtnInfo.setSuccess(false);
//				rtnInfo.setMessage("每次只允许从已经发布公告的商标数据中查询一个国际分类的商标");
//				rtnInfo.setMessageType(-1);
//			}
			
			int pos=tmType.indexOf(",");
			if(pos>-1){
				tmType=tmType.replaceAll(",", ";");
			}
			
		
			//获取具有相同商标名称的商标的信息
			List<TradeMark> list=tmDataService.querySameTm(tmName,tmType);			
			rtnInfo.setData(list);
						
			rtnInfo.setSuccess(true);	
			rtnInfo.setMessage("finish");
			
		} catch (Exception e) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);	
			return rtnInfo; 
		}					
		
		
		return rtnInfo; 
	}
	
	
	
	/**
	 * 获取具有相同名称的商标的集合	
	 */
	@RequestMapping(value = "queryTradeMark" , produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object queryTradeMark(@RequestParam(value = "regNumber", required = false) String regNumber,
			@RequestParam(value = "tmType", required = false) String tmType,
			@RequestParam(value = "tokenID", required=false) String tokenID,
			HttpServletRequest request
	) {	
		
		// 如果请求参数中没有tokenID，返回提示用户重新登录的信息
		ReturnInfo rtnInfo = (ReturnInfo) authenticationService.authorize(tokenID);
		if (!rtnInfo.getSuccess()) {
			return rtnInfo;
		}		
		if(Global.webProperties.API_KEY==null || Global.webProperties.API_KEY.trim().equals("")){
			String message="Api key has not set";//管理员还未配置API呢
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(message);
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);			
			return  rtnInfo;
		}	
	
		
		try {
			
			if (regNumber==null || regNumber.equals("")){
				rtnInfo.setSuccess(false);
				rtnInfo.setMessage("参数regNumber不能为空");
				rtnInfo.setMessageType(-1);
			}
			TradeMark tm=null;	
			if(tmType==null || tmType.equals("")){
				//获取具有相同商标注册号的商标的信息
				tm=tmDataService.searchTradeMark(regNumber);	
			}else{
				StringTokenizer idtok3 = new StringTokenizer(tmType, ",");					
				while (idtok3.hasMoreTokens()) {					
					String tmType1= idtok3.nextToken();					
					if (tmType1 != null && !tmType1.equals("")) {
						if (!StringUtils.isNum(tmType1)) {	
							throw new Exception("商标的国际分类号不是数字");
						}
					}
				}
				//获取具有相同商标注册号和商标国际分类号的商标的信息
				tm=tmDataService.queryTradeMark(regNumber, tmType);
			}
			rtnInfo.setData(tm);
					
			rtnInfo.setSuccess(true);	
			rtnInfo.setMessage("finish");
			
		} catch (Exception e) {
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_INVALID);	
			return rtnInfo; 
		}					
		
		
		return rtnInfo; 
	}
	
	/**
	 * 翻页，从session中读取原来的列表
	 * @param pageNo
	 * @param l
	 * @param intClss
	 * @param intCls
	 * @param searchType
	 * @param searchKey
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "result", method = RequestMethod.GET)
	public String result(@RequestParam(value = "result", defaultValue = "1") String result, //操作结果
			@RequestParam(value = "message", defaultValue = "") String message,  //消息		
			Model model,
			HttpServletRequest request) {    	
		
		//将这些数据返回到页面
		model.addAttribute("result", result);
		model.addAttribute("message", message);
		
		return "trademark/result";
	}
	
	/**进入未登录页面***/
	@RequestMapping(value = "unlogin", method = RequestMethod.GET)
	public String unlogin() { 
		return "trademark/unlogin";
	}
	@RequestMapping(value = "noresult", method = RequestMethod.GET)
	public String noresult() { 
		return "trademark/noresult";
	}
	/**进入机器人提示页面***/
	@RequestMapping(value = "rabot", method = RequestMethod.GET)
	public String rabot() { 
		return "trademark/rabot";
	}
	

	
	
}
