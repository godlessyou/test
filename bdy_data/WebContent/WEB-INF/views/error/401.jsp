<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%response.setStatus(200);%>

<!DOCTYPE html>
<html>
<head>
	<title>401 - 权限拦截</title>
</head>

<body>
	<h2>401 - 权限拦截，您无权访问此网页</h2>
	<p><a href="<c:url value="/"/>">返回首页</a></p>
</body>
</html>