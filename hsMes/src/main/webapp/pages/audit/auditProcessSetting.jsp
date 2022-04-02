<!--
	作者:高飞
	日期:2016-10-24 14:51:44
	页面:流程设置JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>流程设置</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="auditProcessSetting.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<jsp:include page="../base/toolbar.jsp">
				<jsp:param value="add" name="ids"/>
				<jsp:param value="edit" name="ids"/>
				<jsp:param value="icon-add" name="icons"/>
				<jsp:param value="icon-edit" name="icons"/>
				<jsp:param value="增加" name="names"/>
				<jsp:param value="编辑" name="names"/>
				<jsp:param value="add()" name="funs"/>
				<jsp:param value="edit()" name="funs"/>
			</jsp:include>
		</div>

		<table id="dg" singleSelect="false"  class="easyui-datagrid"  url="${path}audit/setting/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="AUDITCODE" sortable="true" width="10">流程代码</th>
					<th field="AUDITNAME" sortable="true" width="15">流程名称</th>
					<th field="AUDITLEVEL" sortable="true" width="10" formatter="levelFormatter">流程等级</th>
					<th field="AUDITFIRSTLEVELUSERS" sortable="true" width="25" formatter="userFormatter1">一级审核人员</th>
					<th field="AUDITSECONDLEVELUSERS" sortable="true" width="25" formatter="userFormatter2">二级审核人员</th>
				</tr>
			</thead>
		</table>
	</div>
</body>