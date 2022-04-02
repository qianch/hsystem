<!--
	作者:高飞
	日期:2016-11-28 21:52:43
	页面:生产计划明细增加或修改页面
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
	<!--生产计划明细表单-->
	<form id="producePlanDetailForm" method="post" ajax="true" action="<%=basePath %>producePlanDetail/${empty producePlanDetail.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${producePlanDetail.id}" />
		
		<table width="100%">
				<tr>
					<td class="title">生产计划ID:</td>
					<!--生产计划ID-->
					<td>
						<input type="text" id="producePlanId" name="producePlanId" value="${producePlanDetail.producePlanId}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">产品id:</td>
					<!--产品id-->
					<td>
						<input type="text" id="productId" name="productId" value="${producePlanDetail.productId}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">任务单号:</td>
					<!--任务单号-->
					<td>
						<input type="text" id="planCode" name="planCode" value="${producePlanDetail.planCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">客户id:</td>
					<!--客户id-->
					<td>
						<input type="text" id="consumerId" name="consumerId" value="${producePlanDetail.consumerId}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">订单号:</td>
					<!--订单号-->
					<td>
						<input type="text" id="salesOrderCode" name="salesOrderCode" value="${producePlanDetail.salesOrderCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">批次号:</td>
					<!--批次号-->
					<td>
						<input type="text" id="batchCode" name="batchCode" value="${producePlanDetail.batchCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">产品规格:</td>
					<!--产品规格-->
					<td>
						<input type="text" id="productModel" name="productModel" value="${producePlanDetail.productModel}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">产品名称:</td>
					<!--产品名称-->
					<td>
						<input type="text" id="productName" name="productName" value="${producePlanDetail.productName}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">门幅:</td>
					<!--门幅-->
					<td>
						<input type="text" id="productWidth" name="productWidth" value="${producePlanDetail.productWidth}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">卷重:</td>
					<!--卷重-->
					<td>
						<input type="text" id="productWeight" name="productWeight" value="${producePlanDetail.productWeight}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">卷长:</td>
					<!--卷长-->
					<td>
						<input type="text" id="productLength" name="productLength" value="${producePlanDetail.productLength}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">订单数量(kg/套):</td>
					<!--订单数量(kg/套)-->
					<td>
						<input type="text" id="orderCount" name="orderCount" value="${producePlanDetail.orderCount}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">工艺版本:</td>
					<!--工艺版本-->
					<td>
						<input type="text" id="bomVersion" name="bomVersion" value="${producePlanDetail.bomVersion}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">工艺代码:</td>
					<!--工艺代码-->
					<td>
						<input type="text" id="bomCode" name="bomCode" value="${producePlanDetail.bomCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">包装版本:</td>
					<!--包装版本-->
					<td>
						<input type="text" id="bcBomVersion" name="bcBomVersion" value="${producePlanDetail.bcBomVersion}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">包装代码:</td>
					<!--包装代码-->
					<td>
						<input type="text" id="bcBomCode" name="bcBomCode" value="${producePlanDetail.bcBomCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">总卷数:</td>
					<!--总卷数-->
					<td>
						<input type="text" id="bcBoxCount" name="bcBoxCount" value="${producePlanDetail.bcBoxCount}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">总托数:</td>
					<!--总托数-->
					<td>
						<input type="text" id="bcTrayCount" name="bcTrayCount" value="${producePlanDetail.bcTrayCount}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">需要生产数量(kg/套):</td>
					<!--需要生产数量(kg/套)-->
					<td>
						<input type="text" id="requirementCount" name="requirementCount" value="${producePlanDetail.requirementCount}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">已生产数量(kg/套):</td>
					<!--已生产数量(kg/套)-->
					<td>
						<input type="text" id="producedCount" name="producedCount" value="${producePlanDetail.producedCount}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">出货日期:</td>
					<!--出货日期-->
					<td>
						<input type="text" id="deleveryDate" name="deleveryDate" value="${producePlanDetail.deleveryDate}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">机台号:</td>
					<!--机台号-->
					<td>
						<input type="text" id="deviceCode" name="deviceCode" value="${producePlanDetail.deviceCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">备注:</td>
					<!--备注-->
					<td>
						<input type="text" id="comment" name="comment" value="${producePlanDetail.comment}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">是否已完成:</td>
					<!--是否已完成-->
					<td>
						<input type="text" id="isFinished" name="isFinished" value="${producePlanDetail.isFinished}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">排序:</td>
					<!--排序-->
					<td>
						<input type="text" id="sort" name="sort" value="${producePlanDetail.sort}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">产品属性:</td>
					<!--产品属性-->
					<td>
						<input type="text" id="productType" name="productType" value="${producePlanDetail.productType}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">生产计划明细Id:</td>
					<!--生产计划明细Id-->
					<td>
						<input type="text" id="producePlanDetailId" name="producePlanDetailId" value="${producePlanDetail.producePlanDetailId}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">打包数量:</td>
					<!--打包数量-->
					<td>
						<input type="text" id="packagedCount" name="packagedCount" value="${producePlanDetail.packagedCount}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">来自套材:</td>
					<!--来自套材-->
					<td>
						<input type="text" id="fromTcId" name="fromTcId" value="${producePlanDetail.fromTcId}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">来自套材名称:</td>
					<!--来自套材名称-->
					<td>
						<input type="text" id="fromTcName" name="fromTcName" value="${producePlanDetail.fromTcName}" class="easyui-textbox"  >
					</td>
				</tr>
			
		</table>
	</form>
</div>