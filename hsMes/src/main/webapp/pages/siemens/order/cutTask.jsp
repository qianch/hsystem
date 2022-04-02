<!--
	作者:高飞
	日期:2017-7-26 10:56:16
	页面:西门子裁剪任务单JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>西门子裁剪任务单</title>
  	<%@ include file="../../base/meta.jsp" %>
	<%@ include file="cutTask.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false">
		<div id="toolbar">
			<jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="enableTask" name="ids"/>
				<jsp:param value="closeTask" name="ids"/>
				<jsp:param value="exportTask" name="ids"/>
				<jsp:param value="exportCheckBarcode" name="ids"/>
				<jsp:param value="genTaskCard" name="ids"/>
				<jsp:param value="viewDrawings" name="ids"/>
				<jsp:param value="viewSuit" name="ids"/>
				
				<jsp:param value="icon-ok" name="icons"/>
				<jsp:param value="platform-close" name="icons"/>
				<jsp:param value="icon-excel" name="icons"/>
				<jsp:param value="icon-excel" name="icons"/>
				<jsp:param value="platform-icon72" name="icons"/>
				<jsp:param value="platform-icon78" name="icons"/>
				<jsp:param value="platform-icon68" name="icons"/>
				
				
				<jsp:param value="启用" name="names"/>
				<jsp:param value="关闭" name="names"/>
				<jsp:param value="导出任务单" name="names"/>
				<jsp:param value="导出小部件核对表" name="names"/>
				<jsp:param value="生成派工单" name="names"/>
				<jsp:param value="查看图纸BOM" name="names"/>
				<jsp:param value="查看组套BOM" name="names"/>
				
				
				<jsp:param value="enableTask()" name="funs"/>
				<jsp:param value="closeTask()" name="funs"/>
				<jsp:param value="exportTask()" name="funs"/>
				<jsp:param value="exportCheckBarcode()" name="funs"/>
				<jsp:param value="genTaskCard()" name="funs"/>
				<jsp:param value="viewDrawings()" name="funs"/>
				<jsp:param value="viewSuit()" name="funs"/>
			</jsp:include>
			<div id="p" class="easyui-panel" title="查询" style="width:100%;height:120px; padding:5px;background:rgb(250, 250, 250);" data-options="iconCls:'icon-search',collapsible:true,onExpand:resizeDg,onCollapse:resizeDg">
	            <form action="#" id="cutTaskSearchForm" autoSearch="true" autoSearchFunction="false">
						任务单号:<input type="text" class="easyui-textbox" like="true" name="filter[TASKCODE]">
						　订单号:<input type="text" class="easyui-textbox" like="true" name="filter[ORDERCODE]">
						　批次号:<input type="text" class="easyui-textbox" like="true" name="filter[BATCHCODE]">
						客户大类:<input type="text" class="easyui-combobox" name="filter[CONSUMERCATEGORY]" data-options="valueField:'v',textField:'t',data:[{v:'',t:'全  部'},{v:'1',t:'国内'},{v:'2',t:'国外'}],onChange:filter">
						<br>
						部件名称:<input type="text" class="easyui-textbox" like="true" name="filter[PARTNAME]">
						客户名称:<input type="text" class="easyui-textbox" like="true" name="filter[CONSUMERSIMPLENAME]">
						完成状态:<input type="text" class="easyui-combobox" name="filter[ISCOMPLETE]" data-options="valueField:'v',textField:'t',data:[{'v':'',t:'全部'},{'v':'0',t:'未完成'},{'v':'1',t:'完成'}],onChange:filter">
						<br>
						交货日期:<input type="text" class="easyui-datebox" name="filter[DELIVERYDATE_S]" data-options="onSelect:filter"> 
							　　至　 <input type="text" class="easyui-datebox" name="filter[DELIVERYDATE_E]" data-options="onSelect:filter">
						　　状态:<input type="text" class="easyui-combobox" name="filter[ISCLOSED]" data-options="valueField:'v',textField:'t',data:[{'v':'',t:'全部'},{'v':'0',t:'启用'},{'v':'1',t:'关闭'}],onChange:filter">
						　　　　<a class="easyui-linkbutton" iconcls="icon-search" onclick="filter()"> 搜索 </a>
				</form>
      	  </div>
		</div>
		<table id="dg" 
			singleSelect="false" 
			class="easyui-datagrid"  
			url="${path}siemens/cutTask/list" 
			toolbar="#toolbar"
			pagination="true" 
			rownumbers="true" 
			fitColumns="true" 
			fit="true" 
			data-options="
				rowStyler:styler,
				view: detailview,
                detailFormatter:function(index,row){
                    return '<div style=\'padding:2px;position:relative;\'><table class=\'ddv\'></table></div>';
                },
				onExpandRow: function(index,row){
                    var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
                    ddv.datagrid({
                        url:path+'siemens/cutTask/drawings?ctId='+row.ID,
                        fitColumns:true,
                        singleSelect:true,
                        rownumbers:true,
                        loadMsg:'',
                        title:'小部件条码核对',
                        height:'auto',
                        columns:[[
                        	<%--
                        	{field:'ID',checkbox:'true',width:15},
                        	{field:'FRAGMENTDRAWINGNO',title:'图号',width:15},
							{field:'FRAGMENTDRAWINGVER',title:'图纸版本',width:15},
							{field:'PRINTSORT',title:'出图顺序',width:15},--%>
                        	{field:'FARBICMODEL',title:'胚布规格',width:15},
                        	{field:'FRAGMENTCODE',title:'小部件名称',width:15},
                        	{field:'FRAGMENTNAME',title:'小部件名称',width:15},
                        	{field:'FRAGMENTCOUNTPERDRAWINGS',title:'单套数量',width:15},
                        	{field:'NEEDTOPRINTCOUNT',title:'应打数量',width:15},
							{field:'PRINTEDCOUNT',title:'已打数量',width:15},
							{field:'REPRINTCOUNT',title:'重打数量',width:15},
							{field:'EXTRAPRINTCOUNT',title:'补打数量',width:15}
                        ]],
                        onResize:function(){
                            $('#dg').datagrid('fixDetailRowHeight',index);
                        },
                        onLoadSuccess:function(){
                            setTimeout(function(){
                                $('#dg').datagrid('fixDetailRowHeight',index);
                            },0);
                        }
                    });
                    $('#dg').datagrid('fixDetailRowHeight',index);
                }
			">
			<thead>
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="ISCLOSED" sortable="true" width="10" formatter="closedFormatter">状态</th>
					<th field="TASKCODE" sortable="true" width="20">任务单编号</th>
					<th field="ORDERCODE" sortable="true" width="15">订单号</th>
					<th field="PARTNAME" sortable="true" width="15">部件名称</th>
					<!-- <th field="TASKPARTID" sortable="true" width="15">部件ID</th> -->
					<th field="BATCHCODE" sortable="true" width="15">批次号</th>
					<th field="CONSUMERSIMPLENAME" sortable="true" width="15">客户简称</th>
					<!-- <th field="TASKCONSUMERID" sortable="true" width="15">客户ID</th> -->
					<th field="CONSUMERCATEGORY" sortable="true" width="15" formatter="ccFormat">客户大类</th>
					<th field="DELIVERYDATE" sortable="true" width="15" formatter="dateFormatter">发货日期</th>
					<th field="SUITCOUNT" sortable="true" width="15">总套数</th>
					<th field="ASSIGNSUITCOUNT" sortable="true" width="15">已派工套数</th>
					<th field="PACKEDSUITCOUNT" sortable="true" width="15">已打包套数</th>
					<th field="ISCOMPLETE" sortable="true" width="15" formatter="completeFormatter">完成情况</th>
					<th field="CREATETIME" sortable="true" width="15"  formatter="dateFormatter">创建时间</th>
					<th field="CREATEUSERNAME" sortable="true" width="15">创建人</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="dlg" class="easyui-dialog" title="创建派工单" style="width:600px;height:400px;"
            data-options="
            	maximizable:true,
            	resizable:true,
            	closed:true,
                iconCls: 'icon-cut',
                buttons: '#dlg-buttons',
                modal:true
            ">
            <div class="easyui-layout" style="width:100%;height:100%;">
		        <div data-options="region:'north',border:'none'" style="height:165px">
		        	<form style="margin: 0;" id="cutTaskForm" method="post" ajax="true" action="<%=basePath %>cutTask/${empty cutTask.id ?'add':'edit'}" autocomplete="off" >
								<input type="hidden" name="ctId">
								<input type="hidden" name="taskCode">
								<input type="hidden" name="orderCode">
								<input type="hidden" name="partName">
								<input type="hidden" name="partId">
								<input type="hidden" name="batchCode">
								<input type="hidden" name="consumerSimpleName">
								<input type="hidden" name="consumerId">
								<input type="hidden" name="consumerCategory">
								<input type="hidden" name="suitCount">
								<input type="hidden" name="deliveryDate">
								<input type="hidden" name="packedSuitCount">
								<input type="hidden" name="cutPlanId">
								<input type="hidden" name="isComplete" value="0">
								<jsp:useBean id="now" class="java.util.Date" /> 
								<input type="hidden" name="createTime" value="<fmt:formatDate value="${now}" type="both" dateStyle="long" pattern="yyyy-MM-dd" />">
								<input type="hidden" name="createUserName" value="${userName }">
								<input type="hidden" name="isClosed" value="0">
								<table width="100%">
										<tr>
											<td class="title">派工单号:</td>
											<!--任务单编号-->
											<td>
												<input type="text" id="ctoCode" name="ctoCode" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
											<td class="title">任务单号:</td>
											<!--发货日期-->
											<td>
												<input type="text" id="taskCode" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
										</tr>
										<tr>
											<td class="title">订单号:</td>
											<!--订单号-->
											<td>
												<input type="text" id="orderCode" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
											<td class="title">批次号:</td>
											<!--批次号-->
											<td>
												<input type="text" id="batchCode" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
										</tr>
										<tr>
											<td class="title">客户简称:</td>
											<!--客户简称-->
											<td>
												<input type="text" id="consumerSimpleName" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
											<td class="title">客户大类:</td>
											<!--客户大类-->
											<td>
												<input type="text" id="taskConsumerCategoryX" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
										</tr>	
										<tr>
											<td class="title">部件名称:</td>
											<!--部件名称-->
											<td>
												<input type="text" id="partName" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
											<td class="title">交货日期:</td>
											<!--任务单套数-->
											<td>
												<input type="text" id="deliveryDate" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
										</tr>
										<tr>
											<td class="title">机长:</td>
											<!--部件名称-->
											<td>
												<input type="text" id="ctoGroupLeader" name="ctoGroupLeader" class="easyui-combobox" data-options="icons:[],filter:comboFilter,data:groups,textField:'GROUPLEADER',valueField:'GROUPLEADER',onHidePanel:validCode,maxPanelHeight:150,panelHeight:'auto'" editable="true" required="true" >
											</td>
											<td class="title">班组:</td>
											<!--任务单套数-->
											<td>
												<input type="text" id="ctoGroupName" name="ctoGroupName" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
										</tr>
										<tr>
											<td class="title">总套数:</td>
											<!--部件名称-->
											<td>
												<input type="text" id="suitCount" class="easyui-textbox" data-options="icons:[]" editable="false" required="true" >
											</td>
											<td class="title">派工套数:</td>
											<!--任务单套数-->
											<td>
												<input type="text" id="assignCount" name="assignSuitCount" class="easyui-numberspinner" precision="0" data-options="icons:[],min:1" editable="true" required="true" >
											</td>
										</tr>
							</table>
					</form>
		        	
		        </div>
		        <div data-options="region:'center',border:'none'">
		            <table id="drawingsDg" class="easyui-datagrid"
						width="100%"
						pagination="false" 
						rownumbers="true" 
						fitColumns="true" 
						fit="true" 
					 >
					 	<thead>
							<tr>
								<th field="partName" width="100" >部件名称</th>
								<th field="fragmentDrawingNo" width="100">图纸号</th>
								<th field="fragmentDrawingVer" width="100">图值版本</th>
								<th field="farbicRollCount" width="100">胚布卷数</th>
								<th field="X" width="100">质检确认</th>
							</tr>
						</thead>
					 </table>
		        </div>
		    </div>
    </div>
    <div id="dlg-buttons">
        <a href="javascript:void(0)" iconCls="icon-save" class="easyui-linkbutton" onclick="saveCutTaskOrder()">保存</a>
        <a href="javascript:void(0)" iconCls="icon-cancel"  class="easyui-linkbutton" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
    </div>
    <%@ include file="bomView.jsp" %>
</body>
</html>