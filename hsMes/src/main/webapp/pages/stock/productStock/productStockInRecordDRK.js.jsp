<!--
	作者:徐波
	日期:2016-10-24 15:08:20
	页面:成品库存表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//查询
function filter() {
	EasyUI.grid.search("dg","productInRecordSearchForm");
}

$(function(){
	$("#dg").datagrid({
		url:"${path}stock/productInRecord/drkList",
		onBeforeLoad:dgOnBeforeLoad,
	})
})




//获取上月最后一天或第一天
var nowdays = new Date();
var year = nowdays.getFullYear();
var month = nowdays.getMonth();
nowdays.setTime(nowdays.getTime()-24*60*60*1000);


if(month==0)
{
	month=12;
	year=year-1;
}
if (month < 10) {
	month = "0" + month;
}
// 获取某年某月有多少天
var myDate = new Date(year, month, 0);
$(document).ready(function(){
    var s1 = nowdays.getFullYear()+"-" + (nowdays.getMonth()+1) + "-" + nowdays.getDate();
	var firstDay = year + "-" + month + "-" + "01";//上个月的第一天
	var lastDay = year + "-" + month + "-" + myDate.getDate();//上个月的最后一天
	var time2 = s1+" "+"08:00:00";
	$("#start").datetimebox("setValue",time2);
	$("#stockType").combobox("setValue",0);
});

/**
 * 行统计
 */
var flg = true;
function onLoadSuccess(data){
	if(flg){
		appendRow();
	}
	flg = true;
}
/**
 * 表格末尾追加统计行
 */
var flg = true;
function appendRow(data){
	if(flg){
		appendRow();
	}
	flg = true;
}
function appendRow(){
	$("#dg").datagrid('appendRow', {
		CATEGORYCODE: '<span class="subtotal" style=" font-weight: bold;">合计</span>',
		WEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + computes("WEIGHT") + '</span>'	//月末累计数量统计
	});
	flg = false;
}
/**
 * 指定列求和
 */
function computes(colName){
	 var rows = $("#dg").datagrid("getRows");
	 var totals = 0;
	 for (var i = 0; i < rows.length; i++) {
         totals += parseFloat(rows[i][colName]);
     }
     return totals.toFixed(2);
}

//var exportUrl = path + "stock/productInRecord/export?";
function exportExcel() {
	//alert(JQ.getFormAsJson("productInRecordSearchForm"));
	location.href= encodeURI(path +  "stock/productInRecord/export1?"+JQ.getFormAsString("productInRecordSearchForm"));
	//location.href=path + "stock/productInRecord/export1";//+JQ.getFormAsString("productInRecordSearchForm");
		/* var orders = JQ.getFormAsJson("productInRecordSearchForm");
		log(orders)
			$.ajax({
				url : path + "stock/productInRecord/export1",
				type : 'post',
				dataType : 'json',
				data : orders,
				success : function() {
					log(111)
				}
			}); */
	}
</script>