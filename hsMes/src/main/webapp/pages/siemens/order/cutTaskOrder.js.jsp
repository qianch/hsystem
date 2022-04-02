<!--
	作者:高飞
	日期:2017-7-31 17:04:13
	页面:裁剪派工单JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//添加裁剪派工单
var addUrl=path+"siemens/cutTaskOrder/add";
//编辑裁剪派工单
var editUrl=path+"siemens/cutTaskOrder/edit";
//删除裁剪派工单
var deleteUrl=path+"siemens/cutTaskOrder/delete";

var closeUrl=path+"siemens/cutTaskOrder/close";

var dialogWidth=700,dialogHeight=350;

//查询
function filter() {
	EasyUI.grid.search("dg","cutTaskOrderSearchForm");
}


//编辑裁剪派工单
var edit = function(){
	var row=EasyUI.grid.getOnlyOneSelected("dg");
	
	if(row==null){
		return;
	}
	Loading.show("正在获取单号，图纸信息");
	
	//获取单号
	$.ajax({
		url:path+"siemens/cutTaskOrder/info?ctId="+row.CTID,
		type:"get",
		dataType:"json",
		success:function(data){
			$("#dlg").dialog("open");
			$("input[name='id']").val(row.ID);
			$("input[name='ctId']").val(row.CTID);
			if(row.CONSUMERCATEGORY=="1"){
				$("#taskConsumerCategoryX").textbox("setValue","国内");
			}
			if(row.CONSUMERCATEGORY=="2"){
				$("#taskConsumerCategoryX").textbox("setValue","国外");
			}
			$("#deliveryDate").textbox("setValue",row.DELIVERYDATE.substring(0,10));
			$("#orderCode").textbox("setValue",row.ORDERCODE);
			$("#batchCode").textbox("setValue",row.BATCHCODE);
			$("#consumerSimpleName").textbox("setValue",row.CONSUMERSIMPLENAME);
			$("#partName").textbox("setValue",row.PARTNAME);
			$("#suitCount").textbox("setValue",row.SUITCOUNT);
			$("#assignCount").textbox("setValue",row.ASSIGNCOUNT);
			$("#taskCode").textbox("setValue",row.TASKCODE);
			$("#ctoCode").textbox("setValue",row.CTOCODE);
			$("#drawingsDg").datagrid("loadData",data.drawings);
			$("#assignCount").numberspinner({
					min:1,
					max:data.assigned+row.ASSIGNSUITCOUNT,
					value:data.assigend+row.ASSIGNSUITCOUNT,
					onChange:setCount,
		        	inputEvents: $.extend({}, $.fn.numberbox.defaults.inputEvents, {
			             keyup: function (e) {
			            	 setCount($("#assignCount").numberspinner("getText"));
			             }
		        	})
		     });
			$('#assignCount').numberbox("setValue",row.ASSIGNSUITCOUNT);
			setCount(row.ASSIGNSUITCOUNT);
			$("#ctoGroupLeader").combobox("setValue",row.CTOGROUPLEADER);
			$("#ctoGroupName").textbox("setValue",row.CTOGROUPNAME);
			Loading.hide();
		}
	});
}


//删除裁剪派工单
var doDelete = function(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	if(r.length == 0){
		return;
	}
	Dialog.confirm(function(){
		JQ.ajax(deleteUrl, "post", {
			id : r.ID
		}, function(data){
			filter();
		});
	});
}


function validCode(){
	var _options = $(this).combobox('options');
    var _data = $(this).combobox('getData');/* 下拉框所有选项 */  
    var _value = $(this).combobox('getValue');/* 用户输入的值 */  
    var _text = $(this).combobox('getText');
    var _b = false;/* 标识是否在下拉列表中找到了用户输入的字符 */  
    for (var i = 0; i < _data.length; i++) {  
        if (_data[i][_options.valueField] == _value&&_data[i][_options.textField]==_text) {  
            _b=true;
            onGroupComboSelect({GROUPLEADER:_text},this);
            break;
        }
    }
    if(!_b){
        $(this).combobox('setValue', '');
        $("#ctoGroupName").textbox("setValue","");
    }
}

function onGroupComboSelect(record){
	for(var i=0;i<groups.length;i++){
		if(record.GROUPLEADER==groups[i].GROUPLEADER){
			$("#ctoGroupName").textbox("setValue",groups[i].GROUPNAME);
			break;
		}
	}
}

function ccFormat(v,r,i){
	if(v=="1"){
		return "国内";
	}
	if(v=="2"){
		return "国外";
	}
}

function dateFormatter(v,r,i){
	if(!v)return '';
	return v.substring(0,10);
}

function completeFormatter(v,r,i){
	if(v==undefined)return '';
	if(v==0)
		return "<font color=red>未完成</font>";
	else
		return "<font color=red>已完成</font>";
}
var enableFilter=true;
function onLoadSuccess(data){
	
}

