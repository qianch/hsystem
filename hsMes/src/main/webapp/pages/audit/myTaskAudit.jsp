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
	<%@ include file="myTaskAudit.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<jsp:include page="../base/toolbar.jsp">
				<jsp:param value="view" name="ids"/>
				<jsp:param value="icon-edit" name="icons"/>
				<jsp:param value="查看审核" name="names"/>
				<jsp:param value="view()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="auditInstanceSearchForm" autoSearchFunction="false">
					审核是否完成:<input type="text" name="filter[iscompleted]" 
								class="easyui-combobox"
								data-options=" valueField: 'id',
      				  textField: 'text',data:[{'id':'3','text':'全部'},{'id':'1','text':'完成'},{'id':'2','text':'未完成'}],onSelect:filter">
					审核是否通过:<input type="text" name="filter[finalresult]" 
								class="easyui-combobox"
								data-options=" valueField: 'id',
      				  textField: 'text',data:[{'id':'3','text':'全部'},{'id':'2','text':'通过'},{'id':'-1','text':'不通过'}],onSelect:filter">
					
				</form>
			</div>
		</div>
		<table id="dg_ins" singleSelect="true" class="easyui-datagrid"  url="${path}audit/myTask/" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:view">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="CREATETIME" sortable="true" width="15">创建时间</th>
					<th field="AUDITCODE" hidden="true" sortable="true" width="15">auditcode</th>
					<th field="FORMID"hidden="true"  sortable="true" width="15">formid</th>
					<th field="AUDITTITLE" sortable="true" width="15">流程标题</th>
					<th field="CURRENTAUDITPROCESSNODE" sortable="true" width="15" formatter="formatNode">当前节点</th>
					<th field="ISCOMPLETED" sortable="true" width="15"formatter="formatIsfinish" >审核是否完成</th>
					<th field="FINALRESULT" sortable="true" width="15" formatter="resultFormatter">审核结果</th>
					<th field="CREATEUSER" hidden='true' sortable="true" width="15">提审人员</th>
				</tr>
			</thead>
		</table>
	</div>
</body>