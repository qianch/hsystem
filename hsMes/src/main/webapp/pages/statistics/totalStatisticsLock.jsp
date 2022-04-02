<!--
	作者:徐波
	日期:2016-11-26 14:44:04
	页面:综合统计增加或修改页面
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
	<!--综合统计表单-->
	<form id="totalStatisticsForm" method="post" ajax="true" action="<%=basePath %>totalStatistics/${empty type ?'lock':'unlock'}" autocomplete="off" >
		
		<input type="hidden" name="ids" value="${ids}" />
		<table width="100%">
				<tr>
				<td class="title">投诉号:</td>
				<td>
				<input id="complaintCode" name="complaintCode" type="text" class="easyui-searchbox" 
					data-options="searcher:selectComplaint,icons:[]">
				</td>
				</tr>
				<tr>
				<td class="title">${empty type ?'冻结原因:':'解冻原因:'}</td>
				<td>
				<textarea style="width:257;height:132" id="reasons" name="reasons" maxlength="1500" placeholder="最多个输入1500个字" required="true"></textarea>
						
				</td>
				</tr>
		</table>
	</form>
</div>