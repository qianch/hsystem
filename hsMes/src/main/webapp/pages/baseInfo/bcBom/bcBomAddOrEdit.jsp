<!--
	作者:徐波
	日期:2016-10-8 16:53:24
	页面:包材bom增加或修改页面
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
	<!--包材bom表单-->
	<form id="bcBomForm" method="post" ajax="true" action="<%=basePath %>bcBom/${empty bcBom.id ?'add':'edit'}" autocomplete="off" >
		
		<input type="hidden" name="id" value="${bcBom.id}" />
		<input type="hidden" id="packBomConsumerId" name="packBomConsumerId" value="${bcBom.packBomConsumerId}" />
		<table width="100%">
				<tr>
					<td class="title">总称:</td>
					<!--总称-->
					<td>
						<input type="text" style="width:360px" id="packBomGenericName" name="packBomGenericName" value="${bcBom.packBomGenericName}" class="easyui-textbox" required="true" data-options="validType:['length[1,100]']">
					</td>
					<td class="title">产品规格:</td>
					<!--产品规格-->
					<td>
						<input type="text"  style="width:360px" id="packBomModel" name="packBomModel" value="${bcBom.packBomModel}" class="easyui-textbox" required="true" data-options="validType:['length[1,100]']">
					</td>
				</tr>
				<tr>
					<%-- <td class="title">包装名称:</td>
					<!--包装名称-->
					<td>
						<input type="text" style="width:360px" id="packBomName" name="packBomName" value="${bcBom.packBomName}" class="easyui-textbox" required="true" data-options="validType:['length[1,100]']">
					</td> --%>
					<td class="title">门幅(毫米):</td>
					<!--门幅-->
					<td>
						<input type="text"  style="width:360px" id="packBomWidth" name="packBomWidth" value="${bcBom.packBomWidth}" class="easyui-numberbox"data-options="validType:['length[1,32]']">
					</td>
					<td class="title">试样工艺:</td>
					<!--客户信息-->
					<td>
						<%-- <input type="text" id="ftcProcBomConsumerId" name="ftcProcBomConsumerId" value="${fTc_Bom.ftcProcBomConsumerId}" class="textbox" required="true" > --%>
						<input type="text" style="width:360px" id="isTestPro" name="isTestPro" value="${bcBom.isTestPro}" class="easyui-combobox"  panelHeight="auto" required="true" editable="false" data-options="data: [
	                        {value:'0',text:'常规产品'},
	                        {value:'1',text:'变更试用'},
	                        {value:'2',text:'新品试样'}
                    	]" >
					</td>
				</tr>
				<tr>
					<td class="title">包装标准代码:</td>
					<!--包装标准代码-->
					<td>
						<input type="text" style="width:360px" id="packBomCode" name="packBomCode" value="${bcBom.packBomCode}" class="easyui-textbox" required="true" data-options="validType:['length[1,100]']">
					</td>
					<td class="title">卷长(米):</td>
					<!--卷长-->
					<td>
						<input type="text"  style="width:360px" id="packBomLength" name="packBomLength" value="${bcBom.packBomLength}" class="easyui-numberbox"  data-options="validType:['length[1,30]']">
					</td>
				</tr>
				<tr>
					<td class="title">包装大类:</td>
					<!--包装大类-->
					<td>
						<input type="text" style="width:360px" id="packBomType" name="packBomType" value="${bcBom.packBomType}" class="easyui-textbox" required="true" data-options="validType:['length[1,100]']">
					</td>
					<td class="title">卷重(公斤):</td>
					<!--卷重-->
					<td>
						<input type="text"  style="width:360px" id="packBomWeight" name="packBomWeight" value="${bcBom.packBomWeight}" class="easyui-numberbox"  data-options="validType:['length[1,30]']">
					</td>
				</tr>
				<tr>
					<td class="title">适用客户:</td>
					<!--适用客户-->
					<td>
					<input id="bcpackBomConsumerId" style="width:360px" class="easyui-searchbox" value="${consumerName}" editable="false" required="true" data-options="searcher:ChooseConsumer">
					</td>
					<td class="title">卷径(毫米):</td>
					<!--卷径-->
					<td>
						<input type="text"  style="width:360px" id="packBomRadius" name="packBomRadius" value="${bcBom.packBomRadius}" class="easyui-textbox"  data-options="validType:['length[1,30]']">
					</td>
				</tr>
	
				<tr>
					<td class="title">每箱卷数:</td>
					<!--每箱卷数-->
					<td>
						<input type="text" style="width:360px" id="packBomRollsPerBox" name="packBomRollsPerBox" value="${bcBom.packBomRollsPerBox}" class="easyui-numberbox" precision="0">
					</td>
					<td class="title">每托箱数:</td>
					<!--每托箱数-->
					<td>
						<input type="text"  style="width:360px" id="packBomBoxesPerTray" name="packBomBoxesPerTray" value="${bcBom.packBomBoxesPerTray}" class="easyui-numberbox" precision="0" >
					</td>
				</tr>
	
				<tr>
					<td class="title">每托卷数:</td>
					<!--每托卷数-->
					<td>
						<input type="text" style="width:360px" id="packBomRollsPerTray" name="packBomRollsPerTray" value="${bcBom.packBomRollsPerTray}" class="easyui-numberbox" required="true" precision="0">
					</td>
				</tr>
				<tr>
					
				</tr>
		</table>
	</form>
</div>