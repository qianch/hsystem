<!--
作者:高飞
日期:2017-8-9 16:30:35
页面:条码扫描错误记录JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>条码扫描错误记录</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="suitErrorLog.js.jsp" %>
    <style type="text/css">
        #fragmentBarcodeSearchForm .textbox {
            width: 100px !important;
        }
    </style>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div id="p" class="easyui-panel" title="查询"
             style="width:100%;height:210px; padding:5px;background:rgb(250, 250, 250);"
             data-options="iconCls:'icon-search',collapsible:true,onExpand:resizeDg,onCollapse:resizeDg">
            <form action="#" id="suitErrorLogSearchForm" autoSearch="true" autoSearchFunction="false">
                　　　条码号:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTBARCODE]">
                　　打印类型:<input type="text" class="easyui-combobox" name="filter[FRAGMENTPRINTTYPE]"
                                  data-options="valueField:'v',textField:'t',data:[{'v':'',t:'全部'},{'v':'常规',t:'常规'},{'v':'补打',t:'补打'},{'v':'重打',t:'重打'}],onChange:filter">
                　　任务单号:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTCTCODE]"><br>
                　　　订单号:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTORDERCODE]">
                　　　批次号:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTBATCHCODE]">
                　　派工单号:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTCTOCODE]"><br>
                　　客户简称:<input type="text" class="easyui-textbox" like="true"
                                  name="filter[FRAGMENTCONSUMERSIMPLENAME]">
                　　客户大类:<input type="text" class="easyui-combobox" name="filter[CONSUMERCATEGORY]"
                                  data-options="valueField:'v',textField:'t',data:[{'v':'',t:'全部'},{'v':'国内',t:'国内'},{'v':'国外',t:'国外'}],onChange:filter">
                　　部件名称:<input type="text" class="easyui-textbox" like="true" name="filter[PARTNAME]"><br>
                　小部件名称:<input type="text" class="easyui-textbox" like="true" name="filter[FRAGMENTNAME]">
                组套任务单号:<input type="text" class="easyui-textbox" like="true" name="filter[SUITCTCODE]">
                　组套订单号:<input type="text" class="easyui-textbox" like="true" name="filter[SUITORDERCODE]"><br>
                　组套批次号:<input type="text" class="easyui-textbox" like="true" name="filter[SUITBATCHCODE]">
                组套派工单号:<input type="text" class="easyui-textbox" like="true" name="filter[SUITCTOCODE]">
                　　错误信息:<input type="text" class="easyui-textbox" like="true" name="filter[ERRORMSG]"><br>
                　　　操作人:<input type="text" class="easyui-textbox" like="true" name="filter[SCANUSER]">
                　　部件条码:<input type="text" class="easyui-textbox" like="true" name="filter[PARTBARCODE]"><br>
                开始扫描时间:<input type="text" id="SCANTIME_S" class="easyui-datebox" name="filter[SCANTIME_S]"
                                    data-options="onSelect:filter">
                结束扫描时间:<input type="text" id="SCANTIME_E" class="easyui-datebox" name="filter[SCANTIME_E]"
                                    data-options="onSelect:filter">
                　　　　<a class="easyui-linkbutton" iconcls="icon-excel" onclick="exportSuitErrorLog()"> 导出 </a>
                　　　　<a class="easyui-linkbutton" iconcls="icon-search" onclick="filter()"> 搜索 </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" class="easyui-datagrid" url="${path}siemens/barcode/suitErrorLog/list"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="false" fit="true">
        <thead data-options="frozen:true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="FRAGMENTBARCODE" sortable="true" width="120">条码号</th>
            <th field="ERRORMSG" sortable="true" width="140">错误信息</th>
            <th field="SCANTIME" sortable="true" width="125">扫描时间</th>
            <th field="SCANUSER" sortable="true" width="60">操作人</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="FRAGMENTPRINTTYPE" sortable="true" width="80">打印类型</th>
            <th field="FRAGMENTORDERCODE" sortable="true" width="120">订单号</th>
            <th field="FRAGMENTBATCHCODE" sortable="true" width="120">批次号</th>
            <th field="FRAGMENTCTCODE" sortable="true" width="120">任务单号</th>
            <th field="FRAGMENTCTOCODE" sortable="true" width="120">派工单号</th>
            <th field="SUITCTCODE" sortable="true" width="120">组套任务单号</th>
            <th field="SUITORDERCODE" sortable="true" width="120">组套订单号</th>
            <th field="SUITBATCHCODE" sortable="true" width="120">组套批次号</th>
            <th field="SUITCTOCODE" sortable="true" width="120">组套派工单号</th>
            <th field="FRAGMENTCONSUMERSIMPLENAME" sortable="true" width="120">客户简称</th>
            <th field="FRAGMENTCONSUMERCATEGORY" sortable="true" width="80">客户大类</th>
            <th field="PARTNAME" sortable="true" width="120">部件名称</th>
            <th field="FRAGMENTNAME" sortable="true" width="120">小部件名称</th>
            <th field="PARTBARCODE" sortable="true" width="120">部件条码</th>
        </tr>
        </thead>
    </table>
</div>
</body>