<!--
	作者:king
	日期:2017-8-2 10:39:01
	页面:成品类别管理JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript">
	
	var exportUrl = path + "productsReport/export";
	var total=0;
	function setTotal(param){
		param.totalRows=total;
	}
	
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
			url:"${path}productsReport/productsSummarylist",
			onBeforedLoad : dgOnBeforeLoad
		})
	});
	

/**
 * 查询
 */
function filter(){
	total=0;
	EasyUI.grid.search("dg","searchForm");
}

/**
 * Excel 导出
 */
function exportExcel(){
	//产品类别名称
	var name = $("#name").textbox("getValue");
	//产品类别代码
	var code = $("#code").textbox("getValue");
	//开始时间
	var start = $("#start").datetimebox("getValue");
	//结束时间
	var end = $("#end").datetimebox("getValue");
	location.href= encodeURI(exportUrl + "?filter[name]="+name+"&filter[code]="+code+"&filter[start]="+start+"&filter[end]="+end);
}
	
/**
 * 行统计
 */
var flg = true;
function onLoadSuccess(data){
	if(flg){
		appendRow();
	}
	flg = true;
	total=data.total;
}
/**
 * 表格末尾追加统计行
 */
function appendRow(){
	$("#dg").datagrid('appendRow', {
		CATEGORYNAME: '<span class="subtotal" style=" font-weight: bold;">合计</span>',
		STARTWEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("STARTWEIGHT") + '</span>',//月初库存数量统计
		INWEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("INWEIGHT") + '</span>',//当月入库数量统计
		USENUM: '<span class="subtotal" style=" font-weight: bold;">' + compute("USENUM") + '</span>',//当月领用数量统计
		OUTWEIGHTS: '<span class="subtotal" style=" font-weight: bold;">' + compute("OUTWEIGHTS") + '</span>',	//当月发出数量统计
		ATUM: '<span class="subtotal" style=" font-weight: bold;">' + compute("ATUM") + '</span>'	//月末累计数量统计
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
</script>