<!--
	作者:king
	日期:2017-8-2 10:39:01
	页面:成品大类汇总JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>产成品汇总(按客户统计)</title>
  	<%@ include file="/pages/base/meta.jsp" %>
  	<%@ include file="customerStockStatistics.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: false;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="productStockSearchForm" autoSearchFunction="false">
				仓　库：<input type="text" class="easyui-combobox" name="filter[warehouseCode]"like="true" data-options="valueField:'warehouseCode',textField:'warehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=cp',onSelect:filter">
				库　　位：<input type="text" name="filter[warehousePosCode]" like="true" class="easyui-textbox">
				条　码：<input type="text" name="filter[code]" class="easyui-textbox">
				<br> 
				订单号：<input type="text" like="true" name="filter[salesCode]" class="easyui-textbox">
				批&nbsp;次&nbsp;号&nbsp;：<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox"> 
				客　户：<input type="text" name="filter[consumer]" like="true" class="easyui-textbox">
					<!-- 状态：<input type="text" name="filter[consumer]" like="true" class="easyui-combobox"  data-options="data: [
		                        {value:'1',text:'合格'},
		                        {value:'2',text:'不合格'},
		                        {value:'3',text:'冻结'},
		                        {value:'5',text:'退货'},
		                        {value:'6',text:'超产'}]"> -->
		                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
				<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="platform-icon9" onclick="exportExcel()">导出 </a>
				<br>
					规　格：<input type="text" name="filter[model]" like="true"
						class="easyui-textbox"> 
					入库时间：<input type="text" id="start" name="filter[start]" class="easyui-datetimebox"> 
					　至　：<input
						type="text" id="end" name="filter[end]" class="easyui-datetimebox">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()"> 搜索 </a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="产成品汇总(按客户统计)" class="easyui-datagrid"
			 toolbar="#toolbar"
			pagination="true" rownumbers="true" fitColumns="false" fit="true" remoteSort="true"
			data-options="showFooter:true,onLoadSuccess:onLoadSuccess">
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="SALESORDERCODE" sortable="true" width="130" formatter="orderFormatter">订单号</th>
					<th field="BATCHCODE" sortable="true" width="100" formatter="batchFormatter">批次号</th>
					<th field="PLANDELIVERYDATE" sortable="true" width="100" formatter="deliveryFormatter">发货日期</th>
					<th field="FACTORYPRODUCTNAME" sortable="true" width="130" formatter="modelFormatter">厂内名称</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="CONSUMERPRODUCTNAME" sortable="true" width="130" formatter="modelFormatter">客户产品名称</th>
					<th field="TCPROCBOMVERSIONPARTSNAME" sortable="true" width="130">部件名称</th>
					<th field="PRODUCTMODEL" sortable="true" width="130" formatter="modelFormatter">产品规格</th>
					<th field="CONSUMERNAME" sortable="true" width="210" formatter="consumerFormatter">客户名称</th>
					<th field="WEIGHT" sortable="true" width="70">重量(KG)</th>
					<th field="INTIME" sortable="true" width="140">入库时间</th>
					<th field="DAYS"  width="140" formatter="inDays">在库天数</th>
					<th field="PRODUCTSHELFLIFE" sortable="true" width="90">保质期(天)</th>
					<th field="STOCKSTATE" sortable="true" width="70"formatter="formatterState">状态</th>
					<th field="ROLLQUALITYGRADECODE" sortable="true" width="70">质量等级</th>
				</tr>
			</thead>
		</table>
	</div>
</body>
