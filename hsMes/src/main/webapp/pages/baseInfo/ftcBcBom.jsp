<!--
作者:高飞
日期:2017-11-28 11:10:48
页面:包装版本信息JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>包装版本信息</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="ftcBcBom.js.jsp" %>
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
            <form action="#" id="ftcBcBomVersionSearchForm" autoSearchFunction="false">
                <label class="panel-title">搜索：</label>
                <input type="text" name="filter[**]" like="true" class="easyui-textbox">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">
                    搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="包装版本信息列表" class="easyui-datagrid"
           url="${path}ftcBcBomVersion/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true"
           fit="true" data-options="onDblClickRow:dbClickEdit">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="BID" sortable="true" width="15">包材BOM ID</th>
            <th field="VERSION" sortable="true" width="15">版本</th>
            <th field="BCTOTALWEIGHT" sortable="true" width="15">包材总重</th>
            <th field="PRODUCTTYPE" sortable="true" width="15">产品类别</th>
            <th field="CONSUMERID" sortable="true" width="15">客户名称</th>
            <th field="ROLLDIAMETER" sortable="true" width="15">卷径</th>
            <th field="PALLETLENGTH" sortable="true" width="15">托长</th>
            <th field="PALLETWIDTH" sortable="true" width="15">托宽</th>
            <th field="ROLLSPERPALLET" sortable="true" width="15">每托卷数</th>
            <th field="PALLETHEIGHT" sortable="true" width="15">托高</th>
            <th field="REQUIREMENT_SULIAOMO" sortable="true" width="15">塑料膜要求</th>
            <th field="REQUIREMENT_BAIFANG" sortable="true" width="15">摆放要求</th>
            <th field="REQUIREMENT_DABAODAI" sortable="true" width="15">打包带要求</th>
            <th field="REQUIREMENT_BIAOQIAN" sortable="true" width="15">标签要求</th>
            <th field="REQUIREMENT_XIAOBIAOQIAN" sortable="true" width="15">小标签要求</th>
            <th field="REQUIREMENT_JUANBIAOQIAN" sortable="true" width="15">卷标签要求</th>
            <th field="REQUIREMENT_TUOBIAOQIAN" sortable="true" width="15">托标签要求</th>
            <th field="REQUIREMENT_CHANRAO" sortable="true" width="15">缠绕要求</th>
            <th field="REQUIREMENT_OTHER" sortable="true" width="15">其他</th>
            <th field="ENABLED" sortable="true" width="15">是否可用</th>
        </tr>
        </thead>
    </table>
</div>
</body>