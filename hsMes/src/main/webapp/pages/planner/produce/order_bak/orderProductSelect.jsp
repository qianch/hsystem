<!--
	作者:徐波
	日期:2016-10-18 13:09:34
	页面:生产计划明细增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<style type="text/css">
</style>
<script type="text/javascript">
</script>
<div id="produceSalesOrderSearchToolbar">
		<form id="produceSalesOrderForm">
			<input type="text" like="true" class="easyui-searchbox" editable="true" style="width:400px" name="filter[condition]" data-options="searcher:reloadProduceOrder" prompt="请输入订单号或者产品型号">
			<input type="hidden" id="_consumerName_salesOrder_detail" name="filter[consumerName]" value="">
		</form>
	</div>
	<table width="100%" id="orderProductSelect" idField="ID" toolbar="#produceSalesOrderSearchToolbar" singleSelect="false" title="" class="easyui-datagrid"  
			url="${path}planner/produce/order/list?all=1" 
			fitColumns="false" 
			fit="true"  
			data-options="
				onLoadSuccess:orderProductSelectLoadSuccess,
				rowStyler:rowStyler,
				onBeforeLoad:function(){
					if(typeof onBerforeLoad === 'function'){
					return onBerforeLoad();
					}
				},
				onDblClickRow:onOrderProductSelectDblClickRow,
				view:groupview,
                groupField:'SALESORDERCODE',
                groupFormatter:function(value,rows){
                    return '订单编号<font color=red>'+value + '</font> - ' + rows.length + ' 产品';
                }
			" >
			<thead frozen="true">
				<tr>
					<!-- 这是生产计划明细ID -->
					<th field="ID" checkbox=true ></th>
					<th field="SALESORDERSUBCODE" sortable="true" width="110">订单号</th>
					<th field="CONSUMERNAME" width="100">客户名称</th>
					<th field="CONSUMERPRODUCTNAME" width="80">客户产品名称</th>
					<th field="FACTORYPRODUCTNAME" width="80">厂内名称</th>
					<th field="PRODUCTMODEL" width="80">产品型号</th>
					<th field="PRODUCTCOUNT" width="80">产品数量</th>
					<th field="PRODUCTWIDTH" width="80">门幅(mm)</th>
					<th field="PRODUCTROLLLENGTH" width="80">卷长(m)</th>
					<th field="PRODUCTROLLWEIGHT" width="80">卷重(kg)</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="PRODUCTBATCHCODE" width="80">产品批次号</th>
					<th field="SALESORDERDATE" sortable="true" width="80" data-options="formatter:orderDateFormat">下单日期</th>
					<th field="USERNAME" width="80">业务员</th>
					<th field="SALESORDERISEXPORT" width="80" data-options="formatter:exportFormat">内销/外销</th>
					<th field="SALESORDERTYPE" width="80" data-options="formatter:orderTypeFormat">订单类型</th>
					<th field="SALESORDERTOTALMONEY" width="80" >订单总金额</th>
					<!-- <th field="SALESORDERDELIVERYTIME"  width="80" data-options="formatter:orderDateFormat">发货时间</th> -->
					<th field="PRODUCTPROCESSCODE" width="80">工艺标准代码</th>
					<th field="PRODUCTPROCESSBOMVERSION" width="80">工艺标准版本</th>
					<th field="PRODUCTPACKAGINGCODE" width="80">包装标准代码</th>
					<th field="PRODUCTPACKAGEVERSION" width="80">包装标准版本</th>
					<th field="PRODUCTROLLCODE" width="80">卷标签代码</th>
					<th field="PRODUCTBOXCODE" width="80">箱唛头代码</th>
					<th field="PRODUCTTRAYCODE" width="80">托唛头代码</th>
					<th field="DELIVERYTIME" width="80">发货时间</th>
					<th field="PRODUCTMEMO" width="80">备注</th>
				</tr>
			</thead>
</table>