function styler(i,r){
	if(r&&r.ISCLOSED=="1")
		return "color:red";
	else
		return "color:#0012ff";
}

function closedFormatter(v,r,i){
	
	if(v==undefined)return;
	
	if(v==0)
		return "<font color=green>✔</font>";
	if(v==1)
		return "<font color=red>✘</font>";
}

function resizeDg(data){
	$('#dg').datagrid('resize',{height:'auto'})
}

var inValidSuitCount=true;

function setCount(nv,ov){
	var v=nv;
	if(v>$("#assignCount").numberspinner("options").max||v==="0"){
		Tip.warn("超出可派工数量");
		$("#assignCount").numberspinner("setValue",$("#assignCount").numberspinner("options").max);
		setCount($("#assignCount").numberspinner("options").max);
		return;
	}
	 if(v==""){
		 setWrongCount();
		 return;
	 }
	 v=parseInt(v);
	 var rows=$("#drawingsDg").datagrid("getRows");
	 var c=undefined;
	 for(var i=0;i<rows.length;i++){
		 /* if(v%rows[i].suitCountPerDrawings!=0){
			 setWrongCount();
			 return;
		 } */
		 //rows[i].farbicRollCount=v/rows[i].suitCountPerDrawings;
		 rows[i].farbicRollCount=Calc.div(v,rows[i].suitCountPerDrawings,1);
		 $("#drawingsDg").datagrid("updateRow",{index:i,row:rows[i]});
	 }
	 inValidSuitCount=false;
}


var groups=${empty groups?"[]":groups};


function setWrongCount(){
	inValidSuitCount=true;
	var rows=$("#drawingsDg").datagrid("getRows");
	 for(var i=0;i<rows.length;i++){
		 rows[i].farbicRollCount="<font color='red'>无效派工套数</font>";
		 $("#drawingsDg").datagrid("updateRow",{index:i,row:rows[i]});
	 }
}

function closeTask(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	if(r.length == 0){
		return;
	}
	enableOrCloseTask(r.ID, 1);
}

function enableTask(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	if(r.length == 0){
		return;
	}
	enableOrCloseTask(r.ID, 0);
}

function enableOrCloseTask(id,action){
	Dialog.confirm(function(){
		JQ.ajaxGet(path+"siemens/cutTaskOrder/close?id="+id+"&closed="+action,function(data){
			filter();
		});
	},"确认"+(action==0?"启用":"关闭")+"选中的任务单?");
}

function saveCutTaskOrder(){
	if(inValidSuitCount){
		Tip.warn("派工套数不正确");
		return;
	}
	if(!$("#cutTaskForm").form("validate")){
		return;
	}
	var formData=JQ.getFormAsJson("cutTaskForm");
	/* var list=$("#drawingsDg").datagrid("getRows");
	formData["list"]=list; */
	$.ajax({
		url:path+"siemens/cutTaskOrder/save",
		type:"post",
		data:JSON.stringify(formData),
		dataType:"json",
		contentType:"application/json;charset=UTF-8",
		success:function(data){
			Tip.success("编辑成功");
			$("#ctoGroupLeader").combobox("setValue","");
			$("#ctoGroupName").textbox("setValue","");
			$("#dlg").dialog("close");
			filter();
		}
	});
}

function printTask(){
	var row=EasyUI.grid.getOnlyOneSelected("dg");
	if(row==null){
		return;
	}
	Loading.show("正在获取单号，图纸信息");
	//获取单号
	$.ajax({
		url:path+"siemens/cutTaskOrder/info?ctId="+row.CTID,
		type:"get",
		dataType:"json",
		success:function(data){
			$("#dlg2").dialog({buttons:"#dlg-buttons2"});
			$("#dlg2").dialog("open");
			if(row.CONSUMERCATEGORY=="1"){
				$("#taskConsumerCategoryX2").textbox("setValue","国内");
			}
			if(row.CONSUMERCATEGORY=="2"){
				$("#taskConsumerCategoryX2").textbox("setValue","国外");
			}
			$("#deliveryDate2").textbox("setValue",row.DELIVERYDATE.substring(0,10));
			$("#orderCode2").textbox("setValue",row.ORDERCODE);
			$("#batchCode2").textbox("setValue",row.BATCHCODE);
			$("#consumerSimpleName2").textbox("setValue",row.CONSUMERSIMPLENAME);
			$("#partName2").textbox("setValue",row.PARTNAME);
			$("#suitCount2").textbox("setValue",row.SUITCOUNT);
			$("#assignCount2").textbox("setValue",row.ASSIGNCOUNT);
			$("#taskCode2").textbox("setValue",row.TASKCODE);
			$("#ctoCode2").textbox("setValue",row.CTOCODE);
			$("#drawingsDg2").datagrid("loadData",data.drawings);
			$('#assignCount2').textbox("setValue",row.ASSIGNSUITCOUNT);
			setCount2(row.ASSIGNSUITCOUNT);
			$("#ctoGroupLeader2").combobox("setValue",row.CTOGROUPLEADER);
			$("#ctoGroupName2").textbox("setValue",row.CTOGROUPNAME);
			$("#qr_code").empty();
			$("#qr_code").qrcode({
				render : "img",
				width : 60,
				height : 60,
				text : row.CTOCODE
			});
			Loading.hide();
		}
	});
}

