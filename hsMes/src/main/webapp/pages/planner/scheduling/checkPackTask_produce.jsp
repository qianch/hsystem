<!--
作者:sunli
日期:2018-03-08 10:46:24
页面:查看非套材包装任务JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>查看非套材包装任务</title>
    <%@ include file="../../base/meta.jsp" %>
    <style type="text/css">
        td {
            min-width: 30px;
            padding: 3px;
        }

        .title {
            padding-right: 5px;
        }
    </style>
    <script type="text/javascript">
    </script>
</head>
<body class="easyui-layout">
<div>
    <table id="dg" singleSelect="true" url="${path}device/scheduling/packTaskList?ppdId=${ppdId}" title=""
           class="easyui-datagrid" pagination="false" rownumbers="true" fitColumns="true" fit="false">
        <thead>
        <tr>
            <th field="VID" checkbox="true"></th>
            <th field="CODE" width="280">包装代码</th>
            <th field="VERSION" width="50">版本</th>
            <th field="PRODUCETOTALCOUNT" width="80">计划总托数</th>
            <th field="MEMO" width="80" editor="{type:'textbox',options:{validType:'length[0,100]'}}">备注</th>
        </tr>
        </thead>
    </table>
</div>
</body>