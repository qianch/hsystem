<!--
	作者:徐波
	日期:2017-2-13 14:10:25
	页面:原料强制出库JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>成品强制出库</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="productForceOutRecord.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<jsp:include page="../base/toolbar.jsp">
				<jsp:param value="add" name="ids"/>
				<jsp:param value="edit" name="ids"/>
				<jsp:param value="delete" name="ids"/>
				<jsp:param value="icon-add" name="icons"/>
				<jsp:param value="icon-edit" name="icons"/>
				<jsp:param value="icon-remove" name="icons"/>
				<jsp:param value="增加" name="names"/>
				<jsp:param value="编辑" name="names"/>
				<jsp:param value="删除" name="names"/>
				<jsp:param value="add()" name="funs"/>
				<jsp:param value="edit()" name="funs"/>
				<jsp:param value="doDelete()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="productForceOutRecordSearchForm" autoSearchFunction="false">
					仓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;库:<input type="text" class="easyui-combobox" name="filter[warehouseCode]"like="true"
				data-options="valueField:'warehouseCode',textField:'warehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=cp',onSelect:filter">
					库&nbsp;&nbsp;&nbsp;&nbsp;位:<input type="text" name="filter[warehousePosCode]" like="true" class="easyui-textbox">
					条&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:<input type="text" name="filter[code]" like="true" class="easyui-textbox">
					出库车间：<input type="text" id="workShopCode" class="easyui-combobox" name="filter[workShopCode]"
										 data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=cut,weave',onSelect:filter">
      				  成品类别名称：<input type="text" name="filter[categoryName]" class="easyui-textbox">
      				  </br>
				 订&nbsp;&nbsp;单&nbsp;号:<input type="text" name="filter[salesCode]" like="true" class="easyui-textbox">
				 批次号:<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox">
					客&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;户:<input type="text" name="filter[consumer]" like="true" class="easyui-textbox">
					产品规格:<input type="text" name="filter[model]" like="true" class="easyui-textbox">
					成品类别代码：<input type="text" name="filter[categoryCode]" class="easyui-textbox">
					<br>
					出库时间:<input type="text" name="filter[start]"  class="easyui-datetimebox">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;至:<input type="text" name="filter[end]"  class="easyui-datetimebox">
					  厂内名称:<input type="text" name="filter[factoryName]" like="true" class="easyui-textbox">&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel" onclick="exportExcel()">
						导出
					</a>
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"  toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  >
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="BARCODE" sortable="true" width="10%">产品条码</th>
					<th field="SALESORDERCODE" sortable="true" width="10%">订单号</th>
					<th field="CATEGORYNAME" sortable="true" width="10%">成品类别名称</th>
					<th field="CATEGORYCODE" sortable="true" width="10%">成品类别代码</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="SCRW" sortable="true" width="12%">生产任务单编号</th>
					<th field="FACTORYNAME" sortable="true" width="18%">厂内名称</th>
					<th field="CONSUMERPRODUCTNAME" sortable="true" width="15%">客户产品名称</th>
					<th field="TCPROCBOMVERSIONPARTSNAME" sortable="true" width="15%">部件名称</th>
					<th field="PRODUCTMODEL" sortable="true" width="10%">产品规格</th>
					<th field="BATCHCODE" sortable="true" width="10%">批次号</th>
					<th field="CONSUMERNAME" sortable="true" width="10%">客户</th>
					<th field="PRODUCTLENGTH" sortable="true" width="10%">米长(m)</th>
					<th field="PRODUCTWIDTH" sortable="true" width="10%" >门幅(mm)</th>
					<th field="PRODUCTWEIGHT" sortable="true" width="10%">重量(kg)</th>
					<th field="ISLOCKED" width="10%" formatter="formatterIslock">冻结状态</th>
					<!-- <th field="WAREHOUSECODE" sortable="true" width="15">库房</th> -->
					<th field="WAREHOUSENAME" sortable="true" width="10%">库房</th>
					<th field="WAREHOUSEPOSCODE" sortable="true" width="10%">库位</th>
					<th field="OUTADDRESS" sortable="true" width="10%">出库地址</th>
					<th field="OUTUSER" sortable="true" width="10%">出库人</th>
					<th field="OUTTIME" sortable="true" width="15%">出库时间</th>
				</tr>
			</thead>
		</table>
	</div>
</body>