<!--
作者:徐波
日期:2016-12-3 16:35:51
页面:托条码JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>托条码</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="../planner/produce/producePlanDetailPrint/producePlanDetailPrint.js.jsp" %>
    <%@ include file="trayBarCode.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="trayBarCodeSearchForm" autoSearchFunction="false">
                选择打印机：
                <input id="pName" class="easyui-combobox" name="pName" required="true"
                       data-options="valueField:'value',textField:'text',url:'<%=basePath %>printer/getPrinterInfo'">
                托&nbsp;条&nbsp;码：
                <input type="text" name="filter[BARCODE]" like="true" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-print"
                   onclick="print()">补打条码</a>
                <div class="datagrid-toolbar"></div>
                <br/>
                任务单号： <input type="text" name="filter[producePlanCode]" class="easyui-textbox">
                创建时间:<input type="text" id="start" name="filter[startTime]" class="easyui-datetimebox">-<input
                    type="text" id="end" name="filter[endTime]" class="easyui-datetimebox"><br/>
                客户名称:
                <input type="text" id="consumerName" name="consumerName" value="${btwFile.consumerName}"
                       class="easyui-searchbox" required="true" data-options="searcher:selectConsumer,icons:[]">
                客户代码：<input type="text" name="filter[CONSUMERCODE]" like="true" class="easyui-textbox">
                客户简称：<input type="text" name="filter[CONSUMERSIMPLENAME]" like="true" class="easyui-textbox">
                标签模版:<input id="btwfileSelect" class="easyui-combobox" width="120px" required="true">
                打印数量:<input id="printCount" class="easyui-numberbox" value="3"
                                data-options="min:1,max:10,precision:0" required="true">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-print" onclick="doPrintTray()">补打托条码</a>
                <input type="hidden" name="filter[consumerId]" id="consumerId" value="${btwFile.consumerId}">
            </form>
        </div>
        <div style="border-top:1px solid #DDDDDD">
            客户条码号数值:
            <input type="number" id="customerBarCodeRecord" name="customerBarCodeRecord" value="0"
                   class="easyui-textbox">
            供销商条码数值:
            <input type="number" id="agentBarCodeRecord" name="agentBarCodeRecord" value="0" class="easyui-textbox">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" onclick="editPlanDetailPrints()">修改打印属性</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit"
               onclick="editBacode()">修改条码
            </a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove"
               onclick="clearTray()">清空条码
            </a>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true"
           rownumbers="true" fitColumns="true" fit="true" data-options="onLoadSuccess:dgLoadSuccess,remoteFilter:true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="BARCODE" sortable="true" width="15">托条码号</th>
            <th field="CUSTOMERBARCODE" sortable="true" width="15">客户条码号</th>
            <th field="AGENTBARCODE" sortable="true" width="15">供销商条码号</th>
            <th field="CONSUMERSIMPLENAME" sortable="true" width="15">客户简称</th>
            <th field="CONSUMERCODE" sortable="true" width="15">客户代码</th>
            <th field="SALESORDERCODE" sortable="true" width="15">订单号</th>
            <th field="BATCHCODE" sortable="true" width="15">批次号</th>
            <th field="PRINTDATE" sortable="true" width="15">打印时间</th>
        </tr>
        </thead>
    </table>
</div>
</body>
