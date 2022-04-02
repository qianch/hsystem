<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
	var tree = null;
	var treeListUrl = path + "deviceType/list";
	var deviceAdd = path + "deviceType/add";
	var deviceDelete = path + "deviceType/delete";
	var deviceEdit = path + "deviceType/edit";
	var categoryParentId=null;
 	var setting = {
		edit : {
			enable : false,
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
			onDrop : onDrop,
			onClick : onClick
		}
	};

 	function onDrop(event, treeId, treeNodes, targetNode, moveType) {
 		var categoryParentId = targetNode == null ? null : targetNode.ID;
 		var list = [];
 		for (var i = 0; i < treeNodes.length; i++) {
 			list.push({
 				id : treeNodes[i].ID,
 				categoryParentId : categoryParentId
 			});
 		}
 		$.ajax({
 			url : path + "deviceType/batchUpdateDeviceTypeLevel",
 			type : "post",
 			data : JSON.stringify(list),
 			dataType : "json",
 			contentType : "application/json",
 			success : function(data) {
 				if (Tip.hasError(data)) {
 					return;
 				}
 			}
 		});
 	};
	
	$(document).ready(function() {
		initTree(); 
	});
	
	function initTree(){
		$.ajax({
			url : treeListUrl + "?all=1",
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
		EasyUI.grid.search("dg", "deviceFilter");
	}
	
	function onClick(event, treeId, treeNode) {
		categoryParentId = treeNode.ID;
		JQ.setValue("#pid", categoryParentId);
		EasyUI.grid.search("dg", "deviceFilter");
	}

	var doDelete = function() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if (r.length == 0)
			return;
		Dialog.confirm(function() {
			var ids = [];
			var treeNode = ZTree.getNode("deviceTree", r.ID);
			getChildren(ids, treeNode);
			JQ.ajax(deviceDelete, "post", {
				ids : ids.toString()
			}, function(data) {
				if(Tip.hasError(data)){
					return;
				}
				EasyUI.grid.search("dg", "deviceFilter");
				ZTree.removeNode("deviceTree", r.ID);
			});
		},'确定删除?<br>删除的时候子类别也会一并删除!');
	};
	

	var add = function() {
		var node=ZTree.getNode("deviceTree", categoryParentId);
		
		if(node!=null&&node.DEPRECATED==1){
			Tip.warn("");
			return;
		} 
		var wid = Dialog.open("增加", 400, 99, deviceAdd+(categoryParentId==null?"":("?categoryParentId="+categoryParentId)), [ EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("deviceForm", deviceAdd, function(data) {
				//向树添加节点
				ZTree.addNode("deviceTree", categoryParentId, data);
				EasyUI.grid.search("dg", "deviceFilter");
				initTree(); 
				if(Dialog.isMore(wid)){
					Dialog.close(wid);
					add();
				}else{
					Dialog.close(wid);
				}
			});
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ],function(){
			Dialog.more(wid);
		});
	};

	// 编辑设备类别
	var edit = function() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if (r.length == 0)
			return;
		var wid = Dialog.open("编辑", 400, 99, deviceEdit+"?id="+r.ID, [ EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("deviceForm", deviceEdit, function(data) {
				console.log(data);
				ZTree.updateNode("deviceTree", "ID", data.ID, ["CATEGORY_NAME"], [ data.CATEGORYNAME]);
				EasyUI.grid.search("dg", "deviceFilter");
				initTree();
				Dialog.close(wid);
			});
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ]);
	};
	
	/**
	 * 双击行，弹出编辑
	 */
	var dbClickEdit=function(index,row){
		var wid = Dialog.open("编辑", 400, 99, deviceEdit+"?id="+row.ID, [ EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("deviceForm", deviceEdit, function(data) {
				console.log(data);
				ZTree.updateNode("deviceTree", "ID", data.ID, ["CATEGORY_NAME"], [ data.CATEGORYNAME]);
				EasyUI.grid.search("dg", "deviceFilter");
				initTree();
				Dialog.close(wid);
			});
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ]);
	}
	
</script>