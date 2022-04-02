<!--
	作者:徐波
	日期:2016-10-24 15:08:19
	页面:原料库存表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>原料库存表</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="materialStock.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: false;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="materialStockSearchForm" autoSearchFunction="false">
					产品大类:<input type="text" name="filter[produceCategory]" like="true" class="easyui-textbox">
					规格型号:<input type="text" name="filter[materialModel]" like="true" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"  url="${path}stock/materialStock/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="rowStyler:rowStyler">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="PRODUCECATEGORY" sortable="true" width="10">产品大类</th>
					<th field="MATERIALMODEL" sortable="true" width="10">规格型号</th>
					<th field="WEIGHT" sortable="true" width="10" formatter="processNumberFormatter">重量(kg)</th>
				</tr>
			</thead>
		</table>
	</div>
</body>
</html>