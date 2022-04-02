<!--
	作者:肖文彬
	日期:2016-10-18 13:38:47
	页面:编织计划JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	var datagridUrl = path + "planner/weavePlan/list";
	//更新index
	var updateIndex = path + "planner/weavePlan/update";
	//添加编织计划
	var addUrl = path + "planner/weavePlan/add";
	//编辑编织计划
	var editUrl = path + "planner/weavePlan/edit";
	//删除编织计划
	var deleteUrl = path + "planner/weavePlan/delete";
	//编织计划状态改成已完成
	var isFinish = path + "planner/weavePlan/isFinish";
	//设备
	var Device = path + "selector/device?singleSelect=false";
	//分配的设备
	var addDevice = path + "planner/weavePlan/addDevice";
	//优先排序
	var sort = path + "planner/weavePlan/sort";
	var chooseProducePlan = path + "selector/producePlan";
	var finishProduce=path+"planner/weavePlan/finishProduce";
	//取消完成
	var iscloseFinish=path + "planner/weavePlan/iscloseFinish";
	//取消关闭
	var isCancelClose = path + "planner/weavePlan/isCancelClose";
	var producePlanWindow = null;
	var dialogWidth = 930, dialogHeight = 450;
	var index1 = "";
	var row1 = "";
	var _data = null;

	var closeUrl = path + "common/close";

	function doClose() {
		var rows = $("#dg").datagrid("getSelections");
		if (rows.length == 0) {
			Tip.warn("请至少选择一条记录");
			return;
		}
		var type = "WEAVE";
		var ids = [];
		for (var i = 0; i < rows.length; i++) {
			ids.push(rows[i].ID);
		}
		Dialog.confirm(function() {
			Loading.show();
			$.ajax({
				url : closeUrl + "?type=" + type + "&ids=" + ids.join(","),
				type : "get",
				dataType : "json",
				success : function(data) {
					Loading.hide();
					if (Tip.hasError(data)) {
						return;
					}
					Tip.success("关闭成功");
					filter();
				}
			});
		}, "确认关闭");
	}


	$(function(){
		var url = encodeURI("${path}planner/weave/turnbag/weaveList?filter[isTurnBagPlan]=翻包");
		$('#dg').datagrid({
			url: url,
			onBeforeLoad: dgOnBeforeLoad,
		});
	});

	//查询
	function filter() {
		EasyUI.grid.search("dg", "wpSearchForm");
		//$("#dg").datagrid('reLoad');
	}

	function ssort() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if(r.CLOSED==1){
			Tip.warn("此编制计划已关闭");
			return;
		}
		if(r.ISFINISHED==1){
			Tip.warn("此编制计划已完成");
			return;
		}
		JQ.ajaxPost(sort, {
			id : r.ID
		}, function() {
			//$("#dg").datagrid('reLoad');
			$.ajax({
				url : path + "planner/weavePlan/weaveList",
				type : "post",
				dataType : "json",
				data : {
					//"all" : 1,
					"sort" : "sort"
				},
				success : function(data) {
					$('#dg').datagrid('loadData', data);
					Loading.hide();
				},
				error : function() {
					Loading.hide();
				}
			});
		})
	}

	/* function finish() {
		var r = EasyUI.grid.getSelections("dg");
		if (r.length == 0) {
			Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
			return;
		}

		var ids = [];
		for (var i = 0; i < r.length; i++) {
			ids.push(r[i].ID);
		}
		Dialog.confirm(function() {
			JQ.ajax(isFinish, "post", {
				ids : ids.toString()
			}, function(data) {
				filter();
			});
		}, '确认将编织计划设置为已完成？');
	} */

	//生产任务完成
	function finish() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if(r.length == 0){
			Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
			return;
		}
		if(r.CLOSED == 1){
			Tip.warn("订单已经关闭!");
			return;
		}
		if(r.ISFINISHED == 1){
			Tip.warn("订单已经完成!");
			return;
		}
		/* var ids=[];
		for(var i=0;i<r.length;i++){
			ids.push(r[i].ID);
		} */
		dialogId = Dialog.open("完成生产任务?", 600, 200, finishProduce + "?id=" + r.ID, [ EasyUI.window.button("icon-ok", "完成", function() {
			JQ.ajax(isFinish, "post", {
				ids : r.ID.toString()
			}, function(data) {
				Tip.success("已完成");
				filter();
				Dialog.close(dialogId);
			});
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(dialogId);
		}) ],function(){
			
		});
	}
	//生产任务关闭
	function closeTask() {
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if(r.length == 0){
			Tip.warn("<spring:message code="Tip.SelectAtLeastOne" />");
			return;
		}
		if(r.CLOSED == 1){
			Tip.warn("订单已经关闭!");
			return;
		}
		var type = "WEAVE";
		/* var ids=[];
		for(var i=0;i<r.length;i++){
			ids.push(r[i].ID);
		} */
		dialogId = Dialog.open("关闭生产任务?", 600, 200, finishProduce + "?id=" + r.ID, [ EasyUI.window.button("icon-ok", "确定", function() {
			Loading.show();
			$.ajax({
				url : closeUrl + "?type=" + type + "&ids=" + r.ID,
				type : "get",
				dataType : "json",
				success : function(data) {
					Loading.hide();
					if (Tip.hasError(data)) {
						return;
					}
					Tip.success("关闭成功");
					filter();
					Dialog.close(dialogId);
				}
			});
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(dialogId);
		}) ],function(){
			
		});
	}
	/* 	$(function() {
	 loadProducePlans();
	 }); */

	function doSelect(record) {
		loadProducePlans();
	}

	/**
	 * 加载左侧 DataList数据，如果无查询条件，默认最新100条数据
	 */
	function loadProducePlans() {
		filter();
		
	}
	//内部函数
	function loadDataList(data) {
		var rs = data.rows;
		for (var i = 0; i < rs.length; i++) {
			rs[i]["CREATETIME"] = rs[i]["CREATETIME"].substring(0, 10);
		}
		$("#dl").datalist({
			"data" : rs,
			groupFormatter : function(value, rows) {
				return value + " <font color=red>" + rows.length + "条计划</font>";
			}/* ,
							textFormatter : function(value, row, index) {
								if(row.SALESORDERCODE==null){
									return value + " [临时计划,无订单号]";
								}else{
									return value + " [订单号" + row.SALESORDERCODE + "]";
								}
							} */
		});
		//默认选中第一行的生产计划
		if (rs.length != 0) {
			$("#dl").datalist("selectRow", 0);
		}
		Loading.hide();
	}

	function loadMrp(index, row) {
		index1 = index;
		row1 = row;
		Loading.show("加载中");
		$.ajax({
			url : path + "planner/weavePlan/list",
			type : "post",
			dataType : "json",
			data : {
				"planCode" : row.PRODUCEPLANCODE
			},
			success : function(data) {
				Loading.hide();
				_data = data;
				$("#dg").datagrid("loadData", data);
			}
		});
	}

	function _common_device_onLoadSuccess() {
		var array = $("#device").val().split(",");
		for (var i = 0; i < array.length; i++) {
			var rs = $("#_common_device_dg").datagrid('getRows');
			for (var a = 0; a < rs.length; a++) {
				if (rs[a].ID == parseInt(array[i])) {
					$("#_common_device_dg").datagrid("selectRow", a);
				}
			}

		}
	}

	//更新index
	function updateSort() {
		var data = $("#dg").datagrid('getRows');
		for (var i = 0; i < data.length; i++) {
			var index = $("#dg").datagrid('getRowIndex', data[i]);
			$.ajax({
				url : updateIndex,
				type : "post",
				dataType : "json",
				data : {
					index : index,
					id : data[i].ID
				},
				success : function(data) {
					editIndex = undefined;
					Loading.hide();
					$("#dg").datagrid('reLoad');
					if (Tip.hasError(data)) {
						return;
					}

				}
			});
		}
	}
	/**
	 * 双击行，弹出编辑
	 */
	var dbClickEdit = function(index, row) {
		var wid = Dialog.open("编辑", 500, 240, editUrl + "?id=" + row.ID, [ EasyUI.window.button("icon-save", "保存", function() {
			EasyUI.form.submit("weavePlanForm", editUrl, function(data) {
				loadMrp(index1, row1);
				Dialog.close(wid);

			})
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid);
		}) ]);
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

	function formatterIsFinish(value, row) {
		if (value == 1) {
			return '已完成';
		}
		if (value == -1) {
			return '未完成';
		}
	}
	function formatterIsClosed(value, row) {
		if (value == 0||value==null) {
			return '正常';
		}else if (value == 1) {
			return '已关闭';
		}else if(value == 3){
			return "";
		}
	}
	
	function rowStyler(index, row) {
		var style = "";
		if (row.WEAVEPLANPRODUCTTYPE == 1) {
			style += 'background-color:#FFD39B';
		}
		if (row.WEAVEPLANPRODUCTTYPE == 2) {
			style += 'background-color:#FFE7BA';
		}
		if (row.WEAVEPLANPRODUCTTYPE == 3) {
			style += 'background-color:#FFEFDB';
		}
		if (row.WEAVEPLANPRODUCTTYPE == 4) {
			style += 'background-color:#FFF8DC';
		}

		if (isEmpty(row.CLOSED) || row.CLOSED == 0) {
		} else {
			style += "text-decoration:line-through;background: #b8b5bd;";
		}
		if (row.ISFINISHED  == 1 && row.CLOSED!=1) {
			style += "background: #8edd9b;";
		} else {
			
		}

		return style;

	}
	
	function formatterC(index, row, value) {
		if (row.ISCOMEFROMTC == 1) {
			return '裁剪车间';
		}
		if (row.ISCOMEFROMTC == "" || row.ISCOMEFROMTC == null) {
			return row.CONSUMERNAME;
		}
		if (row.ISCOMEFROMTC == 0) {
			return row.CONSUMERNAME;
		}
	}
	//选择生产订单号
	function ChooseProducePlan() {
		producePlanWindow = Dialog.open("选择生产订单号", 850, 450, chooseProducePlan, [ EasyUI.window.button("icon-save", "确认", function() {
			var r = EasyUI.grid.getOnlyOneSelected("_common_producePlan_dg");
			$('#producePlan').searchbox('setValue', r.PRODUCEPLANCODE);
			Dialog.close(producePlanWindow);
		}), EasyUI.window.button("icon-cancel", "<spring:message code="Button.Cancel" />", function() {
			Dialog.close(producePlanWindow);
		}) ]);
	}

	//选择订单信息双击事件
	function _common_producePlan_dbClickRow(index, row) {
		$('#producePlan').searchbox('setValue', row.PRODUCEPLANCODE);
		Dialog.close(producePlanWindow);
	}

	//出货时间
	function orderDateFormat(value, row, index) {
		if (value == undefined)
			return null;
		return new Calendar(value).format("yyyy-MM-dd");
	}

	//查看工艺bom明细
	function bomVersionView(value, row, index) {
		console.log(row)
		if (value == null) {
			return "";
		} else if (row.PROCESSBOMCODE == null || row.PRODUCTPROCESSBOMVERSION == "") {
			return "";
		} else {
			return "<a href='#' title='" + value + "' class='easyui-tooltip' onclick='_bomVersionView(" + row.PROCBOMID + "," + row.PRODUCTISTC + ")'>" + value + "</a>"
		}
	}

	var dialogId;
	function _bomVersionView(procBomId, isTc) {
		if (procBomId == null) {
			Tip.error("工艺版本错误，请重新编辑产品");
			return;
		}
		var viewUrl = "";
		if (isTc == 1) {
			viewUrl = path + "selector/view/tc?procBomId=" + procBomId;
		} else {
			viewUrl = path + "selector/view/ftc?procBomId=" + procBomId;
		}
		dialogId = Dialog.open("查看工艺bom明细", 700, 400, viewUrl, [ EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(dialogId);
		}) ], function() {
			$("#" + dialogId).dialog("maximize");
			if (isTc != 1) {
				for (var a = 0; a < details.length; a++) {
					_common_bomDetail_data(details[a]);
				}
			}
		});
	};

	//查看包装bom明细
	function packBomView(value, row, index) {
		if (value == null) {
			return "";
		} else if (value == "无包装") {
			return "";
		} else if (row.PRODUCTPACKAGEVERSION == null || row.PRODUCTPACKAGEVERSION == "") {
			return "";
		} else {
			return "<a href='#' title='" + value + "' class='easyui-tooltip' onclick='_packBomView(" + row.PACKBOMID + ")'>" + value + "</a>"
		}
	}

	var dialogId;
	function _packBomView(packBomId) {
		if (packBomId == null || packBomId == "") {
			Tip.error("包装工艺错误，请重新编辑产品");
			return;
		}
		var viewUrl = path + "selector/view/bc?packBomId=" + packBomId;
		dialogId = Dialog.open("查看包装bom明细", 700, 400, viewUrl, [ EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(dialogId);
		}) ], function() {
			$("#" + dialogId).dialog("maximize");
			for (var a = 0; a < details.length; a++) {
				_common_bcBomDetail_data(details[a]);
			}
		});
	};
	
	function editDevices(index,row){
		var wid = Dialog.open("编辑", dialogWidth, dialogHeight, addDevice + "?id=" + row.ID,
				[ EasyUI.window.button("icon-save", "保存", function() {
					
					if($("#weavePlanForm").form("validate")){
						Tip.warn("请输入必填项");
						return;
					}
					
					if(getTabCount()==0){
						Tip.warn("请分配机台");
						return;
					}
					
					
										
			
					//$("#deviceDg").datagrid("endEdit", editingIndex);
					
					//在FORM中临时插入，车间，日期，计划
					
					/* if($("#deviceDg").datagrid("getRows").length==0){
						Tip.warn("请指定机台以及数量");
						return;
					}
					if($("#productType").combobox("getValue")==''){
						Tip.warn("请指定产品属性");
						return;
					} */
					
					/* $("#weavePlanForm").append("<input type='hidden' name='wid' value='"+row.ID+"' />");
					$("#weavePlanForm").append("<input type='hidden' name='date' value='"+$("#planDate").datebox("getValue")+"' />");
					$("#weavePlanForm").append("<input type='hidden' name='workshop' value='"+$("#workShop").combobox("getValue")+"' />"); */
					
					Dialog.close(wid);
					
				}), EasyUI.window.button("icon-cancel", "关闭", function() {
					Dialog.close(wid);
				}) 
			], 
			function() {
			Dialog.max(wid);
		}
		);
		
	}
	
	function isPlanedFormatter(value, row, index){
		if(value == null || value == 0){
			return "未分配";
		}else if(value==1){
			return "已分配";
		}else if(value==3){
			return "";
		}
	}
	
	//取消完成
	function closefinish(){
		var r = EasyUI.grid.getSelections("dg");
		if (r.length == 0) {
			Tip.warn("请至少选择一个计划");
			return;
		}

		var ids = [];
		for (var i = 0; i < r.length; i++) {
			if(r[i].ISFINISHED!=1){
				Tip.warn("此编制计划未完成");
				return;
			}
			ids.push(r[i].ID);
		}
		
		Dialog.confirm(function() {
			JQ.ajax(iscloseFinish, "post", {
				ids : ids.toString()
			}, function(data) {
				doSelect();
			});
		}, '确认将编织计划改为取消完成？');
	}
	
	function cancelClose(){
		var r = EasyUI.grid.getSelections("dg");
		if (r.length == 0) {
			Tip.warn("请至少选择一个计划");
			return;
		}

		var ids = [];
		for (var i = 0; i < r.length; i++) {
			if(r[i].CLOSED==0||r[i].CLOSED==null){
				Tip.warn("此编制计划未关闭");
				return;
			}
			ids.push(r[i].ID);
		}
		Dialog.confirm(function() {
			JQ.ajax(isCancelClose, "post", {
				ids : ids.toString()
			}, function(data) {
				doSelect();
			});
		}, '确认将编织计划改为取消关闭？');
	}
	
	
	var turnBagContentUrl=path + "planner/tbp/add";
	var fbWinId;
	/**
	 * 设置翻包领料单
	 */
	function viewTurnBagContent(){
		var r=EasyUI.grid.getOnlyOneSelected("dg");
		var buttons=[];
		
		buttons.push(
				EasyUI.window.button("icon-cancel", "关闭", function() {
					Dialog.close(fbWinId)
				}) 
		);
		
		buttons.push(
				EasyUI.window.button("icon-print", "打印", function() {
					$("#"+fbWinId).printArea({popTitle:"翻包领料单",popClose:true});
				}) 
		);
		
		fbWinId = Dialog.open("翻包领料", dialogWidth, dialogHeight, turnBagContentUrl+"?id="+r.PPD_ID, buttons, function() {
			setTimeout(function(){
				var r = EasyUI.grid.getOnlyOneSelected("dg");
				$("#_producePlanDetails").datagrid({
					data : [ r ]
				});
			}, 0)
			$("#position").datagrid({
				title:'货物位置',
				data:position,
				pagination:false,
				columns:[[
				          {field:'SALESORDERCODE',title:'订单号',width:100},
				          {field:'BATCHCODE',title:'批次号',width:100},
				          {field:'WAREHOUSECODE',title:'库房',width:100},
				          {field:'WAREHOUSEPOSCODE',title:'库位',width:100}
				      ]],
				width:"100%",
				fitColumns:true
			});
			Dialog.max(fbWinId);
		});
	}
	
	
	
	
	var wid1;
	
	
	function printBoxCode(){
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if(r==null){
			Tip.warn("请选择编织任务");
			return;
		}
		//log(r.PRODUCEPLANDETAILID)
		var wid1 = Dialog.open("托条码打印", 419, 200,path+"planner/produce/pack/?producePlanDetailId="+r.PRODUCEPLANDETAILID+"&print=true" , [ EasyUI.window.button("icon-save", "打印", function() {
			var isValid = $("#tray-print-form").form('validate');
			if(!isValid)return;
			var tree=$("#printerTree").combotree("tree")
			var node=tree.tree('getSelected');
			if(tree.tree("getParent",node.target)==null){
				Tip.warn("请选择打印机");
				return;
			}
			var c=$("#count").numberspinner("getValue");
			
			var weaveRow=$("#dg").datagrid("getSelected");
			/**
			 * @param weavePlanId
			 * @param cutPlanId
			 * @param count
			 * @param pName
			 * @param type
			 * @param partName
			 * @param departmentName
			 * @param trugPlanId
			 * @param partId
			 * @param packageTaskId
			 */
			Dialog.confirm(function(){
				JQ.ajaxPost(
						path+"printer/doPrintBarcode",
						{weavePlanId:weaveRow.ID,count:c,pName:node.id,type:"box",departmentName:weaveRow.WORKSHOPCODE},
						function(data){
							if(data.url){
								Tip.success("条码打印成功");
								Dialog.close(wid1);
							}else{
								Tip.error(data);
							}
							
						},function(data){
							Loading.hide();
							Tip.error("打印失败");
						}
					);
				
			},"打印"+c+"张盒条码,是否继续?");
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid1);
		}) ],function(){
			$("#count").numberspinner("setValue",r.TOTALTRAYCOUNT||1);
		});
	}
	
	
	function printTrayCode(){
		var r = EasyUI.grid.getOnlyOneSelected("dg");
		if(r==null){
			Tip.warn("请选择编织任务");
			return;
		}
		//log(r.PRODUCEPLANDETAILID)
		var wid1 = Dialog.open("托条码打印", 419, 200,path+"planner/produce/pack/?producePlanDetailId="+r.PRODUCEPLANDETAILID+"&print=true" , [ EasyUI.window.button("icon-save", "打印", function() {
			var isValid = $("#tray-print-form").form('validate');
			if(!isValid)return;
			var tree=$("#printerTree").combotree("tree")
			var node=tree.tree('getSelected');
			if(tree.tree("getParent",node.target)==null){
				Tip.warn("请选择打印机");
				return;
			}
			var c=$("#count").numberspinner("getValue");
			
			var weaveRow=$("#dg").datagrid("getSelected");
			/**
			 * @param weavePlanId
			 * @param cutPlanId
			 * @param count
			 * @param pName
			 * @param type
			 * @param partName
			 * @param departmentName
			 * @param trugPlanId
			 * @param partId
			 * @param packageTaskId
			 */
			Dialog.confirm(function(){
				JQ.ajaxPost(
						path+"printer/doPrintBarcode",
						{weavePlanId:weaveRow.ID,count:c,pName:node.id,trugPlanId:"1",type:"tray",departmentName:weaveRow.WORKSHOPCODE},
						function(data){
							if(data.url){
								Tip.success("条码打印成功");
								Dialog.close(wid1);
							}else{
								Tip.error(data);
							}
							
						},function(data){
							Loading.hide();
							Tip.error("打印失败");
						}
					);
				
			},"打印"+c+"张托条码,是否继续?");
		}), EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(wid1);
		}) ],function(){
			$("#count").numberspinner("setValue",r.TOTALTRAYCOUNT||1);
		});
	}
</script>