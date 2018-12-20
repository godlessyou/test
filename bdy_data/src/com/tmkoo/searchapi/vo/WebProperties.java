package com.tmkoo.searchapi.vo;

/**
 * 网站属性
 * @author Administrator
 *
 */
public class WebProperties { 
	public String WEB_NAME;//网站名 
	
	public String CSS_TEMPLATE_NAME;      //网站色调模板，默认红色
	public Boolean REGISTER_OPEN;//是否开放注册
	
	public String DES_KEY;//加密，用户后期可以修改，必须是32位
	
	public Long MAX_SEARCH_COUNT_UNLOGIN;//未登录用户最大允许的查询次数
	public Long MAX_SEARCH_COUNT_LOGIN;//已登录用户最大允许的查询次数
	public Long MAX_INFO_COUNT_UNLOGIN;//未登录用户最大允许的商标详细页次数
	public Long MAX_INFO_COUNT_LOGIN;//已登录用户最大允许的商标详细页次数
	
	public String API_KEY;
	public String API_PASSWORD;
	
}
