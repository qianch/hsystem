<!--
	作者:宋黎明
	日期:2016-11-27 13:57:45
	页面:出货计划JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>生产计划明细</title>
  	<%@ include file="../../../base/meta.jsp" %>
	<%@ include file="producePlanDetailPrint.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow:auto;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<jsp:include page="../../../base/toolbar.jsp">
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
				<form action="#" id="producePlanSearchForm" autoSearch="true" autoSearchFunction="false">
					任务单号：<input type="text" name="filter[producePlanCode]" like="true" class="easyui-textbox" style="width:200px;">
					订单号：<input type="text" name="filter[salesOrderCode]" like="true" class="easyui-textbox" style="width:150px;">
					批次号：<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox" style="width:150px;">
					厂内名称：<input type="text" name="filter[factoryProductName]" like="true" class="easyui-textbox" style="width:230px;">
					客户名称：<input type="text" name="filter[consumerName]" like="true" class="easyui-textbox" style="width:230px;">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="producePlanDetailfilter()">
						搜索
					</a>
				</form>
			</div>

			<a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onclick="editProducePlanDetailPrints()">修改打印记录</a>

		</div>
		<table id="producePlanDetaildg"  singleSelect="true" title="" class="easyui-datagrid" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  >
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="PRODUCEPLANCODE" width="180">任务单号</th>
					<th field="ISTURNBAGPLAN" width="60">计划类型</th>
					<th field="SALESORDERCODE" width="130" >订单号</th>
					<th field="SALESORDERSUBCODEPRINT" width="100" >客户订单号</th>
					<th field="BATCHCODE" width="100" editor="{type:'textbox',options:{required:true}}" >批次号</th>

					<th field="PRODUCTMODEL" width="130" >产品规格</th>
					<th field="CONSUMERPRODUCTNAME" width="130" >客户产品名称</th>
					<th field="FACTORYPRODUCTNAME" width="130" >厂内名称</th>
					<th field="CONSUMERSIMPLENAME" width="100" >客户简称</th>

					<th field="PRODUCTWIDTH" width="80">门幅(mm)</th>
					<th field="PRODUCTWEIGHT" width="80">卷重(kg)</th>
					<th field="PRODUCTLENGTH" width="80">卷长(m)</th>




				</tr>
			</thead>
		</table>
	</div>
</body>
