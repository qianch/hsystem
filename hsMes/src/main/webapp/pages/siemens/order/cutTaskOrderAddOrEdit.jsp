<!--
	作者:高飞
	日期:2017-7-31 17:04:13
	页面:裁剪派工单增加或修改页面
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
	<!--裁剪派工单表单-->
	<form id="cutTaskOrderForm" method="post" ajax="true" action="<%=basePath %>cutTaskOrder/${empty cutTaskOrder.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${cutTaskOrder.id}" />
		
		<table width="100%">
				<tr>
					<td class="title">派工单编号:</td>
					<!--派工单编号-->
					<td>
						<input type="text" id="ctoCode" name="ctoCode" value="${cutTaskOrder.ctoCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">裁剪任务单ID:</td>
					<!--裁剪任务单ID-->
					<td>
						<input type="text" id="ctId" name="ctId" value="${cutTaskOrder.ctId}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">组别:</td>
					<!--组别-->
					<td>
						<input type="text" id="ctoGroupName" name="ctoGroupName" value="${cutTaskOrder.ctoGroupName}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">机长:</td>
					<!--机长-->
					<td>
						<input type="text" id="ctoGroupLeader" name="ctoGroupLeader" value="${cutTaskOrder.ctoGroupLeader}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">派工套数:</td>
					<!--派工套数-->
					<td>
						<input type="text" id="ctoCount" name="ctoCount" value="${cutTaskOrder.ctoCount}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">创建日期:</td>
					<!--创建日期-->
					<td>
						<input type="text" id="ctoCreateDate" name="ctoCreateDate" value="${cutTaskOrder.ctoCreateDate}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">创建人:</td>
					<!--创建人-->
					<td>
						<input type="text" id="ctoCreateUser" name="ctoCreateUser" value="${cutTaskOrder.ctoCreateUser}" class="easyui-textbox"  >
					</td>
				</tr>
			
		</table>
	</form>
</div>