<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration Success Page</title>
</head>
<body>

	<h1>${title}</h1>
	<br />
	<h1>The user with User Name : ${username} has been registered successfully.</h1>
	<br />
	<br />
	
	<h2>Click <a href='<c:url value="/login" />'>Here</a> to go to the Login Page.</h2>

</body>
</html>