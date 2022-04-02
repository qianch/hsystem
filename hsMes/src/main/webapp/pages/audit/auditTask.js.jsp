<!--
	作者:高飞
	日期:2016-10-25 13:52:50
	页面:流程实例JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//编辑流程实例
var auditUrl=path+"audit/";

var dialogWidth=700,dialogHeight=350;

var dialogId;

//查询
function filter() {
	EasyUI.grid.search("dg","auditInstanceSearchForm");
}

//编辑流程实例
var edit = function(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	dialogId = Dialog.open(r.AUDITTITLE, dialogWidth, dialogHeight, path+"audit/"+r.ID, [
	    EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(dialogId)
		})],function(){
		$("#"+dialogId).dialog("maximize");
	}
	);
}

/**
 * 双击行，弹出编辑
 */
var dbClickEdit=function(index,row){
	dialogId = Dialog.open(row.AUDITTITLE, dialogWidth, dialogHeight, path+"audit/"+row.ID, [
	    EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(dialogId);
		})],function(){
		$("#"+dialogId).dialog("maximize");
	}
	);
}

function formatNode(value,row,index){
	if(value==1){
		return "一级审核";
	}
	if(value==2){
		return "二级审核";
	}
}
function formatIsfinish(value,row,index){
	if(value==1){
		return "完成";
	}else{
		return "未完成";
	}
}
function resultFormatter(value, row, index) {
	if (value == -1)
		return "<font color=red>不通过</font>";
	if (value == 2)
		return "<font color=green>通过</font>";
	
}

</script>