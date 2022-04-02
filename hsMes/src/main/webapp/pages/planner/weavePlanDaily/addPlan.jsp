
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style type="text/css">
</style>
<script type="text/javascript">
	function searchPlanDg(){
		Loading.show();
		EasyUI.grid.search("planDg", "planDgFrom")
	}
</script>
<div id="planDgToolbar">
	<form id="planDgFrom" autoSearchFunction="false">
		销售单号:<input type="text" class="easyui-textbox" name="filter[pcode]" like="true">
		生产计划单号:<input type="text" class="easyui-textbox" name="filter[scode]" like="true">
		<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="searchPlanDg()" id="addPlan" iconCls="icon-search">搜索</a>
	</form>
</div>
<table id="planDg" toolbar="#planDgToolbar" class="easyui-datagrid" singleSelect="false" title="" url="<%=basePath%>weaveDailyPlan/findWeavePlan"  pagination="false" rownumbers="true" fit="true" fitColumns="true" data-options="onLoadSuccess:loadSuccessCallback">
	<thead>
		<tr>
			<th field="ID" checkbox="true" ></th>
			<th field="PRODUCTTYPE" sortable="true" width="15" formatter="formatterType">产品属性</th>
			<th field="PLANCODE" sortable="true" width="25">生产计划单号</th>
			<th field="SALESORDERCODE" sortable="true" width="25">销售订单号</th>
			<th field="BATCHCODE" sortable="true" width="15">批次号</th>
			<th field="PRODUCTMODEL" sortable="true" width="15">产品规格</th>
			<th field="BCBOMCODE" sortable="true" width="15">包装代码</th>
			<th field="BCBOMVERSION" sortable="true" width="15">包装版本</th>
			<th field="PRODUCTROLLLENGTH" sortable="true" width="15">卷长</th>
			<th field="PRODUCTROLLWEIGHT" sortable="true" width="15" formatter="processNumberFormatter">重量</th>
			<th field="COUNT" sortable="true" width="15">数量</th>
			<th field="CONSUMERNAME" sortable="true" width="15">客户名称</th>
		</tr>
	</thead>
</table>
