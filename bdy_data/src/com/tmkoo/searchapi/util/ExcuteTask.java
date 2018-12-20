package com.tmkoo.searchapi.util;

import java.util.List;

import com.tmkoo.searchapi.common.LoginReturnInfo;

/**
 * 执行任务，实现Runable方式
 *
 */
public class ExcuteTask implements Runnable {

	String opt = null;
	Integer custId = 0;
	String custName = null;
	List<String> appNames = null;
	String message = null;
	String url=null;	
	

	TmDataService tmDataService = null;
	
	XuzhanService xuzhanService=null;

	public ExcuteTask() {

	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public List<String> getAppNames() {
		return appNames;
	}

	public void setAppNames(List<String> appNames) {
		this.appNames = appNames;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TmDataService getTmDataService() {
		return tmDataService;
	}

	public void setTmDataService(TmDataService tmDataService) {
		this.tmDataService = tmDataService;
	}

	@Override
	public void run() {
		try {
			if (appNames!=null && appNames.size()>0){
				message=tmDataService.updateTmDataOfApps(appNames, opt);	
			}
//			else{	
//				System.out.println("call updateTmDataOfOneCust");
//				message=tmDataService.updateTmDataOfOneCust(custName, opt);	
//			}	
			
			if(url!=null){					
				LoginReturnInfo rtnInfo = new LoginReturnInfo();
				//更新solr中的商标数据
				String jsonString = GraspUtil.getText(url);					
				rtnInfo = JsonUtil.toObject(jsonString, LoginReturnInfo.class);
				
				if (!rtnInfo.getSuccess()){
					
					AuthenticationService authenticationService = new AuthenticationService();
					String tokenID=authenticationService.getTokenID();
					
					String tokenStr="tokenID=";
					int len=tokenStr.length();
					int pos=url.indexOf(tokenStr);
					url=url.substring(0, pos+len);
					url=url+tokenID;
					
					jsonString = GraspUtil.getText(url);	
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public XuzhanService getXuzhanService() {
		return xuzhanService;
	}

	public void setXuzhanService(XuzhanService xuzhanService) {
		this.xuzhanService = xuzhanService;
	}

}