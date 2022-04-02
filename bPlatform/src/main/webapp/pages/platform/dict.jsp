<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>数据字典管理</title>
    <%@ include file="../base/meta.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>resources/zTree_v3/css/zTreeStyle/zTreeStyle.css">
    <script type="text/javascript" src="<%=basePath %>resources/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="<%=basePath %>resources/zTree_v3/js/jquery.ztree.exedit-3.5.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>resources/ext/zTree.ext.js"></script>
    <%@ include file="dict.js.jsp" %>
    <script type="text/javascript">
        (function ($) {
            $.extend($.fn.validatebox.defaults.rules, {
                CharAndNumber: {
                    validator: function (value) {
                        const reg = /^[A-Za-z0-9_-]+$/;
                        return reg.test(value);
                    }, message: "只能输入数字，字母，下划线，横杠"
                }
            });
        })(jQuery);
    </script>
</head>
<body class="easyui-layout" data-options="fit:true">
<div region="west" split="true" title="<spring:message code="Dict.Tree"></spring:message>" style="width:200px;">
    <div class="datagrid-toolbar" style="text-align: center;font-size:13px;font-weight:bolder;padding:5px;">
        <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="expandAll(1)"><spring:message
                code="Tree.UnFolder"/></a>
        <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="expandAll(0)"><spring:message
                code="Tree.Folder"/></a>
        <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="unselect()"><spring:message
                code="Tree.Unselect"/></a>
    </div>
    <ul id="dictTree" class="ztree"></ul>
</div>
<div data-options="region:'center',border:false" style="overflow: auto;position: relative;">
    <div id="toolbar">
        <jsp:include page="../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="增加" name="names"/>
            <jsp:param value="编辑" name="names"/>
            <jsp:param value="add()" name="funs"/>
            <jsp:param value="edit()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="dictFilter">
                <label class="panel-title"><spring:message code="Dict.Search"/>：</label>
                <input type="hidden" id="pid" in="true" name="filter[pid]">
                <spring:message code="Dict.Name"/>：<input type="text" name="filter[name]" id="code" value="" like="true"
                                                          class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">
                    <spring:message code="Dict.Search"/>
                </a>
            </form>
        </div>
    </div>
    <table singleSelect="true" fit="true" id="dg" title="字典列表" style="width:auto;"
           class="easyui-datagrid" url="<%=basePath%>dict/list" toolbar="#toolbar" pagination="true" rownumbers="true"
           fitColumns="true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="NAME_ZH_CN" width="10">字典名称</th>
            <th field="CODE" sortable="true" width="10">字典代码</th>
            <th field="ROOTCODE" sortable="true" width="10">根节点代码</th>
            <th field="DEPRECATED" width="10" data-options="formatter:isDeprecated">是否有效</th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>