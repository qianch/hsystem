<!--
	作者:徐波
	日期:2016-11-14 15:40:51
	页面:打印机信息增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<style type="text/css">
//
CSS


代码
</style>
<script type="text/javascript">
	//JS代码
	
	
</script>
<div>
	<!--打印机信息表单-->
	<form id="doPrintBarcodeForm" method="post" ajax="true"
		action="<%=basePath%>printer/buda" autocomplete="off">
		<input type="hidden" name="ids" value="${ids}">
		<table width="100%">
			<tr>
				<td class="title">选择打印机:</td>
				<td>
					<input id="pName" class="easyui-combobox" name="pName" style="width:95%"
    data-options="valueField:'value',textField:'text',url:'<%=basePath %>printer/getPrinterInfo'" required="true">
				</td>
			</tr>
		</table>
	</form>
</div>