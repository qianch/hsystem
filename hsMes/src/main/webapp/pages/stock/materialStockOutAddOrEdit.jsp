<!--
	作者:肖文彬
	日期:2016-11-16 12:48:44
	页面:原料出库表增加或修改页面
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
	<!--原料出库表表单-->
	<form id="materialStockOutForm" method="post" ajax="true" action="<%=basePath %>materialStockOut/${empty materialStockOut.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${materialStockOut.id}" />
		
		<table width="100%">
				<tr>
					<td class="title"><span style="color:red;">*</span>出库单号:</td>
					<!--出库单号-->
					<td>
						<input type="text" id="outOrderCode" name="outOrderCode" value="${materialStockOut.outOrderCode}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>出库操作人:</td>
					<!--出库操作人-->
					<td>
						<input type="text" id="outOrderUserId" name="outOrderUserId" value="${materialStockOut.outOrderUserId}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>出库时间:</td>
					<!--出库时间-->
					<td>
						<input type="text" id="outOrderTime" name="outOrderTime" value="${materialStockOut.outOrderTime}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title">备注:</td>
					<!--备注-->
					<td>
						<input type="text" id="outOrderMemo" name="outOrderMemo" value="${materialStockOut.outOrderMemo}" class="easyui-textbox"  >
					</td>
				</tr>
			
		</table>
	</form>
</div>