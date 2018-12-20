package com.tmkoo.searchapi.util;
 
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


public class GraspUtil {
	private static CookieStore cookie;
	/**
	 * 采集核心函数：根据url，获得该url的html值
	 * @param redirectLocation
	 * @return
	 * @throws Exception
	 */
	public static  String getText(String redirectLocation ) throws Exception {
		//Modification start, 2018-06-21
		redirectLocation = redirectLocation.trim();
		redirectLocation = redirectLocation.replace(" ", "%20");
		//Modification end
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 90000);
		HttpConnectionParams.setSoTimeout(httpParams, 90000); 
		HttpGet httpget = new HttpGet(redirectLocation);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParams); 
		String[] agent=new String[]{"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.57 Safari/537.17",
		 "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)"};
		Random rad=new Random();
		httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, agent[rad.nextInt(2)] );
		httpclient.getParams().setParameter(  CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8"); 
		if (cookie!=null){
			httpclient.setCookieStore(cookie);
		} 
		HttpContext httpContext = new BasicHttpContext(); 
		String responseBody = "";
		try { 
			
			HttpResponse response = httpclient.execute(httpget, httpContext);
			if(cookie == null){
				cookie=httpclient.getCookieStore();
				}
			int status = response.getStatusLine().getStatusCode();
			if(status == 503){
				throw new Exception("服务器503错误，连接不上，"+redirectLocation); 
			}
			responseBody = org.apache.http.util.EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
			throw e;
		} finally {
			httpget.abort();
		}
		return responseBody;
	}

}

