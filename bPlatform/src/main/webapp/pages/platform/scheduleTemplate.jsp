<!--
作者:高飞
日期:2016-8-18 15:02:50
页面:任务调度模板JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>任务调度模板</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="scheduleTemplate.js.jsp" %>
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
            <form action="#" id="scheduleTemplateSearchForm">
                调度名称:<input type="text" name="filter[templateName]" like="true" class="easyui-textbox">
                调度描述:<input type="text" name="filter[templateDesc]" like="true" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">
                    搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" url="${path}scheduleTemplate/list"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="TEMPLATENAME" width="15">调度名称</th>
            <th field="CLAZZ" width="15">调度类名</th>
            <th field="CREATETIME" width="15">创建时间</th>
            <th field="TEMPLATEDESC" width="15">描述</th>
        </tr>
        </thead>
    </table>
</div>
</body>