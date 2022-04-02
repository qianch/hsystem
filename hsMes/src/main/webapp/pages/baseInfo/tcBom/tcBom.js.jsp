<!--
	作者:肖文彬
	日期:2016-10-9 9:19:51
	页面:套材BOMJS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	var e = null;
	var addFtcBom = path + "bom/tc/addFtcBom";
	var findparts = path + "bom/tc/find";
	//添加套材BOM
	var addUrl = path + "bom/tc/add";
	//编辑套材BOM
	var editUrl = path + "bom/tc/edit";
	//删除套材BOM
	var deleteUrl = path + "bom/tc/delete";
	//查询bom信息
	var findBom = path + "bom/tc/findBom";
	//添加BOM版本
	var addV = path + "bom/tcBomVersion/add";
	//编辑BOM版本
	var editV = path + "bom/tcBomVersion/edit";
	//删除套材BOM版本
	var deleteV = path + "bom/tc/deleteV";
	//保存部件
	var savePartsUrl = path + "bom/tc/saveParts";
	//校验部件是否可删除
	var delValid= path + "bom/tc/delValid";
	//删除部件
	var deleteP = path + "bom/tc/deleteDetail";
	//查询部件信息
	var partsList = path + "bom/tcBomVersionParts/list";
	//查询部件明细
	var partsD = path + "bom/tcBomVersionPartsDetail/list";
	//删除部件明细
	var deleteD = path + "bom/tcBomVersionPartsDetail/delete";
	//保存部件明细
	var saveDetailUrl = path + "bom/tc/saveDetail";
	//查询部件成品重量胚布信息
	var partsFW = path + "bom/tcBomVersionPartsFinishedWeightEmbryoCloth/list";
	//删除部件成品重量胚布信息
	var deleteFW = path + "bom/tcBomVersionPartsFinishedWeightEmbryoCloth/delete";
	//保存部件成品重量胚布信息
	var saveFWUrl = path + "bom/tc/saveFinishedWeightEmbryoCloth";
	//复制树
	var copyBomVersionUrl = path + "bom/tc/copytcBomVersion";
	var copyBomUrl = path + "bom/tc/copytcBom";
	//审核页面
	var commitAudit = path + "bom/tcBomVersion/commitAudit";
	//添加成品信息
	var _common_product_addUrl = path + "finishProduct/add";
	//选择客户信息
	var chooseConsumer = path + "selector/consumer";
	//根据版本查询审核信息
	var findCode = path + "bom/tc/findB";
	//打开提交审核页面
	var _auditCommitUrl = path + "selector/commitAudit";
	//查询此部件是否有明细
	var findDetial = path + "bom/tc/findDetial";
	//查看部件的版本的审核状态
	var findA = path + "bom/tc/findA";
	//查看bom下有没有版本
	var findV = path + "bom/tc/findV";
	var consumerWindow = null;
	var isFirst = true;
	var nodeID = null;
	var newNode=null;
	var editIndex = undefined;
	var editIndexD = undefined;
	var editIndexFW = undefined;
	var addindex = 0;
	var addindexI = 0;
	var addindexF = 0;
	var indexA = 0;


	//查询
	function filter() {
		EasyUI.grid.search("parts", "partsFilter");
	}

	function filterD() {
		EasyUI.grid.search("partsDetails", "partsDFilter");
	}

	function filterFW() {
		EasyUI.grid.search("partsFinishedWeightEmbryoCloth", "partsFWFilter");
	}

	var data = [ {
		"text" : "套材BOM",
		"state" : "closed",
		"attributes" : {
			"status" : "0",
			"vId" : "0"
		}
	} ];

	//根据选项框选择的是试样还是正式的bom，改变获取bom的url
	function isgetTestPro(){
		var value=$("#testPro").combobox('getValue');
		var state=$("#state").combobox('getValue');
		if(value==0){
			findBom = path + "bom/tc/findBom?state="+state;
		}else if(value==1){
			findBom = path + "bom/tc/listBomTest?state="+state;
		}else{
			findBom = path + "bom/tc/listBomTest1?state="+state;
		}
	}

	function auditStateFormatter(auditState) {
		if (auditState == 1) {
			return '<span style="color:#FF8C00;font-weight:bold;">[审核中]</span>';
		} else if (auditState == 2) {
			return '<span style="color:green;font-weight:bold;">[已通过]</span>';
		} else if (auditState == 0) {
			return '<span style="color:#5a080f;font-weight:bold;">[未提交]</span>';
		} else if (auditState == -1) {
			return '<span style="color:#FF0000;font-weight:bold;">[不通过]</span>';
		}
	}

	function auditTreeStyler1(node) {
		if (node.attributes.status == "2"){
			return node.text + auditStateFormatter(node.attributes.AUDITSTATE);
		}else if(node.attributes.status=="3"){
			if(node.attributes.TCPROCBOMVERSIONPARTSTYPE=="成品胚布"){
				return "<font style='color:red;font-weight:bold;'>"+node.text+"</font>";
			}else{
				return node.text;
			}

		} else{
			return node.text;
		}
	}

	$(function() {
		$('#TcBomTree').tree({
			url : findBom,
			data : data,
			method : 'get',
			animate : true,
			formatter : auditTreeStyler1,
			onContextMenu : function(e, node) {
				e.preventDefault();
				nodeID = node.id;
				newNode=node;
				$(this).tree('select', node.target);
				if (node.attributes.status == "0") {
					$('#treeMenuBom').menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				} else if (node.attributes.status == "1") {

					$('#treeMenuV').menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				} else if (node.attributes.status == "2") {
					var item = $('#treeMenu').menu('findItem', '提交审核');
					var item1 = $('#treeMenu').menu('findItem', '修改');
					var item2 = $('#treeMenu').menu('findItem', '删除');
					var item3 = $('#treeMenu').menu('findItem', '工艺变更');
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
					$('#treeMenu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				}

			},
			onBeforeLoad : function(node, param) {
				if (isFirst) {
					isFirst = false;
					return false;
				} else {
					return true;
				}
			},
			onBeforeExpand : function(node, param) {
				if(findBom.indexOf('?')>0){
									$('#TcBomTree').tree('options').url = findBom + "&status=" + node.attributes.status + "&vId=" + node.attributes.vId;
								}else{
									$('#TcBomTree').tree('options').url = findBom + "?status=" + node.attributes.status + "&vId=" + node.attributes.vId;
								}			},
			onClick : function(node) {
				addindex = 0;
				addindexI = 0;
				if (node.attributes.status == "2") {

					$("#parts").datagrid({
						url : encodeURI(partsList + "?filter[id]=" + node.attributes.ID)
					});
					$('#partsDetails').datagrid('loadData', {
						total : 0,
						rows : []
					});
					$('#partsFinishedWeightEmbryoCloth').datagrid('loadData', {
						total : 0,
						rows : []
					});

				}
				if (node.attributes.status == "3") {
					var node1 = $('#TcBomTree').tree('find', node.attributes.TCPROCBOMVERSIONPARENTPARTS);
					$("#parts").datagrid({
						url : encodeURI( partsList + "?filter[ids]=" + node.attributes.ID)
					});
					$("#partsDetails").datagrid({
						url : encodeURI( partsD + "?filter[ids]=" + node.attributes.ID)
					});
					$("#partsFinishedWeightEmbryoCloth").datagrid({
						url : encodeURI(partsFW + "?filter[ids]=" + node.attributes.ID)
					});

				}
				//树列表展开/收缩
				/* if (node.state == "open") {
					collapse();
				} else {
					expand();
				} */
			}
		});
		$('#TcBomTree').tree('expand', $('#TcBomTree').tree('getRoot').target);
	});
	//-------------------------------------------------------------------------------
	function findTcBom(){
		isgetTestPro();
		$('#TcBomTree').tree({
			url : findBom,
			data : data,
			method : 'get',
			animate : true,
			formatter : auditTreeStyler1,
			onContextMenu : function(e, node) {
				e.preventDefault();
				nodeID = node.id;
				newNode=node;
				$(this).tree('select', node.target);
				if (node.attributes.status == "0") {
					$('#treeMenuBom').menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				} else if (node.attributes.status == "1") {

					$('#treeMenuV').menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				} else if (node.attributes.status == "2") {
					var item = $('#treeMenu').menu('findItem', '提交审核');
					var item1 = $('#treeMenu').menu('findItem', '修改');
					/* var item2 = $('#treeMenu').menu('findItem', '删除'); */
					var item3 = $('#treeMenu').menu('findItem', '工艺变更');
					if (node.attributes.AUDITSTATE == 1) {
						$('#treeMenu').menu('hideItem', item.target);
						$('#treeMenu').menu('hideItem', item1.target);
						/* $('#treeMenu').menu('hideItem', item2.target); */
						$('#treeMenu').menu('hideItem', item3.target);
					} else if (node.attributes.AUDITSTATE == 2) {
						$('#treeMenu').menu('hideItem', item.target);
						$('#treeMenu').menu('hideItem', item1.target);
						/* $('#treeMenu').menu('hideItem', item2.target); */
						$('#treeMenu').menu('showItem', item3.target);
					} else {
						$('#treeMenu').menu('showItem', item.target);
						$('#treeMenu').menu('showItem', item1.target);
						/* $('#treeMenu').menu('showItem', item2.target); */
						$('#treeMenu').menu('hideItem', item3.target);
						//item.hidden="true";
					}
					$('#treeMenu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				}

			},
			onBeforeLoad : function(node, param) {
				if (isFirst) {
					isFirst = false;
					return false;
				} else {
					return true;
				}
			},
			onBeforeExpand : function(node, param) {
				if(findBom.indexOf('?')>0){
					$('#TcBomTree').tree('options').url = findBom + "&status=" + node.attributes.status + "&vId=" + node.attributes.vId;
				}else{
					$('#TcBomTree').tree('options').url = findBom + "?status=" + node.attributes.status + "&vId=" + node.attributes.vId;
				}


			},
			onClick : function(node) {
				addindex = 0;
				addindexI = 0;
				if (node.attributes.status == "2") {

					$("#parts").datagrid({
						url : encodeURI(partsList + "?filter[id]=" + node.attributes.ID)
					});
					$('#partsDetails').datagrid('loadData', {
						total : 0,
						rows : []
					});
					$('#partsFinishedWeightEmbryoCloth').datagrid('loadData', {
						total : 0,
						rows : []
					});

				}
				if (node.attributes.status == "3") {
					var node1 = $('#TcBomTree').tree('find', node.attributes.TCPROCBOMVERSIONPARENTPARTS);
					$("#parts").datagrid({
						url : encodeURI(partsList + "?filter[ids]=" + node.attributes.ID)
					});
					$("#partsDetails").datagrid({
						url : encodeURI(partsD + "?filter[ids]=" + node.attributes.ID)
					});
					$("#partsFinishedWeightEmbryoCloth").datagrid({
						url : encodeURI(partsFW + "?filter[ids]=" + node.attributes.ID)
					});

				}
				//树列表展开/收缩
				/* if (node.state == "open") {
					collapse();
				} else {
					expand();
				} */
			}
		});
	}
	//-----------------------------------------------------------------------------------------------
	function searchInfo() {
		var t = $("#searchInput").searchbox("getText");
		isgetTestPro();
		if(findBom.indexOf('?')>0){
			findBom=findBom + "&status=0&data=" + t.toString()+"&isNeedTop=1";
		}else{
			findBom=findBom + "?status=0&data=" + t.toString();
		}
		if(t!=""){
			$('#TcBomTree').tree({
				url : findBom,
				data : data,
				method : 'get',
				animate : true,
				formatter : auditTreeStyler1,
				onContextMenu : function(e, node) {
					e.preventDefault();
					$(this).tree('select', node.target);
					if (node.attributes.status == "0") {
						$('#treeMenuBom').menu('show', {
							left : e.pageX,
							top : e.pageY
						});
					} else if (node.attributes.status == "1") {
						$('#treeMenuV').menu('show', {
							left : e.pageX,
							top : e.pageY
						});
					} else if (node.attributes.status == "2") {
						var item = $('#treeMenu').menu('findItem', '提交审核');
						var item1 = $('#treeMenu').menu('findItem', '修改');
						//var item2 = $('#treeMenu').menu('findItem', '删除');
						var item3 = $('#treeMenu').menu('findItem', '工艺变更');
						if (node.attributes.AUDITSTATE == 1) {
							$('#treeMenu').menu('hideItem', item.target);
							$('#treeMenu').menu('hideItem', item1.target);
							//$('#treeMenu').menu('hideItem', item2.target);
							$('#treeMenu').menu('hideItem', item3.target);
						} else if (node.attributes.AUDITSTATE == 2) {
							$('#treeMenu').menu('hideItem', item.target);
							$('#treeMenu').menu('hideItem', item1.target);
							//$('#treeMenu').menu('hideItem', item2.target);
							$('#treeMenu').menu('showItem', item3.target);
						} else {
							$('#treeMenu').menu('showItem', item.target);
							$('#treeMenu').menu('showItem', item1.target);
							//$('#treeMenu').menu('showItem', item2.target);
							$('#treeMenu').menu('hideItem', item3.target);
							//item.hidden="true";
						}
						$('#treeMenu').menu('show', {
							left : e.pageX,
							top : e.pageY
						});
					}

				},
				onBeforeLoad : function(node, param) {
					if (isFirst) {
						isFirst = false;
						return false;
					} else {
						return true;
					}
				},
				onBeforeExpand : function(node, param) {
					if(findBom.indexOf('?')>0){
										$('#TcBomTree').tree('options').url = findBom + "&status=" + node.attributes.status + "&vId=" + node.attributes.vId;
									}else{
										$('#TcBomTree').tree('options').url = findBom + "?status=" + node.attributes.status + "&vId=" + node.attributes.vId;
									}
				},
				onClick : function(node) {
					addindex = 0;
					addindexI = 0;
					if (node.attributes.status == "2") {
						$("#parts").datagrid({
							url : encodeURI(partsList + "?filter[id]=" + node.attributes.ID)
						});
						$('#partsDetails').datagrid('loadData', {
							total : 0,
							rows : []
						});
						$('#partsFinishedWeightEmbryoCloth').datagrid('loadData', {
							total : 0,
							rows : []
						});
					}
					if (node.attributes.status == "3") {
						var node1 = $('#TcBomTree').tree('find', node.attributes.TCPROCBOMVERSIONPARENTPARTS);
						$("#parts").datagrid({
							url : encodeURI(partsList + "?filter[ids]=" + node.attributes.ID)
						});
						$("#partsDetails").datagrid({
							url : encodeURI(partsD + "?filter[ids]=" + node.attributes.ID)
						});
						$("#partsFinishedWeightEmbryoCloth").datagrid({
							url : encodeURI(partsFW + "?filter[ids]=" + node.attributes.ID)
						});

					}
					//树列表展开/收缩
					/* if (node.state == "open") {
						collapse();
					} else {
						expand();
					} */
				}
			});
		}

	}

	/**
	 *工艺变更
	 */
	function forceEdit(){
		var node=$("#TcBomTree").tree('getSelected');

		Dialog.confirm(function(){
			Loading.show();

			JQ.ajaxPost(path+"bom/modify",{id:node.id,type:"TC"},function(){
				Loading.hide();
				Tip.success("工艺变更中!");
				reload1();
			},function(){
				Loading.hide();
			});

		},"确认变更?变更期间，销售将无法下达相关产品的订单！");

	}

	//树列表收缩
	function collapse() {
		var node = $('#TcBomTree').tree('getSelected');
		$('#TcBomTree').tree('collapse', node.target);
	}

	//树列表展开
	function expand() {
		var node = $('#TcBomTree').tree('getSelected');
		$('#TcBomTree').tree('expand', node.target);
	}

	function expandAll() {
		$('#TcBomTree').tree('expandAll');
	}

	function collapseAll() {
		$('#TcBomTree').tree('collapseAll');
	}
	function reload() {
		isgetTestPro();
		var node = $('#TcBomTree').tree('getSelected');
		if (node) {
			$('#TcBomTree').tree('reload', node.target);
		} else {
			$('#TcBomTree').tree('reload');
		}
	}
	function reload1() {
		isgetTestPro();
		var node = $("#TcBomTree").tree("getParent", $('#TcBomTree').tree('getSelected').target);
		if (node) {
			$('#TcBomTree').tree('reload', node.target);
		} else {
			$('#TcBomTree').tree('reload');
		}
	}

	//BOM版本复制
	function copyVersion() {
		var node = $('#TcBomTree').tree('getSelected');
		JQ.ajax(copyBomVersionUrl, "post", {
			ids : node.id,
			name : node.attributes.TCPROCBOMVERSIONCODE + "(复制)"
		}, function(data) {
			reload1();
		});
	}
	function copyBom() {
		var node = $('#TcBomTree').tree('getSelected');
		JQ.ajax(copyBomUrl, "post", {
			id : node.id
		}, function(data) {
			reload1();
		});
	}

	//右击删除工艺BOM
	function removeit() {
		var node = $('#TcBomTree').tree('getSelected');
		Dialog.confirm(function() {
			JQ.ajax(deleteUrl, "post", {
				ids : node.id
			}, function(data) {
				reload1();
			});
		}, '确认删除该套材BOM？');
	}

	var cc=null;//导入Excel错误提示框
	//右击添加工艺bom
	function appendit() {
		var wid = Dialog.open("添加套材工艺BOM", 550, 300, addUrl, [ EasyUI.window.button("icon-save", "保存", function() {
			if(cc !=null){
				try {
					cc.window('close');
					cc=null;
				} catch (e) {
					cc=null;
				}
			}
			EasyUI.form.submit("tc_BomForm", addUrl, function(data) {
				if(data.excelErrorMsg){
					var c='<div style="max-height:400px;">'+data.excelErrorMsg+'</div>';
					cc=$.messager.show({
						title:'导入Excel错误',
						msg:c,
						timeout:0,
						showType:'fade'
					});
					return;
				}else if(!data.error){
					Tip.success("添加成功");
				}
				if (Tip.hasError(data)) {
					Loading.hide();
				}
				reload();
				if (Dialog.isMore(wid)) {
					Dialog.close(wid);
					add();
				} else {
					Dialog.close(wid);
				}
			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ], function() {
			Dialog.more(wid);
		});

	}

	//右击编辑工艺bom
	function editVV() {
		var node = $("#TcBomTree").tree('getSelected');
		var wid = Dialog.open("编辑套材工艺BOM", 600, 200, editUrl + "?id=" + node.id, [ EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("tc_BomForm", editUrl, function(data) {
				if (Tip.hasError(data)) {
					Loading.hide();
					return;
				}
				reload1();
				searchInfo();
				if (Dialog.isMore(wid)) {
					Dialog.close(wid);
					add();
				} else {
					Dialog.close(wid);
				}
			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ], function() {
			Dialog.more(wid);
		});
	}

	//右击添加版本
	function appendV() {
		var t = $('#TcBomTree');
		var node = t.tree('getSelected');
		JQ.ajax(findV, "post", {
			id : node.id
		}, function(data) {
		if(data==1){
			Tip.warn("此bom下存在版本无法添加");
			return;
		}
		var wid = Dialog.open("添加套材工艺版本",600, 100, addV + "?id=" + node.id, [ EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("tcBomVersionForm", addV, function(data) {
				if (Tip.hasError(data)) {
					Loading.hide();
				}
				reload();
				if (Dialog.isMore(wid)) {
					Dialog.close(wid);
					add();
				} else {
					Dialog.close(wid);
				}
			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ], function() {
			Dialog.more(wid);
		});
		});
	}

	//右击编辑版本
	function editB() {
		var t = $('#TcBomTree');
		var node = t.tree('getSelected');
		var wid = Dialog.open("编辑套材工艺版本", 600, 200, editV + "?id=" + node.id, [ EasyUI.window.button("icon-save", "保存", function() {
			if(cc !=null){
				try {
					cc.window('close');
					cc=null;
				} catch (e) {
					cc=null;
				}
			}
			EasyUI.form.submit("tcBomVersionForm", editV, function(data) {
				if(data.excelErrorMsg){
					var c='<div style="max-height:400px;">'+data.excelErrorMsg+'</div>';
					cc=$.messager.show({
						title:'导入Excel错误',
						msg:c,
						timeout:0,
						showType:'fade'
					});
					return;
				}else if(!data.error){
					Tip.success("修改成功");
				}
				if (Tip.hasError(data)) {
					Loading.hide();
					return;
				}
				Loading.hide();
				reload1();
				if (Dialog.isMore(wid)) {
					Dialog.close(wid);
					add();
				} else {
					Dialog.close(wid);
				}
			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ], function() {
			Dialog.more(wid);
		});
	}

	//右击删除工艺BOM版本
	function removeB() {
		var t = $('#TcBomTree');
		var node = t.tree('getSelected');
		Dialog.confirm(function() {
			JQ.ajax(deleteV, "post", {
				ids : node.id
			}, function(data) {
				reload1();
			});
		}, '确认删除该条BOM版本信息记录，以下所有的部件信息也会删除？');
	}
	//常规
	var viewUrl = path + "audit/TC/{id}/state";
	//变更试样
	var viewUrl1 = path + "audit/TC1/{id}/state";
	//新品试样
	var viewUrl2 = path + "audit/TC2/{id}/state";
	function view() {
		var node = $('#TcBomTree').tree('getSelected');
		if (node == null) {
			return;
		}

		JQ.ajaxPost(findCode, {
			id : node.id
		}, function(data) {
			if(data.ISTESTPRO==0){
				dialogId = Dialog.open("查看审核状态", 400, 400, viewUrl.replace("{id}", node.id), [ EasyUI.window.button("icon-cancel", "关闭", function() {
					Dialog.close(dialogId)
				}) ], function() {
					$("#" + dialogId).dialog("maximize");
				});
			}else if(data.ISTESTPRO==1){
				dialogId = Dialog.open("查看审核状态", 400, 400, viewUrl1.replace("{id}", node.id), [ EasyUI.window.button("icon-cancel", "关闭", function() {
					Dialog.close(dialogId)
				}) ], function() {
					$("#" + dialogId).dialog("maximize");
				});
			}else{
				dialogId = Dialog.open("查看审核状态", 400, 400, viewUrl2.replace("{id}", node.id), [ EasyUI.window.button("icon-cancel", "关闭", function() {
					Dialog.close(dialogId)
				}) ], function() {
					$("#" + dialogId).dialog("maximize");
				});
			}
		})




	}

	//选择客户
	function ChooseConsumer() {
		consumerWindow = Dialog.open("选择客户", 850, 450, chooseConsumer, [ EasyUI.window.button("icon-save", "确认", function() {
			var r = EasyUI.grid.getOnlyOneSelected("_common_consumer_dg");
			$('#ftcProcBomConsumer').searchbox('setValue', r.CONSUMERNAME);
			JQ.setValue('#ftcProcBomConsumerId', r.ID);
			Dialog.close(consumerWindow);
		}), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function() {
			Dialog.close(consumerWindow);
		}) ]);
	}

	//选择客户双击事件
	function _common_consumer_dbClickRow(index, row) {
		$('#ftcProcBomConsumer').searchbox('setValue', row.CONSUMERNAME);
		JQ.setValue('#ftcProcBomConsumerId', row.ID);
		Dialog.close(consumerWindow);
	}

	//结束部件datagrid行编辑
	function endEditing() {
		if (editIndex == undefined) {
			return true
		}
		if ($('#parts').datagrid('validateRow', editIndex)) {
			$('#parts').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}

	//结束部件编辑
	function onEndEdit(index, row, changes) {

		var rs = $("#parts").datagrid("getRows");
		var names = {};
		for (var i = 0; i < rs.length; i++) {
			var name = rs[i].TCPROCBOMVERSIONPARTSNAME;
			if (!Assert.isEmpty(names[name])) {
				Tip.warn("存在重复的部件名称，数据将不会保存");
				return;
			} else {
				names[name] = rs[i].TCPROCBOMVERSIONPARTSCOUNT;
			}
		}

		addindexI = 0;
		var node = $('#TcBomTree').tree('getSelected');
		if (node.attributes.status == "2") {
			var rowstr = {
				"id" : row.ID,
				"tcProcBomVersoinId" : node.id,
				"tcProcBomVersionMaterialNumber" : row.TCPROCBOMVERSIONMATERIALNUMBER,
				"tcProcBomVersionPartsName" : row.TCPROCBOMVERSIONPARTSNAME,
                "customerMaterialCode" : row.CUSTOMERMATERIALCODE,
				"tcProcBomVersionPartsType" : row.TCPROCBOMVERSIONPARTSTYPE,
				"tcProcBomVersionPartsCutCode" : row.TCPROCBOMVERSIONPARTSCUTCODE,
				"tcProcBomVersionPartsCount" : row.TCPROCBOMVERSIONPARTSCOUNT,
				"tcProcBomVersionPartsSubsCount" : row.TCPROCBOMVERSIONPARTSSUBSCOUNT,
				"tcProcBomVersionPartsWeight" : row.TCPROCBOMVERSIONPARTSWEIGHT,
				"needSort":row.NEEDSORT
			};
			$.ajax({
				url : savePartsUrl,
				type : 'post',
				dataType : 'json',
				contentType : 'application/json',
				data : JSON.stringify(rowstr),
				success : function(data) {
					//$('#parts').datagrid('reload');
					$('#parts').datagrid('reload');
					reload();
					filter();
				}
			});

		}
		if (node.attributes.status == "3") {
			var rowstr = {
				"id" : row.ID,
				"tcProcBomVersoinId" : node.attributes.vId,
				"tcProcBomVersionParentParts" : node.id,
				"tcProcBomVersionPartsName" : row.TCPROCBOMVERSIONPARTSNAME,
				"tcProcBomVersionPartsCutCode" : row.TCPROCBOMVERSIONPARTSCUTCODE,
				"tcProcBomVersionPartsCount" : row.TCPROCBOMVERSIONPARTSCOUNT,
				"tcProcBomVersionPartsSubsCount" : row.TCPROCBOMVERSIONPARTSSUBSCOUNT,
				"tcProcBomVersionPartsWeight" : row.TCPROCBOMVERSIONPARTSWEIGHT
			};
			$.ajax({
				url : savePartsUrl,
				type : 'post',
				dataType : 'json',
				contentType : 'application/json',
				data : JSON.stringify(rowstr),
				success : function(data) {
					reload();
					filter();
				}
			});
		}

	}

	//部件datagrid单击事件
	function onDblClickCell(index, field) {
		addindex = 0;
		if (editIndex != index) {
			if (endEditing()) {
				$('#parts').datagrid('selectRow', index).datagrid('beginEdit', index);
				var ed = $('#parts').datagrid('getEditor', {
					index : index,
					field : field
				});
				if (ed) {
					($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
				}
				editIndex = index;
			} else {
				setTimeout(function() {
					$('#parts').datagrid('selectRow', editIndex);
				}, 0);
			}
		}
	}

	function onClickCell(index, field) {
		addindex = 0;
		endEditing();
	}

	//部件datagrid行添加事件
	var add1 = function() {
		var node = $('#TcBomTree').tree('getSelected');
		if (node == null) {
			Tip.warn("请选中部件后在点击增加");
			return;
		}
		if (node.attributes.status != "2") {
			Tip.warn("只能在版本上增加部件");
			return;
		}

		JQ.ajaxPost(findDetial, {
			id : node.id,
			status : node.attributes.status
		}, function(data) {
			if (data == "0") {
				Tip.warn("此部件已有明细，无法增加部件");
				return;
			}
			if (data == "1") {
				Tip.warn("此版本已经入审核阶段，无法增加部件");
				return;
			}
			if (addindexI == 0) {
				$("#parts").datagrid('insertRow', {
					index : 0,
					row : {"TCPROCBOMVERSIONPARTSCOUNT":1}
				});
				$("#parts").datagrid('beginEdit', 0);
				editIndex = 0;
				addindexI = 1;
			}
		})
	}

	//部件保存datagrid数据
	var edit1 = function() {
		endEditing();

		var rs = $("#parts").datagrid("getRows");
		var names = {};
		for (var i = 0; i < rs.length; i++) {
			var name = rs[i].TCPROCBOMVERSIONPARTSNAME;
			if (!Assert.isEmpty(names[name])) {
				Tip.warn("存在重复的部件名称" + name + "，数据将不会保存");
				return;
			} else {
				names[name] = rs[i].TCPROCBOMVERSIONPARTSCOUNT;
			}
		}
		$("#parts").datagrid('endEdit', editIndex);
		addindexI = 0;
	};

	//删除部件datagrid数据
	var doDelete1 = function() {

		var r = EasyUI.grid.getSelections("parts");
		if (r.length == 0) {
			Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
			return;
		}
		var ids = [];
		for (var i = 0; i < r.length; i++) {
			ids.push(r[i].ID);
		}
		Dialog.confirm(function() {

			JQ.ajax(delValid,"post",{
				ids:ids.toString()
			},function(data){
				if(data.msg){
					Dialog.confirm(function() {
						delPart(ids);
					}, "部件使用中，订单号:<br>"+data.msg.join(",<br>")+",是否删除？<label style='font-weight:bold;color:red;'>删除后，无法产出登记，无法打包，等一系列操作!</label>");
				}else{
					delPart(ids);
				}
			});

		}, '确认删除该条部件信息记录，以下所有的子部件信息也会删除？');
	}

	function delPart(ids){
		JQ.ajax(deleteP, "post", {
			ids : ids.toString()
		}, function(data) {
			filter();
			reload();
			addindexI = 0;
		});
	}

	//结束部件明细datagrid行编辑
	function endEditingD() {
		if (editIndexD == undefined) {
			return true
		}
		if ($('#partsDetails').datagrid('validateRow', editIndexD)) {
			$('#partsDetails').datagrid('endEdit', editIndexD);
			editIndexD = undefined;
			return true;
		} else {
			return false;
		}
	}

	//结束部件明细编辑
	function onEndEditD(index, row) {
		Loading.show("正在保存");
		addindex = 0;
		var node = $('#TcBomTree').tree('getSelected');
		if (node.attributes.status == "3") {
			var rowstr = {
				"id" : row.ID,
				"tcProcBomPartsId" : node.id,
				"tcFinishedProductId" : row.TCFINISHEDPRODUCTID,
				"tcProcBomFabricCount" : row.TCPROCBOMFABRICCOUNT,
				"length" : row.LENGTH,
				"drawingNo" : row.DRAWINGNO,
				"levelNo":row.LEVELNO,
				"rollNo":row.ROLLNO,
				"sorting":row.SORTING,
				"tcTheoreticalWeigh":row.TCTHEORETICALWEIGH
			};
			$.ajax({
				url : saveDetailUrl,
				type : 'post',
				dataType : 'json',
				contentType : 'application/json',
				data : JSON.stringify(rowstr),
				success : function(data) {
					reload();
					filterD();
					Loading.hide();
				}
			});
		}
	}

	//部件明细datagrid单击事件
	function onDblClickCellD(index, field) {
		index1 = index;
		addindex = 0;
		if (editIndexD!=undefined&&editIndexD != index) {
			if (endEditingD()) {
				$('#partsDetails').datagrid('beginEdit', index);
				var ed = $('#partsDetails').datagrid('getEditor', {
					index : index,
					field : field
				});
				if (ed) {
					($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
				}
				editIndexD = index;
			} else {
				setTimeout(function() {
					$('#partsDetails').datagrid('selectRow', editIndexD);
				}, 0);
			}
		}else{
			editIndexD=index;
			$('#partsDetails').datagrid('beginEdit', index);
		}
	}

	function onClickCellD(index, field) {
		addindex = 0;
		endEditingD();
	}

	//部件明细datagrid行添加事件
	var add = function() {
		var node = $('#TcBomTree').tree('getSelected');
		if (node == null) {
			Tip.warn("请选中部件后在点击增加");
			return;
		}
		if (node.attributes.status != "3") {
			Tip.warn("只能在部件上增加部件明细");
			return;
		}
		var children = $("#TcBomTree").tree("getChildren", node.target);
		if (children.length > 0) {
			Tip.warn("请在子部件下添加明细");
			return;
		}
		JQ.ajaxPost(findA, {
			id : node.id
		}, function(data) {
			if (data == "0") {
				Tip.warn("此版本已进入审核阶段，无法添加明细");
				return;
			}
			consumerWindow1 = Dialog.open("选择胚布", 1100, 500, addFtcBom, [ EasyUI.window.button("icon-save", "确认", function() {
				var r = EasyUI.grid.getOnlyOneSelected("dg");
				if (addindex == 0) {
					var r = {
						PID:r.ID,
						MATERIELCODE:r.MATERIELCODE,
						TCPROCBOMFABRICMODEL : r.TCPROCBOMFABRICMODEL,
						PRODUCTPROCESSCODE : r.PRODUCTPROCESSCODE,
						PRODUCTWIDTH : r.PRODUCTWIDTH,
						PRODUCTROLLLENGTH : r.PRODUCTROLLLENGTH,
						PRODUCTMODEL : r.PRODUCTMODEL,
						TCFINISHEDPRODUCTID : r.ID

					};
					/* if (contains("partsDetails", r)) {
						Tip.warn("胚布规格已存在");
						return;
					} */
					$('#partsDetails').datagrid('appendRow', r);
					$("#partsDetails").datagrid('getSelected');
					$("#partsDetails").datagrid('beginEdit', $("#partsDetails").datagrid('getRows').length - 1);
					editIndexD = $("#partsDetails").datagrid('getRows').length - 1;
					addindex = 1;
				}

				Dialog.close(consumerWindow1);
			}), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function() {
				Dialog.close(consumerWindow1);
			}) ]);
		})

	}

	function contains(gridId, rowData) {
		var data = $("#" + gridId).datagrid("getData");
		if (data.total == 0) {
			return false;
		} else {
			for (var i = 0; i < data.rows.length; i++) {
				if (data.rows[i]["TCFINISHEDPRODUCTID"] == rowData["TCFINISHEDPRODUCTID"]) {
					return true
				}
			}
			return false;
		}
	}

	//部件明细保存datagrid数据
	var edit = function() {
		$("#partsDetails").datagrid('endEdit', editIndexD);
		addindex = 0;
	};

	//删除部件明细datagrid数据
	var doDelete = function() {
		var r = EasyUI.grid.getSelections("partsDetails");
		if (r.length == 0) {
			Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
			return;
		}
		var ids = [];
		for (var i = 0; i < r.length; i++) {
			ids.push(r[i].ID);
		}
		Dialog.confirm(function() {
			JQ.ajax(deleteD, "post", {
				ids : ids.toString()
			}, function(data) {
				filterD();
				addindex = 0;
			});
		});
	}

	function audit() {
		var node = $('#TcBomTree').tree('getSelected');
		var wid = Dialog.open("审核", 700, 120, _auditCommitUrl + "?id=" + node.id, [ EasyUI.window.button("icon-ok", "提交审核", function() {
			EasyUI.form.submit("editAuditProduce", commitAudit, function(data) {

				Dialog.close(wid);
				reload1();
			});
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ], function() {
			var node = $('#TcBomTree').tree('getSelected');
			JQ.ajaxPost(findCode, {
				id : node.id
			}, function(data) {
				if(data.ISTESTPRO==0){
				    $("#editAuditProduce #name").textbox("setValue", "常规产品套材BOM审核，编号：" +data.TCPROCBOMNAME+"/"+ data.TCPROCBOMCODE + " " + "版本：" + node.text);
				}else if(data.ISTESTPRO==1){
					$("#editAuditProduce #name").textbox("setValue", "变更试样套材BOM审核，编号：" +data.TCPROCBOMNAME+"/"+ data.TCPROCBOMCODE + " " + "版本：" + node.text);
				}else{
					$("#editAuditProduce #name").textbox("setValue", "新品试样套材BOM审核，编号：" +data.TCPROCBOMNAME+"/"+ data.TCPROCBOMCODE + " " + "版本：" + node.text);
				}
			})

		});
	}

	function _common_product_dbClickRow1(index, row) {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if (addindex == 0) {
			var _r = {
				TCPROCBOMFABRICMODEL : r.TCPROCBOMFABRICMODEL,
				PRODUCTPROCESSCODE : r.PRODUCTPROCESSCODE,
				PRODUCTWIDTH : r.PRODUCTWIDTH,
				LENGTH : r.PRODUCTROLLLENGTH,
				PRODUCTMODEL : r.PRODUCTMODEL,
				TCFINISHEDPRODUCTID : r.ID
			};
			if (contains("partsDetails", _r)) {
				Tip.warn("胚布规格已存在");
				return;
			}
			$('#partsDetails').datagrid('appendRow', _r);
			$("#partsDetails").datagrid('getSelected');
			$("#partsDetails").datagrid('beginEdit', $("#partsDetails").datagrid('getRows').length - 1);
			editIndexD = $("#partsDetails").datagrid('getRows').length - 1;
			addindex = 1;
		}
		Dialog.close(consumerWindow1);
	}

	function setDefult(type, state) {
		var node = $('#TcBomTree').tree('getSelected');
		JQ.ajax(path + "bom/setDefult", "post", {
			'type' : type,
			'defultType' : state,
			'id' : node.id
		}, function(data) {
			Tip.success("设置成功");
			reload1();
		});
	}
	function setEnableState(type, state) {
		var node = $('#TcBomTree').tree('getSelected');
		JQ.ajax(path + "bom/state", "post", {
			'type' : type,
			'state' : state,
			'id' : node.id
		}, function(data) {
			Tip.success("设置成功");

			reload1();
		});
	}

	function tcPartsRowStyler(index,row){

		if(row.TCPROCBOMVERSIONPARTSTYPE=="成品胚布"){
			return "color:red;font-weight:bold;";
		}

	}



	//部件成品重量胚布信息datagrid行添加事件
	var addFW = function() {
		var node = $('#TcBomTree').tree('getSelected');
		if (node == null) {
			Tip.warn("请选中部件后在点击增加");
			return;
		}
		if (node.attributes.status != "3") {
			Tip.warn("只能在部件上增加部件成品重量胚布信息");
			return;
		}
		var children = $("#TcBomTree").tree("getChildren", node.target);
		if (children.length > 0) {
			Tip.warn("请在子部件下添加成品重量胚布信息");
			return;
		}
		JQ.ajaxPost(findA, {
			id : node.id
		}, function(data) {
			if (data == "0") {
				Tip.warn("此版本已进入审核阶段，无法添加成品重量胚布信息");
				return;
			}
			if(addindexF == 0){
				$("#partsFinishedWeightEmbryoCloth").datagrid('insertRow', {
				index : 0,
				row : {}
				});
				$("#partsFinishedWeightEmbryoCloth").datagrid('beginEdit', 0);
				editIndexFW = 0;
				addindexF = 1;
			}
		});

	}

	//部件成品重量胚布信息保存datagrid数据
	var editFW = function() {
		console.log("running editFW"+editIndexFW);
		$("#partsFinishedWeightEmbryoCloth").datagrid('endEdit', editIndexFW);
		addindexF = 0;
	};

	//删除部件成品重量胚布信息datagrid数据
	var doDeleteFW = function() {
		var r = EasyUI.grid.getSelections("partsFinishedWeightEmbryoCloth");
		if (r.length == 0) {
			Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
			return;
		}
		var ids = [];
		for (var i = 0; i < r.length; i++) {
			ids.push(r[i].ID);
		}
		Dialog.confirm(function() {
			JQ.ajax(deleteFW, "post", {
				ids : ids.toString()
			}, function(data) {
				filterFW();
				addindexF = 0;
			});
		});
	}

	//结束部件成品重量胚布信息datagrid行编辑
	function endEditingFW() {
		if (editIndexFW == undefined) {
			return true;
		}
		if ($('#partsFinishedWeightEmbryoCloth').datagrid('validateRow', editIndexFW)) {
			$('#partsFinishedWeightEmbryoCloth').datagrid('endEdit', editIndexFW);
			editIndexFW = undefined;
			return true;
		} else {
			return false;
		}
	}

	//结束部件成品重量胚布信息编辑
	function onEndEditFW(index, row) {
		Loading.show("正在保存");
		addindex = 0;
		var node = $('#TcBomTree').tree('getSelected');
		if (node.attributes.status == "3") {
			var rowstr = {
				"id" : row.ID,
				"tcProcBomPartsId" : node.id,
				"materialNumber" : row.MATERIALNUMBER,
				"embryoClothName" : row.EMBRYOCLOTHNAME,
				"weight" : row.WEIGHT
			};
			$.ajax({
				url : saveFWUrl,
				type : 'post',
				dataType : 'json',
				contentType : 'application/json',
				data : JSON.stringify(rowstr),
				success : function(data) {
					reload();
					filterFW();
					Loading.hide();
				}
			});
		}
	}

	//部件成品重量胚布信息datagrid单击事件
	function onDblClickCellFW(index, field) {
		console.log(index, field);
		index1 = index;
		addindex = 0;
		if (editIndexFW!=undefined&&editIndexFW != index) {
			if (endEditingFW()) {
				$('#partsFinishedWeightEmbryoCloth').datagrid('beginEdit', index);
				var ed = $('#partsFinishedWeightEmbryoCloth').datagrid('getEditor', {
					index : index,
					field : field
				});
				if (ed) {
					($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
				}
				editIndexFW = index;
			} else {
				setTimeout(function() {
					$('#partsFinishedWeightEmbryoCloth').datagrid('selectRow', editIndexFW);
				}, 0);
			}
		}else{
			editIndexFW=index;
			$('#partsFinishedWeightEmbryoCloth').datagrid('beginEdit', index);
		}
	}
</script>
