<!--
	作者:徐波
	日期:2016-11-30 14:03:19
	页面:生产追溯日志JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加生产追溯日志
var addUrl=path+"tracingLog/add";
//编辑生产追溯日志
var editUrl=path+"tracingLog/edit";
//删除生产追溯日志
var deleteUrl=path+"tracingLog/delete";

var dialogWidth=700,dialogHeight=350;

//查询
function filter() {
	EasyUI.grid.search("dg","tracingLogSearchForm");
}

//添加生产追溯日志
var add = function() {
	var wid = Dialog.open("添加", dialogWidth, dialogHeight,addUrl, [
		EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("tracingLogForm",addUrl, function(data){
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

//编辑生产追溯日志
var edit = function(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl+"?id="+r.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("tracingLogForm", editUrl, function(data) {
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
		EasyUI.form.submit("tracingLogForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}

//删除生产追溯日志
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

function formatterStatus(value,row,index){
	if(value=='9'){
		return "冻结";
	}else if(value=='10'){
		return "解冻";
	}
}
//投诉编码
function formatterCode(value,row,index){
	var str="";
	if(value!=undefined){
		var sIndex=value.indexOf("投诉号：");
		if(sIndex!=-1){
			var eIndex=value.lastIndexOf("：");
			//截取字符串
			str=value.substring(sIndex+4,eIndex-4);
			if(str=="null "){
				str="";
			}
		}else{
			str="";
		}
	}
	return str;
}
//操作原因
function formatterCause(value,row,index){
	var str="";
	if(value!=undefined){
		var sIndex=value.lastIndexOf("：");
		str=value.substring(sIndex+1);
	}
	return str;
}
</script>