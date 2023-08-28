<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>库龄数量对比表</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="comparisonDetail.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="comparisonDetailSearchForm" autoSearchFunction="false">
                订单号:<input type="text" name="filter[salesordercode]" like="true" class="easyui-textbox">
                批次号:<input type="text" name="filter[batchcode]" like="true" class="easyui-textbox">
                规格型号:<input type="text" name="filter[productModel]" like="true" class="easyui-textbox">
                厂内名称:<input type="text" name="filter[factoryProductName]" like="true" class="easyui-textbox">
                客户产品名称:<input type="text" name="filter[consumerProductName]" like="true" class="easyui-textbox">
                客户名称:<input type="text" name="filter[consumername]" like="true" class="easyui-textbox">
                库龄:<input type="text" name="filter[days]" class="easyui-combobox" text="全部" data-options="valueField: 'id',
       				 textField: 'text',data:[{'id':'','text':'全部'},{'id':'180','text':'3-6个月'},{'id':'360','text':'6-12个月'},{'id':'361','text':'12个月以上'}],onSelect:filter">
                增减:<input type="text" name="filter[difference]" class="easyui-combobox" style="width: 115px"
                            text="全部" data-options="valueField:'id',
       				 textField: 'text',data:[{'id':'','text':'全部'},{'id':'1','text':'增加'},{'id':'2','text':'减少'}],onSelect:filter">
                上期库存:<input type="text" name="filter[TIME1]" id="start" class="easyui-datetimebox">
                本期库存:<input type="text" name="filter[TIME2]" id="end" class="easyui-datetimebox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">
                    搜索
                </a>
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel"
                   onclick="exportDetail()">
                    导出
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" fit="true"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true"
           data-options="rowStyler:rowStyler,onLoadSuccess:onLoadSuccess,showFooter:true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="SALESORDERCODE" sortable="true" width="15">订单号</th>
            <th field="BATCHCODE" sortable="true" width="15">批次号</th>
            <th field="PRODUCTMODEL" sortable="true" width="20">规格型号</th>
            <th field="FACTORYPRODUCTNAME" sortable="true" width="40">厂内名称</th>
            <th field="CONSUMERPRODUCTNAME" sortable="true" width="25">客户产品名称</th>
            <th field="OLDWEIGHT" sortable="true" width="20" formatter="numberChange">上期结存</th>
            <th field="NOWWEIGHT" sortable="true" width="20" formatter="numberChange">本期结存</th>
            <th field="DIFFERENCE" sortable="true" width="20">出库数量(KG)</th>
            <th field="CONSUMERNAME" sortable="true" width="40">客户名称</th>
            <th field="INTIME" sortable="true" width="25">进库日期</th>
            <th field="DAYS" sortable="true" width="15" formatter="inDays">库龄</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>