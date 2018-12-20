<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>查询中.. | </title>
	<style>
		#jumpTo{
	color:#ff0000;
	font-weight:bole;
	}
	</style> 
	<script>
	
	$(document).ready(function(){  
		ajaxSearchSetp1();
		goR(5);
	});
	
	//显示5秒钟
	function goR(secs ){   
		 $('#jumpTo').html(secs);  
		 if(--secs>0){     
		     setTimeout("goR("+secs+")",1000);     
		 }    
	 } 
	
	//============搜索===============================
	var isSoing=false; 
	//开搜，获得记录数+l
	function ajaxSearchSetp1(){
		if(isSoing){
			return;
		}
		isSoing = true;
	//	TMRAsynPost("${ctx}/search/dosearch","hideForm","text",ajaxSearchSetp2); 
		TMRAsynPost("${ctx}/trademark/dosearch","hideForm","text",ajaxSearchSetp2); 
	}
	function ajaxSearchSetp2(data){
		 isSoing =false;
		 
		 if(data.indexOf("SEARCHRESULT")==0){ 
			 var message = data.split(":")[1]; 		  	
		  	  $("#message").val(message);
		      $("#hideForm").submit();
	   }
		 else if(data.indexOf("RABOT")==0){
			 location.href="${ctx}/trademark/rabot";
		 }
		 else if(data.indexOf("UNLOGIN")==0){
			 location.href="${ctx}/trademark/unlogin";
		 }
		 else if(data.indexOf("NORESULT")==0){
			 location.href="${ctx}/trademark/noresult";
		 }
		 else if(data.indexOf("NOSET")==0){
			 location.href="${ctx}/trademark/noset";
		 }
	    else{
	     	 alert(data);
	  	     return;
	     }
	}
	</script>
</head>

<body>
	<form name="hideForm" id="hideForm" action="${ctx}/trademark/result" method="get">   
		    <input type="hidden" id="custName" name="custName" value="${custName}"/>  
		    <input type="hidden" id="appName" name="appName" value="${appName}"/> 			 
		    <input type="hidden" id="message" name="message" value=""/> 			  
	</form> 
	
	<div style="width:800px;margin:100px auto;text-align:center;">
		<img src="images/loading.gif"/>
		<br/>
		<br/>
		正在查询中...预计<span id="jumpTo">5</span>秒后出结果  
	</div>
</body>
</html>
