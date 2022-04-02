<!--
    作者:高飞
    日期:2016-10-12 11:06:09
    页面:原料JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
//查询
function filter() {
    EasyUI.grid.search("dg","materialSearchForm");
}

$(function(){
    $("#dg").datagrid({
        url:"${path}material/in/list",
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
/**
 * 表格末尾追加统计行
 */
var flg = true;
function appendRow(data){
    if(flg){
        appendRow();
    }
    flg = true;
}
function appendRow(){
    $("#dg").datagrid('appendRow', {
        PRODUCECATEGORY: '<span class="subtotal" style=" font-weight: bold;">合计</span>',
        WEIGHT: '<span class="subtotal" style=" font-weight: bold;">' + computes("WEIGHT") + '</span>'	//月末累计数量统计
    });
    flg = false;
}
/**
 * 指定列求和
 */
function computes(colName){
    var rows = $("#dg").datagrid("getRows");
    var totals = 0;
    for (var i = 0; i < rows.length; i++) {
        totals += parseFloat(rows[i][colName]);
    }
    return totals.toFixed(2);
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
    }else if(value==1){
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

function inTimeFormatter(value, row, index){
    if(!value)return "";
    return new Calendar(value).format("yyyy-MM-dd");
}

function devationFormatter(v,r,i){
    if(!v)return "";
    return r.LOWERDEVIATION+"~"+r.UPPERDEVIATION;
}

function realDevationFormatter(v,r,i){
    if(!v)return "";
    return r.REALLOWERDEVIATION+"~"+r.REALUPPERDEVIATION;
}
//原料入库明细导出
function exportDetail(){
	location.href= encodeURI(path +  "material/export1?"+JQ.getFormAsString("materialSearchForm"));
}

//原料入库明细导出
function exportDetail2(){
	location.href= encodeURI(path +  "material/exportIn?"+JQ.getFormAsString("materialSearchForm"));
}
</script>