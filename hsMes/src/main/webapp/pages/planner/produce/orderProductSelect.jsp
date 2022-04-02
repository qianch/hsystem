<!--
	作者:徐波
	日期:2016-10-18 13:09:34
	页面:生产计划明细增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<style type="text/css">
	font{
	    font-family: 微软雅黑;
    	color: #c31111;
    }
   		@keyframes changed {
			from {color:white;background: #3669c1;}
			to {color:white;background: #82a8e8;}
		}
</style>
<script type="text/javascript">
function unAllocateCount(value,row,index){
	value=row.PRODUCTCOUNT-row.ALLOCATECOUNT;
	return value;
}
function alloCate(){
	var isCheck=$('#isAlloCate').is(':checked');
	if(isCheck){
		$("#isCheck").val(1);
		reloadProduceOrder();
	}else{
		$("#isCheck").val(0);
		reloadProduceOrder();
		$('#orderProductSelect').datagrid('reload');
	}
}
function editTimesFormatter(value,index,row){
	return (isEmpty(value)?0:value)+"次";
}

function detailRowStyler(index, row){
	var style="";
	if(isEmpty(row.CLOSED)||row.CLOSED==0){
	}else{
		style+="text-decoration:line-through;";
	}
	if(row.EDITTIMES!=null&&row.EDITTIMES>0){
		style+="animation: changed 1s infinite ease-in-out;animation-direction: alternate;";
	}
	if (row.PRODUCTISTC == 1) {
		style+="background:rgba(0, 255, 80, 0.31);";
	} else if(row.PRODUCTISTC==2) {
		return "";
	}else if(row.PRODUCTISTC==-1){
		style+="background:rgba(0, 183, 255, 0.58);";
	}else{
		return "";
	}
	return style;
}
function isPlanedFormatter(value, row, index){
	if(!row.ALLOCATECOUNT||row.ALLOCATECOUNT==0){
		return "未分配";
	}else if(row.ALLOCATECOUNT>0&&row.ALLOCATECOUNT<row.TOTALWEIGHT){
		return "部分已分配";
	}else{
		return "全部已分配";
	}
}
enableFilterd=false;
</script>
<!-- <div id="produceSalesOrderSearchToolbar">
		<form id="produceSalesOrderForm" autoSearchFunction="false">
			<input type="text" like="true" class="easyui-searchbox" editable="true" style="width:400px" name="filter[condition]" data-options="searcher:reloadProduceOrder" prompt="请输入订单号或者产品型号">
			<input type="hidden" id="_consumerName_salesOrder_detail" name="filter[consumerName]" value="">
			<input type="hidden" id="isCheck" name="filter[isCheck]" value="">
			<label for="isAlloCate" style="vertical-align:middle; " ><input  type="checkbox"  onchange="alloCate()" id="isAlloCate" style="vertical-align:middle;font-size: 10px">未完成分配 </label>
		</form>
	</div> -->
	<table width="100%" id="orderProductSelect" idField="ID"  singleSelect="false" title="" class="easyui-datagrid"  
			url="${path}planner/produce/order/list3?workShopCode=${workShopCode}"
			fitColumns="false" 
			rowStyler="detailRowStyler"
			fit="true" 
			pagination="true"
			data-options="
				onBeforeLoad:orderProductSelectLoadSuccess,
				rowStyler:rowStyler,
				remoteFilter:true,
			<%--	onBeforeLoad:function(){
					if(typeof onBerforeLoad === 'function'){
					return onBerforeLoad();
					}
				},--%>
				onDblClickRow:onOrderProductSelectDblClickRow
			" >
			<thead frozen="true">
				<tr>
					<!-- 这是生产计划明细ID -->
					<th field="ID" checkbox=true ></th>
					<!-- <th field="EDITTIMES" formatter="editTimesFormatter">变更次数</th> -->
					<th field="SALESORDERSUBCODE" sortable="true" width="110">订单号</th>
					<th field="PRODUCTBATCHCODE" sortable="true" width="110">批次号</th>
					<th field="SALESORDERSUBCODEPRINT" sortable="true" width="110">客户订单号</th>
					<th field="ISPLANED" sortable="true" width="110" formatter="isPlanedFormatter">分配状态</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="PRODUCTCOUNT" data-options="formatter:countFormatter,styler:processStyler">数量</th>
					<th width="80" field="TOTALWEIGHT" formatter="totalWeightFormatter">已分配重量/总重量</th>
					<th width="80" field="TOTALMETRES" formatter="totalMetresFormatter">总米长</th>
					<!-- <th field="ALLOCATECOUNT" width="120" data-options="formatter:countFormatter,styler:processStyler">分配数量</th> -->
					<th field="RC" width="120" data-options="formatter:rcFormatter">生产卷数</th>
					<th field="TC" width="120" data-options="formatter:tcFormatter">打包托数</th>
					<th field="PRODUCECOUNT" width="120" data-options="formatter:processFormatter2">生产进度</th>
					<th field="CONSUMERNAME" width="200">客户名称</th>
					<th field="CONSUMERPRODUCTNAME" width="200">客户产品名称</th>
					<th field="FACTORYPRODUCTNAME" width="200">厂内名称</th>
					<th field="PRODUCTMODEL" width="80">产品型号</th>
					<th field="PRODUCTWIDTH" width="80">门幅(mm)</th>
					<th field="PRODUCTROLLLENGTH" width="80">卷长(m)</th>
					<th field="PRODUCTROLLWEIGHT" width="80">卷重(kg)</th>
					<th field="DRAWNO" width="50" >图号</th>
					<th field="ROLLNO" width="50"  >卷号</th>
					<th field="LEVELNO" width="50">层号</th>
					<th field="SALESORDERDATE" sortable="true" width="80" data-options="formatter:orderDateFormat">下单日期</th>
					<th field="USERNAME" width="80">业务员</th>
					<!-- <th field="SALESORDERISEXPORT" width="80" data-options="formatter:exportFormat">内销/外销</th>
					<th field="SALESORDERTYPE" width="80" data-options="formatter:orderTypeFormat">订单类型</th> -->
					<!-- <th field="SALESORDERTOTALMONEY" width="80" >订单总金额</th> -->
					<!-- <th field="SALESORDERDELIVERYTIME"  width="80" data-options="formatter:orderDateFormat">发货时间</th> -->
					<th field="PRODUCTPROCESSCODE" width="130" styler="vStyler">工艺标准代码</th>
					<th field="PRODUCTPROCESSBOMVERSION" width="80" >工艺标准版本</th>
					<th field="PRODUCTPACKAGINGCODE" width="130" styler="bvStyler">包装标准代码</th>
					<th field="PRODUCTPACKAGEVERSION" width="80" >包装标准版本</th>
					<!-- <th field="PRODUCTROLLCODE" width="80">卷标签代码</th>
					<th field="PRODUCTBOXCODE" width="80">箱唛头代码</th>
					<th field="PRODUCTTRAYCODE" width="80">托唛头代码</th> -->
					<th field="DELIVERYTIME" width="80" formatter="orderDateFormat">发货时间</th>
					<th field="SALESORDERMEMO" width="80">销售订单备注</th>
					<th field="PRODUCTMEMO" width="80">订单产品备注</th>
				</tr>
			</thead>
</table>