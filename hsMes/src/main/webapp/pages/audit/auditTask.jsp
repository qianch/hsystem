<!--
	作者:高飞
	日期:2016-10-25 13:52:50
	页面:流程实例JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>我的审核</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="auditTask.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<jsp:include page="../base/toolbar.jsp">
				<jsp:param value="edit" name="ids"/>
				<jsp:param value="icon-edit" name="icons"/>
				<jsp:param value="审核" name="names"/>
				<jsp:param value="edit()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="auditInstanceSearchForm" autoSearchFunction="false">
					
					
					<label class="panel-title">搜索：</label>
					标题:<input type="text" name="filter[auditTitle]" like="true" class="easyui-textbox">
					提审人员:<input type="text" name="filter[userName]" like="true" class="easyui-textbox">
					创建时间:<input type="text" name="filter[createTime]" like="true" class="easyui-datebox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" class="easyui-datagrid"  url="${path}audit/task" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="CREATETIME" sortable="true" width="15">创建时间</th>
					<th field="AUDITTITLE" sortable="true" width="15">流程标题</th>
					<th field="CURRENTAUDITPROCESSNODE" sortable="true" width="15" formatter="formatNode">当前节点</th>
					<th field="USERNAME" sortable="true" width="15">提审人员</th>
				</tr>
			</thead>
		</table>
	</div>
</body>