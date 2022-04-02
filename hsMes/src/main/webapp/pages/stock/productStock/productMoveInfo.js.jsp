<!--
	作者:宋黎明
	日期:2016-11-16 13:44:47
	页面:成品出库记录表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>

var dialogWidth=700,dialogHeight=350;

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

$(function(){
	// var time1 = new Date().Format("dd/MM/yyyy");
	// var time2 = time1+" "+"0:00:00";
	// $("#start").datetimebox("setValue",time2);
	// var time4 = new Date().Format("yyyy-MM-dd");
	// var time3 = time4+" "+"23:59:59";
	// $("#end").datetimebox("setValue",time3);
	filter();
    $('#dg').datagrid({
        url:"${path}stock/productOutRecord/list",
        onBeforeLoad: dgOnBeforeLoad,
    });
});

//查询
function filter() {
	EasyUI.grid.search("dg","productMoveInfoForm");
}

$(function(){
	$("#dg").datagrid({
		url:"${path}stock/productStock/moveList",
		onBeforeLoad:dgOnBeforeLoad,
	})
})

/**
 * 行统计
 */
var flg = true;
function onLoadSuccess(data){
	if(flg){
		appendRow();
	}
	flg = true;
}
</script>