<!--
	作者:肖文彬
	日期:2016-11-8 15:25:19
	页面:盘库记录表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>盘库记录表</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="stockCheck.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: false;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="stockCheckSearchForm" autoSearchFunction="false">
					盘点时间：<input type="text" name="filter[start]"  class="easyui-datetimebox">
					~<input type="text" name="filter[end]"  class="easyui-datetimebox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="盘库记录表列表" class="easyui-datagrid"  url="${path}stock/stockCheck/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  >
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="CHECKWAREHOUSEPOSCODE" sortable="true" width="15">库位代码</th>
					<th field="CHECKWAREHOUSECODE" sortable="true" width="15">库房代码</th>
					<th field="OPTUSER" sortable="true" width="15">盘点人</th>
					<th field="CHECKTIME" sortable="true" width="15">盘点时间</th>
				</tr>
			</thead>
		</table>
	</div>
</body>