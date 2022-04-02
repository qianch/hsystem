<!--
	作者:高飞
	日期:2016-11-28 21:25:48
	页面:生产任务单JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>生产任务单</title>
  	<%@ include file="../../base/meta.jsp" %>
  	<script type="text/javascript" src="<%=basePath%>resources/jquery/jquery.PrintArea.js?_=<%=nocache%>"></script>
  	<script type="text/javascript" src="<%=basePath%>resources/jquery/jquery.qrcode.min.js?_=<%=nocache%>"></script>
	<%@ include file="producePlan.js.jsp" %>
  </head>

   <body class="easyui-layout" data-options="fit:true,border:false" >
   <div id="toolbar2">
			<jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="doClose" name="ids"/>
				<jsp:param value="cancelClose" name="ids"/>
				<jsp:param value="icon-cancel" name="icons"/>
				<jsp:param value="icon-ok" name="icons"/>
				<jsp:param value="关闭" name="names"/>
				<jsp:param value="取消关闭" name="names"/>
				<jsp:param value="doClose()" name="funs"/>
				<jsp:param value="cancelClose()" name="funs"/>
			</jsp:include>
			<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'platform-folder24'" onClick="turnBagContent()">翻包领料</a>
			<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'" onClick="editReqDetails(2)">查看包装与工艺要求</a>
			<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'platform-plugin'" onClick="_loadTask()">设置包装任务</a>
		</div>
   <div data-options="region:'south',border:false,split:true" height="200px" title="任务单内容" >
   		<table  id="producePlanDetails"
			singleSelect="false"
			class="easyui-datagrid"
			toolbar="#toolbar2"
			fitColumns="false"
			height="100%"
			width="100%"
			data-options="	rowStyler:rowStyler,
							view: detailview,
							onLoadSuccess:function(data){
								var rows=data.rows;
								for(var i=0;i<rows.length;i++){
									if(rows[i].PRODUCTISTC==1){
										$(this).datagrid('expandRow',i);
									}
								}
							},
			                detailFormatter:function(index,row){
			                    return '<div style=\'padding:2px\'><table class=\'ddv\'></table></div>';
			                },
			                onExpandRow: function(index,row){
			                	if(row.PRODUCTISTC!=1){
			                		return;
			                	}
			                    var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
			                    ddv.datagrid({
			                        url:path+'bom/plan/tc/ver/parts/' + row.FROMSALESORDERDETAILID + '/'+row.ID,
			                        fitColumns:true,
			                        singleSelect:true,
			                        rownumbers:true,
			                        width:300,
			                        loadMsg:'',
			                        rowStyler:danxiangbu,
			                        height:'auto',
			                        columns:[ [ {
										field : 'partName',
										title : '部件名称',
										width : 20
									}, {
										field : 'planPartCount',
										title : '计划数量',
										width : 15
									},{
										field : 'partCount',
										title : '订单数量',
										width : 15
									},  {
										field : 'partBomCount',
										title : 'BOM数量',
										width : 15
									} ] ],
			                        onResize:function(){
			                            $('#producePlanDetails').datagrid('fixDetailRowHeight',index);
			                        },
			                        onLoadSuccess:function(){
			                            setTimeout(function(){
			                                $('#producePlanDetails').datagrid('fixDetailRowHeight',index);
			                            },0);
			                        }
			                    });
			                    $('#producePlanDetails').datagrid('fixDetailRowHeight',index);
			                }
							" >
			<thead>
				<tr>
					<!-- 这是生产计划明细ID -->
					<th field="FROMSALESORDERDETAILID" checkbox="true"></th>
					<th field="ISTURNBAGPLAN" width="60">计划类型</th>
					<th field="SALESORDERCODE" width="130" >订单号</th>
					<th field="SALESORDERSUBCODEPRINT" width="100" >客户订单号</th>
					<th field="BATCHCODE" width="100" editor="{type:'textbox',options:{required:true}}" >批次号</th>
					<th field="CATEGORYCODE" width="100" >成品类别代码</th>
					<th field="CATEGORYNAME" width="130" >成品类别名称</th>
					<th field="PRODUCTMODEL" width="130" >产品规格</th>
					<th field="CONSUMERPRODUCTNAME" width="130" >客户产品名称</th>
					<th field="FACTORYPRODUCTNAME" width="130" >厂内名称</th>
					<th field="CONSUMERSIMPLENAME" width="100" >客户简称</th>
					<th field="ORDERCOUNT" data-options="formatter:countFormatter">订单数量</th>
					<th width="150" field="ORDERTOTALWEIGHT" formatter="planTotalWeightFormatter">分配重量/总重量</th>
					<th width="90" field="ORDERTOTALMETRES" formatter="totalMetresFormatter">订单总米长</th>
					<th field="REQUIREMENTCOUNT" data-options="formatter:countFormatter">排产数量</th>
					<th width="80" field="PLANASSISTCOUNT" data-options="formatter:planAssistCountFormatter">排产辅助数量</th>
					<th width="120" field="TOTALTRAYCOUNT" data-options="formatter:totalTrayCount">打包托数/总托数</th>
					<th field="RC" width="80" styler="processStyler" formatter="rcFormatter">生产卷数</th>
					<th field="PRODUCEDCOUNT" width="150" styler="processStyler" formatter="processFormatter3">生产进度</th>
					<th field="PRODUCTWIDTH" width="80">门幅(mm)</th>
					<th field="PRODUCTWEIGHT" width="80">卷重(kg)</th>
					<th field="PRODUCTLENGTH" width="80">卷长(m)</th>
					<th field="RESERVELENGTH" width="80">预留长度(m)</th>
					<th field="DRAWNO" width="50" >图号</th>
					<th field="ROLLNO" width="50"  >卷号</th>
					<th field="LEVELNO" width="50">层号</th>
					<th field="PROCESSBOMCODE" width="130" styler="vStyler">工艺标准代码</th>
					<th field="PROCESSBOMVERSION" width="80" formatter="bomVersionView" >工艺标准版本</th>
					<th field="BCBOMCODE" width="130" styler="bvStyler">包装标准代码</th>
					<th field="BCBOMVERSION" width="80" >包装标准版本</th>

					<!-- <th field="PACKREQ" hidden='true' width="120">包装需求</th>
                    <th field="PROCREQ" hidden='true' width="120">工艺需求</th>  -->
                    <th field="PACKREQ" width="120">包装需求</th>
                    <th field="PROCREQ" width="120">工艺需求</th>
					<!-- <th field="TOTALROLLCOUNT" width="80">总卷数</th>
					<th field="TOTALTRAYCOUNT" width="80">总托数</th> -->
					<th field="DELEVERYDATE" width="100" formatter="dateFormatter">出货日期</th>
					<th field="DEVICECODE" width="100">机台号</th>
					<!-- <th field="packReq" width="100" >包装要求</th>
					<th field="procReq" width="100" >工艺要求</th> -->
					<th field="YX" width="80" >销售订单备注</th>
					<th field="COMMENT" width="80" >订单产品备注</th>
				</tr>
			</thead>
		</table>
   </div>
	<div data-options="region:'center',border:false" style="" title="">
		<div id="toolbar">
			<jsp:include page="../../base/toolbar.jsp">
				<jsp:param value="add" name="ids"/>
				<jsp:param value="edit" name="ids"/>
				<jsp:param value="forceEdit" name="ids"/>
				<jsp:param value="delete" name="ids"/>
				<jsp:param value="commit" name="ids"/>
				<jsp:param value="view" name="ids"/>
				<jsp:param value="reload" name="ids"/>
				<jsp:param value="export" name="ids"/>
				<jsp:param value="export1" name="ids"/>
				<jsp:param value="icon-add" name="icons"/>
				<jsp:param value="icon-edit" name="icons"/>
				<jsp:param value="platform-edit2" name="icons"/>
				<jsp:param value="icon-remove" name="icons"/>
				<jsp:param value="platform-icon154" name="icons"/>
				<jsp:param value="icon-tip" name="icons"/>
				<jsp:param value="icon-remove" name="icons"/>
				<jsp:param value="platform-icon9" name="icons"/>
				<jsp:param value="platform-icon9" name="icons"/>
				<jsp:param value="增加" name="names"/>
				<jsp:param value="编辑" name="names"/>
				<jsp:param value="强制变更" name="names"/>
				<jsp:param value="删除" name="names"/>
				<jsp:param value="提交审核" name="names"/>
				<jsp:param value="查看审核" name="names"/>
				<jsp:param value="重置审核" name="names"/>
				<jsp:param value="导出" name="names"/>
			    <jsp:param value="导出套材编织明细" name="names"/>
				<jsp:param value="add()" name="funs"/>
				<jsp:param value="edit()" name="funs"/>
				<jsp:param value="forceEdit()" name="funs"/>
				<jsp:param value="doDelete()" name="funs"/>
				<jsp:param value="doAudit()" name="funs"/>
				<jsp:param value="view()" name="funs"/>
				<jsp:param value="reloadAudit()" name="funs"/>
				<jsp:param value="_export()" name="funs"/>
				<jsp:param value="_export1()" name="funs"/>
			</jsp:include>
			<div style="border-top:1px solid #DDDDDD">
				<form action="#" id="producePlanSearchForm" autoSearch="true" autoSearchFunction="false">
					任务单号：<input type="text" name="filter[condition]" like="true" class="easyui-textbox" style="width:250px;">
					车间：<input type="text" id="workShopCode" class="easyui-combobox" name="filter[workShopCode]"
							  data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=cut,weave',onSelect:filter">
				    订单号：<input type="text" name="filter[salesOrderCode]" like="true" class="easyui-textbox" style="width:150px;">
				    批次号：<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox" style="width:150px;">
		                    厂内名称：<input type="text" name="filter[factoryProductName]" like="true" class="easyui-textbox" style="width:230px;">
					客户名称：<input type="text" name="filter[consumerName]" like="true" class="easyui-textbox" style="width:230px;">
					客户简称：<input type="text" name="filter[consumerSimpleName]" like="true" class="easyui-textbox" style="width:230px;">
					<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="filter()">
						搜索
					</a>
				</form>
			</div>
		</div>
		<table id="dg" singleSelect="true" class="easyui-datagrid"   toolbar="#toolbar" pagination="true" rownumbers="true" fitColumns="true" fit="true"
			data-options="
				pageList:[10,20,30,50,100,1000],
				onDblClickRow:dbClickEdit,
				onClickRow:onClickRow,
				onLoadSuccess:onLoadSuccess">
			<thead >
				<tr>
					<th field="ID" checkbox=true ></th>
					<th field="PRODUCEPLANCODE" sortable="true" width="90">任务单号</th>
					<th field="WORKSHOPCODE" sortable="true" width="45" formatter="getWorkShopCode">车间</th>
					<th field="CREATEUSERNAME" sortable="true" width="60">创建人</th>
					<th field="CREATETIME" sortable="true" width="80" formatter="dateFormatter">创建时间</th>
					<th field="AUDITSTATE" sortable="true" width="50" formatter="autoAuditStateFormatter">审核状态</th>
					<th field="HASCREATEDCUTANDWEAVEPLAN" sortable="true" width="80" formatter="resultFormatter">生成编织、裁剪计划</th>
					<th field="CREATEFEEDBACK" sortable="true" width="180">备注</th>
				</tr>
			</thead>
		</table>
	</div>
	<%@ include file="../../packTask/pack_task_produce.jsp"%>
</body>
