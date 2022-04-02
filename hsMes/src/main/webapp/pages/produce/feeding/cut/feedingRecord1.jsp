<!--
	作者:徐波
	日期:2017-3-2 13:36:42
	页面:投料记录JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>裁剪投料记录</title>
  	<%@ include file="../../../base/meta.jsp" %>
	<%@ include file="feedingRecord1.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="feedingRecordSearchForm" autoSearchFunction="false">
					机台号:<input type="text" name="filter[deviceCode]" like="true" class="easyui-textbox">
					胚布条码:<input type="text" name="filter[rollCode]" like="true" class="easyui-textbox">
					投料日期:<input type="text" name="filter[start]"  class="easyui-datetimebox">
					　　　至:<input type="text" name="filter[end]"  class="easyui-datetimebox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel" onclick="exportDetail()">
						导出
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false"  class="easyui-datagrid"  toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="DEVICECODE" sortable="true" width="8">机台号</th>
					<th field="ROLLCODE" sortable="true" width="8">胚布条码</th>
					<th field="FEEDINGDATE" sortable="true" width="8">投料日期</th>
					<th field="USERNAME" sortable="true" width="8">操作人</th>
					<th field="ROLLWEIGHT" sortable="true" width="8">卷重</th>
				</tr>
			</thead>
		</table>
	</div>
</body>