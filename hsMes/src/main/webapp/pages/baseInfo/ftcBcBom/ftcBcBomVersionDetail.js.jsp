<!--
	作者:徐秦冬
	日期:2017-11-30 10:46:24
	页面:非套材包材bom明细JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
(function(){
	$.extend($.fn.validatebox.defaults.rules, {
		onlyOneVersion:{
			validator: function (value,params) {  
				var t = $('#ftcBcBomTree');
				var node = t.tree('getSelected');
				var a=true;
				var init_version=$("#init_version").val();
				if(init_version){//修改
					node = $('#ftcBcBomTree').tree('getParent', node.target);
				}
				if(init_version == value){
					return a;
				}
				$.ajax({
					async:false,  //请求同步
				    url:findV,
				    data:{version:value,id:node.id},
				    success:function(data){
				    	if(data==0){
				    		a=false;
				    	}
				    }
				});
				return a;
	         },
	         message : '已经有相同名字的版本'  
		}
	});
})();
	//添加包材bom明细
	var addUrl = path + "bom/packaging/add";
	//编辑非套材包材bom明细
	var editUrl = path + "bom/packaging/edit";
	var saveUrl = path + "bom/ftcBc/saveDetail";
	//删除非套材包材bom明细
	var deleteUrl = path + "bom/ftcBc/delete";
	
	//非套材包材tree
	var ftcBcBomTreeUre= path + "bom/ftcBc/listBom";
	var isFirst = true;
	var commitAudit = path + "selector/commitAudit";
	var commitUrl = path + "bom/ftcBc/commit";
	//查看bom下有没有版本
	var findV = path + "bom/ftcBc/findV";
	//查询
	function filter() {
		EasyUI.grid.search("dg", "ftcBcBomVersionDetailSearchForm");
	}
	//根据选项框选择的是试样还是正式的bom，改变获取bom的url
	function isgetTestPro(){
		var value=$("#testPro").combobox('getValue');
		if(value==0){
			ftcBcBomTreeUre= path + "bom/ftcBc/listBom";
		}else if(value==1){
			ftcBcBomTreeUre= path + "bom/ftcBc/listBomTest";
		}else{
			ftcBcBomTreeUre= path + "bom/ftcBc/listBomTest1";
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
	var addVersionUrl = path + "bom/ftcBc/addBomVersion";
	var editVersionUrl = path + "bom/ftcBc/editBomVersion";
	var deleteVersionUrl = path + "bom/ftcBc/deleteBomVersion";
	//添加版本信息
	function appenditVersion() {
		var t = $('#ftcBcBomTree');
		var node = t.tree('getSelected');
		var children=$('#ftcBcBomTree').tree('getChildren', node.target);
		for(var i=0;i<children.length;i++){
			if(children[i].attributes.AUDITSTATE <2){
				Tip.warn("已有[未提交]、[不通过]或[审核中]的版本！");
				return;
			}
		}
		var value=$("#testPro").combobox('getValue');
		var wid = Dialog.open("添加", 900, 380, addVersionUrl+"?productType="+value, [ EasyUI.window.button("icon-save", "保存", function() {
			
			JQ.setValue("#bid", node.id);

			EasyUI.form.submit("ftcBcBomVersionForm", addVersionUrl, function(data) {
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

	function edititVersion() {

		var t = $('#ftcBcBomTree');
		var node = t.tree('getSelected');
		var wid = Dialog.open("编辑", 900, 380, editVersionUrl + "?id=" + node.id, [ EasyUI.window.button("icon-save", "保存", function() {

			EasyUI.form.submit("ftcBcBomVersionForm", editVersionUrl, function(data) {

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
		var node = $('#ftcBcBomTree').tree('getSelected');
		Dialog.confirm(function() {
			JQ.ajax(deleteVersionUrl, "post", {
				ids : node.id
			}, function(data) {
				if(data.error!=null&&data.error!=undefined){
					return;
				}
				$('#ftcBcBomTree').tree('remove', node.target);
				$("#dg").datagrid('loadData', {
					total : 0,
					rows : []
				});
				//清空非套材包材基本信息
				$("#rc td[id]").html("");
			});
		});
	}
	
	function reload1() {
		isgetTestPro();
		var node = $("#ftcBcBomTree").tree("getParent", $('#ftcBcBomTree').tree('getSelected').target);
		if (node) {
			$('#ftcBcBomTree').tree('reload', node.target);
		} else {
			$('#ftcBcBomTree').tree('reload');
		}
	}

	var data = [ {
		"id" : 1,
		"text" : "非套材包材BOM",
		"state" : "closed",
		"attributes" : {
			'nodeType' : 'root'
		}
	} ];
	$(function() {
		$('#ftcBcBomTree').tree({
			url : ftcBcBomTreeUre,
			data : data,							//要加载的节点数据
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
				} else if (node.attributes.nodeType == "bom1") {
					$('#tree1Menu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});

				} else if (node.attributes.nodeType == "bom2") {
					$('#tree2Menu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});

				}else if (node.attributes.nodeType == "bom3") {
					$('#tree3Menu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});

				}else if (node.attributes.nodeType == "version") {
					var item = $('#treeMenuVersion').menu('findItem', '提交审核');
					var item1 = $('#treeMenuVersion').menu('findItem', '修改');
					var item2 = $('#treeMenuVersion').menu('findItem', '删除');
					if (node.attributes.AUDITSTATE == 1) {
						$('#tree3Menu').menu('hideItem', item.target);
						$('#tree3Menu').menu('hideItem', item1.target);
						$('#tree3Menu').menu('hideItem', item2.target);
					} else if (node.attributes.AUDITSTATE == 2) {
						$('#tree3Menu').menu('hideItem', item.target);
						$('#tree3Menu').menu('hideItem', item1.target);
						$('#tree3Menu').menu('hideItem', item2.target);
					} else {
						$('#tree3Menu').menu('showItem', item.target);
						$('#tree3Menu').menu('showItem', item1.target);
						$('#tree3Menu').menu('showItem', item2.target);
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
					//清空非套材包材基本信息
					$("#rc td[id]").html("");
				} else if (detail.nodeType == "bom1" || detail.nodeType == "bom2" || detail.nodeType == "bom3") {
					$("#dg").datagrid('loadData', {
						total : 0,
						rows : []
					});
					//清空非套材包材基本信息
					$("#rc td[id]").html("");
				} else if (detail.nodeType == "version") {
					$('#packCode').html(detail.packCode);

					$('#consumerName').html(detail.consumerName);

					$('#rollDiameter').html(detail.rollDiameter);

					$('#rollsPerPallet').html(detail.rollsPerPallet);

					$('#palletLength').html(detail.palletLength);

					$('#palletWidth').html(detail.palletWidth);

					$('#palletHeight').html(detail.palletHeight);

					$('#bcTotalWeight').html(detail.bcTotalWeight);

					$('#requirement_suliaomo').html(detail.requirement_suliaomo);

					$('#requirement_baifang').html(detail.requirement_baifang);

					$('#requirement_dabaodai').html(detail.requirement_dabaodai);

					$('#requirement_biaoqian').html(detail.requirement_biaoqian);

					$('#requirement_xiaobiaoqian').html(detail.requirement_xiaobiaoqian);
					$('#requirement_juanbiaoqian').html(detail.requirement_juanbiaoqian);
					$('#requirement_tuobiaoqian').html(detail.requirement_tuobiaoqian);
					$('#requirement_chanrao').html(detail.requirement_chanrao);
					$('#requirement_other').html(detail.requirement_other);
					
					$("#dg").datagrid({
						url :  encodeURI(path + "bom/ftcBc/list" + "?filter[id]=" + node.id)
					});
					$('#dg').datagrid('reload');

				}

			},
			onBeforeExpand : function(node, param) {
				$('#ftcBcBomTree').tree('options').url = ftcBcBomTreeUre + "?nodeType=" + node.attributes.nodeType;// change the url                       
				//param.myattr = 'test';    // or change request parameter
			},

		});
		$('#ftcBcBomTree').tree('expand', $('#ftcBcBomTree').tree('getRoot').target);	//展开一个节点
	});
	
	var dialogWidth = 700, dialogHeight = 350;
	function commit() {
		var r = $('#ftcBcBomTree').tree('getSelected');
		var pNode = $('#ftcBcBomTree').tree('getParent', r.target);
		var children=$('#ftcBcBomTree').tree('getChildren', pNode.target);
		for(var i=0;i<children.length;i++){
			if(children[i].attributes.AUDITSTATE == 1){
				Tip.warn("已有[审核中]的版本！");
				return;
			}
		}
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
			var node = $('#ftcBcBomTree').tree('getSelected');
			var pNode = $('#ftcBcBomTree').tree('getParent', node.target);
			if(node.attributes.productType=="0"){
			  $("#editAuditProduce #name").textbox("setValue", "常规非套材包材BOM审核，编号：" + pNode.text + " " + "版本：" + node.text);
			}else if(node.attributes.productType=="1"){
			  $("#editAuditProduce #name").textbox("setValue", "变更试样非套材包材BOM审核，编号：" + pNode.text + " " + "版本：" + node.text);	
			}else{
			  $("#editAuditProduce #name").textbox("setValue", "新品试样非套材包材BOM审核，编号：" + pNode.text + " " + "版本：" + node.text);	

			}	
			Dialog.more(wid);
		});
	}
	function reload(isSelf) {
		isgetTestPro();
		var node = $('#ftcBcBomTree').tree('getSelected');

		if (isSelf) {
			$('#ftcBcBomTree').tree('reload', node.target);
		} else {
			var pNode = $('#ftcBcBomTree').tree('getParent', node.target);
			$('#ftcBcBomTree').tree('reload', pNode.target);
		}
	}

	//树列表收缩
	function collapse() {
		var node = $('#ftcBcBomTree').tree('getSelected');
		$('#ftcBcBomTree').tree('collapse', node.target);
	}

	//树列表展开
	function expand() {
		var node = $('#ftcBcBomTree').tree('getSelected');
		$('#ftcBcBomTree').tree('expand', node.target);
	}
	
	var cc=null;//导入Excel错误提示框
	
	//添加 “产品规格”和“包装标准代码” 或 “厂内名称”和“二级包装标准代码” 或 “产品包装名称”和“三级包装标准代码”
	function appendit(level){
		var t = $('#ftcBcBomTree');
		var node = t.tree('getSelected');
		var root = t.tree('getRoot');
		var pid="&pid="+node.id;
		if(node == root){
			pid="";
		}
		var wid = Dialog.open("添加", 900, 380, path + "/bom/ftcBc/addBom?level="+level+pid, [ EasyUI.window.button("icon-save", "保存", function() {
			if(cc !=null){
				try {cc.window('close');} catch (e) {}
				cc=null;
			}
			EasyUI.form.submit("ftcBcBomForm", path + "/bom/ftcBc/addBom", function(data) {
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

	//修改 “产品规格”和“包装标准代码” 或 “厂内名称”和“二级包装标准代码” 或 “产品包装名称”和“三级包装标准代码”
	function editit() {
		var t = $('#ftcBcBomTree');
		var node = t.tree('getSelected');

		var wid = Dialog.open("编辑", 900, 380, path + "/bom/ftcBc/editBom" + "?id=" + node.id, [ EasyUI.window.button("icon-save", "保存", function() {
			if(cc !=null){
				try {cc.window('close');} catch (e) {}
				cc=null;
			}
			EasyUI.form.submit("ftcBcBomForm", path + "/bom/ftcBc/editBom", function(data) {
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
					Tip.success("更新成功");
					if(data.msg){
						var c='<div style="max-height:400px;">'+data.msg+'</div>';
						cc=$.messager.show({
							title:'导入Excel提示',
							msg:c,
							timeout:0,
							showType:'fade'
						});
					}
				}
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
// 			$("#" + wid).dialog("maximize");
			Dialog.more(wid);
		});
	}
	function removeit() {
		var node = $('#ftcBcBomTree').tree('getSelected');
		Dialog.confirm(function() {
			JQ.ajax(path + "/bom/ftcBc/deleteBom", "post", {
				ids : node.id
			}, function(data) {
				if(data.error!=null&&data.error!=undefined){
					return;
				}
				$('#ftcBcBomTree').tree('remove', node.target);
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
		var t = $('#ftcBcBomTree');
		var node = t.tree('getSelected');
		var rowstr = {
			"id" : row.ID,
			"packMaterialCode"	: row.PACKMATERIALCODE,
			"packStandardCode"	: row.PACKSTANDARDCODE,
			"packMaterialName"	: row.PACKMATERIALNAME,
			"packMaterialModel"	: row.PACKMATERIALMODEL,
			"packUnit"			: row.PACKUNIT,
			"packMaterialCount"	: row.PACKMATERIALCOUNT,
			"packMemo"			: row.PACKMEMO,
			"packVersionId"		: node.id

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

	//添加非套材包材bom明细
	//datagrid行添加事件

	function add() {
		var node = $('#ftcBcBomTree').tree('getSelected');
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
		consumerWindow = Dialog.open("选择客户", 850, 550, chooseConsumer, [ EasyUI.window.button("icon-save", "确认", function() {
			var r = EasyUI.grid.getOnlyOneSelected("_common_consumer_dg");
			$('#bcBomConsumerId').searchbox('setValue', r.CONSUMERNAME);
			JQ.setValue('#consumerId', r.ID);
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
	var cgUrl = path + "audit/FTCBC/{id}/state";
	//变更试样
	var syUrl = path + "audit/FTCBC1/{id}/state";
	//新品试样
	var syUrl1 = path + "audit/FTCBC2/{id}/state";
	function view() {
		var node = $('#ftcBcBomTree').tree('getSelected');
		var pNode = $('#ftcBcBomTree').tree('getParent', node.target);
		console.log(cgUrl);
		console.log(pNode);
		if (node == null)
			return;
		if(node.attributes.productType=="0"){
			dialogId = Dialog.open("查看审核状态", 700, 200, cgUrl.replace("{id}", node.id), [ EasyUI.window.button("icon-cancel", "关闭", function() {
				Dialog.close(dialogId);
			}) ], function() {
				$("#" + dialogId).dialog("maximize");
			});
		}else if(node.attributes.productType=="1"){
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
			$('#ftcBcBomTree').tree({
				url : ftcBcBomTreeUre + "?nodeType=root&data=" + t.toString(),
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
					}else if (node.attributes.nodeType == "bom1") {
						$('#tree1Menu').menu('show', {
							left : e.pageX,
							top : e.pageY
						});

					} else if (node.attributes.nodeType == "bom2") {
						$('#tree2Menu').menu('show', {
							left : e.pageX,
							top : e.pageY
						});

					}else if (node.attributes.nodeType == "bom3") {
						$('#tree3Menu').menu('show', {
							left : e.pageX,
							top : e.pageY
						});

					} else if (node.attributes.nodeType == "version") {
						var item = $('#treeMenuVersion').menu('findItem', '提交审核');
						var item1 = $('#treeMenuVersion').menu('findItem', '修改');
						var item2 = $('#treeMenuVersion').menu('findItem', '删除');
						if (node.attributes.AUDITSTATE == 1) {
							$('#tree3Menu').menu('hideItem', item.target);
							$('#tree3Menu').menu('hideItem', item1.target);
							$('#tree3Menu').menu('hideItem', item2.target);
						} else if (node.attributes.AUDITSTATE == 2) {
							$('#tree3Menu').menu('hideItem', item.target);
							$('#tree3Menu').menu('hideItem', item1.target);
							$('#tree3Menu').menu('hideItem', item2.target);
						} else {
							$('#tree3Menu').menu('showItem', item.target);
							$('#tree3Menu').menu('showItem', item1.target);
							$('#tree3Menu').menu('showItem', item2.target);
							//item.hidden="true";
						}
						$('#treeMenuVersion').menu('show', {
							left : e.pageX,
							top : e.pageY
						});
					}
				},
				onBeforeExpand : function(node, param) {
					$('#ftcBcBomTree').tree('options').url = ftcBcBomTreeUre + "?nodeType=" + node.attributes.nodeType;
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
						//清空非套材包材基本信息
						$("#rc td[id]").html("");
					} else if (detail.nodeType == "bom1" || detail.nodeType == "bom2" || detail.nodeType == "bom3") {
						$("#dg").datagrid('loadData', {
							total : 0,
							rows : []
						});
						//清空非套材包材基本信息
						$("#rc td[id]").html("");
					} else if (detail.nodeType == "version") {
						$('#packCode').html(detail.packCode);

						$('#consumerName').html(detail.consumerName);

						$('#rollDiameter').html(detail.rollDiameter);

						$('#rollsPerPallet').html(detail.rollsPerPallet);

						$('#palletLength').html(detail.palletLength);

						$('#palletWidth').html(detail.palletWidth);

						$('#palletHeight').html(detail.palletHeight);

						$('#bcTotalWeight').html(detail.bcTotalWeight);

						$('#requirement_suliaomo').html(detail.requirement_suliaomo);

						$('#requirement_baifang').html(detail.requirement_baifang);

						$('#requirement_dabaodai').html(detail.requirement_dabaodai);

						$('#requirement_biaoqian').html(detail.requirement_biaoqian);

						$('#requirement_xiaobiaoqian').html(detail.requirement_xiaobiaoqian);
						$('#requirement_juanbiaoqian').html(detail.requirement_juanbiaoqian);
						$('#requirement_tuobiaoqian').html(detail.requirement_tuobiaoqian);
						$('#requirement_chanrao').html(detail.requirement_chanrao);
						$('#requirement_other').html(detail.requirement_other);
						
						$("#dg").datagrid({
							url : encodeURI (path + "bom/ftcBc/list" + "?filter[id]=" + node.id)
						});
						$('#dg').datagrid('reload');

					}

				}
			});
		}
		
		$('#ftcBcBomTree').tree('expand', $('#ftcBcBomTree').tree('getRoot').target);	//展开一个节点
	}
	
	function findFtcBcBom(){
		isgetTestPro();
		console.log(ftcBcBomTreeUre);
		$('#ftcBcBomTree').tree({
			url : ftcBcBomTreeUre,
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
				}else if (node.attributes.nodeType == "bom1") {
					$('#tree1Menu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});

				} else if (node.attributes.nodeType == "bom2") {
					$('#tree2Menu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});

				}else if (node.attributes.nodeType == "bom3") {
					$('#tree3Menu').menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				} else if (node.attributes.nodeType == "version") {
					var item = $('#treeMenuVersion').menu('findItem', '提交审核');
					var item1 = $('#treeMenuVersion').menu('findItem', '修改');
					var item2 = $('#treeMenuVersion').menu('findItem', '删除');
					if (node.attributes.AUDITSTATE == 1) {
						$('#tree3Menu').menu('hideItem', item.target);
						$('#tree3Menu').menu('hideItem', item1.target);
						$('#tree3Menu').menu('hideItem', item2.target);
					} else if (node.attributes.AUDITSTATE == 2) {
						$('#tree3Menu').menu('hideItem', item.target);
						$('#tree3Menu').menu('hideItem', item1.target);
						$('#tree3Menu').menu('hideItem', item2.target);
					} else {
						$('#tree3Menu').menu('showItem', item.target);
						$('#tree3Menu').menu('showItem', item1.target);
						$('#tree3Menu').menu('showItem', item2.target);
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
					//清空非套材包材基本信息
					$("#rc td[id]").html("");
				} else if (detail.nodeType == "bom1" || detail.nodeType == "bom2" || detail.nodeType == "bom3") {
					$("#dg").datagrid('loadData', {
						total : 0,
						rows : []
					});
					//清空非套材包材基本信息
					$("#rc td[id]").html("");
				} else if (detail.nodeType == "version") {
					$('#packCode').html(detail.packCode);

					$('#consumerName').html(detail.consumerName);

					$('#rollDiameter').html(detail.rollDiameter);

					$('#rollsPerPallet').html(detail.rollsPerPallet);

					$('#palletLength').html(detail.palletLength);

					$('#palletWidth').html(detail.palletWidth);

					$('#palletHeight').html(detail.palletHeight);

					$('#bcTotalWeight').html(detail.bcTotalWeight);

					$('#requirement_suliaomo').html(detail.requirement_suliaomo);

					$('#requirement_baifang').html(detail.requirement_baifang);

					$('#requirement_dabaodai').html(detail.requirement_dabaodai);

					$('#requirement_biaoqian').html(detail.requirement_biaoqian);

					$('#requirement_xiaobiaoqian').html(detail.requirement_xiaobiaoqian);
					$('#requirement_juanbiaoqian').html(detail.requirement_juanbiaoqian);
					$('#requirement_tuobiaoqian').html(detail.requirement_tuobiaoqian);
					$('#requirement_chanrao').html(detail.requirement_chanrao);
					$('#requirement_other').html(detail.requirement_other);
					
					$("#dg").datagrid({
						url : encodeURI(path + "bom/ftcBc/list" + "?filter[id]=" + node.id)
					});
					$('#dg').datagrid('reload');
				}

			},
			onBeforeExpand : function(node, param) {
				$('#ftcBcBomTree').tree('options').url = ftcBcBomTreeUre + "?nodeType=" + node.attributes.nodeType;// change the url                       
				//param.myattr = 'test';    // or change request parameter
			},

		});
// 		$('#ftcBcBomTree').tree('expand', $('#ftcBcBomTree').tree('getRoot').target);	//展开一个节点
	}
	function expandAll() {
		$('#ftcBcBomTree').tree('expandAll');
	}

	function collapseAll() {
		$('#ftcBcBomTree').tree('collapseAll');
	}

	function formatValue(value, row, index) {
		if (value == null) {
			return "";
		} else {
			//return "<div class='easyui-tooltip' title='"+value+"'>"+value+"</div>";
			return '<div class="easyui-panel easyui-tooltip" title="'+value+'" style="width:100px;padding:5px">' + value + '</div>';
		}
	}
	
	function cancelBom() {
		var node = $('#ftcBcBomTree').tree('getSelected');
		Dialog.confirm(function() {
			JQ.ajax(path + "/bom/ftcBc/cancelBom", "post", {
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
</script>