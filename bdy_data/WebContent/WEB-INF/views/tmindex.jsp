<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title></title>
</head>
<body>
 	<div id="tm">
		<br></br>
		<h3 style="margin-left: 8px;">获取客户商标数据并插入数据库</h3>
		<div>
    <div class="clear"></div>
	<form action="${ctx}/trademark" id="idxForm" name="idxForm" method="post" target="_blank">  
		
		
		  <table class="table table-striped table-bordered table-hover table-condensed">				
		      	<tr>
					<td>请输入客户名称</td>
					<td>
					<input id="custname" type="text" size="50" name="custName" autocomplete="off" value="" maxlength="30">
					</td>
					<td><p>每次只能输入一个客户名称</p></td>
				</tr>
					<tr>
					<td>请输入申请人名称</td>
					<td>
					 <textarea id="appname" name="appName"  placeholder="录入申请人" cols=51 rows=20></textarea> 
					</td>
					<td><p>可以输入多个申请人，每输入一个申请人后，用回车换行</p></td>
				</tr>
			         
		    </table>
		    
		    <div class="idxgo" onclick="goSearchTm();">查询</div>	
	    
	 </form>  
</body>
</html>