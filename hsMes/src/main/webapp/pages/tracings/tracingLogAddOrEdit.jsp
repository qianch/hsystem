<!--
	作者:徐波
	日期:2016-11-30 14:03:19
	页面:生产追溯日志增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<style type="text/css">
	//CSS 代码
</style>
<script type="text/javascript">
	//JS代码
</script>
<div>
	<!--生产追溯日志表单-->
	<form id="tracingLogForm" method="post" ajax="true" action="<%=basePath %>tracingLog/${empty tracingLog.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${tracingLog.id}" />
		
		<table width="100%">
			
		</table>
	</form>
</div>