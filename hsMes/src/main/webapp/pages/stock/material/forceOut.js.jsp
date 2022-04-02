<!--
	作者:sunli
	日期:2018-03-06 11:06:09
	页面:异常退回巨石JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//查询
function filter() {
	EasyUI.grid.search("dg","materialSearchForm");
}
//入库类型
function inBankTypeFormatter(value, row, index){
	if(value==0){
		return "入库";
	}else if(value==-1){
		return "退库";
	}
}
//k3同步状态
function syncStateFormatter(value, row, index){
	if(value==0){
		return "未同步";
	}else{
		return "已同步";
	}
}
//库存状态
function stockStateFormatter(value, row, index){
	if(value==0){
		return "在库";
	}else if(value==1){
		return "不在库";
	}
}
//状态
function stateFormatter(value, row, index){
	if(value==0){
		return "待检";
	}else if(value==1){
		return "合格";
	}else if(value==2){
		return "不合格";
	}
}
//是否放行
function isPassFormatter(value, row, index){
	if(value==0){
		return "正常";
	}else if(value==1){
		return "放行";
	}
}

function inTimeFormatter(value, row, index){
	if(!value)return "";
	return new Calendar(value).format("yyyy-MM-dd HH:mm:ss");
}
//异常原料退回导出
function exportDetail(){
	location.href= encodeURI(path + "material/forceExport?"+JQ.getFormAsString("materialSearchForm"));
}

</script>