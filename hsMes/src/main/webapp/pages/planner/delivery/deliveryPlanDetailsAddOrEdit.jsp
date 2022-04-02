<!--
	作者:徐波
	日期:2016-11-2 9:30:07
	页面:出货详情列表增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<style type="text/css">
//
CSS

 

代码
</style>
<script type="text/javascript">
	//JS代码
</script>
<div>
	<!--出货详情列表表单-->
	<form id="deliveryPlanDetailsForm" method="post" ajax="true"
		action="<%=basePath %>planner/deliveryPlanDetails/${empty deliveryPlanDetails.id ?'add':'edit'}"
		autocomplete="off">

		<input type="hidden" name="id" value="${deliveryPlanDetails.id}" />
		<hidden id="productId" name="productId" required="true" value="">
		<table width="100%">

			<tr>
				<td class="title">产品名称:</td>
				<!--成品ID-->
				<td><input id="factoryProductName" name="factoryProductName"
					class="easyui-searchbox" editable="false" required="true"
					data-options="searcher:ChooseProduct"></td>
			</tr>
		</table>

	</form>
			<a href="#" class="easyui-linkbutton" iconCls="icon-add"
					plain="true" onclick="add_dg_delivery_product_detail()">增加</a>
		<a href="#" class="easyui-linkbutton"
					iconCls="icon-delete" plain="true" onclick="doDelete_dg_delivery_product_detail()">删除</a>
		<table id="dg_delivery_product_detail" 	title="发货产品明细" class="easyui-datagrid" url=""
			rownumbers="true" >
			<thead>
				<tr>
					<th field="salesOrderCode" sortable="true" width="15">产品名称</th>
					<th field="ladingCode" sortable="true" width="15">部件名称</th>
					<th field="boxNumber" sortable="true" width="15">库存数量</th>
					<th field="serialNumber" sortable="true" width="15">出货数量</th>
					<th field="count" sortable="true" width="15">备注</th>
				</tr>
			</thead>
		</table>
</div>