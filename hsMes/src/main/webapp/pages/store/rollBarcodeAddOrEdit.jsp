<!--
作者:徐波
日期:2016-12-3 16:35:51
页面:卷条码增加或修改页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div>
    <!--卷条码表单-->
    <form id="rollBarcodeForm" method="post" ajax="true"
          action="<%=basePath %>rollBarcode/${empty rollBarcode.id ?'add':'edit'}" autocomplete="off">
        <input type="hidden" name="id" value="${rollBarcode.id}"/>
        <table width="100%">
            <tr>
                <td class="title">打印时间:</td>
                <td>
                    <input type="text" id="printDate" name="printDate" value="${rollBarcode.printDate}"
                           class="easyui-textbox" readonly="true">
                </td>
            </tr>
            <tr>
                <td class="title">条码号:</td>
                <td>
                    <input type="text" id="barcode" name="barcode" value="${rollBarcode.barcode}" class="easyui-textbox"
                           readonly="true">
                </td>
            </tr>
            <tr>
                <td class="title">客户条码号数值:</td>
                <td>
                    <input type="number" id="customerBarCode" name="customerBarCode" value="" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">供销商条码数值:</td>
                <td>
                    <input type="number" id="agentBarCode" name="agentBarCode" value="" class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">订单号:</td>
                <td>
                    <input type="text" id="salesOrderCode" name="salesOrderCode" value="${rollBarcode.salesOrderCode}"
                           class="easyui-textbox">
                </td>
            </tr>
            <tr>
                <td class="title">批次号:</td>
                <td>
                    <input type="text" id="batchCode" name="batchCode" value="${rollBarcode.batchCode}"
                           class="easyui-textbox" readonly="true">
                </td>
            </tr>
        </table>
    </form>
</div>