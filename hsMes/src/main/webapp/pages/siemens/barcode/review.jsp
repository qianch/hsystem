<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>条码追溯</title>
    <script type="text/javascript" src="resources/jquery/jquery-1.12.4.min.js"></script>
    <style type="text/css">
			body,
			html {
				margin: 0;
				background: #cecece;
			}
			
			.items {
				height: 200px;
				table-layout: fixed;
				border-spacing: 0;
				border-collapse: 0;
				margin:0 auto;
				margin-top:30px;
			}
			
			.item {
				border: 1px dashed gray;
				width: 300px;
				height: 250px;
				background: #f9f9f9;
				text-align: center;
			}
			.item table{
				width:100%;
			}
			
			.title {
				font-weight: bold;
			}
			
			.arrow-right {
				font-size: 60px;
				color: gray;
			}
			ul {
				padding: 0;
				margin: 0;
			}
			
			.icon {
				display: inline-block;
				height: 48px;
				width: 48px;
			}
			
			.icon-farbic {
				background: url(resources/images/zs-farbic.png);
				background-size: 100% 100%;
			}
			
			.icon-suit {
				background: url(resources/images/zs-suit.png);
				background-size: 100% 100%;
			}
			
			.icon-device {
				background: url(resources/images/zs-device.png);
				background-size: 100% 100%;
			}
			
			.icon-barcode {
				background: url(resources/images/zs-barcode.png);
				background-size: 100% 100%;
			}
			.iconTr{
				text-align: center;
			}
			.header{
				background: #f9f9f9;
				text-align: center;
			}
			.header td{
				border: 1px dashed gray;
				background: #f9f9f9;
				text-align: center;
				vertical-align: top;
				border-bottom: none;
			}
			.header-title {
				background: #f9f9f9;
				text-align: center;
				font-weight: bold;
				font-size: 20px;
			}
			.header-title td{
				border: 1px dashed gray;
				border-top: none;
				border-bottom: none;
				background: #f9f9f9;
				text-align: center;
				vertical-align: top;
			}
			
			ul{
				list-style: none;
				margin: 0;
				padding: 0;
				list-style-position: inside;
			}
			li{
				margin: 0;
				padding: 0;
			}
			.input{
				width:550px;
				margin: 0 auto;
				margin-top: 10px;
				height: 30px;
				/* padding: 2px; */
				/*background: white;*/
				border: 1px solid #00b189;
				display:flex;
				background:#00b189;
			}
			.input input{
				height: 100%;
				width:450px;
				border:none;
				outline: none;
				color:#08a967;
				text-align: center;
				font-size: 18px;
				font-weight: bold;
			}
			button{
				background: none;
				border: none;
				color:white;
				height: 30px;
				width:100px;
				font-size: 20px;
				font-weight: bold;
				outline: none;
			}
			button:hover{
				background: #08a07e;
			}
			/* .icon-search{
				display: inline-block;
				height: 48px;
				width: 48px;
				background-size:100% 100%;
				background: url(resources/images/search.png);
				float: right;
			}
			.icon-search:hover{
				border: 1px solid #423d3d;
			} */
			input::-webkit-input-placeholder{
			    color: #b0e0d3;
			    opacity:1;
			    font-weight: normal;
			}
		</style>
		<script type="text/javascript">
		
			function key(event){
				if(event.keyCode==13){
					review();
				}
			}
		
			function review(){
				
				$("#farbic").empty();
				$("#order").text("");
				$("#batch").text("");
				$("#suitUser").text("");
				$("#suitTime").text("");
				$("#device").text("");
				$("#code").text("");
				$("#partBarcode").text("");
				
				var barcode=$("#barcode").val();
				
				if(barcode==""){
					alert("条码号不能为空");
					return;
				}
				
				$.ajax({
					url:"siemens/fragmentBarcode/review",
					data:{barcode:barcode},
					type:"post",
					dataType:"json",
					success:function(data){
						var farbics="";
						for(var i=0;i<data.farbic.length;i++){
							farbics+="<li>"+data.farbic[i]+"</li>";
						}
						$("#farbic").append(farbics);
						$("#order").text(data.order);
						$("#batch").text(data.batch);
						$("#suitTime").text(data.suitTime);
						$("#suitUser").text(data.suitUser);
						$("#device").text(data.device);
						$("#code").text(data.code);
						$("#partBarcode").text(data.partBarcode);
					},error:function(xhr,error){
						var json=JSON.parse(xhr.responseText);
						alert(json.error);
						$("#barcode").val("");
					}
				});
			}
		
		</script>
  </head>
  
  <body>
  		<div class="input">
			<input type="text" id="barcode" spellcheck="false" onkeyup="this.value=this.value.toUpperCase()" onkeydown="key(event)" maxlength="20" placeholder="请输入小部件条码号"/>
			<button onclick="review()">查询</button>
			<!-- <div onclick="review()" class="icon icon-search"></div> -->
		</div>
		<table class="items">
			<tr class="header">
				<td><div class="icon icon-farbic"></div></td>
				<td style="background: #cecece;border:none;"></td>
				<td><div class="icon icon-device"></div></td>
				<td style="background: #cecece;border:none;"></td>
				<td><div class="icon icon-barcode"></div></td>
				<td style="background: #cecece;border:none;"></td>
				<td><div class="icon icon-suit"></div></td>
			</tr>
			<tr class="header-title">
				<td>投料</td>
				<td style="background: #cecece;border:none;"></td>
				<td>机台</td>
				<td style="background: #cecece;border:none;"></td>
				<td>条码</td>
				<td style="background: #cecece;border:none;"></td>
				<td>组套</td>
			</tr>
			<tr>
				<td class="item">
					<ul id="farbic">
					</ul>
				</td>
				<td class="arrow-right">→</td>
				<td class="item" style="width:100px;">
					<ul>
						<li id="device"></li>
					</ul>
				</td>
				<td class="arrow-right">→</td>
				<td class="item" style="width:200px;">
					<ul >
						<li style="font-weight:bold">条码号</li>
						<li id="code"></li>
						<li style="font-weight:bold">订单号</li>
						<li id="order"></li>
						<li style="font-weight:bold">批次号</li>
						<li id="batch"></li>
					</ul>
				</td>
				<td class="arrow-right">→</td>
				<td class="item" style="width:180px;">
					<ul>
						<li id="suitUser"></li>
						<li id="suitTime"></li>
						<li id="partBarcode" style="color:red;"></li>
					</ul>
				</td>
			</tr>
		</table>
  </body>
</html>
