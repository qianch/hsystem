<!--
作者:孙利
日期:2017-7-10 8:44:34
页面:称重载具JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>称重载具</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="weightCarrier.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <jsp:include page="../base/toolbar.jsp">
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
            <form action="#" id="weightCarrierSearchForm" autoSearchFunction="false">
                &nbsp;&nbsp;&nbsp;&nbsp;<label class="panel-title">载具编号：</label>
                <input type="text" name="filter[carrierCode]" like="true" class="easyui-textbox">&nbsp;&nbsp;&nbsp;&nbsp;
                <label class="panel-title">载具名称：</label>
                <input type="text" name="filter[carrierName]" like="true" class="easyui-textbox">&nbsp;&nbsp;&nbsp;&nbsp;
                <label class="panel-title">车间：</label>
                <input type="text" id="workShopCode" class="easyui-combobox" name="filter[workShopCode]"
                       data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=company,cut,weave',onSelect:filter"
                       panelHeight="200">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">
                    搜索
                </a>
            </form>
        </div>
    </div>
    <table id="weight_dg" singleSelect="false" title="" class="easyui-datagrid" url="${path}weightCarrier/list"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"
           data-options="onDblClickRow:dbClickEdit">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="CARRIERCODE" sortable="true" width="15">载具编号</th>
            <th field="CARRIERNAME" sortable="true" width="15">载具名称</th>
            <th field="CARRIERWEIGHT" sortable="true" width="15">重量(kg)</th>
            <th field="WORKSHOPNAME" sortable="true" width="15">所属车间</th>
            <th field="CREATER" sortable="true" width="15">创建人</th>
            <th field="CREATETIME" sortable="true" width="15">创建时间</th>
            <th field="MODIFYUSER" sortable="true" width="15">修改人</th>
            <th field="MODIFYTIME" sortable="true" width="15">修改时间</th>
        </tr>
        </thead>
    </table>
</div>
</body>
