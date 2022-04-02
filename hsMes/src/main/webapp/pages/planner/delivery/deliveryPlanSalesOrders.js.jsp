<!--
	作者:徐波
	日期:2016-11-2 9:30:08
	页面:出货订单关联JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加出货订单关联
var addUrl_order=path+"planner/deliveryPlanSalesOrders/add";
//编辑出货订单关联
var editUrl_order=path+"planner/deliveryPlanSalesOrders/edit";
//删除出货订单关联
var deleteUrl_order=path+"planner/deliveryPlanSalesOrders/delete";
//添加出货订单关联
var add_order = function() {
	var wid = Dialog.open("添加", dialogWidth, dialogHeight,addUrl, [
		EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("deliveryPlanSalesOrdersForm",addUrl, function(data){
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
//编辑出货订单关联
var edit_order = function(){
	var r=EasyUI.grid.getOnlyOneSelected("dg_order");
	var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl+"?id="+r.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("deliveryPlanSalesOrdersForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}
//删除出货订单关联
var doDelete_order = function(){
	var r=EasyUI.grid.getSelections("dg_order");
	if(r.length == 0){
		Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
		return;
	}
		
	var ids=[];
	for(var i=0;i<r.length;i++){
		ids.push(r[i].ID);
	}
	Dialog.confirm(function(){
		JQ.ajax(deleteUrl_order, "post", {
			ids : ids.toString()
		}, function(data){
			filter();
		});
	});
}

var dialogWidth=700,dialogHeight=350;

//查询
function filter() {
	EasyUI.grid.search("dg","deliveryPlanSalesOrdersSearchForm");
}




/**
 * 双击行，弹出编辑
 */
var dbClickEdit=function(index,row){
	var wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl+"?id="+row.ID, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("deliveryPlanSalesOrdersForm", editUrl, function(data) {
			filter();
			Dialog.close(wid);
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}) ]);
}


</script>