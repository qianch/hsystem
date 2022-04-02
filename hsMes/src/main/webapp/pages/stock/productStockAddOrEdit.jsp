<!--
	作者:徐波
	日期:2016-10-24 15:08:20
	页面:成品库存表增加或修改页面
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
	<!--成品库存表表单-->
	<form id="productStockForm" method="post" ajax="true" action="<%=basePath %>stock/productStock/${empty productStock.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${productStock.id}" />
		
		<table width="100%">
				<tr>
					<td class="title">库位id:</td>
					<!--库位id-->
					<td>
						<input type="text" id="warehousePosId" name="warehousePosId" value="${productStock.warehousePosId}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title">卷条码:</td>
					<!--卷条码-->
					<td>
						<input type="text" id="rollCode" name="rollCode" value="${productStock.rollCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">托条码:</td>
					<!--托条码-->
					<td>
						<input type="text" id="trayCode" name="trayCode" value="${productStock.trayCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">入库时间:</td>
					<!--入库时间-->
					<td>
						<input type="text" id="inTime" name="inTime" value="${productStock.inTime}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title">出库时间:</td>
					<!--出库时间-->
					<td>
						<input type="text" id="outTime" name="outTime" value="${productStock.outTime}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">状态:</td>
					<!--状态-->
					<td>
						<input type="text" id="state" name="state" value="${productStock.state}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title">库存状态:</td>
					<!--库存状态-->
					<td>
						<input type="text" id="stockState" name="stockState" value="${productStock.stockState}" class="easyui-textbox" required="true" >
					</td>
				</tr>
			
		</table>
	</form>
</div>