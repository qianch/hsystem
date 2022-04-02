<!--
	作者:高飞
	日期:2016-10-12 11:06:09
	页面:原料JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>原料</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="material.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
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
				<form action="#" id="materialSearchForm" autoSearchFunction="false">
					物料代码：<input type="text" name="filter[materialCode]" like="true" class="easyui-textbox">
					产品大类：<input type="text" name="filter[produceCategory]" like="true" class="easyui-textbox">
					规格型号：<input type="text" name="filter[materialModel]" like="true" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"  url="${path}material/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="false" fit="true" data-options="onDblClickRow:dbClickEdit">
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="MATERIALCODE" width="150">物料代码</th>
					<th field="PRODUCECATEGORY" width="120">产品大类</th>
					<th field="MATERIALMODEL" width="160">规格型号</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="UPPERDEVIATION" width="80">上偏差</th>
					<th field="LOWERDEVIATION" width="80">下偏差</th>
					<th field="SUBWAY" width="90">接头方式</th>
					<th field="MATERIALMEASURE" width="80">计量单位</th>
					<th field="MATERIALMINSTOCK" width="80">最低库存(KG)</th>
					<th field="MATERIALMAXSTOCK" width="80">最大库存(KG)</th>
					<th field="WEIGHT" width="80" formatter="stockFormatter">当前库存</th>
					<th field="MATERIALSHELFLIFE" width="90">保质期(天)</th>
					<th field="MADERATE" width="90">制成率(%)</th>
					<th field="MATERIALMEMO" width="100">备注</th>
			</thead>
		</table>
	</div>
</body>