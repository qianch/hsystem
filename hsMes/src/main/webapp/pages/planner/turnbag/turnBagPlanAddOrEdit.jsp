<!--
	作者:高飞
	日期:2017-2-9 11:28:32
	页面:翻包计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<script type="text/javascript">
	var details = ${empty details?'[]':details};
	var _editingRow;
	var position=${empty details?'[]':position};

	function onBeforeEdit(index, row) {
		_editingRow = row;
	}

	function loadOldBatchCode(param) {
		param.orderCode = _editingRow.SALESORDERSUBCODE;
		param.productId = _editingRow.PRODUCTID;
		param.isNew = false;
	}

	function onChange(newValue, oldValue) {
		var text = $(this).combobox("getText");
		if (!isEmpty(text)) {
			var count = text.substring(text.indexOf("*") + 1).replace("托", "");
			var index = $(this).parent().parent().parent().parent().parent().parent().parent().attr("datagrid-row-index");
			var countEditor = $("#oldOrders").datagrid("getEditor", {
				index : index,
				field : 'TURNBAGCOUNT'
			});
			$(countEditor.target).numberspinner({
				max : parseInt(count)
			});
		}
	}

	function comboboxLoadSuccess(data) {
		var text = $(this).combobox("getText");
		if (!isEmpty(text)) {
			var count = text.substring(text.indexOf("*") + 1).replace("托", "");
			var index = $(this).parent().parent().parent().parent().parent().parent().parent().attr("datagrid-row-index");
			var countEditor = $("#oldOrders").datagrid("getEditor", {
				index : index,
				field : 'TURNBAGCOUNT'
			});
			var turnBagCount = $(countEditor.target).numberspinner("getValue");
			$(countEditor.target).numberspinner({
				max : parseInt(count)
			});
			if (!isEmpty(turnBagCount)) {
				$(countEditor.target).numberspinner("setValue", turnBagCount);
			}
		}
	}

	var orderWinId;

	function addOldSalesOrder() {
		orderWinId = Dialog.open("选择订单", 1000, 400, path + "/planner/tbp/order/select", [ EasyUI.window.button("icon-save", "添加", function() {
			var rs = $("#orderProductSelect").datagrid("getChecked");
			if (rs.length == 0) {
				Tip.warn("请选择订单");
				return;
			}
			/* $("#oldOrders").datagrid("appendRow",rs[0]); */
			rs[0].SALESORDERDETAILID = rs[0].ID;
			appendRow2(rs[0]);
			Dialog.close(orderWinId);
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(orderWinId);
		}) ]);
	}

	function onOrderProductSelectDblClickRow(index, row) {
		row.SALESORDERDETAILID = row.ID;
		appendRow2(row);
		Dialog.close(orderWinId);
	}

	function appendRow2(row) {
		$("#oldOrders").datagrid("appendRow", row);
		$("#oldOrders").datagrid('expandRow',$("#oldOrders").datagrid("getRows").length-1);
	}

	function deleteOldSalesOrder() {
		var rows = $("#oldOrders").datagrid("getChecked");
		if (rows.length == 0) {
			Tip.warn("请至少选择一行");
			return;
		}
		$("#oldOrders").datagrid("deleteRow", EasyUI.grid.getRowIndex("oldOrders", rows[0]));
	}

	function orderProductSelectLoadSuccess(data) {
		Loading.hide();
		$(this).datagrid("enableFilter");
		$(".datagrid-filter[name='SALESORDERTYPE']").remove();
		$(".datagrid-filter[name='SALESORDERISEXPORT']").remove();
		$(".datagrid-filter[name='SALESORDERDATE']").remove();
	}

	function loadTempData() {
		try {
			var r = EasyUI.grid.getOnlyOneSelected("producePlanDetails");
			$("#_producePlanDetails").datagrid({
				data : [ r ]
			});
		} catch (e) {
			console.log(e)
		}
	}
</script>
<style>

