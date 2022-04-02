<!--
	作者:高飞
	日期:2019-4-15 18:44:42
	页面:西门子裁剪车间机台打印任务JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>西门子裁剪车间机台打印任务</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="cutTaskOrderPrintTask.js.jsp" %>
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
				<form action="#" id="cutTaskOrderPrintTaskSearchForm" autoSearchFunction="false">
					
					
					<label class="panel-title">搜索：</label>
					<input type="text" name="filter[**]" like="true" class="easyui-textbox">
					
					
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="西门子裁剪车间机台打印任务列表" class="easyui-datagrid"  url="${path}cutTaskOrderPrintTask/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="IP" sortable="true" width="15">计算机IP</th>
					<th field="CTOCODE" sortable="true" width="15">派工单号</th>
					<th field="DRAWINGNO" sortable="true" width="15">图号</th>
					<th field="CURRENTPRINTORDER" sortable="true" width="15">当前打印序号</th>
					<th field="LEVECOUNT" sortable="true" width="15">层数</th>
				</tr>
			</thead>
		</table>
	</div>
</body>