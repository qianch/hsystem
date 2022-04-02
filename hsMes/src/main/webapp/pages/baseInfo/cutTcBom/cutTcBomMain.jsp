<!--
作者:宋黎明
日期:2016-11-27 13:57:45
页面:出货计划JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>裁剪图纸套材bom</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="cutTcBomMain.js.jsp" %>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="add" name="ids"/>
            <jsp:param value="edit" name="ids"/>
            <jsp:param value="cancel" name="ids"/>
            <jsp:param value="effect" name="ids"/>
            <jsp:param value="importDetail" name="ids" />
            <jsp:param value="exportDetail" name="ids" />

            <jsp:param value="icon-add" name="icons"/>
            <jsp:param value="icon-edit" name="icons"/>
            <jsp:param value="icon-remove" name="icons"/>
            <jsp:param value="platform-icon9" name="icons" />
            <jsp:param value="icon-excel" name="icons" />
            <jsp:param value="platform-icon9" name="icons" />

            <jsp:param value="增加" name="names"/>
            <jsp:param value="编辑" name="names"/>
            <jsp:param value="作废" name="names"/>
            <jsp:param value="恢复" name="names"/>
            <jsp:param value="导入" name="names"/>
            <jsp:param value="导出" name="names"/>

            <jsp:param value="add()" name="funs"/>
            <jsp:param value="edit()" name="funs"/>
            <jsp:param value="cancel()" name="funs"/>
            <jsp:param value="effect()" name="funs"/>
            <jsp:param value="importDetail()" name="funs"/>
            <jsp:param value="exportDetail()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="cutTcBomMainSearchForm" autoSearchFunction="false">
                BOM代码版本:<input type="text" name="filter[tcProcBomCodeVersion]" like="true" class="easyui-textbox">
                叶型名称：<input type="text" name="filter[bladeTypeName]" like="true" class="easyui-textbox">

                创建时间:<input type="text" name="filter[start]" class="easyui-datetimebox">
                至:<input type="text" name="filter[end]" class="easyui-datetimebox">

                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="filter()">
                    搜索
                </a>
            </form>
        </div>



    </div>
    <table id="dg" singleSelect="true" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true"
           rownumbers="true" fitColumns="true" fit="true" data-options="showFooter:true">
        <thead>
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="TCPROCBOMCODEVERSION" sortable="true" width="80">BOM代码版本</th>
            <th field="CUSTOMERNAME" sortable="true" width="80">客户名称</th>
            <th field="BLADETYPENAME" sortable="true" width="80">叶型名称</th>
            <th field="CREATETIME" sortable="true" width="100">创建时间</th>
            <th field="STATE" width="15" data-options="formatter:function(value,row,index){return dictFormatter('State',value)}" >状态</th>
        </tr>
        </thead>
    </table>
</div>
</body>
