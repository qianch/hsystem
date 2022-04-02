<!--
	作者:高飞
	日期:2017-07-18 14:19:51
	页面:西门子BOM文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	$.extend($.fn.textbox.defaults, {
		"icons" : []
	});
	$.extend($.fn.combobox.defaults, {
		"icons" : []
	});
	$.extend($.fn.combotree.defaults, {
		"icons" : []
	});
	$.extend($.fn.numberbox.defaults, {
		"icons" : []
	});

	$.extend($.fn.searchbox.defaults, {
		"icons" : []
	});

	var bomList = path + "siemens/bom/list";
	var drawingsList = path + "siemens/bom/drawings/list";
	var importUrl = path + "siemens/bom/drawingsImport";
	var fragmentMap = {};

	$(function() {
		searcher();
	});

	//套材BOM搜索
	function searcher(value, name) {
		$(".tooltip").remove();
		$("#TcBomTree").tree("reload");
	}

	//BOM树加tooltip
	function treeFormatter(node) {
		return "<span title='"+node.text+"' class='easyui-tooltip'>"
				+ node.text + "</span>";
	}

	var comboboxLoaded = false;
	function onComboboxLoadSuccess() {
		if (comboboxLoaded)
			return;
		comboboxLoaded = true;
		fragmentMap = {};
		var data = $(this).combobox("getData");
		for (var i = 0; i < data.length; i++) {
			fragmentMap[data[i].fragmentCode] = data[i]
		}
	}

	function onTreeBeforeLoad(node, param) {
		var siemens = $("#isSiemens:checked").is(':checked');
		param.siemens = siemens;
		try {
			var code = $("#searchInput").searchbox("getValue");
			param.code = code;
		} catch (e) {
		}
		if (node != null) {
			param.tcBomId = node.id;
		}
	}

	//树加载成功事件
	function onTreeLoadSuccess(node, data) {
		$.parser.parse($(this));
	}

	//树点击事件
	function onTreeClick(node) {
		if (!isPartNode(node))
			return;
		//var bomNode=$("#TcBomTree").tree("getParent",node.target);
		loadDrawings(node.id);
		bomState(node.id, -1, true);
	}

	var saveDrawingsUrl = path + "siemens/bom/drawings/add";

	//加载图纸BOM
	function loadDrawings(partId) {
		$("#drawingsDg").datagrid("uncheckAll");
		var rows = $("#drawingsDg").datagrid("getRows");

		for (var i = 0; i < rows.length; i++) {
			$("#drawingsDg").datagrid("endEdit", i);
		}

		var inserted = $("#drawingsDg").datagrid('getChanges', "inserted");
		var deleted = $("#drawingsDg").datagrid('getChanges', "deleted");
		var updated = $("#drawingsDg").datagrid('getChanges', "updated");

		if (inserted.length != 0 || deleted.length != 0 || updated.length != 0) {
			$.messager.confirm("信息提示", "是否保存当前更改？", function(data) {
				if (data) {
					if (saveDrawings(partId)) {
						comboboxLoaded = false;
					}
				} else {
					if (!partId)
						return;
					Loading.show("正在加载");
					comboboxLoaded = false;
					JQ.ajaxGet(drawingsList + "?partId=" + partId, function(
							data) {
						Loading.hide();
						$("#drawingsDg").datagrid("loadData", data);
					});
				}
			});
		} else {
			if (!partId)
				return;
			Loading.show("正在加载");
			JQ.ajaxGet(drawingsList + "?partId=" + partId, function(data) {
				Loading.hide();
				comboboxLoaded = false;
				$("#drawingsDg").datagrid({
					data : data
				});
			});
		}
	}
	//图纸BOM列表加载完毕
	function onDrawingsDgLoadSuccess(data) {
		//$("#drawingsDg").datagrid("enableDnd");
	}

	function isPartNode(node) {
		if (node == null)
			return false;
		if ($("#TcBomTree").tree("getParent", node.target) == null)
			return false;
		if ($("#TcBomTree").tree("getParent",
				$("#TcBomTree").tree("getParent", node.target).target) == null)
			return false;
		return true;
	}

	function onDrawingsComboboxBeforeLoad(param) {
		var index = $(this).parent().parent().parent().parent().parent()
				.parent().parent().attr("datagrid-row-index");
		var row = EasyUI.grid.getRowByIndex('drawingsDg', index);
		param.tcBomId = row.tcBomId;
	}

	function addDrawings() {
		var node = $("#TcBomTree").tree("getSelected");
		if (!isPartNode(node)) {
			Tip.warn("请选择部件节点");
			return;
		}
		var bomNode = $("#TcBomTree").tree("getParent", node.target);
		$("#drawingsDg").datagrid("appendRow", {
			tcBomId : bomNode.id,
			isDeleted : 0,
			partId : node.id,
			partName : node.text,
			id : (new Date().getTime())
		});
	}

	function onClickDrawingsDgRow(index, row) {
		$(this).datagrid("beginEdit", index);
	}

	function saveDrawings(partId) {
		Loading.show("正在校验");
		var rows = $("#drawingsDg").datagrid("getRows");
		var NO = {};
		for (var i = 0; i < rows.length; i++) {
			//$("#drawingsDg").datagrid("beginEdit",i);
			if (!$("#drawingsDg").datagrid("validateRow", i)) {
				Tip.warn("请修正红色输入框的内容");
				Loading.hide();
				return;
			}
			$("#drawingsDg").datagrid("endEdit", i);
			if (NO[rows[i].printSort] != null) {
				Tip.warn("第" + (i + 1) + "行 和 第" + NO[rows[i].printSort]
						+ "行 顺序号 一样，请修改");
				Loading.hide();
				return;
			}
			NO[rows[i].printSort] = i + 1;
		}

		if (!partId && rows.length > 0)
			partId = rows[0].partId;

		var inserted = $("#drawingsDg").datagrid('getChanges', "inserted");
		var deleted = $("#drawingsDg").datagrid('getChanges', "deleted");
		var updated = $("#drawingsDg").datagrid('getChanges', "updated");
		if (inserted.length == 0 && deleted.length == 0 && updated == 0) {
			Tip.warn("未作任何修改");
			Loading.hide();
			return false;
		}
		var drawingsGrid = {
			inserted : inserted,
			deleted : deleted,
			updated : updated
		};
		Loading.hide();
		Loading.show("正在保存");
		$.ajax({
			url : saveDrawingsUrl,
			type : "post",
			data : JSON.stringify(drawingsGrid),
			contentType : "application/json",
			success : function(data) {
				Loading.hide();
				Tip.success("保存成功");
				$("#drawingsDg").datagrid("acceptChanges");
				$("#drawingsDg").datagrid("uncheckAll");
				if (partId)
					loadDrawings(partId);
				return true;
			}
		});
	}

	function deleteDrawings() {
		EasyUI.grid.deleteSelected("drawingsDg");
	}

	function fragmentFormatter(v, r, i) {
		return r.fragmentName;
	}

	function drawingsValidCode() {
		var _options = $(this).combobox('options');
		var _data = $(this).combobox('getData');/* 下拉框所有选项 */
		var _value = $(this).combobox('getValue');/* 用户输入的值 */
		var _text = $(this).combobox('getText');
		var _b = false;/* 标识是否在下拉列表中找到了用户输入的字符 */
		for (var i = 0; i < _data.length; i++) {
			if (_data[i][_options.valueField] == _value
					&& _data[i][_options.textField] == _text) {
				_b = true;
				onDrawingsComboSelect({
					id : _value,
					fragmentCode : _text
				}, this);
				break;
			}
		}
		if (!_b) {
			$(this).combobox('setValue', '');
			var index = $(this).parent().parent().parent().parent().parent()
					.parent().parent().attr("datagrid-row-index");
			var row = EasyUI.grid.getRowByIndex('drawingsDg', index);

			row.fragmentId = undefined;
			row.fragmentCode = undefined;

			var ed = $("#drawingsDg").datagrid('getEditor', {
				index : index,
				field : "fragmentName"
			});
			$(ed.target).textbox("setValue", "");

			var ed = $("#drawingsDg").datagrid('getEditor', {
				index : index,
				field : "fragmentWeight"
			});
			$(ed.target).textbox("setValue", "");

			var ed = $("#drawingsDg").datagrid('getEditor', {
				index : index,
				field : "fragmentLength"
			});
			$(ed.target).textbox("setValue", "");

			var ed = $("#drawingsDg").datagrid('getEditor', {
				index : index,
				field : "fragmentWidth"
			});
			$(ed.target).textbox("setValue", "");

			/* var ed = $("#drawingsDg").datagrid('getEditor', {index:index,field:"fragmentCountPerDrawings"});
			$(ed.target).textbox("setValue",""); */

			var ed = $("#drawingsDg").datagrid('getEditor', {
				index : index,
				field : "fragmentMemo"
			});
			$(ed.target).textbox("setValue", "");

			var ed = $("#drawingsDg").datagrid('getEditor', {
				index : index,
				field : "farbicModel"
			});
			$(ed.target).textbox("setValue", "");

			return;
		}
	}

	function onDrawingsComboSelect(record, _this) {
		var index = $(_this ? _this : this).parent().parent().parent().parent()
				.parent().parent().parent().attr("datagrid-row-index");
		var row = EasyUI.grid.getRowByIndex('drawingsDg', index);

		var fragment = fragmentMap[record.fragmentCode];

		row.fragmentId = fragment.id;
		row.fragmentCode = fragment.fragmentCode;

		var ed = $("#drawingsDg").datagrid('getEditor', {
			index : index,
			field : "fragmentName"
		});
		$(ed.target).textbox("setValue", fragment.fragmentName);

		ed = $("#drawingsDg").datagrid('getEditor', {
			index : index,
			field : "fragmentWeight"
		});
		$(ed.target).textbox("setValue", fragment.fragmentWeight + "");

		ed = $("#drawingsDg").datagrid('getEditor', {
			index : index,
			field : "fragmentLength"
		});
		$(ed.target).textbox("setValue", fragment.fragmentLength);

		ed = $("#drawingsDg").datagrid('getEditor', {
			index : index,
			field : "fragmentWidth"
		});
		$(ed.target).textbox("setValue", fragment.fragmentWidth);

		/* var ed = $("#drawingsDg").datagrid('getEditor', {index:index,field:"fragmentCountPerDrawings"});
		$(ed.target).textbox("setValue",fragment.fragmentCountPerDrawings); */

		var ed = $("#drawingsDg").datagrid('getEditor', {
			index : index,
			field : "fragmentMemo"
		});
		$(ed.target).textbox("setValue", fragment.fragmentMemo);

		var ed = $("#drawingsDg").datagrid('getEditor', {
			index : index,
			field : "farbicModel"
		});
		$(ed.target).textbox("setValue", fragment.farbicModel);
	}

	function beginEdit() {
		var rows = $("#drawingsDg").datagrid("getRows");
		for (var i = 0; i < rows.length; i++) {
			$("#drawingsDg").datagrid("beginEdit", 0);
			break;
		}
	}

	function comboFilter(q, row) {
		return row.fragmentName.toUpperCase().indexOf(q.toUpperCase()) != -1
				|| row.fragmentCode.toUpperCase().indexOf(q.toUpperCase()) != -1;
	}

	function cancelDrawingsEdit() {
		$("#drawingsDg").datagrid("uncheckAll");
		$("#drawingsDg").datagrid("rejectChanges");
	}

	function fragmentFormatter(row) {
		return row.fragmentCode + " / " + row.fragmentName;
	}

	function disableBom() {
		var node = $("#TcBomTree").tree("getSelected");
		if (!isPartNode(node)) {
			Tip.warn("请选择部件节点");
			return;
		}
		bomState(node.id, 0, true);
	}

	function enableBom() {
		var node = $("#TcBomTree").tree("getSelected");
		if (!isPartNode(node)) {
			Tip.warn("请选择部件节点");
			return;
		}

		$("#drawingsDg").datagrid("uncheckAll");
		$("#drawingsDg").datagrid("rejectChanges");

		setTimeout(function() {
			var rows = $("#drawingsDg").datagrid("getRows");
			var drawingNoCount = {};

			for (var i = 0; i < rows.length; i++) {
				if (drawingNoCount[rows[i].fragmentDrawingNo] == undefined) {
					drawingNoCount[rows[i].fragmentDrawingNo] = 1;
				} else {
					drawingNoCount[rows[i].fragmentDrawingNo] += 1;
				}
			}

			var _rows = [];
			var total = 0;
			for ( var NO in drawingNoCount) {
				total++;
				_rows.push({
					"NO" : NO,
					"COUNT" : drawingNoCount[NO]
				});
			}

			var footer = [ {
				"NO" : "总计",
				"COUNT" : rows.length
			} ];

			var data = {
				"total" : total,
				"rows" : _rows,
				"footer" : footer
			};

			console.log(data);

			$("#dlg").dialog("open");

			$("#summaryDg").datagrid("loadData", data);
		}, 0);
	}

	function doEnable() {
		var node = $("#TcBomTree").tree("getSelected");
		if (!isPartNode(node)) {
			Tip.warn("请选择部件节点");
			return;
		}
		bomState(node.id, 1, true);
	}

	function enableBomButton() {
		$("#add").linkbutton("enable");
		$("#del").linkbutton("enable");
		$("#edit").linkbutton("enable");
		$("#cancelEdit").linkbutton("enable");
		$("#save").linkbutton("enable");
		$("#enable").linkbutton("enable");
		$("#disable").linkbutton("disable");
	}

	function disableBomButton() {
		$("#add").linkbutton("disable");
		$("#del").linkbutton("disable");
		$("#edit").linkbutton("disable");
		$("#cancelEdit").linkbutton("disable");
		$("#save").linkbutton("disable");
		$("#enable").linkbutton("disable");
		$("#disable").linkbutton("enable");
	}

	function bomState(partId, state, isDrawingsBom) {
		JQ.ajaxPost(path + "siemens/bom/enable", {
			partId : partId,
			enable : state == -1 ? "" : state,
			isDrawingsBom : isDrawingsBom
		}, function(data) {
			if (data.state == 0) {
				enableBomButton();
			} else {
				disableBomButton();
			}
			$("#dlg").dialog("close");
		});
	}

	function onContextMenu(e, node) {
		$(".tooltip").remove();
		e.preventDefault();
		$("#TcBomTree").tree("expand", node.target);
	}

	/**
	 * 西门子裁剪图纸BOM导入
	 */

	var importDrawings = function() {
		var node = $("#TcBomTree").tree("getSelected");//当前节点
		if ((!isPartNode(node)) || (node == null)) {
			Tip.warn("请选择部件节点");
			return;
		}
		if (!$("#disable").linkbutton('options').disabled) {//判断禁用按钮是否被禁用（图纸BOM必须处于禁用状态，才能导入）
			Tip.warn("图纸BOM必须处于禁用状态,才能导入数据!");
			return;
		}
		var partId=node.id;//部件ID
		var parentNode = $("#TcBomTree").tree('getParent', node.target);//获得当前节点的父节点;  
		var tcBomId = parentNode.id;
		var wid = Dialog.open("导入", 280, 280, importUrl, [
				EasyUI.window.button("icon-save", "保存", function() {
					EasyUI.form.submit("form", importUrl + "?tcBomId="+tcBomId+ "&partId="+partId, function(data) {
						if (data.excelErrorMsg) {//如果有错误信息，就显示出来
							var c='<div style="max-height:400px;">'+data.excelErrorMsg+'</div>';
							$.messager.show({
								title:'导入Excel错误',
								msg:c,
								timeout:0,
								showType:'fade'
							});
							return;

						} else if (!data.error) {
							Tip.success("保存成功");
							Loading.show("正在加载");
							JQ.ajaxGet(drawingsList + "?partId=" + partId, function(data) {
								Loading.hide();
								$("#drawingsDg").datagrid({
									data : data
								});
							});
							 Dialog.close(wid);
						}
						filter();
						reload(true);
						if (Dialog.isMore(wid)) {
							Dialog.close(wid);
							add();
						} else {
							Dialog.close(wid);
						}
					});
				}), EasyUI.window.button("icon-cancel", "关闭", function() {
					Dialog.close(wid);
				}) ]);
	};
</script>