<!--
	作者:徐波
	日期:2017-3-2 13:36:42
	页面:投料记录JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>编织投料记录</title>
  	<%@ include file="../../../base/meta.jsp" %>
	<%@ include file="feedingRecord.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="feedingRecordSearchForm" autoSearchFunction="false">
					　　客户:<input type="text" name="filter[consumerSimpleName]" like="true" class="easyui-textbox">
					　　订单:<input type="text" name="filter[salesorderCode]" like="true" class="easyui-textbox">
					　计划号:<input type="text" name="filter[planCode]" like="true" class="easyui-textbox">
					产品名称:<input type="text" name="filter[productName]" like="true" class="easyui-textbox"><br>
					原料条码:<input type="text" name="filter[materialcode]" like="true" class="easyui-textbox">
					车间：<input type="text" name="filter[workShop]" id="workShop" class="easyui-combobox"  data-options="data: [
		                        {value:'编织一车间',text:'编织一车间'},
		                        {value:'编织二车间',text:'编织二车间'},
		                        {value:'编织三车间',text:'编织三车间'},
				                 {value:'裁剪车间',text:'裁剪车间'}],onSelect:filter">
					机  台  号:　<input type="text" id="deviceCode" name="filter[deviceCode]" in="true" class="easyui-textbox" prompt="A1,A2逗号分割" style="width:122px;"><br>
					投料日期:<input type="text" name="filter[start]"  class="easyui-datetimebox">
					　　至:　<input type="text" name="filter[end]"  class="easyui-datetimebox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel" onclick="exportDetail()">
						导出
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false"  class="easyui-datagrid"   toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<!-- <th field="CONSUMERNAME" sortable="true" width="20">客户</th> -->
					<th field="CONSUMERSIMPLENAME" width="20" sortable="true">客户简称</th>
					<th field="SALESORDERCODE" sortable="true" width="8">订单</th>
					<th field="BATCHCODE" sortable="true" width="8">批次号</th>
					<th field="PLANCODE" sortable="true" width="12">计划号</th>
					<th field="PRODUCTMODEL" sortable="true" width="15">产品型号</th>
					<th field="PRODUCTNAME" sortable="true" width="15">产品名称</th>
					<th field="MATERIALCODE" sortable="true" width="15">原料条码</th>
					<th field="MATERIALMODEL" sortable="true" width="15">原料型号</th>
					<th field="DEVICECODE" sortable="true" width="4">机台</th>
					<th field="USERNAME" sortable="true" width="8">操作人</th>
					<th field="FEEDINGDATE" sortable="true" width="15">投料日期</th>
					<th field="WEIGHT" sortable="true" width="15">重量(kg)</th>
				</tr>
			</thead>
		</table>
	</div>
</body>