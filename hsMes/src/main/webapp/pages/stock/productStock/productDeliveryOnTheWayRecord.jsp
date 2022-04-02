<!--
	作者:宋黎明
	日期:2016-11-27 13:57:45
	页面:出货计划JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>出库调拨单</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="productDeliveryOnTheWayRecord.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: false;position: relative; height: 140px; width: 925px">
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
				<form action="#" id="productDeliveryRecordSearchForm" autoSearchFunction="false">
					出库调拨单:<input type="text" name="filter[deliveryCode]" like="true" class="easyui-textbox">
					订单号：<input type="text" name="filter[salesOrderSubCode]" like="true" class="easyui-textbox">
					批 次 号：<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox"><br>
					厂内名称:<input type="text" name="filter[factoryProductName]" like="true" class="easyui-textbox">
					客户产品名称:<input type="text" name="filter[consumerProductName]" like="true" class="easyui-textbox">

					在途时间:<input type="text" name="filter[start]"  class="easyui-datetimebox">
					至:<input type="text" name="filter[end]"  class="easyui-datetimebox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>

			<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-zip" onclick="export3()">
						导出出库调拨单汇总
			</a>

			<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-zip" onclick="exportDetail()">
				导出出库调拨单明细
			</a>

			<a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onclick="PMove()">移库</a>

		</div>
		<table id="dg"  singleSelect="true" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true" data-options="showFooter:true" >
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="DELIVERYCODE" sortable="true" width="80">出库调拨单</th>
					<th field="WAREHOUSENAME" sortable="true" width="80">仓库名称</th>
					<th field="WAREHOUSECODE" sortable="true" width="80">仓库编码</th>
					<th field="DELIVERYDATE" sortable="true" width="100">在途时间</th>
					<th field="USERNAME" sortable="true" width="80">出货人</th>
					<th field="PLATE" sortable="true" width="100">车牌号</th>
					<th field="TOTALCOUNT" sortable="true" width="100">件数</th>
					<th field="TOTALWEIGHT" sortable="true" width="100">重量</th>
					<th field="ONTHEWAYCOUNT" sortable="true" width="100">在途条码数量</th>

				</tr>
			</thead>
		</table>
	</div>
</body>