<!--
	作者:高飞
	日期:2017-02-10
	页面:翻包审核
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="../../base/meta.jsp"%>
<%@ include file="turnBagPlan.js.jsp"%>
<style type="text/css">
.title {
	width: 150px;
	height:35px;
}
table td {
	padding-left:15px;
}
</style>
<script type="text/javascript">
var details=${empty details?'[]':details};
</script>
</head>
<body>
	<div>
	<!--翻包计划表单-->
	<form id="turnBagPlanForm" method="post" ajax="true" action="<%=basePath %>tbp/${empty turnBagPlan.id?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${turnBagPlan.id}" />
		<input type="hidden" id="newSalesOrderDetailsId" name="newSalesOrderDetailsId" value="${turnBagPlan.newSalesOrderDetailsId}" >
		<input type="hidden" id="auditState" name="auditState" value="${empty turnBagPlan.auditState?0:turnBagPlan.auditState}" >
		<input type="hidden" id="isCompleted" name="isCompleted" value="0">
		
		<table width="100%">
				<tr>
					<td class="title">翻包任务单号:</td>
					<!--翻包任务单号-->
					<td>
						<input type="text" id="trunBagCode" name="trunBagCode" value="${empty turnBagPlan.trunBagCode?code:turnBagPlan.trunBagCode}" class="easyui-textbox" required="true" style="width:200px" >
					</td>
					<td class="title">翻包数量:</td>
					<!--翻包任务单号-->
					<td>
						<input type="text" id="trunBagCount" name="trunBagCount" value="${turnBagPlan.trunBagCount}" class="easyui-numberspinner" required="true" data-options="min:1" style="width:200px" >
					</td>
				</tr>
				<tr>
					<td class="title">新订单号:</td>
					<!--新批次号-->
					<td>
						<input type="text" id="newSalesOrderCode" editable="false" name="newSalesOrderCode" value="${turnBagPlan.newSalesOrderCode}" class="easyui-textbox" required="true"  style="width:200px">
					</td>
					<td class="title">新批次号:</td>
					<!--新批次号-->
					<td>
						<input type="text" id="newBatchCode" name="newBatchCode" editable="true" value="${turnBagPlan.newBatchCode}" class="easyui-combobox" required="true" data-options="valueField:'v',textField:'t'" style="width:200px" >
					</td>
				</tr>
				<tr>
					<td class="title">下单时间:</td>
					<!--下单时间-->
					<td>
						<input type="text" id="createTime" name="createTime" value="${turnBagPlan.createTime}" readonly="true" class="easyui-textbox" required="true" style="width:200px" >
					</td>
					<td class="title">完成截止时间:</td>
					<!--完成截止时间-->
					<td>
						<input type="text" id="finishTime" name="finishTime" value="${turnBagPlan.finishTime}" class="easyui-datebox" required="true"   style="width:200px">
					</td>
				</tr>
				<tr>
					<td class="title">下单人:</td>
					<!--下单人-->
					<td>
						<input type="text" id="userName" readonly="true" name="userName" value="${empty turnBagPlan.userName?userName:turnBagPlan.userName}" class="easyui-textbox" required="true" style="width:200px" >
					</td>
					<td class="title">翻包执行部门:</td>
					<!--翻包执行部门-->
					<td>
						<%-- <input type="text" id="departmentName" name="departmentName" value="${turnBagPlan.departmentName}" class="easyui-textbox" required="true" style="width:200px" > --%>
						<input type="text" id="departmentName" class="easyui-combobox"  required="true" name="departmentName" value="${turnBagPlan.departmentName}" style="width:200px" data-options="data: [
		                        {value:'编织一车间',text:'编织一车间'},
		                        {value:'编织二车间',text:'编织二车间'},
		                        {value:'编织三车间',text:'编织三车间'},
		                        {value:'裁剪车间',text:'裁剪车间'}],onSelect:filter">
					</td>
				</tr>
				<tr>
					<td class="title">说明</td>
					<td colspan="3">
						<textarea style="width:100%;height:100px;" placeholder="说明(500字以内)" name="memo" maxlength="1500">${turnBagPlan.memo }</textarea>
					</td>
				</tr>
		</table>
	</form>
		<table id="oldOrders" title="待翻包订单" singleSelect="true" class="easyui-datagrid" fitColumns="true" style="width:100%;" data-options="data:details">
			<thead  frozen="true">
				<tr>
					<th width="120" field="SALESORDERSUBCODE">订单号</th>
					<th width="120" field="BATCHCODE">翻包批次</th>
					<th width="80" field="TURNBAGCOUNT" formatter="processFormatter" styler="processStyler">翻包数量</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<!-- <th width="100" field="SALESORDERSUBCODEPRINT">客户订单号</th> -->
					<th width=150 field="FACTORYPRODUCTNAME">厂内名称</th>
					<th width="100" field="CONSUMERPRODUCTNAME">客户产品名称</th>
					<th width="150" field="PRODUCTMODEL">产品型号</th>
					<th field="PRODUCTWIDTH" width="80">门幅(mm)</th>
					<th field="PRODUCTROLLLENGTH" width="80">卷长(m)</th>
					<th field="PRODUCTROLLWEIGHT" width="80">卷重(kg)</th>
					<th field="PRODUCTPROCESSCODE" width="140" styler="vStyler">工艺代码</th>
					<th field="PRODUCTPROCESSBOMVERSION" width="80" >工艺版本</th>
					<th field="PRODUCTPACKAGINGCODE" width="140" styler="bvStyler">包装代码</th>
					<th field="PRODUCTPACKAGEVERSION" width="80" >包装版本</th>
				</tr>
			</thead>
		</table>
</div>
</body>
</html>