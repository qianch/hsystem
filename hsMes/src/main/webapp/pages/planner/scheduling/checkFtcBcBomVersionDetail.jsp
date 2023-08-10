<!--
作者:sunli
日期:2018-03-08 10:46:24
页面:查看非套材包材bom明细JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>查看非套材包材bom明细</title>
    <%@ include file="../../base/meta.jsp" %>
    <style type="text/css">
        td {
            min-width: 30px;
            padding: 3px;
        }

        .title {
            padding-right: 5px;
        }

    </style>
</head>
<body class="easyui-layout">
<div id="middle" style="float:left">
    <div id="left" style=" width:700px; float:left">
        <table id="dg" singleSelect="true" title="非套材包材BOM明细列表" class="easyui-datagrid"
               url="${path}device/scheduling/packBomList?packVersionId=${packVersionId}">
            <thead>
            <tr>
                <!-- <th field="ID" checkbox=true></th> -->
                <th field="PACKMATERIALCODE" width="15%" data-options="editor:{type:'textbox',options:{required:true}}">
                    物料代码
                </th>
                <th field="PACKSTANDARDCODE" width="12%" data-options="editor:{type:'textbox',options:{}}">标准码</th>
                <th field="PACKMATERIALNAME" width="12%" data-options="editor:{type:'textbox',options:{required:true}}">
                    材料名称
                </th>
                <th field="PACKMATERIALMODEL" width="15%" data-options="editor:{type:'textbox'}">规格</th>
                <th field="PACKUNIT" width="7%" data-options="editor:{type:'textbox',options:{required:true}}">单位</th>
                <th field="PACKMATERIALCOUNT" width="15%"
                    data-options="editor:{type:'numberbox',options:{precision:0,required:true}}">数量
                </th>
                <th field="PACKMEMO" width="15%" data-options="editor:{type:'textbox'}">备注</th>
            </tr>
            </thead>
        </table>
    </div>
    <div id="left" title="非套材包材基本信息" style="height:100px;width:200px; float:left">
        <table>
            <tr>
                <td class="title" style="text-align: left;">包装标准代码</td>
            </tr>
            <tr>
                <td id="packCode">${ftcBcBom.code}</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">适用客户</td>
            </tr>
            <tr>
                <td id="consumerName">${consumer.consumerName }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">卷径/cm</td>
            </tr>
            <tr>
                <td id="rollDiameter">${ftcBcBomVersion.rollDiameter }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">每托卷数</td>
            </tr>
            <tr>
                <td id="rollsPerPallet">${ftcBcBomVersion.rollsPerPallet }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">托长/cm</td>
            </tr>
            <tr>
                <td id="palletLength">${ftcBcBomVersion.palletLength }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">托宽/cm</td>
            </tr>
            <tr>
                <td id="palletWidth">${ftcBcBomVersion.palletWidth }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">托高/cm</td>
            </tr>
            <tr>
                <td id="palletHeight">${ftcBcBomVersion.palletHeight }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">包材重量/kg</td>
            </tr>
            <tr>
                <td id="bcTotalWeight">${ftcBcBomVersion.bcTotalWeight }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">塑料膜要求</td>
            </tr>
            <tr>
                <td id="requirement_suliaomo">${ftcBcBomVersion.requirement_suliaomo }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">摆放要求</td>
            </tr>
            <tr>
                <td id="requirement_baifang">${ftcBcBomVersion.requirement_baifang }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">打包带要求</td>
            </tr>
            <tr>
                <td id="requirement_dabaodai">${ftcBcBomVersion.requirement_dabaodai }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">标签要求</td>
            </tr>
            <tr>
                <td id="requirement_biaoqian" colspan="3">${ftcBcBomVersion.requirement_biaoqian }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">小标签要求</td>
            </tr>
            <tr>
                <td id="requirement_xiaobiaoqian" colspan="3">${ftcBcBomVersion.requirement_xiaobiaoqian }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">卷（箱）标签要求</td>
            </tr>
            <tr>
                <td id="requirement_juanbiaoqian" colspan="3">${ftcBcBomVersion.requirement_juanbiaoqian }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">托标签要求</td>
            </tr>
            <tr>
                <td id="requirement_tuobiaoqian" colspan="3">${ftcBcBomVersion.requirement_tuobiaoqian }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">缠绕要求</td>
            </tr>
            <tr>
                <td id="requirement_chanrao" colspan="3">${ftcBcBomVersion.requirement_chanrao }</td>
            </tr>
            <tr>
                <td style="text-align: left;" class="title">其他要求</td>
            </tr>
            <tr>
                <td id="requirement_other">${ftcBcBomVersion.requirement_other }</td>
            </tr>
        </table>
    </div>
</div>
</body> 

