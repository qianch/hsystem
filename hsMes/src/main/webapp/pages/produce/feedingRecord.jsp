<!--
	作者:徐波
	日期:2017-8-3 9:06:25
	页面:投料记录JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>投料记录</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="feedingRecord.js.jsp" %>
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
				<form action="#" id="feedingRecordSearchForm" autoSearchFunction="false">
					
					
					<label class="panel-title">搜索：</label>
					<input type="text" name="filter[**]" like="true" class="easyui-textbox">
					
					
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="投料记录列表" class="easyui-datagrid"  url="${path}feedingRecord/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="DEVICECODE" sortable="true" width="15">机台ID</th>
					<th field="OPERATEUSERID" sortable="true" width="15">操作人ID</th>
					<th field="MATERIALCODE" sortable="true" width="15">原料条码</th>
					<th field="ROLLCODE" sortable="true" width="15">卷条码</th>
					<th field="WEAVEID" sortable="true" width="15">编织计划ID</th>
					<th field="CUTID" sortable="true" width="15">裁减计划ID</th>
					<th field="FEEDINGDATE" sortable="true" width="15">投料日期</th>
				</tr>
			</thead>
		</table>
	</div>
</body>