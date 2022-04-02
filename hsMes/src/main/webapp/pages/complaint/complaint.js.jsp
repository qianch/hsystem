<!--
	作者:高飞
	日期:2016-11-25 15:40:05
	页面:投诉JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加投诉
var addUrl=path+"complaint/add";
//编辑投诉
var editUrl=path+"complaint/edit";
//删除投诉
var deleteUrl=path+"complaint/delete";
//获取序列号
var serialUrl=path+"complaint/serial";

var dialogWidth=700,dialogHeight=350;
var ccode;

//查询
function filter() {
	EasyUI.grid.search("dg","complaintSearchForm");
}

//添加投诉
var add = function() {
	var wid = Dialog.open("添加投诉信息", dialogWidth, dialogHeight,addUrl, [
		EasyUI.window.button("icon-save", "保存", function() {
			JQ.setValue("#code", ccode);
		EasyUI.form.submit("complaintForm",addUrl, function(data){
			filter();
			if(Dialog.isMore(wid)){
				Dialog.close(wid);
				add();
			}else{
				Dialog.close(wid);
			}
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ],function(){Dialog.more(wid);$("#" + wid).dialog("maximize");}
	);
}

//编辑投诉
var edit = function(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	var wid = Dialog.open("编辑投诉信息", dialogWidth, dialogHeight, editUrl+"?id="+r.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("complaintForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ],function(){
		$("#" + wid).dialog("maximize");
	});
}

/**
 * 双击行，弹出编辑
 */
var dbClickEdit=function(index,row){
	var wid = Dialog.open("编辑投诉信息", dialogWidth, dialogHeight, editUrl+"?id="+row.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("complaintForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ],function(){
		$("#" + wid).dialog("maximize");
	});
}

function dateFormatter(value,row,index){
	return Assert.isEmpty(value)?"":value.substring(0,10);
}

function doSelect(r){
	ccode=r.value;
	JQ.ajaxGet(serialUrl+"?code="+r.value+"&flag=1",function(data){
		$("#suffix").val(data.serial);
		$("input[name='complaintCode']").val(r.value+data.serial);
	});
}

function clickRow(index,row){
	$.ajax({
		url:path+"file/list",
		type:"post",
		data:{path:row.FILEPATH},
		dataType:"json",
		success:function(data){
			$("#attachment").datalist("loadData",data);
		}
	});
	
	$("#question").panel({"content":"<pre>"+row.QUESTIONDESC+"</pre>"});
	$("#analysis").panel({"content":"<pre>"+row.ANALYSISOFCAUSES+"</pre>"});
	$("#corrective").panel({"content":"<pre>"+row.CORRECTIVEMEASURES+"</pre>"});
}

function fileDbClick(index,row){
	location.href= encodeURI(path+"file/download?id="+row.ID+"&md5="+row.MD5);
}

var selectConsumerWindowId;
function selectConsumer() {
	selectConsumerWindowId = Dialog.open("选择客户", 900, 500, path + "selector/consumer?singleSelect=true", [ EasyUI.window.button("icon-ok", "选择", function() {
		var row = $("#_common_consumer_dg").datagrid("getChecked");
		if (row.length == 0) {
			Tip.warn("至少选择一个客户");
			return;
		}
		if (row.length >1) {
			Tip.warn("只能选择一个客户");
			return;
		}
		$("#consumerName").searchbox("setValue", row[0].CONSUMERNAME);
		$("#complaintConsumerId").val(row[0].ID);
		Dialog.close(selectConsumerWindowId);
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(selectConsumerWindowId);
	}) ], function() {
	}, function() {
		Dialog.close(selectConsumerWindowId);
	});
}
function _common_consumer_dbClickRow(index, row) {
	$("#consumerName").searchbox("setValue",row.CONSUMERNAME);
	$("#complaintConsumerId").val(row.ID);
	console.log($("#complaintConsumerId").val());
	Dialog.close(selectConsumerWindowId);
}

function _common_consumer_onLoadSuccess(data) {
	
}
function bomVersionView(value, row, index) {
	if (value == null) {
		return "";
	} else {
		return "<a href='#' title='" + value
				+ "' class='easyui-tooltip' onclick='_bomVersionView("
				+ row.PROCBOMID + "," + row.PRODUCTISTC + ")'>" + value
				+ "</a>"
	}
}
function packBomView(value, row, index){
	if(value==null){
		return "";
	}else if(value=="无包装"){
		return "";
	}else if(row.PRODUCTPACKAGEVERSION==null||row.PRODUCTPACKAGEVERSION==""){
		return "";
	}else{
		return "<a href='#' title='"+value+"' class='easyui-tooltip' onclick='_packBomView("+row.PACKBOMID+")'>"+value+"</a>"
	}
}
var selectProductWindowId;

function selectProduct() {
	selectProductWindowId = Dialog.open("选择产品", 900, 500, path + "selector/product?singleSelect=true&isShow=1", [ EasyUI.window.button("icon-ok", "选择", function() {
		var rows = $("#_common_product_dg").datagrid("getChecked");
		/* if (rows.length > 1) {
			Tip.warn("只能选择一个产品");
			return;
		} */
		if (rows.length == 0) {
			Tip.warn("至少选择一个客户");
			return;
		}
		var s = "";
		for(var i = 0 ; i < rows.length ; i++){
			s += rows[i].PRODUCTMODEL+";";
		}
			$("#productModel").searchbox("setValue",s);
		Dialog.close(selectProductWindowId);
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(selectProductWindowId);
	}) ], function() {
		$("#consumerId").val($("#complaintConsumerId").val());
		
		_common_product_filter();
	}, function() {
		Dialog.close(selectProductWindowId);
	});
}

function _commons_product_dg_onBeforeLoad_callback() {
	var cid=$("#consumerId").val();
	if(cid!=""&&cid!=undefined&&cid!=null){
		return true;
	}else{
		return false;
	}
	
}

function _common_product_dbClickRow(index, row) {
	$("#productModel").searchbox("setValue",row.PRODUCTMODEL);
	Dialog.close(selectProductWindowId);
}

function _common_product_onLoadSuccess(data) {
	var data = $("#product_dg").datagrid("getData");
	for (var i = 0; i < data.rows.length; i++) {
		$("#_common_product_dg").datagrid("selectRecord", data.rows[i]["productId"]);
	}
}

//删除投诉
var doDelete = function(){
	var r=EasyUI.grid.getSelections("dg");
	if(r.length == 0){
		Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
		return;
	}
		
	var ids=[];
	for(var i=0;i<r.length;i++){
		ids.push(r[i].ID);
	}
	Dialog.confirm(function(){
		JQ.ajax(deleteUrl, "post", {
			ids : ids.toString()
		}, function(data){
			filter();
		});
	});
}

function dgLoadSuccess(data){
	$("#dg").datagrid("selectRow",0);
	clickRow(0,(data.rows)[0]);
}

function exportExcel(){
	location.href= encodeURI(path+"excel/export/投诉列表/com.bluebirdme.mes.excel.export.ComplaintExportHandler/filter?"+JQ.getFormAsString("complaintSearchForm"));
}

</script>