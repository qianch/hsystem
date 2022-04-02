<!--
	作者:肖文彬
	日期:2016-11-8 15:24:59
	页面:盘库结果表增加或修改页面
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
	<!--盘库结果表表单-->
	<form id="stockCheckResultForm" method="post" ajax="true" action="<%=basePath %>stockCheckResult/${empty stockCheckResult.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${stockCheckResult.id}" />
		
		<table width="100%">
			
		</table>
	</form>
</div>