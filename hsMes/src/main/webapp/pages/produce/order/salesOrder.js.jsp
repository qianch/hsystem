<!--
	作者:高飞
	日期:2016-10-13 11:06:42
	页面:销售订单JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	//添加销售订单
	var addUrl = path + "salesOrder/add";
	//编辑销售订单
	var editUrl = path + "salesOrder/edit";
	//删除销售订单
	var deleteUrl = path + "salesOrder/delete";
	//打开提交审核页面
	var _auditCommitUrl = path + "selector/commitAudit";
	//发送提交审核数据
	var auditCommitUrl = path + "salesOrder/commitAudit";
	//关闭订单
	var closeUrl = path + "salesOrder/close";

	var dialogWidth = 700, dialogHeight = 500;

	var action = "add";

	var currentConsumerCode = "";

	//查询
	function filter() {
		EasyUI.grid.search("dg", "salesOrderSearchForm");
	}

	$(function() {
		$('#dg').datagrid({
			view : detailview,
			fit : true,
			detailFormatter : function(index, row) {
				return '<div style="padding:2px"><table class="ddv"></table></div>';
			},
			onExpandRow : function(index, row) {
				var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
				ddv.datagrid({
					url : path + 'salesOrder/product?orderId=' + row.ID,
					fitColumns : true,
					
					singleSelect : true,
					rownumbers : true,
					loadMsg : '',
					height : 'auto',
					frozenColumns : [ [
					{
						field:'ID',
						checkbox:true
					},
					{
						field : 'CONSUMERPRODUCTNAME',
						title : '客户产品名称'
					}, {
						field : 'FACTORYPRODUCTNAME',
						title : '厂内名称'
					}, {
						field : 'PRODUCTMODEL',
						title : '产品型号'
					}, {
						field : 'PRODUCTBATCHCODE',
						title : '产品批次号'
					}, {
						field : 'SALESORDERSUBCODE',
						title : '子订单号'
					}, {
						field : 'PRODUCTCOUNT',
						title : '数量',
						styler : function(value, index, row) {
							return "background:rgb(255, 197, 197);"
						}
					},{
						field : 'ALLOCATECOUNT',
						width : 120,
						title : '已分配数量',
						styler : processStyler,
						formatter : countFormatter
					}, {
						field : 'PRODUCEDROLLS',
						title : '生产卷数',
						width : 80,
						styler:processStyler
					}, {
						field : 'PRODUCEDTRAYS',
						title : '打包托数',
						width : 80,
						styler:processStyler
					},{
						field : 'PRODUCECOUNT',
						title : '生产进度',
						width : 80,
						styler:processStyler
					}, {
						field : 'DELIVERYTIME',
						title : '发货时间',
						formatter : function(value, row, index) {
							if (value == undefined)
								return null;
							return new Calendar(value).format("yyyy-MM-dd");
						}
					} ] ],
					columns : [ [

					{
						field : 'PRODUCTWIDTH',
						title : '门幅'
					}, {
						field : 'PRODUCTROLLLENGTH',
						title : '卷长'
					}, {
						field : 'PRODUCTROLLWEIGHT',
						title : '卷重'
					}, {
						field : 'PRODUCTPROCESSCODE',
						title : '工艺标准代码'
					}, {
						field : 'PRODUCTPROCESSBOMVERSION',
						title : '工艺标准版本'
					}, {
						field : 'PRODUCTPACKAGINGCODE',
						title : '包装标准代码'
					}, {
						field : 'PRODUCTPACKAGEVERSION',
						title : '包装标准版本'
					}, {
						field : 'PRODUCTROLLCODE',
						title : '卷标签代码'
					}, {
						field : 'PRODUCTBOXCODE',
						title : '箱唛头代码'
					}, {
						field : 'PRODUCTTRAYCODE',
						title : '托唛头代码'
					}, {
						field : 'PRODUCTMEMO',
						title : '备注',
						width : 50
					}

					] ],
					onResize : function() {
						$('#dg').datagrid('fixDetailRowHeight', index);
					},
					onLoadSuccess : function() {
						setTimeout(function() {
							$('#dg').datagrid('fixDetailRowHeight', index);
						}, 0);
					}
				});
				$('#dg').datagrid('fixDetailRowHeight', index);
			}
		});
	});

	var dialogId;

	//添加销售订单
	var add = function() {
		action = "add";
		dialogId = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [ EasyUI.window.button("icon-save", "保存", function() {
			saveForm();
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(dialogId)
		}) ], function() {
			Dialog.more(dialogId);
			$("#" + dialogId).dialog("maximize");
		});
	}

	//编辑销售订单
	var edit = function() {
		action = "edit";
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if (r.AUDITSTATE > 0) {
			Tip.warn("审核中或审核通过的记录不能编辑！");
			return;
		}
		dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + r.ID, [ EasyUI.window.button("icon-save", "保存", function() {
			saveForm();
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(dialogId)
		}) ], function() {
			$("#" + dialogId).dialog("maximize");
			$("#product_dg").datagrid("loadData", details);
		});
	}

	/**
	 * 保存表单信息
	 * 保存表单的时候，前端会报错，是因为验证表格行的时候，
	 * 首先使行进入编辑状态，验证单元格中的编辑器的值，是否符合要求，
	 * 这时候会加载combobox，但是验证的速度，远大于combobox的请求速度，
	 * 所以验证通过后，这行就结束编辑了，
	 * 这时候combobox的值再返回回来，找不到对应的combobox了，
	 * 所以会报错，可以不管
	 */
	function saveForm() {
		if ($("#salesOrderForm").form("validate")) {
			if ($("#product_dg").datagrid("getRows").length != 0) {
				if (endEdit()) {
					var order = JQ.getFormAsJson("salesOrderForm");
					order.details = $("#product_dg").datagrid("getData").rows;
					$.ajax({
						url : path + "salesOrder/" + action,
						type : 'post',
						dataType : 'json',
						contentType : 'application/json',
						data : JSON.stringify(order),
						success : function(data) {
							Dialog.close(dialogId);
							filter();
						}
					});
				}
			} else {
				Tip.warn("请选择订单产品！");
			}
		}
	}

	/**
	 * 双击行，弹出编辑
	 */
	var dbClickEdit = function(index, row) {
		action = "edit";
		if (row.AUDITSTATE > 0) {
			Tip.warn("审核中或审核通过的记录不能编辑！");
			return;
		}
		dialogId = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [ EasyUI.window.button("icon-save", "保存", function() {
			saveForm();
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(dialogId)
		}) ], function() {
			$("#" + dialogId).dialog("maximize");
			$("#product_dg").datagrid("loadData", details);
		});
	}

	//删除销售订单
	var doDelete = function() {
		var r = EasyUI.grid.getSelections("dg");
		if (r.length == 0) {
			Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
			return;
		}
		var index = null;

		var ids = [];
		for (var i = 0; i < r.length; i++) {
			ids.push(r[i].ID);
			if (r[i].AUDITSTATE > 0) {
				var rs = $("#dg").datagrid('getRows');
				for (var a = 0; a < rs.length; a++) {
					if (rs[a].ID == r[i].ID) {
						index = a + 1;
						Tip.warn("第" + index + "行为审核中或审核通过的记录，不能删除！");
						return;
					}
				}
			}
		}
		Dialog.confirm(function() {
			JQ.ajax(deleteUrl, "post", {
				ids : ids.toString()
			}, function(data) {
				filter();
			});
		});
	}

	function orderDateFormat(value, row, index) {
		if (value == undefined)
			return null;
		return new Calendar(value).format("yyyy-MM-dd");
	}

	function exportFormat(value, row, index) {
		if(value==0){
			return "外销";
		}
		else if(value==-1){
			return "胚布";
		}else{
			return "内销";
		}
	}

	function orderTypeFormat(value, row, index) {
		//（3新品，2试样，1常规产品，-1未知）
		switch (value) {
		case 3:
			return "新品";
		case 2:
			return "试样";
		case 1:
			return "常规产品";
		default:
			return "未知";
		}
	}

	function rowStyler(index, row) {
		return row.SALESORDERISEXPORT == 0 ? "background:rgba(255, 0, 0, 0.23);" : "";
	}

	var selectProductWindowId;

	function selectProduct() {
		if ($("#salesOrderConsumerId").val() == "") {
			Tip.warn("请先选择客户信息");
			selectConsumer();
			return;
		}
		selectProductWindowId = Dialog.open("选择产品", 900, 600, path + "selector/product?singleSelect=false", [ EasyUI.window.button("icon-ok", "选择", function() {
			var rows = $("#_common_product_dg").datagrid("getChecked");
			if (rows.length == 0) {
				Tip.warn("至少选择一个产品");
				return;
			}
			for (var i = 0; i < rows.length; i++) {
				addToProductDg(rows[i]);
			}
			Dialog.close(selectProductWindowId);
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(selectProductWindowId);
		}) ], function() {
			var consumerId = $("#salesOrderConsumerId").val();
			$("#consumerId").val(consumerId);
			$(".datagrid .datagrid-pager").hide();
			$("#_common_product_dg_Form").hide();
			_common_product_filter();
		}, function() {
			Dialog.close(selectProductWindowId);
		});
	}

	function _commons_product_dg_onBeforeLoad_callback() {
		if (Assert.isEmpty($("#consumerId").val())) {
			return false;
		}
		return true;
	}

	function _common_product_dbClickRow(index, row) {
		addToProductDg(row);
		Dialog.close(selectProductWindowId);
	}

	function addToProductDg(r) {
		var _row = {
			"productId" : r.ID,
			"productBatchCode" : "",
			"salesOrderSubCode" : "",
			"consumerProductName" : r.CONSUMERPRODUCTNAME,
			"factoryProductName" : r.FACTORYPRODUCTNAME,
			"productWidth" : r.PRODUCTWIDTH,
			"productRollLength" : r.PRODUCTROLLLENGTH,
			"productRollWeight" : r.PRODUCTROLLWEIGHT,
			"productProcessCode" : r.PRODUCTPROCESSCODE,
			"productProcessBomVersion" : r.PRODUCTPROCESSBOMVERSION,
			"productPackagingCode" : r.PRODUCTPACKAGINGCODE,
			"productPackageVersion" : r.PRODUCTPACKAGEVERSION,
			"productRollCode" : r.PRODUCTROLLCODE,
			"productBoxCode" : r.PRODUCTBOXCODE,
			"productTrayCode" : r.PRODUCTTRAYCODE,
			"productModel" : r.PRODUCTMODEL,
			"productMemo" : r.PRODUCTMEMO,
			"productCount" : 0,
			"productIsTc" : r.PRODUCTISTC,
			"deliveryTime" : "",
			"produceCount" : 0,
			"packagingCount" : 0

		};
		console.log(_row)
		if (!contains(_row))
			$("#product_dg").datagrid("appendRow", _row);
	}

	function contains(row) {
		var data = $("#product_dg").datagrid("getData");
		if (data.total == 0) {
			return false;
		} else {
			for (var i = 0; i < data.rows.length; i++) {
				if (data.rows[i]["productId"] == row["productId"]) {
					return true
				}
			}
			return false;
		}
	}

	function _common_product_onLoadSuccess(data) {
		var data = $("#product_dg").datagrid("getData");
		for (var i = 0; i < data.rows.length; i++) {
			$("#_common_product_dg").datagrid("selectRecord", data.rows[i]["productId"]);
		}
	}
	var editingIndex = -1;

	function clickRow(index, row) {
		if (editingIndex != -1) {
			if ($("#product_dg").datagrid("validateRow", editingIndex)) {

				$("#product_dg").datagrid("endEdit", editingIndex);

				editingIndex = index;
				$("#product_dg").datagrid("beginEdit", index);
			}
		} else {
			editingIndex = index;
			$("#product_dg").datagrid("beginEdit", index);
		}
	}

	var bomVersion, bcVersion;

	/* ////1:是套材  2:非套材
	 function beforeEdit(index,row){
	 Loading.show("加载工艺BOM版本信息");
	 $.ajax({
	 url:path+"bom/"+(row.productIsTc==1?"":"f")+"tc/"+row.productProcessCode,
	 type:"get",
	 async:false,
	 dataType:"json",
	 success:function(data){
	 bomVersion=data;
	 }
	 });
	 Loading.hide();
	 Loading.show("加载包材BOM版本信息");
	 $.ajax({
	 url:path+"bom/packaging/"+row.productPackagingCode,
	 type:"get",
	 async:false,
	 dataType:"json",
	 success:function(data){
	 bcVersion=data;
	 }
	 });
	 Loading.hide();
	 }
	 */

	function onBeforeLoad(param) {
		var row = EasyUI.grid.getRowByIndex("product_dg", editingIndex);
		param.code = row.productProcessCode;
		param.bomType = (row.productIsTc == 1 ? "tc" : "ftc");
	}

	function onBeforeLoad_bc(param) {
		var row = EasyUI.grid.getRowByIndex("product_dg", editingIndex);
		param.code = row.productPackagingCode;
		param.bomType = 'bc';
	}

	function endEdit() {
		var rows = $("#product_dg").datagrid("getRows");
		for (var i = 0; i < rows.length; i++) {
			editingIndex = i;
			$("#product_dg").datagrid("beginEdit", i);
			if (!$("#product_dg").datagrid("validateRow", i)) {
				return false;
			} else {
				$("#product_dg").datagrid("endEdit", i);
			}
		}
		editingIndex = -1;
		return true;
	}

	//选择业务员信息
	var selectUserWindowId;
	function selectUser() {
		selectUserWindowId = Dialog.open("选择用户", 900, 500, path + "selector/user?singleSelect=true", [ EasyUI.window.button("icon-ok", "选择", function() {
			var row = $("#_common_user_dg").datagrid("getChecked");
			if (row.length == 0) {
				Tip.warn("至少选择一个用户");
				return;
			}
			$("#salesOrderBizUserName").searchbox("setValue", row[0].USERNAME);
			$("#salesOrderBizUserId").val(row[0].ID);
			Dialog.close(selectUserWindowId);
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(selectUserWindowId);
		}) ], function() {
		}, function() {
			Dialog.close(selectUserWindowId)
		});
	}
	function _common_user_dbClickRow(index, row) {
		$("#salesOrderBizUserName").searchbox("setValue", row.USERNAME);
		$("#salesOrderBizUserId").val(row.ID);
		Dialog.close(selectUserWindowId);
	}

	function _common_user_onLoadSuccess(data) {
		var rows = $("#_common_user_dg").datagrid("getRows");
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].ID == $("#salesOrderBizUserId").val()) {
				$("#_common_user_dg").datagrid("checkRow", i);
			}
		}
	}

	var consumerProducts = {};

	//选择客户信息
	var selectConsumerWindowId;
	function selectConsumer() {
		selectConsumerWindowId = Dialog.open("选择客户", 900, 500, path + "selector/consumer?singleSelect=true", [ EasyUI.window.button("icon-ok", "选择", function() {
			var row = $("#_common_consumer_dg").datagrid("getChecked");
			if (row.length == 0) {
				Tip.warn("至少选择一个客户");
				return;
			}
			if (row[0].ID != $("#salesOrderConsumerId").val() && $("#salesOrderConsumerId").val() != "") {
				Dialog.confirm(function() {

					//备份数据
					consumerProducts[$("#salesOrderConsumerId").val()] = $("#product_dg").datagrid("getData").rows;
					//切换数据
					$("#product_dg").datagrid("loadData", consumerProducts[row[0].ID] ? consumerProducts[row[0].ID] : []);

					$("#salesOrderConsumerName").searchbox("setValue", row[0].CONSUMERNAME);
					$("#salesOrderConsumerId").val(row[0].ID);
					$("#consumerCode").val(row[0].CONSUMERCODE);
					Dialog.close(selectConsumerWindowId);
				}, "选择了不同的客户信息，会变更产品列表信息，是否继续？");
			} else {
				$("#salesOrderConsumerName").searchbox("setValue", row[0].CONSUMERNAME);
				$("#salesOrderConsumerId").val(row[0].ID);
				$("#consumerCode").val(row[0].CONSUMERCODE);
				Dialog.close(selectConsumerWindowId);
			}

		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(selectConsumerWindowId);
		}) ], function() {
		}, function() {
			Dialog.close(selectConsumerWindowId)
		});
	}
	function _common_consumer_dbClickRow(index, row) {
		if (row.ID != $("#salesOrderConsumerId").val() && $("#salesOrderConsumerId").val() != "") {
			Dialog.confirm(function() {

				//备份数据
				consumerProducts[$("#salesOrderConsumerId").val()] = $("#product_dg").datagrid("getData").rows;
				//切换数据
				$("#product_dg").datagrid("loadData", consumerProducts[row.ID] ? consumerProducts[row.ID] : []);

				$("#salesOrderConsumerName").searchbox("setValue", row.CONSUMERNAME);
				$("#salesOrderConsumerId").val(row.ID);
				$("#consumerCode").val(row.CONSUMERCODE);
				Dialog.close(selectConsumerWindowId);
			}, "选择了不同的客户信息，会变更产品列表信息，是否继续？");
		} else {
			$("#salesOrderConsumerName").searchbox("setValue", row.CONSUMERNAME);
			$("#salesOrderConsumerId").val(row.ID);
			$("#consumerCode").val(row.CONSUMERCODE);
			Dialog.close(selectConsumerWindowId);
		}
	}

	function _common_consumer_onLoadSuccess(data) {
		var rows = $("#_common_consumer_dg").datagrid("getRows");
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].ID == $("#salesOrderConsumerId").val()) {
				$("#_common_consumer_dg").datagrid("checkRow", i);
			}
		}
	}

	function removeProduct() {
		var rows = $("#product_dg").datagrid("getSelections");
		for (var i = 0; i < rows.length; i++) {
			$("#product_dg").datagrid("deleteRow", $("#product_dg").datagrid("getRowIndex", rows[i]));
		}
	}

	//审核销售订单
	var doAudit = function() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if (r.AUDITSTATE > 0) {
			Tip.warn("审核中或审核通过的记录，不能在提交审核！");
			return;
		}
		var wid = Dialog.open("审核", 370, 120, _auditCommitUrl + "?id=" + r.ID, [ EasyUI.window.button("icon-ok", "提交审核", function() {
			EasyUI.form.submit("editAuditProduce", auditCommitUrl, function(data) {
				filter();
				Dialog.close(wid);
			});
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ],function(){
			$("#editAuditProduce #name").textbox("setValue","销售订单审核，单号："+r.SALESORDERCODE);
		});
	}

	function formatterReviewState(val, row, index) {
		return auditStateFormatter(row.AUDITSTATE);
	}
	function closeOrder() {
		var r = EasyUI.grid.getSelections("dg");
		if (r.length == 0) {
			Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
			return;
		}
		
		var index = null;

		var ids = [];
		for (var i = 0; i < r.length; i++) {
			ids.push(r[i].ID);
			if (r[i].AUDITSTATE != 2) {
				Tip.warn("审核通过的订单才可以关闭");
				return;
			}
		}
		Dialog.confirm(function() {
			JQ.ajaxGet(closeUrl + "?_ids=" + ids.join(","), function(data){
				Tip.suc("操作成功");
				filter();
			})
		});
	}

	function stateFormatter(value, row, index) {
		if (value == null || value == 0) {
			return "<label style='background:green;width:100%;display: inline-block;color:white;text-align:center;'>正常</label>";
		}
		return "<label style='background:red;width:100%;display: inline-block;color:white;text-align:center;'>关闭</label>";
	}

	var viewUrl = path + "audit/XS/{id}/state";
	function view() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if (r == null)
			return;
		dialogId = Dialog.open("查看审核状态", dialogWidth, dialogHeight, viewUrl.replace("{id}", r.ID), [ EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(dialogId)
		}) ], function() {
			$("#" + dialogId).dialog("maximize");
		});

	}
	
	function orderRowClick(index,row){
		$("#salesOrderMemo").panel({content:"<pre>"+row.SALESORDERMEMO+"</pre>"});
	}
	
	function exportExcel(){
		location.href= encodeURI(path+"excel/export/销售订单/com.bluebirdme.mes.excel.export.SalesOrderExportHandler/filter?"+JQ.getFormAsString("salesOrderSearchForm"));
	}
</script>