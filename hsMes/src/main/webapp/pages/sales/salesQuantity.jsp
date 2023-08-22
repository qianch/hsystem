<!--
作者:谢辉
日期:2017-06-08 16:44:20
页面:销售下单数量统计表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>销售下单数量统计表</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="salesQuantity.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="salesQuantityFrom" autoSearchFunction="false">
                客户产品名称:<input type="text" name="filter[consumerProductName]" like="true" class="easyui-textbox"
                                    data-options="onSelect:filter">
                厂内名称:<input type="text" name="filter[factoryProductName]" like="true" class="easyui-textbox"
                                data-options="onSelect:filter">
                客户名称:<input type="text" name="filter[consumerName]" like="true" class="easyui-textbox"
                                data-options="onSelect:filter">
                &nbsp;&nbsp;&nbsp;&nbsp;
                </br>
                下&nbsp;单&nbsp;时&nbsp;间&nbsp;:
                &nbsp;
                <input type="text" style="width:173px;" class="easyui-datebox" id="start" name="filter[start]"
                       data-options="icons:[]">
                &nbsp;&nbsp;&nbsp;&nbsp;至 &nbsp;&nbsp;&nbsp;&nbsp;
                <input type="text" style="width:173px;" class="easyui-datebox" id="end" name="filter[end]"
                       data-options="icons:[]">
                出货时间 :<input type="text" style="width:173px;" class="easyui-datebox" id="start2"
                                 name="filter[start2]" data-options="icons:[]">
                &nbsp;&nbsp;至 &nbsp;&nbsp;
                <input type="text" style="width:173px;" class="easyui-datebox" id="end2" name="filter[end2]"
                       data-options="icons:[]">
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="javascript:void(0)"
                   class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()"> 搜索 </a>&nbsp;&nbsp;
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-print"
                   onclick="export1()">导出
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid"
           toolbar="#toolbar"
           pagination="true" rownumbers="true" fitColumns="true" fit="true" remoteSort="true"
           data-options="rowStyler:rowStyler,showFooter:true">
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="CONSUMERNAME" sortable="true" width="150">客户名称</th>
            <th field="PRODUCTISTC" sortable="true" width="150" formatter="formatterIsTc">产品类型</th>
            <th field="FACTORYPRODUCTNAME" sortable="true" width="150">厂内名称</th>
            <th field="CONSUMERPRODUCTNAME" sortable="true" width="150">客户产品名称</th>
            <th field="TWEIGHT" sortable="true" width="120" formatter="weightFormatter">下单总量(kg)</th>
            <th field="PRODUCTIONWEIGHT" sortable="true" width="120" formatter="PRFormatter">已生产总量(kg)</th>
            <th field="PRWEIGHT" sortable="true" width="120" formatter="prWeightFormatter">未生产总量(kg)</th>
            <th field="PRWWEIGHT" sortable="true" width="120" formatter="prwWeightFormatter">出库总量(kg)</th>
        </tr>
        </thead>
    </table>
</div>
</body>