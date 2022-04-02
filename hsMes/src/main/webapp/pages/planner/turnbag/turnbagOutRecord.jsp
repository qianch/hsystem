<!--
	作者:sunli 
	日期:2018-4-11 14:10:25
	页面:翻包领出JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>翻包领料查询</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="turnbagOutRecord.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="turnBagOutRecordSearchForm" autoSearchFunction="false">
					产品条码:<input type="text" name="filter[code]" like="true" class="easyui-textbox">
					翻包单号:<input type="text" name="filter[turnBagCode]" like="true" class="easyui-textbox">
					领出车间:<input type="text" name="filter[outAddress]" like="true" class="easyui-combobox" data-options=" valueField: 'id',
      				  textField: 'text',data:[{'id':'','text':'全部车间'},{'id':'编织一车间','text':'编织一车间'},{'id':'编织二车间','text':'编织二车间'},{'id':'编织三车间','text':'编织三车间'},{'id':'裁剪车间','text':'裁剪车间'}],onSelect:filter">
					操&nbsp;&nbsp;&nbsp;作&nbsp;&nbsp;人:<input type="text" name="filter[optUserName]" like="true" class="easyui-textbox">
					领出时间:<input type="text" name="filter[start]"  class="easyui-datetimebox">
					　　至:　<input type="text" name="filter[end]"  class="easyui-datetimebox"><br>
					新订单号:<input type="text" name="filter[salesOrderCode]" like="true" class="easyui-textbox">
					新批次号:<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox">
					计划单号:<input type="text" name="filter[planCode]" like="true" class="easyui-textbox">
					门幅(mm):<input type="text" name="filter[productWidth]" like="true" class="easyui-textbox">
					米&nbsp;长(m):<input type="text" name="filter[productLength]" like="true" class="easyui-textbox">
					产品规格:<input type="text" name="filter[productModel]" like="true" class="easyui-textbox"><br>
					旧订单号:<input type="text" name="filter[oldSalesOrderCode]" like="true" class="easyui-textbox">
					旧批次号:<input type="text" name="filter[oldBbatchCode]" like="true" class="easyui-textbox">
					计划单号:<input type="text" name="filter[oldPlanCode]" like="true" class="easyui-textbox">
					门幅(mm):<input type="text" name="filter[oldProductWidth]" like="true" class="easyui-textbox">
					米&nbsp;长(m):<input type="text" name="filter[oldProductLength]" like="true" class="easyui-textbox">
					产品规格:<input type="text" name="filter[oldProductModel]" like="true" class="easyui-textbox"><br>
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel" onclick="exportExcel()">
						导出
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"  toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  >
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true >
					<th field="TRAYCODE" sortable="true" width="8%">产品条码</th>
					<th field="TURNBAGCODE" sortable="true" width="8%">翻包单号</th>
					<th field="SALESORDERCODE" sortable="true" width="10%">新订单号</th>
					<th field="SALESORDERSUBCODEPRINT" sortable="true" width="10%">客户订单号</th>
					<th field="PLANCODE" sortable="true" width="12%">计划单号</th>
					<!-- <th field="CATEGORYNAME" sortable="true" width="10%">成品类别名称</th>
					<th field="CATEGORYCODE" sortable="true" width="10%">成品类别代码</th>  -->
				</tr>
			</thead>
			 <thead>
				<tr>
				    <th field="FACTORYPRODUCTNAME" sortable="true" width="15%">厂内名称</th>
					<th field="CONSUMERNAME" sortable="true" width="15%">客户名称</th>
					<th field="PRODUCTMODEL" sortable="true" width="15%">产品规格</th>
					<th field="BATCHCODE" sortable="true" width="6%">批次号</th>
					<th field="PRODUCTWIDTH" sortable="true" width="6%" >门幅(mm)</th>
					<th field="PRODUCTLENGTH" sortable="true" width="6%">米长(m)</th>
					<th field="OLDSALESORDERCODE" sortable="true" width="8%">旧订单号</th>
					<th field="OLDBATCHCODE" sortable="true" width="6%" >批次号</th>
					<th field="OLDPRODUCTWIDTH" sortable="true" width="6%" >门幅(mm)</th>
					<th field="OLDPRODUCTLENGTH" sortable="true" width="6%">米长(m)</th>
					<th field="OLDCONSUMERNAME" sortable="true" width="15%">客户名称</th>
					<th field="OLDPRODUCEPLANCODE" sortable="true" width="12%">计划单号</th>
					<th field="OLDPRODUCTMODEL" sortable="true" width="15%">产品规格</th>
					<th field="PRODUCTWEIGHT" sortable="true" width="6%" formatter="formatterWeight">重量(kg)</th>
					<!-- <th field="ISLOCKED" width="10%" formatter="formatterIslock">冻结状态</th> -->
					<!-- <th field="WAREHOUSECODE" sortable="true" width="15">库房</th>
					<th field="WAREHOUSENAME" sortable="true" width="10%">库房</th> -->
					<!-- <th field="WAREHOUSEPOSCODE" sortable="true" width="10%">库位</th> -->
					<th field="OPTUSERNAME" sortable="true" width="6%">操作人</th>
					<th field="OUTDATE" sortable="true" width="10%">领出时间</th>
					<th field="OUTADDRESS" sortable="true" width="6%">领出车间</th>
				</tr>
			</thead>
		</table> 
	</div>
</body>