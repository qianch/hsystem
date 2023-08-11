<!--
作者:徐波
日期:2016-11-2 9:30:07
页面:出货计划增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<style type="text/css">
</style>
<script type="text/javascript">
    //JS代码
    const orderDatas = ${orderDatas};
    const productDatas = ${productDatas};

    function isFinished(val, row, index) {
        if (val === 1) {
            return "已发货";
        } else {
            return "未发货";
        }
    }

    function unbindFormatter(v, r, i) {
        if (!r.OPTUSER) return "";
        return "<a id='unbind-" + r.ID + "' href='javascript:void(0)' onclick='unbind(" + r.ID + ")'>　解除绑定　" + r.OPTUSER + "（PDA IMEI:" + r.PDAID + "）</a>　";
    }

    function unbind(id) {
        if (confirm("确认解除？如果正在扫描出库，则无法保存!")) {
            JQ.ajaxGet(path + "mobile/stock/product/unbind?id=" + id, function (data) {
                alert("解除成功");
                $("#unbind-" + id).remove();
            });
        }
    }
</script>
<div>
    <!--出货计划表单-->
    <form id="deliveryPlanForm" style="margin-bottom: 0px;" method="post" ajax="true"
          action="<%=basePath %>planner/deliveryPlan/${empty deliveryPlan.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${deliveryPlan.id}"/>
        <input type="hidden" name="auditState" value="${deliveryPlan.auditState}"/>
        <input type="hidden" name="deliveryBizUserId" value="${userId }"/>
        <table width="100%">
            <tr>
                <td class="title">出货单编号:</td>
                <!--条码-->
                <td>
                    <select id="deliveryType" onchange="getSerial()">
                        <option value="NX">内销</option>
                        <option value="WX">外销</option>
                    </select>
                    <input type="text" id="deliveryCode" readonly="true"
                           style="width: 95%;//宽度 height=20px;//高度"
                           name="deliveryCode" value="${deliveryPlan.deliveryCode}"
                           class="easyui-textbox" required="true">
                </td>
                <td class="title">出货时间:</td>
                <!--出货时间-->
                <td>
                    <input type="text" id="deliveryDate" name="deliveryDate"
                           style="width: 95%;//宽度 height=20px;//高度" readonly="true"
                           value="${deliveryPlan.deliveryDate}" class="easyui-datebox" required="true"></td>
                <td class="title">包装方式:</td>
                <!--出货人-->
                <td>
                    <input type="text" id="packagingType" name="packagingType" style="width: 95%; height: 20px;"
                           readonly="true" value="${deliveryPlan.packagingType}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title" width="6%">要货公司:</td>
                <!--物流公司-->
                <input type="hidden" name="consumerId" id="consumerId" value="">
                <td width="27%">
                    <input type="text" readonly="true" style="width: 95%;//宽度 height=20px;//高度"
                           id="deliveryTargetCompany" name="deliveryTargetCompany"
                           value="${deliveryPlan.deliveryTargetCompany}" required="true"
                           class="easyui-searchbox" data-options="searcher:selectConsumer"
                           style="width:70%;height:22px;">
                </td>
                <td class="title" width="6%">条码:</td>
                <!--条码-->
                <td width="27%">
                    <input type="text" id="barcode" name="barcode"
                           style="width: 95%;//宽度 height=20px;//高度"
                           readonly="true" value="${deliveryPlan.barcode}"
                           class="easyui-textbox"></td>
                <td class="title" width="6%">物流公司:</td>
                <!--物流公司-->
                <td width="28%">
                    <input type="text" id="logisticsCompany" style="width: 95%;//宽度 height=20px;//高度"
                           name="logisticsCompany" readonly="true"
                           value="${deliveryPlan.logisticsCompany}" class="easyui-textbox"></td>
            </tr>
            <tr>
                <td class="title">客户基地:</td>
                <!--客户基地-->
                <td>
                    <input type="text" id="basePlace" style="width: 95%;//宽度 height=20px;//高度"
                           name="basePlace" value="${deliveryPlan.basePlace}"
                           class="easyui-textbox">
                </td>
                <td class="title">码单订单号:</td>
                <!--码单订单号-->
                <td>
                    <input type="text" id="codeOrderNo" style="width: 95%;//宽度 height=20px;//高度"
                           name="codeOrderNo" value="${deliveryPlan.codeOrderNo}"
                           class="easyui-textbox">
                </td>
                <td class="title">联系人及电话:</td>
                <!--联系人及电话-->
                <td>
                    <input type="text" id="linkmanAndPhone" style="width: 95%;//宽度 height=20px;//高度"
                           name="linkmanAndPhone" value="${deliveryPlan.linkmanAndPhone}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">客户备注信息:</td>
                <!--客户基地-->
                <td>
                    <input type="text" id="customerNotes" style="width: 95%;//宽度 height=20px;//高度"
                           name="customerNotes" value="${deliveryPlan.customerNotes}"
                           class="easyui-textbox">
                </td>
                <td class="title">收货地址:</td>
                <!--收货地址-->
                <td>
                    <input type="text" id="shippingAddress" style="width: 95%;//宽度 height=20px;//高度"
                           name="shippingAddress" value="${deliveryPlan.shippingAddress}"
                           class="easyui-textbox">
                </td>
                <td class="title">客户批号:</td>
                <!--客户批号-->
                <td>
                    <input type="text" id="customerBatchCode" style="width: 95%;//宽度 height=20px;//高度"
                           name="customerBatchCode" value="${deliveryPlan.customerBatchCode}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">Po No.:</td>
                <!--Po No.-->
                <td>
                    <input type="text" id="poNo" style="width: 95%;//宽度 height=20px;//高度"
                           name="poNo" value="${deliveryPlan.poNo}"
                           class="easyui-textbox">
                </td>
                <td class="title">关税编号:</td>
                <!--关税编号-->
                <td>
                    <input type="text" id="tariffCode" style="width: 95%;//宽度 height=20px;//高度"
                           name="tariffCode" value="${deliveryPlan.tariffCode}"
                           class="easyui-textbox">
                </td>
                <td class="title">海关代码:</td>
                <!--海关代码-->
                <td>
                    <input type="text" id="customsCode" style="width: 95%;//宽度 height=20px;//高度"
                           name="customsCode" value="${deliveryPlan.customsCode}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">装运唛头:</td>
                <!--Po No.-->
                <td>
                    <input type="text" id="shippingMark" style="width: 95%;//宽度 height=20px;//高度"
                           name="shippingMark" value="${deliveryPlan.shippingMark}"
                           class="easyui-textbox">
                </td>
            </tr>
        </table>
        <table width="100%">
            <tr>
                <td class="title">样布信息:</td>
                <!--样布信息-->
                <td>
                    <textarea id="sampleInformation" name="sampleInformation"
                              style="width:100%;height:50px;"
                              placeholder="1000字以内">${deliveryPlan.sampleInformation}
                    </textarea>
                </td>
                <!--样布信息-->
            </tr>
        </table>
        <table width="100%">
            <tr>
                <td class="title">注意事项:</td>
                <!--注意事项-->
                <td>
                    <textarea id="attention" name="attention" readonly="true"
                              style="width:100%;height:70px;"
                              placeholder="1000字以内">${deliveryPlan.attention}
                    </textarea>
                </td>
                <td><!-- <img id="image" width="70px" height="70px" /> --></td>
                <!--注意事项-->
            </tr>
        </table>
    </form>
    <div style="height: 300px;">
        <div style="height: 50%">
            <div id="tb">
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search"
                   onclick="searchProduct()">查看货物位置
                </a>
            </div>
            <table id="dg_order" singleSelect="true" title="订单"
                   class="easyui-datagrid" url="" toolbar="#tb" rownumbers="true" fitColumns="true"
                   fit="true" data-options="onClickRow:onClickRow,onSelect:onSelect">
                <thead>
                <tr>
                    <th field="ID" checkbox=true></th>
                    <th field="ISFINISHED" width="15" formatter="isFinished">发货状态</th>
                    <th field="PN" width="15">装箱号</th>
                    <th field="LADINGCODE" width="15">提单号</th>
                    <th field="PLATE" width="15">车牌号</th>
                    <th field="BOXNUMBER" width="15">箱号</th>
                    <th field="SERIALNUMBER" width="15">封号</th>
                    <th field="COUNT" width="15">件数</th>
                    <th field="WEIGHT" width="15">毛重</th>
                    <th field="REALBOXNUMBER" width="15">实际箱号</th>
                    <th field="SIZE" width="15">尺码</th>
                    <th field="XX" width="35" formatter="unbindFormatter">当前发货人</th>
                </tr>
                </thead>
            </table>
        </div>
        <div style="height: 90%">
            <table id="dg_product" singleSelect="false" title="产品" style="table-layout:fixed;"
                   class="easyui-datagrid" url="" rownumbers="true" fitColumns="false" fit="true"
                   data-options="onClickRow:clickRow,rowStyler:product_rowStyle,onBeforeEdit:onBeforeEdit">
                <thead frozen="true">
                <tr>
                    <th field="PRODUCTID" checkbox=true></th>
                    <th field="PN" width="60">装箱号</th>
                    <th field="SALESORDERSUBCODE" width="100px">订单号</th>
                    <th field="SALESORDERCODE" width="100px">订单编号</th>
                    <th field="BATCHCODE" width="100px">批次号</th>
                    <th field="DELIVERYCOUNT" width="50px">出库计划<br/>数量(托)</th>
                    <th field="DELIVERYSUITCOUNT" width="50px">出库计划<br/>数量(套)</th>
                    <th field="orderxdl" width="50px">订单<br/>数量</th>
                    <th field="orderfhl" width="50px">订单<br/>发货量</th>
                    <th field="orderkcl" width="50px">订单<br/>库存量</th>
                    <th field="detailplanxdl" width="50px">计划<br/>下单量</th>
                    <th field="detailplanfhl" width="50px">计划<br/>发货量</th>
                    <th field="detailplankcl" width="50px">计划<br/>库存量</th>
                    <th field="MEMO" width="50px">备注(kg)</th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th field="CONSUMERPRODUCTNAME" width="150px">客户产品名称</th>
                    <th field="FACTORYPRODUCTNAME" width="150px">厂内名称</th>
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