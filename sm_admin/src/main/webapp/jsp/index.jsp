<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*" %>
<html>
<head>
<title>Index Page</title>
</head>
<body>
	<h1>Hello world! ${uname}</h1>
	<h2>
		Current Time:<%=new Date().toLocaleString()%></h2>	
</body>
</html>