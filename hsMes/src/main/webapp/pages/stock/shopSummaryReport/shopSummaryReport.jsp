<!--
	作者:king
	日期:2017-8-2 10:39:01
	页面:车间入库汇总报表（产品大类、订单号、批次号、厂内名称）JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>间入库汇总报表（产品大类、订单号、批次号、厂内名称）</title>
  	<%@ include file="/pages/base/meta.jsp" %>
  	<%@ include file="shopSummaryReport.js.jsp" %>
  </head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div>
				<form action="#" id="searchForm" autoSearchFunction="false">
					
					<label class="panel-title">厂内名称：</label>
					<input type="text" id="factoryProductName" name="filter[factoryProductName]" like="true" class="easyui-textbox">
					
					<label class="panel-title">产品大类：</label>
					<input type="text" id="name" name="filter[categoryName]" like="true" class="easyui-textbox">
					
					<label class="panel-title">客户名称:</label>
					<input type="text" id="salesOrderCode" name="filter[CONSUMERNAME]" like="true" class="easyui-textbox">
					
					<label class="panel-title">客户订单号:</label>
					<input type="text" id="salesOrderSubCodePrint" name="filter[salesOrderSubCodePrint]" like="true" class="easyui-textbox">
					<br/>
					<label class="panel-title">类别代码:</label>&nbsp;&nbsp;
					<input type="text" id="code" name="filter[categoryCode]" like="true" class="easyui-textbox">
					
					<label class="panel-title">开始时间:</label>&nbsp;&nbsp;&nbsp;
					<input type="text" id="start" name="filter[start]" class="easyui-datetimebox">
					<label class="panel-title">结束时间:</label>
					<input type="text" id="end" name="filter[end]"  class="easyui-datetimebox">
					<label class="panel-title">批次号:</label>
					<input type="text" id="batchCode" name="filter[batchCode]" like="true" class="easyui-textbox">
					<br/>
					<label class="panel-title">生产车间：</label>
					<input type="text" name="filter[workShop]" like="true"
								class="easyui-combobox"
								data-options=" valueField: 'id',
      				  textField: 'text',data:[{'id':'','text':'全部车间'},{'id':'编织一车间','text':'编织一车间'},{'id':'编织二车间','text':'编织二车间'},{'id':'编织三车间','text':'编织三车间'},{'id':'裁剪车间','text':'裁剪车间'}],onSelect:filter">
					<label class="panel-title">产品名称：</label>
					<input type="text" id="salesOrderCode" name="filter[salesOrderCode]" like="true" class="easyui-textbox">
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="platform-icon9" onclick="exportExcel()">
						导出
					</a>
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="true" remotesort="false" title="车间入库汇总报表（产品大类、订单号、批次号、厂内名称）" class="easyui-datagrid"  toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true" data-options="onLoadSuccess:onLoadSuccess,showFooter:true">
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="PRODUCEPLANCODE"  width="12%">计划单号</th>
					<th field="SALESORDERCODE"  width="12%">订单号</th>
					<th field="SALESORDERSUBCODEPRINT"  width="12%">客户订单号</th>
					<th field="BATCHCODE"  width="10%">批次号</th>
					<th field="CONSUMERNAME"  width="12%">客户名称</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="CATEGORYNAME"  width="10%">产品大类</th>
					<th field="CATEGORYCODE"  width="10%">成品类别代码</th>
					<th field=CONSUMERPRODUCTNAME  width="10%">客户产品名称</th>
					<th field="FACTORYPRODUCTNAME"  width="10%">厂内名称</th>
					<th field="PRODUCTMODEL"  width="10%">产品规格</th>
					<th field="ROLLQUALITYGRADECODE"  width="10%">质量等级</th>
					<th field="WORKSHOPNAME"  width="10%">车间</th>
					<th field="TNUM"  width="10%">托</th>
					<th field="WEIGHT"  width="10%">重量(Kg)</th>
				</tr>
			</thead>
		</table>
	</div>
</body>