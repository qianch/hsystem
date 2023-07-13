<!--
作者:高飞
日期:2016-11-6 10:22:52
页面:PDA终端版本JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>PDA终端版本</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="version.js.jsp" %>
    <style>
        .app_qrcode {
            position: fixed;
            bottom: 40px;
            right: 0;
            height: 150px;
            width: 150px;
            z-index: 99999;
            background: url(<%=basePath%>resources/images/app.png);
            background-size: 100% 100%;
        }
    </style>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div class="app_qrcode"></div>
<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <jsp:include page="../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="增加" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="add()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
        </jsp:include>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" url="${path}app/list" toolbar="#toolbar"
           pagination="true" rownumbers="true" fitColumns="true" fit="true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="VERSION" sortable="true" width="15">版本号</th>
            <th field="UPLOADTIME" sortable="true" width="15">上传时间</th>
            <th field="VERSIONMEMO" sortable="true" width="15">版本说明</th>
            <th field="PATH" sortable="true" width="35" formatter="urlFormatter">下载地址</th>
            <th field="ISLATEST" sortable="true" width="15" formatter="latestFormatter">是否最新</th>
        </tr>
        </thead>
    </table>
</div>
</body>