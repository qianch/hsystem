<!--
	作者:徐波
	日期:2016-10-8 16:53:24
	页面:包材bomJSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>包材bom</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="bcBom.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
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
				<form action="#" id="bcBomSearchForm" autoSearchFunction="false">
					
					
					<label class="panel-title">搜索：</label>
					<input type="text" name="filter[**]" like="true" class="easyui-textbox">
					
					
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="包材bom列表" class="easyui-datagrid"  url="${path}bcBom/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="PACKBOMGENERICNAME" width="15">总称</th>
					<!-- <th field="PACKBOMNAME" width="15">包装名称</th> -->
					<th field="PACKBOMCODE" width="15">包装标准代码</th>
					<th field="PACKBOMTYPE" width="15">包装大类</th>
					<th field="PACKBOMCONSUMERID" width="15">适用客户</th>
					<th field="PACKBOMMODEL" width="15">产品规格</th>
					<th field="PACKBOMWIDTH" width="15">门幅</th>
					<th field="PACKBOMLENGTH" width="15">卷长</th>
					<th field="PACKBOMWEIGHT" width="15">卷重</th>
					<th field="PACKBOMRADIUS" width="15">卷径</th>
					<th field="PACKBOMROLLSPERBOX" width="15">每箱卷数</th>
					<th field="PACKBOMBOXESPERTRAY" width="15">每托箱数</th>
					<th field="PACKBOMROLLSPERTRAY" width="15">每托卷数</th>
				</tr>
			</thead>
		</table>
	</div>
</body>