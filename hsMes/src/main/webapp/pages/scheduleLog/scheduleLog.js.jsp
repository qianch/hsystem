<!--
	作者:徐秦冬
	日期:2018-2-8 11:01:23
	页面:调度日志JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>

var dialogWidth=700,dialogHeight=350;

function clearAll() {
	Dialog.confirm(function() {
		$.ajax({
			url : path + "scheduleLog/clear",
			type : "post",
			dataType : "json",
			success : function(data) {
				if (Tip.hasError(data)) {
					return;
				}
				Tip.success('已清空所有调度日志信息');
				filter();
			}
		});
		},'确定清空?');
}


//查询
function filter() {
	EasyUI.grid.search("dg","scheduleLogSearchForm");
}

function executeResultFormatter(value,row,index){
	if(value==0){
		return "<font color=green>成功</font>";
	}else{
		return "<font color=red>失败</font>";
	}
}
function executeSpendTimeFormatter(value,row,index){
	if(typeof value == "number"){
		return value/1000;
	}else{
		return value/1000;
	}
}
$(function(){
	laydate({
	    elem: '#startexecuteTime',
	    event: 'focus',
	    format: 'YYYY-MM-DD hh:mm:ss',
	    istime: true
	});
	
	laydate({
	    elem: '#endexecuteTime',
	    event: 'focus',
	    format: 'YYYY-MM-DD hh:mm:ss',
	    istime: true
	});

	$('#dg').datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div class="ddv" style="padding:5px 0"></div>';
        },
        onExpandRow: function(index,row){
        	var exceptionStack="<div style='color:red;'>"
        	+row.EXECUTEEXCEPTIONSTACK.replace(/\n/g,"<br/>").replace(/\t/g,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
        	+"</div>";
            var ddv = $(this).datagrid('getRowDetail',index).find('div.ddv');
            ddv.panel({
//                 height:80,
                border:false,
                cache:false,
                content:exceptionStack
                
            });
            $('#dg').datagrid('fixDetailRowHeight',index);
        }
    });
});
</script>