<!--
作者:肖文彬
日期:2016-9-29 15:45:32
页面:订单管理增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style>
</style>
<script type="text/javascript">
</script>
<div>
    <!--订单管理表单-->
    <form id="cutSalesOrderForm" method="post" ajax="true" autocomplete="off">
        <input type="hidden" name="id" value="${cutSalesOrder.id}"/>
        <table width="100%">
            <tr>
                <td class="title">批次号:</td>
                <!--订单代码-->
                <td>
                    <input type="text" id="batchCode" name="batchCode" value="${cutSalesOrder.batchCode}"
                           class="easyui-searchbox" required="true"
                           data-options="searcher:chooseproducePlanDetail,icons:[]">
                    <input type="hidden" id="producePlanDetailId" name="producePlanDetailId"
                           value="${cutSalesOrder.producePlanDetailId}"/>
                </td>
                <td class="title">订单号:</td>
                <!--订单名称-->
                <td>
                    <input type="text" id="salesOrderCode" name="salesOrderCode" value="${cutSalesOrder.salesOrderCode}"
                           readonly="true" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">叶型:</td>
                <!--订单代码-->
                <td>
                    <input type="text"
                           id="bladeTypeName" name="bladeTypeName"
                           value="${cutSalesOrder.bladeTypeName}" class="easyui-searchbox"
                           required="true" data-options="searcher:chooseCutTcBomMain,icons:[]">
                    <input type="hidden" id="tcBomMainId" name="tcBomMainId"
                           value="${cutSalesOrder.tcBomMainId}"/>
                </td>
                <td class="title">订单数量:</td>
                <!--订单代码-->
                <td>
                    <input type="number" id="orderNum" name="orderNum" value="${cutSalesOrder.orderNum}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">备注:</td>
                <!--订单代码-->
                <td>
                    <input type="text" id="remark" name="remark" value="${cutSalesOrder.remark}" class="easyui-textbox">
                </td>
            </tr>
        </table>
    </form>
</div>
