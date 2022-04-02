<!--
	作者:肖文彬
	日期:2016-9-28 11:24:47
	页面:客户管理增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<style type="text/css">
	textarea{
		border-radius:5px;
	    margin: 0px;
    	height: 112px;
    	width: 238px;
    	resize:none;}
</style>
<script type="text/javascript">
	//JS代码
</script>
<div>
	<!--客户管理表单-->
	<form id="consumerForm" method="post" ajax="true" action="<%=basePath %>consumer/${empty consumer.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${consumer.id}" />
		
		<table width="100%">
				<tr>
					<td class="title">客户代码:</td>
					<!--客户代码-->
					<td>
						<input type="text" style="width:100%" data-options="required:true" validType="length[1,100]" id="consumerCode" name="consumerCode" value="${consumer.consumerCode}" class="easyui-textbox"  >
					</td>
					<td class="title">客户等级:</td>
					<!--客户等级-->
					<td>
						<input type="text"  style="width:90%" id="consumerGrade" required=true name="consumerGrade"  value="${consumer.consumerGrade}" class="easyui-combobox" panelHeight="auto" editable="false" data-options="data: [
	                        {value:'1',text:'A'},
	                        {value:'2',text:'B'},
	                        {value:'3',text:'C'}
                    	]">
					</td>
				</tr>
				<tr>
					<td class="title">客户名称:</td>
					<!--客户名称-->
					<td>
						<input type="text" style="width:90%" data-options="required:true" validType="length[1,100]" id="consumerName" name="consumerName" value="${consumer.consumerName}" class="easyui-textbox"  >
					</td>
					<td class="title">客户简称:</td>
					<!--客户名称-->
					<td>
						<input type="text" style="width:90%" data-options="required:true" id="consumerSimpleName" name="consumerSimpleName" value="${consumer.consumerSimpleName}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">客户大类:</td>
					<!--客户大类-->
					<td>
						<input type="text" style="width:90%" id="consumerCategory" required=true name="consumerCategory"  value="${consumer.consumerCategory}" class="easyui-combobox" panelHeight="auto" editable="false" data-options="data: [
	                        {value:'1',text:'国内'},
	                        {value:'2',text:'国外'}
                    	]">
					</td>
					<td class="title">客户联系人:</td>
					<!--客户联系人-->
					<td>
						<input type="text"  style="width:90%"  id="consumerContact" name="consumerContact" value="${consumer.consumerContact}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					
				</tr>
				<tr>
					<td class="title">客户地址:</td>
					<!--客户地址-->
					<td>
						<input type="text" style="width:90%"  id="consumerAddress" name="consumerAddress" value="${consumer.consumerAddress}" class="easyui-textbox"  >
					</td>
					<td class="title">联系电话:</td>
					<!--联系电话-->
					<td>
						<input type="text" style="width:90%" data-options="validType:'length[1,20]'" id="consumerPhone" name="consumerPhone" value="${consumer.consumerPhone}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">邮件地址:</td>
					<!--邮件地址-->
					<td>
						<input type="text" style="width:90%" data-options="validType:['email']" id="consumerEmail" name="consumerEmail" value="${consumer.consumerEmail}" class="easyui-textbox"  >
					</td>
					<td class="title">客户ERP代码:</td>
					<!--客户ERP代码-->
					<td>
						<input type="text" style="width:90%" id="consumerCodeErp"  name="consumerCodeErp" value="${consumer.consumerCodeErp}" class="easyui-textbox"  >
					</td>
				</tr>
				<tr>
					<td class="title">备注:</td>
					<!--备注-->
					<td colspan="3">
						<textarea style="width:90%" maxlength=250 placeholder="请输入1-200之间的文本" name="consumerMemo" rows="2" cols="22">${consumer.consumerMemo}</textarea>
					</td>
				</tr>
				
		</table>
	</form>
</div>