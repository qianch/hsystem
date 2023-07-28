<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>设备类别</title>
    <%@ include file="../../base/meta.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resources/zTree_v3/css/zTreeStyle/zTreeStyle.css">
    <script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.exedit-3.5.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/ext/zTree.ext.js"></script>
    <%@ include file="deviceType.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true">
<div region="west" split="true" title="设备类别" style="width:200px;">
    <ul id="deviceTree" class="ztree"></ul>
</div>
<div data-options="region:'center',border:false" style="position: relative;">
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
            <form action="#" id="deviceFilter" autoSearchFunction="false">
                <label class="panel-title">搜索：</label>
                <input type="hidden" id="pid" in="true" name="filter[pid]">
                类别名称：<input type="text" name="filter[name]" id="code" value="" like="true" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">
                    搜索
                </a>
            </form>
        </div>
    </div>
    <table singleSelect="true" fit="true" id="dg" title="设备类别列表" style="width:auto;" class="easyui-datagrid"
           url="${path}deviceType/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true"
           data-options="onDblClickRow:dbClickEdit">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="CATEGORYNAME" sortable="true" width="10">类别名称</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>