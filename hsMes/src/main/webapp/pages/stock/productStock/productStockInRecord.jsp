<!--
	作者:宋黎明
	日期:2016-11-14 15:08:20
	页面:成品库存表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>成品入库记录表</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="productStockInRecord.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<%-- <jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="excel" name="ids"/>
				<jsp:param value="platform-icon9" name="icons"/>
				<jsp:param value="exportExcel()" name="funs"/>
				<jsp:param value="导出" name="names"/>
			</jsp:include> --%>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="productInRecordSearchForm" autoSearchFunction="false">
					仓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;库:<input type="text" class="easyui-combobox" name="filter[warehouseCode]"like="true"
				data-options="valueField:'warehouseCode',textField:'warehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=cp',onSelect:filter">
					库&nbsp;&nbsp;&nbsp;&nbsp;位:<input type="text" name="filter[warehousePosCode]" like="true" class="easyui-textbox">
					条&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:<input type="text" name="filter[code]" like="true" class="easyui-textbox">
					生产车间:<input type="text" id="workShopCode" class="easyui-combobox" name="filter[workShopCode]"
								data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=cut,weave',onSelect:filter">
      				  成品类别名称：<input type="text" name="filter[categoryName]" class="easyui-textbox">
      				  </br>
				 订&nbsp;&nbsp;单&nbsp;号:<input type="text" name="filter[salesCode]" like="true" class="easyui-textbox">
				 批次号:<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox">
					客&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;户:<input type="text" name="filter[consumer]" like="true" class="easyui-textbox">
					工艺名称:<input type="text" name="filter[model]" like="true" class="easyui-textbox">
					成品类别代码：<input type="text" like="true" name="filter[categoryCode]" class="easyui-textbox">
					<br>
					入库时间:<input type="text" id="start" name="filter[start]"  class="easyui-datetimebox">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;至:<input type="text" name="filter[end]"  class="easyui-datetimebox">
					  厂内名称:<input type="text" name="filter[factoryProductName]" class="easyui-textbox">
<%--					  库存状态：<input type="text" id="stockState" name="filter[stockState]"--%>
<%--								class="easyui-combobox"--%>
<%--								required="true"--%>
<%--								data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=stockStatus'">--%>
					条码类型:<input type="text" id="stockType" name="filter[stockType]"
								class="easyui-combobox"
								required="true"
								data-options="valueField:'v',textField:'t',url:'<%=basePath %>dict/queryDict?rootcode=stockType,alloptions'">&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel" onclick="exportExcel()">
						导出
					</a>
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" showFooter="true" title="" class="easyui-datagrid"   toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true" data-options="onLoadSuccess:onLoadSuccess">

			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="BARCODE" sortable="true" width="10%">条码号</th>
					<th field="SALESORDERCODE" sortable="true" width="10%">订单号</th>
					<th field="CATEGORYNAME" sortable="true" width="10%">成品类别名称</th>
					<th field="CATEGORYCODE" sortable="true" width="10%">成品类别代码</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="SCRW" sortable="true" width="10%">生产任务单编号</th>
					<th field="FACTORYPRODUCTNAME" sortable="true" width="10%">厂内名称</th>
					<!-- <th field="FACTORYPRODUCTNAME" sortable="true" width="10%">厂内名称</th> -->
					<th field="CONSUMERPRODUCTNAME" sortable="true" width="10%">客户产品名称</th>
					<th field="TCPROCBOMVERSIONPARTSNAME" sortable="true" width="10%">部件名称</th>
					<th field="PRODUCTMODEL" sortable="true" width="10%">产品规格</th>
					<th field="BATCHCODE" sortable="true" width="10%">批次号</th>
					<th field="CONSUMERNAME" sortable="true" width="10%">客户</th>
					<th field="PRODUCTLENGTH" sortable="true" width="10%">米长(m)</th>
					<th field=PRODUCTWIDTH sortable="true" width="10%">门幅(mm)</th>
					<th field="WEIGHT" sortable="true" width="10%" >重量(kg)</th>
					<th field="WAREHOUSEPOSCODE" sortable="true" width="10%">库位</th>
					<th field="WAREHOUSENAME" sortable="true" width="10%">仓库</th>
					<th field="WORKSHOP" sortable="true" width="10%">生产车间</th>
					<th field="OPERATEUSERNAME" sortable="true" width="10%">操作人</th>
					<th field="INTIME" sortable="true" width="10%">入库时间</th>
				</tr>
			</thead>
		</table>
	</div>
</body>
