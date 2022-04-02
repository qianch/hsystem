<!--
	作者:高飞
	日期:2016-10-12 10:34:41
	页面: 质量等级增加或修改页面
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
	<!-- 质量等级表单-->
	<form id="qualityGradeForm" method="post" ajax="true" action="<%=basePath %>qualityGrade/${empty qualityGrade.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${qualityGrade.id}" />
		
		<table width="100%">
				<tr>
					<td class="title"><span style="color:red;">*</span>等级名称:</td>
					<!--等级名称-->
					<td>
						<input type="text" id="gradeName" name="gradeName" value="${qualityGrade.gradeName}" class="easyui-textbox" required="true" validType="length[1,30]" >
					</td>
				</tr>
				<tr>
					<td class="title">等级描述:</td>
					<!--等级描述-->
					<td>
						<input type="text" id="gradeDesc" name="gradeDesc" value="${qualityGrade.gradeDesc}" class="easyui-textbox" validType="length[1,100]" >
					</td>
				</tr>
				<tr>
					<td class="title">备注:</td>
					<!--备注-->
					<td>
						<input type="text" id="gradeMemo" name="gradeMemo" value="${qualityGrade.gradeMemo}" style="height:200px;width:400px;" class="easyui-textbox" multiline="true" validType="length[1,1000]"  >
					</td>
				</tr>
			
		</table>
	</form>
</div>