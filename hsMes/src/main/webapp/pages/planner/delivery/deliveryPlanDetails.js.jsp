<!--
	作者:徐波
	日期:2016-11-2 9:30:07
	页面:出货详情列表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加出货详情列表
var addUrl_product=path+"planner/deliveryPlanDetails/add";
//编辑出货详情列表
var editUrl_product=path+"planner/deliveryPlanDetails/edit";
//删除出货详情列表
var deleteUrl_product=path+"planner/deliveryPlanDetails/delete";
//添加出货详情列表
var add_product = function() {
	var wid = Dialog.open("添加", dialogWidth, dialogHeight,addUrl_product, [
		EasyUI.window.button("icon-save", "保存", function() {
 		
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ],function(){Dialog.more(wid);}
	);
}

//编辑出货详情列表
var edit_product = function(){
	var r=EasyUI.grid.getOnlyOneSelected("dg_product");
	var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl_product+"?id="+r.ID, [ 
	    EasyUI.window.button("icon-save", "修改", function() {
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}
//删除出货详情列表
var doDelete_product = function(){
	var r=EasyUI.grid.getSelections("dg_product");
	if(r.length == 0){
		Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
		return;
	}
		
	
}
var dialogWidth=700,dialogHeight=350;

//查询
function filter() {
	EasyUI.grid.search("dg","deliveryPlanDetailsSearchForm");
}


/**
 * 双击行，弹出编辑
 */
var dbClickEdit=function(index,row){
	var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl+"?id="+row.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("deliveryPlanDetailsForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}


</script>