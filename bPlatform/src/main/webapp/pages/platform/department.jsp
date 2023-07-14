<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>部门信息</title>
    <%@ include file="../base/meta.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resources/zTree_v3/css/zTreeStyle/zTreeStyle.css">
    <script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.exedit-3.5.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/ext/zTree.ext.js"></script>
    <script type="text/javascript" src="<%=basePath%>resources/platform/department.js"></script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div region="west" split="true" title="部门树形列表" style="width:200px;">
    <ul id="deptTree" class="ztree"></ul>
</div>
<div data-options="region:'center',border:false" style="overflow: auto;position: relative;">
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
            <jsp:param value="deleteDepartment()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="departmentFilter">
                <label class="panel-title">部门搜索：</label>
                <input type="hidden" id="pid" name="filter[pid]">
                部门名称：<input type="text" name="filter[name]" id="name" value="" like="true" class="easyui-textbox">
                部门编码：<input type="text" name="filter[code]" id="code" value="" like="true" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" title="部门列表" class="easyui-datagrid" fit="true" url="<%=basePath%>department/list"
           toolbar="#toolbar"
           pagination="true" rownumbers="true" fitColumns="true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="NAME" width="10">部门名称</th>
            <th field="CODE" width="10">部门编码</th>
            <th field="TYPE" width="15"
                data-options="formatter:function(value,row,index){return dictFormatter('DepartmentType',value)}">部门类型
            </th>
            <th field="PREFIX" width="10">前缀</th>
            <th field="USERCOUNT" width="10">部门人数</th>
        </tr>
        </thead>
    </table>
</div>
<div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px" closed="true"
     buttons="#dlg-buttons">
    <!-- 编辑or添加 -->
</div>
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="save()"
       style="width:90px">保存</a> <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
                                      onclick="javascript:$('#dlg').dialog('close')" style="width:90px">取消</a>
</div>
<script type="text/javascript">
    function dictFormatter(type, value) {
        if (value == "undefined") {
            return "";
        }
        $.ajax({
            url: "<%=basePath%>dict/queryDictText?type=" + type + "&value=" + value,
            type: "get",
            async: false,
            dataType: "text",
            success: function (ajaxdata) {
                return ajaxdata;
            }
        });
    }
</script>
</body>
</html>
