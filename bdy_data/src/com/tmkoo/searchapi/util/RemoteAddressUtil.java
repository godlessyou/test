package com.tmkoo.searchapi.util;

import javax.servlet.http.HttpServletRequest;

public class RemoteAddressUtil {

	public static String getIp(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
        if (ip != null && !"unknown".equalsIgnoreCase(ip)) {
            String[] ipList = ip.split(",");
            for (String ipItem : ipList) {
                //ip min length 7: 0.0.0.0 
                if (ipItem.length() >= 7 && !"unknown".equalsIgnoreCase(ipItem)) {
                    return ipItem;
                }
            }
        }
        else if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("Proxy-Client-IP");    
        }    
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("WL-Proxy-Client-IP");    
        }    
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getRemoteAddr();    
        }
        return ip; 
	}
}
