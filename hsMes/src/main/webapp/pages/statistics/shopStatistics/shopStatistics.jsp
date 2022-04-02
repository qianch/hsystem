

<!--
	作者:徐波
	日期:2016-11-26 14:44:04
	页面:综合统计JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>综合统计</title>

<%@ include file="/pages/base/meta.jsp"%>
<%@ include file="shopStatistics.js.jsp"%>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false"
		style="position: relative; height: 140px; width: 925px">
		<div id="toolbar">

			<div style="border:0px solid #DDDDDD">
				<form action="#" id="totalStatisticsSearchForm" autoSearchFunction="false">
					
					
					产品大类:<input type="text" id="name" name="filter[categoryName]" like="true" class="easyui-textbox">
					类别代码:<input type="text" id="code" name="filter[categoryCode]" like="true" class="easyui-textbox">
					客户名称:<input type="text"name="filter[CONSUMERNAME]" like="true" class="easyui-textbox">
					厂内名称:<input type="text"name="filter[factoryProductName]" like="true" class="easyui-textbox">
					<br/>
					产品规格:<input type="text" name="filter[productModel]" like="true"class="easyui-textbox"> 
					产出车间:<input type="text"name="filter[name]" like="true" class="easyui-combobox"
							data-options=" valueField: 'id',textField: 'text',data:[{'id':'','text':'全部车间'},{'id':'编织一车间','text':'编织一车间'},{'id':'编织二车间','text':'编织二车间'},{'id':'编织三车间','text':'编织三车间'},{'id':'裁剪车间','text':'裁剪车间'}],onSelect:filter">
					订 单 号&nbsp;:<input type="text" name="filter[salesOrderCode]" like="true" class="easyui-textbox">
					&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="platform-icon9" onclick="exportExcel()">
						导出
					</a>
					<br/>
					批 次 号&nbsp;:<input type="text" name="filter[batchCodes]" like="true" class="easyui-textbox">
					开始时间:<input type="text" id="start" name="filter[start]" value=""class="easyui-datetimebox" > 
					结束时间:<input type="text" id="end" name="filter[end]" value=""class="easyui-datetimebox">&nbsp;&nbsp;&nbsp; 

					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>					
				
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="车间生产统计(产品大类、订单号、批次号、车间)" class="easyui-datagrid"
			toolbar="#toolbar"
			pagination="true" rownumbers="true" fitColumns="false" fit="true" data-options="onLoadSuccess:onLoadSuccess,showFooter:true">
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="PRODUCEPLANCODE"  width="10%" >计划单号</th>
					<th field="SALESORDERCODE"  width="10%" >订单号</th>
					<th field="BATCHCODE"  width="10%" >批次号</th>
					<th field="FACTORYPRODUCTNAME"  width="10%" >厂内名称</th>
					<th field="CONSUMERPRODUCTNAME"  width="10%" >客户产品名称</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="NAME"  width="10%" >车间</th>
					<th field="CATEGORYNAME"  width = '10%' >产品大类</th>
					<th field="CATEGORYCODE"  width="10%" >成品类别代码</th>
					<th field="PRODUCTWEIGHT" width="10%">重量(Kg)</th>
					<th field="TNUM" width="10%">托</th>
					<th field="CONSUMERNAME"  width="10%">客户名称</th>
					<th field="CONSUMERPRODUCTNAME"  width="10%">客户产品名称</th>
					<th field="PRODUCTMODEL"  width="10%">产品规格</th>
					<th field="ROLLQUALITYGRADECODE" width="10%">质量等级</th>
					<th field="ROLLOUTPUTTIME" width="10%">产出时间</th>
<!-- 					<th field="STATES" width="10%" formatter="stockStateFormatter">状态</th> -->
				</tr>
			</thead>
		</table>
	</div>


</body>