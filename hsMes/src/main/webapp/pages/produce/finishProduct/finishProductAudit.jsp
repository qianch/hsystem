<!--
	作者:宋黎明
	日期:2016-9-30 10:49:34
	页面:成品信息增加或修改页面
-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../base/meta.jsp" %>
<style type="text/css">
textarea {
	border-radius: 5px;
}
</style>
<script type="text/javascript">
	
</script>
<div>
	<!--成品信息表单-->
	<form id="finishProductForm" method="post" ajax="true"
		action="<%=basePath %>finishProduct/${empty finishProduct.id ?'add':'edit'}"
		autocomplete="off">

		<input type="hidden" name="id" value="${finishProduct.id}" />
		<input type="hidden" id="packBomId" name="packBomId"
					value="${finishProduct.packBomId} ">
		<input type="hidden" id="obsolete" name="obsolete"
					value="${finishProduct.obsolete} ">
		<!--成品客户信息ID-->
		<input type="hidden" id="productConsumerId" name="productConsumerId"
			value="${finishProduct.productConsumerId}" />
		<table width="100%">
			<tr>
				<td class="title">客户名称:</td>
				<!--客户名称-->
				<td><input id="productConsumerName" class="easyui-textbox" readOnly="true" 
					value="${consumer.consumerName}"  required="true" style="width:400px"></td>

				<td class="title">客户代码:</td>
				<!--客户代码-->
				<td><input id="productConsumerCode" class="easyui-textbox" readonly="true" readOnly="true"
					value="${consumer.consumerCode}" editable="false" style="width:300px"></td>
			</tr>
			<tr>
				<td class="title">客户产品名称:</td>
				<!--客户产品名称-->
				<td><input type="text" id="consumerProductName"
					name="consumerProductName"
					value="${finishProduct.consumerProductName}" class="easyui-textbox"readOnly="true"
					required="true" data-options="validType:['length[0,150]']" style="width:400px"></td>

				<td class="title">厂内名称:</td>
				<!--厂内名称-->
				<td><input type="text" id="factoryProductName"
					name="factoryProductName"readOnly="true"
					value="${finishProduct.factoryProductName}" class="easyui-textbox" required="true"
					data-options="validType:['length[0,150]']" style="width:300px"></td>
			</tr>
			<tr>
				<td class="title">门幅(mm):</td>
				<!--门幅-->
				<td><input type="text" id="productWidth" name="productWidth"readOnly="true"
					value="${finishProduct.productWidth}" class="easyui-numberbox"
					suffix="mm" precision="2" style="width:400px"></td>

				<td class="title">卷长(m):</td>
				<!--卷长-->
				<td><input type="text" id="productRollLength"readOnly="true"
					name="productRollLength" value="${finishProduct.productRollLength}"
					class="easyui-numberbox" suffix="m" precision="2" style="width:300px"></td>
			</tr>

			<tr>
				<td class="title">预留长度(m):</td>
				<!--预留长度-->
				<td><input type="text" id="reserveLength"readOnly="true"
						   name="reserveLength" value="${finishProduct.reserveLength}"
						   class="easyui-numberbox" suffix="m" precision="2" style="width:300px"></td>
				<td class="title">客户物料号:</td>
				<!--客户物料号-->
				<td><input type="text" id="customerMaterialCodeOfFP"readOnly="true"
						   name="customerMaterialCodeOfFP" value="${finishProduct.customerMaterialCodeOfFP}"
						   class="easyui-textbox" suffix="m" precision="2" style="width:300px"></td>
			</tr>

			<tr>
				<td class="title">卷重(kg):</td>
				<!--卷重-->
				<td><input type="text" id="productRollWeight"readOnly="true"
					name="productRollWeight" value="${finishProduct.productRollWeight}"
					class="easyui-numberbox" suffix="Kg" precision="2" style="width:400px"></td>

				<td class="title">托唛头:</td>
				<!--托唛头代码-->
				<td><input type="text" id="productTrayCode"readOnly="true"
					name="productTrayCode" value="${finishProduct.productTrayCode}"
					class="easyui-textbox" style="width:300px"></td>
			</tr>
			
			<tr id="isShowWeight">
				<td class="title">最大卷重(kg):</td>
				<!--最大卷重-->
				<td><input type="text" id="maxWeight" name="maxWeight"
					value="${finishProduct.maxWeight}" class="easyui-numberbox"
					readonly="true" precision="2" style="width:400px"></td>

				<td class="title">最小卷重(kg):</td>
				<!--最小卷重-->
				<td><input type="text" id="minWeight" name="minWeight"
					value="${finishProduct.minWeight}" class="easyui-numberbox"
					readonly="true"  precision="2" style="width:300px"></td>
			</tr>

			<tr>
				<td class="title">卷标签:</td>
				<!--卷标签代码-->
				<td><input type="text" id="productRollCode"readOnly="true"
					name="productRollCode" value="${finishProduct.productRollCode}"
					class="easyui-textbox" style="width:400px"></td>

				<td class="title">箱唛头:</td>
				<!--箱唛头代码-->
				<td><input type="text" id="productBoxCode"readOnly="true"
					name="productBoxCode" value="${finishProduct.productBoxCode}"
					class="easyui-textbox" style="width:300px"></td>
			</tr>
			<tr>
				<td class="title">产品类型:</td>
				<td>
					<label for="productIsTc1"><input id="productIsTc1" type="radio" value="1"  disabled name="productIsTc" ${finishProduct.productIsTc eq 1 ?'checked':''}>
					套材</label>&nbsp 
					<label for="productIsTc2"><input id="productIsTc2" type="radio" value="2"  disabled name="productIsTc" ${finishProduct.productIsTc eq 2 ?'checked':''} >
					非套材</label>
					<label for="productIsTc3"><input id="productIsTc3" type="radio" value="-1"  disabled name="productIsTc" ${finishProduct.productIsTc eq -1 ?'checked':''}>
					胚布</label>
				</td>
				<td class="title">产品属性:</td>
				<td>
					<label for="productIsComm1"><input id="productIsComm1" type="radio" value="1" disabled name="isCommon" ${finishProduct.isCommon eq 1 ?'checked':''}>
					常规</label>&nbsp 
					<label for="productIsComm0"><input id="productIsComm0" type="radio" value="0" disabled name="isCommon" ${finishProduct.isCommon eq 0 ?'checked':''} >
					试样</label>
				</td>
			</tr>
			<tr>
				<td class="title">产品规格:</td>
				<td><input type="text" id="productModel" name="productModel"readOnly="true"
					value="${finishProduct.productModel}" class="easyui-textbox"
					required="true" data-options="validType:['length[0,150]']" style="width:400px"></td>
				</td>
				<td class="title">保质期(天):</td>
				<!--备注-->
				<td><input type="text" id="productShelfLife"readOnly="true"
					name="productShelfLife" value="${finishProduct.productShelfLife}"
					class="easyui-numberbox" required="true"  validType="length[1,5]" min="1" precision="0" style="width:300px"></td>
				
			</tr>
			<%-- <tr>
				<td class="title">是否为胚布</td>
				<td><input id="productIsFab2"
					type="radio" value="0" name="isShow"
					${finishProduct.isShow eq 0 || finishProduct.isShow eq null ?'checked':''}>
					<label for="productIsFab2">产品</label>&nbsp 
				<input id="productIsFab1" type="radio" name="isShow"
					value="1" ${finishProduct.isShow eq 1  ?'checked':''}> <label
					for="productIsFab1">胚布</label></td>
			</tr> --%>
			<tr>
				<td class="title">工艺代码:</td>
				<!--工艺标准代码-->
				<td><input id="productProcessBom" name="productProcessCode"
					value="${finishProduct.productProcessCode}"readOnly="true"
					class="easyui-searchbox" editable="false" required="true" style="width:400px"></td>
				<td class="title">工艺版本:</td>
				<!--工艺标准版本-->
				<input type="hidden" id="procBomId" name="procBomId"readOnly="true"
					value="${finishProduct.procBomId} " style="width:360px">
				<td><input type="text" id="productProcessBomVersion"readOnly="true"
					name="productProcessBomVersion" readonly="true" required="true" 
					value="${finishProduct.productProcessBomVersion}"
					class="easyui-textbox"  style="width:300px"></td>
				
					
			</tr>
			<tr>
				<td class="title">包装代码:</td>
				<!--包装标准代码-->
				<td><input type="text" id="productPackagingCode"
					name="productPackagingCode"readOnly="true"
					value="${empty finishProduct.productPackagingCode?'无包装':finishProduct.productPackagingCode}"   ${finishProduct.productIsTc eq -1||finishProduct.productIsTc eq null ?'readonly':''}
					class="easyui-searchbox" editable="false"  ${finishProduct.productIsTc eq -1 ?'':'required=\"true\"'}
					 style="width:400px"></td>	
				<td class="title">包装版本:</td>
				<!--包装标砖版本-->
				<td><input type="text" id="productPackageVersion"
					name="productPackageVersion" readonly="true"readOnly="true"
					value="${empty finishProduct.productPackageVersion?'无包装':finishProduct.productPackageVersion}"
					class="easyui-textbox" editable="false" style="width:300px"></td>
					
			</tr>
			<tr>
				<td class="title">衬管编码:</td>
				<td>
					<input type="text" id="carrierCode" name="carrierCode" value="${finishProduct.carrierCode}"  class="easyui-searchbox" 
						class="easyui-searchbox" editable="false" required="false"
						data-options="searcher:selectorCarrierCode" style="width:400px" readonly="readonly"/>
				</td>
				<td class="title">成品类别代码：</td>
				<td>
					<input type="text" id="pcType" name="categoryName" value="${productCategory.categoryCode}" 
						class="easyui-searchbox" editable="false" required="true"
						data-options="searcher:selectPcType" style="width:300px" readonly="readonly"/>
				</td>
			</tr>
			<tr>
				<td class="title">成品类别名称:</td>
				<td><input type="text" id="pcCode" name="categoryCode" value="${productCategory.categoryName}" class="easyui-textbox" style="width:400px" readonly="readonly"></td>			
				
				<td class="title">物料编码:</td>
				<td><input type="text" id="materielCode" name="materielCode" value="${finishProduct.materielCode}" class="easyui-textbox" style="width:300px" required="true" readonly="readonly"></td>
			</tr>
			<tr>
				<td class="title">衬管规格:</td>
				<td><input type="text" id="pcType1" name="zgmc"
					value="${zgmc}" class="easyui-textbox"
					style="width:360px" readonly="readonly"></td>
					
				<td class="title">工艺名称:</td>
				<td><input type="text" id="productProcessName"
					name="productProcessName"
					value="${finishProduct.productProcessName}" class="easyui-textbox"
					data-options="validType:['length[0,150]']" style="width:400px"
					readonly="true"></td>
				

			</tr>
			<tr>
				<td class="title">包装要求:</td>
				<!--备注-->
				<td style="padding-left:10px"><textarea id="packReq"readOnly="true"
						name="packReq" style="width:90%;height:100px;" maxlength="500" placeholder="限500字以内">${finishProduct.packReq}</textarea></td>
				<td class="title">称重规则</td>
				<!--备注-->
				<td><input type="text" id="productWeigh"readOnly="true"
					name="productWeigh" 
					<c:choose>
						<c:when test='${finishProduct.productWeigh==0}'>value="全称  (单卷重量300KG以上)"</c:when>	
						<c:when test='${finishProduct.productWeigh==1}'>value="抽称  (单卷重量300KG以下)"</c:when>	
						<c:when test='${finishProduct.productWeigh==2}'>value="不称  (套裁、坯布)"</c:when>	
					</c:choose>
				
					class="easyui-textrbox"  style="width:300px"></td>
			</tr>

			<tr>
				<td class="title" style="width:300px">备注:</td>
				<!--备注-->
				<td style="padding-left:10px"><textarea id="productMemo"readOnly="true"
						name="productMemo" style="width:90%;height:100px;">${finishProduct.productMemo}</textarea></td>
				<td class="title">工艺要求:</td>
				<!--备注-->
				<td style="padding-left:10px"><textarea id="procReq"readOnly="true"
						name="procReq" style="width:90%;height:100px;" maxlength="500" placeholder="限500字以内">${finishProduct.procReq}</textarea></td>
			</tr>
		</table>
	</form>
</div>