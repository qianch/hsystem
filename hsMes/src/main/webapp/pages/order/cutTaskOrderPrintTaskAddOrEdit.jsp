<!--
	作者:高飞
	日期:2019-4-15 18:44:42
	页面:西门子裁剪车间机台打印任务增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<style type="text/css">
	//CSS 代码
</style>
<script type="text/javascript">
	//JS代码
</script>
<div>
	<!--西门子裁剪车间机台打印任务表单-->
	<form id="cutTaskOrderPrintTaskForm" method="post" ajax="true" action="<%=basePath %>cutTaskOrderPrintTask/${empty cutTaskOrderPrintTask.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${cutTaskOrderPrintTask.id}" />
		
		<table width="100%">
				<tr>
					<td class="title">计算机IP:</td>
					<!--计算机IP-->
					<td>
						<input type="text" id="ip" name="ip" value="${cutTaskOrderPrintTask.ip}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">派工单号:</td>
					<!--派工单号-->
					<td>
						<input type="text" id="ctoCode" name="ctoCode" value="${cutTaskOrderPrintTask.ctoCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">图号:</td>
					<!--图号-->
					<td>
						<input type="text" id="drawingNo" name="drawingNo" value="${cutTaskOrderPrintTask.drawingNo}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">当前打印序号:</td>
					<!--当前打印序号-->
					<td>
						<input type="text" id="currentPrintOrder" name="currentPrintOrder" value="${cutTaskOrderPrintTask.currentPrintOrder}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">层数:</td>
					<!--层数-->
					<td>
						<input type="text" id="leveCount" name="leveCount" value="${cutTaskOrderPrintTask.leveCount}" class="easyui-textbox"  >
					</td>
				</tr>
			
		</table>
	</form>
</div>