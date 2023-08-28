<!--
作者:周志祥
日期:2017-6-9 15:08:19
页面:库龄明细表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>库龄明细表</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="warehouseDetail.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="warehousedetailSearchForm" autoSearchFunction="false">
                订单号:<input type="text" name="filter[salesordercode]" like="true" class="easyui-textbox">
                批次号:<input type="text" name="filter[batchcode]" like="true" class="easyui-textbox">
                厂内名称:<input type="text" name="filter[factoryProductName]" like="true" class="easyui-textbox">
                客户名称:<input type="text" name="filter[consumername]" like="true" class="easyui-textbox">
                时间:<input type="text" id="start" name="filter[inTime]" class="easyui-datetimebox">
                库龄:<input type="text" name="filter[days]" class="easyui-combobox" text="全部" data-options="valueField: 'id',
       				 textField: 'text',data:[{'id':'','text':'全部'},{'id':'30','text':'1个月以内'},{'id':'90','text':'1-3个月'},{'id':'180','text':'3-6个月'},{'id':'360','text':'6-12个月'},{'id':'361','text':'12个月以上'}],onSelect:filter">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="exportDetail()">导出
                </a>
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true"
           rownumbers="true" fitColumns="true" fit="true" data-options="rowStyler:rowStyler">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="SALESORDERCODE" sortable="true" width="10">订单号</th>
            <th field="BATCHCODE" sortable="true" width="10">批次号</th>
            <th field="FACTORYPRODUCTNAME" sortable="true" width="20">厂内名称</th>
            <th field="CONSUMERPRODUCTNAME" sortable="true" width="15">客户产品名称</th>
            <th field="PRODUCTMODEL" sortable="true" width="20">规格型号</th>
            <th field="SUMWEIGHT" sortable="true" width="10" formatter="warehouseCount">期末结存数量(kg)</th>
            <th field="CONSUMERNAME" sortable="true" width="20">客户名称</th>
            <th field="INTIME" sortable="true" width="15">进库日期</th>
            <th field="DAYS" sortable="true" width="10" formatter="inDays">库龄</th>
            <th field="LOGINNAME" sortable="true" width="20">责任人</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>