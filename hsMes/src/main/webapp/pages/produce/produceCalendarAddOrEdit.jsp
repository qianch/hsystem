<!--
	作者:高飞
	日期:2016-11-1 13:05:53
	页面:排产日历增加或修改页面
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
	<!--排产日历表单-->
	<form id="produceCalendarForm" method="post" ajax="true" action="<%=basePath %>produceCalendar/${empty produceCalendar.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${produceCalendar.id}" />
		
		<table width="100%">
				<tr>
					<td class="title"><span style="color:red;">*</span>销售订单号:</td>
					<!--销售订单号-->
					<td>
						<input type="text" id="salesOrderCode" name="salesOrderCode" value="${produceCalendar.salesOrderCode}" class="easyui-searchbox" required="true" data-options="searcher:selectSalesOrder" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>开始时间:</td>
					<!--开始时间-->
					<td>
						<input type="text" id="startTime" name="startTime" value="${produceCalendar.startTime}" class="easyui-datebox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>结束时间:</td>
					<!--结束时间-->
					<td>
						<input type="text" id="endTime" name="endTime" value="${produceCalendar.endTime}" class="easyui-datebox" required="true" >
					</td>
				</tr>
			
		</table>
	</form>
</div>