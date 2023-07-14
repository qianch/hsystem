<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>部门选择</title>
    <%@ include file="../base/meta.jsp" %>
</head>
<body>
<div data-options="region:'center',border:false" style="overflow: auto;position: relative;">
    <div id="deptToolbar">
        <div>
            <label class="panel-title">部门搜索：</label>
            部门名称：<input type="text" name="filter[name]" id="name" value="" class="easyui-textbox">
            部门名称：<input type="text" name="filter[code]" id="code" value="" class="easyui-textbox">
            <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
               onclick="deptFilter()">
                搜索
            </a>
        </div>
    </div>
    <table id="deptGrid" toolbar="#deptToolbar" idField="id" class="easyui-datagrid" url="<%=basePath%>department/list"
           pagination="true" rownumbers="true" fitColumns="true" singleSelect="true"
           data-options="onLoadSuccess:onLoadSuccess">
        <thead>
        <tr>
            <th field="id" checkbox=true></th>
            <th field="name" width="10">部门名称</th>
            <th field="code" width="10">部门编码</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>
