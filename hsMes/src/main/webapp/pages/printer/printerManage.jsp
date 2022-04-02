<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>打印机管理</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="printerManage.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
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
			<!--  
			<div style="border-top:1px solid #DDDDDD" >
				<form action="#" id="printerManageSearchForm">
				</form>
			</div>
			-->
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"  url="${path}printerManage/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="PRINTERIP" sortable="true" width="15" formatter="formatter">打印机IP</th>
					<th field="PRINTERNAME" sortable="true" width="15">打印机名称</th>
					<th field="PRINTERTXTNAME" sortable="true" width="15">打印机显示名称</th>
					<th field="DEPARTMENT" sortable="true" width="15">部门</th>
				</tr>
			</thead>
		</table>
	</div>
</body>