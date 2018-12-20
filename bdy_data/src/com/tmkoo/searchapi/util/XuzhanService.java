package com.tmkoo.searchapi.util;


import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tmkoo.searchapi.common.ReturnInfo;



@Component
public class XuzhanService {
	
	private static Logger logger = Logger.getLogger(XuzhanService.class);

	public void createXuzhan(String url, String custId) {
		// 返回结果对象
		
//		ReturnInfo rtnInfo = new ReturnInfo();	
		
		
//		System.out.println(url);
	
		try {
			String jsonString = GraspUtil.getText(url);
//			rtnInfo=JsonUtil.toObject(jsonString, ReturnInfo.class);
//			boolean success=rtnInfo.getSuccess();
			
			logger.info("create xuzhan for "+ custId+" success!");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} 
		
		
	
	}
		


}
