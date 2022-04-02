<!--
	作者:肖文彬
	日期:2016-9-29 15:45:32
	页面:仓库管理JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>仓库管理</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="warehouse.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
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
				<jsp:param value="作废" name="names"/>
				<jsp:param value="add()" name="funs"/>
				<jsp:param value="edit()" name="funs"/>
				<jsp:param value="old()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="warehouseSearchForm" autoSearchFunction="false">
					<label class="panel-title">搜索：</label>
					仓库代码:<input type="text" name="filter[code]" like="true" class="easyui-textbox">
					仓库名称:<input type="text" name="filter[name]" like="true" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"  url="${path}warehouse/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true" data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="WAREHOUSECODE" width="15">仓库代码</th>
					<th field="WAREHOUSENAME" width="15">仓库名称</th>
					<th field="WARETYPE" width="15" data-options="formatter:function(value,row,index){return dictFormatter('WareType',value)}" >仓库类型</th>

				</tr>
			</thead>
		</table>
	</div>
</body>