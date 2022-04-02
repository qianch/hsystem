<!--
	作者:肖文彬
	日期:2016-10-18 13:38:47
	页面:编织计划JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>编织计划</title>
<%@ include file="../../base/meta.jsp"%>
<%@ include file="weavePlan.js.jsp"%>
<style type="text/css">
#sort{
	display: none;
}
</style>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<!-- <div region="west" split="true" resizable="false" title="生产计划" minWidth="200" width="200">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:false,border:false" style="/* background:red; */height:48px;text-align: center;">
				<input type="text" id="workShop" class="easyui-combobox" prompt="请选择车间"  data-options="onSelect:doSelect,data: [
		                        {value:'编织一车间',text:'编织一车间'},
		                        {value:'编织二车间',text:'编织二车间'},
		                        {value:'编织三车间',text:'编织三车间'}]">
				<input id="searchInput" type="text" class="easyui-searchbox" prompt="输入计划单号" searcher="loadProducePlans" editable="true" data-options="icons:[{iconCls:'icon-clear',handler:function(e){$(e.data.target).searchbox('clear');loadProducePlans();}}]" >
			</div>
			<div data-options="region:'center',border:false" style="overflow-x:hidden;">
				<ul id="dl" class="easyui-datalist" lines="true" style="width:100%;" valueField="ID" textField="PRODUCEPLANCODE" groupField="CREATETIME" data-options="onSelect:loadMrp">
				</ul>
			</div>
		</div>
	</div> -->
	<div data-options="region:'center',border:false"
		style="position: relative; height: 140px; width: 925px">
		
		<div id="toolbar">
			<jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="finish" name="ids"/>
				<jsp:param value="doClose" name="ids"/>
				<jsp:param value="doclosefinish" name="ids"/>
				<jsp:param value="doCancelClose" name="ids"/>
				<jsp:param value="sort" name="ids"/>
				<jsp:param value="icon-ok" name="icons"/>
				<jsp:param value="platform-close" name="icons"/>
				<jsp:param value="icon-cancel" name="icons"/>
				<jsp:param value="platform-icon80" name="icons"/>
				<jsp:param value="icon-cancel" name="icons"/>
				<jsp:param value="完成生产任务" name="names"/>
				<jsp:param value="关闭生产任务" name="names"/>
				<jsp:param value="取消完成" name="names"/>
				<jsp:param value="取消关闭" name="names"/>
				<jsp:param value="优先排序" name="names"/>
				<jsp:param value="finish()" name="funs"/>
				<jsp:param value="closeTask()" name="funs"/>
				<jsp:param value="closefinish()" name="funs"/>
				<jsp:param value="cancelClose()" name="funs"/>
				<jsp:param value="ssort()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form id="wpSearchForm" action="#" autoSearchFunction="false">
					<input type="text" id="workShop" name="filter[workShopCode]" like="false" class="easyui-combobox" prompt="请选择车间"
						   data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=weave'">
		                        <input type="text" class="easyui-textbox" prompt="生产计划单号" name="filter[code]" like="true" data-options="onChange:filter">
		           <input type="text" class="easyui-textbox" prompt="销售单号" name="filter[saleCode]" like="true" data-options="onChange:filter">
		           <input type="text" class="easyui-textbox" prompt="产品规格" name="filter[model]" like="true" data-options="onChange:filter">
		           <input type="text" class="easyui-textbox" prompt="批次号" name="filter[batchCode]" like="true" data-options="onChange:filter">
		           <input type="text" class="easyui-textbox" prompt="米长" name="filter[productLength]" like="true" data-options="onChange:filter">
		           <input type="text" class="easyui-textbox" prompt="门幅" name="filter[productWidth]" like="true" data-options="onChange:filter">
		           <input type="text" class="easyui-textbox" prompt="工艺代码" name="filter[processBomCode]" like="true" data-options="onChange:filter">
		           <input type="text" class="easyui-textbox" prompt="工艺版本" name="filter[processBomVersion]" like="true" data-options="onChange:filter">
		           <input type="text" class="easyui-textbox" prompt="客户名称" name="filter[consumerName]" like="true" data-options="onChange:filter">
		           <input type="text" id="workShop" value="-1" name="filter[isFinish]" class="easyui-combobox" prompt="已完成选项"  data-options="onSelect:filter,onChange:filter,data: [
		                       {value:'3',text:'全部'},  {value:'-1',text:'未完成'}, {value:'1',text:'已完成'}]">
		           <input type="text" id="workShop" value="0" name="filter[closed]" class="easyui-combobox" prompt="已关闭选项"  data-options="onSelect:filter,onChange:filter,data: [
		                       {value:'3',text:'全部'}, {value:'0',text:'未关闭'},{value:'1',text:'已关闭'}]">
		           <input type="text" id="isplaned" name="filter[isplaned]" class="easyui-combobox" prompt="计调分配"  
		           data-options="data:[{value:'3',text:'全部'},{value:'0',text:'未分配'},{value:'1',text:'已分配'}],onSelect:filter,onChange:filter">
				   <input type="text" id="start" name="filter[start]" value="" prompt="开始时间" class="easyui-datetimebox" data-options="onChange:filter"> 
				   <input type="text" id="end" name="filter[end]" value="" prompt="截止时间" class="easyui-datetimebox" data-options="onChange:filter">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="filter()"  iconCls="icon-search">搜索</a>
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-print" onclick="export2()">
						导出
					</a>
				</form>				
			</div>
		</div>
		<table id="dg" singleSelect="false" title=""
			class="easyui-datagrid"  toolbar="#toolbar"  rownumbers="true" fitColumns="false" pagination="true" fit="true"
			data-options="rowStyler:rowStyler,showFooter:true" >
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="ISFINISHED" sortable="true" width="80" formatter="formatterIsFinish">完成状态</th>
					<th field="CLOSED" sortable="true" width="80" formatter="formatterIsClosed">关闭状态</th>
					<th field="PLANCODE" sortable="true" width="160">生产计划单号</th>
					<th field="ISPLANED" sortable="true" width="110" formatter="isPlanedFormatter">分配状态</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="SALESORDERSUBCODE" sortable="true" width="130">销售订单号</th>
					<th field="SALESORDERSUBCODEPRINT" sortable="true" width="130">客户订单号</th>
					<th field="BATCHCODE" sortable="true" width="100">批次号</th>
					<th field="PRODUCTMODEL" sortable="true" width="150">产品规格</th>
					<!-- <th field="PRODUCTNAME" sortable="true" width="150">厂内产品名称</th> -->
					<th field="FACTORYPRODUCTNAME" sortable="true" width="150">厂内产品名称</th>
					<th field="CONSUMERPRODUCTNAME" sortable="true" width="150">客户产品名称</th>
					<th field="PRODUCTWIDTH" sortable="true" width="80">门幅(mm)</th>
					<th field="PRODUCTLENGTH" sortable="true" width="80">卷长(m)</th>
					<th field="RESERVELENGTH" sortable="true" width="80">预留长度(m)</th>
					<th field="PRODUCTWEIGHT" sortable="true" width="80">卷重(kg)</th>
					<th width="80" field="PARTNAME">部件名称</th>
					<th field="DRAWNO" width="50" >图号</th>
					<th field="ROLLNO" width="50"  >卷号</th>
					<th field="LEVELNO" width="50">层号</th>
					<th field="REQUIREMENTCOUNT" sortable="true" width="100" formatter="countFormatter">计划数量</th>
					<th field="PRODUCECOUNT" width="120" data-options="formatter:processFormatter4">生产进度</th>
					<th width="120" field="TOTALTRAYCOUNT" data-options="formatter:totalTrayCount">打包托数/总托数</th>
					<th field="PLANTOTALWEIGHT" sortable="true" width="80" formatter="planTotalWeightFormatter2">计划总重量</th>
					<!-- <th field="CONSUMERNAME" sortable="true" width="250" formatter="formatterC">客户名称</th> -->
					<th field="CONSUMERSIMPLENAME" width="250" sortable="true" formatter="formatterAddYX">客户简称</th>
					<th field="PRODUCTTYPE" sortable="true" width="80" formatter="formatterType">产品属性</th>
					<th field="DELEVERYDATE" sortable="true" width="90" formatter="orderDateFormat">出货日期</th>
					
					
					<th field="PROCESSBOMCODE" width="130" styler="vStyler">工艺代码</th>
					<th field="PROCESSBOMVERSION" width="80" >工艺版本</th>
					<th field="BCBOMCODE" width="130" styler="bvStyler">包装代码</th>
					<th field="BCBOMVERSION" width="80" >包装版本</th>
					
					<th field="PREQ" width="80" >包装要求</th>
					<th field="COM" width="80" >备注</th>
					
					<th field="ISCOMEFROMTC" sortable="true" hidden="hidden" formatter="formatterIsFinish"></th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="okEvent()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#w').dialog('close')">Close</a>
    </div>

	<div id="w" class="easyui-dialog" title="" data-options="closed:true,buttons: '#dlg-buttons',onOpen:loadData" style="width:700px;height:400px;">
		<table id="dg_view" singleSelect="false" title="" class="easyui-datagrid" rownumbers="true" fitColumns="false" fit="true" data-options="rowStyler:rowStyler,showFooter:true" >
			<thead >
				<tr>
					<th field="PLANCODE" sortable="true" width="160">生产计划单号</th>
					<th field="SALESORDERSUBCODE" sortable="true" width="130">销售订单号</th>
					<th field="REQUIREMENTCOUNT" sortable="true" width="100" formatter="countFormatter">计划数量</th>
					<th field="PRODUCECOUNT" width="120" data-options="formatter:processFormatter4">生产进度</th>
					<th width="120" field="TOTALTRAYCOUNT" data-options="formatter:totalTrayCount">打包托数/总托数</th>
				</tr>
			</thead>
		</table>
    </div>
</body>