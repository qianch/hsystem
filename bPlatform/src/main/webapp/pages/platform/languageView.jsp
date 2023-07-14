<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>客户管理</title>
    <%@ include file="../base/meta.jsp" %>
    <script type="text/javascript" src="<%=basePath%>resources/easyui/extentions/datagrid-dnd.js"></script>
    <%@ include file="languageView.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true">
<div data-options="region:'center',border:false" style="overflow: auto;position: relative;">
    <div id="toolbar">
        <jsp:include page="../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="add" name="names"/>
            <jsp:param value="edit" name="names"/>
            <jsp:param value="remove" name="names"/>
            <jsp:param value="add()" name="funs"/>
            <jsp:param value="edit()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">

        </div>
    </div>
    <table singleSelect="true" fit="true" id="dg" title="语言列表" style="width:auto;"
           class="easyui-datagrid" url="${path}language/list" toolbar="#toolbar" pagination="true" rownumbers="true"
           fitColumns="true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="LANGUAGECODE" width="10"><spring:message code="Identification"/></th>
            <th field="CHINESE" width="10"><spring:message code="Chinese"/></th>
            <th field="ENGLISH" width="10"><spring:message code="English"/></th>
            <th field="ARABIC" width="10"><spring:message code="Arabic"/></th>
            <th field="TURKEY" width="10"><spring:message code="Turkey"/></th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>