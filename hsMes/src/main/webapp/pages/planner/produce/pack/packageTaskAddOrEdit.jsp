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
	var packCodes = ${empty codes?"[]":codes};
	function addPackType(){
		$("#pack_task_dg").datagrid("appendRow",{"ID":"","PACKBOMCODE":"","TRAY_COUNT":1});
	}
	function beginEdit(i,r){
		$(this).datagrid('beginEdit',i);
	}
	function validCode(){
		var _options = $(this).combobox('options');  
	    var _data = $(this).combobox('getData');/* 下拉框所有选项 */  
	    var _value = $(this).combobox('getValue');/* 用户输入的值 */  
	    var _b = false;/* 标识是否在下拉列表中找到了用户输入的字符 */  
	    for (var i = 0; i < _data.length; i++) {  
	        if (_data[i][_options.valueField] == _value) {  
	            _b=true;  
	            break;
	        }  
	    }  
	    if(!_b){  
	        $(this).combobox('setValue', '');
	        return;
	    }
	}
	
	function comboFilter(q, row){
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q) !=-1;
	}
	
	function codeFormatter(v,r,i){
		if(!v)return "";
		return r.PACKAGECODE;
	}
	
	function deleteRows(){
		EasyUI.grid.deleteSelected("pack_task_dg");
	}
	
</script>
<div id="pack_task_toolbar">
	<a class="easyui-linkbutton" iconCls="icon-add" onclick="addPackType()">添加包装</a>
	<a class="easyui-linkbutton" iconCls="platform-del3" onclick="deleteRows()">删除包装</a>
</div>
<!--包装任务列表表单-->
<table id="pack_task_dg" singleSelect="false" class="easyui-datagrid" toolbar="#pack_task_toolbar" pagination="false" rownumbers="true" fitColumns="false" fit="true" data-options="
	onClickRow:beginEdit,
	data:tasks">
	<thead>
		<tr>
			<th field="ID" checkbox=true></th>
			<th field="PACKBOMID" formatter="codeFormatter"  width="550" editor="{type:'combobox',options:{icons:[],filter:comboFilter,required:true,data:packCodes,valueField:'PACKAGEBOMID',textField:'PACKAGECODE',editable:true,onHidePanel:validCode,panelHeight:'100'}}">包装代码</th>
			<th field="TRAYCOUNT" width="60"  editor="{type:'numberspinner',options:{required:true,min:1,max:9999,precision:0}}">总托数</th>
		</tr>
	</thead>
</table>
