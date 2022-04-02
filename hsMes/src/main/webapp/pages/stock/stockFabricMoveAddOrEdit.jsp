<!--
	作者:徐波
	日期:2017-2-11 8:53:07
	页面:胚布移库表增加或修改页面
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
	<!--胚布移库表表单-->
	<form id="stockFabricMoveForm" method="post" ajax="true" action="<%=basePath %>stockFabricMove/${empty stockFabricMove.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${stockFabricMove.id}" />
		
		<table width="100%">
				<tr>
					<td class="title"><span style="color:red;">*</span>条码号:</td>
					<!--条码号-->
					<td>
						<input type="text" id="barcode" name="barcode" value="${stockFabricMove.barcode}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>原库位:</td>
					<!--原库位-->
					<td>
						<input type="text" id="originWarehousePosCode" name="originWarehousePosCode" value="${stockFabricMove.originWarehousePosCode}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>原库房:</td>
					<!--原库房-->
					<td>
						<input type="text" id="originWarehouseCode" name="originWarehouseCode" value="${stockFabricMove.originWarehouseCode}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>新库位:</td>
					<!--新库位-->
					<td>
						<input type="text" id="newWarehousePosCode" name="newWarehousePosCode" value="${stockFabricMove.newWarehousePosCode}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>新库房:</td>
					<!--新库房-->
					<td>
						<input type="text" id="newWarehouseCode" name="newWarehouseCode" value="${stockFabricMove.newWarehouseCode}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>移库时间:</td>
					<!--移库时间-->
					<td>
						<input type="text" id="moveTime" name="moveTime" value="${stockFabricMove.moveTime}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>操作人:</td>
					<!--操作人-->
					<td>
						<input type="text" id="moveUserId" name="moveUserId" value="${stockFabricMove.moveUserId}" class="easyui-textbox" required="true" >
					</td>
				</tr>
			
		</table>
	</form>
</div>