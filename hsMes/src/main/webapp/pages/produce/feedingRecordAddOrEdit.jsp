<!--
	作者:徐波
	日期:2017-8-3 9:06:25
	页面:投料记录增加或修改页面
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
	<!--投料记录表单-->
	<form id="feedingRecordForm" method="post" ajax="true" action="<%=basePath %>feedingRecord/${empty feedingRecord.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${feedingRecord.id}" />
		
		<table width="100%">
				<tr>
					<td class="title">机台ID:</td>
					<!--机台ID-->
					<td>
						<input type="text" id="deviceCode" name="deviceCode" value="${feedingRecord.deviceCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>操作人ID:</td>
					<!--操作人ID-->
					<td>
						<input type="text" id="operateUserId" name="operateUserId" value="${feedingRecord.operateUserId}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title">原料条码:</td>
					<!--原料条码-->
					<td>
						<input type="text" id="materialCode" name="materialCode" value="${feedingRecord.materialCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">卷条码:</td>
					<!--卷条码-->
					<td>
						<input type="text" id="rollCode" name="rollCode" value="${feedingRecord.rollCode}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">编织计划ID:</td>
					<!--编织计划ID-->
					<td>
						<input type="text" id="weaveId" name="weaveId" value="${feedingRecord.weaveId}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">裁减计划ID:</td>
					<!--裁减计划ID-->
					<td>
						<input type="text" id="cutId" name="cutId" value="${feedingRecord.cutId}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>投料日期:</td>
					<!--投料日期-->
					<td>
						<input type="text" id="feedingDate" name="feedingDate" value="${feedingRecord.feedingDate}" class="easyui-textbox" required="true" >
					</td>
				</tr>
			
		</table>
	</form>
</div>