<!--
	作者:king
	日期:2017-8-2 10:39:01
	页面:间入库汇总报表（产品大类、订单号、批次号、厂内名称）
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript">
	
	var exportUrl = path + "shopReport/export1";
		
	Date.prototype.Format = function (fmt) { //author: meizz 
	    var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "h+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds(), //秒 
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	        "S": this.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	}
		
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
	var time2 = lastDay+" "+"12:00:00";
	$("#start").datetimebox("setValue",time2);
	// setTimeout( function(){
	// 	filter();
	// }, 1500 );
	$("#dg").datagrid({
		url:"${path}shopReport/sscList",
		onBeforeLoad : dgOnBeforeLoad
	})
});
	
/**
 * 查询
 */
function filter(){
	EasyUI.grid.search("dg","searchForm");
}

/**
 * Excel 导出
 */
function exportExcel(){
	//var a = JQ.getFormAsString("searchForm");
	//alert();
	location.href= encodeURI(exportUrl + "?"+JQ.getFormAsString("searchForm"));
}
	
/**
 * 行统计
 */
function onLoadSuccess(data){
	$("#dg").datagrid('appendRow', {
		CATEGORYNAME: '<span class="subtotal" style=" font-weight: bold;">合计</span>',
		TNUM: '<span class="subtotal" style=" font-weight: bold;">' + compute("TNUM") + '</span>',//托
		WEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("WEIGHT") + '</span>'//重量
	});
}
// /**
//  * 表格末尾追加统计行
//  */
// function appendRow(){
// 	$("#dg").datagrid('appendRow', {
// 		CATEGORYNAME: '<span class="subtotal" style=" font-weight: bold;">合计</span>',
// 		TNUM: '<span class="subtotal" style=" font-weight: bold;">' + compute("TNUM") + '</span>',//托
// 		WEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("WEIGHT") + '</span>'//重量
// 	});
// }
/**
 * 指定列求和
 */
function compute(colName){
	 var rows = $("#dg").datagrid("getRows");
	 var total = 0;
	 for (var i = 0; i < rows.length; i++) {
         total += parseFloat(rows[i][colName]);
     }
     return total.toFixed(2);
}
</script>