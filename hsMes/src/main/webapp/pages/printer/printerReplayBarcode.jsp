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

CSS


代码
</style>
<script type="text/javascript">
	//JS代码


</script>
<div>
	<!--打印机信息表单-->
	<form id="doPrintBarcodeForm" method="post" ajax="true" action="<%=basePath%>individualprinter/doReplayPrintBarcode" autocomplete="off">
		<input type="hidden" name="customerId" value="${customerId}" />
		<input type="hidden" name="barCode" value="${barCode}" />
		<input type="hidden" name="type" value="${type}" />
		<input type="hidden" name="id" value="${id}" />

		<table width="100%">
			<tr>
				<td class="title">	条码编号：</td>
				<td>
					<label>${barCode}</label>
				</td>
			</tr>
			<tr>
				<td class="title">		车　　间：</td>
				<td>
					<input type="text" id="departmentCode" class="easyui-combobox" name="departmentCode"
						   data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=cut,weave'" data-options="panelHeight:'150'">
				</td>
			</tr>

			<tr>
				<td class="title">选择打印机:</td>
				<td>
					<input id="pName" class="easyui-combobox"  name="pName"  style="width:80%" data-options="panelHeight:'100',valueField:'value',textField:'text',url:'<%=basePath %>printer/getPrinterInfo'" required="true">
				</td>
			</tr>


			<tr>
				<input type="hidden" name="btwfileId" id="btwfileId">
				<td class="title">挑选版本:</td>
				<td>
					<input id="btwfileSelect" class="easyui-combobox"  style="width:80%"
						   data-options="panelHeight:'100',valueField:'v',textField:'t',url:'<%=basePath %>btwFile/queryBtwFilebyCustomerId?customerId=${customerId}&type=${type}',onLoadSuccess:function(data){
					      if(data.length>0){
					    	$('#btwfileSelect').combobox('select',data[0].value);
					       }
					    }, onSelect: function(rec){
					             $('#btwfileId').val(rec.v);
					        }"  required="true"
					>
				</td>
			</tr>

			<tr>
				<td class="title" >下载地址:</td>
				<td id="downLoad"></td>
			</tr>
		</table>
	</form>
</div>
