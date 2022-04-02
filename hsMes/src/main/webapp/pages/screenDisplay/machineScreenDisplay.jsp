<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() +(request.getServerPort()==80?"":(":"+request.getServerPort()))+path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="refresh" content="0;url=<%=basePath%>jt">
</head>

<body>

</body>
</html>