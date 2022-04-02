<!--
	作者:高飞
	日期:2016-10-24 14:51:44
	页面:流程设置增加或修改页面
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
	<!--流程设置表单-->
	<form id="auditProcessSettingForm" method="post" ajax="true" action="<%=basePath %>audit/setting/${empty auditProcessSetting.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${auditProcessSetting.id}" />
		
		<table width="100%">
				<tr>
					<td class="title">流程代码:</td>
					<!--流程代码-->
					<td>
						<input type="text" id="auditCode" name="auditCode"  value="${auditProcessSetting.auditCode}" class="easyui-textbox" required="true" data-options="icons:[]">
					</td>
				</tr>
				<tr>
					<td class="title">流程名称:</td>
					<!--流程名称-->
					<td>
						<input type="text" id="auditName" name="auditName"  value="${auditProcessSetting.auditName}" class="easyui-textbox" required="true" data-options="icons:[]" >
					</td>
				</tr>
				<tr>
					<td class="title">流程等级:</td>
					<!--流程等级-->
					<td>
						<input type="text" id="auditLevel" name="auditLevel"  value="${auditProcessSetting.auditLevel}" class="easyui-textbox" required="true" data-options="icons:[]" >
					</td>
				</tr>
				<tr>
					<td class="title">一级审核人员:</td>
					<!--一级审核人员-->
					<td>
						<input type="hidden" id="firstLevelUsers" name="firstLevelUsers" >
						<input style="width:400px;" type="text" id="auditFirstLevelUsers" name="auditFirstLevelUsers" value="${auditProcessSetting.auditFirstLevelUsers}" class="easyui-searchbox" data-options="searcher:selectLevelOneUser,icons:[]">
					</td>
				</tr>
				<tr>
					<td class="title">二级审核人员:</td>
					<!--二级审核人员-->
					<td>
						<input type="hidden" id="secondLevelUsers" name="secondLevelUsers" >
						<input style="width:400px;" type="text" id="auditSecondLevelUsers" name="auditSecondLevelUsers" value="${auditProcessSetting.auditSecondLevelUsers}"  class="easyui-searchbox" data-options="searcher:selectLevelTwoUser,icons:[]">
					</td>
				</tr>
			
		</table>
	</form>
</div>