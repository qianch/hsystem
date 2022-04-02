<!--
	作者:king
	日期:2017-8-2 10:39:01
	页面:成品类别管理JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加成品类别管理
var addUrl=path+"product/category/add";
//编辑成品类别管理
var editUrl=path+"product/category/edit";


var dialogWidth=260,dialogHeight=235;

/**
 * 成品类别查询
 */
function filter() {
	EasyUI.grid.search("category_dg","finishedProductCategorySearchForm");
}

/**
 * 添加产品类别
 */
var add = function() {
	var wid = Dialog.open("添加产品类别", dialogWidth, dialogHeight,addUrl, [
		EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("finishedProductCategoryForm",addUrl, function(data){
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

/**
 * 编辑产品类别
 */
var edit = function(){
	var r=EasyUI.grid.getOnlyOneSelected("category_dg");
	var wid = Dialog.open("编辑产品类别", dialogWidth, dialogHeight, editUrl+"?id="+r.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("finishedProductCategoryForm", editUrl, function(data) {
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
		EasyUI.form.submit("finishedProductCategoryForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}

//删除成品类别管理
var doDelete = function(){
	var r=EasyUI.grid.getSelections("category_dg");
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
</script>