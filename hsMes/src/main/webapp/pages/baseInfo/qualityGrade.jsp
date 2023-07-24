<!--
作者:高飞
日期:2016-10-12 10:34:41
页面: 质量等级JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title> 质量等级</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="qualityGrade.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
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
            <form action="#" id="qualityGradeSearchForm" autoSearchFunction="false">
                等级名称：<input type="text" name="filter[gradeName]" like="true" class="easyui-textbox">
                等级描述：<input type="text" name="filter[gradeDesc]" like="true" class="easyui-textbox">
                等级备注：<input type="text" name="filter[gradeMemo]" like="true" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-search" onclick="filter()">
                    搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid" url="${path}qualityGrade/list"
           toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"
           data-options="onDblClickRow:dbClickEdit">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="GRADENAME" width="15">等级名称</th>
            <th field="GRADEDESC" width="15">等级描述</th>
            <th field="GRADEMEMO" width="15" formatter="memoFormatter">备注</th>
        </tr>
        </thead>
    </table>
</div>
</body>