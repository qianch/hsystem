<!--
	作者:孙利
	日期:2017-7-10 8:44:34
	页面:称重载具JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加称重载具
var addUrl=path+"weightCarrier/add";
//编辑称重载具
var editUrl=path+"weightCarrier/edit";
//删除称重载具
var deleteUrl=path+"weightCarrier/delete";
//导出
//var exportUrl=path+"weightCarrier/export";

var dialogWidth=380,dialogHeight=401.4;

//查询
function filter() {
	EasyUI.grid.search("weight_dg","weightCarrierSearchForm");
}


//添加称重载具
var add = function() {
	var wid = Dialog.open("添加", dialogWidth, dialogHeight,addUrl, [
		EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("weightCarrierForm",addUrl, function(data){
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

//编辑称重载具
var edit = function(){
	var r=EasyUI.grid.getOnlyOneSelected("weight_dg");
	var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl+"?id="+r.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("weightCarrierForm", editUrl, function(data) {
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
		EasyUI.form.submit("weightCarrierForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}

//删除称重载具
var doDelete = function(){
	var r=EasyUI.grid.getSelections("weight_dg");
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

//导出
/* function exportExcel() {
	var r=EasyUI.grid.getSelections("dg");
	var ids=[];
	for(var i=0;i<r.length;i++){
		ids.push(r[i].ID);
	}
	window.open(exportUrl + "?ids=" + ids);} */
</script>