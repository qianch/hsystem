<!--
	作者:高飞
	日期:2017-7-25 10:39:09
	页面:组别管理JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>组别管理</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="cutGroup.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
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
				<form action="#" id="cutGroupSearchForm" autoSearchFunction="false">
					组别:<input type="text" name="filter[groupName]" like="true" class="easyui-textbox">
					机长:<input type="text" name="filter[groupLeader]" like="true" class="easyui-textbox">
					班别:<input type="text" name="filter[groupType]" like="true" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="组别管理列表" class="easyui-datagrid"  url="${path}cutGroup/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="GROUPNAME" sortable="true" width="15">组别</th>
					<th field="GROUPLEADER" sortable="true" width="15">机长</th>
					<th field="GROUPTYPE" sortable="true" width="15">班别</th>
					<th field="CREATEUSER" sortable="true" width="15">创建人</th>
					<th field="CREATETIME" sortable="true" width="15">创建时间</th>
					<th field="MODIFYUSER" sortable="true" width="15">修改人</th>
					<th field="MODIFYTIME" sortable="true" width="15">修改时间</th>
				</tr>
			</thead>
		</table>
	</div>
</body>