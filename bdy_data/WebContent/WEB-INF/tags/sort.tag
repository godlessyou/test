<%@tag pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  
<div class="menu">
<ul>
<li>排序：</li>
<li>
<a class="hide" href="javascrip:void(-1);"><c:if test="${empty param.sortType}">自动</c:if><c:if test="${not empty  param.sortType}">${sortTypes[param.sortType]}</c:if></a> 
<ul>
	  <c:forEach items="${sortTypes}" var="entry">
	   		<li><a href="?sortType=${entry.key}&${searchParams}">${entry.value}</a></li>
		</c:forEach> 
</ul>
</li>
</ul>
</div>