function doPrint(){
	$("#pp").printArea({mode:'popup',popTitle:"恒石MES",popClose:true});
}


function setCount2(nv){
	console.log("123")
	var v=nv;
	 v=parseInt(v);
	 var rows=$("#drawingsDg2").datagrid("getRows");
	 var c=undefined;
	 for(var i=0;i<rows.length;i++){
		 /* if(v%rows[i].suitCountPerDrawings!=0){
			 return;
		 } */
		 rows[i].farbicRollCount=Calc.div(v,rows[i].suitCountPerDrawings,1);
		 //rows[i].farbicRollCount=v/rows[i].suitCountPerDrawings;
		 $("#drawingsDg2").datagrid("updateRow",{index:i,row:rows[i]});
	 }
}

function exportTask(){
	var url=path+"excel/export/裁剪派工单/com.bluebirdme.mes.siemens.excel.CutTaskOrderExportHandler/filter?"+JQ.getFormAsString("cutTaskOrderSearchForm");
	location.href=url;
}

var printers=${empty printers?"[]":printers};

function printBarcode(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	if(r==null)return;
	JQ.ajaxGet(path+"siemens/cutTaskOrder/drawingsNo?ctId="+r.CTID,function(data){
		$("#dlg3").dialog("open");
		$("#drawingsDgPrint").datagrid("loadData",data);
		$("#printSuitCount").numberspinner("setValue",r.ASSIGNSUITCOUNT);
	});
}

function doPrintBarcode(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	if(r==null)return;
	var p=$("#printer").combobox("getValue");
	if(p==""){
		Tip.warn("请选择打印机");
		return;
	}
	var c=$("#printSuitCount").numberspinner("getValue");
	if(c==""){
		Tip.warn("请输入打印套数");
		return;
	}
	
	var rs=EasyUI.grid.getSelections("drawingsDgPrint");
	if(rs.length==0){
		Tip.warn("请选择图号");
		return;
	}
	
	var dns=[];
	
	for(var i=0;i<rs.length;i++){
		dns.push(rs[i].FRAGMENTDRAWINGNO);
	}
	
	Loading.show("正在打印");
	JQ.ajaxPost(path+"siemens/cutTaskOrder/printBarcode",{ctoId:r.ID,cutPlanId:r.CUTPLANID,drawingsNo:dns.join(","),suitCount:c,printer:p},function(data){
		Loading.hide();
		if(Tip.hasError(data)){
			return;
		}
		Tip.success("打印成功");
		$("#dlg3").dialog("close");
	},function(){
		Loading.hide();
		Tip.error("打印失败");
	});
}


function rePrint(ctoId,dwId,fragment){
	$("#dlg4").dialog("open");
	$("#fragmentInfo").text(fragment);
	$("#rePrintCount").numberspinner("setValue",1);
	$("#reason").val("");
	$("#ctoId").val(ctoId);
	$("#dwId").val(dwId);
}

function doRePrint(){
	
	var printer=$("#rePrinter").combobox("getValue");
	if(printer==""){
		Tip.warn("请选择打印机");
		return;
	}
	var rePrintCount=$("#rePrintCount").numberspinner("getValue");
	if(rePrintCount==""){
		Tip.warn("请输入打印数量");
		return;
	}
	var reason=$("#reason").val();
	var ctoId=$("#ctoId").val();
	var dwId=$("#dwId").val();
	
	if(reason==""){
		Tip.warn("请输入重打原因");
		return;
	}
	
	JQ.ajaxPost(path+"siemens/cutTaskOrder/rePrintBarcode",{ctoId:ctoId,dwId:dwId,rePrintCount:rePrintCount,printer:printer,user:"${userName}",reason:reason},function(data){
		Tip.success("打印成功");
		$("#dlg4").dialog("close");
	});
	
}

function exportCheckBarcode(){
	var r=EasyUI.grid.getOnlyOneSelected("dg");
	if(r==null)return;
	location.href=path+"siemens/cutTaskOrder/checkBarcode?ctoId="+r.ID;
}

function viewDrawings(){
	var row=EasyUI.grid.getOnlyOneSelected("dg");
	
	if(row==null){
		return;
	}
	viewDrawingsBom(row.CTID);
}

function viewSuit(){
	var row=EasyUI.grid.getOnlyOneSelected("dg");
	
	if(row==null){
		return;
	}
	viewSuitBom(row.CTID);
}
</script>