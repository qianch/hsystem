<!--
	作者:肖文彬
	日期:2016-11-16 11:25:41
	页面:原料入库记录表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加原料入库记录表
var addUrl=path+"stock/materialInRecord/add";
//编辑原料入库记录表
var editUrl=path+"stock/materialInRecord/edit";
//删除原料入库记录表
var deleteUrl=path+"stock/materialInRecord/delete";

var dialogWidth=700,dialogHeight=350;

//查询
function filter() {
	EasyUI.grid.search("dg","materialInRecordSearchForm");
}

/* //查询默认时间
$(function(){
	$("#produceStart").datetimebox("setValue",new Calendar().format("yyyy-MM-dd")+" 08:00:00");
	$("#produceEnd").datetimebox("setValue",new Calendar().format("yyyy-MM-dd")+" 20:00:00");
	$("#inStart").datetimebox("setValue",new Calendar().format("yyyy-MM-dd")+" 08:00:00");
	$("#inEnd").datetimebox("setValue",new Calendar().format("yyyy-MM-dd")+" 20:00:00");
	filter();
}); */

//添加原料入库记录表
var add = function() {
	var wid = Dialog.open("添加", dialogWidth, dialogHeight,addUrl, [
		EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("materialInRecordForm",addUrl, function(data){
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

//编辑原料入库记录表
var edit = function(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl+"?id="+r.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("materialInRecordForm", editUrl, function(data) {
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
		EasyUI.form.submit("materialInRecordForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}

//删除原料入库记录表
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
//导出原料入库明细记录
function exportInRecordDetail(){
	location.href= encodeURI(path +  "stock/materialInRecord/materialInRecordExport?"+JQ.getFormAsString("materialInRecordSearchForm"));
}
</script>