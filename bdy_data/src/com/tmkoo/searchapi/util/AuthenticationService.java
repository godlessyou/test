package com.tmkoo.searchapi.util;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tmkoo.searchapi.common.ReturnInfo;
import com.tmkoo.searchapi.common.LoginReturnInfo;
import com.tmkoo.searchapi.common.Globals;

@Component
public class AuthenticationService {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ServiceUrlConfig serviceUrlConfig;

	public ReturnInfo authorize(String tokenID) {
		// 返回结果对象

		ReturnInfo rtnInfo = new ReturnInfo();

		if (serviceUrlConfig == null) {
			serviceUrlConfig = new ServiceUrlConfig();
		}

		// String url=serviceUrlConfig.getBdysysmUrl()+"/authorize?tokenID="+
		// tokenID;

		String url = serviceUrlConfig.getBdysysmUrl()
				+ "/auth/authenticate?tokenID=" + tokenID;

		logger.info(url);

		try {
			String jsonString = GraspUtil.getText(url);
			rtnInfo = JsonUtil.toObject(jsonString, LoginReturnInfo.class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// TODO Auto-generated catch block
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_EXCEPTION);

		}

		return rtnInfo;

	}

	// 登录认证
	public LoginReturnInfo login(String username, String password) {
		// 返回结果对象
		LoginReturnInfo rtnInfo = new LoginReturnInfo();

		String url = serviceUrlConfig.getBdysysmUrl() + "/auth/login?"
				+ "username=" + username + "&password=" + password;
		String jsonString;
		try {
			jsonString = GraspUtil.getText(url);
			// System.out.println(jsonString);
			rtnInfo = JsonUtil.toObject(jsonString, LoginReturnInfo.class);

		} catch (Exception e) {
			e.printStackTrace();
			rtnInfo.setSuccess(false);
			rtnInfo.setMessage(e.getMessage());
			rtnInfo.setMessageType(Globals.MESSAGE_TYPE_OPERATION_EXCEPTION);
		}
		return rtnInfo;
	}

	
	
	public String getTokenID() {

		String username = "yd_lina";
		String password = "123456";

		Object obj = login(username, password);
		LoginReturnInfo rtnInfo = (LoginReturnInfo) obj;

		String tokenID = rtnInfo.getTokenID();

		return tokenID;
	}

	
	
	public static void main(String[] args) {
		AuthenticationService authenticationService = new AuthenticationService();
//		authenticationService.authorize("15241193275160");
		
		String url="http://localhost:8080/bdy_bdyservice/interface/trademark/update?tokenID=11111111222";
		String tokenID="445555";
		
		String tokenStr="tokenID=";
		int len=tokenStr.length();
		int pos=url.indexOf(tokenStr);
		url=url.substring(0, pos+len);
		url=url+tokenID;
		
		System.out.println(url);

	}

}
