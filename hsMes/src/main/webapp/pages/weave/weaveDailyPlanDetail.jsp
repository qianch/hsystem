<!--
	作者:肖文彬
	日期:2016-11-24 14:11:43
	页面:日计划和编织计划表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>日计划和编织计划表</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="weaveDailyPlanDetail.js.jsp" %>
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
				<form action="#" id="weaveDailyPlanDetailSearchForm" autoSearchFunction="false">
					
					
					<label class="panel-title">搜索：</label>
					<input type="text" name="filter[**]" like="true" class="easyui-textbox">
					
					
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="日计划和编织计划表列表" class="easyui-datagrid"  url="${path}weaveDailyPlanDetail/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="WEAVEPLANID" sortable="true" width="15">编织计划id</th>
					<th field="DAILYID" sortable="true" width="15">日计划id</th>
				</tr>
			</thead>
		</table>
	</div>
</body>