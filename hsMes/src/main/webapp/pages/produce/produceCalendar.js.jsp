<!--
	作者:高飞
	日期:2016-11-1 13:05:53
	页面:排产日历JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加排产日历
var addUrl=path+"produce/calendar/add";
//编辑排产日历
var editUrl=path+"produce/calendar/edit";
//删除排产日历
var deleteUrl=path+"produce/calendar/delete";

var dialogWidth=325,dialogHeight=188;

var wid;

$(document).ready(function(){
	
	var height = $(document).height();
	$('#calendar').fullCalendar({
		header : {
			left : '',
			center : 'title',
			right : 'prev,next today'
		},
		height:height - 30,
		events: path+'produce/calendar/list',
		eventClick: function(calEvent, jsEvent, view) {

	       /*  alert('Event: ' + calEvent.title);
	        alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
	        alert('View: ' + view.name); */
	        // change the border color just for fun
	        edit(calEvent.title.substring(0,calEvent.title.indexOf("(")));
	    }
	});
	
});

//查询
function filter() {
	$('#calendar').fullCalendar('refetchEvents');
}

//添加排产日历
var add = function() {
	wid = Dialog.open("添加", dialogWidth, dialogHeight,addUrl, [
		EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("produceCalendarForm",addUrl, function(data){
			if(Tip.hasError(data)){
				return;
			}
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

//编辑排产日历
var edit = function(code){
	wid = Dialog.open("编辑", dialogWidth, dialogHeight, editUrl+"?salesOrderCode="+code, [ 
	    EasyUI.window.button("icon-save", "保存", function() {
		EasyUI.form.submit("produceCalendarForm", editUrl, function(data) {
			if(Tip.hasError(data))return;
			Dialog.close(wid);
			filter();
		})
	}), EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(wid)
	}),EasyUI.window.button("icon-remove","删除",function(){
		doDelete(code);
	}) ]);
}


//删除排产日历
var doDelete = function(code){
	Dialog.confirm(function(){
		JQ.ajax(deleteUrl, "post", {
			salesOrderCode:code
		}, function(data){
			filter();
			Dialog.close(wid);
		});
	});
}

var salesOrderSelectorId;

function selectSalesOrder(){
	salesOrderSelectorId=Dialog.open("选择订单", 900,500, path+"selector/salesOrder?singleSelect=true", [EasyUI.window.button("icon-ok", "选择", function(){
		var row=$("#_common_salesOrder_dg").datagrid("getChecked");
		if(row.length==0){
			Tip.warn("请选择订单");
			return;
		}
		$("#salesOrderCode").searchbox("setValue",row[0].SALESORDERCODE);
		Dialog.close(salesOrderSelectorId);
	}),EasyUI.window.button("icon-cancel", "关闭", function(){
		Dialog.close(salesOrderSelectorId);
	})], function(){
	}, function(){Dialog.close(salesOrderSelectorId)});
}

function _common_salesOrder_onLoadSuccess(data){
	$("#_common_salesOrder_dg").datagrid("selectRecord",$("#salesOrderCode").searchbox("getValue"));
}

function _common_salesOrder_dbClickRow(index,row){
	$("#salesOrderCode").searchbox("setValue",row.SALESORDERCODE);
	Dialog.close(salesOrderSelectorId);
}


</script>