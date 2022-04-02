<!--
	作者:sunli
	日期:2018-05-10 15:08:20
	页面:成品库存表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>成品库存表</title>
<%@ include file="../../base/meta.jsp"%>
</head>

<body class="easyui-layout"  data-options="fit:true,border:false" >
	<div data-options="region:'center',border:false,fit:true" style="height:100%">
		<table id="_view_stock_" singleSelect="false" class="easyui-datagrid"
			url="${path}stock/productStock/viewList1?factoryProductName=${factoryProductName}&productProcessCode=${productProcessCode}&productProcessBomVersion=${productProcessBomVersion}&workShopCode=${workShopCode}" toolbar="#toolbar1"
			rownumbers="true" showFooter="true" fitColumns="true" fit="true" remoteSort="false" data-options="emptyMsg:'无库存',rowStyler:rowStyler,onLoadSuccess:function(data){if(data.total==0){Tip.warn('无库存');}}">
			<!-- ,onLoadSuccess:function(data){
             $('#_view_stock_').datagrid('autoMergeCells',['CONSUMERNAME','SALESORDERSUBCODEPRINT','SALESORDERCODE','BATCHCODE']);
            } -->
			<thead>
				<tr>
					<th field="CONSUMERNAME" sortable="true" >客户名称</th>
					<th field="SALESORDERCODE" sortable="true" >销售单号</th>
					<th field="SALESORDERSUBCODEPRINT" sortable="true" >客户单号</th>
					<th field="BATCHCODE" sortable="true" >批次号</th>
					<%--<th field="PARTNAME" sortable="true" >部件名称</th>--%>
					<th field="WEIGHT" sortable="true" >库存重量</th>
					<th field="COUNT" sortable="true" >库存托数</th>
					<th field="EARLIESTTIME" sortable="true">最早打包时间</th>
					<th field="LATESTTIME" sortable="true">最晚打包时间</th>
				</tr>
			</thead>
		</table>
	</div>
</body>