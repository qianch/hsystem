<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>编织车间电视机页面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<script type="text/javascript" src="resources/jquery/jquery-1.8.0.js"></script>
<style type="text/css">
			@font-face { 
			  font-family: "宋体"; 
			  src: url(resources/themes/粗体.otf); 
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
				table-layout:fixed;
			}
			
			th,
			td {
				font-weight: bolder;
				text-align: center;
				word-wrap:break-word;
			}
			
			th {
				background: rgb(11, 109, 60);
    			color: #ffffff;
			}
			
			td {
				color: #2dde23;
				background: black;
				font-size: 15px;
			}
			
			.tb_header {
				width: 100%;
				position: fixed;
				left: 0;
				top: 0;
				background: #ececec;
				font-size: 20px;
			}
			tr{height:75px;}
			
			.tb_body {
				margin-top: 55px;
			}
			.odd{}
			.odd td{
				background:#1b1b1b;
				color:#3bdcce;
			}
</style>
</head>

<body>
	<table class="tb_header" border="1" cellspacing="0" cellpadding="0" bordercolor="#333333" style="BORDER-COLLAPSE: collapse;">
		<tr style="height:55px;">
			<th width="6%">机台号</th>
			<th width="14%">订单号</th>
			<th width="10%">批次号</th>
			<th width="20%">任务单号</th>
			<th width="16%">产品信息</th>
			<th width="6%">订单卷数</th>
			<th width="6%">产出卷数</th>
			<th width="6%">完成率</th>
			<th width="6%">已打托数</th>
			<th width="8%">交货日期</th>
			<th width="8%">客户名称</th>
		</tr>
	</table>
	<table class="tb_body" border="1" cellspacing="0" cellpadding="0" bordercolor="#333333" style="BORDER-COLLAPSE: collapse">
		
	</table>
</body>
<script type="text/javascript">
		var stay=100;
		var i=0;
		!function getData(){
			$.ajax({
				url:"tv/bz${no}",
				type:"post",
				dataType:"json",
				success:function(data){
					var table="";
					for(var i=0;i<data.length;i++){//<td>"+data[i].PACKAGEDCOUNT+"</td>
						var precent=0;
						//(((data[i].RC/data[i].TOTALROLLCOUNT)*100)+"").substring(0,4)
						if(data[i].RC==null)
							precent=0;
						if(data[i].TOTALROLLCOUNT==null||data[i].TOTALROLLCOUNT==0)
							precent=100;
						if(data[i].RC!=null&&data[i].TOTALROLLCOUNT!=null&&data[i].TOTALROLLCOUNT!=0)
							precent=(((data[i].RC/data[i].TOTALROLLCOUNT)*100)+"").substring(0,4);
						precent=precent>100?100:precent;
						var deviceCode=data[i].DEVICECODE;
						deviceCode=deviceCode==null?"":deviceCode;
						deviceCode=deviceCode.toUpperCase();
						table+="<tr class='"+(i%2==0?"odd":"")+"'><td width='6%'>"+deviceCode+"</td><td width='14%'>"+data[i].SALESORDERCODE+"</td><td width='10%'>"+data[i].BATCHCODE+"</td><td width='20%'>"+data[i].PLANCODE+"</td><td width='16%'>"+data[i].PRODUCTMODEL+"</td><td width='6%'>"+data[i].TOTALROLLCOUNT+"&nbsp;卷</td><td width='6%'>"+data[i].RC+"&nbsp;卷</td><td width='6%'>"+precent+"%</td><td width='6%'>"+data[i].TC+"&nbsp;托</td><td width='8%'>"+data[i].DELEVERYDATE.substring(0,10)+"</td><td width='8%'>"+data[i].CONSUMERSIMPLENAME+"</td></tr>";
					}
					$(".tb_body").empty();
					if(table==""){
						table="<tr><td colspan='8'>暂无数据</td></tr>"
					}
					$(".tb_body").append(table);
					setInterval(function() {
						i+=75;//每次移动75个像素
						$("body").animate({"scrollTop":i});
						if(i>=(document.body.scrollHeight-document.body.clientHeight+stay)){
							i=0-stay;
						}},3000);
				}
			});
		}();
</script>
</html>