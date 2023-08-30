<!--
作者:徐波
日期:2016-12-3 16:35:51
页面:部件条码增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--部件条码表单-->
    <form id="partBarcodeForm" method="post" ajax="true"
          action="<%=basePath %>partBarcode/${empty partBarcode.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${partBarcode.id}"/>
        <table width="100%">
            <tr>
                <td class="title">条码号:</td>
                <td>
                    <input type="text" id="barcode" name="barcode" value="${partBarcode.barcode}" class="easyui-textbox"
                           readonly="true">
                </td>
            </tr>
            <tr>
                <td class="title">客户条码号:</td>
                <td>
                    <input type="text" id="customerBarCode" name="customerBarCode"
                           value="${partBarcode.customerBarCode}" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">供销商条码号:</td>
                <td>
                    <input type="text" id="agentBarCode" name="agentBarCode" value="${partBarcode.agentBarCode}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">订单号:</td>
                <td>
                    <input type="text" id="salesOrderCode" name="salesOrderCode" value="${partBarcode.salesOrderCode}"
                           class="easyui-textbox" readonly="true">
                </td>
            </tr>
            <tr>
                <td class="title">产品部件名称:</td>
                <td>
                    <input type="text" id="partName" name="partName" value="${partBarcode.partName}"
                           class="easyui-textbox" readonly="true">
                </td>
            </tr>
            <tr>
                <td class="title">批次号:</td>
                <td>
                    <input type="text" id="batchCode" name="batchCode" value="${partBarcode.batchCode}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">打印时间:</td>
                <td>
                    <input type="text" id="printDate" name="printDate" value="${partBarcode.printDate}"
                           class="easyui-textbox">
                </td>
            </tr>
        </table>
    </form>
</div>