#fbContent{
	width:100%;
}
#fbContent td{
	padding:5px;
}
</style>
<div>
	<div>
		<input id="ppdId" type="hidden" value="${ppdId }">
		<table id="fbContent">
			<tr>
				<td class="title">翻包单号</td>
				<td><input type="text" style="border:none;background:white;" id="turnBagCode" value="${tbCode }" readonly></td>
				<td class="title">翻包部门</td>
				<td><input type="text" style="border:none;background:white;" id="turnBagDept" value="${tbDept }" readonly></td>
				<td class="title">二维码</td>
				<td>
					<div id="qr_code"></div>
				</td>
			</tr>
		</table>
	</div>
	<table id="_producePlanDetails" title="翻包订单" singleSelect="true" fitColumns="false" width="100%" >
		<thead>
			<tr>
				<th field="SALESORDERCODE" width="130">订单号</th>
				<th field="BATCHCODE" width="100" editor="{type:'textbox',options:{required:true}}">批次号</th>
				<th field="PRODUCTMODEL" width="130">产品规格</th>
				<th field="CONSUMERPRODUCTNAME" width="130">客户产品名称</th>
				<th field="FACTORYPRODUCTNAME" width="130">厂内名称</th>
				<th field="CONSUMERSIMPLENAME" width="100">客户简称</th>
				<th field="REQUIREMENTCOUNT" data-options="formatter:countFormatter" width="100">翻包数量</th>
				 <th width="120" field="TOTALTRAYCOUNT" data-options="formatter:totalTrayCount">翻包托数/总托数</th> 
				<th field="PRODUCTWIDTH" width="80">门幅(mm)</th>
				<th field="PRODUCTWEIGHT" width="80">卷重(kg)</th>
				<th field="PRODUCTLENGTH" width="80">卷长(m)</th>
			</tr>
		</thead>
	</table>
	<script type="text/javascript">
		setTimeout(function() {
			loadTempData();
		}, 0)
	</script>
	<div id="toolbar_product">
		<a class="easyui-linkbutton" plain="true" iconCls="icon-add" onclick="addOldSalesOrder()">增加</a> <a class="easyui-linkbutton" plain="true" iconCls="icon-remove" onclick="deleteOldSalesOrder()">删除</a>
	</div>
	<table id="oldOrders" title="领料单" singleSelect="true" class="easyui-datagrid" fitColumns="false" fit="false" style="width:100%;" toolbar="#toolbar_product" 
		data-options="
			onClickRow:function(index,row){$(this).datagrid('beginEdit',index);},
			onBeforeEdit:onBeforeEdit,
			data:details,
			view : detailview,
			onLoadSuccess:function(data){
				var rows=data.rows;
				for(var i=0;i<rows.length;i++){
					$(this).datagrid('expandRow',i);
					setTimeout(function() {
									$('#oldOrders').datagrid('fixDetailRowHeight', i);
					}, 0);
				}
			},
			detailFormatter:function(index,row){
				return '<div style=\'padding:2px\'><table class=\'ddv\'></table></div>';
			},
			onExpandRow : function(index, row) {
				if(row.productIsTc==1){
					Tip.warn('套材产品无法翻包');
					return;
				}
				var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
				var ppdId=$('#ppdId').val();
				JQ.ajaxPost(path+'planner/tbp/batchInfo?fromProducePlanDetailId='+row.FROMPRODUCEPLANDETAILID+'&targetProducePlanDetailId='+ppdId,{},function(data){
						ddv.datagrid({
							data:[data],
							fitColumns : false,
							rownumbers : true,
							fit:false,
							width : '100%',
							columns : [ [ {
								field : 'REQUIREMENTCOUNT',
								title : '订单卷数',
								width : 60
							}, {
								field : 'TOTALTRAYCOUNT',
								title : '订单托数',
								width : 60
							}, {
								field : 'PLANTOTALWEIGHT',
								title : '订单重量',
								width : 60
							} , {
								field : 'INCOUNT',
								title : '在库数量',
								width : 90,
								formatter:function(v,r,i){
									return v+'托';
								}
							} , {
								field : 'INCOUNTGROUP',
								title : '在库详情',
								width : 100
							} , {
								field : 'NOTINCOUNT',
								title : '未入库',
								width : 60,
								formatter:function(v,r,i){
									return v+'托';
								}
							} , {
								field : 'NOTINCOUNTGROUP',
								title : '未入库详情',
								width : 100
							}, {
								field : 'HISTORY',
								title : '历史翻包数量',
								width : 90,
								formatter:function(v,r,i){
									return (v||0)+'托';
								}
							},
							{
								field : 'X',
								title : '可用翻包数量',
								width : 80,
								formatter:function(v,r,i){
									return r.TOTALTRAYCOUNT-(r.HISTORY||0);
								}
							},
							{
								field : 'MEMO',
								title : '历史备注',
								width : 100
							}
							
							] ],
							onResize : function() {
								setTimeout(function() {
									$('#oldOrders').datagrid('fixDetailRowHeight', index);
								}, 0);
							},
							onLoadSuccess : function() {
								setTimeout(function() {
									$('#oldOrders').datagrid('fixDetailRowHeight', index);
								}, 100);
							}
						});
						$('#oldOrders').datagrid('fixDetailRowHeight',index);
						$('#oldOrders').datagrid('resize',{});
					},function(data){
						Loading.hide();
					});
			}
			">
		<thead>
			<tr>
				<th field="PID" checkbox=true></th>
				<th width="120" field="SALESORDERSUBCODE">订单号</th>
				<th width="100" field="BATCHCODE" <%-- editor="{type:'combobox',options:{'required':true,'icons':[],onBeforeLoad:loadOldBatchCode,onLoadSuccess:comboboxLoadSuccess,url:'${path }planner/tbp/batchCode',valueField:'v',textField:'t'}}" --%>>翻包批次</th>
				<th width="100" field="TAKEOUTCOUNTFROMWAREHOUSE" editor="{type:'numberspinner',options:{min:0,max:9999,'required':true,width:'100%','icons':[],precision:0}}">仓库领出数量(托)</th>
				<th width="80" field="TURNBAGCOUNT" editor="{type:'numberspinner',options:{min:1,max:9999,'required':true,width:'100%','icons':[],precision:0}}">翻包数量(托)</th>
				<th width="150" field="MEMO" editor="{type:'textbox',options:{'required':false,width:'100%','icons':[]}}">备注</th>
				<th field="CONSUMERSIMPLENAME" width="100">客户简称</th>
				<th width="100" field="CONSUMERPRODUCTNAME">客户产品</th>
				<th width=150 field="FACTORYPRODUCTNAME">厂内名称</th>
				<th width="150" field="PRODUCTMODEL">产品型号</th>
				<th field="PRODUCTWIDTH" width="80">门幅(mm)</th>
				<th field="PRODUCTROLLLENGTH" width="80">卷长(m)</th>
				<th field="PRODUCTROLLWEIGHT" width="80">卷重(kg)</th>
				<th field="PRODUCTPROCESSCODE" width="140" styler="vStyler">工艺代码</th>
				<th field="PRODUCTPROCESSBOMVERSION" width="80">工艺版本</th>
				<th field="PRODUCTPACKAGINGCODE" width="140" styler="bvStyler" formatter="packBomView">包装代码</th>
				<th field="PRODUCTPACKAGEVERSION" width="80">包装版本</th>
				<th field="PACKREQ" width="120">包装要求</th>
			</tr>
		</thead>
	</table>
	<table id="position"></table>
