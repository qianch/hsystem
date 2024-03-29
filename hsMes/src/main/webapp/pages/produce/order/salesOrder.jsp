<!--
	作者:高飞
	日期:2016-10-13 11:06:42
	页面:销售订单JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>生产订单</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="salesOrder.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="createProduceTask" name="ids"/>
				<jsp:param value="platform-icon85" name="icons"/>
				<jsp:param value="生产任务单" name="names"/>
				<jsp:param value="createProduceTask()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="salesOrderSearchForm" autoSearchFunction="false">
					客户名称:<input type="text" name="filter[consumerName]" like="true" class="easyui-textbox">
					业务人员:<input type="text" name="filter[userName]" like="true" class="easyui-textbox">
					订单号码:<input type="text" name="filter[orderCode]" like="true" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
					<br>
					订单类型:<input type="text" name="filter[orderType]"class="easyui-combobox" data-options="valueField:'v',textField:'t',data:[{'v':'1','t':'常规'},{'v':'2','t':'试样'},{'v':'3','t':'新品'},{'v':'-1','t':'未知'}]">
					内销外销:<input type="text" name="filter[export]"class="easyui-combobox" data-options="valueField:'v',textField:'t',data:[{'v':'1','t':'内销'},{'v':'0','t':'外销'}]">
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="生产订单列表" class="easyui-datagrid"  url="${path}salesOrder/list?filter[auditState]=2" toolbar="#toolbar" pagination="true"  data-options="onDblClickRow:dbClickEdit,rowStyler:rowStyler,onClickRow:orderRowClick " >
			<thead>
				<tr>
					<!-- <th field="ID" checkbox=true ></th> -->
					<th field="SALESORDERCODE" sortable="true" width="15">订单号</th>
					<th field="SALESORDERDATE" sortable="true" width="15" data-options="formatter:orderDateFormat">下单日期</th>
					<th field="CONSUMERNAME" sortable="true" width="15">客户名称</th>
					<th field="USERNAME" sortable="true" width="15">业务员</th>
					<th field="SALESORDERISEXPORT" sortable="true" width="15" data-options="formatter:exportFormat">内销/外销</th>
					<th field="SALESORDERTYPE" sortable="true" width="15" data-options="formatter:orderTypeFormat">订单类型</th>
					<th field="SALESORDERTOTALMONEY" sortable="true" width="15" >订单总金额</th>
					<th field="SALESORDERDELIVERYTIME" sortable="true" width="15" data-options="formatter:orderDateFormat">发货时间</th>
					<th field="AUDITSTATE" sortable="true" width="15" formatter="formatterReviewState">订单审核状态</th>
					<th field="ISCLOSED" sortable="true" width="15" formatter="stateFormatter">订单状态</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="salesOrderMemo" data-options="region:'east',border:true" title="销售订单备注" style="width:200px;padding:5px;">
		
	</div>
</body>