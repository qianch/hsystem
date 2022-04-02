<!--
	作者:肖文彬
	日期:2016-9-28 11:24:47
	页面:客户管理JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>客户管理</title>
  	<%@ include file="../../base/meta.jsp" %>
  	<script type="text/javascript" src="<%=basePath%>resources/easyui/extentions/datagrid-dnd.js"></script>
	<%@ include file="consumer.js.jsp" %>
	
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: false;position: relative; height: 140px; width: 925px">
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
				<jsp:param value="作废" name="names"/>
				<jsp:param value="add()" name="funs"/>
				<jsp:param value="edit()" name="funs"/>
				<jsp:param value="old()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<!-- <form action="#" id="consumerSearchForm" autoSearchFunction="false">
					客户代码：<input type="text" id="code" name="filter[code]" like="true" class="easyui-textbox">
					客户名称：<input type="text" name="filter[name]" like="true" class="easyui-textbox">
					客户大类：<input type="text" name="filter[type]" like="true" class="easyui-combobox" panelHeight="auto" editable="false" data-options="data: [
	                        {value:'1',text:'国内'},
	                        {value:'2',text:'国外'}
                    	] ,icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]">
				</br>
					客户等级：<input type="text" name="filter[level]" like="true" class="easyui-combobox" panelHeight="auto" editable="false" data-options="data: [
	                        {value:'1',text:'A'},
	                        {value:'2',text:'B'},
	                        {value:'3',text:'C'}
                    	],icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$(e.data.target).combobox('clear');
					}
				}]">
					备&nbsp;&nbsp;注：<input type="text" name="filter[memo]" like="true"
			class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form> -->
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" url="${path}consumer/list", class="easyui-datagrid"  toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="false" fit="true" data-options="onDblClickRow:dbClickEdit,onLoadSuccess:dgLoadSuccess,remoteFilter:true">
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="CONSUMERCODE" width="30" sortable="true">客户代码</th>
					<th field="CONSUMERNAME" width="120" sortable="true">客户名称</th>
					<th field="CONSUMERSIMPLENAME" width="120" sortable="true">客户简称</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="CONSUMERGRADE" width="60" formatter="formatterLevel" sortable="true">客户等级</th>
					<th field="CONSUMERCATEGORY" width="60" formatter="formatterType" sortable="true">客户大类</th>
					<th field="CONSUMERCONTACT" width="80" sortable="true">客户联系人</th>
					<th field="CONSUMERADDRESS" width="80" sortable="true">客户地址</th>
					<th field="CONSUMERPHONE" width="80" sortable="true">联系电话</th>
					<th field="CONSUMEREMAIL" width="80" sortable="true">邮件地址</th>
					<th field="CONSUMERCODEERP" width="60" sortable="true">客户ERP代码</th>
					<th field="CONSUMERMEMO" width="100" sortable="true">备注</th>
				</tr>
			</thead>
		</table>
	</div>
</body>