<!--
	作者:徐秦冬
	日期:2018-2-8 11:01:23
	页面:调度日志JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>调度日志</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="scheduleLog.js.jsp" %>
	<script type="text/javascript" src="<%=basePath %>/resources/layerDate/laydate.js"></script>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: auto;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<jsp:include page="../base/toolbar.jsp">
				<jsp:param value="clear" name="ids"/>
				<jsp:param value="icon-remove" name="icons"/>
				<jsp:param value="清空" name="names"/>
				<jsp:param value="clearAll()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="scheduleLogSearchForm" autoSearchFunction="false">
					任务名称：<input type="text" name="filter[taskName]" id="taskName" like="true" value="" class="textbox" style="height:22px;">
					运行结果:<input type="text" name="filter[executeResult]" id="executeResult" class="easyui-combobox"   data-options="valueField:'v',textField:'t',data:[{'v':'0','t':'成功'},{'v':'1','t':'失败'}],onSelect:filter"> 
					起始时间：<input type="text" name="filter[startexecuteTime]" id="startexecuteTime" value="" readonly="readonly" class="textbox laydate-icon">
					结束时间：<input type="text" name="filter[endexecuteTime]" id="endexecuteTime" value="" readonly="readonly" class="textbox laydate-icon">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
							搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" class="easyui-datagrid" nowrap="false" url="${path}scheduleLog/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="TASKNAME" width="5">任务名称</th>
					<th field="EXECUTETIME" width="10">运行时间</th>
					<th field="EXECUTEUSER" width="6">执行人</th>
					<th field="EXECUTERESULT" sortable="false" width="5" formatter="executeResultFormatter">运行结果</th>
					<th field="EXECUTESPENDTIME" sortable="false" width="5" formatter="executeSpendTimeFormatter" >耗时(秒)</th>
					<th field="EXECUTEMEMO" sortable="false" width="10">备注</th>
					<!-- <th field="EXECUTEEXCEPTIONMESSAGE" sortable="false" width="10">异常消息</th> -->
					<th field="EXECUTEEXCEPTIONCAUSE" sortable="false" width="15">异常原因</th>
				</tr>
			</thead>
		</table>
	</div>
</body>