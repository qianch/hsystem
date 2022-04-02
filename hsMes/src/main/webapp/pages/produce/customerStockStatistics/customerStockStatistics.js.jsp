<!--
	作者:king
	日期:2017-8-2 10:39:01
	页面:成品类别管理JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript">
	
	var exportUrl = path + "productsReport/export3";
		
	//获取上月最后一天或第一天
  	var nowdays = new Date();
	var year = nowdays.getFullYear();
	var month = nowdays.getMonth();
	var day = nowdays.getDate();
	var hours = nowdays.getHours(); //获取系统时，
	var minutes = nowdays.getMinutes(); //分
	var seconds = nowdays.getSeconds(); //秒
	var myDate = new Date(year, month, 0);
	$(document).ready(function(){
		var time2 = year + month + day+" "+hours+":"+minutes+":"+seconds;
		$("#end").datetimebox("setValue",time2);
});
	$(function(){

		$('#dg').datagrid({
			url:"${path}productsReport/pcsslist",
			onBeforeLoad: dgOnBeforeLoad,
		});
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
function appendRow(){
	$("#dg").datagrid('appendRow', {
		FACTORYPRODUCTNAME: '<span class="subtotal" style=" font-weight: bold;">小计</span>',
		WEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + compute("WEIGHT") + '</span>'//重量
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
	 console.log(total);
     return total;
}

/**
 * Excel 导出
 */
function exportExcel(){
	location.href=  encodeURI(exportUrl + "?"+JQ.getFormAsString("productStockSearchForm")) ;
}
	
//查询
function filter() {
	EasyUI.grid.search("dg","productStockSearchForm");
}

function formatterStockState(value,row){
	if(value==1){
		return "入库";
	}else{
		return "出库";
	}
	
}


function formatterState(value,row){
	if(value==1){
		return "在库";
	}else if(value==-1){
		return "不在库";
	}else{
		return "";
	}
}

//超期
function rowStyler(index, row) {
	var style="";
	if(row.PRODUCTSHELFLIFE<inDays(null,row)){
		style+='color:red';
	}
	if(!isEmpty(row.NEWBATCHCODE)){
		style+='background:#f9ff00;';
	}
	return style;
}
function inDays(value,row){
	if(isEmpty(row.INTIME))return "";
	var intimes=row.INTIME+"";
	var begintime_ms =new Date(intimes.replace(/-/g, "/"));
	var nowtimes=new Date();
	var millions=nowtimes.getTime()-begintime_ms.getTime();
	var days=Math.floor(millions/(24*3600*1000))
	return days;
}

function deliveryFormatter(value,row,index){
	if(!isEmpty(row.PLANDELIVERYDATE)){
		return row.PLANDELIVERYDATE.substring(0,10);
	}
	return row.DELIVERYDATE;
}

function orderFormatter(value,row,index){
	if(!isEmpty(row.NEWSALESORDERCODE)){
		return row.NEWSALESORDERCODE;
	}
	return value;
}

function warehouseFormatter(value,row,index){
	if(!isEmpty(value)){
		return value.toUpperCase();
	}
}

function batchFormatter(value,row,index){
	if(!isEmpty(row.NEWBATCHCODE)){
		return row.NEWBATCHCODE;
	}
	return value;
}

function modelFormatter(value,row,index){
	if(!isEmpty(row.NEWPRODUCTMODEL)){
		return row.NEWPRODUCTMODEL;
	}
	return value;
}

function consumerFormatter(value,row,index){
	if(!isEmpty(row.NEWCONSUMER)){
		return row.NEWCONSUMER;
	}
	return value;
}

</script>
