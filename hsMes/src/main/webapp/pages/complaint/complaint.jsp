<!--
	作者:高飞
	日期:2016-11-25 80:40:05
	页面:投诉JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>投诉</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="complaint.js.jsp" %>
	<style>
		pre {
			white-space: pre-wrap;
			word-wrap: break-word;
		}
	</style>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
   	<div data-options="region:'east',border:false,title:''" style="width: 300px;overflow-x:hidden;">
   		<ul id="attachment"  class="easyui-datalist" textField="FILENAME" title="附件列表（双击下载）" lines="true" style="width:100%;" height="auto"  data-options="onDblClickRow:fileDbClick">
			
		</ul>
		<div id="question" class="easyui-panel" title="问题描述" style="width:100%;padding:10px;">
        	
    	</div>
    	<div id="analysis" class="easyui-panel" title="原因分析" style="width:100%;padding:10px;">
        	
    	</div>
    	<div id="corrective" class="easyui-panel" title="整改措施" style="width:100%;padding:10px;">
        	
    	</div>
   	</div>
	<div data-options="region:'center',border:false" style="overflow: false;position: relative; ">
		<div id="toolbar">
			<jsp:include page="../base/toolbar.jsp">
				<jsp:param value="add" name="ids"/>
				<jsp:param value="edit" name="ids"/>
				<jsp:param value="delete" name="ids"/>
				<jsp:param value="excel" name="ids"/>
				<jsp:param value="icon-add" name="icons"/>
				<jsp:param value="icon-edit" name="icons"/>
				<jsp:param value="icon-remove" name="icons"/>
				<jsp:param value="platform-icon9" name="icons"/>
				<jsp:param value="增加" name="names"/>
				<jsp:param value="编辑" name="names"/>
				<jsp:param value="删除" name="names"/>
				<jsp:param value="导出" name="names"/>
				<jsp:param value="add()" name="funs"/>
				<jsp:param value="edit()" name="funs"/>
				<jsp:param value="doDelete()" name="funs"/>
				<jsp:param value="exportExcel()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="complaintSearchForm" autoSearchFunction="false">
					&nbsp投 诉 代 码 ： &nbsp<input type="text" name="filter[complaintCode]" like="true" class="easyui-textbox"  like="true" data-options="onChange:filter">
					&nbsp客 户 简 称 ： &nbsp<input type="text" name="filter[consumerSimpleName]" like="true" class="easyui-textbox"  like="true" data-options="onChange:filter">
					供货规格： <input type="text" name="filter[productModel]" like="true" class="easyui-textbox"  like="true" data-options="onChange:filter"></br>
					&nbsp客 户 来 源 ： &nbsp<input type="text" id="cfrom" name="filter[consumerFrom]" class="easyui-combobox"  data-options="onSelect:filter,onChange:filter,data: [
		                       {value:'3',text:'全部'},  {value:'国内',text:'国内'}, {value:'国外',text:'国外'}]">
					&nbsp投 诉 日 期 ： &nbsp<input type="text" name="filter[complaintDate]" class="easyui-datebox" data-options="onSelect:filter">
					</br>
					计划完成日期：<input type="text" name="filter[planFinishDate]" class="easyui-datebox" data-options="onSelect:filter">
					实际完成日期：<input type="text" name="filter[realFinishDate]" class="easyui-datebox" data-options="onSelect:filter">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="true" title="" class="easyui-datagrid"  url="${path}complaint/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="false" fit="true"  data-options="onDblClickRow:dbClickEdit,onClickRow:clickRow,onLoadSuccess:dgLoadSuccess">
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="COMPLAINTCODE" sortable="true" width="100">投诉代码</th>
					<!-- <th field="CONSUMERNAME" sortable="true" width="80">客户名称</th> -->
					<th field="CONSUMERSIMPLENAME" width="80" sortable="true">客户简称</th>
					<th field="PRODUCTMODEL" sortable="true" width="80">供货规格</th>
					<th field="LASTUPDATEUSER" sortable="true" width="80">最后操作人</th>
					<th field="LASTUPDATEDATE" sortable="true" width="80" formatter="dateFormatter">最后操作时间</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="MODEL" sortable="true" width="80">叶型</th>
					<th field="PRODUCT" sortable="true" width="100">涉及产品/服务</th>
					<th field="BATCHCODE" sortable="true" width="80">批号</th>
					<th field="ROLLBARCODE" sortable="true" width="80">卷编号</th>
					<th field="DEVICE" sortable="true" width="80">机台</th>
					<th field="SHIFT" sortable="true" width="80">班别</th>
					<th field="OPERATOR" sortable="true" width="80">操作工</th>
					<th field="PRODUCTTYPE" sortable="true" width="80">产品结构分类</th>
					<th field="RECTIFICATIONUNIT" sortable="true" width="80">整改单位</th>
					<th field="REASON" sortable="true" width="80">原因分类</th>
					<th field="SPECIFICREASONS" sortable="true" width="80">原因细分</th>
					<th field="PLANFINISHDATE" sortable="true" width="80">计划完成时间</th>
					<th field="REALFINISHDATE" sortable="true" width="80">实际完成时间</th>
					<th field="D8REPORT" sortable="true" width="80">8D报告</th>
					<th field="CONSUMERFROM" sortable="true" width="80">客户来源</th>
					<th field="BASEPLACE" sortable="true" width="80">基地</th>
					<th field="COMPLAINTDATE" sortable="true" width="100" formatter="dateFormatter">投诉日期</th>
					<th field="INFOFROM" sortable="true" width="80">信息来源</th>
					<th field="SUPPLYSTATE" sortable="true" width="80">供货状态</th>
					<th field="SUPPLYCOUNT" sortable="true" width="80">出货数量</th>
					<th field="DELIVERYDATE" sortable="true" width="100" >发货日期</th>
					<th field="PRODUCEDATE" sortable="true" width="100" >生产日期</th>
					<!-- <th field="QUESTIONDESC" sortable="true" width="80">问题描述</th> -->
					<th field="BUSINESSUNIT" sortable="true" width="80">生产单位</th>
					<th field="COMPLAINTTYPE" sortable="true" width="80">投诉类型</th>
					<th field="CHECKMESURES" sortable="true" width="80">措施验收</th>
					<th field="COMPENSATION" sortable="true" width="80">赔偿金额</th>
					<th field="HANDLINGPROGRESS" sortable="true" width="80">处理进度</th>
				</tr>
			</thead>
		</table>
	</div>
</body>