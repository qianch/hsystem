<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<title>选择人员信息</title>
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

	var _common_user_treeNode=null;
	var _did=${did};
	//查询
	function _common_user_filter() {
		EasyUI.grid.search("_common_user_dg", "_common_user_dg_form");
	}

	$(document).ready(function() {
		//加载树
		$.ajax({
			url : path + "department/list?all=1",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (Tip.hasError(data)) {
					return;
				}
				ZTree.init("zTreeUser", setting, data.rows, true);
				_common_user_treeNode =ZTree.getNode("zTreeUser", did);
			}
		});
		
		//设置数据表格的DetailFormatter内容
		$('#_common_user_dg').datagrid({
			onDblClickRow : function(index, row) {
				if (typeof _common_user_dbClickRow === "function") {
					_common_user_dbClickRow(index, row);
				} else {
					if (window.console) {
						console.log("没有为用户选择界面提供_common_user_dbClickRow方法，参数为index,row");
					}
				}
			},
			onLoadSuccess : function(data) {
				if (typeof _common_user_onLoadSuccess === "function") {
					_common_user_onLoadSuccess(data);
				} else {
					if (window.console) {
						console.log("未定义用户选择界面加载完成的方法：_common_user_onLoadSuccess(data)");
					}
				}
			}

		});
		
		if(_did!=null){
			JQ.setValue("#did", _did);
			EasyUI.grid.search("_common_user_dg", "_common_user_dg_form");
		}
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
				pIdKey : "PID"
			},
			key : {
				name : "NAME"
			}
		},
		callback : {
			onClick : zTreeOnClick
		}
	};

	var did = null;
	function zTreeOnClick(event, treeId, _common_user_treeNode) {
		// $.fn.zTree.getZTreeObj("zTreeUser").expandNode(_common_user_treeNode);
		did = _common_user_treeNode.ID;
		var ids=[];
		id=getChildren(ids,_common_user_treeNode);
		JQ.setValue("#did", id);
		reload();
	}

	function getChildren(ids, _common_user_treeNode) {
		if(_common_user_treeNode.ID!=undefined){
			ids.push(_common_user_treeNode.ID);
		}
		if (_common_user_treeNode.isParent) {
			for ( var obj in _common_user_treeNode.children) {
				getChildren(ids, _common_user_treeNode.children[obj]);
			}
		}
		return ids;
	}
	
	function sexFormatter(value, row, index) {
		return value == 0 ? '男' : '女';
	}

	function statusFormatter(value, row, index) {
		return value == 0 ? '启用' : '禁用';
	}
	
	function reload() {
		var ids = [];
		if (_common_user_treeNode == null) {
			EasyUI.grid.search("_common_user_dg", "_common_user_dg_form");
		} else {
			ids = getChildren(ids, _common_user_treeNode);
			JQ.setValue("#did", ids.toString());
			EasyUI.grid.search("_common_user_dg", "_common_user_dg_form");
		}
	}

</script>

<div  class="easyui-layout" data-options="fit:true,border:false">
 	<div region="west" split="true" title="部门列表" style="width:200px;">
		<ul id="zTreeUser"  class="ztree"></ul>
	</div>
	<div data-options="region:'center',border:false" style="position: relative;">
	<div id="_common_user_toolbar" >
		<div style="border-top:1px solid #DDDDDD">
			<form action="#" id="_common_user_dg_form" autoSearchFunction="false">
				<label class="panel-title">用户搜索：</label>
				<input type="hidden" id="did" name="filter[u.did]" in="true">
				用户名：<input type="text" id="userName" name="filter[u.userName]" like="true" value="" class="easyui-textbox">
				登录名称：<input type="text" id="loginName" name="filter[u.loginName]" like="true" value="" class="easyui-textbox">
				<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="_common_user_filter()">
						搜索
				</a>
			</form>
		</div>
    </div>
	 <table id="_common_user_dg" singleSelect="${empty singleSelect?'true':singleSelect }" fit="true" class="easyui-datagrid"  url="<%=basePath %>user/list" toolbar="#_common_user_toolbar" pagination="true" rownumbers="true" fitColumns="true">
        <thead>
            <tr>
            	<th field="ID" checkbox=true></th>
                <th field="USERNAME" width="15">姓名</th>
                <th field="USERCODE" width="15">编号</th>
                <th field="LOGINNAME" width="15">登录帐号</th>
                <th field="SEX" width="10" data-options="field:'sex',formatter:sexFormatter">性别</th>
                <th field="PHONE" width="15">电话</th>
                <th field="EMAIL" width="20">邮箱</th>
                <th field="NAME" width="20">所在部门</th>
                <th field="STATUS" width="20" data-options="field:'status',formatter:statusFormatter">状态</th>
            </tr>
        </thead>
    </table>
	</div>
</div>
