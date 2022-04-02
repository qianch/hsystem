<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<%@ include file="../base/jstl.jsp"%>
	<%-- <%@ include  file="../base/meta.jsp" %> --%>
	<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/zTree_v3/css/zTreeStyle/zTreeStyle.css">
	<script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.exedit-3.5.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/ext/zTree.ext.js"></script>
<script type="text/javascript">
	//树
	var _common_device_treeListUrl = path + "deviceType/list";

	//查询
	function _common_device_filter() {
		EasyUI.grid.search("_common_device_dg", "_common_device_dg_form");
	}

	$(document).ready(function() {
		//加载树
		initTree(); 
		
		//设置数据表格的DetailFormatter内容
		$('#_common_device_dg').datagrid({
			url:'${path}device/list?filter[workShop]=${workShop}',
			onDblClickRow : function(index, row) {
				if (typeof _common_device_dbClickRow === "function") {
					_common_device_dbClickRow(index, row);
				} else {
					if (window.console) {
						console.log("没有为用户选择界面提供_common_device_dbClickRow方法，参数为index,row");
					}
				}
			},
			onLoadSuccess : function(data) {
				if (typeof _common_device_onLoadSuccess === "function") {
					_common_device_onLoadSuccess(data);
				} else {
					if (window.console) {
						console.log("未定义用户选择界面加载完成的方法：_common_device_onLoadSuccess(data)");
					}
				}
			}

		});
		
	});
	
	var setting = {
			edit : {
				enable : true,
				showRemoveBtn : false,
				showRenameBtn : false
			},
			data : {
				simpleData : {
					enable : true,
					idKey : "ID",
					pIdKey : "CATEGORYPARENTID"
				},
				key : {
					name : "CATEGORYNAME"
				}
			},
			callback : {
				onClick : onClick
			}
		};
		
		function initTree(){
			$.ajax({
				url : _common_device_treeListUrl + "?all=1",
				type : "get",
				dataType : "json",
				success : function(data) {
					if (Tip.hasError(data)) {
						return;
					}
					ZTree.init("deviceTree", setting, data.rows, true);
				}
			});
		}
		
		var getChildren = function(ids, treeNode) {
			ids.push(treeNode.ID);
			if (treeNode.isParent) {
				for ( var obj in treeNode.children) {
					getChildren(ids, treeNode.children[obj]);
				}
			}
			return ids;
		};

		function filter(){
			EasyUI.grid.search("dg", "deviceSearchForm");
		}
		
		//树列表单击事件
		function onClick(event,treeId,treeNode){
			pid=treeNode.PID;
		 	var ids=[];
			id=getChildren(ids,treeNode);
			JQ.setValue("#device_common_id",id);
			_common_device_filter();
		}
</script>

<div  class="easyui-layout" data-options="fit:true,border:false">
 	 <div region="west" split="true" title="设备类别" style="width:200px;">
			<ul id="deviceTree" class="ztree"></ul>
		</div>
	<div data-options="region:'center',border:false" style="overflow: false;position: relative; height: 140px; width: 925px">
		<div id="_common_device_toolbar">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="_common_device_dg_form" autoSearchFunction="false">
					<label class="panel-title">搜索：</label>
					<input type="hidden" id="device_common_id" name="filter[id]" in="true">
					<input type="hidden" id="device_common_workShop" name="filter[workShop]" like="true" value="">
					设备资产编号：<input type="text" name="filter[deviceAssetCode]" like="true" class="easyui-textbox">
					设备代码：<input type="text" name="filter[deviceCode]" like="true" class="easyui-textbox">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="_common_device_filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="_common_device_dg" idField="ID" singleSelect="${empty singleSelect?'true':singleSelect }"  title="设备信息列表" class="easyui-datagrid"  toolbar="#_common_device_toolbar" pagination="true" rownumbers="true" fitColumns="false" fit="true">
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="DEVICEASSETCODE" sortable="true" width="90">设备资产编号</th>
					<th field="SPECMODEL" sortable="true" width="80">规格型号</th>
					<th field="DEVICECODE" sortable="true" width="70">设备代码</th>
					<th field="DEVICENAME" sortable="true" width="100">资产名称</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<!-- <th field="DEVICECATETORYID" width="15">设备类别</th> -->
					<th field="DEVICEUNIT" sortable="true" width="80">单位</th>
					<th field="DEVICECOUNT" sortable="true" width="80">数量</th>
					<th field="DEVICESUPPLIER" sortable="true" width="80">供应商</th>
					<th field="DEVICEUSINGDATE" sortable="true" width="80">使用时间</th>
					<th field="DEPTNAME" sortable="true" width="80">所属部门</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
