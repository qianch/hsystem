<!--
作者:徐波
日期:2016-11-2 9:30:07
页面:出货计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<%@ include file="../../base/meta.jsp" %>

<style type="text/css">
</style>
<script type="text/javascript">
    //JS代码
    var orderDatas = ${orderDatas};
    var productDatas = ${productDatas};

    function appendOrderUrl(row) {
        if (row) {
            var _row = {
                "salesOrderCode": row.SALESORDERCODE,
                "ladingCode": row.LADINGCODE,
                "boxNumber": row.BOXNUMBER,
                "serialNumber": row.SERIALNUMBER,
                "count": row.COUNT,
                "weight": row.WEIGHT,
                "size": row.SIZE

            };
            $("#dg_order").datagrid("appendRow", _row);
        }
    }

    function appendProductUrl(row) {
        if (row) {
            var _row = {
                "productId": row.PRODUCTID,
                "deliveryCount": row.DELIVERYCOUNT,
                "memo": row.MEMO
            };

            $("#dg_product").datagrid("appendRow", _row);
        }
    }

    $(document).ready(function () {
        $("#dg_order").datagrid("loadData", orderDatas);
        $("#dg_product").datagrid("loadData", productDatas);
    });
</script>
<div>
    <!--出货计划表单-->
    <table width="100%">
        <tr>
            <td class="title">出货单编号:</td>
            <!--条码-->
            <td>
                <input type="text" id="deliveryCode" name="deliveryCode" value="${deliveryPlan.deliveryCode}"
                       class="easyui-textbox" required="true" readonly="readonly">
            </td>
            <td class="title">出货时间:</td>
            <!--出货时间-->
            <td><input type="text" id="deliveryDate" name="deliveryDate" value="${deliveryPlan.deliveryDate}"
                       class="easyui-datebox" required="true" readonly="readonly"></td>
            <td class="title">包装方式:</td>
            <!--出货人-->
            <td><input type="text" id="packagingType" name="packagingType" value="${deliveryPlan.packagingType}"
                       class="easyui-textbox" readonly="readonly"></td>

        </tr>

        <tr>
            <td class="title">要货公司:</td>
            <!--物流公司-->
            <td><input type="text" id="deliveryTargetCompany" name="deliveryTargetCompany"
                       value="${deliveryPlan.deliveryTargetCompany}" required="true" readonly="readonly"
                       class="easyui-textbox" style="width:80%;height:22px;"></td>
            <td class="title">条码:</td>
            <!--条码-->
            <td><input type="text" id="barcode" name="barcode" value="${deliveryPlan.barcode}" class="easyui-textbox"
                       readonly="readonly"></td>
            <td class="title">物流公司:</td>
            <!--物流公司-->
            <td><input type="text" id="logisticsCompany" name="logisticsCompany"
                       value="${deliveryPlan.logisticsCompany}" class="easyui-textbox" readonly="readonly"></td>
        </tr>
        <tr>
            <td class="title">客户基地:</td>
            <!--客户基地-->
            <td><input type="text" id="basePlace" style="width: 615px;"
                       name="basePlace" value="${deliveryPlan.basePlace}"
                       class="easyui-textbox"></td>
        </tr>
        <tr>
            <td class="title">客户备注信息:</td>
            <!--客户基地-->
            <td><input type="text" id="customerNotes" style="width: 615px;"
                       name="customerNotes" value="${deliveryPlan.customerNotes}"
                       class="easyui-textbox"></td>
        </tr>
    </table>
    <table width="100%">
        <tr>
            <td class="title">注意事项:</td>
            <!--注意事项-->
            <td><textarea id="attention" name="attention" style="width:100%;height:50px;" placeholder="1000字以内"
                          readonly="readonly">${deliveryPlan.attention}</textarea></td>
            <!--注意事项-->
        </tr>
    </table>
    <div style="height:300px;">
        <div style="height:50%;">
            <table id="dg_order" singleSelect="false" title="出货订单关联列表" class="easyui-datagrid" url="" rownumbers="true"
                   fitColumns="true" fit="true">
                <thead>
                <tr>
                    <th field="PN" width="15">装箱号</th>
                    <th field="LADINGCODE" width="15">提单号</th>
                    <th field="PLATE" width="15">车牌号</th>
                    <th field="BOXNUMBER" width="15">箱号</th>
                    <th field="SERIALNUMBER" width="15">封号</th>
                    <th field="COUNT" width="15">件数</th>
                    <th field="WEIGHT" width="15">毛重</th>
                    <th field="SIZE" width="15">尺码</th>
                </tr>
                </thead>
            </table>
        </div>
        <div style="height:50%;">
            <table id="dg_product" width="100%" singleSelect="false" title="产品" class="easyui-datagrid" url=""
                   rownumbers="true" fitColumns="false" fit="true">
                <thead frozen="true">
                <tr>
                    <th field="PN" width="60">装箱号</th>
                    <th field="SALESORDERSUBCODE" width="100px">订单号</th>
                    <th field="BATCHCODE" width="100px">批次号</th>
                    <th field="DELIVERYCOUNT" width="100px">发货数量(托)</th>
                    <th field="DELIVERYSUITCOUNT" width="100px">发货数量(套)</th>
                    <th field="MEMO" width="120px">备注</th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th field="FACTORYPRODUCTNAME" width="150px">产品名称</th>
                    <th field="CONSUMERPRODUCTNAME" width="150px">厂内名称</th>
                    <th field="PRODUCTMODEL" width="150px">产品型号</th>
                    <th field="CUSTOMERMATERIALCODEOFFP" width="150px">客户物料号</th>
                    <th field="PARTNAME" width="80px">部件名称</th>
                    <th field="PRODUCTROLLWEIGHT" width="80px">卷重(kg)</th>
                    <th field="PRODUCTROLLLENGTH" width="80px">卷长(m)</th>
                    <th field="PRODUCTWIDTH" width="80px">门幅(mm)</th>
                    <th field="PRODUCTSHELFLIFE" width="100px">保质期(天)</th>
                    <th field="PRODUCTPROCESSCODE" width="120px">工艺代码</th>
                    <th field="PRODUCTPROCESSBOMVERSION" width="100px">工艺版本</th>
                    <th field="PRODUCTPACKAGINGCODE" width="120px">包装代码</th>
                    <th field="PRODUCTPACKAGEVERSION" width="100px">包装版本</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>

</div>
