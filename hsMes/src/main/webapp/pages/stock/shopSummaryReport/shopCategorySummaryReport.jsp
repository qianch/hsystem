<!--
	作者:king
	日期:2017-8-2 10:39:01
	页面:车间入库汇总报表（产品大类、订单号、批次号、厂内名称）JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>车间入库汇总报表（产品大类、厂内名称汇总重量）</title>
  	<%@ include file="../../../pages/base/meta.jsp" %>
  	<%@ include file="shopCategorySummaryReport.js.jsp" %>
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
					<label class="panel-title">类别代码：</label>
					<input type="text" id="code" name="filter[categoryCode]" like="true" class="easyui-textbox">
					<label class="panel-title">客户产品名称：</label>
					<input type="text" id="consumerProductName" name="filter[consumerProductName]" like="true" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="platform-icon9" onclick="exportExcel()">
						导出
					</a>
					<br/>

					<label class="panel-title">开始时间：</label>
					<input type="text" id="start" name="filter[start]" class="easyui-datetimebox">
					<label class="panel-title">结束时间：</label>
					<input type="text" id="end" name="filter[end]"  class="easyui-datetimebox">
					<label class="panel-title">生产车间：</label>
					<input type="text" id="workShopCode" class="easyui-combobox" name="filter[workShopCode]"
						   data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=weave,cut'">
					<label class="panel-title">产品规格：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
					<input type="text" id="salesOrderCode" name="filter[productModel]" like="true" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
					<br/>

				</form>
			</div>
		</div>
		<table id="dg" singleSelect="true" remotesort="false" title="车间入库汇总报表（产品大类、厂内名称汇总重量）" class="easyui-datagrid"  toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true" data-options="onLoadSuccess:onLoadSuccess,showFooter:true">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="CATEGORYNAME"  width="14%">产品大类</th>
					<th field="CATEGORYCODE"  width="14%">成品类别代码</th>
					<th field=CONSUMERPRODUCTNAME  width="14%">客户产品名称</th>
					<th field="FACTORYPRODUCTNAME"  width="14%">厂内名称</th>
					<th field="PRODUCTMODEL"  width="14%">产品规格</th>
					<th field="WORKSHOPNAME"  width="14%">车间</th>
					<th field="WEIGHT"  width="12%">重量(Kg)</th>
				</tr>
			</thead>
		</table>
	</div>
</body>
