<!--
作者:徐波
日期:2016-11-30 14:03:19
页面:生产追溯日志JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>冻结记录</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="freezeLog.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="tracingLogSearchForm" autoSearchFunction="false">
                　　条码:<input type="text" name="filter[barCode]" like="true" class="easyui-textbox">
                批次号:<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox">
                订单号:<input type="text" name="filter[salesCode]" like="true" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()"> 搜索
                </a><br>
                工艺名称:<input type="text" name="filter[model]" like="true" class="easyui-textbox">
                　状态:<input type="text" name="filter[logType]" in="true" class="easyui-combobox"
                             data-options=" valueField: 'id',textField: 'text',data:[{'id':'9','text':'冻结'},{'id':'10','text':'解冻'}],onSelect:filter">
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" url="${path}tracingLog/freezeLog/list"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"
           data-options="
                singleSelect:true,
                collapsible:true,
                view:groupview,
                groupField:'DATETIME',
                groupFormatter:function(value,rows){
                    return value+ ' (' + rows.length + '条)';
                }">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <!-- <th field="DATETIME" sortable="true" width="15" >操作时间</th> -->
            <!-- <th field="ROLLBARCODE" sortable="true" width="130">卷条码</th>
            <th field="PARTBARCODE" sortable="true" width="130">部件条码</th>
            <th field="BOXBARCODE" sortable="true" width="130">箱条码</th>
            <th field="TRAYBARCODE" sortable="true" width="130">托条码</th> -->
            <th field="BARCODE" sortable="true" width="130">条码号</th>
            <th field="PRODUCTMODEL" sortable="true" width="135">产品规格</th>
            <th field="BATCHCODE" sortable="true" width="80">批次号</th>
            <th field="SALESCODE" sortable="true" width="155">订单号</th>
            <th field="DEVICECODE" sortable="true" width="50">机台</th>
            <th field="CODE" sortable="true" width="100" formatter="formatterCode">投诉编号</th>
            <th field="LOGTYPE" sortable="true" width="60" formatter="formatterStatus">状态</th>
            <th field="CAUSE" sortable="true" width="100" formatter="formatterCause">操作原因</th>
            <th field="USERNAME" sortable="true" width="60">操作人员</th>
        </tr>
        </thead>
    </table>
</div>
</body>