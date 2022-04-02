<!--
	作者:肖文彬
	日期:2016-9-29 15:45:32
	页面:订单管理JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>裁剪订单管理</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="cutSalesOrder.js.jsp" %>
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
				<form action="#" id="cutSalesOrderSearchForm" autoSearchFunction="false">
					<label class="panel-title">搜索：</label>
					批次号:<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox">
					叶型名称:<input type="text" name="filter[bladeTypeName]" like="true" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"  url="${path}cutSalesOrder/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true" data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="BATCHCODE" width="15">批次号</th>
					<th field="BLADETYPENAME" width="15">叶型名称</th>
					<th field="SALESORDERCODE" width="15">订单号</th>
					<th field="CUSTOMERNAME" width="15">客户名称</th>
					<th field="ORDERNUM" width="15">订单数量</th>
					<th field="REMARK" width="15">备注</th>
				</tr>
			</thead>
		</table>
	</div>
</body>
