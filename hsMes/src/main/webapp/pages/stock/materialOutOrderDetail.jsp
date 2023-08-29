<!--
作者:肖文彬
日期:2016-11-16 12:33:40
页面:出库单明细JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>出库单明细</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="materialOutOrderDetail.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="materialOutOrderDetailSearchForm" autoSearchFunction="false">
                托盘编号:<input type="text" name="filter[code]" like="true" class="easyui-textbox">
                仓库:<input type="text" class="easyui-combobox" name="filter[warehouseCode]" like="true"
                            data-options="valueField:'warehouseCode',textField:'warehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=ycl',onSelect:filter">
                库&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位:<input type="text" name="filter[warehousePosCode]" like="true"
                                                                class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel"
                   onclick="exportOutRecordDetail()">导出
                </a><br>
                生产日期:<input type="text" id="produceStart" name="filter[produceStart]"
                                value="" class="easyui-datetimebox">
                至:&nbsp;&nbsp;&nbsp;<input type="text" id="produceEnd" name="filter[produceEnd]"
                                            value="" class="easyui-datetimebox">
                出库时间:<input type="text" id="outStart" name="filter[outStart]"
                                value="" class="easyui-datetimebox">
                至:&nbsp;<input type="text" id="outEnd" name="filter[outEnd]"
                                value="" class="easyui-datetimebox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" url="${path}stock/materialOutOrderDetail/list"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="false" fit="true">
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="OUTORDERCODE" sortable="true" width="110">出库单号</th>
            <th field="MATERIALCODE" sortable="true" width="150">托盘编号</th>
            <th field="WAREHOUSENAME" sortable="true" width="80">仓库</th>
            <th field="WAREHOUSEPOSCODE" sortable="true" width="80">库位</th>
            <th field="PRODUCECATEGORY" sortable="true" width="110">产品大类</th>
            <th field="NUMBERDEVIATION" sortable="true" width="120">号数偏差</th>
            <!-- <th field="DEVIATION" sortable="true" width="80">上偏差</th>
            <th field="LOWERDEVIATION" sortable="true" width="80">下偏差</th> -->
            <th field="SUBWAY" sortable="true" width="100">接头方式</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="MATERIALMODEL" sortable="true" width="150">规格型号</th>
            <th field="OUTWEIGHT" sortable="true" width="80" formatter="processNumberFormatter">重量(kg)</th>
            <th field="OUTTIME" sortable="true" width="140">出库时间</th>
            <th field="PRODUCEDATE" sortable="true" width="140">生产日期</th>
            <th field="NAME" sortable="true" width="90">操作人</th>
        </tr>
        </thead>
    </table>
</div>
</body>