<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/"; 
%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/zTree_v3/css/zTreeStyle/zTreeStyle.css">
<script type="text/javascript" src="<%=basePath%>resources/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/ext/zTree.ext.js"></script>
<style>
.layout-panel-east,.layout-panel-west {
	z-index: 0;
}
</style>
<script type="text/javascript">
	//点击的节点
	var _common_user_treeNode = null;
	//ztree 设置
	var _common_user_setting = {
		edit : {
			enable : true,
			showRemoveBtn : false,
			showRenameBtn : false
		},
		data : {
			simpleData : {
				enable : true,
				idKey : "ID",
				pIdKey : "PID"
			},
			key : {
				name : "NAME"
			}
		},
		callback : {
			onClick : _common_user_zTreeOnClick
		}
	};
	
	var _common_user_did = null;
	//点击事件
	function _common_user_zTreeOnClick(event, treeId, treeNode) {
		// $.fn.zTree.getZTreeObj("zTreeUser").expandNode(treeNode);
		_common_user_did = treeNode.ID;
		var ids = [];
		var id = _common_user_getChildren(ids, treeNode);
		JQ.setValue("#_common_user_did", id);
		_common_user_treeNode = treeNode;
		_common_user_reload();
	}
	
	//获取子节点
	function _common_user_getChildren(ids, treeNode) {
		if (treeNode.ID != undefined) {
			ids.push(treeNode.ID);
		}
		if (treeNode.isParent) {
			for ( var obj in treeNode.children) {
				_common_user_getChildren(ids, treeNode.children[obj]);
			}
		}
		return ids;
	}

	$(document).ready(
			//初始化部门数据
			function() {
				$.ajax({
					url : path + "department/list?all=1",
					type : "get",
					dataType : "json",
					success : function(data) {
						if (Tip.hasError(data)) {
							return;
						}
						ZTree.init("_common_user_zTreeUser", _common_user_setting, data.rows, true);
						_common_user_treeNode = ZTree.getNode("_common_user_zTreeUser", _common_user_did);
					}
				});
				//设置数据表格的DetailFormatter内容
				$('#_common_user_dg').datagrid(
						{
							onDblClickRow:function(index,row){
								if(typeof _common_user_dbClickRow==="function"){
									_common_user_dbClickRow(index,row);
								}else{
									if(window.console){
										console.log("没有为用户选择界面提供_common_user_dbClickRow方法，参数为index,row");
									}
								}
							},
							onLoadSuccess:function(data){
								if(typeof _common_user_onLoadSuccess==="function"){
									_common_user_onLoadSuccess(data);
								}else{
									if(window.console){
										console.log("未定义用户选择界面加载完成的方法：_common_user_onLoadSuccess(data)");
									}
								}
							},
							detailFormatter : function(rowIndex, rowData) {
								return "<div style='border:1px dotted gray;margin:5px;'><table style='width:100%;'>" + "<tr><td class='title'>员工编码</td><td>" + (rowData.STAFFCODE == null ? "" : rowData.STAFFCODE) + "</td><td class='title'>职务</td><td>"
										+ (rowData.STAFFJOB == null ? "" : rowData.STAFFJOB) + "</td><td  class='title'>家庭住址</td><td>" + (rowData.STAFFADDRESS == null ? "" : rowData.STAFFADDRESS) + "</td><td  class='title'>属性</td><td>" + (rowData.STAFFATTR == null ? "" : rowData.STAFFATTR)
										+ "</td></tr>" + "<tr><td  class='title'>其他一</td><td>" + (rowData.KEY1 == null ? "" : rowData.KEY1) + "</td><td  class='title'>其他二</td><td>" + (rowData.KEY2 == null ? "" : rowData.KEY2) + "</td><td  class='title'>其他三</td><td>"
										+ (rowData.KEY3 == null ? "" : rowData.KEY3) + "</td><td  class='title'>其他四</td><td>" + (rowData.KEY4 == null ? "" : rowData.KEY4) + "</td></tr>" + "<tr><td  class='title'>其他五</td><td>" + (rowData.KEY5 == null ? "" : rowData.KEY5)
										+ "</td><td  class='title'>其他六</td><td>" + (rowData.KEY6 == null ? "" : rowData.KEY6) + "</td><td  class='title'>其他七</td><td>" + (rowData.KEY7 == null ? "" : rowData.KEY7) + "</td><td  class='title'>其他八</td><td>" + (rowData.KEY8 == null ? "" : rowData.KEY8)
										+ "</td></tr>" + "<tr><td  class='title'>其他九</td><td>" + (rowData.KEY9 == null ? "" : rowData.KEY9) + "</td><td class='title'>其他十</td><td colspan='5'>" + (rowData.KEY10 == null ? "" : rowData.KEY1) + "</td></tr>" + "</table></div>";
							}
				});
				

			});
	function _common_user_filter() {
		_common_user_reload();
	}

	function _common_user_sexFormatter(value, row, index) {
		return value == 0 ? '男' : '女';
	}

	function _common_user_statusFormatter(value, row, index) {
		return value == 0 ? '启用' : '禁用';
	}

	function _common_user_reload() {
		var ids = [];
		if (_common_user_treeNode == null) {
			EasyUI.grid.search("_common_user_dg", "_common_user_userFilter");
		} else {
			ids = _common_user_getChildren(ids, _common_user_treeNode);
			JQ.setValue("#_common_user_did", ids.toString());
			EasyUI.grid.search("_common_user_dg", "_common_user_userFilter");
		}
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div region="west" split="true" title="部门列表" style="width:200px;">
		<ul id="_common_user_zTreeUser" class="ztree"></ul>
	</div>
	<div data-options="region:'center',border:false" style="position: relative;">
		<div id="_common_user_toolbar">
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="_common_user_userFilter" autoSearchFunction="false">
					<label class="panel-title">用户搜索：</label> <input type="hidden" id="_common_user_did" name="filter[u.did]" in="true"> 用户名：<input type="text" id="_common_user_userName" name="filter[u.userName]" like="true" value="" class="easyui-textbox"> 登录名称：<input type="text"
						id="_common_user_loginName" name="filter[u.loginName]" like="true" value="" class="easyui-textbox"> <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="_common_user_filter()"> 搜索 </a>
				</form>
			</div>
		</div>
		<table id="_common_user_dg" idField="ID" fit="true" singleSelect="${empty singleSelect?'true':singleSelect }" title="人员信息列表"  url="<%=basePath%>user/list?all=1" toolbar="#_common_user_toolbar" pagination="false" rownumbers="true" fitColumns="true" singleSelect="false">
			<thead>
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="USERNAME" width="15">姓名</th>
					<th field="LOGINNAME" width="15">登录帐号</th>
					<th field="SEX" width="10" data-options="field:'sex',formatter:_common_user_sexFormatter">性别</th>
					<th field="PHONE" width="15">电话</th>
					<th field="EMAIL" width="20">邮箱</th>
					<th field="NAME" width="20">所在部门</th>
					<th field="STATUS" width="20" data-options="field:'status',formatter:_common_user_statusFormatter">状态</th>
				</tr>
			</thead>
		</table>
	</div>
</div>