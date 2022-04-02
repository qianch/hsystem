<!--
	作者:肖文彬
	日期:2016-11-24 11:02:50
	页面:日计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<style type="text/css">
</style>
<script type="text/javascript">
	$(function () {
		$("#dgg2").datagrid({
			url: encodeURI("<%=basePath %>planner/weavePlan/findUnCompletedWeavePlan?filter[workShopCode]=${workShopCode}&filter[isTurnBagPlan]=生产")
		})
	})
	//查询
	function _weave_select_filter() {
	    EasyUI.grid.search("dgg2","_weave_plan_select_form");
	}
	function formatterType(value, row) {
		if (value == 1) {
			return "大卷产品";
		}
		if (value == 2) {
			return "中卷产品";
		}
		if (value == 3) {
			return "小卷产品";
		}
		if (value == 4) {
			return "其他产品";
		}
	}
	
	function _weave_plan_select_search(){
		EasyUI.grid.search("dgg2","_weave_plan_select_form");
	}
	
	var enabledFilter=false;
	function enableDgFilter(){
		if(!enabledFilter){
			$("#dgg2").datagrid('enableFilter');
		}
		enabledFilter=true;
	}

	function ddg2LoadSuccess(data) {
		//enableDgFilter();
	}
	
	function isPlanedFormatter(value, row, index){
		if(value == null || value == 0){
			return "未分配";
		}else if(value==1){
			return "已分配";
		}
	}
		$("#_weave_plan_select_form").delegate(".textbox-text","keyup",function(){
			//searchboxname,textbox
			if($("input[name='"+$(this).parent().prev().attr("textboxname")+"']").length!=0){
				$("input[name='"+$(this).parent().prev().attr("textboxname")+"']").val($(this).val());
			}else{
				$("input[name='"+$(this).parent().prev().attr("searchboxname")+"']").val($(this).val());
			}
			
			clearTimeout(autoSearch);
			autoSearchFunction=$(this).parent().parent().attr("autoSearchFunction");
			if(autoSearchFunction){
				autoSearch=setTimeout(window[autoSearchFunction], 500);
			}else{
				if(typeof filter !=="undefined")
					autoSearch=setTimeout(filter, 500);
			}
		});

</script>
<div style="height:100%;">
	<!--日计划表单-->
	<div id="_weave_plan_select_">
		<form id="_weave_plan_select_form" autoSearchFunction="_weave_plan_select_search" autoSearchFunction="false">
			订&nbsp单&nbsp号:&nbsp<input type="text" class="easyui-textbox" like="true" name="filter[scode]">
			批&nbsp次&nbsp号:&nbsp<input type="text" class="easyui-textbox" like="true" name="filter[batch]">
			<!-- 分配状态:<input type="text" class="easyui-combobox" name="filter[settled]" data-options="onChange:_weave_plan_select_search,data:[{value:'0',text:'未分配'},{value:'1',text:'已分配'}]"> -->
			生产单号:<input type="text" class="easyui-textbox" like="true" name="filter[pcode]"></br>
			产品规格:<input type="text" class="easyui-textbox" like="true" name="filter[productmodel]">
			产品名称:<input type="text" class="easyui-textbox" like="true" name="filter[productname]">
			门&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp幅:<input type="text" class="easyui-textbox" like="true" name="filter[productwidth]">
			米&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp长:&nbsp<input type="text" class="easyui-textbox" like="true" name="filter[productlength]">
			<a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="_weave_select_filter()"> 搜索 </a>
		</form>
	</div>
	<table id="dgg2"  idField="ID" width="100%" singleSelect="false" class="easyui-datagrid" style="height:100%;" toolbar="#_weave_plan_select_"
			pagination="true"rownumbers="true" fitColumns="false" fit="true" remoteSort="true">
			<thead frozen="true">
				<tr>
					<th field="ID" checkbox=true></th>
					<!-- <th field="PRODUCTTYPE" sortable="true" width="100" formatter="formatterType">产品属性</th> -->
					<th field="SALESORDERSUBCODE" sortable="true" width="120">销售订单号</th>
					<th field="BATCHCODE" sortable="true" width="100">批次号</th>
					<th field="DELEVERYDATE" sortable="true" width="90" formatter="dateFormat">出货日期</th>
					<th field="DEVICECODE2" sortable="true" width="110">分配机台</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th field="PLANCODE" sortable="true" width="130">生产计划单号</th>
					<th field="PRODUCTMODEL" sortable="true" width="150">产品规格</th>
					<th field="PRODUCTNAME" sortable="true" width="150">产品名称</th>
					<th field="PARTNAME" sortable="true" width="150">部件名称</th>
					<th field="PRODUCTWIDTH" sortable="true" width="80">门幅(mm)</th>
					<th field="PRODUCTLENGTH" sortable="true" width="80">卷长(m)</th>
					<th field="PRODUCTWEIGHT" sortable="true" width="80">卷重(kg)</th>
					
					<th field="DRAWINGNO" sortable="true" width="80">图号</th>
					<th field="ROLLNO" sortable="true" width="80">卷号</th>
					<th field="LEVELNO" sortable="true" width="80">层数</th>
					<th field="REQCOUNT" sortable="true" width="80" formatter="planCountFormatter" >计划数量</th>
					<th field="PLANTOTALWEIGHT" sortable="true" width="80" formatter="totalWeightFormatter2" >计划总重量</th>
					<th width="80" field="PLANASSISTCOUNT" data-options="formatter:planAssistCountFormatter">排产辅助数量</th>
					<th field="RC" sortable="true" width="80" formatter="processFormatter3">生产进度</th>
					<th field="TC" sortable="true" width="80" formatter="totalTrayCount">打包进度</th>
					<th field="PROCESSBOMCODE" width="130" >工艺代码</th>
					<th field="PROCESSBOMVERSION" width="80">工艺版本</th>
					<th field="BCBOMCODE" width="130">包装代码</th>
					<th field="BCBOMVERSION" width="80" >包装版本</th>
					<th field="PREQ" sortable="true" width="200">包装需求</th>
					<th field="PROCREQ" sortable="true" width="200">工艺需求</th>
					<!-- <th field="PRODUCTROLLLENGTH" sortable="true" width="80">总托数</th> -->

					
					<th field="CONSUMERSIMPLENAME" sortable="true" width="200">客户简称</th>
				</tr>
			</thead>
	</table>
</div>