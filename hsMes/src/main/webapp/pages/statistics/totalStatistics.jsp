

<!--
	作者:徐波
	日期:2016-11-26 14:44:04
	页面:综合统计JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>综合统计</title>
<%@ include file="../base/meta.jsp"%>
<%@ include file="totalStatistics.js.jsp"%>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false"
		style="position: relative; height: 140px; width: 925px">
		<div id="toolbar">

			<div style="border:0px solid #DDDDDD">
				<form action="#" id="totalStatisticsSearchForm" autoSearchFunction="false">
					条码类型:<input type="text" name="searchType" class="easyui-combobox"
						text="全部"
						data-options="valueField: 'id',
       				 textField: 'text',data:[{'id':'','text':'全部'},{'id':'roll','text':'卷条码'},{'id':'part','text':'部件条码'},{'id':'box','text':'箱条码'},{'id':'tray','text':'托条码'}],onSelect:filter">
					　　条码:<input type="text" name="filter[rollBarcode]" like="true" class="easyui-textbox"> 
					　订单号:<input type="text"
						name="filter[salesOrderCode]" like="true" class="easyui-textbox">
					批次号:<input type="text" name="filter[batchCode]" like="true"
						class="easyui-textbox"></br> 
					客户名称:<input type="text"name="filter[CONSUMERNAME]" like="true" class="easyui-textbox">
					产品规格:<input type="text" name="filter[productModel]" like="true"
					　	class="easyui-textbox"> 
					　　车间:<input type="text"
						name="filter[workshopcode]" class="easyui-combobox"
								data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=weave,cut'">
					机台号:<input type="text" name="filter[deviceCode]" like="true" class="easyui-textbox"></br> 
					在库状态:<input
						type="easyui-combobox" name="filter[state]" value=""
						class="easyui-combobox"
						data-options=" valueField: 'id',
        textField: 'text' ,data:[{'id':'','text':'全部'},{'id':'1','text':'在库'},{'id':'-1','text':'出库'},{'id':'0','text':'未入库'}],onSelect:filter">
					　冻结人:<input type="text" id="isNameLock" name="filter[isNameLock]" class="easyui-textbox" like="true">
					产出时间:<input type="text" id="start" name="filter[start]" value=""
						class="easyui-datetimebox" > 
						　至:　<input
						type="text" id="end" name="filter[end]" value=""
						class="easyui-datetimebox">&nbsp;&nbsp;&nbsp; <a
						href="javascript:void(0)"
						class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
						onclick="filter()"> 搜索 </a>
				</form>
			</div>
			<jsp:include page="../base/toolbar.jsp">
				<jsp:param value="lock" name="ids" />
				<jsp:param value="unlock" name="ids" />
				<jsp:param value="judge" name="ids" />
				<jsp:param value="buda" name="ids" />
				<jsp:param value="icon-add" name="icons" />
				<jsp:param value="icon-delete" name="icons" />
				<jsp:param value="icon-ok" name="icons" />
				<jsp:param value="icon-ok" name="icons" />
				<jsp:param value="冻结" name="names" />
				<jsp:param value="解冻" name="names" />
				<jsp:param value="质量判级" name="names" />
				<jsp:param value="重打条码" name="names" />
				<jsp:param value="lock()" name="funs" />
				<jsp:param value="unlock()" name="funs" />
				<jsp:param value="judge()" name="funs" />
				<jsp:param value="buda()" name="funs" />
			</jsp:include>

		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"
			 toolbar="#toolbar"
			pagination="true" rownumbers="true" fitColumns="false" fit="true">
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="ROLLBARCODE" sortable="true" width="130"
						formatter="barcodeFormatter">条码号</th>
					<th field="BARCODETYPE" sortable="true" width="60"
						formatter="barcodeTypeFormatter">条码类型</th>
					<th field="ENDPACK" sortable="true" width = '80'
						formatter="endPackFormatter">打包状态</th>
					<th field="STATE" sortable="true" width="80"
						formatter="stockStateFormatter">库存状态</th>
					
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="CONSUMERNAME" sortable="true" width="200">客户名称</th>
					<th field="PRODUCTMODEL" sortable="true" width="140">产品规格</th>
					<th field="BATCHCODE" sortable="true" width="100">批次号</th>
					<th field="ROLLQUALITYGRADECODE" width="60">质量等级</th>
					<th field="ROLLWEIGHT" sortable="true" width="70" formatter="processNumberFormatter">卷重(kg)</th>
					<th field="PRODUCTWIDTH" sortable="true" width="70" formatter="processNumberFormatter">门幅(mm)</th>
					<th field="PRODUCTWEIGHT" sortable="true" width="85" formatter="processNumberFormatter">称重重量(kg)</th>
					<th field="PRODUCTLENGTH" sortable="true" width="70" formatter="processNumberFormatter">卷长(m)</th>
					<th field="ROLLREALLENGTH" sortable="true" width="70" formatter="processNumberFormatter">实际卷长(m)</th>
					<th field="PRODUCEPLANCODE" sortable="true" width="150">计划单号</th>
					<th field="SALESORDERCODE" sortable="true" width="150">订单号</th>
					<th field="ROLLOUTPUTTIME" sortable="true" width="150">生产时间</th>
					<th field="DEVICECODE" sortable="true" width="60">机台号</th>
					<th field="NAME" sortable="true" width="80" formatter="getWorkShop">车间</th>
					<th field="LOGINNAME" sortable="true" width="60">操作人</th>
					<th field="ISNAMELOCK" sortable="true" width="100">冻结人</th>
					<th field="ISLOCKED" sortable="true" width="80"
						formatter="lockStateFormatter">状态</th>
					<th field="ISABANDON" sortable="true" width="80" 
						formatter="isAbandonFormatter">是否作废</th>
					<th field="MEMO" sortable="true" width="150">备注</th>
					
				</tr>
			</thead>
		</table>
	</div>


</body>