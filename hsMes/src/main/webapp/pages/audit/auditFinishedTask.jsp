<!--
	作者:高飞
	日期:2016-10-25 13:52:50
	页面:流程实例JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>流程实例</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="auditFinishedTask.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="auditInstanceSearchForm" autoSearchFunction="false">
					
					标题:<input type="text" name="filter[auditTitle]" like="true" class="easyui-textbox">
					提审人员:<input type="text" name="filter[userName]" like="true" class="easyui-textbox">
					创建时间:<input type="text" name="filter[createTime]" like="true" class="easyui-datebox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" class="easyui-datagrid"  url="${path}audit/task/finished" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true" data-options="onLoadSuccess:loadSuccess,onDblClickRow:dbClickEdit" >
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="CREATETIME" sortable="true" width="15">创建时间</th>
					<th field="AUDITTITLE" sortable="true" width="15">流程标题</th>
					<!-- <th field="AUDITCODE" sortable="true" width="15">流程代码</th> -->
					<th field="USERNAME" sortable="true" width="15">提审人员</th>
					<th field="AUDITLEVEL" sortable="true" width="15" formatter="formatNode">审核节点</th>
					<th field="AUDITTIME" sortable="true" width="15" >审核时间</th>
					<th field="AUDITRESULT" sortable="true" width="15" formatter="resultFormatter">审核结果</th>
				</tr>
			</thead>
		</table>
	</div>
</body>