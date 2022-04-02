<!--
	作者:徐波
	日期:2016-11-30 14:03:19
	页面:生产追溯日志JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>生产追溯日志</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="tracingLog.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="tracingLogSearchForm" autoSearchFunction="false">
					
					
					<label class="panel-title">搜索：</label>
					<input type="text" name="filter[**]" like="true" class="easyui-textbox">
					
					
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>



	</div>
</body>