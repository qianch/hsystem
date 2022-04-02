<!--
	作者:宋黎明
	日期:2016-11-16 13:44:47
	页面:成品出库记录表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>成品出库记录表</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="productOutRecord.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: false;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
		<jsp:include page="../../base/toolbar.jsp">
			<jsp:param value="excel" name="ids"/>
			<jsp:param value="platform-icon9" name="icons"/>
			<jsp:param value="exportExcel()" name="funs"/>
			<jsp:param value="导出" name="names"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="productOutRecordSearchForm" autoSearchFunction="false">
					仓 &nbsp;&nbsp;库：<input type="text" class="easyui-combobox" name="filter[warehouseCode]"like="true"
				data-options="valueField:'warehouseCode',textField:'warehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=cp'">
					库 &nbsp;&nbsp; 位 ：<input type="text" name="filter[warehousePosCode]" like="true" class="easyui-textbox">
					条码：<input type="text" name="filter[code]" like="true" class="easyui-textbox">
					发货单编号：<input type="text" name="filter[deliveryCode]" like="true" class="easyui-textbox"><br>
					订单号：<input type="text" name="filter[salesCode]" like="true" class="easyui-textbox">
					批 次 号：<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox">
					客户：<input type="text" name="filter[consumer]" like="true" class="easyui-textbox"><br>
					规&nbsp;&nbsp;&nbsp;格：<input type="text" name="filter[model]" like="true" class="easyui-textbox">
					出库时间：<input type="text" id="start" name="filter[start]" class="easyui-datetimebox">
					&nbsp;至：<input type="text" id="end" name="filter[end]"  class="easyui-datetimebox">

					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel" onclick="productExport()">
						导出
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true" >
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="BARCODE" sortable="true" width="100">条码号</th>
					<th field="DELIVERYCODE" sortable="true" width="100">发货单编号</th>
					<th field="PRODUCTFACTORYNAME" sortable="true" width="120">厂内名称</th>
					<th field="PRODUCTCONSUMERNAME" sortable="true" width="120">客户产品名称</th>
					<th field="PRODUCTMODEL" sortable="true" width="100">产品规格</th>
					<th field="ROLLWEIGHT" sortable="true" width="60" formatter="processNumberFormatter">重量(KG)</th>
					<th field="SALESORDERCODE" sortable="true" width="80">订单号</th>
					<th field="BATCHCODE" sortable="true" width="80">批次号</th>
					<th field="CONSUMERNAME" sortable="true" width="120">客户</th>
					<th field="WAREHOUSENAME" sortable="true" width="60">仓库</th>
					<th field="WAREHOUSEPOSCODE" sortable="true" width="60">库位</th>
					<th field="OPERATEUSERNAME" sortable="true" width="60">操作人</th>
					<th field="OUTTIME" sortable="true" width="120">出库时间</th>
				</tr>
			</thead>
		</table>
	</div>
</body>