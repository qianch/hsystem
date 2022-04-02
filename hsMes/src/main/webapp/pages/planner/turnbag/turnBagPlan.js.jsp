<!--
	作者:高飞
	日期:2017-2-9 11:28:32
	页面:翻包计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	//添加翻包计划
	var addUrl = path + "planner/tbp/add";
	//编辑翻包计划
	var editUrl = path + "planner/tbp/edit";
	//删除翻包计划
	var deleteUrl = path + "planner/tbp/delete";
	//打开提交审核页面
	var _auditCommitUrl = path + "selector/commitAudit";
	//提交审核流程
	var auditCommitUrl = path + "planner/tbp/commitAudit";
	//获取批次号
	var batchCode = path + "planner/tbp/batchCode";
	//获取明细
	var detailsUrl= path + "planner/tbp/details";
	
	var completeUrl = path + "planner/tbp/complete";

	var dialogWidth = 760, dialogHeight = 520;
	
	var wid,orderWid;

	//查询
	function filter() {
		EasyUI.grid.search("dg", "turnBagPlanSearchForm");
	}

	//添加翻包计划
	var add = function() {
		wid = Dialog.open("添加", dialogWidth, dialogHeight, addUrl, [ EasyUI.window.button("icon-save", "保存", function() {
			if (!$("#turnBagPlanForm").form("validate")) {
				return;
			}
			save();
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid)
		}) ], function() {
			Dialog.more(wid);
			Dialog.max(wid);
		});
	}

	//编辑翻包计划
	var edit = function() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if (r.AUDITSTATE > 0) {
			Tip.warn("审核中或已通过的计划无法编辑");
			return;
		}
		dbClickEdit(0, r);
	}

	/**
	 * 双击行，弹出编辑
	 */
	var dbClickEdit = function(index, row) {
		if (row.AUDITSTATE > 0) {
			Tip.warn("审核中或已通过的计划无法编辑");
			return;
		}
		wid = Dialog.open("添加", dialogWidth, dialogHeight, editUrl + "?id=" + row.ID, [ EasyUI.window.button("icon-save", "保存", function() {
			if (!$("#turnBagPlanForm").form("validate")) {
				return;
			}
			save();
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid)
		}) ], function() {
			Dialog.more(wid);
			Dialog.max(wid);
		});
	}
	
	function save(){
		var plan = JQ.getFormAsJson("turnBagPlanForm");
		var rows=$("#oldOrders").datagrid("getRows");
		var details=[];
		var repeat={};
		for(var i=0;i<rows.length;i++){
			$("#oldOrders").datagrid("beginEdit",i);
			if(!$("#oldOrders").datagrid("validateRow",i)){
				return;
			}
			$("#oldOrders").datagrid("endEdit",i);
			if(!isEmpty(repeat[rows[i].SALESORDERDETAILID])){
				if(repeat[rows[i].SALESORDERDETAILID]==rows[i].BATCHCODE){
					Tip.warn("重复的批次号");
					return;
				}
			}
			repeat[rows[i].SALESORDERDETAILID]=rows[i].BATCHCODE;
			
			details.push({
				salesOrderDetailId:rows[i].SALESORDERDETAILID,
				batchCode:rows[i].BATCHCODE,
				turnBagCount:rows[i].TURNBAGCOUNT,
				turnBagCountProcess:0
			});
		}
		if(details.length==0){
			Tip.warn("请添加待翻包的订单");
			return;
		}
		plan.details=details;
		
		Dialog.confirm(function(){
			Loading.show();
			$.ajax({
				url : addUrl,
				type : 'post',
				dataType : 'json',
				contentType : 'application/json',
				data : JSON.stringify(plan),
				success : function(data) {
					Loading.hide();
					if(Tip.hasError(data)){
						return;
					}
					Tip.success("保存成功");
					Dialog.close(wid);
					filter();
				}
			});
		},"确认保存?");
	}

	//删除翻包计划
	var doDelete = function() {
		var r = EasyUI.grid.getSelections("dg");

		if (r.length == 0) {
			Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
			return;
		}

		if (r[0].AUDITSTATE > 0) {
			Tip.warn("审核中或已通过的计划无法编辑");
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
				Tip.success("删除成功");
				
			});
		});
	}

	function salesOrderSelect(value, name) {
		onOrderProductSelectDblClickRow=function(index, row) {
			loadInfo(row);
			Dialog.close(orderId);
		}
		orderId = Dialog.open("选择订单", 1000, 400, path + "/planner/tbp/order/select", [ EasyUI.window.button("icon-save", "添加", function() {
			var rs = $("#orderProductSelect").datagrid("getChecked");
			if (rs.length == 0) {
				return;
			}
			loadInfo(rs[0]);
			Dialog.close(orderId);
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(orderId);
		}) ]);
	}
	
	function loadInfo(row){
		$("#newSalesOrderCode").searchbox("setText", row.SALESORDERSUBCODE);
		$("#newSalesOrderCode").searchbox("setValue",row.SALESORDERSUBCODE);
		$("#newSalesOrderDetailsId").val(row.ID);
		$("#newConsumer").val(row.CONSUMERNAME);
		$("#newProductModel").val(row.PRODUCTMODEL);
		$("#oldOrders").datagrid("loadData",[]);
		$("#deliveryDate").datebox("setValue",row.DELIVERYTIME.substring(0,10));
		//finishTime
		var cal=new Calendar(row.DELIVERYTIME);
		cal.add(Calendar.field.DAY_OF_MONTH,-1);
		$("#finishTime").datebox("setValue",cal.format("yyyy-MM-dd"));
		var memoInfo="";
		memoInfo+="产品型号："+row.PRODUCTMODEL+"\n";
		memoInfo+="门幅(mm)："+(isEmpty(row.PRODUCTWIDTH)?"无":row.PRODUCTWIDTH)+"\n";
		memoInfo+="卷长 (m)："+(isEmpty(row.PRODUCTROLLLENGTH)?"无":row.PRODUCTROLLLENGTH)+"\n";
		memoInfo+="卷重(kg)："+(isEmpty(row.PRODUCTROLLWEIGHT)?"无":row.PRODUCTROLLWEIGHT)+"\n";
		memoInfo+="工艺代码："+row.PRODUCTPROCESSCODE+"\n";
		memoInfo+="工艺版本："+row.PRODUCTPROCESSBOMVERSION+"\n";
		memoInfo+="包装代码："+row.PRODUCTPACKAGINGCODE+"\n";
		memoInfo+="包装版本："+row.PRODUCTPACKAGEVERSION+"\n";
		$("#memo").val(memoInfo);
		loadBatchCode(row);
	}

	function dgLoadSuccess(data) {
		if(data.total == 0) {
			$("#CONTENT td").each(function(i, e) {
				if (!$(this).hasClass("title")) {
					$(this).html("");
				}
			});
			$("#details").datagrid("loadData",[]);
			return;
		}
		doClick(0, data.rows[0]);
	}

	function orderProductSelectLoadSuccess(data) {
		Loading.hide();
		$(this).datagrid("enableFilter");
		$(".datagrid-filter[name='SALESORDERTYPE']").remove();
		$(".datagrid-filter[name='SALESORDERISEXPORT']").remove();
		$(".datagrid-filter[name='SALESORDERDATE']").remove();
	}

	function onOrderProductSelectDblClickRow(index, row) {

		row.SALESORDERDETAILID=row.ID;
		appendRow(row);
		Dialog.close(orderId);
	}

	function loadBatchCode(row) {
		$("#newBatchCode").combobox("setValue", null);
		Loading.show();
		JQ.ajaxPost(batchCode, {
			orderCode : row.SALESORDERSUBCODE,
			productId : row.PRODUCTID,
			isNew : true
		}, function(data) {
			$("#newBatchCode").combobox("loadData", data);
			if(data.length==1){
				$("#newBatchCode").combobox("select", data[0].v);
			}
			Loading.hide();
		});
	}

	function addOldSalesOrder() {
		onOrderProductSelectDblClickRow=function(index,row){
			row.SALESORDERDETAILID=row.ID;
			appendRow(row);
			Dialog.close(orderId);
		}
		orderId = Dialog.open("选择订单", 1000, 400, path + "/planner/tbp/order/select", [ EasyUI.window.button("icon-save", "添加", function() {
				var rs = $("#orderProductSelect").datagrid("getChecked");
				if (rs.length == 0) {
					Tip.warn("请选择订单");
					return;
				}
				/* $("#oldOrders").datagrid("appendRow",rs[0]); */
				rs[0].SALESORDERDETAILID=rs[0].ID;
				appendRow(rs[0]);
				Dialog.close(orderId);
			}), EasyUI.window.button("icon-cancel", "关闭", function() {
				Dialog.close(orderId);
			})
		]);
	}
	
	function appendRow(row){
		if(row.SALESORDERSUBCODE==$("#newSalesOrderCode").searchbox("getText")){
			Tip.warn("订单号和新订单号一致，无法翻包");
			return;
		}
		$("#oldOrders").datagrid("appendRow",row);
	}
	
	function deleteOldSalesOrder(){
		var rows=$("#oldOrders").datagrid("getChecked");
		if(rows.length==0){
			Tip.warn("请至少选择一行");
			return;
		}
		Dialog.confirm(function(){
			$("#oldOrders").datagrid("deleteRow",EasyUI.grid.getRowIndex("oldOrders",rows[0]));
		},"确定删除?");
	}
	
	var editingRow;
	
	function onBeforeEdit(index,row){
		editingRow=row;
	}
	
	function loadOldBatchCode(param){
		param.orderCode=editingRow.SALESORDERSUBCODE;
		param.productId=editingRow.PRODUCTID;
		param.isNew=false;
	}
	
	function onChange(newValue,oldValue){
		var text=$(this).combobox("getText");
		if(!isEmpty(text)){
			var count=text.substring(text.indexOf("*")+1).replace("托","");
			var index=$(this).parent().parent().parent().parent().parent().parent().parent().attr("datagrid-row-index");
			var countEditor=$("#oldOrders").datagrid("getEditor",{index:index,field:'TURNBAGCOUNT'});
			$(countEditor.target).numberspinner({max:parseInt(count)});
		}
	}
	
	function comboboxLoadSuccess(data){
		var text=$(this).combobox("getText");
		if(!isEmpty(text)){
			var count=text.substring(text.indexOf("*")+1).replace("托","");
			var index=$(this).parent().parent().parent().parent().parent().parent().parent().attr("datagrid-row-index");
			var countEditor=$("#oldOrders").datagrid("getEditor",{index:index,field:'TURNBAGCOUNT'});
			var turnBagCount=$(countEditor.target).numberspinner("getValue");
			$(countEditor.target).numberspinner({max:parseInt(count)});
			if(!isEmpty(turnBagCount)){
				$(countEditor.target).numberspinner("setValue",turnBagCount);
			}
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

	function stateFormatter(value, row, index) {
		if (value == null || value == 0) {
			return "<label style='background:green;width:100%;display: inline-block;color:white;text-align:center;'>正常</label>";
		}
		return "<label style='background:red;width:100%;display: inline-block;color:white;text-align:center;'>关闭</label>";
	}

	function rowStyler(index, row) {
		var style = row.SALESORDERISEXPORT == 0 ? "background:rgba(255, 0, 0, 0.23);" : "";
		if (isEmpty(row.closed) || row.closed == 0) {
		} else {
			style += "text-decoration:line-through;background: #989696;";
		}

		return style;
	}

	function orderDateFormat(value, row, index) {
		if (value == undefined)
			return null;
		return new Calendar(value).format("yyyy-MM-dd");
	}
	function exportFormat(value, row, index) {
		return value == 0 ? "外销" : "内销";
	}

	function doClick(index, row) {
		for ( var index in row) {
			if(index=="DELIVERYDATE"){
				$("#" + index).html(row[index].substring(0,10));
			}else if (index == "MEMO") {
				$("#" + index).html("<pre style='margin: 2px; color: #16bfb9; font-family: -webkit-body;'>" + (isEmpty(row[index])?"":row[index]) + "</pre>");
			} else {
				$("#" + index).html(row[index]);
			}
		}
		JQ.ajaxGet(detailsUrl+"?id="+row.ID,function(data){
			$("#details").datagrid("loadData",data);
		});
	}

	//审核销售订单
	var doAudit = function() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if (r.AUDITSTATE > 0) {
			Tip.warn("审核中或审核通过的记录，不能在提交审核！");
			return;
		}
		wid = Dialog.open("提交审核", 600, 127, _auditCommitUrl + "?id=" + r.ID, [ EasyUI.window.button("icon-ok", "提交审核", function() {
			EasyUI.form.submit("editAuditProduce", auditCommitUrl, function(data) {
				filter();
				Dialog.close(wid);
			});
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ], function() {
			$("#editAuditProduce #name").textbox("setValue", "翻包任务单审核，单号：" + r.TRUNBAGCODE);
		});
	}

	var viewUrl = path + "audit/FB/{id}/state";
	function view() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if (r == null)
			return;
		wid = Dialog.open("查看审核状态", dialogWidth, dialogHeight, viewUrl.replace("{id}", r.ID), [ EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid)
		}) ], function() {
			//$("#" + dialogId).dialog("maximize");
		});

	}

	function isCompleteFormatter(value, row, index) {
		if (value == 1)
			return "已完成";
		return "<font color='red'>未完成</font>";
	}

	function completeStyler(value, row, index) {
		if (value == undefined)
			return "";

		if (row.ISCOMPLETED == 1) {
			return "background: #0e8407;color: white;";
		}
	}

	function complete() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if (r == null)
			return;
		if (r.AUDITSTATE != 2) {
			Tip.warn("只有审核通过的才能完成");
			return;
		}
		Dialog.confirm(function() {
			Loading.show();
			JQ.ajaxPost(completeUrl, {
				id : r.ID
			}, function(data) {
				Loading.hide();
				Tip.success("更改成功");
				filter();
			}, function() {
				Loading.hide();
			})
		}, "确定已完成？");
	}

	//获取翻包任务单号
	function getSerial() {
		Loading.show();
		$.ajax({
			url : path + "planner/tbp/serial",
			type : "post",
			dataType : "text",
			success : function(data) {
				Loading.hide();
				$("#trunBagCode").textbox("setValue", data);
			},
			error : function() {
				Loading.hide();
			}
		});
	}
	
	function processFormatter(value,row,index){
		return row.TURNBAGCOUNTPROCESS+"/"+row.TURNBAGCOUNT;
	}
	
	function processStyler(index,row){
		return "text-align:center;color:red;font-weight:bold;background:url(resources/images/ps.png) no-repeat;background-size:"+(row.TURNBAGCOUNTPROCESS/row.TURNBAGCOUNT)*100+"% 100%;";
	}
	
</script>