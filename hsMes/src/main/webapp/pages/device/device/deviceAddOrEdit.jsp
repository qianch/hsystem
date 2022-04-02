<!--
	作者:宋黎明
	日期:2016-9-29 11:46:46
	页面:设备信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<style type="text/css">
	.title{
		width:150px;
	}
</style>
<script type="text/javascript">
	//JS代码
</script>
<div>
	<!--设备信息表单-->
	<form id="deviceForm" method="post" ajax="true" action="<%=basePath %>device/${empty device.id ?'add':'edit'}" autocomplete="off" >

		<input type="hidden" name="id" value="${device.id}" />
		<input type="hidden" name="deviceCatetoryId" value="${device.deviceCatetoryId}"/>
		<input type="hidden" id="deviceDepartmentId" name="deviceDepartmentId" value="${device.deviceDepartmentId}"/>
		<table width="95%">
				<tr>
					<td class="title">设备资产编号:</td>
					<!--设备资产编号-->
					<td>
						<input type="text" id="deviceAssetCode" name="deviceAssetCode" value="${device.deviceAssetCode}" class="easyui-textbox" required="true" data-options="validType:['length[0,120]']" style="width:200px;">
					</td>
				</tr>
				<tr>
					<td class="title">设备代码:</td>
					<!--设备代码-->
					<td>
						<input type="text" id="deviceCode" name="deviceCode" value="${device.deviceCode}" class="easyui-textbox" required="true" data-options="validType:['length[0,120]']" style="width:200px;">
					</td>
				</tr>
				<tr>
					<td class="title">资产名称:</td>
					<!--资产名称-->
					<td>
						<input type="text" id="deviceName" name="deviceName" value="${device.deviceName}" class="easyui-textbox" required="true" data-options="validType:['length[0,120]']" style="width:200px;">
					</td>
				</tr>
				<%-- <tr>
					<td class="title"><span style="color:red;">*</span>设备类别:</td>
					<!--设备类别-->
					<td>
						<input type="text" id="deviceCatetoryId" name="deviceCatetoryId" value="${device.deviceCatetoryId}" class="easyui-textbox" required="true"  style="width:200px;">
					</td>
				</tr> --%>
				<tr>
					<td class="title">单位:</td>
					<!--单位-->
					<td>
						<input type="text" id="deviceUnit" name="deviceUnit" value="${device.deviceUnit}" class="easyui-textbox" data-options="validType:['length[0,120]']"  style="width:200px;">
					</td>
				</tr>
				<tr>
					<td class="title">数量:</td>
					<!--数量-->
					<td>
						<input type="text" id="deviceCount" name="deviceCount" value="${device.deviceCount}" class="easyui-numberbox" required="true" data-options="validType:['length[0,120]'],precision:1" style="width:200px;">
					</td>
				</tr>
				<tr>
					<td class="title">供应商:</td>
					<!--供应商-->
					<td>
						<input type="text" id="deviceSupplier" name="deviceSupplier" value="${device.deviceSupplier}" class="easyui-textbox"  data-options="validType:['length[0,100]']" style="width:200px;">
					</td>
				</tr>
				<tr>
					<td class="title">使用时间:</td>
					<!--使用时间-->
					<td>
						<input type="text" id="deviceUsingDate" name="deviceUsingDate" value="${device.deviceUsingDate}" class="easyui-datetimebox" required="true" data-options="validType:['length[0,120]']" style="width:200px;">
					</td>
				</tr>
				<tr>
					<td class="title">所属部门:</td>
					<!--所属部门-->
					<td>
						<input type="text" name="deviceCatetoryId" id="departmentSelect" required="true" class="easyui-tree"  value="${device.deviceDepartmentId}" panelHeight="200" data-options="validType:['length[0,120]']" style="width:200px;">
					</td>
				</tr>
				<tr>
					<td class="title">规格型号:</td>
					<!--规格型号-->
					<td>
						<input type="text" id="specModel" name="specModel" value="${device.specModel}" class="easyui-textbox" required="true" data-options="validType:['length[0,120]']" style="width:200px;">
					</td>
				</tr>
				<tr>
					<td class="title">机台屏幕ip:</td>
					<!--机台屏幕ip-->
					<td>
						<input type="text" id="machineScreenIp" name="machineScreenIp" value="${empty device.machineScreenIp?'192.168.0.0':device.machineScreenIp}" class="easyui-textbox" required="true" validType="ip" style="width:200px;">
					</td>
				</tr>
				<tr>
					<td class="title">机台屏幕名称:</td>
					<!--机台屏幕名称-->
					<td>
						<input type="text" id="machineScreenName" name="machineScreenName" value="${device.machineScreenName}" class="easyui-textbox" required="true" data-options="validType:['length[0,120]']" style="width:200px;">
					</td>
				</tr>
		</table>
	</form>
</div>
