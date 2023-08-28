<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>胚布库存表</title>
    <%@ include file="../../base/meta.jsp" %>
    <%@ include file="greigeStock.js.jsp" %>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false"
     style="overflow: false;position: relative; height: 140px; width: 925px">
    <div id="toolbar">
        <div style="border-top:1px solid #DDDDDD">
            <form action="#" id="greigeStockSearchForm" autoSearchFunction="false">
                　　仓库：<input type="text" class="easyui-combobox" name="filter[warehouseCode]" like="true"
                              data-options="valueField:'warehouseCode',textField:'warehouseName',url:'<%=basePath%>warehouse/getWarehouseInfo?type=pb',onSelect:filter">
                　　库位：<input type="text" name="filter[warehousePosCode]" like="true" class="easyui-textbox">
                条码：<input type="text" name="filter[code]" class="easyui-textbox" like="true">
                成品类别名称：<input type="text" name="filter[categoryName]" class="easyui-textbox"><br>
                　订单号：<input type="text" like="true" name="filter[salesCode]" class="easyui-textbox">
                　批次号：<input type="text" name="filter[batchCode]" like="true" class="easyui-textbox">
                客户：<input type="text" name="filter[consumer]" like="true" class="easyui-textbox">
                成品类别代码：<input type="text" name="filter[categoryCode]" class="easyui-textbox"><br>
                产品规格：<input type="text" name="filter[model]" like="true" class="easyui-textbox">
                入库时间：<input type="text" id="start" name="filter[start]" class="easyui-datetimebox">
                至　：<input
                    type="text" name="filter[end]" class="easyui-datetimebox">
                　　仓库类型：<input type="text" name="filter[type]" class="easyui-combobox" like="true"
                                  data-options="valueField:'id',textField:'text',data:[{'id':'bz%','text':'编织'},{'id':'cj%','text':'裁剪'}],onSelect:filter">
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small"
                   iconcls="icon-search" onclick="filter()"> 搜索 </a>&nbsp;&nbsp;
                <a href="javascript:void(0)" class="easyui-linkbutton l-btn l-btn-small" iconcls="icon-excel"
                   onclick="exportDetail()">导出
                </a>
            </form>
        </div>
    </div>
    <table id="dg" singleSelect="false" title="" class="easyui-datagrid"
           url="${path}stock/productStock/getGreigeStockInfo" toolbar="#toolbar"
           pagination="true" rownumbers="true" fitColumns="false" fit="true" remoteSort="true"
           data-options="rowStyler:rowStyler,showFooter:true">
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="BARCODE" sortable="true" width="120">条码号</th>
            <th field="SALESORDERCODE" sortable="true" width="130" formatter="orderFormatter">订单号</th>
            <th field="BATCHCODE" sortable="true" width="100" formatter="batchFormatter">批次号</th>
            <th field="PLANDELIVERYDATE" sortable="true" width="100" formatter="deliveryFormatter">发货日期</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="CATEGORYNAME" sortable="true" width="130">成品类别名称</th>
            <th field="CATEGORYCODE" sortable="true" width="130">成品类别代码</th>
            <th field="FACTORYPRODUCTNAME" sortable="true" width="130" formatter="modelFormatter">厂内名称</th>
            <th field="CONSUMERPRODUCTNAME" sortable="true" width="130" formatter="modelFormatter">客户产品名称</th>
            <th field="TCPROCBOMVERSIONPARTSNAME" sortable="true" width="130">部件名称</th>
            <th field="PRODUCTMODEL" sortable="true" width="130" formatter="modelFormatter">产品规格</th>
            <th field="CONSUMERNAME" sortable="true" width="210" formatter="consumerFormatter">客户名称</th>
            <th field="WEIGHT" sortable="true" width="70" formatter="processNumberFormatter">重量(KG)</th>
            <th field="WAREHOUSENAME" sortable="true" width="70" formatter="warehouseFormatter">仓库</th>
            <th field="WAREHOUSEPOSCODE" sortable="true" width="70">库位</th>
            <th field="INTIME" sortable="true" width="140">入库时间</th>
            <th field="DAYS" width="140">在库天数</th>
            <th field="PRODUCTSHELFLIFE" sortable="true" width="90">保质期(天)</th>
            <th field="STOCKSTATE" sortable="true" width="70" formatter="formatterState">状态</th>
            <th field="ROLLQUALITYGRADECODE" sortable="true" width="70">质量等级</th>
            <th field="TYPE" sortable="true" width="70" formatter="typeFormatter">仓库类型</th>
            <th field="ROLLCOUNTINTRAY" sortable="true" width="70">卷数</th>
        </tr>
        </thead>
    </table>
</div>
</body>