<!--
	作者:宋黎明
	日期:2016-9-29 11:46:46
	页面:设备机台显示JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>设备机台显示</title>
  	<%@ include file="../base/meta.jsp" %>
<script type="text/javascript" src="../resources/js/template.js"></script>
  	<style type="text/css">
@font-face { 
  font-family: "宋体"; 
  src: url(../resources/themes/粗体.otf);
}

::-webkit-scrollbar {
	width: 0;
}

html,body {
	margin: 0;
	font-family: 宋体;
}

table {
	width: 100%;
	table-layout: fixed;
}

th,td {
	font-weight: bolder;
	text-align: center;
	word-wrap: break-word;
}
hr{
	padding:0px;
	margin:0;
}
th{
	font-size: 30px;
	color:rgb(199, 0, 0);
	text-shadow: 1px 1px #c7c7c7,2px 2px #c7c7c7,3px 3px #c7c7c7,4px 4px #c7c7c7;
	
}
td{
	font-size:30px;
}
.jt{
	text-align: center;
	font-size: 60px;
	font-weight: bold;
	font-family: fantasy;
	color: #000;
    text-shadow: 1px 1px #c7c7c7,2px 2px #c7c7c7,3px 3px #c7c7c7,4px 4px #c7c7c7;
}
.producing{
	background:green!important;
	color:white;
}
.prior{
	color:rgb(255, 133, 0)!important;
}
.odd{
	background:rgba(237, 226, 249, 0.95);
}
</style>
<script type="text/javascript">

      var   onChange= function (newValue, oldValue) {
        	 var jtID = $("#ip").combobox('getValue');
             var url = path + "jt/viewComp?jtId="+jtID;
             console.log(url);
             $.ajax({
     			url:url,
     			type:"get",
     			dataType:"json",
     			success:function(data){
     				console.log(data);
     				template.helper('code', function (content) {
     					if(content){
     						return content.substring(content.lastIndexOf("/")+1);
     					}
     				});
     				template.helper('prior', function (PRIOR) {
     					if(PRIOR){
     						return "prior";
     					}
     				});
     				template.helper('colorful', function (i) {
     					if(i%2==0){
     						return "odd";
     					}
     				});
     				template.helper('producing', function (ISPRODUCING) {
     					if(ISPRODUCING==1){
     						return "producing";
     					}
     				});
     				template.helper('moreCount',function(row){
    					var c=row.REQUIREMENTCOUNT-row.RC;
    					return c<0?0:c;
    				});
     				
     				var html = template('orders', {
     					"data": data
     					
     				});
     				$(".tb_header tr[data-row='true']").remove();
     				$(".tb_header").append(html);
     				setTimeout(function(){
     					//getData();
     				}, 3000);
     			 }
             });
        }

</script>
</head>

 <body>
<div id="toolbar" class="datagrid-toolbar">
	&nbsp; 监控机台屏幕&nbsp;
	<input id="ip" class="easyui-combobox"  editable="false" prompt="请选择机台屏幕"  url="${path}screen/combobox?all=1" data-options="
                  valueField: 'id',
                  panelMaxHeight:200,
                  textField: 'text',
                  labelPosition: 'top',
                 onChange: onChange
                  ">
</div>
	<table id="deviceShow" class="tb_header" border="1" cellspacing="0" cellpadding="0" bordercolor="gray" style="BORDER-COLLAPSE: collapse">
		<tr>
					<th style="width:10%;">序号</th>
					<th style="width:35%;">客户产品<hr>厂内名称</th>
					<th style="width:30%;">生产工艺<hr>包装工艺</th>
					<th style="width:20%;">客户订单号<hr>批次号</th>
					<th style="width:15%;">未完成<hr>已完成<hr>订单总量</th>
					<th style="width:15%;">本机完成</th>
				</tr>
	</table>
	
	<script id="orders" type="text/template">
		{{each data as value i}}
				<tr data-row="true" class="{{colorful(i)}} {{producing(value.ISPRODUCING)}} {{prior(value.SORT)}}">
					<td>{{i+1}}</td>
					<td>{{value.CONSUMERPRODUCTNAME}}<hr>{{value.PRODUCTNAME}}</td>
					<td>{{code(value.PROCESSBOMCODE)}}({{value.PROCESSBOMVERSION}})<hr>{{code(value.PRODUCTPACKAGINGCODE)}}({{value.PRODUCTPACKAGEVERSION}})</td>
					<td>{{value.SALESORDERCODE}}<hr>{{value.BATCHCODE}}</td>
					<td>{{moreCount(value)}}<hr>{{value.RC}}<hr>{{value.REQUIREMENTCOUNT}}</td>
					<td>{{value.THISDEVICECOUNT}}</td>
				</tr>
		{{/each}}
	</script>

 </body>