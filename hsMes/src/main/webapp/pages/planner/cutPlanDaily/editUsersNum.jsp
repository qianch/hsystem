<!--
	作者:宋黎明
	日期:2016-12-13 13:38:47
	页面:编织计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<style type="text/css">
textarea {
	border-radius: 5px;
	height: 120px;
	resize: none;
	padding: 2px;
}
</style>
<script type="text/javascript">
	
</script>
<div style="height:100%;">
	<!--裁剪计划指定人员表单-->
	<!-- onDblClickRow:userRowClick -->
	<div id="userNumToolbar">
		<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="ChooseUser()" id="addDevice" iconCls="icon-add">增加人员</a> <a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="deleteUser()" id="deleteDevice" iconCls="icon-remove">移除人员</a>
	</div>
	<table id="userNumDg" fit="true" idField="" class="easyui-datagrid" singleSelect="true" title="" toolbar="#userNumToolbar" style="width:100%;height:250px" data-options="onClickRow:userNumRowClick">
		<thead>
			<tr>
				<th data-options="field:'USERNAME',width:100">人员名称</th>
				<th data-options="field:'NUM',width:80,editor:{type:'numberbox',options:{required:true,min:1,precision:0}}">数量</th>
			</tr>
		</thead>
	</table>
</div>

