<!--
	作者:高飞
	日期:2016-10-12 11:06:09
	页面:原料增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<style type="text/css">
//
CSS



 



代码
</style>
<script type="text/javascript">
	//JS代码
</script>
<div>
	<!--原料表单-->
	<form id="materialForm" method="post" ajax="true" action="<%=basePath %>material/${empty material.id ?'add':'edit'}" autocomplete="off">

		<input type="hidden" name="id" value="${material.id}" />

		<table width="100%">
			<tr>
				<td class="title">物料编码:</td>
				<!--编码-->
				<td><input type="text" id="materialCode" name="materialCode" value="${material.materialCode}" class="easyui-textbox" validType="length[1,50]" required="true"></td>
				<td class="title">保质期(天):</td>
				<td><input id="materialShelfLife" name="materialShelfLife" value="${material.materialShelfLife}" class="easyui-numberbox" precision="0" validType="length[1,5]" required="true"></td>
			</tr>
			<tr>
				<td class="title">大类名称:</td>
				<!--原料名称-->
				<td><input type="text" id="produceCategory" name="produceCategory" value="${material.produceCategory}" class="easyui-textbox" validType="length[1,50]" required="true"></td>
				<td class="title">规格型号:</td>
				<!--规格型号-->
				<td><input type="text" id="materialModel" name="materialModel" value="${material.materialModel}" class="easyui-textbox" validType="length[1,80]" required="true"></td>
			</tr>
			<tr>
				<td class="title">最低库存:</td>
				<!--最低库存-->
				<td><input type="text" id="materialMinStock" name="materialMinStock" value="${material.materialMinStock}" class="easyui-numberbox" min=0 max=999999999 required="true"  precision=2 validType="length[1,50]" data-options="onChange:changeMin"></td>
				<td class="title">最大库存:</td>
				<!--最大库存-->
				<td><input type="text" id="materialMaxStock" name="materialMaxStock" value="${material.materialMaxStock}" class="easyui-numberbox" min=0 max=999999999 required="true" precision=2 validType="length[1,50]" data-options="onChange:changeMax"></td>
			</tr>
			<tr>
				<td class="title">上偏差:</td>
				<!--上偏差-->
				<td><input type="text" id="upperDeviation" name="upperDeviation" value="${material.upperDeviation}" class="easyui-numberbox" validType="length[1,10]" min=-99999999 max=99999999999999 precision=0 validType="length[1,50]" data-options="onChange:deviation"></td>
				<td class="title">下偏差:</td>
				<!--下偏差-->
				<td><input type="text" id="lowerDeviation" name="lowerDeviation" value="${material.lowerDeviation}" class="easyui-numberbox"  min=-99999999 max=99999999999999 precision=0 validType="length[1,10]" data-options="onChange:lowerDeviation"></td>
			</tr>
			<tr>
				<td class="title">接头方式:</td>
				<!--接头方式-->
				<td><input type="text" id="subWay" name="subWay" value="${material.subWay}" class="easyui-textbox" validType="length[1,50]"></td>
				<td class="title">制成率:</td>
				<!--产品大类名称-->
				<td><input type="text" id="madeRate" name="madeRate" value="${material.madeRate}" class="easyui-numberbox" min="1" max="100" precision="2" suffix="%" validType="length[1,10]"></td>
			</tr>
			<tr>
				<td class="title">备注:</td>
				<!--备注-->
				<td colspan="3"><input type="text" id="materialMemo" name="materialMemo" value="${material.materialMemo}" class="easyui-textbox" validType="length[1,50]" validType="length[1,100]" multiline="true" style="width:100%;height:100px;"></td>
			</tr>

		</table>
	</form>
</div>