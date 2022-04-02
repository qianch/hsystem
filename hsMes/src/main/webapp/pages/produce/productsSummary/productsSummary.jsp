<!--
	作者:king
	日期:2017-8-2 10:39:01
	页面:成品大类汇总JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>产成品汇总(成品类别)</title>
  	<%@ include file="/pages/base/meta.jsp" %>
  	<%@ include file="productsSummary.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="height:24px;">
				<form action="#" id="searchForm" autoSearchFunction="false">
					
					<label class="panel-title">产品大类：</label>
					<input type="text" id="name" name="filter[name]" like="true" class="easyui-textbox">
					
					<label class="panel-title">类别代码：</label>
					<input type="text" id="code" name="filter[code]" like="true" class="easyui-textbox">
					
					<label class="panel-title">开始时间:</label><input type="text" id="start" name="filter[start]" class="easyui-datetimebox">
					<label class="panel-title">结束时间:</label><input type="text" id="end" name="filter[end]"  class="easyui-datetimebox">
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="platform-icon9" onclick="exportExcel()">
						导出
					</a>
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="true" remotesort="false" title="产成品汇总(成品类别)" class="easyui-datagrid"  toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true" data-options="onBeforeLoad:setTotal,onLoadSuccess:onLoadSuccess">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="CATEGORYNAME"  width="21%">产品大类</th>
					<th field="CATEGORYCODE"  width="21%">成品类别代码</th>
					<th field="STARTWEIGHT"  width="10%">月初库存数量(Kg)</th>
					<th field="INWEIGHT"  width="10%">当月入库数量(Kg)</th>
					<th field="USENUM"  width="10%">当月领用数量(Kg)</th>
					<th field="OUTWEIGHTS"  width="10%">当月发出数量(Kg)</th>
					<th field="ATUM"  width="10%">月末累计数量(Kg)</th>
				</tr>
			</thead>
		</table>
	</div>
</body>