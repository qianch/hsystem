<!--
	作者:徐波
	日期:2016-10-24 15:08:20
	页面:成品库存表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>成品库存表</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="productStock.js.jsp" %>
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
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="productStockSearchForm" autoSearchFunction="false">
					
					
					<label class="panel-title">搜索：</label>
					<input type="text" name="filter[**]" like="true" class="easyui-textbox">
					
					
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="成品库存表列表" class="easyui-datagrid"  url="${path}stock/productStock/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="WAREHOUSEPOSID" sortable="true" width="15">库位id</th>
					<th field="ROLLCODE" sortable="true" width="15">卷条码</th>
					<th field="TRAYCODE" sortable="true" width="15">托条码</th>
					<th field="INTIME" sortable="true" width="15">入库时间</th>
					<th field="OUTTIME" sortable="true" width="15">出库时间</th>
					<th field="STATE" sortable="true" width="15">状态</th>
					<th field="STOCKSTATE" sortable="true" width="15">库存状态</th>
				</tr>
			</thead>
		</table>
	</div>
</body>

</html>