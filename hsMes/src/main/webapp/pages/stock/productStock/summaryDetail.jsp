<!--
作者:周志祥
日期:2017-6-9 15:08:19
页面:库龄明细表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>库龄汇总表</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="summaryDetail.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="summaryDetailSearchForm" autoSearchFunction="false">
                上期时间:<input type="text" id="start" name="filter[TIME1]" class="easyui-datetimebox">
                本期时间:<input type="text" id="end" name="filter[TIME2]" class="easyui-datetimebox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索
                </a>
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel"
                   onclick="exportDetail()">导出
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" fit="true"
           toolbar="#toolbar"
           pagination="true" rownumbers="true" fitColumns="true"
           data-options="rowStyler:rowStyler">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="DAYS" sortable="true" width="20">库龄时间段</th>
            <th field="OLDWEIGHT" sortable="true" width="20">上期结存</th>
            <th field="NOWWEIGHT" sortable="true" width="20">本期结存</th>
            <th field="DIFFERENCE" sortable="true" width="20">增降数量(-为减少)</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>