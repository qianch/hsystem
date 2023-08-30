<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>托盒卷对应关系</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="trayBoxRollRelationSummary.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="trayBoxRollRelationSearchForm" autoSearchFunction="false">
                托条码:<input type="text" name="filter[trayBarcode]" like="true" class="easyui-textbox">
                　盒条码:<input type="text" name="filter[boxBarcode]" like="true" class="easyui-textbox">
                卷条码:<input type="text" name="filter[rollBarcode]" like="true" class="easyui-textbox">
                客户名称:<input type="text" name="filter[consumerName]" like="true" class="easyui-textbox">
                &nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small"
                               iconcls="icon-print" onclick="export1()">导出</a>
                <br/>
                订单号:<input type="text" name="filter[salesOrderCode]" like="true" class="easyui-textbox">
                产品规格:<input type="text" name="filter[productModel]" like="true" class="easyui-textbox">
                机台号:<input type="text" name="filter[rollDeviceCode]" class="easyui-textbox">
                车间:<input type="text" name="filter[workshop]" class="easyui-combobox"
                            data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=weave,cut'">
                <br/>
                批次号:<input type="text" name="filter[batchcode]" like="true" class="easyui-textbox">
                　门幅:<input type="text" name="filter[productWidth]" class="easyui-numberbox" precision="2">
                　卷长:<input type="text" name="filter[productRollLength]" class="easyui-numberbox" precision="2">
                生产时间:<input type="text" id="start" name="filter[start]" class="easyui-datetimebox">
                至: <input type="text" id="end" name="filter[end]" class="easyui-datetimebox">
                &nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small"
                               iconcls="icon-search" onclick="filter()">搜索</a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="true" title="托盒卷对应关系" class="easyui-datagrid" toolbar="#toolbar"
           pagination="true" rownumbers="true" fitColumns="false" fit="true" data-options="showFooter:true">
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="ROLLBARCODE" sortable="true" width="150">卷条码</th>
            <th field="BOXBARCODE" sortable="true" width="150">盒条码</th>
            <th field="TRAYBARCODE" sortable="true" width="150">托条码</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="PRODUCEPLANCODE" sortable="true" width="150">计划单号</th>
            <th field="SALESORDERCODE" sortable="true" width="150">订单号</th>
            <th field="CONSUMERNAME" sortable="true" width="150">客户名称</th>
            <th field="PRODUCTMODEL" sortable="true" width="130">产品规格</th>
            <th field="BATCHCODE" sortable="true" width="80">批次号</th>
            <th field="ROLLQUALITYGRADECODE" width="60">质量等级</th>
            <th field="ROLLDEVICECODE" sortable="true" width="80">机台号</th>
            <th field="NAME" sortable="true" width="80">车间</th>
            <th field="ROLLWEIGHT" sortable="true" width="120">卷重(kg)</th>
            <th field="PRODUCTWIDTH" sortable="true" width="70">门幅(mm)</th>
            <th field="PRODUCTWEIGHT" sortable="true" width="70">实际重量（kg）</th>
            <th field="PRODUCTLENGTH" sortable="true" width="70">卷长(m)</th>
            <th field="ROLLOUTPUTTIME" sortable="true" width="140">生产时间</th>
        </tr>
        </thead>
    </table>
</div>
</body>