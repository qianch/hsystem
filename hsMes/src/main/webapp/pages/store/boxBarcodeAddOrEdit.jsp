<!--
	作者:徐波
	日期:2016-12-3 16:35:51
	页面:箱条码增加或修改页面
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
	<!--箱条码表单-->
	<form id="boxBarcodeForm" method="post" ajax="true" action="<%=basePath %>boxBarcode/${empty boxBarcode.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${boxBarcode.id}" />
		
		<table width="100%">
				<tr>
					<td class="title">条码号:</td>
					<!--条码号-->
					<td>
						<input type="text" id="barcode" name="barcode" value="${boxBarcode.barcode}" class="easyui-textbox"  readonly="true" >
					</td>
				</tr>
			<tr>
				<td class="title">客户条码号:</td>
				<!--条码号-->
				<td>
					<input type="text" id="customerBarCode" name="customerBarCode" value="${boxBarcode.customerBarCode}" class="easyui-textbox"   >
				</td>
			</tr>
			<tr>
				<td class="title">供销商条码号:</td>
				<!--条码号-->
				<td>
					<input type="text" id="agentBarCode" name="agentBarCode" value="${boxBarcode.agentBarCode}" class="easyui-textbox"  >
				</td>
			</tr>

				<tr>
					<td class="title">订单号:</td>
					<!--订单号-->
					<td>
						<input type="text" id="salesOrderCode" name="salesOrderCode" value="${boxBarcode.salesOrderCode}" class="easyui-textbox"  readonly="true" >
					</td>
				</tr>
				<tr>
					<td class="title">批次号:</td>
					<!--批次号-->
					<td>
						<input type="text" id="batchCode" name="batchCode" value="${boxBarcode.batchCode}" class="easyui-textbox"  readonly="true" >
					</td>
				</tr>

				<tr>
					<td class="title">打印时间:</td>
					<!--打印时间-->
					<td>
						<input type="text" id="printDate" name="printDate" value="${boxBarcode.printDate}" class="easyui-textbox"  readonly="true" >
					</td>
				</tr>
				<tr>
					<td class="title">部件名称:</td>
					<!--部件名称-->
					<td>
						<input type="text" id="partName" name="partName" value="${boxBarcode.partName}" class="easyui-textbox" readonly="true" >
					</td>
				</tr>
			
		</table>
	</form>
</div>