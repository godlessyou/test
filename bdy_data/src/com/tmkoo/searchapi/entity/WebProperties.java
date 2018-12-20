package com.tmkoo.searchapi.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
/**
 * 网站属性 * 
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "web_properties")
public class WebProperties extends IdEntity {
	private String webName;
	private String cssTemeplateName;
	private String registerOpen;
	private String desKey; 
	private Long maxSearchCountUnlogin; 
	private Long maxSearchCountLogin; 
	private Long maxInfoCountUnlogin; 
	private Long maxInfoCountLogin; 
	private String apiKey; 
	private String apiPassword; 
	
	public String getWebName() {
		return webName;
	}


	public void setWebName(String webName) {
		this.webName = webName;
	}


	public String getCssTemeplateName() {
		return cssTemeplateName;
	}


	public void setCssTemeplateName(String cssTemeplateName) {
		this.cssTemeplateName = cssTemeplateName;
	}


	public String getRegisterOpen() {
		return registerOpen;
	}


	public void setRegisterOpen(String registerOpen) {
		this.registerOpen = registerOpen;
	}


	public String getDesKey() {
		return desKey;
	}


	public void setDesKey(String desKey) {
		this.desKey = desKey;
	}


	public Long getMaxSearchCountUnlogin() {
		return maxSearchCountUnlogin;
	}


	public void setMaxSearchCountUnlogin(Long maxSearchCountUnlogin) {
		this.maxSearchCountUnlogin = maxSearchCountUnlogin;
	}


	public Long getMaxSearchCountLogin() {
		return maxSearchCountLogin;
	}


	public void setMaxSearchCountLogin(Long maxSearchCountLogin) {
		this.maxSearchCountLogin = maxSearchCountLogin;
	}


	public Long getMaxInfoCountUnlogin() {
		return maxInfoCountUnlogin;
	}


	public void setMaxInfoCountUnlogin(Long maxInfoCountUnlogin) {
		this.maxInfoCountUnlogin = maxInfoCountUnlogin;
	}


	public Long getMaxInfoCountLogin() {
		return maxInfoCountLogin;
	}


	public void setMaxInfoCountLogin(Long maxInfoCountLogin) {
		this.maxInfoCountLogin = maxInfoCountLogin;
	}


	public String getApiKey() {
		return apiKey;
	}


	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}


	public String getApiPassword() {
		return apiPassword;
	}


	public void setApiPassword(String apiPassword) {
		this.apiPassword = apiPassword;
	} 

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
