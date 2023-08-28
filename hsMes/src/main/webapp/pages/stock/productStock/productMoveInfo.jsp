<!--
作者:宋黎明
日期:2016-11-14 15:08:20
页面:成品库存表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>移库查询</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="productMoveInfo.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="productMoveInfoForm" autoSearchFunction="false">
                条码号 ：<input type="text" name="filter[barCode]" like="true" class="easyui-textbox">
                起始仓库：<input type="text" class="easyui-combobox" name="filter[originWarehouseCode]"
                                data-options="valueField:'originWarehouseCode',textField:'originWarehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=cp'">
                终点仓库：<input type="text" class="easyui-combobox" name="filter[newWarehouseCode]"
                                data-options="valueField:'warehouseCode',textField:'warehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=cp'">
                移库时间：<input type="text" id="start" name="filter[start]" class="easyui-datetimebox">
                &nbsp;至：<input type="text" id="end" name="filter[end]" class="easyui-datetimebox">
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true"
           rownumbers="true" fitColumns="true" fit="true" data-options="onLoadSuccess:onLoadSuccess">
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="BARCODE" sortable="true" width="10%">条码号</th>
            <th field="MOVEUSER" sortable="true" width="10%">操作人</th>
            <th field="ORIGINWAREHOUSENAME" sortable="true" width="10%">起始仓库名称</th>
            <th field="ORIGINWAREHOUSECODE" sortable="true" width="10%">起始仓库编码</th>
            <th field="ORIGINWAREHOUSEPOSCODE" sortable="true" width="10%">起点库位</th>
            <th field="NEWWAREHOUSENAME" sortable="true" width="10%">终点仓库名称</th>
            <th field="NEWWAREHOUSECODE" sortable="true" width="10%">终点仓库编码</th>
            <th field="NEWWAREHOUSEPOSCODE" sortable="true" width="10%">终点库位</th>
            <th field="MOVETIME" sortable="true" width="10%">移库时间</th>
        </tr>
        </thead>
    </table>
</div>
</body>