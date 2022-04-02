<!--
	作者:徐波
	日期:2016-11-14 15:40:51
	页面:打印机信息JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>打印机信息</title>
<%@ include file="../base/meta.jsp"%>
<style type="text/css">
	.title{
		font-size:30px;
		width:200px;
	}
	
	td{
		padding:5px;
	}
	
	input{
		    width: 300px;
		    height: 35px;
		    font-size: 25px;
		    color: #0c967e;
		    border: none;
		    border-bottom: 1px solid #e2e2e2;
		    font-weight: bold;
		    padding: 4px;
		    outline:none;
	}
	
	button{
		background: #3c6508;
	    border: none;
	    color: white;
	    font-size: 25px;
	    height: 45px;
	    width: 300px;
	    font-weight: bold;
	    border-radius: 5px;
	}
	
	button:active{
		background:red;
	}
	
	select{
		height: 35px;
	    border: none;
	    border-bottom: 1px solid #e2e2e2;
	    font-size: 20px;
	    border: none;
	    border-bottom: 1px solid #e2e2e2;
	    font-weight: bold;
	    outline:none;
	    color:#0c967e;
	}
	
</style>

<script type="text/javascript">

	function getPrinter(node){
		
		if($(this).tree("getParent",node.target)!=null){
			$("#printer").html(node.text);
			$("#printer").attr("v",node.id);
		}else{
			$("#printer").html(null);
			$("#printer").attr("v","");
		}
	}
	
	function doPrint() {
		var printer = $("#printer").text();
		var printerId = $("#printer").attr("v");
		var workshop = $("#ws").val();
		var boxCount = $("#boxCount").val() == "" ? 0 : $("#boxCount").val();
		var trayCount = $("#trayCount").val() == "" ? 0 : $("#trayCount").val();
		var trayPartCount = $("#trayPartCount").val() == "" ? 0 : $("#trayPartCount").val();
        var copies = $("#copies").val() == "" ? 0 : $("#copies").val();
		if (isEmpty(printerId)) {
			Tip.warn("请选择打印机");
			return;
		}

		if (workshop == -1) {
			Tip.warn("请选择使用车间");
			return;
		}

		if (trayCount == 0 && boxCount == 0 && trayPartCount == 0) {
			Tip.warn("请填写托条码或者盒条码数量或者成品胚布托条码数量");
			return;
		}

		Dialog.confirm(function () {
			Loading.show("正在打印...");
			JQ.ajaxPost(
					path + "printer/printBarcodeFirst",
					{
						departmentType: workshop,
						pName: printerId,
						trayCount: trayCount,
						boxCount: boxCount,
						trayPartCount: trayPartCount,
                        copies:copies
					},
					function (data) {
						Loading.hide();
						if (Tip.hasError(data)) return;
						$("#printer").text("");
						$("#printer").attr("v", "");
						$("#ws").val("-1");
						$("#boxCount").val("");
						$("#trayCount").val("");
						$("#trayPartCount").val("");
                        $("#copies").val("");
						Tip.success("条码打印成功");
					}, function (data) {
						Loading.hide();
						Tip.error(data + "条码打印失败");
					}
			);

		}, "<font color='red'>" + $("#ws").find("option:selected").text() + "</font> 打印 <font color='red'>" + boxCount + "张盒条码</font>，<font color='red'>" + trayCount + "张托条码</font>，<font color='red'>" + trayPartCount + "张托(胚布)条码</font>，使用 <font color='red'>" + printer + "</font>,是否继续?");
	}
		function setWorkShop(){

			var workshopType=$("#wst").val();
				JQ.ajaxGet(
						path+"department/queryDepartmentByType?type="+workshopType,
						function(data){
							$('#ws').empty();
							console.log(data);
							for (var i = 0; i < data.length; i++) {
								var option = document.createElement("option");
								$(option).val(data[i].v);
								$(option).text(data[i].t);
								$('#ws').append(option);
							}

						}
				);

	}
</script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'west',border:true,split:true,title:'打印机列表',width:300">
		<ul class="easyui-tree" data-options="url:'${path }/print/printers',method:'get',animate:true,onClick:getPrinter,lines:true"></ul>
	</div>
	<div data-options="region:'center',border:true,title:'条码打印'" >
		<table width="100%">
			<tr>
				<td class="title" style="width: 270px;">当前打印机</td>
				<td id="printer" v="" style="color:red;font-size:20px;font-weight:bold;"><label style="font-size:12px;color:gray;font-weight: normal;">请选择左侧打印机</label></td>
			</tr>
			<tr>
				<td class="title">使用车间</td>
				<td>
					<select id="wst" onChange="setWorkShop()"><!-- 0:裁剪车间，1：编织1车间，2：编织2车间，3：编织3车间 -->
						<option value="-1">请选择车间类型</option>
						<optgroup label="编织车间">
							<option value="weave">编织车间</option>
						</optgroup>
						<optgroup label="裁剪车间">
							<option value="cut">裁剪车间</option>
						</optgroup>
					</select>
				</td>
			</tr>
			<tr>
				<td class="title">使用车间</td>
				<td>
					<select id="ws"><!-- 0:裁剪车间，1：编织1车间，2：编织2车间，3：编织3车间 -->
						<option value="-1">请选择车间</option>
						</optgroup>
					</select>
				</td>
			</tr>
			<tr>
				<td class="title">盒条码数量</td>
				<td>
					<input id="boxCount" maxlength="3"  type="text" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" placeholder="请输入盒条码数量"/>盒
				</td>
			</tr>
			<tr>
				<td class="title">托条码数量</td>
				<td>
					<input id="trayCount" maxlength="3"  type="text" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" placeholder="请输入托条码数量"/>托
				</td>
			</tr>
			<tr>
				<td class="title">成品胚布托条码数量</td>
				<td>
					<input style="width: auto;" id="trayPartCount" maxlength="3"  type="text" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" placeholder="请输入成品胚布托条码数量"/>托
				</td>
			</tr>
			<tr>
				<td class="title">每托条码份数</td>
				<td>
					<input style="width: auto;" id="copies" maxlength="3"  type="text" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" placeholder="请输入每托条码需打印的份数"/>份
				</td>
			</tr>
			<tr>
				<td class="title"></td>
				<td>
					<button onclick="doPrint()">打印</button>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>