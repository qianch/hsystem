<!--
	作者:孙利
	日期:2017-06-08 16:44:20
	页面:月度订单产品汇总表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>月度订单产品汇总表</title>
<%@ include file="../base/meta.jsp"%>
<%@ include file="summaryMonthlyOrder.js.jsp"%>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: false;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="summaryMonthlyOrderFrom" autoSearchFunction="false">
					客户名称:<input type="text" name="filter[consumerName]" like="true" class="easyui-textbox" data-options="onSelect:filter">
					客户产品名称:<input type="text" name="filter[consumerProductName]" like="true" class="easyui-textbox" data-options="onSelect:filter">
					厂内名称:<input type="text" name="filter[factoryProductName]" like="true" class="easyui-textbox" data-options="onSelect:filter">
					
					</br>
					计调下单时间:
					<input type="text" style="width:173px;" class="easyui-datebox" id="start" name="filter[start]" data-options="icons:[]">
					
					至<input type="text" style="width:173px;" class="easyui-datebox" id="end" name="filter[end]" data-options="icons:[]">
					&nbsp;&nbsp;出货时间:
					<input type="text" style="width:173px;" class="easyui-datebox" id="outstart" name="filter[outstart]" data-options="icons:[]">
					
					至<input type="text" style="width:173px;" class="easyui-datebox" id="outend" name="filter[outend]" data-options="icons:[]">
					<a href="javascript:void(0)"
					class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
					onclick="filter()"> 搜索 </a>
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-print" onclick="export1()">
						导出</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"
			url="${path}/salesOrder/summaryMonthlylist" toolbar="#toolbar"
			pagination="true" rownumbers="true" fitColumns="false" fit="true" remoteSort="true"
			data-options="rowStyler:rowStyler" >
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="CONSUMERNAME" sortable="true" width="210">客户名称</th>
					<th field="CONSUMERPRODUCTNAME" sortable="true" width="210">客户产品名称</th>
					<th field="FACTORYPRODUCTNAME" sortable="true" width="210">厂内名称</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="PLANTOTALWEIGHT" sortable="true" width="120" formatter="plantotalweightFormatter">计调下单量(kg)</th>
					<th field="COMPLETEDAMOUNT"  width="120" formatter="completedamountFormatter">已完成量(kg)</th>	
					<th field="WWEIGHT" width="120" formatter="wweightFormatter">未完成量(kg)</th>
					<th field="STOCKOUTWEIGHT"  width="120" formatter="stockoutweightFormatter">出货量(kg)</th>
				</tr>
			</thead>
		</table>
	</div>
</body>