<!--
	作者:高飞
	日期:2017-5-26 10:04:05
	页面:包装任务列表增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<style type="text/css">
</style>
<script type="text/javascript">
	var tasks = ${empty packageTask?"[]":packageTask};
	var pid = ${pid};
	function codeFormatter(v,r,i){
		if(!v)return "";
		return r.PACKAGECODE;
	}
	function optFormatter(v,r,i){
		return "<span class=\"l-btn-left l-btn-icon-left\" style=\"cursor:hand\" onclick=\"doPrintTrayBarcode("+r.TRAYCOUNT+","+r.ID+")\"><span class=\"l-btn-text\">打印</span><span class=\"l-btn-icon icon-print\">&nbsp;</span></span>";
	}
	function doPrintTrayBarcode(){
		
		
		
	}
	
	function printTrayCountFormatter(v,r,i){
		return v||0;
	}
</script>
<form id="tray-print-form">
	<table style="width:100%;">
		<tr>
			<td class="title">选择打印机</td>
			<td>
				<ul class="easyui-combotree" required="true" style="width:250px"  panelHeight="300px" id="printerTree" data-options="url:'${path }/print/printers',method:'get',animate:true,lines:true"></ul>
			</td>
		</tr>
		<tr>
			<td class="title">打印数量</td>
			<td>
				<input class="easyui-numberspinner" required="true" style="width:250px" id="count" min=1>
			</td>
		</tr>
	</table>
</form>