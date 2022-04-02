<!--
	作者:高飞
	日期:2016-11-28 21:52:43
	页面:生产计划明细JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>生产计划明细</title>
  	<%@ include file="../base/meta.jsp" %>
	<%@ include file="producePlanDetail.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<jsp:include page="../base/toolbar.jsp">
				<jsp:param value="add" name="ids"/>
				<jsp:param value="edit" name="ids"/>
				<jsp:param value="delete" name="ids"/>
				<jsp:param value="icon-add" name="icons"/>
				<jsp:param value="icon-edit" name="icons"/>
				<jsp:param value="icon-remove" name="icons"/>
				<jsp:param value="增加" name="names"/>
				<jsp:param value="编辑" name="names"/>
				<jsp:param value="删除" name="names"/>
				<jsp:param value="add()" name="funs"/>
				<jsp:param value="edit()" name="funs"/>
				<jsp:param value="doDelete()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="producePlanDetailSearchForm" autoSearchFunction="false">
					
					
					<label class="panel-title">搜索：</label>
					<input type="text" name="filter[**]" like="true" class="easyui-textbox">
					
					
					
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="生产计划明细列表" class="easyui-datagrid"  url="${path}producePlanDetail/list" toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"  data-options="onDblClickRow:dbClickEdit">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="PRODUCEPLANID" sortable="true" width="15">生产计划ID</th>
					<th field="PRODUCTID" sortable="true" width="15">产品id</th>
					<th field="PLANCODE" sortable="true" width="15">任务单号</th>
					<th field="CONSUMERID" sortable="true" width="15">客户id</th>
					<th field="SALESORDERCODE" sortable="true" width="15">订单号</th>
					<th field="BATCHCODE" sortable="true" width="15">批次号</th>
					<th field="PRODUCTMODEL" sortable="true" width="15">产品规格</th>
					<th field="PRODUCTNAME" sortable="true" width="15">产品名称</th>
					<th field="PRODUCTWIDTH" sortable="true" width="15">门幅</th>
					<th field="PRODUCTWEIGHT" sortable="true" width="15">卷重</th>
					<th field="PRODUCTLENGTH" sortable="true" width="15">卷长</th>
					<th field="ORDERCOUNT" sortable="true" width="15">订单数量(kg/套)</th>
					<th field="BOMVERSION" sortable="true" width="15">工艺版本</th>
					<th field="BOMCODE" sortable="true" width="15">工艺代码</th>
					<th field="BCBOMVERSION" sortable="true" width="15">包装版本</th>
					<th field="BCBOMCODE" sortable="true" width="15">包装代码</th>
					<th field="BCBOXCOUNT" sortable="true" width="15">总卷数</th>
					<th field="BCTRAYCOUNT" sortable="true" width="15">总托数</th>
					<th field="REQUIREMENTCOUNT" sortable="true" width="15">需要生产数量(kg/套)</th>
					<th field="PRODUCEDCOUNT" sortable="true" width="15">已生产数量(kg/套)</th>
					<th field="DELEVERYDATE" sortable="true" width="15">出货日期</th>
					<th field="DEVICECODE" sortable="true" width="15">机台号</th>
					<th field="COMMENT" sortable="true" width="15">备注</th>
					<th field="ISFINISHED" sortable="true" width="15">是否已完成</th>
					<th field="SORT" sortable="true" width="15">排序</th>
					<th field="PRODUCTTYPE" sortable="true" width="15">产品属性</th>
					<th field="PRODUCEPLANDETAILID" sortable="true" width="15">生产计划明细Id</th>
					<th field="PACKAGEDCOUNT" sortable="true" width="15">打包数量</th>
					<th field="FROMTCID" sortable="true" width="15">来自套材</th>
					<th field="FROMTCNAME" sortable="true" width="15">来自套材名称</th>
				</tr>
			</thead>
		</table>
	</div>
</body>