package com.tmkoo.searchapi.common;

public class Globals {
	//返回的消息类型
	public final static Integer MESSAGE_TYPE_SESSION_INVALID = -1;// session失效

	public final static Integer MESSAGE_TYPE_GETDATA_FAILED = -2;// 操作数据库失败
	
	public final static Integer MESSAGE_TYPE_AUTHORTY_INVALID = -3;// 没有访问权限
	
	public final static Integer MESSAGE_TYPE_PARAM_INVALID = -4;// 参数不正确
	
	public final static Integer MESSAGE_TYPE_LINK_INVALID = -5;// 无效的链接

	public final static Integer MESSAGE_TYPE_OPERATION_INVALID = -6;// 无效操作
	
	public final static Integer MESSAGE_TYPE_OPERATION_EXCEPTION = -7;// 发生异常
	
		
}
