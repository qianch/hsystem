<!--
作者:周志祥
日期:2017-6-9 15:08:19
页面:生产领料汇总表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>生产领料汇总表</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="materialStockReport.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="materialStockReportSearchForm" autoSearchFunction="false">
                产品大类:<input type="text" class="easyui-textbox" name="filter[producecategory]" like="true">
                物料名称:<input type="text" class="easyui-textbox" name="filter[materialmodel]" like="true">
                领料单位:<input type="text" class="easyui-combobox" name="filter[warehouseCode]" like="true"
                                data-options="valueField:'warehouseCode',textField:'warehouseName',url:'<%=basePath%>stock/materialStockState/getWarehouseInfo',onSelect:filter">
                开始时间:<input type="text" id="start" name="filter[produceDate]" class="easyui-datetimebox">-
                结束时间:<input type="text" id="end" name="filter[produceDate1]" class="easyui-datetimebox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="export1()">导出
                </a>
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" url="${path}stock/materialStockState/list1"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"
           data-options="rowStyler:rowStyler">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="PRODUCECATEGORY" sortable="true" width="10">产品大类</th>
            <th field="MATERIALMODEL" sortable="true" width="10">物料名称</th>
            <th field="INWEIGHTS" sortable="true" width="10" formatter="processNumberFormatter">数量(常用)(kg)</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>