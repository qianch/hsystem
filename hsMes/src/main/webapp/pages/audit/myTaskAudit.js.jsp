<!--
	作者:高飞
	日期:2016-10-25 13:52:50
	页面:流程实例JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//编辑流程实例
var auditUrl=path+"audit/";

var dialogWidth=700,dialogHeight=350;

var dialogId;

//查询
function filter() {
	EasyUI.grid.search("dg_ins","auditInstanceSearchForm");
}



function formatNode(value,row,index){
	if(value==1){
		return "一级审核";
	}
	if(value==2){
		return "二级审核";
	}
}
function formatIsfinish(value,row,index){
	if(value==1){
		return "完成";
	}else{
		return "未完成";
	}
}
function resultFormatter(value, row, index) {
	if (value == -1)
		return "<font color=red>不通过</font>";
	if (value == 2)
		return "<font color=green>通过</font>";
	
}


function view() {
	var r=EasyUI.grid.getOnlyOneSelected("dg_ins");
		//
		var dialogId = Dialog.open("查看审核状态", 700, 400, path + "audit/"+r.AUDITCODE+"/"+r.FORMID+"/state", [ EasyUI.window.button("icon-cancel", "关闭", function() {
			Dialog.close(dialogId);
		}) ], function() {
			$("#" + dialogId).dialog("maximize");
		});

}

</script>