<!--
	作者:高飞
	日期:2016-11-6 10:22:52
	页面:PDA终端版本JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加PDA终端版本
var addUrl=path+"app/add";
//编辑PDA终端版本
var editUrl=path+"app/edit";
//删除PDA终端版本
var deleteUrl=path+"app/delete";

var dialogWidth=700,dialogHeight=350;

//查询
function filter() {
	EasyUI.grid.search("dg","appVersionSearchForm");
}

//添加PDA终端版本
var add = function() {
	var wid = Dialog.open("添加", dialogWidth, dialogHeight,addUrl, [
		EasyUI.window.button("icon-save", "保存", function() {
			if($("#tips").text()=="请上传文件"){
				Tip.warn("请上传新版本APK文件");
				return;
			}
			EasyUI.form.submit("appVersionForm",addUrl, function(data){
				filter();
				if(Dialog.isMore(wid)){
					Dialog.close(wid);
					add();
				}else{
					Dialog.close(wid);
				}
			});
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ],function(){Dialog.more(wid);}
	);
}

//编辑PDA终端版本
var edit = function(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl+"?id="+r.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("appVersionForm", editUrl, function(data) {
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
		EasyUI.form.submit("appVersionForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}

//删除PDA终端版本
var doDelete = function(){
	var r=EasyUI.grid.getSelections("dg");
	if(r.length == 0){
		Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
		return;
	}
	
	var ids=[];
	for(var i=0;i<r.length;i++){
		if(r[i].ISLATEST==1){
			Tip.warn("最新版本无法删除");
			return;
		}
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

function latestFormatter(value,row,index){
	return value==1?"新版本":"旧版本";
}

function urlFormatter(value,row,index){
	return "<a target='_blank' href='"+path+value+"'>"+path+value+"</a>";
}

</script>