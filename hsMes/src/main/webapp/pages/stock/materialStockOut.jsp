<!--
	作者:肖文彬
	日期:2016-11-16 12:48:44
	页面:原料出库表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>原料出库表</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="materialStockOut.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="materialStockOutSearchForm" autoSearchFunction="false">
					出库单:<input type="text" name="filter[code]" like="true" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"  url="${path}stock/materialStockOut/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  >
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="OUTORDERCODE"  width="15">出库单号</th>
					<th field="CUNSUMERNAME"  width="15">领料车间</th>
				</tr>
			</thead>
		</table>
	</div>
</body>