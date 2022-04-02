<!--
	作者:肖文彬
	日期:2016-10-18 13:38:47
	页面:编织计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<style type="text/css">
	textarea{
		border-radius:5px;
    	height:120px;
    	resize:none;
    	padding:2px;
    }
</style>
<script type="text/javascript">
	function formatterA(value,row){
		return '<div><a href="#"  onclick="detailed(' + row.ID+ ')" >删除</a></div>'
	}
	
	//结束datagrid行编辑
	function endEditing() {
		if (editIndex == undefined) {
			return true;
		}
		if ($('#dg').datagrid('validateRow', editIndex)) {
			$('#dg').datagrid('endEdit', editIndex);
			editIndex = undefined;
			addindex = 0;
			return true;
		} else {
			return false;
		}
	}

	//结束编辑
	function onEndEdit(index, row) {
		addindex = 0;
	}

	//datagrid双击编辑事件
	function onDblClickCell(index, field) {
		if (editIndex != index) {
			if (endEditing()) {
				$('#dg').datagrid('selectRow', index).datagrid('beginEdit', index);
				var ed = $('#dg').datagrid('getEditor', {
					index : index,
					field : field
				});
				if (ed) {
					($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
				}
				editIndex = index;
			} else {
				setTimeout(function() {
					$('#dg').datagrid('selectRow', editIndex);
				}, 0);
			}
		}
	}

	//datagrid单击结束编辑
	function onClickCell(index, field) {
		endEditing();
	}
	
	var deviceDgData=${empty deviceDg?"[]":deviceDg};
</script>
<div>
	<!--编织计划表单-->
	<form id="weavePlanForm" method="post" ajax="true" action="<%=basePath %>weavePlan/${empty weavePlan.id ?'add':'edit'}" autocomplete="off"  autoSearchFunction="false">
		
		<input type="hidden" name="id" value="${weavePlan.id}" />
		<input type="hidden" name="producePlanDetailId" value="${weavePlan.producePlanDetailId}" />
		<input type="hidden" name="salesOrderCode" value="${weavePlan.salesOrderCode}" />
		<input type="hidden" id="device" name="deviceIds" value="${deviceId}">
		<table width="100%">
				<tr>
					<td class="title">产品属性:</td>
					<!--产品属性-->
					<td>
						<input type="text" id="productType" name="productType" value="${weavePlan.productType}"  class="easyui-combobox" required="true"data-options="data: [
		                        {value:'1',text:'大卷产品'},
		                        {value:'2',text:'中卷产品'},
		                        {value:'3',text:'小卷产品'},
		                        {value:'4',text:'其他产品'}]">
					</td>
					<td class="title">总重量:</td>
				    <td>
				    	<input type="text" id="weight" name="requirementCount" value="${weavePlan.requirementCount}"  ${empty weavePlan.requirementCount?"":"readonly"} class="easyui-numberbox" min="1" required="true" >
				    </td>
				</tr>
				<tr>
					<td class="title">总托数:</td>
				    <td>
				    	<input type="text" id="productTrayCount" name="totalTrayCount" value="${weavePlan.totalTrayCount}" readonly="true" precision="0"  min="1" class="easyui-numberbox"   >
				    </td>
				    <td class="title">总卷数:</td>
				    <td>
				    	<input type="text" id="count" name="totalRollCount" value="${weavePlan.totalRollCount}" ${empty weavePlan.totalRollCount?"":"readonly"} min="1" class="easyui-numberbox" precision="0"  required="true"  >
				    </td>
				</tr>
				<tr>
					<td class="title">备注:</td>
					<td colspan="3">
						<textarea id="_comment" name="_comment" style="width:99%;" placeholder="最多输入1000字"></textarea>
					</td>
				</tr>
		</table>
		<div id="deviceToolbar">
			<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="ChooseDevice()" id="addDevice" iconCls="icon-add">增加机台</a>
			<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="deleteDevice()" id="deleteDevice" iconCls="icon-remove">移除机台</a>
		</div>
		<table id="deviceDg" idField="DEVICEID" class="easyui-datagrid" singleSelect="true" title="生产机台" style="width:100%;height:250px" toolbar="#deviceToolbar" data-options="onClickRow:deviceRowClick,onAfterEdit:onAfterEdit">
	        <thead>
	            <tr>
	            	<th data-options="field:'DEVICEID',checkbox:true"></th>
	                <th data-options="field:'DEVICENAME',width:80">机台名称</th>
	                <th data-options="field:'DEVICECODE',width:100">机台代码</th>
	                <th data-options="field:'PRODUCECOUNT',width:80,editor:{type:'numberbox',options:{required:true,min:1,precision:0}}">生产数量</th>
	            </tr>
	        </thead>
    	</table>
	</form>
</div>
