<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>机台显示屏</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<script type="text/javascript" src="resources/jquery/jquery-1.8.0.js"></script>
<style type="text/css">
::-webkit-scrollbar {
	width: 0;
}
html,body {
	margin: 0;
	background: black;
	height: 100%;
}
</style>
</head>

<body>
	<iframe id="main" frameborder="0" style="width:100%;height:100%;overflow: auto;" src="tv/bz${no}/page"></iframe>
</body>
<script type="text/javascript">
	setInterval(function() {
		document.getElementById("main").src=document.getElementById("main").getAttribute("src");
	}, 1000*60*15);
</script>
</html>