<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error Page</title>
</head>
<body>

	<h1>HTTP Status : 403 - Access Denied !!</h1>
	<br />
	<c:choose>
		<c:when test="${not empty username}">
			<h1>Hello ${username} ! You are not authorized to access this page !</h1>
		</c:when>
		<c:otherwise>
			<h1>You are not authorized to access this page !</h1>
		</c:otherwise>
	</c:choose>

</body>
</html>