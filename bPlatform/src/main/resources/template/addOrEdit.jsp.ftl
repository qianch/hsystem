<!--
	作者:${author}
	日期:${date}
	页面:${table}增加或修改页面
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
	<!--${table}表单-->
	<form id="${entity}Form" method="post" ajax="true" action="<%=basePath %>${entity}/${"$"}{empty ${entity}.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${"$"}{${entity}.id}" />
		
		<table width="100%">
			<#list list as m>
				<tr>
					<td class="title"><#if m["required"]??><span style="color:red;">*</span></#if>${m["title"]}:</td>
					<!--${m["title"]}-->
					<td>
						<input type="text" id="${m["field2"]}" name="${m["field2"]}" value="${"$"}{${entity}.${m["field2"]}}" class="easyui-textbox" <#if m["required"]??>required="true"</#if> >
					</td>
				</tr>
			</#list>
			
		</table>
	</form>
</div>