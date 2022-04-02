<!--
	作者:高飞
	日期:2016-10-12 10:34:41
	页面: 质量等级JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加 质量等级
var addUrl=path+"qualityGrade/add";
//编辑 质量等级
var editUrl=path+"qualityGrade/edit";
//删除 质量等级
var deleteUrl=path+"qualityGrade/delete";

//查询
function filter() {
	EasyUI.grid.search("dg","qualityGradeSearchForm");
}

//添加 质量等级
var add = function() {
	var wid = Dialog.open("添加", 580, 450,addUrl, [
		EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("qualityGradeForm",addUrl, function(data){
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

//编辑 质量等级
var edit = function(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	var wid = Dialog.open("编辑", 580, 450, editUrl+"?id="+r.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("qualityGradeForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}

//删除 质量等级
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

/**
 * 双击行，弹出编辑
 */
var dbClickEdit=function(index,r){
	var wid = Dialog.open("编辑", 580, 450, editUrl+"?id="+r.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("qualityGradeForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}

var memoFormatter=function(value,row,index){
	if(value==null){
		return "";
	}else{
		return "<a href='#' title='"+value+"' class='easyui-tooltip'>"+value+"</a>"
	}
	
}
</script>