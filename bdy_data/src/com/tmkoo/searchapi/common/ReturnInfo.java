package com.tmkoo.searchapi.common;

public class ReturnInfo {

	//消息状态
	private Boolean success;
	
	//数据
	private Object data;	
	
	//总记录数
	private Long total;
	
	//当前页数
	private Integer currPage;
	
	//提示信息
	private String message;
	
	//消息类型
	private Integer messageType;
	
	
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getCurrPage() {
		return currPage;
	}

	public void setCurrPage(Integer currPage) {
		this.currPage = currPage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	

//	@Override
//	public String toString() {
//		return "ReturnInfo [success=" + success + ", data=" + data + ", total=" + total + ", currPage=" + currPage
//				+ ", message=" + message + ", messageType=" + messageType + ", casetotal=" + casetotal + "]";
//	}

	
}
