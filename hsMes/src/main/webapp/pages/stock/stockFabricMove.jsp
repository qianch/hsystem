<!--
作者:徐波
日期:2017-2-11 8:53:07
页面:胚布移库表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>胚布领料</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="stockFabricMove.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="stockFabricMoveSearchForm" autoSearchFunction="false">
                <!-- <label class="panel-title">搜索：</label> -->
                <!--TODO 按日期，车间，条码搜索  -->
                卷 条 码：<input type="text" name="filter[barcode]" like="true" class="easyui-textbox">
                领料日期：<input type="text" id="start" name="filter[moveTimeb]" class="easyui-datetimebox">
                &nbsp;至：<input type="text" name="filter[moveTimea]" class="easyui-datetimebox"></br>
                来自车间:
                <input type="easyui-combobox" name="filter[originWarehouseCode]" value="" class="easyui-combobox"
                       data-options=" valueField: 'id',
        textField: 'text' ,data:[{'id':'','text':'全部'},{'id':'bz1pbk','text':'编织一胚布库'},{'id':'bz2pbk','text':'编织二胚布库'},{'id':'bz3pbk','text':'编织三胚布库'}],onSelect:filter">
                领 料 人：<input type="text" name="filter[userName]" like="true" class="easyui-textbox">
                产品规格：<input type="text" name="filter[productModel]" like="true" class="easyui-textbox">&nbsp;&nbsp;
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索
                </a>&nbsp;&nbsp;
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="platform-icon9"
                   onclick="exportExcel()">导出
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true"
           rownumbers="true" fitColumns="true" fit="true"
           data-options="onDblClickRow:dbClickEdit,onLoadSuccess:onLoadSuccess,showFooter:true">
        <!-- 		data-options="onDblClickRow:dbClickEdit,onLoadSuccess:onLoadSuccess,showFooter:true"> -->
        <thead frozen="true">
        <th field="ID" checkbox=true></th>
        <th field="SALESORDERSUBCODE" sortable="true" width="10%">订单号</th>
        <th field="BARCODE" sortable="true" width="10%">条码号</th>
        <th field="BATCHCODE" sortable="true" width="10%">批次号</th>
        <th field="FACTORYPRODUCTNAME" sortable="true" width="10%">厂内名称</th>
        </thead>
        <thead>
        <tr>
            <th field="PRODUCTMODEL" sortable="true" width="10%">产品规格</th>
            <th field="PRODUCTWIDTH" sortable="true" width="10%">门幅(mm)</th>
            <th field="ORIGINWAREHOUSECODE" sortable="true" width="10%" formatter="warhouseName">原库房</th>
            <!-- <th field="ORIGINWAREHOUSEPOSCODE" sortable="true" width="15">原库位</th> -->
            <th field="NEWWAREHOUSECODE" sortable="true" width="10%" formatter="warhouseName">新库房</th>
            <th field="ROLLWEIGHT" sortable="true" width="10%">重量(Kg)</th>
            <th field="PRODUCTROLLLENGTH" sortable="true" width="10%" formatter="warhouseName">定长</th>
            <!-- <th field="NEWWAREHOUSEPOSCODE" sortable="true" width="15">新库位</th> -->
            <!-- 					<th field="ROLLQUALITYGRADECODE" sortable="true" width="10%">质量等级</th> -->
            <th field="MOVETIME" sortable="true" width="10%">移库时间</th>
            <th field="MOVEUSERID" sortable="true" width="10%">操作人</th>
        </tr>
        </thead>
    </table>
</div>
</body>