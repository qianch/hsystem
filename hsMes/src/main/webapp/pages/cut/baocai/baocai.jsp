<!--
作者:宋黎明
日期:2016-11-27 13:57:45
页面:出货计划JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>包材领料登记</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="baocai.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="cancel" name="ids"/>
            <jsp:param value="exportDetail" name="ids" />
            <jsp:param value="packing" name="ids" />

            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="icon-excel" name="icons" />
            <jsp:param value="icon-remove" name="icons"/>

            <jsp:param value="增加" name="names"/>
            <jsp:param value="编辑" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="导出" name="names"/>
            <jsp:param value="领料" name="names"/>

            <jsp:param value="add()" name="funs"/>
            <jsp:param value="edit()" name="funs"/>
            <jsp:param value="cancel()" name="funs"/>
            <jsp:param value="exportDetail()" name="funs"/>
            <jsp:param value="packing()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="cutTcBomMainSearchForm" autoSearchFunction="false">
                类别:<input type="text" name="filter[type]" like="true" class="easyui-combobox"
                          data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=baoCaiType'">

                包装材料：<input type="text" name="filter[specs]" like="true" class="easyui-textbox">
                尺寸:<input type="text" name="filter[size]" like="true" class="easyui-textbox">

                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">
                    搜索
                </a>
            </form>
        </div>



</div>

<table id="dg" singleSelect="true" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true"
       rownumbers="true" fitColumns="true" fit="true" data-options="showFooter:true">
    <thead>
    <tr>
        <th field="ID" checkbox=true ></th>
        <th field="BCKID" sortable="true" width="2">包材ID</th>
        <th field="TYPE" sortable="true" width="5">类别</th>
        <th field="SPECS" sortable="true" width="5">包装材料</th>
        <th field="SIZE" sortable="true" width="5">尺寸</th>
        <th field="STOCK" sortable="true" width="5">库存量</th>
        <th field="WARNING" sortable="true" width="5">预警值</th>
        <th field="REMARKS" sortable="true" width="10">备注</th>
    </tr>
    </thead>
</table>
</div>
</body>
