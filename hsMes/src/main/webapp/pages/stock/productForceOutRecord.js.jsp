<!--
	作者:徐波
	日期:2017-2-13 14:10:25
	页面:原料强制出库JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	//添加原料强制出库
	var addUrl = path + "productForceOutRecord/add";
	//编辑原料强制出库
	var editUrl = path + "productForceOutRecord/edit";
	//删除原料强制出库
	var deleteUrl = path + "productForceOutRecord/delete";

	var dialogWidth = 700, dialogHeight = 350;

	$(function () {
		$("#dg").datagrid({
			url:"${path}productForceOutRecord/list",
			onBeforeLoad:dgOnBeforeLoad,
		})
	});
	//查询
	function filter() {
		EasyUI.grid.search("dg", "productForceOutRecordSearchForm");
	}

	//添加原料强制出库
	var add = function() {
		var wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [
				EasyUI.window.button("icon-save", "保存", function() {
					EasyUI.form.submit("productForceOutRecordForm", addUrl,
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

	//编辑原料强制出库
	var edit = function() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id="
				+ r.ID, [
				EasyUI.window.button("icon-save", "保存", function() {
					EasyUI.form.submit("productForceOutRecordForm", editUrl,
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
					EasyUI.form.submit("productForceOutRecordForm", editUrl,
							function(data) {
								filter();
								Dialog.close(wid);
							})
				}), EasyUI.window.button("icon-cancel", "关闭", function() {
					Dialog.close(wid)
				}) ]);
	}

	//删除原料强制出库
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
	function formatterIslock(value, row, index) {
		if (value == 1) {
			return "冻结";
		} else {
			return "正常";
		}
	}
	//异常成品导出
	function exportExcel(){
		location.href = encodeURI(path + "productForceOutRecord/exportExcel?" + JQ.getFormAsString("productForceOutRecordSearchForm"));
	}
</script>