</div>

<script type="text/javascript">
//查看包装bom明细
function packBomView(value, row, index) {
	if (value == null) {
		return "";
	} else if(value=="无包装"){
		return "";
	}else if(row.PRODUCTPACKAGEVERSION==null||row.PRODUCTPACKAGEVERSION==""){
		return "";
	}else {
		return "<a href='#' title='" + value + "' class='easyui-tooltip' onclick='_packBomView(" + row.PACKBOMID + ")'>" + value + "</a>"
	}
}

var BOM_VIEW_ID;
function _packBomView(packBomId) {
	if(packBomId==null||packBomId==""){
		Tip.error("包装工艺错误，请重新编辑产品");
		return;
	}
	var viewUrl = path + "selector/view/bc?packBomId=" + packBomId;
	BOM_VIEW_ID = Dialog.open("查看包装bom明细", 700, 400, viewUrl, [ EasyUI.window.button("icon-cancel", "关闭", function() {
		Dialog.close(BOM_VIEW_ID);
	}) ], function() {
		$("#" + BOM_VIEW_ID).dialog("maximize");
		for (var a = 0; a < details.length; a++) {
			_common_bcBomDetail_data(details[a]);
		}
	});
};

	$("#qr_code").qrcode({
		render : "img",
		width : 80,
		height : 80,
		text : $("#turnBagCode").val()
	});
</script>