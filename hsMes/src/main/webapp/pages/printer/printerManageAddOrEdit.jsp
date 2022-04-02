<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>

<style type="text/css">
	//CSS 代码
</style>
<script type="text/javascript">
	$(function(){
		$('#departmentId').combobox({    
		    url:path+"printerManage/departmentCombo",    
		    valueField:'id',    
		    textField:'text' ,
		    panelHeight:160
		});  
	});
</script>
<div>
	<form id="printerManageForm" method="post" ajax="true" autocomplete="off"
			action="<%=basePath %>printerManage/${empty printer.id ?'add':'edit'}"  >
		<input type="hidden" name="id" value="${printer.id}" />
		<table width="100%">
			<tr>
				<td class="title"><span style="color:red;">*</span>打印机名称:</td>
				<td>
					<input type="text" id="printerName" name="printerName" value="${printer.printerName}" class="easyui-textbox" required="true" >
				</td>
			</tr>
			<tr>
				<td class="title"><span style="color:red;">*</span>打印机IP:</td>
				<td>
					<input type="text" id="printerIp" name="printerIp" value="${printer.printerIp}" class="easyui-textbox" required="true" >
				</td>
			</tr>
			<tr>
				<td class="title"><span style="color:red;">*</span>打印机显示名称:</td>
				<td>
					<input type="text" id="printerTxtName" name="printerTxtName" value="${printer.printerTxtName}" class="easyui-textbox" required="true" >
				</td>
			</tr>
			<tr>
				<td class="title"><span style="color:red;">*</span>部门:</td>
				<td>
					<input type="text" id="departmentId" name="departmentId" value="${printer.departmentId}" class="easyui-combobox" required="true" >
				</td>
			</tr>
		</table>
	</form>
</div>