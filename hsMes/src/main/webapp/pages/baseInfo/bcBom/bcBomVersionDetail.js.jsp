<!--
	作者:徐波
	日期:2016-10-8 16:53:24
	页面:包材bom明细JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	//添加包材bom明细
	var addUrl = path + "bom/packaging/add";
	//编辑包材bom明细
	var editUrl = path + "bom/packaging/edit";
	var saveUrl = path + "bom/packaging/saveDetail";
	//删除包材bom明细
	var deleteUrl = path + "bom/packaging/delete";
	var bcBomtreeUre = path + "bom/packaging/listBom";
	var isFirst = true;
	var commitAudit = path + "selector/commitAudit";
	var commitUrl = path + "bom/packaging/commit";
	//查看bom下有没有版本
	var findV = path + "bom/packaging/findV";
	//查询
	function filter() {
		EasyUI.grid.search("dg", "bcBomVersionDetailSearchForm");
	}
	//根据选项框选择的是试样还是正式的bom，改变获取bom的url
	function isgetTestPro(){
		var value=$("#testPro").combobox('getValue');
		if(value==0){
			bcBomtreeUre = path + "bom/packaging/listBom";
		}else if(value==1){
			bcBomtreeUre = path + "bom/packaging/listBomTest";
		}else{
			bcBomtreeUre = path + "bom/packaging/listBomTest1";	
		}
	}
	
	function add_bomDetail_data(r) {
		var _row = {
			"ID" : r.ID,
			"PACKMATERIALNAME" : r.PACKMATERIALNAME,
			"PACKMATERIALMODEL" : r.PACKMATERIALMODEL,
			"PACKMATERIALATTR" : r.PACKMATERIALATTR,
			"PACKMATERIALCOUNT" : r.PACKMATERIALCOUNT,
			"PACKMATERIALUNIT" : r.PACKMATERIALUNIT,
			"PACKUNIT" : r.PACKUNIT,
			"PACKREQUIRE" : r.PACKREQUIRE,
			"PACKMEMO" : r.PACKMEMO
		};
		$("#dg_cutPlan").datagrid("appendRow", _row);
	}
	//左边的tree
	var addVersionUrl = path + "bom/packaging/addBomVersion";
	var editVersionUrl = path + "bom/packaging/editBomVersion";
	var deleteVersionUrl = path + "bom/packaging/deleteBomVersion";
	//添加版本信息
	function appenditVersion() {
		var t = $('#BcBomTree');
		var node = t.tree('getSelected');
		JQ.ajax(findV, "post", {
			id : node.id
		}, function(data) {
		if(data==1){
			Tip.warn("此bom下存在版本无法添加");
			return;
		}
		var wid = Dialog.open("添加", 380, 150, addVersionUrl, [ EasyUI.window.button("icon-save", "保存", function() {
			
			JQ.setValue("#packBomId", node.id);

			EasyUI.form.submit("bCBomVersionForm", addVersionUrl, function(data) {
				reload(true);
				if (Dialog.isMore(wid)) {

					Dialog.close(wid);
					add();

				} else {
					Dialog.close(wid);
				}
			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid)
		}) ], function() {
			Dialog.more(wid);
		});
		});
	}
	var copyUrl = path + "bom/packaging/copyVersion";
	//复制版本信息
	function copyVersion() {
		var t = $('#BcBomTree');
		var node = t.tree('getSelected');
		Dialog.confirm(function() {
			JQ.ajax(copyUrl, "post", {
				ids : node.id,
				name : node.attributes.PACKVERSION + "(复制)"
			}, function(data) {

				reload(false);
			});
		});
	}

	var updateByCodeUrl = path + "bom/packaging/updateByCode";
	//复制版本信息
	function updateByCode() {
		var t = $('#BcBomTree');
		var node = t.tree('getSelected');
		Dialog.confirm(function() {
			JQ.ajax(updateByCodeUrl, "post", {
				ids : node.id,
				name : node.attributes.PACKVERSION
			}, function(data) {
				reload(false);
			});
		});
	}
	var copyBomUrl = path + "bom/packaging/copyBom";
	//复制版本信息
	function copyBom() {
		var t = $('#BcBomTree');
		var node = t.tree('getSelected');
		Dialog.confirm(function() {
			JQ.ajax(copyBomUrl, "post", {
				ids : node.id
			}, function(data) {
				reload(false);
			});
		});
	}

	function edititVersion() {

		var t = $('#BcBomTree');
		var node = t.tree('getSelected');
		var wid = Dialog.open("编辑", 380, 250, editVersionUrl + "?id=" + node.id, [ EasyUI.window.button("icon-save", "保存", function() {

			EasyUI.form.submit("bCBomVersionForm", editVersionUrl, function(data) {

				filter();
				Dialog.close(wid);
				reload(false);
			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid)
		}) ], function() {
			Dialog.more(wid);
		});
	}
	function removeitVersion() {
		var node = $('#BcBomTree').tree('getSelected');
		Dialog.confirm(function() {
			JQ.ajax(deleteVersionUrl, "post", {
				ids : node.id
			}, function(data) {
				if(data.error!=null&&data.error!=undefined){
					return;
				}
				$('#BcBomTree').tree('remove', node.target);
			});
		});
	}
	
	function reload1() {
		isgetTestPro();
		var node = $("#BcBomTree").tree("getParent", $('#BcBomTree').tree('getSelected').target);
		if (node) {
			$('#BcBomTree').tree('reload', node.target);
		} else {
			$('#BcBomTree').tree('reload');
		}
	}

	var data = [ {
		"id" : 1,
		"text" : "包材BOM",
		"state" : "closed",
		"attributes" : {
			'nodeType' : 'root'
		}
	} ];
	$(function() {
		$('#BcBomTree').tree({
			url : bcBomtreeUre,
			data : data,
			method : 'get',
			animate : true,
			onBeforeLoad : function(node, param) {
				if (isFirst) {
					isFirst = false;
					return false;
				} else {
					return true;
				}
			},
			formatter : auditTreeStyler,
			onContextMenu : function(e, node) {
				e.preventDefault();
				$(this).tree('select', node.target);
				if (node.attributes.nodeType == "root") {
					$('#mainMenu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				} else if (node.attributes.nodeType == "bom") {
					$('#treeMenu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});

				} else if (node.attributes.nodeType == "version") {
					var item = $('#treeMenuVersion').menu('findItem', '提交审核');
					var item1 = $('#treeMenuVersion').menu('findItem', '修改');
					var item2 = $('#treeMenuVersion').menu('findItem', '删除');
					var item3 = $('#treeMenuVersion').menu('findItem', '工艺变更');
					var item3=$('#treeMenuVersion').menu('findItem', '工艺变更');
					if (node.attributes.AUDITSTATE == 1) {
						$('#treeMenu').menu('hideItem', item.target);
						$('#treeMenu').menu('hideItem', item1.target);
						$('#treeMenu').menu('hideItem', item2.target);
						$('#treeMenu').menu('hideItem', item3.target);
					} else if (node.attributes.AUDITSTATE == 2) {
						$('#treeMenu').menu('hideItem', item.target);
						$('#treeMenu').menu('hideItem', item1.target);
						$('#treeMenu').menu('hideItem', item2.target);
						$('#treeMenu').menu('showItem', item3.target);
					} else {
						$('#treeMenu').menu('showItem', item.target);
						$('#treeMenu').menu('showItem', item1.target);
						$('#treeMenu').menu('showItem', item2.target);
						$('#treeMenu').menu('hideItem', item3.target);
						//item.hidden="true";
					}
					$('#treeMenuVersion').menu('show', {
						left : e.pageX,
						top : e.pageY
					});

				}

			},
			onClick : function(node) {
				if (node.state == "open") {
					collapse();
				} else {
					expand();
				}
				//getTreeChildrens();
				//获取节点的attribute属性，进行展示或者在datagrid进行查询
				var detail = node.attributes;
				if (detail.nodeType == "root") {
					$("#dg").datagrid('loadData', {
						total : 0,
						rows : []
					});
				} else if (detail.nodeType == "bom") {
					$("#dg").datagrid('loadData', {
						total : 0,
						rows : []
					});
					$('#PACKBOMGENERICNAME').html(detail.PACKBOMGENERICNAME);

					$('#PACKBOMNAME').html(detail.PACKBOMNAME);

					$('#PACKBOMCODE').html(detail.PACKBOMCODE);

					$('#PACKBOMTYPE').html(detail.PACKBOMTYPE);

					$('#PACKBOMCONSUMERID').html(detail.comsumerName);

					$('#PACKBOMMODEL').html(detail.PACKBOMMODEL);

					$('#PACKBOMWIDTH').html(detail.PACKBOMWIDTH);

					$('#PACKBOMLENGTH').html(detail.PACKBOMLENGTH);

					$('#PACKBOMWEIGHT').html(detail.PACKBOMWEIGHT);

					$('#PACKBOMRADIUS').html(detail.PACKBOMRADIUS);

					$('#PACKBOMROLLSPERBOX').html(detail.PACKBOMROLLSPERBOX);

					$('#PACKBOMBOXESPERTRAY').html(detail.PACKBOMBOXESPERTRAY);

					$('#PACKBOMROLLSPERTRAY').html(detail.PACKBOMROLLSPERTRAY);

				} else if (detail.nodeType == "version") {
					var pNode = $(this).tree('getParent', node.target);
					detail = pNode.attributes;
					$('#PACKBOMGENERICNAME').html(detail.PACKBOMGENERICNAME);

					$('#PACKBOMNAME').html(detail.PACKBOMNAME);

					$('#PACKBOMCODE').html(detail.PACKBOMCODE);

					$('#PACKBOMTYPE').html(detail.PACKBOMTYPE);

					$('#PACKBOMCONSUMERID').html(detail.comsumerName);

					$('#PACKBOMMODEL').html(detail.PACKBOMMODEL);

					$('#PACKBOMWIDTH').html(detail.PACKBOMWIDTH);

					$('#PACKBOMLENGTH').html(detail.PACKBOMLENGTH);

					$('#PACKBOMWEIGHT').html(detail.PACKBOMWEIGHT);

					$('#PACKBOMRADIUS').html(detail.PACKBOMRADIUS);

					$('#PACKBOMROLLSPERBOX').html(detail.PACKBOMROLLSPERBOX);

					$('#PACKBOMBOXESPERTRAY').html(detail.PACKBOMBOXESPERTRAY);

					$('#PACKBOMROLLSPERTRAY').html(detail.PACKBOMROLLSPERTRAY);

					$("#dg").datagrid({
						url : encodeURI(path + "bom/packaging/list" + "?filter[id]=" + node.attributes.ID)
					});
					$('#dg').datagrid('reload');

				}

			},
			onBeforeExpand : function(node, param) {

				$('#BcBomTree').tree('options').url = bcBomtreeUre + "?nodeType=" + node.attributes.nodeType;// change the url                       
				//param.myattr = 'test';    // or change request parameter
			},

		});
		$('#BcBomTree').tree('expand', $('#BcBomTree').tree('getRoot').target);
	});

	/**
	 *工艺变更
	 */
	function forceEdit(){
		var node=$("#BcBomTree").tree('getSelected');
		
		Dialog.confirm(function(){
			Loading.show();
			
			JQ.ajaxPost(path+"bom/modify",{id:node.id,type:"BC"},function(){
				Loading.hide();
				Tip.success("工艺变更中!");
				reload(false);
			},function(){
				Loading.hide();
			});

		},"确认变更?变更期间，销售将无法下达相关产品的订单！");
		
	}
	
	var dialogWidth = 700, dialogHeight = 350;
	function commit() {
		var r = $('#BcBomTree').tree('getSelected');
		var id = r.id;
		dialogId = Dialog.open("审核", 700, 100, commitAudit + "?id=" + id, [ EasyUI.window.button("icon-save", "提交审核", function() {
			EasyUI.form.submit("editAuditProduce", commitUrl, function(data) {
				if (Dialog.isMore(dialogId)) {
					Dialog.close(dialogId);
				} else {
					Dialog.close(dialogId);
				}
				reload(false);
			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(dialogId);
		}) ], function() {
			var node = $('#BcBomTree').tree('getSelected');
			var pNode = $('#BcBomTree').tree('getParent', node.target);
			if(pNode.attributes.ISTESTPRO==0){
			  $("#editAuditProduce #name").textbox("setValue", "常规包材BOM审核，编号：" + pNode.text + " " + "版本：" + node.text);
			}else if(pNode.attributes.ISTESTPRO==1){
			  $("#editAuditProduce #name").textbox("setValue", "变更试样包材BOM审核，编号：" + pNode.text + " " + "版本：" + node.text);	
			}else{
			  $("#editAuditProduce #name").textbox("setValue", "新品试样包材BOM审核，编号：" + pNode.text + " " + "版本：" + node.text);	

			}	
			Dialog.more(wid);
		});
	}
	function reload(isSelf) {
		isgetTestPro();
		var node = $('#BcBomTree').tree('getSelected');

		if (isSelf) {
			$('#BcBomTree').tree('reload', node.target);
		} else {
			var pNode = $('#BcBomTree').tree('getParent', node.target);
			$('#BcBomTree').tree('reload', pNode.target);
		}
	}

	//树列表收缩
	function collapse() {
		var node = $('#BcBomTree').tree('getSelected');
		$('#BcBomTree').tree('collapse', node.target);
	}

	//树列表展开
	function expand() {
		var node = $('#BcBomTree').tree('getSelected');
		$('#BcBomTree').tree('expand', node.target);
	}
	function appendit() {
		
		var wid = Dialog.open("添加", 900, 380, path + "/bom/packaging/addBom", [ EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("bcBomForm", path + "/bom/packaging/addBom", function(data) {
				reload(true);
				if (Dialog.isMore(wid)) {
					Dialog.close(wid);
					add();
					
				} else {
					Dialog.close(wid);
				}
			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid)
		}) ], function() {

			Dialog.more(wid);
		});

	}
	function editit() {
		var t = $('#BcBomTree');
		var node = t.tree('getSelected');

		var wid = Dialog.open("编辑", 380, 380, path + "/bom/packaging/editBom" + "?id=" + node.id, [ EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("bcBomForm", path + "/bom/packaging/editBom", function(data) {
				if (Tip.hasError(data)) {
					Loading.hide();
					return;
				}
				Dialog.close(wid);
				searchInfo();
				reload1();
				
				
			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid)
		}) ], function() {
			$("#" + wid).dialog("maximize");
			Dialog.more(wid);
		});
	}
	function removeit() {
		var node = $('#BcBomTree').tree('getSelected');
		Dialog.confirm(function() {
			JQ.ajax(path + "/bom/packaging/deleteBom", "post", {
				ids : node.id
			}, function(data) {
				if(data.error!=null&&data.error!=undefined){
					return;
				}
				$('#BcBomTree').tree('remove', node.target);
			});
		});

	}
	
	function cancelBom() {
		var node = $('#BcBomTree').tree('getSelected');
		Dialog.confirm(function() {
			JQ.ajax(path + "/bom/packaging/cancelBom", "post", {
				ids : node.id
			}, function(data) {
				Loading.hide();
				Tip.success("作废成功!");
				reload(false);
			},function(){
				Loading.hide();
			});
		});

	}
	//行编辑
	var editIndex = undefined;
	var addindex = 0;
	function endEditing() {
		if (editIndex == undefined) {
			return true
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
		var t = $('#BcBomTree');
		var node = t.tree('getSelected');
		var rowstr = {
			"id" : row.ID,
			"packMaterialName" : row.PACKMATERIALNAME,
			"packMaterialModel" : row.PACKMATERIALMODEL,
			"packMaterialAttr" : row.PACKMATERIALATTR,
			"packMaterialCount" : row.PACKMATERIALCOUNT,
			"packMaterialUnit" : row.PACKMATERIALUNIT,
			"packUnit" : row.PACKUNIT,
			"packRequire" : row.PACKREQUIRE,
			"packMemo" : row.PACKMEMO,
			"packVersionId" : node.id

		};
		$.ajax({
			url : saveUrl,
			type : 'post',
			dataType : 'json',
			contentType : 'application/json',
			data : JSON.stringify(rowstr),
			success : function(data) {
				$('#dg').datagrid('reload');
			}
		});

	}

	function onClickCell(index, field) {
		endEditing();
	}

	//datagrid单击事件
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

	//添加包材bom明细
	//datagrid行添加事件

	function add() {
		var node = $('#BcBomTree').tree('getSelected');
		if (node == null) {
			Tip.warn("请先选择BOM版本！");
		} else if (node.attributes.nodeType == "version") {
		 	if (addindex == 0) { 
				$("#dg").datagrid('insertRow', {
					index : 0,
					row : {}
				});
				$("#dg").datagrid('beginEdit', 0);
				//$("#dg").datagrid('selectRow',0);
				editIndex = 0;
				addindex = 1;
			 } 
		} else {
			Tip.warn("请先选择BOM版本！");
		}
	}
	var bomDetailExcel = path + "bom/packaging/upload";
	function importDetail() {
		var node = $('#BcBomTree').tree('getSelected');
		if (node == null) {
			Tip.warn("请先选择BOM版本！");
		} else if (node.attributes.nodeType == "version") {
			if (addindex == 0) {
				dialogId = Dialog.open("导入", 550, 300, bomDetailExcel, [EasyUI.window.button("icon-save", "导入", function () {
					excel(node.id);
					filter();
					Dialog.close(dialogId);
				}), EasyUI.window.button("icon-cancel", "关闭", function () {
					Dialog.close(dialogId)
				})], function () {

				});
			}
		} else {
			Tip.warn("请先选择BOM版本！");
		}
	}
	function exportDetail(){
		location.href= encodeURI(path +  "bom/packaging/export");
	}
	function excel(id) {
		var cutTcBomUploadFile = $("#cutTcBomUploadFile");
		if (cutTcBomUploadFile.val().length <= 0) {
			alert("请选择文件");
			return false;
		}


		var filepath = cutTcBomUploadFile.val();
		var extStart = filepath.lastIndexOf(".");
		var ext = filepath.substring(extStart, filepath.length).toUpperCase();
		if (ext != ".XLSX" && ext != ".XLS" && ext != ".XLSM") {
			alert("请上传excel格式文档");
			return false;
		}

		//获取到上传的文件信息
		// var data = cutTcBomUploadFile.files[0];

		var data = document.getElementById("cutTcBomUploadFile").files[0];

		var fromData = new FormData();
// var cutTcBomMainUploadFileUrl = path + "bom/cutTcBom/importcutTcBomMainUploadFile";
		//提交导入文件内容
		var cutTcBomMainUploadFileUrl = path + "bom/packaging/importBcBomUploadFile";
		if (data != null) {
			fromData.append("file", data);
			fromData.append("packVersionId",id)
			$.ajax({
				type: "post",
				url: cutTcBomMainUploadFileUrl,
				data: fromData,
				dataType: "json",
				contentType: false,
				processData: false,
				beforeSend: function () {
					//dss.load(true);
				},
				complete: function () {
					//dss.load(false);
				},
				success: function (data) {
					alert("导入成功！");
				}
			});
		}
	}
	//保存datagrid数据
	var doSave = function() {
		endEditing();
	};

	//编辑包材bom明细
	var edit = function() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		var wid = Dialog.open("编辑", 300, 145, editUrl + "?id=" + r.ID, [ EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("bcBomVersionDetailForm", editUrl, function(data) {
				filter();
				reload(false);
				Dialog.close(wid);
			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid)
		}) ]);
	}

	//删除包材bom明细
	var doDelete = function() {
		var r = EasyUI.grid.getSelections("dg");
		if (r.length == 0) {
			Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
			return;
		}

		var ids = [];
		for (var i = 0; i < r.length; i++) {
			if(isEmpty(r[i].ID)){
				$("#dg").datagrid("deleteRow",editIndex);
				addindex = 0;
			}else{
				ids.push(r[i].ID);
			}
		}
		if(ids.length==0){
			return;
		}
		Dialog.confirm(function() {
			JQ.ajax(deleteUrl, "post", {
				ids : ids.toString()
			}, function(data) {
				filter();
			});
		});
	}
	var chooseConsumer = path + "selector/consumer?singleSelect=true";
	//选择客户
	function ChooseConsumer() {
		consumerWindow = Dialog.open("选择客户", 850, 450, chooseConsumer, [ EasyUI.window.button("icon-save", "确认", function() {
			var r = EasyUI.grid.getOnlyOneSelected("_common_consumer_dg");
			$('#bcpackBomConsumerId').searchbox('setValue', r.CONSUMERNAME);
			JQ.setValue('#packBomConsumerId', r.ID);
			Dialog.close(consumerWindow);
		}), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function() {
			Dialog.close(consumerWindow);
		}) ]);
	}

	function getTreeChildrens() {
		var node = $('#BcBomTree').tree('getSelected');
		var children = $('#BcBomTree').tree('getChildren', node.target);
		var s = '';
		for (var i = 0; i < children.length; i++) {
			s += children[i].text + ',';
		}
	}
	//查看审核
	var dialogId;
	//常规产品
	var cgUrl = path + "audit/BC/{id}/state";
	//变更试样
	var syUrl = path + "audit/BC1/{id}/state";
	//新品试样
	var syUrl1 = path + "audit/BC2/{id}/state";
	function view() {
		var node = $('#BcBomTree').tree('getSelected');
		var pNode = $('#BcBomTree').tree('getParent', node.target);
		if (node == null)
			return;
		if(pNode.attributes.ISTESTPRO==0){
			dialogId = Dialog.open("查看审核状态", 700, 200, cgUrl.replace("{id}", node.id), [ EasyUI.window.button("icon-cancel", "关闭", function() {
				Dialog.close(dialogId);
			}) ], function() {
				$("#" + dialogId).dialog("maximize");
			});
		}else if(pNode.attributes.ISTESTPRO==1){
			dialogId = Dialog.open("查看审核状态", 700, 200, syUrl.replace("{id}", node.id), [ EasyUI.window.button("icon-cancel", "关闭", function() {
				Dialog.close(dialogId);
			}) ], function() {
				$("#" + dialogId).dialog("maximize");
			});
		}else{
			dialogId = Dialog.open("查看审核状态", 700, 200, syUrl1.replace("{id}", node.id), [ EasyUI.window.button("icon-cancel", "关闭", function() {
				Dialog.close(dialogId);
			}) ], function() {
				$("#" + dialogId).dialog("maximize");
			});
		}
		

	}
	function setDefult(type, state) {
		var node = $('#BcBomTree').tree('getSelected');
		JQ.ajax(path + "bom/setDefult", "post", {
			'type' : type,
			'defultType' : state,
			'id' : node.id
		}, function(data) {
			Tip.success("设置成功");
			reload(false);
		});
	}
	function setEnableState(type, state) {
		var node = $('#BcBomTree').tree('getSelected');
		JQ.ajax(path + "bom/state", "post", {
			'type' : type,
			'state' : state,
			'id' : node.id
		}, function(data) {
			Tip.success("设置成功");

			reload(false);
		});
	}
	function searchInfo() {
		var t = $("#searchInput").searchbox("getText");
		if(t!=""){
			$('#BcBomTree').tree({
				url : bcBomtreeUre + "?nodeType=root&data=" + t.toString(),
				data : data,
				animate : true,
				onBeforeLoad : function(node, param) {
					if (isFirst) {
						isFirst = false;
						return false;
					} else {
						return true;
					}
				},
				formatter : auditTreeStyler,
				onContextMenu : function(e, node) {
					e.preventDefault();
					$(this).tree('select', node.target);
					if (node.attributes.nodeType == "root") {
						$('#mainMenu').menu('show', {
							left : e.pageX,
							top : e.pageY
						});
					} else if (node.attributes.nodeType == "bom") {

						$('#treeMenu').menu('show', {
							left : e.pageX,
							top : e.pageY
						});
					} else if (node.attributes.nodeType == "version") {
						var item = $('#treeMenuVersion').menu('findItem', '提交审核');
						var item1 = $('#treeMenuVersion').menu('findItem', '修改');
						var item2 = $('#treeMenuVersion').menu('findItem', '删除');
						var item3=$('#treeMenuVersion').menu('findItem', '工艺变更');
						if (node.attributes.AUDITSTATE == 1) {
							$('#treeMenu').menu('hideItem', item.target);
							$('#treeMenu').menu('hideItem', item1.target);
							$('#treeMenu').menu('hideItem', item2.target);
							$('#treeMenu').menu('hideItem', item3.target);
						} else if (node.attributes.AUDITSTATE == 2) {
							$('#treeMenu').menu('hideItem', item.target);
							$('#treeMenu').menu('hideItem', item1.target);
							$('#treeMenu').menu('hideItem', item2.target);
							$('#treeMenu').menu('showItem', item3.target);
						} else {
							$('#treeMenu').menu('showItem', item.target);
							$('#treeMenu').menu('showItem', item1.target);
							$('#treeMenu').menu('showItem', item2.target);
							$('#treeMenu').menu('hideItem', item3.target);
							//item.hidden="true";
						}
						$('#treeMenuVersion').menu('show', {
							left : e.pageX,
							top : e.pageY
						});
					}
				},
				onBeforeExpand : function(node, param) {
					$('#BcBomTree').tree('options').url = bcBomtreeUre + "?nodeType=" + node.attributes.nodeType;
				},
				onClick : function(node) {
					var detail = node.attributes;
					if (node.attributes.nodeType == "version") {
						$("#dg").datagrid({
							url : encodeURI(path + "bom/packaging/list" + "?filter[id]=" + node.attributes.ID)
						});
					} else {
						$("#dg").datagrid('loadData', {
							total : 0,
							rows : []
						});
					}
					if (detail.nodeType == "bom") {
						$("#dg").datagrid('loadData', {
							total : 0,
							rows : []
						});
						$('#PACKBOMGENERICNAME').html(detail.PACKBOMGENERICNAME);

						$('#PACKBOMNAME').html(detail.PACKBOMNAME);

						$('#PACKBOMCODE').html(detail.PACKBOMCODE);

						$('#PACKBOMTYPE').html(detail.PACKBOMTYPE);

						$('#PACKBOMCONSUMERID').html(detail.comsumerName);

						$('#PACKBOMMODEL').html(detail.PACKBOMMODEL);

						$('#PACKBOMWIDTH').html(detail.PACKBOMWIDTH);

						$('#PACKBOMLENGTH').html(detail.PACKBOMLENGTH);

						$('#PACKBOMWEIGHT').html(detail.PACKBOMWEIGHT);

						$('#PACKBOMRADIUS').html(detail.PACKBOMRADIUS);

						$('#PACKBOMROLLSPERBOX').html(detail.PACKBOMROLLSPERBOX);

						$('#PACKBOMBOXESPERTRAY').html(detail.PACKBOMBOXESPERTRAY);

						$('#PACKBOMROLLSPERTRAY').html(detail.PACKBOMROLLSPERTRAY);

					} else if (detail.nodeType == "version") {
						var pNode = $(this).tree('getParent', node.target);
						detail = pNode.attributes;
						$('#PACKBOMGENERICNAME').html(detail.PACKBOMGENERICNAME);

						$('#PACKBOMNAME').html(detail.PACKBOMNAME);

						$('#PACKBOMCODE').html(detail.PACKBOMCODE);

						$('#PACKBOMTYPE').html(detail.PACKBOMTYPE);

						$('#PACKBOMCONSUMERID').html(detail.PACKBOMCONSUMERID);

						$('#PACKBOMMODEL').html(detail.PACKBOMMODEL);

						$('#PACKBOMWIDTH').html(detail.PACKBOMWIDTH);

						$('#PACKBOMLENGTH').html(detail.PACKBOMLENGTH);

						$('#PACKBOMWEIGHT').html(detail.PACKBOMWEIGHT);

						$('#PACKBOMRADIUS').html(detail.PACKBOMRADIUS);

						$('#PACKBOMROLLSPERBOX').html(detail.PACKBOMROLLSPERBOX);

						$('#PACKBOMBOXESPERTRAY').html(detail.PACKBOMBOXESPERTRAY);

						$('#PACKBOMROLLSPERTRAY').html(detail.PACKBOMROLLSPERTRAY);

						/* $("#dg").datagrid(
								{
									url : path + "bom/packaging/list"
											+ "?filter[id]="
											+ node.attributes.ID
								});
						$('#dg').datagrid('reload'); */

					}

					addindex = 0;
					//树列表展开/收缩
					if (node.state == "open") {
						collapse();
					} else {
						expand();
					}
				}
			});
		}
		
		//$('#BcBomTree').tree('expand',$('#BcBomTree').tree('getRoot').target);
	}
	
	function findBcBom(){
		isgetTestPro();
		$('#BcBomTree').tree({
			url : bcBomtreeUre,
			data : data,
			method : 'get',
			animate : true,
			onBeforeLoad : function(node, param) {
				if (isFirst) {
					isFirst = false;
					return false;
				} else {
					return true;
				}
			},
			formatter : auditTreeStyler,
			onContextMenu : function(e, node) {
				e.preventDefault();
				$(this).tree('select', node.target);
				if (node.attributes.nodeType == "root") {
					$('#mainMenu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				} else if (node.attributes.nodeType == "bom") {
					$('#treeMenu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});

				} else if (node.attributes.nodeType == "version") {
					var item = $('#treeMenuVersion').menu('findItem', '提交审核');
					var item1 = $('#treeMenuVersion').menu('findItem', '修改');
					var item2 = $('#treeMenuVersion').menu('findItem', '删除');
					var item3 = $('#treeMenuVersion').menu('findItem', '工艺变更');
					var item3=$('#treeMenuVersion').menu('findItem', '工艺变更');
					if (node.attributes.AUDITSTATE == 1) {
						$('#treeMenu').menu('hideItem', item.target);
						$('#treeMenu').menu('hideItem', item1.target);
						$('#treeMenu').menu('hideItem', item2.target);
						$('#treeMenu').menu('hideItem', item3.target);
					} else if (node.attributes.AUDITSTATE == 2) {
						$('#treeMenu').menu('hideItem', item.target);
						$('#treeMenu').menu('hideItem', item1.target);
						$('#treeMenu').menu('hideItem', item2.target);
						$('#treeMenu').menu('showItem', item3.target);
					} else {
						$('#treeMenu').menu('showItem', item.target);
						$('#treeMenu').menu('showItem', item1.target);
						$('#treeMenu').menu('showItem', item2.target);
						$('#treeMenu').menu('hideItem', item3.target);
						//item.hidden="true";
					}
					$('#treeMenuVersion').menu('show', {
						left : e.pageX,
						top : e.pageY
					});

				}

			},
			onClick : function(node) {
				if (node.state == "open") {
					collapse();
				} else {
					expand();
				}
				//getTreeChildrens();
				//获取节点的attribute属性，进行展示或者在datagrid进行查询
				var detail = node.attributes;
				if (detail.nodeType == "root") {
					$("#dg").datagrid('loadData', {
						total : 0,
						rows : []
					});
				} else if (detail.nodeType == "bom") {
					$("#dg").datagrid('loadData', {
						total : 0,
						rows : []
					});
					$('#PACKBOMGENERICNAME').html(detail.PACKBOMGENERICNAME);

					$('#PACKBOMNAME').html(detail.PACKBOMNAME);

					$('#PACKBOMCODE').html(detail.PACKBOMCODE);

					$('#PACKBOMTYPE').html(detail.PACKBOMTYPE);

					$('#PACKBOMCONSUMERID').html(detail.comsumerName);

					$('#PACKBOMMODEL').html(detail.PACKBOMMODEL);

					$('#PACKBOMWIDTH').html(detail.PACKBOMWIDTH);

					$('#PACKBOMLENGTH').html(detail.PACKBOMLENGTH);

					$('#PACKBOMWEIGHT').html(detail.PACKBOMWEIGHT);

					$('#PACKBOMRADIUS').html(detail.PACKBOMRADIUS);

					$('#PACKBOMROLLSPERBOX').html(detail.PACKBOMROLLSPERBOX);

					$('#PACKBOMBOXESPERTRAY').html(detail.PACKBOMBOXESPERTRAY);

					$('#PACKBOMROLLSPERTRAY').html(detail.PACKBOMROLLSPERTRAY);

				} else if (detail.nodeType == "version") {
					var pNode = $(this).tree('getParent', node.target);
					detail = pNode.attributes;
					$('#PACKBOMGENERICNAME').html(detail.PACKBOMGENERICNAME);

					$('#PACKBOMNAME').html(detail.PACKBOMNAME);

					$('#PACKBOMCODE').html(detail.PACKBOMCODE);

					$('#PACKBOMTYPE').html(detail.PACKBOMTYPE);

					$('#PACKBOMCONSUMERID').html(detail.comsumerName);

					$('#PACKBOMMODEL').html(detail.PACKBOMMODEL);

					$('#PACKBOMWIDTH').html(detail.PACKBOMWIDTH);

					$('#PACKBOMLENGTH').html(detail.PACKBOMLENGTH);

					$('#PACKBOMWEIGHT').html(detail.PACKBOMWEIGHT);

					$('#PACKBOMRADIUS').html(detail.PACKBOMRADIUS);

					$('#PACKBOMROLLSPERBOX').html(detail.PACKBOMROLLSPERBOX);

					$('#PACKBOMBOXESPERTRAY').html(detail.PACKBOMBOXESPERTRAY);

					$('#PACKBOMROLLSPERTRAY').html(detail.PACKBOMROLLSPERTRAY);

					$("#dg").datagrid({
						url : encodeURI(path + "bom/packaging/list" + "?filter[id]=" + node.attributes.ID)
					});
					$('#dg').datagrid('reload');
				}

			},
			onBeforeExpand : function(node, param) {
				$('#BcBomTree').tree('options').url = bcBomtreeUre + "?nodeType=" + node.attributes.nodeType;// change the url                       
				//param.myattr = 'test';    // or change request parameter
			},

		})
	}
	function expandAll() {
		$('#BcBomTree').tree('expandAll');
	}

	function collapseAll() {
		$('#BcBomTree').tree('collapseAll');
	}

	function formatValue(value, row, index) {
		if (value == null) {
			return "";
		} else {
			//return "<div class='easyui-tooltip' title='"+value+"'>"+value+"</div>";
			return '<div class="easyui-panel easyui-tooltip" title="'+value+'" style="width:100px;padding:5px">' + value + '</div>';
		}
	}
</script>