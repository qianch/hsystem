<!--
	作者:徐波
	日期:2016-10-8 16:53:24
	页面:包材bom版本信息JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>包材bom版本信息</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="bCBomVersion.js.jsp" %>
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
				<jsp:param value="删除" name="names"/>
				<jsp:param value="add()" name="funs"/>
				<jsp:param value="edit()" name="funs"/>
				<jsp:param value="doDelete()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="bCBomVersionSearchForm" autoSearchFunction="false">
					
					
					<label class="panel-title">搜索：</label>
					版本号:<input type="text" name="filter[PACKVERSION]" like="true" class="easyui-textbox">
					
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="包材Bom版本" class="easyui-datagrid"  url="${path}bCBomVersion/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="PACKVERSION" width="15">版本号</th>
					<th field="PACKENABLED" width="15" formatter="formatterU">是否启用</th>
					<th field="PACKISDEFAULT" width="15" formatter="formatterDefult">是否默认</th>
					<th field="PACKBOMID" width="15">包材BOM ID</th>
				</tr>
			</thead>
		</table>
	</div>
</body>