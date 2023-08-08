<!--
作者:高飞
日期:2016-10-13 11:06:42
页面:销售订单JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>销售订单</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="order.js.jsp" %>
    <style type="text/css">
        font {
            font-family: 微软雅黑;
            color: #c31111;
        }

        @keyframes changed {
            from {
                color: white;
                background: #3669c1;
            }
            to {
                color: white;
                background: #82a8e8;
            }
        }
    </style>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:true,split:true">
    <div id="produceSalesOrderSearchToolbar">
        <jsp:include page="../../base/toolbar.jsp">
            <jsp:param value="close" name="ids"/>
            <jsp:param value="hasPlan" name="ids"/>
            <jsp:param value="icon-ok" name="icons"/>
            <jsp:param value="icon-ok" name="icons"/>
            <jsp:param value="订单生产完成" name="names"/>
            <jsp:param value="计调分配完成" name="names"/>
            <jsp:param value="finishProduct()" name="funs"/>
            <jsp:param value="hasPlan()" name="funs"/>
        </jsp:include>
        <div style="border-top:1px solid #DDDDDD">
        </div>
    </div>
    <table width="100%" id="orderProductSelect" idField="ID" toolbar="#produceSalesOrderSearchToolbar"
           singleSelect="false" title="" class="easyui-datagrid" url="${path}planner/produce/order/list2"
           fitColumns="false" fit="true" pagination="true" rowStyler="detailRowStyler"
           data-options="
				onClickRow:orderRowClick,
				onLoadSuccess:dgLoadSuccess,
				view:groupview,
				pageList:[10],
				remoteFilter:true,
                groupField:'SALESORDERCODE',
                groupFormatter:function(value,rows){
                    return '订单编号<font color=red>'+value + '</font> - ' + rows.length + ' 产品';
                }">
        <thead frozen="true">
        <tr>
            <!-- 这是生产计划明细ID -->
            <th field="ID" checkbox=true></th>
            <th field="SALESORDERSUBCODE" width="110">订单号</th>
            <th field="SALESORDERSUBCODEPRINT" width="110">客户订单号</th>
            <th field="ISCOMPLETE" width="110" formatter="isCompleteFormatter">完成状态</th>
            <th field="ISPLANED" width="110" formatter="isPlanedFormatter">分配状态</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="PRODUCTCOUNT" data-options="formatter:countFormatter,styler:processStyler">数量</th>
            <th width="150" field="TOTALWEIGHT" formatter="totalWeightFormatter">已分配/总重量</th>
            <th width="90" field="TOTALMETRES" formatter="totalMetresFormatter">总米长</th>
            <th field="DRAWINGNO" width="90">图号</th>
            <th field="ROLLNO" width="90">卷号</th>
            <th field="LEVELNO" width="90">层数</th>
            <th field="RC" width="120" data-options="formatter:rcFormatter">生产卷数</th>
            <th field="TC" width="120" data-options="formatter:tcFormatter">打包托数</th>
            <th field="PRODUCECOUNT" width="120" data-options="formatter:processFormatter2">生产进度</th>
            <th field="SALESORDERDATE" width="80" formatter="orderDateFormat">下单时间</th>
            <th field="DELIVERYTIME" width="80" formatter="orderDateFormat">发货时间</th>
            <th field="CONSUMERNAME" width="200">客户名称</th>
            <th field="CONSUMERPRODUCTNAME" width="200">客户产品名称</th>
            <th field="FACTORYPRODUCTNAME" width="200">厂内名称</th>
            <th field="PRODUCTMODEL" width="150">产品型号</th>
            <th field="PRODUCTWIDTH" width="80">门幅(mm)</th>
            <th field="PRODUCTROLLLENGTH" width="80">卷长(m)</th>
            <th field="PRODUCTROLLWEIGHT" width="80">卷重(kg)</th>
            <!-- <th field="PRODUCTBATCHCODE" width="80">产品批次号</th> -->
            <!-- <th field="SALESORDERDATE" sortable="true" width="80" data-options="formatter:orderDateFormat">下单日期</th> -->
            <th field="USERNAME" width="80">业务员</th>
            <th field="SALESORDERISEXPORT" width="80" data-options="formatter:exportFormat">内销/外销/胚布订单</th>
            <th field="SALESORDERTYPE" width="80" data-options="formatter:orderTypeFormat">订单类型</th>
            <!-- <th field="SALESORDERTOTALMONEY" width="80" >订单总金额</th> -->
            <!-- <th field="SALESORDERDELIVERYTIME"  width="80" data-options="formatter:orderDateFormat">发货时间</th> -->
            <th field="PRODUCTPROCESSCODE" width="130" formatter="bomVersionView" styler="vStyler">工艺标准代码</th>
            <th field="PRODUCTPROCESSBOMVERSION" width="80">工艺标准版本</th>
            <th field="PRODUCTPACKAGINGCODE" width="130" formatter="packBomView" styler="bvStyler">包装标准代码</th>
            <th field="PRODUCTPACKAGEVERSION" width="80">包装标准版本</th>
            <th field="PRODUCTMEMO" width="80">备注</th>
            <th field="SALESORDERMEMO" hidden='true' width="80">销售订单备注</th>
        </tr>
        </thead>
    </table>
</div>
<div id="salesOrderMemo" data-options="region:'east',border:true,split:true" title="销售订单备注"
     style="width:200px;padding:5px;">
    <table id="partCounts" class="easyui-datagrid" style="width:100%;" fitColumns="true" title="产品部件数量"
           data-options="rowStyler:danxiangbu">
        <thead>
        <tr>
            <th field="partName" width="80">部件名称</th>
            <th field="partCount" width="80">订单数量</th>
            <th field="partBomCount" width="80">BOM数量</th>
        </tr>
        </thead>
    </table>
    <div class="easyui-panel" title="销售订单备注" style="padding:10px;width:100%;">
        <div id="orderMemo"></div>
    </div>
    <div class="easyui-panel" title="订单产品备注" style="padding:10px;width:100%;">
        <div id="productMemo"></div>
    </div>
</div>
</body>