<!--
作者:肖文彬
日期:2016-10-18 13:38:47
页面:完成的订单详情
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>完成的订单详情</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="order.js.jsp" %>
</head>
<script type="javascript">
</script>
<body>
<div style="margin:20px 0;"></div>
<table width="100%" class="easyui-datagrid" id="showProduce" url="${path}planner/produce/findfinished?Ids=${Ids}"
       data-options="singleSelect:true,collapsible:true,method:'POST'">
    <thead>
    <tr>
        <th field="SALESORDERSUBCODE" sortable="true" width="180">订单号</th>
        <th field="PRODUCTCOUNT" width="80" data-options="formatter:countFormatter,styler:processStyler">订单数量</th>
        <th field="PRODUCECOUNT" width="80" data-options="formatter:processFormatter,styler:processStyler">生产进度</th>
    </tr>
    </thead>
</table>
</body>