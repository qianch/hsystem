<!--
作者:xubo
日期:2016-11-24 17:19:34
页面:产品追溯
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>产品追溯</title>
    <%@ include file="../base/meta.jsp" %>
    <%@ include file="tracing.js.jsp" %>
    <style type="text/css">
        table {
            margin: 20px;
            box-shadow: 2px 5px 20px #c3c3c3;
        }

        .content {
            width: 200px;
        }

        .header {
            background: gray;
            font-weight: bold;
            vertical-align: middle;
            text-align: center;
            font-size: 18px;
            color: white;
            width: 10px !important;
            padding: 5px;
        }

        ul {
            list-style: none;
            padding: 5px;
        }

        td {
            padding: 5px;
        }

        input {
            border: 1px solid gray;
            width: 350px;
            height: 50px;
            background: #f9f9f9;
            text-align: center;
            font-size: 22px;
            color: #777777;
            font-weight: bold;
            vertical-align: middle;
            border-radius: 5px;
        }

        button {
            border: 1px solid gray;
            height: 50px;
            text-align: center;
            font-size: 22px;
            font-weight: bold;
            vertical-align: middle;
            border-radius: 5px;
        }

        input:-webkit-autofill {
            background-color: rgb(250, 255, 189);
            background-image: none;
            color: rgb(0, 0, 0);
        }

        .layout-body {
            background: #f7f7f7;
        }
    </style>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'north',border:true"
     style="position: relative; height: 90px;text-align: center;line-height: 70px; ">
    <!-- <form action="#" id="producePlanSearchForm" autoSearchFunction="false">
        <label class="panel-title"></label>产品条码:<input type="text" id="barcode" name="code" class="easyui-textbox"> <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-search" onclick="tracing()"> 搜索 </a>
    </form> -->
    <input type="text" id="barcode" placeholder="请输入条码号" onkeyup="this.value=this.value.toUpperCase()">
    <button onclick="tracing()">追溯</button>
