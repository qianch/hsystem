<!--
	作者:宋黎明
	日期:2016-10-27 10:06:42
	页面:查看审核后页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../base/meta.jsp"%>
<script type="text/javascript" src="<%=basePath%>resources/jquery/jquery.PrintArea.js?_=<%=nocache%>"></script>
<script type="text/javascript" src="<%=basePath%>resources/jquery/jquery.qrcode.min.js?_=<%=nocache%>"></script>
<style type="text/css">
/* CSS 代码 */
</style>
<script type="text/javascript">
	var details = ${empty details?"[]":details};

	function dateFormatter(value, row, index) {
		if (value == undefined)
			return "";
		return value.substring(0, 10);
	}
	function productNameFormatter(value, row, index) {
		//		return row.factoryProductName+"-"+row.productWidth+"mm";
		return row.factoryProductName;
	}
	var add_req_detailUrl = path + "selector/addReq";
	function viewReqDetails() {
		var r = EasyUI.grid.getOnlyOneSelected("produceProducts");
		_innerParam = r;
		var wid1 = Dialog.open("查看", 500, 400, add_req_detailUrl + "?productId=" + r.PRODUCTID, [ EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid1);
		}) ], function() {
			$("#" + wid1).dialog("maximize");
			$('#packReq').val(_innerParam.PACKREQ);
			$('#procReq').val(_innerParam.PROCREQ);
		});
	}
	/* JS代码 */
	$(function() {
		$("#produceProducts").datagrid("loadData", details);
	});
	
	var turnBagContentUrl=path + "planner/tbp/add";
	var fbWinId;
	/**
	 * 设置翻包领料单
	 */
	function turnBagContent(){
		var r=EasyUI.grid.getOnlyOneSelected("produceProducts");
		if(r.ISTURNBAGPLAN=="生产"){
			Tip.warn("无翻包领料单");
			return;
		}
		var buttons=[];
		
		buttons.push(
				EasyUI.window.button("icon-cancel", "关闭", function() {
					Dialog.close(fbWinId)
				}) 
		);
		
		fbWinId = Dialog.open("翻包领料", 600, 500, turnBagContentUrl+"?id="+r.ID, buttons, function() {
			setTimeout(function(){
				var r = EasyUI.grid.getOnlyOneSelected("produceProducts");
				$("#_producePlanDetails").datagrid({
					data : [ r ]
				});
			}, 0)
			Dialog.max(fbWinId);
		});
	}
	
	function _loadTask(){
        var row=EasyUI.grid.getOnlyOneSelected("produceProducts");
        loadTask(row.ID,row.FROMSALESORDERDETAILID);
    }
</script>
<body>
	<div style="height:98%">
		<!--生产任务单表单-->
		<form id="producePlanForm" method="post" ajax="true" action="<%=basePath %>planner/produce/${empty producePlan.id ?'add':'edit'}" autocomplete="off">

			<input type="hidden" name="id" value="${producePlan.id}" />

			<table width="100%">
				<tr style="font-size:14px;height:35px;">
					<td class="title">生产任务单号&nbsp;</td>
					<!--生产任务单号-->
					<td>&nbsp;${pp.producePlanCode}</td>
					<td class="title">车间&nbsp;</td>
					<!--生产任务单号-->
					<td>&nbsp;${pp.workshop}</td>
				</tr>
			</table>
		</form>
		<div id="produceProducts_toolbar">
			<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'platform-folder24'" onClick="turnBagContent()">翻包领料</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onclick="viewReqDetails()">查看 工艺/包装 需求</a>
			<a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'platform-plugin'" onClick="_loadTask()">查看包装任务</a>
		</div>
		<table id="produceProducts" singleSelect="true" class="easyui-datagrid" fitColumns="false" height="300px" width="100%" toolbar="#produceProducts_toolbar"
			data-options="	view: detailview,
			                detailFormatter:function(index,row){
			                    return '<div style=\'padding:2px\'><table class=\'ddv\'></table></div>';
			                },
			                onLoadSuccess:function(data){
			                	var rows=data.rows;
								for(var i=0;i<rows.length;i++){
									$(this).datagrid('expandRow',i);
								}
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
			                            $('#produceProducts').datagrid('fixDetailRowHeight',index);
			                        },
			                        onLoadSuccess:function(){
			                            setTimeout(function(){
			                                $('#produceProducts').datagrid('fixDetailRowHeight',index);
			                            },0);
			                        }
			                    });
			                    $('#produceProducts').datagrid('fixDetailRowHeight',index);
			                }"
		
			>
			<thead>
				<tr>
					<!-- 这是生产计划明细ID -->
					<th field="FROMSALESORDERDETAILID" checkbox="true"></th>
					<th field="ISTURNBAGPLAN" width="50">类型</th>
					<th field="CONSUMERSIMPLENAME" width="150">客户名称</th>
					<th field="SALESORDERCODE" width="100">订单号</th>
					<th field="BATCHCODE" width="80">批次号</th>
					<th field="PRODUCTMODEL" width="120">产品规格</th>
					<th field="CONSUMERPRODUCTNAME" width="120">客户产品名称</th>
					<th field="FACTORYPRODUCTNAME" width="130">厂内产品名称</th>
					<th field="PRODUCTWIDTH" width="80">门幅(mm)</th>
					<th field="PRODUCTWEIGHT" width="80">卷重(kg)</th>
					<th field="PRODUCTLENGTH" width="80">卷长(m)</th>
					<th field="PROCESSBOMCODE" width="80">工艺代码</th>
					<th field="PROCESSBOMVERSION" width="120">工艺版本</th>
					<th field="BCBOMCODE" width="80">包装代码</th>
					<th field="BCBOMVERSION" width="120">包装版本</th>
					<th field="ORDERCOUNT" data-options="formatter:countFormatter">订单数量</th>
					<!-- <th  width="150" field="ORDERTOTALWEIGHT" formatter="planTotalWeightFormatter3">分配重量/总重量</th> -->
					<th width="150" field="ORDERTOTALWEIGHT" formatter="planTotalWeightFormatter">分配重量/总重量</th>
					<th width="90" field="ORDERTOTALMETRES" formatter="totalMetresFormatter">订单总米长</th>
					<th field="REQUIREMENTCOUNT" data-options="formatter:countFormatter">排产数量</th>
					<th width="120" field="TOTALTRAYCOUNT" data-options="formatter:totalTrayCount">打包托数/总托数</th>
					<th width="80" field="PLANASSISTCOUNT" data-options="formatter:planAssistCountFormatter">排产辅助数量</th>
					<th field="DELEVERYDATE" width="100" formatter="dateFormatter">出货日期</th>
					<th field="DEVICECODE" width="100">机台号</th>
					<th field="COMMENT" width="80">备注</th>
				</tr>
			</thead>
		</table>
	</div>
	</table>
	<%@ include file="../../packTask/pack_task_produce.jsp"%>
</body>