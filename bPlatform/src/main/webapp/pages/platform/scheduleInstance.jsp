<!--
作者:高飞
日期:2016-8-19 9:34:14
页面:调度实例JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>调度实例</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="scheduleInstance.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <jsp:include page="../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="start" name="ids"/>
            <jsp:param value="stop" name="ids"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="icon-start" name="icons"/>
            <jsp:param value="icon-stop" name="icons"/>
            <jsp:param value="增加" name="names"/>
            <jsp:param value="编辑" name="names"/>
            <jsp:param value="删除" name="names"/>
            <jsp:param value="启动" name="names"/>
            <jsp:param value="停止" name="names"/>
            <jsp:param value="add()" name="funs"/>
            <jsp:param value="edit()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
            <jsp:param value="start()" name="funs"/>
            <jsp:param value="stop()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="scheduleInstanceSearchForm">
                调度名称:<input type="text" name="filter[t.templateName]" like="true" class="easyui-textbox">
                实例描述:<input type="text" name="filter[i.instanceDesc]" like="true" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">
                    搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" url="${path}scheduleInstance/list"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="TEMPLATENAME" width="15">调度任务名称</th>
            <th field="CRON" width="15">CRON表达式</th>
            <th field="STATUS" width="15" formatter="statusFromatter">状态</th>
            <th field="EDITABLE" width="15" formatter="editableFormatter">是否可编辑</th>
            <th field="INSTANCEDESC" width="15">描述</th>
        </tr>
        </thead>
    </table>
</div>
</body>