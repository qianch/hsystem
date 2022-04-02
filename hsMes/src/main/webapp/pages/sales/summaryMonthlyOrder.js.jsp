<!--
	作者:谢辉
	日期:2017-06-08 16:54:20
	页面:月度订单产品汇总JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//查询
function filter() {
	EasyUI.grid.search("dg","summaryMonthlyOrderFrom");
}

$(function() {
	var cal = new Calendar();

	cal.add(Calendar.field.MONTH, -1);
	cal.set(Calendar.field.DAY_OF_MONTH, 1);

	$("#start").datebox("setValue", cal.format("yyyy-MM-dd"));
	$("#outstart").datebox("setValue", cal.format("yyyy-MM-dd"));

	cal.add(Calendar.field.MONTH, 1);
	cal.add(Calendar.field.DAY_OF_MONTH, -1);

	$("#end").datebox("setValue", cal.format("yyyy-MM-dd"));
	$("#outend").datebox("setValue", cal.format("yyyy-MM-dd"));

	filter();
});


function export1(){
	//var order = JQ.getFormAsJson("summaryMonthlyOrderFrom");
	//alert(JQ.getFormAsString("summaryMonthlyOrderFrom"));
	location.href= encodeURI(path+"salesOrder/export3?"+JQ.getFormAsString("summaryMonthlyOrderFrom"));
}

function plantotalweightFormatter(value, row, inde){
	if (value == undefined)
		return null;
	return value.toFixed(2);
}
function wweightFormatter(value, row, inde){
	if (value == undefined)
		return null;
	return value.toFixed(2);
}
function completedamountFormatter(value, row, inde){
	if (value == undefined)
		return null;
	return value.toFixed(2);
}function stockoutweightFormatter(value, row, inde){
	if (value == undefined)
		return null;
	return value.toFixed(2);
}
function PRFormatter(value, row, inde){
	value = row.RW+row.PW;
	return value.toFixed(2);
}

function rowStyler(index, row) {
	var style="";
	return style;
}




</script>