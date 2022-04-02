<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<script>


</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" style="position: relative; height: 140px; width: 925px">
		<table id="weight_dg1234" singleSelect="true" title="" class="easyui-datagrid"   toolbar="#category_toolbar" 
		 url="<%=basePath %>planner/weavePlan/viewBjInfo?id=${id}&yx=${yx}&partname=${partname}" 
		pagination="fasle" rownumbers="true" fitColumns="false" fit="true"  >
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true></th>
					<th field="DEVCODE" sortable="true" width="60">设备代码</th>
					<!-- <th field="ISFINISHED" sortable="true" width="80" formatter="formatterIsFinish">完成状态</th>
					<th field="CLOSED" sortable="true" width="80" formatter="formatterIsClosed">关闭状态</th> -->
					<!-- <th field="SALESORDERSUBCODE" sortable="true" width="130">销售订单号</th> -->
					<th field="SALESORDERSUBCODEPRINT" sortable="true" width="180">客户订单号</th>
					<th field="BATCHCODE" sortable="true" width="100">批次号</th>
					<th field="DELEVERYDATE" sortable="true" width="90" formatter="dateFormat">出货日期</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="YX" sortable="true" width="100">叶型名称</th>
					<th field="PARTNAME" sortable="true" width="100">部件名称</th>
					<th field="DRAWNO" sortable="true" width="50">图号</th>
					<th field="ROLLNO" sortable="true" width="50" >卷号</th>
					<th field="LEVELNO" sortable="true" width="50">层数</th>
					<th field="SORTING" sortable="true" width="50">排序号</th>
					<th field="PLANCODE" sortable="true" width="160">生产计划单号</th>
					<th field="PRODUCTMODEL" sortable="true" width="150">产品规格</th>
					<th field="CONSUMERPRODUCTNAME" sortable="true" width="150">客户产品名称</th>
					<th field="PRODUCTWIDTH" sortable="true" width="80">门幅(mm)</th>
					<th field="PRODUCTLENGTH" sortable="true" width="80">卷长(m)</th>
					<th field="PRODUCTWEIGHT" sortable="true" width="80">卷重(kg)</th>
					<th field="REQCOUNT" sortable="true" width="80" formatter="planCountFormatter" >计划数量</th>
					<th field="PLANTOTALWEIGHT" sortable="true" width="80" formatter="planTotalWeightFormatter2" >计划总重量</th>
					<th width="80" field="PLANASSISTCOUNT" data-options="formatter:planAssistCountFormatter">排产辅助数量</th>
					<th field="RC" sortable="true" width="80" formatter="processFormatter3">生产进度</th>
					<th field="TC" sortable="true" width="80" formatter="totalTrayCount">打包进度</th>
					<!-- <th field="TOTALROLLCOUNT" sortable="true" width="100">总卷数</th>
					<th field="TOTALTRAYCOUNT" sortable="true" width="100">总托数</th> -->
					<th field="CONSUMERSIMPLENAME" sortable="true" width="250" formatter="formatterC">客户简称</th>
					<!-- <th field="PRODUCTTYPE" sortable="true" width="80" formatter="formatterType">产品属性</th> -->
					<th field="PROCESSBOMCODE" width="130" styler="vStyler">工艺代码</th>
					<th field="PROCESSBOMVERSION" width="80" styler="vStyler">工艺版本</th>
					<th field="PROCREQ" width="80" styler="vStyler">工艺需求</th>
					<th field="PREQ" width="130" styler="bvStyler">包装需求</th>
					<th field="BC" width="130" styler="bvStyler">包装代码</th>
					<th field="BV" width="80" styler="bvStyler">包装版本</th>
					<th field="COM" width="80" >备注</th>
					<th field="ISCOMEFROMTC" sortable="true" hidden="hidden" formatter="formatterIsFinish"></th>
					<th field="ISStAMP" sortable="true" hidden="hidden"></th>
				</tr>
			</thead>
		</table>
	</div>
</div>
