<!--
	作者:sunli
	日期：2017.11.29
	页面:西门子裁片导入
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/resources/uploadify/uploadify.css">
<script type="text/javascript"
	src="<%=basePath%>/resources/uploadify/jquery.uploadify.min.js"></script>
<style type="text/css">
.file {
	color: black;
	height: 16px;
	line-height: 16px;
	display: inline-block;
	font-size: 12px;
	margin: 0;
	padding: 0;
	width: 100px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.file_remove {
	color: red;
	height: 16px;
	line-height: 16px;
	display: inline-block;
	font-size: 12px;
	font-family: cursive;
	margin: 0;
	padding: 0;
	width: 15px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.upload_file_list {
	margin: 2px;
	padding: 2px;
	display: inline-block;
}

.upload_file_list a {
	text-decoration: none;
}
</style>
<script type="text/javascript">
	
</script>

<form id="form" method="post" ajax="true" autocomplete="off">
	<h3>提示:导入会清空之前的裁片数据！</h3>
	<input type="hidden" name="fileId" id="fileId" value="">
	<div id="files"></div>
	<table>
		<tr>
			<td style="border:none;">工艺文件上传:</td>
			<td style="border:none;">
				<hr style="border:none;"> 
				<input style="width:100px;"
				type="file" name="file_upload" id="file_upload">
			</td>
		</tr>
	</table>

</form>

<%@ include file="fragmentImport.js.jsp"%>
