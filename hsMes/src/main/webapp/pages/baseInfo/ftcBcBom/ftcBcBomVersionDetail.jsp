<!--
	作者:徐秦冬
	日期:2017-11-30 10:46:24
	页面:非套材包材bom明细JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>非套材包材bom明细</title>
<%@ include file="../../base/meta.jsp"%>
<%@ include file="ftcBcBomVersionDetail.js.jsp"%>
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
	<div region="west" split="true" title="非套材包材BOM列表" style="width:260px;">
		<div class="easyui-layout" fit=true>
			<div data-options="region:'north',split:false,border:false" class="datagrid-toolbar" style="height:75px;text-align: center;">
				<input id="searchInput" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searchInfo" editable="true" data-options="width:'90%',icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');findFtcBcBom();}}]">
				<input type="text" id="testPro" value="0" class="easyui-combobox" style="width: 90%" panelHeight="auto" editable="false" data-options="data: [
	                        {value:'0',text:'常规产品'},
	                        {value:'1',text:'变更试样'},
	                        {value:'2',text:'新品试样'}
                    	],onSelect:function(rec){
                    	findFtcBcBom();
        				}" >
				<div class="datagrid-toolbar" style="text-align: center;font-size:13px;font-weight:bolder;padding:5px;">
					<a href="javascript:void(0)" style="color:#0E2D5F;" onclick="expandAll()">展开</a> &nbsp; <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="collapseAll()">收缩</a> &nbsp; <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="reload(true)">刷新</a>
				</div>
			</div>
			<div data-options="region:'center'">
				<ul id="ftcBcBomTree" class="easyui-tree"></ul>
			</div>
		</div>
	</div>
	<div data-options="region:'center',border:false" style="position: relative;">
		<div id="toolbar">
			<jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="add" name="ids" />
				<jsp:param value="save" name="ids" />
				<jsp:param value="delete" name="ids" />
				<jsp:param value="icon-add" name="icons" />
				<jsp:param value="icon-save" name="icons" />
				<jsp:param value="icon-remove" name="icons" />
				<jsp:param value="增加" name="names" />
				<jsp:param value="保存" name="names" />
				<jsp:param value="删除" name="names" />
				<jsp:param value="add()" name="funs" />
				<jsp:param value="doSave()" name="funs" />
				<jsp:param value="doDelete()" name="funs" />
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="ftcBcBomVersionDetailSearchForm" autoSearchFunction="false">
					材料名称: <input type="text" name="filter[PACKMATERIALNAME]" like="true" class="easyui-textbox"> <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()"> 搜索 </a>
				</form>
			</div>
		</div>
		<!-- ,nowrap:false -->
		<table id="dg" singleSelect="false" title="非套材包材BOM明细列表" class="easyui-datagrid" url="" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="false" fit="true" data-options="onDblClickRow:onDblClickCell,onEndEdit:onEndEdit,onClickRow:onClickCell,nowrap:false">
			<thead>
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="PACKMATERIALCODE" width="15%" data-options="editor:{type:'textbox',options:{required:true}}">物料代码</th>
					<th field="PACKSTANDARDCODE" width="12%" data-options="editor:{type:'textbox',options:{}}">标准码</th>
					<th field="PACKMATERIALNAME" width="12%" data-options="editor:{type:'textbox',options:{required:true}}">材料名称</th>
					<th field="PACKMATERIALMODEL" width="15%" data-options="editor:{type:'textbox'}">规格</th>
					<th field="PACKUNIT" width="7%" data-options="editor:{type:'textbox',options:{required:true}}">单位</th>
					<th field="PACKMATERIALCOUNT" width="15%" data-options="editor:{type:'numberbox',options:{precision:0,required:true}}">数量</th>
					<th field="PACKMEMO" width="15%" data-options="editor:{type:'textbox'}" >备注</th>

				</tr>
			</thead>
		</table>
	</div>
	<div data-options="region:'east',border:true,split:true" title="非套材包材基本信息" style="height:100px;width:200px">
		<table id="rc" style="width: 99%;">
			<tr>
				<td class="title" style="text-align: left;">包装标准代码</td>
			</tr>
			<tr>
				<td id="packCode"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">适用客户</td>
			</tr>
			<tr>
				<td id="consumerName"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">卷径/cm</td>
			</tr>
			<tr>
				<td id="rollDiameter"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">每托卷数</td>
			</tr>
			<tr>
				<td id="rollsPerPallet"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">托长/cm</td>
			</tr>
			<tr>
				<td id="palletLength"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">托宽/cm</td>
			</tr>
			<tr>
				<td id="palletWidth"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">托高/cm</td>
			</tr>
			<tr>
				<td id="palletHeight"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">包材重量/kg</td>
			</tr>
			<tr>
				<td id="bcTotalWeight"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">塑料膜要求</td>
			</tr>
			<tr>
				<td id="requirement_suliaomo"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">摆放要求</td>
			</tr>
			<tr>
				<td id="requirement_baifang"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">打包带要求</td>
			</tr>
			<tr>
				<td id="requirement_dabaodai"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">标签要求</td>
			</tr>
			<tr>
				<td id="requirement_biaoqian" colspan="3"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">小标签要求</td>
			</tr>
			<tr>
				<td id="requirement_xiaobiaoqian" colspan="3"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">卷（箱）标签要求</td>
			</tr>
			<tr>
				<td id="requirement_juanbiaoqian" colspan="3"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">托标签要求</td>
			</tr>
			<tr>
				<td id="requirement_tuobiaoqian" colspan="3"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">缠绕要求</td>
			</tr>
			<tr>
				<td id="requirement_chanrao" colspan="3"></td>
			</tr>
			<tr>
				<td style="text-align: left;" class="title">其他要求</td>
			</tr>
			<tr>
				<td id="requirement_other" colspan="3"></td>
			</tr>
		</table>
	</div>

	<!-- 非套材包材BOM的菜单 -->
	<div id="mainMenu" class="easyui-menu" style="width:130px;">
		<div onclick="appendit(1)" data-options="iconCls:'icon-add'">添加</div>
		<div onclick="reload(true)" data-options="iconCls:'icon-reload'">刷新节点</div>
	</div>
	
	<!-- 1级菜单 -->
	<div id="tree1Menu" class="easyui-menu" style="width:130px;">
		<div onclick="appendit(2)" data-options="iconCls:'icon-add'">添加</div>
		<div onclick="editit()" data-options="iconCls:'icon-edit'">修改/查看</div>
		<div onclick="removeit()" data-options="iconCls:'icon-remove'">删除</div>
		<div onclick="reload(true)" data-options="iconCls:'icon-reload'">刷新节点</div>
	</div>
	
	<!-- 2级菜单 -->
	<div id="tree2Menu" class="easyui-menu" style="width:130px;">
		<div onclick="appendit(3)" data-options="iconCls:'icon-add'">添加</div>
		<div onclick="editit()" data-options="iconCls:'icon-edit'">修改/查看</div>
		<div onclick="removeit()" data-options="iconCls:'icon-remove'">删除</div>
		<div onclick="reload(true)" data-options="iconCls:'icon-reload'">刷新节点</div>
	</div>
	
	<!-- 3级菜单 -->
	<div id="tree3Menu" class="easyui-menu" style="width:130px;">
		<div onclick="appenditVersion()" data-options="iconCls:'icon-add'">添加</div>
		<div onclick="editit()" data-options="iconCls:'icon-edit'">修改/查看</div>
		<div onclick="removeit()" data-options="iconCls:'icon-remove'">删除</div>
		<div onclick="cancelBom()" data-options="iconCls:'icon-cancel'">作废</div>
		<div onclick="reload(true)" data-options="iconCls:'icon-reload'">刷新节点</div>
	</div>
	
	<!-- 版本上的菜单 -->
	<div id="treeMenuVersion" class="easyui-menu" style="width:130px;">
		<div onclick="edititVersion()" data-options="iconCls:'icon-edit'">修改</div>
		<div onclick="removeitVersion()" data-options="iconCls:'icon-remove'">删除</div>
		<!-- <div onclick="copyVersion()" data-options="iconCls:'platform-copy'">复制</div> -->
		<!-- <div onclick="updateByCode()" data-options="iconCls:'platform-copy'">批量更新</div> -->
		<div class="menu-sep"></div>
		<div onclick="commit()" data-options="iconCls:'icon-ok'">提交审核</div>
		<div onclick="view()" data-options="iconCls:'icon-tip'">查看审核</div>
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