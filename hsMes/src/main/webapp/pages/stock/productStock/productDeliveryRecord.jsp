<!--
作者:宋黎明
日期:2016-11-27 13:57:45
页面:出货计划JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>出货计划</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="productDeliveryRecord.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
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
            <form action="#" id="productDeliveryRecordSearchForm" autoSearchFunction="false">
                出库单编号:<input type="text" name="filter[deliveryCode]" like="true" class="easyui-textbox">
                客户名称:<input type="text" name="filter[deliveryTargetCompany]" like="true" class="easyui-textbox">
                订单号：<input type="text" name="filter[salesCode]" like="true" class="easyui-textbox">
                批 次 号：<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox"><br>
                厂内名称:<input type="text" name="filter[productFactoryName]" like="true" class="easyui-textbox">
                客户产品名称:<input type="text" name="filter[productConsumerName]" like="true" class="easyui-textbox">
                客户：<input type="text" name="filter[consumer]" like="true" class="easyui-textbox"><br>
                出货时间:<input type="text" name="filter[start]" class="easyui-datetimebox">
                至:<input type="text" name="filter[end]" class="easyui-datetimebox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索
                </a>
            </form>
        </div>
        <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-zip" onclick="export1()">
            导出跟车单
        </a>
        <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-zip" onclick="export3()">
            导出跟车单汇总
        </a>
        <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-rar" onclick="export2()">
            导出出库明细
        </a>
        <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-rar"
           onclick="exportMyfd()">
            导出明阳风电出库码单
        </a>
        <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-rar"
           onclick="exportExcelnew()">导出出库单竖排</a>
        <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-rar"
           onclick="exportExcelnew1()">导出出库单横排</a>
        <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-rar"
           onclick="exportExcelnew2()">导出出库单横排2</a>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true"
           rownumbers="true" fitColumns="true" fit="true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="CONSUMERNAME" sortable="true" width="130">客户名称</th>
            <th field="DELIVERYCODE" sortable="true" width="80">出货单编号</th>
            <th field="OUTTIME" sortable="true" width="100">出货时间</th>
            <th field="USERNAME" sortable="true" width="80">出货人</th>
            <th field="PN" width="80">装车序号</th>
            <th field="LOGISTICSCOMPANY" sortable="true" width="100">物流公司</th>
            <th field="LADINGCODE" sortable="true" width="100">提单号</th>
            <th field="PLATE" sortable="true" width="100">车牌号</th>
            <th field="REALBOXNUMBER" sortable="true" width="100">箱号</th>
            <th field="SERIALNUMBER" sortable="true" width="100">封号</th>
            <th field="COUNT" sortable="true" width="100">件数</th>
            <th field="SAMPLEINFORMATION" sortable="true" width="100">样布信息</th>
        </tr>
        </thead>
    </table>
</div>
</body>