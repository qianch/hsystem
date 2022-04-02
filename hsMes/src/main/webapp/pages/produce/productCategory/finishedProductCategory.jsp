<!--
	作者:king
	日期:2017-8-2 10:39:01
	页面:成品类别管理JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>成品类别管理</title>
  	<%@ include file="/pages/base/meta.jsp" %>
	<%@ include file="finishedProductCategory.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<jsp:include page="/pages/base/toolbar.jsp">
				<jsp:param value="add" name="ids"/>
				<jsp:param value="edit" name="ids"/>
				<jsp:param value="icon-add" name="icons"/>
				<jsp:param value="icon-edit" name="icons"/>
				<jsp:param value="增加" name="names"/>
				<jsp:param value="编辑" name="names"/>
				<jsp:param value="add()" name="funs"/>
				<jsp:param value="edit()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="finishedProductCategorySearchForm" autoSearchFunction="false">
					
					
					<label class="panel-title">类别代码：</label>
					<input type="text" name="filter[categoryCode]" like="true" class="easyui-textbox">
					
					<label class="panel-title">类别名称：</label>
					<input type="text" name="filter[categoryName]" like="true" class="easyui-textbox">
					
					
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="category_dg" singleSelect="false" title="成品类别管理列表" class="easyui-datagrid"  url="${path}product/category/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID"  checkbox=true ></th>
					<th field="CATEGORYCODE"  sortable="true" width="15">成品类别代码</th>
					<th field="CATEGORYNAME" sortable="true" width="15">成品类别名称</th>
					<th field="CREATER" sortable="true" width="15">创建人</th>
					<th field="CREATETIME" sortable="true" width="15">创建时间</th>
					<th field="MODIFYUSER" sortable="true" width="15">修改人</th>
					<th field="MODIFYTIME" sortable="true" width="15">修改时间</th>
				</tr>
			</thead>
		</table>
	</div>
</body>