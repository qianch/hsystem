<!--
作者:谢辉
日期:2017-06-08 16:44:20
页面:销售出库明细表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>销售出库明细表</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="outSalesOrder.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="OutSaloderFrom" autoSearchFunction="false">
                订单号:<input type="text" name="filter[salesOrdercode]" like="true" class="easyui-textbox"
                              data-options="onChange:filter">
                部件名称:<input type="text" name="filter[tcprocBomversionpartsName]" like="true" class="easyui-textbox">
                客户产品名称:<input type="text" name="filter[productConsumerName]" like="true" class="easyui-textbox">
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-print"
                   onclick="export1()">导出
                </a>
                <br>
                批次号:<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox"
                              data-options="onChange:filter">
                厂内名称:<input type="text" name="filter[productFactoryName]" like="true" class="easyui-textbox"
                                data-options="onSelect:filter">
                &nbsp;
                规 格 型 号 : <input type="text" name="filter[productModel]" like="true" class="easyui-textbox"
                                     data-options="onSelect:filter"><br>
                发货人:<input type="text" name="filter[operateuserName]" like="true" class="easyui-textbox"
                              data-options="onChange:filter">
                出库时间:<input type="text" style="width:173px;" class="easyui-datebox" id="start" name="filter[start]"
                                data-options="icons:[]">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                至　&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="text" style="width:173px;" class="easyui-datebox" id="start" name="filter[end]"
                       data-options="icons:[]">
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="javascript:void(0)"
                   class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()"> 搜索 </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid"
           toolbar="#toolbar"
           pagination="true" rownumbers="true" fitColumns="false" fit="true" remoteSort="true"
           data-options="rowStyler:rowStyler">
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="DEVICECODE" sortable="true" width="130">出库单编号</th>
            <th field="OUTTIME" sortable="true" width="120" formatter="dataFormatter">出库日期</th>
            <th field="CONSUMERNAME" sortable="true" width="100">购货单位</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="SALESORDERCODE" sortable="true" width="100">订单号</th>
            <th field="BATCHCODE" sortable="true" width="130">批次号</th>
            <th field="TCPROCBOMVERSIONPARTSNAME" sortable="true" width="130">部件名称</th>
            <th field="PRODUCTCONSUMERNAME" sortable="true" width="130">客户产品名称</th>
            <th field="PRODUCTFACTORYNAME" sortable="true" width="130">厂内名称</th>
            <th field="PRODUCTMODEL" sortable="true" width="210">规格型号</th>
            <th field="COUNT" sortable="true" width="70">辅助数量</th>
            <th field="UNIT" width="70" formatter="unitFormatter">辅助单位</th>
            <th field="ROLLWEIGHT" sortable="true" width="140" formatter="weightFormatter">实发数量(kg)</th>
            <th field="OPERATEUSERNAME" width="140">发货人</th>
            <th field="SALESNAME" width="90">业务员</th>
        </tr>
        </thead>
    </table>
</div>
</body>