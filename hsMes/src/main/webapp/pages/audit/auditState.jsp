
<!--
	作者:高飞
	日期:2016-10-25 13:52:50
	页面:流程实例JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../base/jstl.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<style type="text/css">
.title {
	width: 100px;
}

th {
	text-align: right;
	padding-right: 5px;
}

tr {
	height: 30px;
}

td {
	text-align: left;
	padding-left: 5px;
}

.easyui-layout {
	overflow: auto;
}
</style>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div id="_audit_state_page_" class="easyui-tabs" style="width:700px;height:250px" fit="true" data-options="selected:${fn:length(audit)-1 }">
		<c:forEach var="m" items="${audit }" varStatus="index">
		 <div title="第${index.index+1 }次审核" style="padding:0px">
				<table id="_audit_state_view_" style="width:100%;text-align: center;">
					<tr>
						<th class="title">审核标题</th>
						<td>${m.title }</td>
					</tr>
					<tr>
						<th class="title">提审人</th>
						<td>${m.user }</td>
					</tr>
					<tr>
						<th class="title">提审时间</th>
						<td><fmt:formatDate value="${m.submitTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr>
						<th class="title">一级审核人</th>
						<td>${m.levelOneUser }</td>
					</tr>
					<tr>
						<th class="title">一级审核时间</th>
						<td><fmt:formatDate value="${m.levelOneCheckTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr>
						<th class="title">一级审核结果</th>
						<td>${m.levelOneResult }</td>
					</tr>
					<tr>
						<th class="title">一级审核内容</th>
						<td><pre>${m.levelOneMsg }</pre></td>
					</tr>
					<c:if test="${level eq 2}">
					<tr>
						<th class="title">二级审核人</th>
						<td>${m.levelTwoUser }</td>
					</tr>
					<tr>
						<th class="title">二级审核时间</th>
						<td><fmt:formatDate value="${m.levelTwoCheckTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr>
						<th class="title">二级审核结果</th>
						<td>${m.levelTwoResult }</td>
					</tr>
					<tr>
						<th class="title">二级审核内容</th>
						<td><pre>${m.levelTwoMsg }</pre></td>
					</tr>
					</c:if>
				</table>
			 </div>
		</c:forEach>
		 </div>
</div>
<script type="text/javascript">
</script>
