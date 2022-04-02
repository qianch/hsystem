<!--
	作者:肖文彬
	日期:2016-11-16 12:33:40
	页面:出库单明细JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	//添加出库单明细
	var addUrl = path + "stock/materialOutOrderDetail/add";
	//编辑出库单明细
	var editUrl = path + "stock/materialOutOrderDetail/edit";
	//删除出库单明细
	var deleteUrl = path + "stock/materialOutOrderDetail/delete";

	var dialogWidth = 700, dialogHeight = 350;

	//查询
	function filter() {
		EasyUI.grid.search("dg", "materialOutOrderDetailSearchForm");
	}

	//添加出库单明细
	var add = function() {
		var wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
				EasyUI.window.button("icon-save", "保存", function() {
					EasyUI.form.submit("materialOutOrderDetailForm", addUrl,
							function(data) {
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

	//编辑出库单明细
	var edit = function() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id="
				+ r.ID, [
				EasyUI.window.button("icon-save", "保存", function() {
					EasyUI.form.submit("materialOutOrderDetailForm", editUrl,
							function(data) {
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
				+ row.ID, [
				EasyUI.window.button("icon-save", "保存", function() {
					EasyUI.form.submit("materialOutOrderDetailForm", editUrl,
							function(data) {
								filter();
								Dialog.close(wid);
							})
				}), EasyUI.window.button("icon-cancel", "关闭", function() {
					Dialog.close(wid)
				}) ]);
	}

	//删除出库单明细
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
	//导出原料出库单明细
	function exportOutRecordDetail() {
		location.href = path
				+ "stock/materialOutOrderDetail/materialOutRecordExport?"
				+ JQ.getFormAsString("materialOutOrderDetailSearchForm");
	}
</script>