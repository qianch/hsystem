<!--
	作者:徐波
	日期:2016-11-26 14:44:04
	页面:综合统计JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>翻包订单查询</title>
<%@ include file="../base/meta.jsp"%>
<%@ include file="turnBagStatistics.js.jsp"%>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false"
		style="overflow: false;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="border:0px solid #DDDDDD">
				<form action="#" id="totalStatisticsSearchForm" autoSearchFunction="false">
       				　订单号:<input type="text" name="filter[salesOrderCode]" like="true" class="easyui-textbox">					　
					    批次号:<input type="text"name="filter[batchCode]" like="true" class="easyui-textbox">
       			       　　<a href="javascript:void(0)"
					class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
					onclick="filter()"> 搜索 </a>
				</form>
			</div>
			<jsp:include page="../base/toolbar.jsp">
				<jsp:param value="changeInfo" name="ids" />
				<jsp:param value="icon-edit" name="icons" />
				<jsp:param value="修改重量" name="names" />
				<jsp:param value="changeInfo()" name="funs" />

			</jsp:include>
		</div>
		<table id="dg" singleSelect="false" title=""
			class="easyui-datagrid"
			toolbar="#toolbar" pagination="true" rownumbers="true"
			fitColumns="false" fit="true" data-options="onBeforeLoad:setTotal,onLoadSuccess:onLoadSuccess">
			<thead>
				<tr>
					<th field="ISTURNBAGPLAN" width="60">计划类型</th>
					<th field="SALESORDERCODE" width="130" >订单号</th>
					<th field="SALESORDERSUBCODEPRINT" width="100" >客户订单号</th>
					<th field="BATCHCODE" width="100" editor="{type:'textbox',options:{required:true}}" >批次号</th>
					<th field="PRODUCTMODEL" width="130" >产品规格</th>
					<th field="CONSUMERPRODUCTNAME" width="130" >客户产品名称</th>
					<th field="FACTORYPRODUCTNAME" width="130" >厂内名称</th>
					<th width="120" field="TOTALTRAYCOUNT" data-options="formatter:totalTrayCount">打包托数/总托数</th>
					<th field="RC" width="80" styler="processStyler" formatter="rcFormatter">生产卷数</th>
					<th field="PRODUCEDCOUNT" width="150" styler="processStyler" formatter="processFormatter3">生产进度</th>
				</tr>
			</thead>
		</table>
	</div>
</body>