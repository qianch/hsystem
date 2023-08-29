<!--
作者:肖文彬
日期:2016-11-16 11:25:41
页面:原料入库记录表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>原料入库记录表</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="materialInRecord.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="materialInRecordSearchForm" autoSearchFunction="false">
                托盘编号:<input type="text" name="filter[code]" like="true" class="easyui-textbox">
                产品规格:<input type="text" name="filter[materialModel]" like="true" class="easyui-textbox">
                仓&nbsp;&nbsp;库:<input type="text" class="easyui-combobox" name="filter[warehouseCode]" like="true"
                                        data-options="valueField:'warehouseCode',textField:'warehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=ycl',onSelect:filter">
                库位:<input type="text" name="filter[warehousePosCode]" like="true" class="easyui-textbox"></br>
                生产日期:<input type="text" id="produceStart" name="filter[produceStart]" value=""
                                class="easyui-datetimebox">
                至:&nbsp;<input type="text" id="produceEnd" name="filter[produceEnd]" value=""
                                class="easyui-datetimebox">
                入库时间:<input type="text" id="inStart" name="filter[inStart]" value="" class="easyui-datetimebox">
                至:&nbsp;<input type="text" id="inEnd" name="filter[inEnd]" value="" class="easyui-datetimebox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel"
                   onclick="exportInRecordDetail()">导出
                </a></br>
                产品大类:<input type="text" name="filter[produceCategory]" like="true" class="easyui-textbox">&nbsp;&nbsp;&nbsp;
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()"> 搜索 </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title=""
           class="easyui-datagrid" url="${path}stock/materialInRecord/list"
           toolbar="#toolbar" pagination="true" rownumbers="true"
           fitColumns="false" fit="true">
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="MATERIALCODE" sortable="true" width="150">托盘编号</th>
            <th field="PRODUCECATEGORY" sortable="true" width="110">产品大类</th>
            <th field="MATERIALMODEL" sortable="true" width="150">产品规格</th>
            <th field="INWEIGHT" sortable="true" width="90" formatter="processNumberFormatter">重量(kg)</th>
            <th field="SUBWAY" sortable="true" width="110">接头方式</th>
            <th field="NUMBERDEVIATION" sortable="true" width="110">号数偏差</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="WAREHOUSEPOSCODE" sortable="true" width="90">库位</th>
            <th field="WAREHOUSENAME" sortable="true" width="90">仓库</th>
            <th field="PRODUCEDATE" sortable="true" width="150">生产日期</th>
            <th field="INTIME" sortable="true" width="150">入库时间</th>
            <th field="NAME" sortable="true" width="90">操作人</th>
        </tr>
        </thead>
    </table>
</div>
</body>