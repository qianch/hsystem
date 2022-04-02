<!--
	作者:高飞
	日期:2017-8-9 16:30:35
	页面:条码扫描错误记录JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>

var dialogWidth=700,dialogHeight=350;

//查询
function filter() {
	EasyUI.grid.search("dg","suitErrorLogSearchForm");
}

function resizeDg(data){
	$('#dg').datagrid('resize',{height:'auto'})
}

function exportSuitErrorLog(){
	var d1=$("#SCANTIME_S").datebox("getValue");
	var d2=$("#SCANTIME_E").datebox("getValue");
	if(d1==""||d2==""){
		Tip.warn("必须输入扫描结束和开始时间");
		return;
	}
	var url= encodeURI(path+"excel/export/组套错误记录/com.bluebirdme.mes.siemens.excel.SuitErrorLogExportHandler/filter?"+JQ.getFormAsString("suitErrorLogSearchForm")) ;
	location.href=url;
}

</script>