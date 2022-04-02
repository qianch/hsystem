
<!--
      作者:徐波
      日期:2016-11-26 14:44:04
      页面:综合统计JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
var exportUrl = path + "shopStatistics/export";
//查询
function filter() {
      EasyUI.grid.search("dg", "totalStatisticsSearchForm");
}

$(function(){

      $('#dg').datagrid({
            url:"${path}shopStatistics/shopSummarylist",
            onBeforeLoad: dgOnBeforeLoad,
      });
});

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
});
/**
 * 行统计
 */
function onLoadSuccess(data){
	appendRow();
}
/**
 * 表格末尾追加统计行
 */
function appendRow(){
	$("#dg").datagrid('appendRow', {
		FACTORYPRODUCTNAME: '<span class="subtotal" style=" font-weight: bold;">小计</span>',
		PRODUCTWEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("PRODUCTWEIGHT") + '</span>',//托
		TNUM: '<span class="subtotal" style=" font-weight: bold;">' + compute("TNUM") + '</span>'//重量
	});
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
	 total = total.toFixed(2);
     return total;
} 

/**
 * Excel导出
 */
function exportExcel(){
	location.href= encodeURI(exportUrl + "?"+JQ.getFormAsString("totalStatisticsSearchForm"));
}

function stockStateFormatter(value, row, index) {
    /* var firstString=row.ROLLBARCODE+"";
    firstString=firstString.substring(0, 1);
    if(firstString!="T"){
          return "";
    } */
       if (value == null) {
             return "未入库";
       }
       if (value == 1) {
             return "在库";
       } else if (value == -1) {
             return "出库";
       } else if (value == 0) {
             return "未入库";
       }
}
</script>
