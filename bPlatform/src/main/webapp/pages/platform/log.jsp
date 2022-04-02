<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>系统日志</title>
    <%@ include file="../base/meta.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/layerDate/need/laydate.css">
    <script type="text/javascript" src="<%=basePath %>/resources/layerDate/laydate.js"></script>
    <script type="text/javascript" src="<%=basePath %>/resources/platform/log.js"></script>
</head>

<body style="margin:0;">
<div id="toolbar">
    <jsp:include page="../base/toolbar.jsp">
        <jsp:param value="excel" name="ids"/>
        <jsp:param value="clear" name="ids"/>
        <jsp:param value="icon-excel" name="icons"/>
        <jsp:param value="icon-remove" name="icons"/>
        <jsp:param value="导出" name="names"/>
        <jsp:param value="清空" name="names"/>
        <jsp:param value="excel()" name="funs"/>
        <jsp:param value="clearAll()" name="funs"/>
    </jsp:include>
    <div>
        <form id="logFilter" style="margin: 0;">
            用户名称：<input type="text" name="filter[user]" id="user" like="true" value="" class="textbox"
                        style="height:22px;">
            登录账户：<input type="text" name="filter[name]" id="name" value="" like="true" class="textbox"
                        style="height:22px;">
            关键字：<input type="text" name="filter[operate]" id="operate" like="true" value="" class="textbox"
                       style="height:22px;">
            参数值：<input type="text" name="filter[paramsValue]" id="paramsValue" like="true" value="" class="textbox"
                       style="height:22px;">
            起始时间：<input type="text" name="filter[startlogDate]" id="startlogDate" value="" readonly="readonly"
                        class="textbox laydate-icon">
            结束时间：<input type="text" name="filter[endlogDate]" id="endlogDate" value="" readonly="readonly"
                        class="textbox laydate-icon">
            <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
               onclick="filter()">
                搜索
            </a>
        </form>
    </div>
</div>
<table id="dg" fit="true" fitColumns="false" title="" class="easyui-datagrid" url="<%=basePath %>log/list"
       style="width:100%;" toolbar="#toolbar" pagination="true"
       rownumbers="true" singleSelect="false">
    <thead data-options="frozen:true">
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="LOGDATE" sortable="true" width="130">日期</th>
        <th field="USERNAME" width="85">用户名称</th>
        <th field="LOGINNAME" width="85">登录账户</th>
        <th field="OPERATE" width="270">操作</th>
    </tr>
    </thead>
    <thead>
    <tr>
        <th field="IP" width="120">IP</th>
        <th field="PARAMS" width="120">参数</th>
        <th field="PARAMSVALUE" width="200">参数值</th>
        <th field="REQUESTIDENTITY" width="750">请求标识</th>
    </tr>
    </thead>
</table>
</body>
</html>
