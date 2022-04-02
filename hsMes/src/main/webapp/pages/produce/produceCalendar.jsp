<!--
	作者:高飞
	日期:2016-11-1 13:05:53
	页面:排产日历JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>排产日历</title>
<%@ include file="../base/meta.jsp"%>
<%@ include file="produceCalendar.js.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/fullcalendar/fullcalendar.min.css">
<script type="text/javascript" src="<%=basePath%>/resources/fullcalendar/lib/moment.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/fullcalendar/fullcalendar.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/fullcalendar/locale/zh-cn.js"></script>
<style type="text/css">

.fc-ltr .fc-basic-view .fc-day-top .fc-day-number {
    float: right;
    color: black;
    font-weight: bold;
}

</style>
</head>

<body style="background: #f5f2f2;">
	<jsp:include page="../base/toolbar.jsp">
		<jsp:param value="add" name="ids" />
		<jsp:param value="icon-add" name="icons" />
		<jsp:param value="增加" name="names" />
		<jsp:param value="add()" name="funs" />
	</jsp:include>
	<div id='calendar'></div>
</body>
</html>