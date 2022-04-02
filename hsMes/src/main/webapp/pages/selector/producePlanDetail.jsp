<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<title>选择生产计划明细信息</title>
<%@ include file="../base/jstl.jsp" %>
<script type="text/javascript">
    //查询
    function _common_common_producePlanDetail_filter() {
        EasyUI.grid.search("_common_producePlanDetail_dg", "_common_producePlanDetail_dg_form");
    }

    $(document).ready(function () {
        //设置数据表格的DetailFormatter内容
        $('#_common_producePlanDetail_dg').datagrid({
            onDblClickRow: function (index, row) {
                if (typeof _common_producePlanDetail_dbClickRow === "function") {
                    _common_producePlanDetail_dbClickRow(index, row);
                } else {
                    if (window.console) {
                        console.log("没有为用户选择界面提供_common_producePlanDetail_dbClickRow方法，参数为index,row");
                    }
                }
            },
            onLoadSuccess: function (data) {
                if (typeof _common_producePlanDetail_onLoadSuccess === "function") {
                    _common_producePlanDetail_onLoadSuccess(data);
                } else {
                    if (window.console) {
                        console.log("未定义用户选择界面加载完成的方法：_common_producePlanDetail_onLoadSuccess(data)");
                    }
                }
            }

        });

    });

    function formatterIsTc(value, row) {
        if (value == '1') {
            return "套材";
        } else {
            return "非套材";
        }
    }

</script>

<div id="_common_producePlanDetail_toolbar">
    <div style="border-top:1px solid #DDDDDD">
        <form action="#" id="_common_producePlanDetail_dg_form" autoSearch="true" autoSearchFunction="false">
            任务单号：<input type="text" name="filter[producePlanCode]" like="true" class="easyui-textbox"
                        style="width:200px;">
            订单号：<input type="text" name="filter[salesOrderCode]" like="true" class="easyui-textbox"
                       style="width:150px;">
            批次号：<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox" style="width:150px;">
            厂内名称：<input type="text" name="filter[factoryProductName]" like="true" class="easyui-textbox"
                        style="width:200px;">
            <br/>
            客户名称：<input type="text" name="filter[consumerName]" like="true" class="easyui-textbox" style="width:200px;">
            <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
               onclick="_common_common_producePlanDetail_filter()">
                搜索
            </a>
        </form>

    </div>
</div>
<table id="_common_producePlanDetail_dg" singleSelect="${empty singleSelect?'true':singleSelect }"
       class="easyui-datagrid" url="${path}planner/producePlanDetail/list" toolbar="#_common_producePlanDetail_toolbar"
       pagination="true" rownumbers="true" fitColumns="true" fit="true">
    <thead>
    <tr>
        <th field="ID" checkbox=true></th>
        <th field="SALESORDERCODE" sortable="true" width="15">订单号</th>
        <th field="FROMSALESORDERDETAILID" sortable="true"  width="15">订单明细id</th>
        <th field="BATCHCODE" sortable="true" width="15">批次号</th>
        <th field="PRODUCEPLANCODE" sortable="true" width="30">任务单号</th>
        <th field="SUBCODE" sortable="true" width="15" hidden="true">子订单号</th>
        <th field="CONSUMERPRODUCTNAME" sortable="true" width="15">客户产品名称</th>
        <th field="FACTORYPRODUCTNAME" sortable="true" width="15">厂内名称</th>
        <th field="PRODUCTWIDTH" sortable="true" width="5">门幅</th>
        <th field="PRODUCTMODEL" width="15">产品规格</th>
        <th field="CONSUMERNAME" width="15">客户名称</th>
        <th field="PRODUCTID" sortable="true" width="15">产品信息ID</th>
        <th field="PRODUCTISTC" sortable="true" width="5" formatter="formatterIsTc">是否套材</th>
    </tr>
    </thead>
</table>
