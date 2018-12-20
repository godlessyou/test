package com.tmkoo.searchapi.util;

import org.springframework.stereotype.Component;

import com.tmkoo.searchapi.common.Constants;
import com.tmkoo.searchapi.common.Global;


@Component
public class ServiceUrlConfig {

	// 用户管理等服务接口
	private String bdysysmUrl="http://localhost:8080/bdy_sysm/interface";
	
	
	private String bdyserviceUrl="http://localhost:8080/bdy_service/interface";
	
	// 相关文件的存储目录
    private String fileUrl="";
	
	private String dataBaseIp="localhost";
		
	
	public String getBdysysmUrl() {
		return bdysysmUrl;
	}

	public void setBdysysmUrl(String bdysysmUrl) {
		this.bdysysmUrl = bdysysmUrl;
	}

	public String getDataBaseIp() {
		return dataBaseIp;
	}

	public void setDataBaseIp(String dataBaseIp) {
		this.dataBaseIp = dataBaseIp;
		
		//设置链接数据库所用Url中的IP地址
		boolean initFlag=DatabaseUtil.isInitFlag();
		if (!initFlag){
			DatabaseUtil.init(dataBaseIp);
		}
	}

	public String getBdyserviceUrl() {
		return bdyserviceUrl;
	}

	public void setBdyserviceUrl(String bdyserviceUrl) {
		this.bdyserviceUrl = bdyserviceUrl;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {		
		fileUrl=fileUrl.trim();
		this.fileUrl = fileUrl;
		
		//设置文件存储所用的目录
		boolean initFlag=Constants.isInitFlag();
		if (!initFlag){
			Constants.init(fileUrl);
		}
		
		//设置key
		setKey();
		
	}
	
	private void setKey(){
    	if (Global.webProperties.API_KEY==null || !Global.webProperties.API_KEY.equals("QIJIAN_380109332")){
			Global.webProperties.API_KEY = "QIJIAN_380109332";
			Global.webProperties.API_PASSWORD = "SMQicSvJNB";
		}

    }
    
    

}
