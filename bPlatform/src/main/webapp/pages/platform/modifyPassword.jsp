<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
	<form action="<%=basePath%>user/modifyPassword" ajax="true" id="modifypwd">
		<table style="width:100%;">
			<tr><td  class="title">旧密码</td><td><input type="password" name="p1" id="p1" class="easyui-textbox" required="true"></td></tr>
			<tr><td class="title">新密码</td><td><input type="password" name="p2" id="p2" class="easyui-textbox" required="true"></td></tr>
			<tr><td  class="title">确认新密码</td><td><input type="password" id="p3" class="easyui-textbox" required="true"></td></tr>
		</table>
	</form>
</div>
