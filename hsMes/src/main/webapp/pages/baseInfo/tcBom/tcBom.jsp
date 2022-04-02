<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>套材BOM</title>
<%@ include file="../../base/meta.jsp"%>
<%@ include file="tcBom.js.jsp"%>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div region="west" split="true" title="BOM列表" style="width:260px;">
		<div class="easyui-layout" data-options="fit:true"	>
			<div data-options="region:'north',split:false,border:false" class="datagrid-toolbar" style="height:95px;text-align: center;">
				<input id="searchInput" type="text" class="easyui-searchbox" prompt="请输入内容" searcher="searchInfo" editable="true" data-options="width:'90%',icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');findTcBom();}}]">
				<input type="text" id="testPro" value="0" class="easyui-combobox" style="width: 90%" panelHeight="auto" editable="false" data-options="
					data: [
	                        {value:'0',text:'常规产品'},
	                        {value:'1',text:'变更试样'},
	                        {value:'2',text:'新品试样'}
                    	],onSelect:function(rec){
                    		findTcBom();
        				},icons:[]" >
        		<div hidden=true>
        		<input type="text" id="state" value="" class="easyui-combobox" style="width: 90%" panelHeight="auto" editable="false"   data-options="data: [
	                        {value:'0',text:'未提交'},
	                        {value:'1',text:'审核中'},
	                        {value:'2',text:'通过'},
	                        {value:'-1',text:'未通过'}
	                        
                    	],onSelect:function(rec){
                    	findTcBom();
        				},icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');findTcBom();}}]" >	
        				</div>	
				<div class="datagrid-toolbar" style="text-align: center;font-size:13px;font-weight:bolder;padding:5px;">
					<a href="javascript:void(0)" style="color:#0E2D5F;" onclick="expandAll()">展开</a> &nbsp; <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="collapseAll()">收缩</a> &nbsp; <a href="javascript:void(0)" style="color:#0E2D5F;" onclick="reload()">刷新</a>
				</div>
			</div>
			<div data-options="region:'center'">
				<ul id="TcBomTree" class="easyui-tree"></ul>
			</div>
		</div>
	</div>
	<div data-options="region:'center',border:false">
		<!-- 部件明细toolbar -->
		<div id="toolbar">
			<jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="add" name="ids" />
				<jsp:param value="delete" name="ids" />
				<jsp:param value="save" name="ids" />
				<jsp:param value="icon-add" name="icons" />
				<jsp:param value="icon-remove" name="icons" />
				<jsp:param value="icon-save" name="icons" />
				<jsp:param value="增加" name="names" />
				<jsp:param value="删除" name="names" />
				<jsp:param value="保存" name="names" />
				<jsp:param value="add()" name="funs" />
				<jsp:param value="doDelete()" name="funs" />
				<jsp:param value="edit()" name="funs" />
			</jsp:include>
		</div>
		
		<!-- 部件成品重量胚布信息toolbar -->
		<div id="toolbarFW">
			<jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="addFW" name="ids" />
				<jsp:param value="deleteFW" name="ids" />
				<jsp:param value="saveFW" name="ids" />
				<jsp:param value="icon-add" name="icons" />
				<jsp:param value="icon-remove" name="icons" />
				<jsp:param value="icon-save" name="icons" />
				<jsp:param value="增加" name="names" />
				<jsp:param value="删除" name="names" />
				<jsp:param value="保存" name="names" />
				<jsp:param value="addFW()" name="funs" />
				<jsp:param value="doDeleteFW()" name="funs" />
				<jsp:param value="editFW()" name="funs" />
			</jsp:include>
		</div>
		
		<div style="height:55%;">
			<div class="easyui-tabs" fit="true" >
				<div title="部件明细列表">
					<table singleSelect="true" fit="true" id="partsDetails" remoteSort="false" fitColumns="true" pagination="true" style="width:100%;" class="easyui-datagrid" url="" toolbar="#toolbar" rownumbers="true" fitColumns="true" data-options="onClickRow:onDblClickCellD,onEndEdit:onEndEditD">
						<thead>
							<tr>
								<th field="I" checkbox=true ></th>
								<th field="PID" width="10" >产品ID</th>
								<!-- <th field="TCFINISHEDPRODUCTID" width="20" >成品ID</th> -->
								<th field="MATERIELCODE" sortable="true" width=20>物料编码</th>
								<th field="PRODUCTPROCESSNAME" sortable="true" width=20>工艺名称</th>
								<th field="PRODUCTPROCESSCODE" sortable="true" width=20>工艺代码</th>
								<th field="PRODUCTWIDTH" sortable="true" width=8>门幅(mm)</th>
								<th field="PRODUCTROLLLENGTH" sortable="true" width=8>米长(m)</th>
								<th field="PRODUCTROLLWEIGHT" width="20" sortable="true">定重(kg)</th>
								<th field="DRAWINGNO" sortable="true" width=10 data-options="editor:{type:'textbox',options:{required:false}}">图号</th>
								<th field="ROLLNO" width=10 data-options="editor:{type:'textbox',options:{required:false}}">卷号</th>
								<th field="LEVELNO" sortable="true" width=10 data-options="editor:{type:'textbox',options:{required:false}}">层数</th>
								<th field="SORTING" sortable="true" width=10 data-options="editor:{type:'numberspinner',options:{min:1,max:9999,precision:0,required:true}}">排序号</th>
								<th field="TCPROCBOMFABRICCOUNT" sortable="true" width=10 data-options="editor:{type:'numberbox',options:{precision:2,required:true}}">数量(卷/套)</th>
								<th field="TCTHEORETICALWEIGH" sortable="true" width=12 data-options="editor:{type:'numberbox',options:{precision:2,required:false}}">理论耗用重量(kg)</th>
							</tr>
						</thead>
					</table>
				</div>
				<div title="部件成品重量胚布信息">
					<table singleSelect="true" fit="true" id="partsFinishedWeightEmbryoCloth" remoteSort="false" fitColumns="true" pagination="true" style="width:100%;" class="easyui-datagrid" url="" toolbar="#toolbarFW" rownumbers="true" fitColumns="true" data-options="onClickRow:onDblClickCellFW,onEndEdit:onEndEditFW">
						<thead>
							<tr>
								<th field="ID" checkbox=true ></th>
								<th field="MATERIALNUMBER" sortable="true" width=20 data-options="editor:{type:'textbox',options:{required:true}}" >物料代码</th>
								<th field="EMBRYOCLOTHNAME" sortable="true" width=20 data-options="editor:{type:'textbox',options:{required:true}}">胚布名称</th>
								<th field="WEIGHT" sortable="true" width=20 data-options="editor:{type:'numberbox',options:{precision:2,required:true}}">重量(KG)</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
			
		</div>

		<div id="toolbar2">
			<jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="add1" name="ids" />
				<jsp:param value="delete1" name="ids" />
				<jsp:param value="save1" name="ids" />
				<jsp:param value="icon-add" name="icons" />
				<jsp:param value="icon-remove" name="icons" />
				<jsp:param value="icon-save" name="icons" />
				<jsp:param value="增加" name="names" />
				<jsp:param value="删除" name="names" />
				<jsp:param value="保存" name="names" />
				<jsp:param value="add1()" name="funs" />
				<jsp:param value="doDelete1()" name="funs" />
				<jsp:param value="edit1()" name="funs" />
			</jsp:include>
		</div>
		<div style="height:45%">
			<table singleSelect="true" fit="true" id="parts" pagination="true" title="版本部件列表" class="easyui-datagrid" url="" toolbar="#toolbar2" rownumbers="true" fitColumns="false" data-options="onDblClickRow:onDblClickCell,onEndEdit:onEndEdit,onClickRow:onClickCell,rowStyler:tcPartsRowStyler">
				<thead>
					<tr>
						<th field="ID" checkbox=true></th>
						<th field="TCPROCBOMVERSIONMATERIALNUMBER" sortable="true" width="100" data-options="editor:{type:'textbox',options:{required:true}}">部件物料号</th>
						<th field="TCPROCBOMVERSIONPARTSNAME" sortable="true" width="100" data-options="editor:{type:'textbox',options:{required:true}}">部件名称</th>
						<th field="CUSTOMERMATERIALCODE" sortable="true" width="100"  data-options="editor:{type:'textbox'}">客户物料号</th>
						<th field="TCPROCBOMVERSIONPARTSTYPE" sortable="true" width="100" data-options="editor:{type:'combobox',options:{required:true,data:[{value:'常规部件',text:'常规部件'},{value:'成品胚布',text:'成品胚布'},{value:'中小部件',text:'中小部件'}]}}">部件类型</th>
						<th field="TCPROCBOMVERSIONPARTSCUTCODE" sortable="true" width="100" data-options="editor:{type:'textbox'}">工艺代码</th>
						<th field="TCPROCBOMVERSIONPARTSCOUNT" sortable="true" width="160" data-options="editor:{type:'numberbox',options:{precision:2,required:true}}">部件数量</th>
						<th field="TCPROCBOMVERSIONPARTSSUBSCOUNT" sortable="true" width="160" data-options="editor:{type:'numberbox',options:{precision:2,required:true}}">小部件个数</th>
						<th field="TCPROCBOMVERSIONPARTSWEIGHT" sortable="true" width="160" data-options="editor:{type:'numberbox',options:{precision:2,required:true}}">部件重量(kg/套)</th>
						<th field="NEEDSORT" sortable="true" width="160" data-options="editor:{type:'combobox',options:{required:true,valueField:'v',textField:'t',data:[{v:'0',t:'是'},{v:'1',t:'否'}]}},formatter:function(v,r,i){return v==0?'是':'否';}">按序组套</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="treeMenuBom" class="easyui-menu" style="width:160px;">
		<div onclick="appendit()" data-options="iconCls:'icon-add'">添加</div>
		<div onclick="reload()" data-options="iconCls:'icon-reload'">刷新节点</div>
	</div>
	<div id="treeMenuV" class="easyui-menu" style="width:160px;">
		<div onclick="appendV()" data-options="iconCls:'icon-add'">添加</div>
		<div onclick="copyBom()" data-options="iconCls:'icon-cut'">复制</div>
		<div onclick="editVV()" data-options="iconCls:'icon-edit'">修改/查看</div>
		
		<div onclick="removeit()" data-options="iconCls:'icon-remove'">删除</div>
		<div onclick="reload()" data-options="iconCls:'icon-reload'">刷新节点</div>
	</div>
	<div id="treeMenu" class="easyui-menu" style="width:160px;">
		<div onclick="editB()" data-options="iconCls:'icon-edit'">修改</div>
		<div onclick="removeB()" data-options="iconCls:'icon-remove'">删除</div>
		<!-- <div onclick="copyVersion()" data-options="iconCls:'icon-cut'">复制</div> -->
		<div onclick="audit()" data-options="iconCls:'icon-ok'">提交审核</div>
		<div onclick="view()" data-options="iconCls:'icon-tip'">查看审核</div>
		<div onclick="forceEdit()" data-options="iconCls:'platform-arrow_switch'">工艺变更</div>
		<!-- <div class="menu-sep"></div>
		<div onclick="setDefult('tc',1)">默认</div>
		<div onclick="setDefult('tc',-1)">非默认</div>
		<div class="menu-sep"></div>
		<div onclick="setEnableState('tc',1)">启用</div>
		<div onclick="setEnableState('tc',0)">改版</div>
		<div onclick="setEnableState('tc',-1)">停用</div> -->
		<div class="menu-sep"></div>
		<div onclick="reload()" data-options="iconCls:'icon-reload'">刷新节点</div>
	</div>
</body>
</html>