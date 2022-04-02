<!--
	作者:肖文彬
	日期:2016-9-29 16:26:04
	页面:库位管理JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>库位管理</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="warehosePos.js.jsp" %>
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
				<form action="#" id="warehosePosSearchForm" autoSearchFunction="false">
					
					
					<label class="panel-title">搜索：</label>
					库位代码:<input type="text" name="filter[code]" like="true" class="easyui-textbox">
					库位名称:<input type="text" name="filter[code]" like="true" class="easyui-textbox">
					仓库名称:<input type="text" name="filter[wareHouseName]" like="true" class="easyui-textbox">
					仓库类型：<input type="text" id="wareType" name="filter[wareType]"
								class="easyui-combobox"
								required="true"
								data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=WareType'">
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"  url="${path}warehosePos/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true" data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="WAREHOUSEPOSCODE" width="15">库位代码</th>
					<th field="WAREHOUSEPOSNAME" width="15">库位名称</th>
					<th field="WAREHOUSEPOSMEMO" width="15">备注</th>
					<th field="WAREHOUSENAME" width="15">所属仓库</th>
				</tr>
			</thead>
		</table>
	</div>
</body>