</div>
<div data-options="region:'center',border:false" style="position: relative;">
    <table>
        <tr>
            <td rowspan="4" class="header">卷信息</td>
            <td class="title">产品名称</td>
            <td class="content" id="productName"></td>
            <td class="title">规格型号</td>
            <td class="content" id="productModel"></td>
            <td class="title">质量等级</td>
            <td class="content" id="rollQualityGradeCode"></td>
        </tr>
        <tr>
            <td class="title">订单号</td>
            <td class="content" id="orderCode"></td>
            <td class="title">批次号</td>
            <td class="content" id="batchCode"></td>
            <td class="title">卷重</td>
            <td class="content" id="rollWeight"></td>
        </tr>
        <tr>
            <td class="title">下单客户</td>
            <td class="content" id="consumerName"></td>
            <td class="title">打包时间</td>
            <td class="content" id="packTime"></td>
            <td class="title">打包人</td>
            <td class="content" id="packUser" colspan="3"></td>
        </tr>
        <tr>
            <td class="title">状态</td>
            <td class="content" id="isAbandon"></td>
        </tr>
    </table>
    <table>
        <tr>
            <td class="header" rowspan="2">投料</td>
            <td style="margin:0;padding:0;vertical-align: top;">
                <table style="padding:0;margin:0;height: 100%;width:100%;box-shadow: none;">
                    <tr>
                        <td class="title" style="text-align: left;height: 30px;">条码号</td>
                        <td class="title" style="text-align: left;height: 30px;">规格型号</td>
                        <td class="title" style="text-align: left;height: 30px;">机台</td>
                        <td class="title" style="text-align: left;height: 30px;">投料时间</td>
                        <td class="title" style="text-align: left;height: 30px;">操作人</td>
                    </tr>
                    <tbody id="feeding">
                    </tbody>
                </table>
            </td>
        </tr>
    </table>
    <table>
        <tr>
            <td rowspan="3" class="header">生产</td>
        </tr>
        <tr>
            <td class="title" style="text-align: left;height: 30px;">产出时间</td>
            <td class="title" style="text-align: left;height: 30px;">产出机台</td>
            <td class="title" style="text-align: left;height: 30px;">操作人</td>
        </tr>
        <tr>
            <td class="content" id="rollOutputTime"></td>
            <td class="content" id="rollDeviceCode"></td>
            <td class="content" id="rollUserName"></td>
        </tr>
    </table>
    <table>
        <tr>
            <td rowspan="3" class="header">工艺</td>
        </tr>
        <tr>
            <td class="title" style="text-align: left;height: 30px;">工艺代码</td>
            <td class="title" style="text-align: left;height: 30px;">版本</td>
            <td class="title" style="text-align: left;height: 30px;">包装代码</td>
            <td class="title" style="text-align: left;height: 30px;">版本</td>
        </tr>
        <tr>
            <td class="content" id="processCode"></td>
            <td class="content" id="processVersion"></td>
            <td class="content" id="packageCode"></td>
            <td class="content" id="packageVersion"></td>
        </tr>
    </table>
    <table>
        <tr>
            <td class="header">包装</td>
            <td id="pack_task"></td>
        </tr>
    </table>
    <table>
        <tr>
            <td rowspan="3" class="header">待入库</td>
        </tr>
        <tr>
            <td class="title" style="text-align: left;height: 30px;">首次待入库仓库</td>
            <td class="title" style="text-align: left;">首次待入库库位</td>
            <td class="title" style="text-align: left;">待入库人</td>
            <td class="title" style="text-align: left;">待入库时间</td>
        </tr>
        <tr>
            <td class="content" id="pendingWarehousecode"></td>
            <td class="content" id="pendingWarehouseposcode"></td>
            <td class="content" id="pendingUsername"></td>
            <td class="content" id="pendingIntime"></td>
        </tr>
    </table>
    <table>
        <tr>
            <td rowspan="3" class="header">入库</td>
        </tr>
        <tr>
            <td class="title" style="text-align: left;height: 30px;">首次入库仓库</td>
            <td class="title" style="text-align: left;">首次入库库位</td>
            <td class="title" style="text-align: left;">入库人</td>
            <td class="title" style="text-align: left;">入库时间</td>
        </tr>
        <tr>
            <td class="content" id="warehousecode"></td>
            <td class="content" id="warehouseposcode"></td>
            <td class="content" id="warehouseUsername"></td>
            <td class="content" id="intime"></td>
        </tr>
    </table>
    <table>
        <tr>
            <td class="header" rowspan="3">移库</td>
            <td style="margin:0;padding:0;vertical-align: top;">
                <table style="padding:0;margin:0;height: 100%;width:100%;box-shadow: none;">
                    <td class="title" style="text-align: left;height: 30px;">被移库仓库</td>
                    <td class="title" style="text-align: left;">被移库库位</td>
                    <td class="title" style="text-align: left;">移库仓库</td>
                    <td class="title" style="text-align: left;">移库库位</td>
                    <td class="title" style="text-align: left;">操作人</td>
                    <td class="title" style="text-align: left;">入库时间</td>
                    <tbody id="ykinfos">
                    </tbody>
                </table>
            </td>
        </tr>
    </table>
    <table>
        <tr>
            <td rowspan="3" class="header">发货</td>
        </tr>
        <tr>
            <td class="title" style="text-align: left;height: 30px;">发货时间</td>
            <td class="title" style="text-align: left;">发货客户</td>
        </tr>
        <tr>
            <td class="content" id="outTime"></td>
            <td class="content" id="consumerSimpleName"></td>
        </tr>
    </table>
    <table>
        <tr>
            <td colspan="3" class="header">拆包</td>
        </tr>
        <tr>
            <td class="title" style="text-align: left;height: 30px;">拆包日期</td>
            <td class="title" style="text-align: left;">拆包人</td>
            <td class="title" style="text-align: left;">拆包内容</td>
        </tr>
        <tbody id="openPackBarCode">
        </tbody>
    </table>
    <table>
        <tr>
            <td colspan="3" class="header">作废信息</td>
        </tr>
        <tr>
            <td class="title" style="text-align: left;">作废人</td>
            <td class="title" style="text-align: left;">作废时间</td>
        </tr>
        <tbody id="abandonBarCode">
        </tbody>
    </table>
</div>
</body>
