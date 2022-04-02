<!--
	作者:徐波
	日期:2016-11-2 9:30:08
	页面:出货订单关联增加或修改页面
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
	<!--出货订单关联表单-->
	<form id="deliveryPlanSalesOrdersForm" method="post" ajax="true" action="<%=basePath %>planner/deliveryPlanSalesOrders/${empty deliveryPlanSalesOrders.id ?'add':'edit'}" autocomplete="off" >
		
		<table width="100%">
				<tr>
					<td class="title">订单号:</td>
					<input type="hidden" id="salesOrderId" value="">
					<!--销售订单号-->
					<td>
					<input id="salesOrderCode" name="salesOrderCode" class="easyui-searchbox"
					value="${deliveryPlanSalesOrders.salesOrderCode}" editable="false" required="true"
					data-options="searcher:ChooseOrder">
					
					</td>
				</tr>
				<tr>
					<td class="title">提单号:</td>
					<!--提单号-->
					<td>
						<input type="text" id="ladingCode" name="ladingCode" value="${deliveryPlanSalesOrders.ladingCode}" class="easyui-textbox">
					</td>
				</tr>
				<tr>
					<td class="title">箱号:</td>
					<!--箱号-->
					<td>
						<input type="text" id="boxNumber" name="boxNumber" value="${deliveryPlanSalesOrders.boxNumber}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">封号:</td>
					<!--封号-->
					<td>
						<input type="text" id="serialNumber" name="serialNumber" value="${deliveryPlanSalesOrders.serialNumber}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">件数:</td>
					<!--件数-->
					<td>
						<input type="text" id="count" name="count" value="${deliveryPlanSalesOrders.count}" class="easyui-textbox" >
					</td>
				</tr>
				<tr>
					<td class="title">毛重:</td>
					<!--毛重-->
					<td>
						<input type="text" id="weight" name="weight" value="${deliveryPlanSalesOrders.weight}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">尺码:</td>
					<!--尺码-->
					<td>
						<input type="text" id="size" name="size" value="${deliveryPlanSalesOrders.size}" class="easyui-textbox"  >
					</td>
				</tr>
			
		</table>
	</form>
</div>