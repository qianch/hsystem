<!--
作者:徐波
日期:2016-11-30 14:03:19
页面:btw文件JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>btw文件</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="btwFile.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <jsp:include page="../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="add" name="ids"/>
            <jsp:param value="delete" name="ids"/>
            <jsp:param value="edit" name="ids"/>

            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>

            <jsp:param value="新增" name="names"/>
            <jsp:param value="上传" name="names"/>
            <jsp:param value="作废" name="names"/>
            <jsp:param value="修改打印记录" name="names"/>

            <jsp:param value="addbtwFile()" name="funs"/>
            <jsp:param value="importbtwFile()" name="funs"/>
            <jsp:param value="doDelete()" name="funs"/>
            <jsp:param value="editBtwFilePrint()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="btwFileSearchForm" autoSearchFunction="false">
                客户简称:<input type="text" name="filter[consumerName]" like="true" class="easyui-textbox">
                客户代码:<input type="text" name="filter[consumerCode]" like="true" class="easyui-textbox">
                标签名称:<input type="text" name="filter[tagName]" like="true" class="easyui-textbox">
                标签类型:
                <input type="text" id="tagType" name="filter[tagType]"
                       class="easyui-combobox"
                       required="true"
                       data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=alloptions,TagType',onSelect:filter">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">
                    搜索
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="btw文件列表" class="easyui-datagrid" toolbar="#toolbar" pagination="true"
           rownumbers="true" fitColumns="true" fit="true" data-options="onDblClickRow:dbClickEdit">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="CONSUMERID" sortable="true" width="15" hidden="true">适用客户</th>
            <th field="CONSUMERCODE" sortable="true" width="15">客户代码</th>
            <th field="CONSUMERNAME" sortable="true" width="15">适用客户名称</th>
            <th field="TAGNAME" sortable="true" width="15">标签名称</th>
            <th field="TAGACTNAME" sortable="true" width="15">标签实际名称</th>
            <th field="CONSUMERBARCODEPREFIX" sortable="true" width="15">客户条码前缀</th>
            <th field="CONSUMERBARCODERECORD" sortable="true" width="15">客户条码记录</th>
            <th field="CONSUMERBARCODEDIGIT" sortable="true" width="15">客户条码位数</th>
            <th field="AGENTBARCODEPREFIX" sortable="true" width="15">供销商条码前缀</th>
            <th field="AGENTBARCODERECORD" sortable="true" width="15">供销商客户条码记录</th>
            <th field="AGENTBARCODEDIGIT" sortable="true" width="15">供销商客户条码位数</th>
            <th field="TAGTYPETEXT" sortable="true" width="15">标签类型</th>
            <th field="UPLOADUSERNAME" sortable="true" width="15">上传人</th>
            <th field="UPLOADDATE" sortable="true" width="15">上传时间</th>
            <th field="STATE" width="15"
                data-options="formatter:function(value,row,index){return dictFormatter('State',value)}">状态
            </th>
        </tr>
        </thead>
    </table>
</div>
</body>
