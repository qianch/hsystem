<!--
	作者:高飞
	日期:2016-11-25 15:40:05
	页面:投诉增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>


<!--
	作者:高飞
	日期:2016-11-25 15:40:05
	页面:投诉增加或修改页面
-->


<style type="text/css">
textarea {
	width: 100%;
	height: 100%;
	border-radius: 5px;
	resize: none;
	border: none;
}
代码
</style>
<script type="text/javascript">
	//JS代码
</script>
<div>
	<!--投诉表单-->
	<form id="complaintForm" method="post" ajax="true" action="<%=basePath %>compalint/${empty complaint.id ?'add':'edit'}" autocomplete="off">
		<input type="hidden" name="id" value="${complaint.id }" />
		<input type="hidden" id="code" name="code"/>
		<input type="hidden" name="salesOrderConsumerId" id="salesOrderConsumerId" value="${salesOrder.salesOrderConsumerId }">
		<table width="100%" height="98%">
			<tr>
				<td class="title">投诉代码:</td>
				<!--投诉代码-->
				<td>
					<input type="hidden" name="complaintCode" value="${complaint.complaintCode }">
					<input type="text" id="complaintCodePrefix" ${empty complaint.id ?'':'readonly'} required="true" class="easyui-combobox" value="${fn:substring(complaint.complaintCode,0, 1)}"  style="width:55px;" data-options="onSelect:doSelect,textField:'value',valueField:'id',icons:[],data:[{id:'A',value:'A'},{id:'B',value:'B'},{id:'C',value:'C'},{id:'D',value:'D'},{id:'E',value:'E'}]">
					<input type="text" readonly id="suffix" style="width:120px;border:none;font-size:12px;" value="${fn:substring(complaint.complaintCode,1, 13)}">
				</td>
				<td class="title">叶型:</td>
				<!--叶型-->
				<td>
					<input type="text" id="model" name="model" value="${complaint.model }" class="easyui-textbox"style="width:160px;">
				</td>
				<td rowspan="14">
					<table height="100%" width="100%">
						<tr>
							<td class="title">问题描述:</td>
							<!--问题描述-->
							<td><textarea id="questionDesc" name="questionDesc" maxlength="800" placeholder="最多个输入1500个字">${complaint.questionDesc }</textarea></td>
						</tr>
						<tr>
							<!--8D报告-->
							<td class="title">原因分析:</td>
							<td><textarea id="analysisOfCauses" name="analysisOfCauses" maxlength="800" placeholder="最多个输入1500个字">${complaint.analysisOfCauses }</textarea></td>
						</tr>
						<tr>
							<!--投诉代码-->
							<td class="title">措施验收:</td>
							<td><textarea id="checkMesures" name="checkMesures" maxlength="800" placeholder="最多个输入1500个字">${complaint.checkMesures }</textarea></td>
						</tr>
						<tr>
							<!--客户来源-->
							<td class="title">整改措施:</td>
							<td><textarea id="correctiveMeasures" name="correctiveMeasures" maxlength="800" placeholder="最多个输入1500个字">${complaint.correctiveMeasures }</textarea></td>
						</tr>
						<tr>
							<td class="title">计划完成时间</td>
							<td><input type="text" id="planFinishDate" name="planFinishDate" value="${complaint.planFinishDate }" class="easyui-datebox" required="true"style="width:160px;"></td>
						</tr>
						<tr>
							<td class="title">实际完成时间</td>
							<td><input type="text" id="realFinishDate" name="realFinishDate" value="${complaint.realFinishDate }" class="easyui-datebox" style="width:160px;"></td>
						</tr>
						<tr>
							<!--客户名称-->
							<td class="title">文件上传:</td>
							<td>
								<input type="hidden" id="filePath" name="filePath" value="${complaint.filePath }" />
								<jsp:include page="../base/upload.jsp">
									<jsp:param value="${complaint.filePath }" name="path"/>
								</jsp:include>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="title">8D报告:</td>
				<!--8D报告-->
				<td><input type="text" id="d8Report" name="d8Report" value="${complaint.d8Report }" class="easyui-textbox"style="width:160px;"></td>
				
				<td class="title">涉及产品/服务:</td>
				<!--涉及产品/服务-->
				<td><input type="text" id="product" name="product" value="${complaint.product }" class="easyui-textbox"style="width:160px;"></td>
			</tr>
			<tr>
				<td class="title">客户来源:</td>
				<!--客户来源-->
				<td><input type="text" id="consumerFrom" name="consumerFrom" value="${complaint.consumerFrom }" class="easyui-combobox"style="width:160px;" required="true" data-options="'valueField':'id','textField':'text',data:[{'id':'国内','text':'国内'},{'id':'国外','text':'国外'}]"></td>
				
				<td class="title">批号:</td>
				<!--批号-->
				<td><input type="text" id="batchCode" name="batchCode" value="${complaint.batchCode }" class="easyui-textbox"style="width:160px;"></td>
			</tr>
			<tr>
				<td class="title">客户名称:</td>
				<!--客户名称-->
				<input type="hidden" id="complaintConsumerId" name="complaintConsumerId" value="${complaint.complaintConsumerId }">
				<td><input type="text" id="consumerName" name="consumerName" value="${complaint.consumerName }"style="width:160px;" required="true" class="easyui-searchbox" data-options="searcher:selectConsumer" onClick="selectConsumer()"></td>
				
				<td class="title">卷编号:</td>
				<!--卷编号-->
				<td><input type="text" id="rollBarcode" name="rollBarcode" value="${complaint.rollBarcode }" class="easyui-textbox"style="width:160px;"></td>
			</tr>
			<tr>
				<td class="title">基地:</td>
				<!--基地-->
				<td><input type="text" id="basePlace" name="basePlace" value="${complaint.basePlace }" class="easyui-textbox"style="width:160px;"></td>
				
				<td class="title">机台:</td>
				<!--机台-->
				<td><input type="text" id="device" name="device" value="${complaint.device }" class="easyui-textbox"style="width:160px;"></td>
			</tr>
			<tr>
				<td class="title">投诉日期:</td>
				<!--发货日期-->
				<td><input type="text" id="complaintDate" name="complaintDate" value="${complaint.complaintDate }" class="easyui-datebox" required="true"style="width:160px;"></td>
				
				<td class="title">班别:</td>
				<!--班别-->
				<td><input type="text" id="shift" name="shift" value="${complaint.shift }" class="easyui-textbox"style="width:160px;"></td>
			</tr>
			<tr>
				<td class="title">信息来源:</td>
				<!--信息来源-->
				<td><input type="text" id="infoFrom" name="infoFrom"  value="${complaint.infoFrom }" required="true" class="easyui-textbox" validType="length[1,85]"style="width:160px;"></td>
				
				<td class="title">操作工:</td>
				<!--操作工-->
				<td><input type="text" id="operator" name="operator" value="${complaint.operator }" class="easyui-textbox"style="width:160px;"></td>
			</tr>
			<tr>
				<td class="title">供货规格:</td>
				<!--供货规格-->
				<td>
				<input type="text" id="productModel" name="productModel" value="${complaint.productModel }"  required="true" class="easyui-searchbox" style="width:160px;" data-options="searcher:selectProduct" ></td>
				
				<td class="title">产品结构分类:</td>
				<!--产品结构分类-->
				<td><input type="text" id="productType" name="productType" value="${complaint.productType }" class="easyui-textbox"style="width:160px;"></td>
			</tr>
			<tr>
				<td class="title">供货状态:</td>
				<!--供货状态-->
				<td><input type="text" id="supplyState" name="supplyState" value="${complaint.supplyState }" class="easyui-combobox" required="true"style="width:160px;" data-options="'valueField':'id','textField':'text',data:[{'id':'常规供货','text':'常规供货'},{'id':'试样供货','text':'试样供货'}]"></td>
				
				<td class="title">整改单位:</td>
				<!--整改单位-->
				<td><input type="text" id="rectificationUnit" name="rectificationUnit" value="${complaint.rectificationUnit }" class="easyui-textbox"style="width:160px;"></td>
			</tr>
			<tr>
				<td class="title">出货数量:</td>
				<!--出货数量-->
				<td><input type="text" id="supplyCount" name="supplyCount" value="${complaint.supplyCount }" class="easyui-textbox" required="true" style="width:160px;"></td>
				
				<td class="title">原因分类:</td>
				<!--原因分类-->
				<td><input type="text" id="reason" name="reason" value="${complaint.reason }" class="easyui-textbox"style="width:160px;"></td>
			</tr>
			<tr>
				<td class="title">发货日期:</td>
				<!--发货日期-->
				<td><input type="text" id="deliveryDate" name="deliveryDate" value="${complaint.deliveryDate }" class="easyui-textbox" validType="length[1,85]" required="true"style="width:160px;"></td>
				
				<td class="title">原因细分:</td>
				<!--原因细分-->
				<td><input type="text" id="specificReasons" name="specificReasons" value="${complaint.specificReasons }" class="easyui-textbox"style="width:160px;"></td>
			</tr>
			<tr>
				<td class="title">生产日期:</td>
				<!--生成日期-->
				<td><input type="text" id="produceDate" name="produceDate" value="${complaint.produceDate }" required="true" class="easyui-textbox" validType="length[1,85]"style="width:160px;"></td>
				
				<td class="title">处理进度:</td>
				<!--处理进度-->
				<td><input type="text" id="handlingProgress" name="handlingProgress" value="${complaint.handlingProgress }" class="easyui-textbox" validType="length[0,100]"style="width:160px;"></td>
			</tr>
			<tr>
				<td class="title">责任单位:</td>
				<!--责任单位-->
				<td><input type="text" id="businessUnit" name="businessUnit" value="${complaint.businessUnit }" class="easyui-textbox"style="width:160px;"></td>
				
				<td class="title">赔偿金额:</td>
				<!--赔偿金额-->
				<td><input type="text" id="compensation" name="compensation" 
					<c:choose>
						<c:when test="${complaint.compensation==null }">value='￥0'</c:when>
						<c:otherwise>value='${complaint.compensation}'</c:otherwise> 
					</c:choose>  
				class="easyui-textbox" validType="length[0,100]" style="width:160px;"></td>
			</tr>
			<tr>
				<td class="title">投诉类型:</td>
				<!--投诉类型-->
				<td><input type="text" id="complaintType" name="complaintType" required="true" value="${complaint.complaintType }" class="easyui-combobox"style="width:160px;" data-options="'valueField':'id','textField':'text',data:[{'id':'补货','text':'补货'},{'id':'抱怨','text':'抱怨'},{'id':'退货','text':'退货'},{'id':'索赔','text':'索赔'},{'id':'换货','text':'换货'}]"></td>
			</tr>
		</table>
	</form>
</div>