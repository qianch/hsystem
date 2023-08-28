<!--
作者:高飞
日期:2016-10-13 11:06:42
页面:销售订单增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../base/jstl.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style type="text/css">
    form {
        margin: 0px;
    }

    .datagrid-row-selected {
        background: #d4d8f7 !important;
        color: #000000;
    }
</style>
<div id='salesorder_form_layout' class="easyui-layout" style="width:100%;height:100%;" data-options="fit:true">
    <div data-options="region:'north',height:160,title:'出库调拨单信息',split:true">
        <!--销售订单表单-->
        <form id="DeliveryOnTheWayPlanForm" method="post" ajax="true" autocomplete="off">
            <input type="hidden" name="id" value="${deliveryOnTheWayPlan.id}"/>
            <table width="100%">
                <tr>
                    <td class="title">出库调拨单号:</td>
                    <!--出库调拨单号-->
                    <td>
                        <input type="text" id="deliveryCode" name="deliveryCode"
                               value="${deliveryOnTheWayPlan.deliveryCode}" class="easyui-textbox" readonly="readonly"
                               required="true">
                    </td>

                    <td class="title">终点仓库:</td>
                    <td>
                        <input type="text" id="wareHoseCode" name="wareHoseCode"
                               value="${deliveryOnTheWayPlan.wareHouseCode}" class="easyui-textbox" readonly="readonly"
                               required="true">
                    </td>
                </tr>
                <tr>
                    <td class="title">车牌:</td>
                    <td>
                        <input type="text" id="plate" name="plate" value="${deliveryOnTheWayPlan.plate}"
                               class="easyui-textbox" readonly="readonly" required="true">
                    </td>
                </tr>
                <tr>
                    <td class="title">库位:</td>
                    <td>
                        <input type="text" id="warehousePosCode" class="easyui-searchbox" required="true"
                               data-options="searcher:selectWareHousePos,icons:[]">
                        <input type="hidden" id="warehouseCode"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',title:'条码明细'">
        <div id="toolbar_product">
            <c:if test="${empty force }">
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-add"
                   onclick="selectOnTheWayDetail()">增加</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" iconcls="icon-remove"
                   onclick="removeOnTheWayDetail()">删除</a>
            </c:if>
        </div>
        <table id="pmoveOnTheWayDetail_dg" singleSelect="true" class="easyui-datagrid" fitColumns="true"
               style="width:100%;height:100%;" toolbar="#toolbar_product">
            <thead>
            <th field="ID" checkbox=true></th>
            <th field="BARCODE" sortable="true" width="15">托条码</th>
            <th field="SALESORDERSUBCODE" sortable="true" width="15">客户订单号</th>
            <th field="BATCHCODE" sortable="true" width="15">批次号</th>
            <th field="PARTNAME" sortable="true" width="15">厂内名称</th>
            <th field="CONSUMERPRODUCTNAME" sortable="true" width="15">客户产品名称</th>
            <th field="FACTORYPRODUCTNAME" sortable="true" width="15">部位</th>
            <th field="WEIGHT" sortable="true" width="15">重量(Kg)</th>
            <th field="STOCKSTATE" hidden="true" sortable="true" width="15">条码状态</th>
            <th field="STOCKSTATETEXT" sortable="true" width="15">条码状态</th>
            </thead>
        </table>
    </div>
</div>

