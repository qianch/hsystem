<!--
	作者:徐波
	日期:2016-10-24 15:08:19
	页面:原料库存表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	//添加原料库存表
	var addUrl = path + "stock/materialStock/add";
	//编辑原料库存表
	var editUrl = path + "stock/materialStock/edit";
	//删除原料库存表
	var deleteUrl = path + "stock/materialStock/delete";

	var dialogWidth = 700, dialogHeight = 350;

	function formatterStockState(value, row) {
		if (value == 1) {
			return "入库";
		} else {
			return "出库";
		}

	}

	function rowStyler(index, row) {
		var str = new Date(row.MATERIALSHELFLIFES);
		str=str.setTime(str.getTime()-8000*60*60);
		var date1 = new Date();
		date1=date1.setDate(date1.getDate()-1);  
		if (str < date1) {
			return 'background-color:red';
		}
	}

	function formatterState(value, row) {
		if (value == 0) {
			return "待检";
		}
		if (value == 1) {
			return "合格";
		}
		if (value == 2) {
			return "不合格";
		}
		if (value == 3) {
			return "冻结";
		}
		if (value == 4) {
			return "放行";
		}
	}

	//查询
	function filter() {
		EasyUI.grid.search("dg", "materialStockSearchForm");
	}

	function sentenceLevel() {
		var r = EasyUI.grid.getSelections("dg");
		if (r.length == 0) {
			Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
			return;
		}

		var ids = [];
		for (var i = 0; i < r.length; i++) {
			ids.push(r[i].ID);
		}
		var wid = Dialog.open("设置", 400, 200, addUrl, [
				EasyUI.window.button("icon-save", "保存", function() {
					var state = $("#state").combobox("getValue");
					JQ.ajax(addUrl, "post", {
						ids:ids.toString(),
						state:state
					}, function(data){
						filter();
						Dialog.close(wid)
					})
				}), EasyUI.window.button("icon-cancel", "关闭", function() {
					Dialog.close(wid)
				}) ]);
		
	}

	//添加原料库存表
	var add = function() {
		var wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
				EasyUI.window.button("icon-save", "保存", function() {
					EasyUI.form.submit("materialStockForm", addUrl, function(
							data) {
						filter();
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

	//编辑原料库存表
	var edit = function() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id="
				+ r.ID, [ EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("materialStockForm", editUrl, function(data) {
				filter();
				Dialog.close(wid);
			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid)
		}) ]);
	}

	/**
	 * 双击行，弹出编辑
	 */
	var dbClickEdit = function(index, row) {
		var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id="
				+ row.ID, [ EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("materialForm", editUrl, function(data) {
				filter();
				Dialog.close(wid);
			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid)
		}) ]);
	}

	//删除原料库存表
	var doDelete = function() {
		var r = EasyUI.grid.getSelections("dg");
		if (r.length == 0) {
			Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
			return;
		}

		var ids = [];
		for (var i = 0; i < r.length; i++) {
			ids.push(r[i].ID);
		}
		Dialog.confirm(function() {
			JQ.ajax(deleteUrl, "post", {
				ids : ids.toString()
			}, function(data) {
				filter();
			});
		});
	}
</script>