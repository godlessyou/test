<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.tmkoo.searchapi.common.Global" %> 
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<!--  <title><sitemesh:title/><%=Global.webProperties.WEB_NAME %></title> -->
<title><sitemesh:title/>获取客户的商标数据</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<link type="image/x-icon" href="${ctx}/images/favicon.ico" rel="shortcut icon">
<link href="${ctx}/js/jquery-validation/1.11.1/validate.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/css/<%=Global.webProperties.CSS_TEMPLATE_NAME %>.css?v=20151024" type="text/css" rel="stylesheet" />
<script src="${ctx}/js/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctx}/js/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/js/jquery-validation/1.11.1/messages_bs_zh.js" type="text/javascript"></script>
<script src="${ctx}/js/artDialog/artDialog.js?skin=twitter"  type="text/javascript"></script>
<script src="${ctx}/js/artDialog/other.js"  type="text/javascript"></script>
<script src="${ctx}/js/artDialog/plugins/iframeTools.source.js"  type="text/javascript" ></script>
<script src="${ctx}/js/base.js" type="text/javascript"></script>
<sitemesh:head/>
</head>



	
	<div class="container">
		<sitemesh:body/>
	</div>
	
	
	<script src="${ctx}/js/bootstrap/2.3.2/js/bootstrap.min.js" type="text/javascript"></script>
	
</body>
</html>