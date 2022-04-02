<!--
	作者:肖文彬
	日期:2016-11-16 12:33:40
	页面:出库单明细增加或修改页面
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
	<!--出库单明细表单-->
	<form id="materialOutOrderDetailForm" method="post" ajax="true" action="<%=basePath %>materialOutOrderDetail/${empty materialOutOrderDetail.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${materialOutOrderDetail.id}" />
		
		<table width="100%">
				<tr>
					<td class="title"><span style="color:red;">*</span>出库单ID:</td>
					<!--出库单ID-->
					<td>
						<input type="text" id="outOrderId" name="outOrderId" value="${materialOutOrderDetail.outOrderId}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>库位编码:</td>
					<!--库位编码-->
					<td>
						<input type="text" id="warehousePosCode" name="warehousePosCode" value="${materialOutOrderDetail.warehousePosCode}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>仓库编码:</td>
					<!--仓库编码-->
					<td>
						<input type="text" id="warehouseCode" name="warehouseCode" value="${materialOutOrderDetail.warehouseCode}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>物料条码:</td>
					<!--物料条码-->
					<td>
						<input type="text" id="materialCode" name="materialCode" value="${materialOutOrderDetail.materialCode}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>重量:</td>
					<!--重量-->
					<td>
						<input type="text" id="outWeight" name="outWeight" value="${materialOutOrderDetail.outWeight}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>出库时间:</td>
					<!--出库时间-->
					<td>
						<input type="text" id="outTime" name="outTime" value="${materialOutOrderDetail.outTime}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title">号数偏差:</td>
					<!--号数偏差-->
					<td>
						<input type="text" id="numberDeviation" name="numberDeviation" value="${materialOutOrderDetail.numberDeviation}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">接头方式:</td>
					<!--接头方式-->
					<td>
						<input type="text" id="subWay" name="subWay" value="${materialOutOrderDetail.subWay}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">产品大类:</td>
					<!--产品大类-->
					<td>
						<input type="text" id="produceCategory" name="produceCategory" value="${materialOutOrderDetail.produceCategory}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">规格型号:</td>
					<!--规格型号-->
					<td>
						<input type="text" id="materialModel" name="materialModel" value="${materialOutOrderDetail.materialModel}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">生产日期:</td>
					<!--生产日期-->
					<td>
						<input type="text" id="produceDate" name="produceDate" value="${materialOutOrderDetail.produceDate}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">操作人:</td>
					<!--操作人-->
					<td>
						<input type="text" id="outUserId" name="outUserId" value="${materialOutOrderDetail.outUserId}" class="easyui-textbox"  >
					</td>
				</tr>
			
		</table>
	</form>
</div>