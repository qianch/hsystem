<!--
作者:徐波
日期:2016-11-2 9:30:08
页面:出货订单关联JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>出货订单关联</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="deliveryPlanSalesOrders.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="增加" name="names"/>
            <jsp:param value="编辑" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="add()" name="funs"/>
            <jsp:param value="edit()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="deliveryPlanSalesOrdersSearchForm" autoSearchFunction="false">
                <label class="panel-title">搜索：</label>
                <input type="text" name="filter[**]" like="true" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="出货订单关联列表" class="easyui-datagrid"
           url="${path}planner/deliveryPlanSalesOrders/list" toolbar="#toolbar" pagination="true" rownumbers="true"
           fitColumns="true" fit="true" data-options="onDblClickRow:dbClickEdit">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="SALESORDERCODE" sortable="true" width="15">销售订单号</th>
            <th field="LADINGCODE" sortable="true" width="15">提单号</th>
            <th field="BOXNUMBER" sortable="true" width="15">箱号</th>
            <th field="SERIALNUMBER" sortable="true" width="15">封号</th>
            <th field="COUNT" sortable="true" width="15">件数</th>
            <th field="WEIGHT" sortable="true" width="15">毛重</th>
            <th field="SIZE" sortable="true" width="15">尺码</th>
        </tr>
        </thead>
    </table>
</div>
</body>