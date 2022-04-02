<!--
	作者:徐波
	日期:2017-2-13 14:10:25
	页面:原料强制出库JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>原料强制出库</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="materialForceOutRecord.js.jsp" %>
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
				<form action="#" id="materialForceOutRecordSearchForm" autoSearchFunction="false">
				托盘编号:<input type="text" name="filter[code]" like="true" class="easyui-textbox">
					产品规格:<input type="text" name="filter[materialModel]" like="true" class="easyui-textbox">
					仓&nbsp;&nbsp;库:<input type="text" class="easyui-combobox" name="filter[warehouseCode]"like="true" 
				data-options="valueField:'warehouseCode',textField:'warehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=ycl',onSelect:filter"> 
					库位:<input type="text" name="filter[warehousePosCode]" like="true" class="easyui-textbox">
					</br>
					生产日期:<input type="text" id="produceStart" name="filter[produceStart]" 
								value="" class="easyui-datetimebox">
					至:&nbsp;<input type="text" id="produceEnd" name="filter[produceEnd]"
								 value="" class="easyui-datetimebox">
								 
					出库时间:<input type="text" id="outStart" name="filter[outStart]" 
								value="" class="easyui-datetimebox">
					至:&nbsp;<input type="text" id="outEnd" name="filter[outEnd]"
								 value="" class="easyui-datetimebox">
								 <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel" onclick="exportMaterialForceOutRecord()">
						导出
					</a></br>
								 
					产品大类:<input type="text" name="filter[produceCategory]" like="true" class="easyui-textbox">&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
						onclick="filter()"> 搜索 </a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"  url="${path}materialForceOutRecord/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="MATERIALCODE" sortable="true" width="20">托盘编号</th>
					<th field="MATERIALMODEL" sortable="true" width="23">产品规格</th>
					<th field="PRODUCECATEGORY" sortable="true" width="15">产品大类</th>
					<th field="STATE" sortable="true" width="10" formatter="formatterState">状态</th>
					<th field="ISPASS" sortable="true" width="10" formatter="formatterIspass">是否放行</th>
					<th field="ISLOCKED" sortable="true" width="10" formatter="formatterIslock">冻结状态</th>
					<!-- <th field="WAREHOUSECODE" sortable="true" width="15">库房</th> -->
					<th field="WAREHOUSENAME" sortable="true" width="13">库房</th>
					<th field="WAREHOUSEPOSCODE" sortable="true" width="10">库位</th>
					<th field="OUTADDRESS" sortable="true" width="10">出库地址</th>
					<th field="OUTUSER" sortable="true" width="10">出库人</th>
					<th field="OUTTIME" sortable="true" width="23">出库时间</th>
					<th field="INWEIGHT" sortable="true" width="9" formatter="processNumberFormatter">重量(kg)</th>
					<th field="SUBWAY" sortable="true" width="11">接头方式</th>
					<th field="NUMBERDEVIATION" sortable="true" width="11">号数偏差</th>
					<th field="PRODUCEDATE" sortable="true" width="20">生产日期</th>
				</tr>
			</thead>
		</table>
	</div>
</body>