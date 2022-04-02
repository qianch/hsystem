<!--
	作者:肖文彬
	日期:2016-10-18 13:38:47
	页面:编织计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<style type="text/css">
textarea {
	height: 50px;
	resize: none;
	padding: 2px;
	border: none;
}
</style>
<script type="text/javascript">
	var deviceDgData = ${empty deviceDg?"[]":deviceDg};
</script>
<div>
	<!--编织计划表单-->
	<form id="weavePlanForm" method="post" ajax="true" action="<%=basePath %>weavePlan/${empty weavePlan.id ?'add':'edit'}" autocomplete="off">

		<input type="hidden" name="id" value="${weavePlan.id}" />
		<table width="100%">
			<tr>
				<td class="title">车间:</td>
					<!--车间-->
					<td>
						<input type="text" id="workShop" name="workShop" value="${weaveDailyPlan.workShop}" ${empty weaveDailyPlan.workShop?"":"readonly"} class="easyui-combobox"
							   required="true"
							   data-options="valueField:'v',textField:'t',url:'<%=basePath %>department/queryDepartmentByType?type=weave'">
					</td>
				<td class="title">产品属性:</td>
				<!--产品属性-->
				<td><input type="text" id="productType" name="productType" value="${weavePlan.productType}" class="easyui-combobox" required="true"
					data-options="data: [
		                        {value:'1',text:'大卷产品'},
		                        {value:'2',text:'中卷产品'},
		                        {value:'3',text:'小卷产品'},
		                        {value:'4',text:'其他产品'}]"></td>
				<td class="title">总托数:</td>
				<td><input type="text" id="productTrayCount" name="totalTrayCount" value="${weavePlan.totalTrayCount}" readonly="true" precision="0" min="1" class="easyui-numberbox"></td>
			</tr>
			<tr>
				<td class="title">总卷数:</td>
				<td><input type="text" id="count" name="totalRollCount" value="${weavePlan.totalRollCount}" ${empty weavePlan.totalRollCount?"":"readonly"} min="1" class="easyui-numberbox" precision="0" required="true"></td>
				<td class="title">总重量:</td>
				<td><input type="text" id="weight" name="requirementCount" value="${weavePlan.requirementCount}" ${empty weavePlan.requirementCount?"":"readonly"} class="easyui-numberbox" min="1" required="true"></td>
				<td class="title">备注:</td>
				<td ><textarea id="_comment" name="_comment" style="width:99%;" placeholder="最多输入1000字"></textarea></td>
			</tr>
		</table>
		<!-- <div id="cal" style="padding: 5px; background: #fafafa; border-bottom: 1px solid #dddddd; text-align: center;"></div>
		<div id="deviceToolbar" class="datagrid-toolbar">
			<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="ChooseDevice()" id="addDevice" iconCls="icon-add">增加机台</a> 
			<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="deleteDevice()" id="deleteDevice" iconCls="icon-remove">移除机台</a> 
			<a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="syncDevice()" id="syncDevice" iconCls="platform-refresh1">同步机台</a>
		</div> -->
		<!-- <div class="easyui-tabs" id="deviceTabs" style="width:100%;height:auto" data-options="onBeforeClose:doCloseTab"></div> -->
		<!-- <table class="easyui-datagrid" width="100%" fitColumns="true" rownumbers="true" pagination="false" data-options="onDblClickRow:ChooseDevice">
		
		</table> -->
	</form>
</div>

<script type="text/javascript" charset="utf-8">
	/* var k = new Kalendae({
		attachTo : document.getElementById("cal"),
		months : 3,
		mode : 'multiple',
		weekStart : 1,
		selected : [ ${dates}],
		blackout:'2017-03-05',
		subscribe : {
			'date-clicked' : function(date) {
				if (!this.isSelected(date.format("YYYY-MM-DD"))) {
					addDay(date.format("YYYY-MM-DD"));
				} else {
					removeDay(date.format("YYYY-MM-DD"));
				}
			}
		}
	});

	function doCloseTab(title, index) {
		var _this=this;
		$.messager.confirm({
			title: '操作确认',
			msg: '确认删除'+title+'的计划？',
			fn: function(r){
				if(r){
					var opts = $(_this).tabs('options');
					var bc = opts.onBeforeClose;
					opts.onBeforeClose = function(){};  // allowed to close now
					$(_this).tabs('close',index);
					opts.onBeforeClose = bc;  // restore the event function
					k.removeSelected(title);
				}else{
					k.addSelected(title);
				}
			}
		});
		return false;
	}

	//删除选项卡
	function removeDay(date) {
		$('#deviceTabs').tabs('close', date);
	}
	
	//获取当前tab的datagrid的id
	function getDgId(){
		return $("#deviceTabs").tabs("getSelected").panel("options").title+"-dg";
	}
	
	function getTabCount(){
		return $("#deviceTabs").tabs("tabs").length;
	}
	
	function getTabs(){
		return $("#deviceTabs").tabs("tabs");
	}
	
	//删除设备
	function deleteDevice(){
		if(getTabCount()==0){
			Tip.warn("尚无设备分配信息");
			return;
		}
		var dg=getDgId();
		var devices=$("#"+dg).datagrid("getSelections");
		if(devices.length==0)return;
		for(var i=devices.length-1;i>-1;i--){
			$("#"+dg).datagrid("deleteRow",i);
		}
	}
	
	//同步设备
	function syncDevice(){
		if(getTabCount()==0){
			Tip.warn("尚无设备分配信息");
			return;
		}
		var sourceTab=$("#deviceTabs").tabs("getSelected").panel("options").title;
		
		Dialog.confirm(function(){
			var tabs=$("#deviceTabs").tabs("tabs");
			var data=$("#"+sourceTab+"-dg").datagrid("getData").rows;
			
			//先结束编辑
			for(var j=0;j<data.length;j++){
				$("#"+sourceTab+"-dg").datagrid("endEdit",j);
			}
			
			for(var i=0;i<tabs.length;i++){
				if(sourceTab==tabs[i].panel("options").title){
					continue;
				}
				$("#"+tabs[i].panel("options").title+"-dg").datagrid("loadData",$("#"+sourceTab+"-dg").datagrid("getData"));
			}
			
		},"同步"+sourceTab+"的机台至其他日期?");
	}
	
	var deviceWindow;
	function ChooseDevice() {
		/* if(getTabCount()==0){
			Tip.warn("请选择日期");
			return;
		}
		var dg=getDgId();
		var row={};
		var repeat=true; */
		deviceWindow = Dialog.open("选择机台信息", 850, 450, path+"planner/weavePlan/device", [ EasyUI.window.button("icon-save", "确认", function() {
			var rs = EasyUI.grid.getSelections("_common_device_dg");
			for (var i = 0; i < rs.length; i++) {
				var devices=$("#"+dg).datagrid("getRows");
				repeat=false;
				for(var j=0;j<devices.length;j++){
					if(devices[j].deviceId==rs[i].ID){
						repeat=true;
					}
				}
				if(!repeat){
					row={DEVICEID:rs[i].ID,DEVICENAME:rs[i].DEVICENAME,DEVICECODE:rs[i].DEVICECODE,PRODUCECOUNT:1};
					$("#"+dg).datagrid("appendRow",row);
				}
				
			}
			Dialog.close(deviceWindow);
		}), EasyUI.window.button("icon-cancel", "取消", function() {
			Dialog.close(deviceWindow);
		}) ], function() {

		});
	} */
</script>
