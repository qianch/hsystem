<!--
	作者:徐波
	日期:2017-2-13 14:10:25
	页面:原料强制出库JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>

	var dialogWidth = 700, dialogHeight = 350;

	$(function () {
		$("#dg").datagrid({
			url:"${path}productReturnDetails/list",
			onBeforeLoad:dgOnBeforeLoad,
		})
	});
	//查询
	function filter() {
		EasyUI.grid.search("dg", "productReturnDetailSearchForm");
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

		location.href = encodeURI(path + "productReturnDetails/exportExcel?" + JQ.getFormAsString("productReturnDetailSearchForm"));
	}
</script>