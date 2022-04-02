<!--
	作者:高飞
	日期:2016-10-13 11:06:42
	页面:物料需求计划JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>物料需求计划</title>
<%@ include file="../../base/meta.jsp"%>
<%@ include file="mrpList.js.jsp"%>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="west" split="true" resizable="false" title="生产计划"
		minWidth="318" width="318">
		<div id="toolbar">
			<jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="export" name="ids" />
				<jsp:param value="icon-print" name="icons" />
				<jsp:param value="导出" name="names" />
				<jsp:param value="_export()" name="funs" />
			</jsp:include>
		</div>
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:false,border:false"
				style="text-align: center;width:318px;">
				<form action="#" id="salesOrderSearchForm" style="text-align: left"
					class="datagrid-toolbar" autoSearchFunction="loadProducePlans" autoSearchFunction="false">
					时间:<input type="text" name="filter[begin]" like="false"
						class="easyui-datebox" style="width: 115px" prompt="开始时间">
					至 <input type="text" name="filter[end]" like="false"
						class="easyui-datebox" style="width: 115px" prompt="结束时间"></br>
					单号:<input type="text" name="filter[code]" like="true"
						class="easyui-textbox" style="width: 200px" prompt="生产或者订单号">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						iconcls="icon-search" onclick="loadProducePlans()"> 搜索 </a>
				</form>
				<div class="datagrid-toolbar"
					style="text-align: center;font-size:13px;font-weight:bolder;padding:5px;">
					<a href="javascript:void(0)" style="color:#0E2D5F;"
						onClick="allSelect()">全选</a>&nbsp<a href="javascript:void(0)"
						style="color:#0E2D5F;" onClick="unAllSelect()">取消</a>
				</div>

			</div>

			<div data-options="region:'center',border:false"
				style="overflow-x:hidden;">
				<ul id="dl" class="easyui-datalist" checkbox="true"
					singleSelect="false" lines="true" style="width:100%;"
					valueField="ID" textField="PRODUCEPLANCODE"
					data-options="onSelect:loadMrp,onUnselect:loadMrp">
				</ul>
			</div>
		</div>
	</div>
	<div data-options="region:'center',border:false"
		style="position: relative; height: 140px; width: 925px">
		<!-- <table id="dg_tc" style="width:100%;" class="easyui-datagrid" rownumbers="true" fitColumns="true" >
			<thead>
				<tr>
					<th field="FABRICMODEL">胚布规格</th>
					<th field="FABRICCODE">胚布代码</th>
					<th field="FABRICWIDTH">门幅</th>
					<th field="FABRICLENGTH">米长</th>
					<th field="FABRICCOUNT">数量(卷)</th>
				</tr>
			</thead>
		</table> -->
		<table id="dg_ftc" title="原料需求" style="width:100%;height:48%"
			class="easyui-datagrid" rownumbers="true" fitColumns="true" data-options="onLoadSuccess:onLoadSuccess">
			<thead>
				<tr>
					<!-- <th field="MATERIALNAME" width="15">物料名称</th> -->
					<th field="MATERIALMODEL" width="15">规格型号</th>
					<th field="MATERIALTOTALWEIGHT" width="15" formatter="numberFormatter">需要总重</th>
					<!-- <th field="MATERIALSTOCKWEIGHT" width="15">库存总重</th> -->
				</tr>
			</thead>
		</table>
		<table id="dg_bc" title="包材需求" style="width:100%;"
			class="easyui-datagrid" rownumbers="true" fitColumns="true">
			<thead>
				<tr>
				    <th field="MTCODE" width="15">物料代码</th>
				    <th field="STCODE" width="15">标准码</th>
					<th field="PACKMATERIALNAME" width="15">包材名称</th>
					<th field="PACKMATERIALMODEL" width="15">规格型号</th>
					<th field="PACKMATERIALATTR" width="15">材质</th>
					<th field="PACKMATERIATOTALCOUNT" width="15"
						formatter="numberFormatter2">需要数量</th>
				</tr>
			</thead>
		</table>
	</div>

</body>