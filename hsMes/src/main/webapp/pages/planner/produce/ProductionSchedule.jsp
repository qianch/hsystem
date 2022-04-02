<!--
	作者:谢辉
	日期:2017-06-11 10:57:33
	页面:生产进度跟踪报表JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>生产进度跟踪报表</title>
<%@ include file="../../base/meta.jsp"%>
<%@ include file="ProductionSchedule.js.jsp"%>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="overflow: false;position: relative; height: 140px; width: 925px">
		<div id="toolbar">
			<div style="border-top:1px solid #DDDDDD">
			<div id="p" class="easyui-panel" title="查询" style="width:100%;height:180px; padding:5px;background:rgb(250, 250, 250);" data-options="iconCls:'icon-search',collapsible:true,onExpand:resizeDg,onCollapse:resizeDg">
				<form action="#" id="ProductscheFrom" autoSearchFunction="false">
					　任务单编号:<input type="text" name="filter[plancode]" like="true" class="easyui-textbox" data-options="onChange:filter">
					客户名称:<input type="text" name="filter[consumername]" like="true"  class="easyui-textbox" data-options="onChange:filter">
					　　　订单号:<input type="text" name="filter[salesordercode]" like="true"  class="easyui-textbox" data-options="onChange:filter">
					　　车间:<input type="text" id="workShop" class="easyui-combobox" name="filter[workshop]"
								data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=weave,cut'">
					&nbsp;&nbsp;&nbsp;&nbsp;
					<br>
					客户产品名称:<input type="text" name="filter[consumerproductname]"  like="true"  class="easyui-textbox" data-options="onChange:filter">
					厂内名称:<input type="text" name="filter[factoryproductname]"  like="true"  class="easyui-textbox" data-options="onChange:filter">
					　　　批次号:<input type="text" name="filter[batchcode]"  like="true"  class="easyui-textbox" data-options="onChange:filter">
					　　卷长:<input type="text" name="filter[productlength]"  like="true"  class="easyui-textbox"   data-options="onChange:filter">
					<br>
					　　工艺代码:<input type="text" name="filter[processbomcode]"  like="true"  class="easyui-textbox" data-options="onChange:filter">
					工艺版本:<input type="text" name="filter[processbomversion]"  like="true"  class="easyui-textbox" data-options="onChange:filter">
					　　关闭状态:<input type="text" name="filter[closed]"class="easyui-combobox"   data-options="valueField:'v',textField:'t',data:[{'v':'','t':'正常'},{'v':'1','t':'已关闭'}],onSelect:filter">
					　　门幅:<input type="text" name="filter[productwidth]"  like="true"  class="easyui-textbox" data-options="onChange:filter">
					<br>
					　　包装代码:<input type="text" name="filter[bcbomcode]"  like="true"  class="easyui-textbox" data-options="onChange:filter">
					包装版本:<input type="text" name="filter[bcbomversion]"  like="true"  class="easyui-textbox" data-options="onChange:filter">
					　出口(内 销):<input type="text" name="filter[salesorderisexport]"class="easyui-combobox"   data-options="valueField:'v',textField:'t',data:[{'v':'1','t':'内销'},{'v':'0','t':'外销'}],onSelect:filter">
				       计划类型:<input type="text" name="filter[isTurnBagPlan]"class="easyui-combobox"   data-options="valueField:'v',textField:'t',data:[{'v':'生产','t':'生产'},{'v':'翻包','t':'翻包'}],onSelect:filter"> 
					 
					<br>
					计调下单时间:<input type="text" style="width:173px;" class="easyui-datebox" id="start" name="filter[start]" data-options="icons:[]">
				
                                        　　　至:<input type="text" style="width:173px;" class="easyui-datebox" id="end" name="filter[end]" data-options="icons:[]">
					
					　　出货时间:<input type="text" style="width:173px;" class="easyui-datebox" id="sstart" name="filter[sstart]" data-options="icons:[]">
					　　　至:<input type="text" style="width:173px;" class="easyui-datebox" id="send" name="filter[send]" data-options="icons:[]">
					&nbsp;&nbsp;&nbsp;&nbsp;<br>
					　　销售人员:<input type="text" name="filter[username_s]"  like="true"  class="easyui-textbox" data-options="onChange:filter">
					计调人员:<input type="text" name="filter[username_p]"  like="true"  class="easyui-textbox" data-options="onChange:filter">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;生产状态:<input type="text" name="filter[prodStatus]"class="easyui-combobox"   data-options="valueField:'v',textField:'t',data:[{'v':'0','t':'未生产'},{'v':'1','t':'生产中'},{'v':'2','t':'已完成'}],onSelect:filter">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机台:<input type="text" name="filter[devicecode]"  like="true"  class="easyui-textbox" data-options="onChange:filter">
					<a href="javascript:void(0)"
					class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
					onclick="filter()"> 搜索 </a>
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-print" onclick="new_window = window.open(); export1();">导出</a>
				</form>
				</div>
			</div>
		</div>
		<table id="dg" singleSelect="false" title="" class="easyui-datagrid"
			 toolbar="#toolbar"
			pagination="true" rownumbers="true" fitColumns="false" fit="true" remoteSort="true"
			data-options="rowStyler:rowStyler,showFooter:true,pageList:[15,30,100,500],onBeforeLoad:setTotal,onLoadSuccess:onLoadSuccess">
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="PRODUCEPLANCODE" sortable="true" width="135">任务单编号</th>
					<th field="WORKSHOP" sortable="true" width="80">车间</th>
					<th field="CONSUMERSIMPLENAME" sortable="true" width="100">客户名称</th>
					<th field="CATEGORYCODE" sortable="true" width="100">产品类别代码</th>
					<th field="CATEGORYNAME" sortable="true" width="80">产品大类</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="SALESORDERSUBCODE" sortable="true" width="100">订单号</th>
					<th field="ISTURNBAGPLAN" sortable="true" width="100">计划类型</th>
					<th field="BATCHCODE" sortable="true" width="130" >批次号</th>
					<th field="CONSUMERPRODUCTNAME" sortable="true" width="130">客户产品名称</th>
					<th field="FACTORYPRODUCTNAME" sortable="true" width="130" >厂内名称</th>
					<th field="PARTNAME" sortable="true" width="110">部件名称</th>
					<th field="PRODUCTWIDTH" sortable="true" width="80">产品门幅(mm)</th>
					<th field="PRODUCTWEIGHT" sortable="true" width="60" >卷重(kg)</th>
					<th field="PRODUCTLENGTH" sortable="true" width="60" >卷长(m)</th>
					<th field="CLOSED" sortable="true" width="70" formatter="closedFormatter">关闭状态</th>
                    <th field="PRODUCTCOMPLETE"  width="80" formatter="ProductCompleteFormatter">生产完成状态</th>
                    <th field=_YUJING  width="90" >交期预警</th>
					<th field="PLANTOTALWEIGHT" sortable="true" width="125"><font color=blue>计划重量</font></th>
					<th field="REALTOTALWEIGHT" width="90" ><font color=blue>已生产重量</font></th>
					<th field="UNCOMPLETEWEIGHT" width="90"><font color=blue>未完成重量</font></th>
					<th field="UNIT" sortable="true" width="70">数量单位→</th>
					<th field="REQUIREMENTCOUNT" sortable="true" width="70"><font color=red>计划数量</font></th>
					<th field="REALROLLCOUNT" width="90"><font color=red>已生产数</font></th>
					<th field="UNCOMPLETEROLLCOUNT" width="90"><font color=red>未完成数</font></th>
					<th field="PRODSTATUS" width="90"><font color=red>生产状态</font></th>
					<!-- <th field="REQUIREMENTCOUNT" sortable="true" width="70" formatter="countFormatter">辅助数量</th> -->
					<th field="TOTALTRAYCOUNT" width="90">计划托数</th>
					<th field="REALPALLETCOUNT" width="90">打包托数</th>
					<th field="INBANKPALLETCOUNT"  width="90">入库托数</th>
					<th field="INBANKPALLETWEIGHT" width="90" formatter="weightFormatter">入库重量</th>
					<th field="STOCKPALLETCOUNT" width="90">在库托数</th>
					<th field="STOCKPALLETWEIGHT" width="90" formatter="weightFormatter">在库重量</th>
					<th field="DELIVERYPALLETCOUNT"  width="90" formatter="StockinFormat">已发托数</th>
					<th field="DELIVERYPALLETWEIGHT" width="90" formatter="weightFormatter">已发货重量</th>
					<th field="DELEVERYDATE" sortable="true" width="90" formatter="dataFormatter">出货时间</th>
					<th field="CREATETIME" sortable="true" width="150">计调下单时间</th>
					<th field="DEVICECODE" sortable="true" width="90">计调安排的机台</th>
					<th field="_DEVICECODE"  width="90">实际生产机台</th>
					<th field="PROCESSBOMCODE" sortable="true" width="150">工艺代码</th>
					<th field="PROCESSBOMVERSION" sortable="true" width="90">工艺版本</th>
					<th field="BCBOMCODE" sortable="true" width="150">包装代码</th>
					<th field="BCBOMVERSION" sortable="true" width="90">包装版本</th>
					<th field="PACKREQ" sortable="true" width="90">包装要求</th>
					<th field="SALESORDERISEXPORT" sortable="true" width="90" data-options="formatter:exportFormat">出口(内销)</th>
					<th field="PLANBIZUSERNAME" sortable="true" width="90">计调员</th>
					<th field="ORDERBIZUSERNAME" sortable="true" width="90">销售员</th>
				</tr>
			</thead>
		</table>
	</div>
</body>