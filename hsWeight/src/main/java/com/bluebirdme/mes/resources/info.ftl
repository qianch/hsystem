<html>

	<head>
		<meta charset="UTF-8">
		<style>
			table{   
				border-collapse:collapse;   
				border:none;
			}   
			th{
				width:15%;
				text-align:right;
				border:1px solid gray;
				color:gray;
			}
			td{
				width:30%;
				text-align:left;
				border:1px solid gray;
				color:#096473;
				font-weight: bold;
			}
		</style>
	</head>

	<body>
		<table style="width:100%;" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<th style="padding:5px;">客户名称</th>
				<td style="padding:5px;">${consumer}</td>
				<th style="padding:5px; ">规格型号</th>
				<td style="padding:5px;">${model}</td>
			</tr>
			<tr>
				<th style="padding:5px; ">订单号</th>
				<td style="padding:5px;">${order}</td>
				<th style="padding:5px; ">客户产品名称</th>
				<td style="padding:5px;">${comsumerProductName}</td>
			</tr>
			<tr>
				<th style="padding:5px; ">批次号</th>
				<td style="padding:5px;">${batch}</td>
				<th style="padding:5px; ">厂内名称</th>
				<td style="padding:5px;">${factoryProductName}</td>
			</tr>
			<tr>
				<th style="padding:5px; ">交货日期</th>
				<td style="padding:5px;">${deliveryDate}</td>
				<th style="padding:5px; ">质量等级</th>
				<td style="padding:5px;">${grade}</td>
			</tr>
		</table>
	</body>

</html>