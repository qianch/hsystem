<!--
作者:肖文彬
日期:2016-11-8 15:24:59
页面:盘库结果表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>盘库结果表</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="stockCheckResult.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="stockCheckResultSearchForm" autoSearchFunction="false">
                <label class="panel-title">搜索：</label>
                物料代码：<input type="text" id="wCode" name="filter[wCode]" like="true" class="easyui-textbox">
                卷代码：<input type="text" id="jCode" name="filter[jCode]" like="true" class="easyui-textbox">
                托代码：<input type="text" id="tCode" name="filter[tCode]" like="true" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="盘库结果表列表" class="easyui-datagrid"
           url="${path}stock/stockCheckResult/list" toolbar="#toolbar" pagination="true" rownumbers="true"
           fitColumns="true" fit="true" data-options="onDblClickRow:dbClickEdit">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="BARCODE" sortable="true" width="15">物料条码</th>
            <th field="ROLLCODE" sortable="true" width="15">卷条码</th>
            <th field="TRAYCODE" sortable="true" width="15">托条码</th>
            <th field="CHECKRESULT" sortable="true" width="15" formatter="formatterCheck">盘点状态</th>
        </tr>
        </thead>
    </table>
</div>
</body>