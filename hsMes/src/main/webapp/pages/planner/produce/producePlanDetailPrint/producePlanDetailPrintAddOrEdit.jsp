<!--
作者:高飞
日期:2016-10-13 11:06:42
页面:销售订单增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="../../../base/jstl.jsp" %>
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
<script type="text/javascript">
    $(function () {
        loadTagType();
        loadPlanDetailPrint();
    });
</script>

<div id='salesorder_form_layout' class="easyui-layout" style="width:100%;height:100%;" data-options="fit:true">
    <div data-options="region:'north',height:160,title:'打印条码信息',split:true">
        <!--销售订单表单-->
        <form id="producePlanDetailPrintForm" method="post" ajax="true" autocomplete="off">

            <input type="hidden" id="producePlanDetailId" name="producePlanDetailId" value="${producePlanDetail.id}"/>
            <table width="100%">
                <tr>
                    <td class="title">任务单号:</td>
                    <!--出库调拨单号-->
                    <td>
                        <input type="text" id="planCode" name="planCode" value="${producePlanDetail.planCode}"
                               class="easyui-textbox" readonly="readonly" required="true">
                    </td>

                    <td class="title">订单号:</td>
                    <!--终点仓库-->
                    <td>
                        <input type="text" id="salesOrderCode" name="salesOrderCode"
                               value="${producePlanDetail.salesOrderCode}" class="easyui-textbox" readonly="readonly"
                               required="true">
                    </td>
                </tr>

                <tr>
                    <td class="title">客户订单号:</td>
                    <!--出库调拨单号-->
                    <td>
                        <input type="text" id="salesOrderSubcodePrint" name="salesOrderSubcodePrint"
                               value="${producePlanDetail.salesOrderSubcodePrint}" class="easyui-textbox"
                               readonly="readonly" required="true">
                    </td>

                    <td class="title">批次号:</td>
                    <!--终点仓库-->
                    <td>
                        <input type="text" id="batchCode" name="batchCode" value="${producePlanDetail.batchCode}"
                               class="easyui-textbox" readonly="readonly" required="true">
                    </td>
                </tr>

                <tr>
                    <td class="title">厂内名称:</td>
                    <!--出库调拨单号-->
                    <td>
                        <input type="text" id="factoryProductName" name="factoryProductName"
                               value="${producePlanDetail.factoryProductName}" class="easyui-textbox"
                               readonly="readonly" required="true">
                    </td>

                    <td class="title">产品名称:</td>
                    <!--终点仓库-->
                    <td>
                        <input type="text" id="consumerProductName" name="consumerProductName"
                               value="${producePlanDetail.consumerProductName}" class="easyui-textbox"
                               readonly="readonly" required="true">
                    </td>
                </tr>

                <tr>

                    <td class="title">标签类型:</td>
                    <td>
                        <input type="hidden" name="tagType" id="tagType" value="${tagType}">
                        <input type="text" id="tagTypeSelect"  class="easyui-combobox"  required="true" style="width: 80px;">
                    </td>
                    <td class="title">标签名称:</td>
                    <!--出库调拨单号-->
                    <td>
                        <input type="hidden" name="btwFileId" id="btwFileId" value="${btwFileId}">
                        <input type="hidden" name="customerId" id="customerId" value="${customerId}">
                        <input id="btwFileCmb"  class="easyui-combobox" style="width:200px" required="true">
                    </td>
                </tr>
            </table>
        </form>
    </div>


    <div data-options="region:'center',title:'打印明细'">
        <div id="toolbar_product">
            <c:if test="${empty force }">

            </c:if>
        </div>

        <table id="dg_PlanDetailPrint" singleSelect="true" title="打印信息"
               class="easyui-datagrid"
               rownumbers="true" fitColumns="true" fit="true"
               data-options="onClickRow:onClickRow">
            <thead>
            <tr>
                <th field="ID" checkbox=true></th>
                <th field="PRINTATTRIBUTE" width="100">打印属性</th>
                <th field="PRINTATTRIBUTENAME" width="100">打印属性名称</th>

                <th field="PRINTATTRIBUTECONTENT" width="150" editor="{type:'textbox',options:{}}">打印内容</th>
            </tr>
            </thead>
        </table>


    </div>


</div>

