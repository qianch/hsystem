<!--
	作者:徐波
	日期:2016-10-8 16:53:24
	页面:包材bom明细JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>包材bom明细</title>
<%@ include file="../../base/meta.jsp"%>
<%@ include file="bcBomVersionDetail.js.jsp"%>
<style type="text/css">
td {
	min-width: 30px;
	padding: 3px;
}

.title {
	padding-right: 5px;
}

</style>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="west" split="true" title="包材BOM列表" style="width:260px;">
		<div class="easyui-layout" fit=true>
			<div data-options="region:'north',split:false,border:false" class="datagrid-toolbar" style="height:75px;text-align: center;">
				<input id="searchInput" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searchInfo" editable="true" data-options="width:'90%',icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');findBcBom();}}]">
				<input type="text" id="testPro" value="0" class="easyui-combobox" style="width: 90%" panelHeight="auto" editable="false" data-options="data: [
	                        {value:'0',text:'常规产品'},
	                        {value:'1',text:'变更试样'},
	                        {value:'2',text:'新品试样'}
                    	],onSelect:function(rec){
                    	findBcBom();
        				}" >
				<div class="datagrid-toolbar" style="text-align: center;font-size:13px;font-weight:bolder;padding:5px;">
					<a href="javascript:void(0)" style="color:#0E2D5F;" onclick="expandAll()">展开</a> &nbsp; <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="collapseAll()">收缩</a> &nbsp; <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="reload(true)">刷新</a>
				</div>
			</div>
			<div data-options="region:'center'">
				<ul id="BcBomTree" class="easyui-tree"></ul>
			</div>
		</div>
	</div>
	<div data-options="region:'center',border:false" style="position: relative;">
		<div id="toolbar">
			<jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="add" name="ids" />
				<jsp:param value="save" name="ids" />
				<jsp:param value="delete" name="ids" />
				<jsp:param value="excel" name="ids" />
				<jsp:param value="export" name="ids" />
				<jsp:param value="icon-add" name="icons" />
				<jsp:param value="icon-save" name="icons" />
				<jsp:param value="icon-remove" name="icons" />
				<jsp:param value="icon-excel" name="icons"/>
				<jsp:param value="icon-excel" name="icons"/>
				<jsp:param value="增加" name="names" />
				<jsp:param value="保存" name="names" />
				<jsp:param value="删除" name="names" />
				<jsp:param value="导入" name="names" />
				<jsp:param value="导出" name="names" />
				<jsp:param value="add()" name="funs" />
				<jsp:param value="doSave()" name="funs" />
				<jsp:param value="doDelete()" name="funs" />
				<jsp:param value="importDetail()" name="funs" />
				<jsp:param value="exportDetail()" name="funs" />
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="bcBomVersionDetailSearchForm" autoSearchFunction="false">
					包材名称: <input type="text" name="filter[PACKMATERIALNAME]" like="true" class="easyui-textbox"> <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()"> 搜索 </a>
				</form>
			</div>
		</div>
		<!-- ,nowrap:false -->
		<table id="dg" singleSelect="false" title="包材BOM明细列表" class="easyui-datagrid" url="" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="false" fit="true" data-options="onDblClickRow:onDblClickCell,onEndEdit:onEndEdit,onClickRow:onClickCell,nowrap:false">
			<thead>
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="PACKMATERIALNAME" width="13%" data-options="editor:{type:'textbox',options:{required:true}}">包材名称</th>
					<th field="PACKMATERIALMODEL" width="12%" data-options="editor:{type:'textbox',options:{}}">规格</th>
					<th field="PACKMATERIALATTR" width="7%" data-options="editor:{type:'textbox',options:{}}">材质</th>
					<th field="PACKMATERIALCOUNT" width="7%" data-options="editor:{type:'numberbox',options:{precision:0}}">数量</th>
					<th field="PACKMATERIALUNIT" width="7%" data-options="editor:{type:'textbox',options:{}}">物料单位</th>
					<th field="PACKUNIT" width="7%" data-options="editor:{type:'textbox',options:{}}">包装单位</th>
					<th field="PACKREQUIRE" width="32%" data-options="editor:{type:'textbox',options:{}}" >包装要求</th>
					<th field="PACKMEMO" width="15%" data-options="editor:{type:'textbox'}" >备注</th>

				</tr>
			</thead>
		</table>
	</div>
	<div data-options="region:'east',border:true,split:true" title="包材基本信息" style="height:100px;width:200px">
		<table style="width: 99%;">
			<tr>
				<td class="title" style="text-align: left;">总称</td>
			</tr>
			<tr>
				<td id="PACKBOMGENERICNAME"></td>
			</tr>
			<!-- <tr>
				<td style="text-align: left;" class="title">包装名称</td>
			</tr>
			<tr>
				<td id="PACKBOMNAME"></td>
			</tr> -->
			<tr>
				<td style="text-align: left;" class="title">包装大类</td>
			</tr>
			<tr>
				<td id="PACKBOMTYPE"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">门幅</td>
			</tr>
			<tr>
				<td id="PACKBOMWIDTH"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">卷重</td>
			</tr>
			<tr>
				<td id="PACKBOMWEIGHT"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">每箱卷数</td>
			</tr>
			<tr>
				<td id="PACKBOMROLLSPERBOX"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">每托箱数</td>
			</tr>
			<tr>
				<td id="PACKBOMBOXESPERTRAY"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">包装标准代码</td>
			</tr>
			<tr>
				<td id="PACKBOMCODE"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">适用客户</td>
			</tr>
			<tr>
				<td id="PACKBOMCONSUMERID"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">产品规格</td>
			</tr>
			<tr>
				<td id="PACKBOMMODEL"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">卷长</td>
			</tr>
			<tr>
				<td id="PACKBOMLENGTH"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">卷径</td>
			</tr>
			<tr>
				<td id="PACKBOMRADIUS"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">每托卷数</td>
			</tr>
			<tr>
				<td id="PACKBOMROLLSPERTRAY" colspan="3"></td>
			</tr>
		</table>
	</div>

	<div id="mainMenu" class="easyui-menu" style="width:130px;">
		<div onclick="appendit()" data-options="iconCls:'icon-add'">添加</div>
		<div onclick="reload(true)" data-options="iconCls:'icon-reload'">刷新节点</div>
	</div>
	<div id="treeMenu" class="easyui-menu" style="width:130px;">		
		<div onclick="appenditVersion()" data-options="iconCls:'icon-add'">添加</div>
		<div onclick="editit()" data-options="iconCls:'icon-edit'">修改/查看</div>
		<div onclick="removeit()" data-options="iconCls:'icon-remove'">删除</div>
		<div onclick="copyBom()" data-options="iconCls:'platform-copy'">复制</div>
		<div onclick="cancelBom()" data-options="iconCls:'icon-add'">作废</div>
		<div onclick="reload(true)" data-options="iconCls:'icon-reload'">刷新节点</div>

	</div>
	<div id="treeMenuVersion" class="easyui-menu" style="width:130px;">
		<div onclick="edititVersion()" data-options="iconCls:'icon-edit'">修改</div>
		<div onclick="removeitVersion()" data-options="iconCls:'icon-remove'">删除</div>
		<!-- <div onclick="copyVersion()" data-options="iconCls:'platform-copy'">复制</div> -->
		<!-- <div onclick="updateByCode()" data-options="iconCls:'platform-copy'">批量更新</div> -->
		<div class="menu-sep"></div>
		<div onclick="commit()" data-options="iconCls:'icon-ok'">提交审核</div>
		<div onclick="view()" data-options="iconCls:'icon-tip'">查看审核</div>
		<div onclick="forceEdit()" data-options="iconCls:'platform-arrow_switch'">工艺变更</div>
		<!-- <div class="menu-sep"></div>
		<div onclick="setDefult('bc',1)">默认</div>
		<div onclick="setDefult('bc',-1)">非默认</div>
		<div class="menu-sep"></div>
		<div onclick="setEnableState('bc',1)">启用</div>
		<div onclick="setEnableState('bc',0)">改版</div>
		<div onclick="setEnableState('bc',-1)">停用</div> -->
		<div class="menu-sep"></div>
		<div onclick="reload(true)" data-options="iconCls:'icon-reload'">刷新节点</div>
	</div>
</body>