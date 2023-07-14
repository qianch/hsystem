<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>订阅通知</title>
    <%@ include file="../base/meta.jsp" %>
</head>

<body>
<form id="subMessage" method="post"
      action="<%=basePath %>msg/${addOrDelete}" autocomplete="off">
    <div id="dl" class="easyui-datalist" checkbox="true" singleSelect="false" lines="true" style="width:100%;"
         valueField="ID" textField="VALUE"
         data-options="url: '<%=basePath %>msg/${mTypeUrl}',
            method: 'post'"></div>
</form>
</body>
</html>
