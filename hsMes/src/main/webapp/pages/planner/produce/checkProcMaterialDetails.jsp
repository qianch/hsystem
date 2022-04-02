<!--
	作者:徐波
	日期:2017-01-03 15:09:34
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
	<table width="100%" id="procMaterials" idField="ID"  singleSelect="true" title="" class="easyui-datagrid"  
			url="${path}produce/procMaterials?ids=${deliveryPlan.auditState}"
			fitColumns="false" 
			fit="true"  
			 >
			<thead frozen="true">
				<tr>
					<!-- 这是生产计划明细ID -->
					<th field="ID" checkbox=true ></th>
					<th field="SALESORDERSUBCODE" sortable="true" width="110">订单号</th>
					<th field="PRODUCTROLLWEIGHT" width="80">卷重(kg)</th>
				</tr>
			</thead>

</table>