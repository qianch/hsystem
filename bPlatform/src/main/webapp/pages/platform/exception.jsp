<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

    <title>系统日志</title>
    <%@ include file="../base/meta.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/layerDate/need/laydate.css">
    <script type="text/javascript" src="<%=basePath%>/resources/layerDate/laydate.js"></script>
    <script type="text/javascript" src="<%=basePath%>/resources/platform/exception.js"></script>
</head>

<body style="margin:0;">
<div id="toolbar">
    <a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="excel()" iconCls="icon-excel">导出</a>
    <a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="clearAll()"
       iconCls="icon-remove">清空</a>
    <div>
        <form id="exceptionFilter" style="margin: 0;">
            类名：<input type="text" name="filter[clazz]" id="clazz" like="true" value="" class="textbox"
                      style="height:22px;"> 方法名：<input type="text" name="filter[method]" id="method" value=""
                                                       like="true" class="textbox" style="height:22px;"> 起始时间：<input
                type="text"
                name="filter[occurDate1]" like="true" id="startoccurDate" value="" class="textbox laydate-icon"
                readonly="readonly">
            结束时间：<input type="text" name="filter[occurDate2]" like="true" id="endoccurDate" value=""
                        class="textbox laydate-icon" readonly="readonly"> <a href="javascript:void(0)"
                                                                             class="easyui-linkbutton l-btn l-btn-small"
                                                                             iconcls="icon-search" onclick="filter()">
            搜索 </a>
        </form>
    </div>
</div>
<table id="dg" fit="true" title="" nowrap="false" class="easyui-datagrid" url="<%=basePath%>exception/list"
       toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" singleSelect="false">
    <thead>
    <tr>
        <th field="ID" width="10" checkbox=true></th>
        <th field="OCCURDATE" width="10" sortable="true">发生时间</th>
        <th field="CLAZZ" width="10">类</th>
        <th field="METHOD" width="10">方法</th>
        <th field="LINENUMBER" width="10">行号</th>
        <th field="MSG" width="60">异常信息</th>
    </tr>
    </thead>
</table>
</body>
</html>
