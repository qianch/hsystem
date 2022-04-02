<!--
	作者:徐波
	日期:2016-10-24 15:08:20
	页面:成品库存表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>成品库存表</title>
<%@ include file="../../base/meta.jsp"%>
<%@ include file="productStock.js.jsp"%>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: false;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="productStockSearchForm" autoSearchFunction="false">
					终点仓库：<input type="text" class="easyui-combobox" name="filter[warehouseCode]" panelHeight="250" data-options="valueField:'v',textField:'t',method:'get',url:'<%=basePath%>warehouse/queryWarehousebyType?waretype=cpdrk,cpbdk,cpwk'">
					仓库类型：<input type="text" id="wareType" name="filter[wareType]"
													 class="easyui-combobox"
													 required="true"
													 data-options="valueField:'v',textField:'t',method:'get',url:'<%=basePath %>warehouse/queryWarehouseType?rootcode=WareType'">
					库位：<input type="text" class="easyui-textbox" name="filter[warehousePosCode]" like="true">
                    条码：<input type="text" class="easyui-textbox" name="filter[code]"  like="true"><br>
				类别名称：<input type="text" class="easyui-textbox" name="filter[categoryName]" >
				厂内名称：<input type="text" class="easyui-textbox" name="filter[factoryProductName]" like="true" >
				规格型号：<input type="text" class="easyui-textbox" name="filter[model]" like="true">
                　订单号：<input type="text" class="easyui-textbox" like="true" name="filter[salesCode]" ><br>
                　批次号：<input type="text" class="easyui-textbox" name="filter[batchCode]" like="true" >
				工艺版本：<input type="text" class="easyui-textbox" name="filter[processBomVersion]"  like="true">
                                工艺代码：<input type="text" class="easyui-textbox" name="filter[processBomCode]" like="true">
				包装代码：<input type="text" class="easyui-textbox" name="filter[bcBomCode]" like="true"><br>
				包装版本：<input type="text" class="easyui-textbox" name="filter[bcBomVersion]" like="true">

			入库时间：<input type="text" id="start" name="filter[start]" class="easyui-datetimebox">
				　　　至：<input
						type="text" name="filter[end]" class="easyui-datetimebox"><br>
						冻结状态：<input type="text" name="filter[isLocked]"  class="easyui-combobox"  data-options="data: [
		                        {value:'-1',text:'正常'},
		                        {value:'1',text:'冻结'}]"> 
					 	所属客户：<input type="text" class="easyui-textbox" name="filter[consumer]" like="true" >
				库存状态：<input type="text" id="stockState" name="filter[stockState]"
							class="easyui-combobox"
							required="true"
							data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=stockState'">
					条码类型：<input type="text" id="stockType" name="filter[stockType]"
								class="easyui-combobox"
								required="true"
								data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=stockType'">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" 
						iconcls="icon-search" onclick="filter()"> 搜索 </a>&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel" onclick="exportDetail()">
						导出
					</a>	
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid" toolbar="#toolbar"
			pagination="true" rownumbers="true" fitColumns="false" fit="true" remoteSort="true"
			data-options="rowStyler:rowStyler,showFooter:true,onBeforeLoad:setTotal,onLoadSuccess:onLoadSuccess">
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="BARCODE" width="120">条码号</th>
					<th field="SALESORDERCODE"  width="130" formatter="orderFormatter">订单号</th>
					<th field="SALESORDERSUBCODEPRINT"  width="130" >客户订单号</th>
					<th field="BATCHCODE"  width="100" formatter="batchFormatter">批次号</th>
					<th field="PLANDELIVERYDATE"  width="100" formatter="deliveryFormatter">发货日期</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="CATEGORYNAME"  width="130" >成品类别名称</th>
					<th field="CATEGORYCODE"  width="130" >成品类别代码</th>
					<th field="FACTORYPRODUCTNAME"  width="130" formatter="modelFormatter">厂内名称</th>
					<th field="PRODUCTPROCESSCODE"  width="130">工艺代码</th>
					<th field="PRODUCTPROCESSBOMVERSION"  width="70">工艺版本</th>
					<th field="PRODUCTPACKAGINGCODE"  width="130" >包装代码</th>
					<th field="PRODUCTPACKAGEVERSION"  width="70">包装版本</th>
					<th field="CONSUMERPRODUCTNAME"  width="130" formatter="modelFormatter">客户产品名称</th>
					<th field="TCPROCBOMVERSIONPARTSNAME"  width="130">部件名称</th>
					<th field="PRODUCTMODEL"  width="130" formatter="modelFormatter">产品规格</th>
					<th field="CONSUMERNAME"  width="150" >客户名称</th>
					<th field="WEIGHT"  width="70" formatter="processNumberFormatter">重量(KG)</th>
					<th field="WAREHOUSENAME"  width="100" formatter="warehouseFormatter">终点仓库名称</th>
					<th field="WAREHOUSECODE"  width="100" formatter="warehouseFormatter">终点仓库编码</th>
					<th field="WARETYPE" width="80" data-options="formatter:function(value,row,index){return dictFormatter('WareType',value)}" >仓库类型</th>
					<th field="WAREHOUSEPOSCODE"  width="80">终点库位</th>
					<th field="WAREHOUSEPOSCODE"  width="80">终点库位</th>
					<th field="INTIME" sortable="true" width="140">入库时间</th>
					<th field="ROLLOUTPUTTIME" sortable="true" width="150">生产时间</th>
					<th field="DAYS"  width="60" formatter="inDays">在库天数</th>
					<th field="PRODUCTSHELFLIFE"  width="60">保质期(天)</th>
					<th field="STOCKSTATE"  width="70" formatter="formatterState">状态</th>
					<th field="ISLOCKED"  width="70" formatter="lockStateFormatter">冻结状态</th>
					<th field="ROLLQUALITYGRADECODE"  width="70">质量等级</th>
					<th field="ROLLCOUNTINTRAY"  width="70" >卷数</th>
				</tr>
			</thead>
		</table>
	</div>
</body>