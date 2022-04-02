<!--
	作者:徐波
	日期:2016-11-26 14:44:04
	页面:综合统计增加或修改页面
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
	<!--综合统计表单-->
	<form id="totalStatisticsForm" method="post" ajax="true" action="<%=basePath %>totalStatistics/${empty totalStatistics.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${totalStatistics.id}" />
		
		<table width="100%">
				<tr>
					<td class="title">条码号:</td>
					<!--条码号-->
					<td>
						<input type="text" id="rollBarcode" name="rollBarcode" value="${totalStatistics.rollBarcode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">条码类型:</td>
					<!--条码类型-->
					<td>
						<input type="text" id="barcodeType" name="barcodeType" value="${totalStatistics.barcodeType}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">客户名称:</td>
					<!--客户名称-->
					<td>
						<input type="text" id="CONSUMERNAME" name="CONSUMERNAME" value="${totalStatistics.CONSUMERNAME}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">产品规格:</td>
					<!--产品规格-->
					<td>
						<input type="text" id="productModel" name="productModel" value="${totalStatistics.productModel}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">批次号:</td>
					<!--批次号-->
					<td>
						<input type="text" id="batchCode" name="batchCode" value="${totalStatistics.batchCode}" class="easyui-textbox"  >
					</td>
				</tr>
				
				<tr>
					<td class="title">计划单号:</td>
					<!--计划单号-->
					<td>
						<input type="text" id="producePlanCode" name="producePlanCode" value="${totalStatistics.producePlanCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">订单号:</td>
					<!--订单号-->
					<td>
						<input type="text" id="salesOrderCode" name="salesOrderCode" value="${totalStatistics.salesOrderCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">出产时间:</td>
					<!--出产时间-->
					<td>
						<input type="text" id="rollOutputTime" name="rollOutputTime" value="${totalStatistics.rollOutputTime}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">机台号:</td>
					<!--机台号-->
					<td>
						<input type="text" id="deviceCode" name="deviceCode" value="${totalStatistics.deviceCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">车间:</td>
					<!--车间-->
					<td>
						<input type="text" id="name" name="name" value="${totalStatistics.name}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">操作人:</td>
					<!--操作人-->
					<td>
						<input type="text" id="loginName" name="loginName" value="${totalStatistics.loginName}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">状态:</td>
					<!--操作人-->
					<td>
						<input type="text" id="state" name="state" value="${totalStatistics.state}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">重量:</td>
					<!--重量-->
					<td>
						<input type="text" id="rollWeight" name="rollWeight" value="${totalStatistics.rollWeight}" class="easyui-textbox"  >
					</td>
				</tr>
		</table>
	</form>
</div>