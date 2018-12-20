<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>出错.. | </title> 
</head>

<body>
	<div style="width:800px;margin:100px auto;line-height: 28px;text-align:center;font-size:28px;color:#b20707">
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		查无记录！
		<br/>
	</div>
</body>
</html>
