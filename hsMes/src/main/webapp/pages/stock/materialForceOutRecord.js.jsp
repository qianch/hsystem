<!--
	作者:徐波
	日期:2017-2-13 14:10:25
	页面:原料强制出库JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加原料强制出库
var addUrl=path+"materialForceOutRecord/add";
//编辑原料强制出库
var editUrl=path+"materialForceOutRecord/edit";
//删除原料强制出库
var deleteUrl=path+"materialForceOutRecord/delete";

var dialogWidth=700,dialogHeight=350;

//查询
function filter() {
	EasyUI.grid.search("dg","materialForceOutRecordSearchForm");
}

//添加原料强制出库
var add = function() {
	var wid = Dialog.open("添加", dialogWidth, dialogHeight,addUrl, [
		EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("materialForceOutRecordForm",addUrl, function(data){
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
	}) ],function(){Dialog.more(wid);}
	);
}

//编辑原料强制出库
var edit = function(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl+"?id="+r.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("materialForceOutRecordForm", editUrl, function(data) {
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
var dbClickEdit=function(index,row){
	var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl+"?id="+row.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("materialForceOutRecordForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}

//删除原料强制出库
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
	if (value == 5) {
		return "退货";
	}
}
function formatterIslock(value, row, index) {
	if (value == 1) {
		return "冻结";
	} else {
		return "正常";
	}
}
function formatterIspass(value, row, index) {
	if (value == 1) {
		return "是";
	} else {
		return "否";
	}
}
//导出异常原料退回记录
function exportMaterialForceOutRecord() {
	location.href = encodeURI(path+ "materialForceOutRecord/exportExcel?"+ JQ.getFormAsString("materialForceOutRecordSearchForm"));
}



</script>