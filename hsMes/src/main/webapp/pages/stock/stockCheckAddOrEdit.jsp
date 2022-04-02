<!--
	作者:肖文彬
	日期:2016-11-8 15:25:19
	页面:盘库记录表增加或修改页面
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
	<!--盘库记录表表单-->
	<form id="stockCheckForm" method="post" ajax="true" action="<%=basePath %>stockCheck/${empty stockCheck.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${stockCheck.id}" />
		
		<table width="100%">
				<tr>
					<td class="title"><span style="color:red;">*</span>库位代码:</td>
					<!--库位代码-->
					<td>
						<input type="text" id="checkWarehousePosCode" name="checkWarehousePosCode" value="${stockCheck.checkWarehousePosCode}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>库房代码:</td>
					<!--库房代码-->
					<td>
						<input type="text" id="checkWarehouseCode" name="checkWarehouseCode" value="${stockCheck.checkWarehouseCode}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>盘点人:</td>
					<!--盘点人-->
					<td>
						<input type="text" id="checkUserId" name="checkUserId" value="${stockCheck.checkUserId}" class="easyui-textbox" required="true" >
					</td>
				</tr>
			
		</table>
	</form>
</div>