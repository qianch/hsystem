<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>新增或者编辑部门</title>
<%@ include file="../base/meta.jsp"%>
<style type="text/css">
	table{
		width:100%;
	}
</style>
</head>

<body>
	<table>
		<tr>
			<td class="title">时间:</td>
			<td>${msg.createTime }</td>
		</tr>
		<tr>
			<td class="title">发送人:</td>
			<td>${u eq null?'系统':u.userName }</td>
		</tr>
		<tr>
			<td class="title">消息内容:</td>
			<td ><pre style="white-space: pre-wrap;word-break:break-all;">${msg.content }</pre></td>
		</tr>
		<tr>
			<td class="title">附件</td>
			<td>
				<jsp:include page="../base/files.jsp">
					<jsp:param value="${msg.attachment }" name="path"/>
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td class="title">链接</td>
			<td><a href="${msg.link }">${msg.link }</a></td>
		</tr>
	</table>
</body>
</html>
