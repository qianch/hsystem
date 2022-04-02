<!--
	作者:宋黎明
	日期:2016-9-29 11:46:46
	页面:设备信息JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>设备信息</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="device.js.jsp" %>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/zTree_v3/css/zTreeStyle/zTreeStyle.css">
	<script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.exedit-3.5.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/ext/zTree.ext.js"></script>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	   <div region="west" split="true" title="设备类别" style="width:200px;">
			<ul id="deviceTree" class="ztree"></ul>
		</div>
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<jsp:include page="../../base/toolbar.jsp">
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
				<form action="#" id="deviceSearchForm" autoSearchFunction="false">
					<label class="panel-title">搜索：</label>
					<input type="hidden" id="id" name="filter[id]" in="true">
					设备资产编号：<input type="text" name="filter[deviceAssetCode]" like="true" class="easyui-textbox">
					设备代码：<input type="text" name="filter[deviceCode]" like="true" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="设备信息列表" class="easyui-datagrid"  url="${path}device/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="false" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="DEVICEASSETCODE" sortable="true" width="100">设备资产编号</th>
					<th field="SPECMODEL" sortable="true" width="100">规格型号</th>
					<th field="DEVICECODE" sortable="true" width="100">设备代码</th>
					<th field="DEVICENAME" sortable="true" width="100">资产名称</th>
					<!-- <th field="DEVICECATETORYID" width="15">设备类别</th> -->
					<th field="DEVICEUNIT" sortable="true" width="100">单位</th>
					<th field="DEVICECOUNT" sortable="true" width="100">数量</th>
					<th field="DEVICESUPPLIER" sortable="true" width="100">供应商</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="DEVICEUSINGDATE" sortable="true" width="150">使用时间</th>
					<th field="DEPTNAME" sortable="true" width="100">所属部门</th>
					<th field="MACHINESCREENIP" sortable="true" width="100">机台屏幕ip</th>
					<th field="MACHINESCREENNAME" sortable="true" width="100">机台屏幕名称</th>
					<th field="PRODUCTIONSALEORDERCODE" sortable="true" width="100">正在生产的订单</th>
				</tr>
			</thead>
		</table>
	</div>
</body>