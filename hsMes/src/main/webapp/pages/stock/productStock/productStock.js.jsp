<!--
	作者:徐波
	日期:2016-10-24 15:08:20
	页面:成品库存表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>

var total=0;

$(function(){
	$("#dg").datagrid({
		url:"${path}stock/productStock/list",
		onBeforeLoad:dgOnBeforeLoad,
	})
})

function setTotal(param){
	param.totalRows=total;
}

function onLoadSuccess(data){
	total=data.total;
}

//查询
function filter() {
	total=0;
	EasyUI.grid.search("dg","productStockSearchForm");
}

function formatterStockState(value,row){
	if(value==1){
		return "入库";
	}else{
		return "出库";
	}
	
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
//获取某年某月有多少天
var myDate = new Date(year, month, 0);
$(document).ready(function(){
var firstDay = year + "-" + month + "-" + "01";//上个月的第一天
var lastDay = year + "-" + month + "-" + myDate.getDate();//上个月的最后一天
var time2 = lastDay+" "+"00:00:00";
$("#start").datetimebox("setValue",time2);
$("#stockState").combobox("setValue",1);
$("#stockType").combobox("setValue",0);
});
//冻结状态
function lockStateFormatter(value, row, index) {
    if (value == 1) {
          return "冻结";
    } else {
          return "正常";
    }
}
function exportDetail(){
	location.href= encodeURI(path +  "stock/productStock/productStockExport?"+JQ.getFormAsString("productStockSearchForm"));
}

function formatterState(value,row) {
	if (value == 1) {
		return "在库";
	} else if (value == -1) {
		return "不在库";
	} else if (value == 2) {
		return "待入库";
	} else if (value == 0) {
		return "未入库";
	} else if (value == 3) {
		return "在途";
	}
	else {
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

function typeFormatter(value,row,index){
	if(!isEmpty(value)){
		if(value.indexOf("cp")==0){
			return "成品";
		}else if(value.indexOf("bz")==0){
			return "编织";
		}else if(value.indexOf("cj")==0){
			return "裁剪";
		}else{
			return "";
		}
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