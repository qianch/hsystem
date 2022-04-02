<!--
	作者:king
	日期:2017-8-2 10:39:01
	页面:成品类别管理增加或修改页面
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
var PCisExistURL = path  + "product/category/isexist";
/**
 * 成品类别编码唯一性验证
 */
 $("#categoryCode").textbox({
	 onChange:function(){
		 var code =  $("#categoryCode").val();
		 JQ.ajaxGet(PCisExistURL+"?code="+code,function(data){
			if(data == "0"){
				$.messager.alert("提示信息","成品类别编码重复:"+code,"warning");
			}
		});			
	 }
 });
</script>
<div>
	<!--成品类别管理表单-->
	<form id="finishedProductCategoryForm" method="post" ajax="true" action="<%=basePath %>product/category/${empty finishedProductCategory.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${finishedProductCategory.id}" />
		
		<table width="100%">
				<tr>
					<td class="title"><span style="color:red;">*</span>成品类别编号:</td>
					<!--类别编号-->
					<td>
						<input type="text" id="categoryCode" name="categoryCode" value="${finishedProductCategory.categoryCode}" class="easyui-textbox" required="true" >
					</td>
				</tr>
				<tr>
					<td class="title"><span style="color:red;">*</span>成品类别名称:</td>
					<!--类别名称-->
					<td>
						<input type="text" id="categoryName" name="categoryName" value="${finishedProductCategory.categoryName}" class="easyui-textbox" required="true" >
					</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td class="title">创建人:</td> -->
					<!--创建人-->
<!-- 					<td> -->
<!-- 						<input type="text" id="creater" name="creater" value="${finishedProductCategory.creater}" class="easyui-textbox"  > -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="title">创建时间:</td> -->
					<!--创建时间-->
<!-- 					<td> -->
<!-- 						<input type="text" id="createTime" name="createTime" value="${finishedProductCategory.createTime}" class="easyui-textbox"  > -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="title">修改人:</td> -->
					<!--修改人-->
<!-- 					<td> -->
<!-- 						<input type="text" id="modifyUser" name="modifyUser" value="${finishedProductCategory.modifyUser}" class="easyui-textbox"  > -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="title">修改时间:</td> -->
					<!--修改时间-->
<!-- 					<td> -->
<!-- 						<input type="text" id="modifyTime" name="modifyTime" value="${finishedProductCategory.modifyTime}" class="easyui-textbox"  > -->
<!-- 					</td> -->
<!-- 				</tr> -->
			
		</table>
	</form>
</div>