<!--
	作者:高飞
	日期:2016-8-18 15:02:50
	页面:任务调度模板增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<style type="text/css">
</style>
<script type="text/javascript">
</script>
<div>
	<!--任务调度模板表单-->
	<form id="scheduleTemplateForm" method="post" ajax="true" action="<%=basePath %>scheduleTemplate/${empty scheduleTemplate.id ?'add':'edit'}" autocomplete="off">
		<input type="hidden" name="id" value="${scheduleTemplate.id}" /> <input type="hidden" name="createTime" value="${scheduleTemplate.createTime}" />
		<table width="100%">

			<tr>
				<td class="title">调度类名:</td>
				<!--对应的执行的类名-->
				<td>
					<input type="text" id="clazz" name="clazz" validType="schedule" value="${scheduleTemplate.clazz}" class="easyui-textbox" style="width:420px;" required="true" prompt="如:com.xxx.xxx.xxTask">
					<a id="btn" href="#" onclick="getTaskName()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" plain="true">校验</a>
				</td>
			</tr>
			<tr>
				<td class="title">调度名称:</td>
				<!--模板名称-->
				<td><input type="text" class="easyui-textbox" id="templateName" name="templateName" value="${scheduleTemplate.templateName}" style="width:420px;" required="true" editable="false"></td>
			</tr>
			<tr>
				<td class="title">描述:</td>
				<!--描述-->
				<td><input type="text" id="templateDesc" name="templateDesc" value="${scheduleTemplate.templateDesc}" class="easyui-textbox" style="height:50px;width:420px;"  validType="length[1,100]" multiline="true" ></td>
			</tr>

		</table>
	</form>
</div>