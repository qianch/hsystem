<!--
	作者:徐波
	日期:2016-12-3 16:35:51
	页面:托条码增加或修改页面
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
	<!--托条码表单-->
	<form id="trayBarCodeForm" method="post" ajax="true" action="<%=basePath %>trayBarCode/${empty trayBarCode.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${trayBarCode.id}" />
		
		<table width="100%">
				<tr>
					<td class="title">托条码号:</td>
					<!--托条码号-->
					<td>
						<input type="text" id="barcode" name="barcode" value="${partBarcode.barcode}" class="easyui-textbox" readonly="true" >
					</td>
				</tr>
			<tr>
				<td class="title">客户条码号:</td>
				<!--条码号-->
				<td>
					<input type="text" id="customerBarCode" name="customerBarCode" value="${partBarcode.customerBarCode}" class="easyui-textbox"   >
				</td>
			</tr>
			<tr>
				<td class="title">供销商条码号:</td>
				<!--条码号-->
				<td>
					<input type="text" id="agentBarCode" name="agentBarCode" value="${partBarcode.agentBarCode}" class="easyui-textbox"  >
				</td>
			</tr>
				<tr>
					<td class="title">订单号:</td>
					<!--订单号-->
					<td>
						<input type="text" id="salesOrderCode" name="salesOrderCode" value="${trayBarCode.salesOrderCode}" class="easyui-textbox"  readonly="true" >
					</td>
				</tr>
				<tr>
					<td class="title">批次号:</td>
					<!--批次号-->
					<td>
						<input type="text" id="batchCode" name="batchCode" value="${trayBarCode.batchCode}" class="easyui-textbox"  readonly="true" >
					</td>
				</tr>
				<tr>
					<td class="title">打印时间:</td>
					<!--打印时间-->
					<td>
						<input type="text" id="printDate" name="printDate" value="${trayBarCode.printDate}" class="easyui-textbox"  readonly="true" >
					</td>
				</tr>
			
		</table>
	</form>
</div>