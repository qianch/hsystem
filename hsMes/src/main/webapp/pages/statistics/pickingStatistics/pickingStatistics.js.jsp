<!--
	作者:肖文彬
	日期:2016-11-16 12:33:40
	页面:出库单明细JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
var exportUrl = path + "shopStatistics/export2";
//获取上月最后一天或第一天
var nowdays = new Date();
var year = nowdays.getFullYear();
var month = nowdays.getMonth();
if(month==0)
{
    month=12;
    year=year-1;
}
if (month < 10) {
    month = "0" + month;
}
var myDate = new Date(year, month, 0);
$(document).ready(function(){
	var firstDay = year + "-" + month + "-" + "01";//上个月的第一天
	var lastDay = year + "-" + month + "-" + myDate.getDate();//上个月的最后一天
	var time2 = lastDay+" "+"08:00:00";
	$("#start").datetimebox("setValue",time2);
	filter();
	/* setTimeout( function(){
		filter();
	}, 1500 ); */
});

//查询
function filter() {
	EasyUI.grid.search("dg","materialOutOrderDetailSearchForm");
}

$(function(){

	$('#dg').datagrid({
		url:"${path}shopStatistics/pickingStatisticsList",
		onBeforeLoad: dgOnBeforeLoad,
	});
});


/**
 * 行统计
 */
var flg = true
function onLoadSuccess(data){
	if(flg){
		appendRow();
	}
	flg = true;
}
/**
 * 表格末尾追加统计行
 */
function appendRow(){
	$("#dg").datagrid('appendRow', {
		PRODUCECATEGORY: '<span class="subtotal" style=" font-weight: bold;">小计</span>',
		OUTWEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("OUTWEIGHT") + '</span>',//重量
	});
	flg = false;
}
/**
 * 指定列求和
 */
function compute(colName){
	 var rows = $("#dg").datagrid("getRows");
	 var total = 0;
	 for (var i = 0; i < rows.length; i++) {
         total += parseFloat(rows[i][colName]);
     }
     return total;
}

function outTimeFormatter(value, row, index){
    if(!value)return "";
    return new Calendar(value).format("yyyy-MM-dd");
}

/**
 * Excel 导出
 */
function exportExcel(){
	location.href= encodeURI(exportUrl + "?"+JQ.getFormAsString("materialOutOrderDetailSearchForm"));
}
</script>