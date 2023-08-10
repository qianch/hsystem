<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>机台排产查询</title>
    <meta charset="UTF-8">
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <%@ include file="../../base/meta.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/fullcalendar/fullcalendar.min.css">
    <script type="text/javascript" src="<%=basePath%>/resources/fullcalendar/lib/moment.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/resources/fullcalendar/fullcalendar.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/resources/fullcalendar/locale/zh-cn.js"></script>
    <%@ include file="view.js.jsp" %>
    <style type="text/css">
        .producing {
            background: rgb(3, 183, 35);
            color: white;
            float: right;
            margin-left: 10px;
        }

        .prior {
            color: orange;
            float: right;
        }
    </style>
</head>
<body class="easyui-layout">
<div data-options="region:'center'">
    <div id="weaveDgToolbar">
        <form id="dgForm" autoSearchFunction="false">
            车　　间:<input type="text" id="did" name="filter[did]" class="easyui-combobox"
                          url="${path}device/scheduling/department"
                          data-options="valueField:'ID',textField:'NAME',onChange:filter">
            机台号:<input type="text" id="dcodes" name="filter[dcodes]" in="true" class="easyui-textbox"
                          prompt="A1,A2逗号分割">
            　订单号：<input type="text" id="salesOrder" name="filter[salesOrder]" like="true" class="easyui-textbox">
            计划单号：<input type="text" id="planCode" name="filter[planCode]" like="true" class="easyui-textbox">
            <br>
            产品规格:<input type="text" id="salesOrder" name="filter[productModel]" like="true" class="easyui-textbox">
            门 幅：<input type="text" id="planCode" name="filter[productWidth]" like="true" class="easyui-textbox">
            订单交期：<input type="text" style="width:110px;" class="easyui-datebox" id="start" name="filter[start]"
                            data-options="icons:[]">~<input type="text" style="width:110px;" class="easyui-datebox"
                                                            id="end" name="filter[end]" data-options="icons:[]"><br>
            客户简称:<input type="text" id="cname" name="filter[cname]" like="true" class="easyui-textbox">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="filter()">搜索</a>
            <jsp:include page="../../base/toolbar.jsp">
                <jsp:param value="print" name="ids"/>
                <jsp:param value="icon-print" name="icons"/>
                <jsp:param value="打印条码" name="names"/>
                <jsp:param value="doPrintList()" name="funs"/>
            </jsp:include>
        </form>
    </div>
    <table id="dg" singleSelect="false"
           class="easyui-datagrid" toolbar="#weaveDgToolbar" rownumbers="true" fitColumns="false" pagination="true"
           fit="true" data-options="rowStyler:isProducing,onBeforeLoad:beforeLoad,onLoadSuccess:loadSuccess">
        <!-- rowStyler:rowStyler,onDblClickRow:editDevices -->
        <thead frozen="true">
        <tr>
            <th field="ID" checkbox=true></th>
            <th field="WORKSHOP" sortable="true" width="120">车间</th>
            <th field="_DEVICECODE" sortable="true" width="80">设备代码</th>
            <th field="SALESORDERCODE" sortable="true" width="130">销售单号</th>
            <th field="SALESORDERSUBCODEPRINT" sortable="true" width="130">打印单号</th>
            <th field="BATCHCODE" sortable="true" width="100">批次号</th>
            <th field="DELEVERYDATE" sortable="true" width="90" formatter="dateFormat">出货日期</th>
        </tr>
        </thead>
        <thead>
        <tr>
            <th field="PLANCODE" sortable="true" width="160">生产计划单号</th>
            <th field="PRODUCTMODEL" sortable="true" width="150">产品规格</th>
            <th field="CONSUMERPRODUCTNAME" sortable="true" width="150">客户产品名称</th>
            <th field="FACTORYPRODUCTNAME" sortable="true" width="150">厂内名称</th>
            <th field="PRODUCTWIDTH" sortable="true" width="80">门幅(mm)</th>
            <th field="PRODUCTLENGTH" sortable="true" width="80">卷长(m)</th>
            <th field="PRODUCTWEIGHT" sortable="true" width="80">卷重(kg)</th>
            <th field="DRAWNO" sortable="true" width="150"
                data-options="editor:{type:'textbox',options:{required:false}}">图号
            </th>
            <th field="ROLLNO" sortable="true" width="80"
                data-options="editor:{type:'textbox',options:{required:false}}">卷号
            </th>
            <th field="LEVELNO" sortable="true" width="80"
                data-options="editor:{type:'textbox',options:{required:false}}">层数
            </th>
            <th field="REQUIREMENTCOUNT" sortable="true" width="100" formatter="countFormatter">计划数量</th>
            <th field="PRODUCECOUNT" width="120" data-options="formatter:processFormatter4">生产进度</th>
            <th width="120" field="TOTALTRAYCOUNT" data-options="formatter:totalTrayCount">打包托数/总托数</th>
            <!-- <th field="CONSUMERNAME" sortable="true" width="250" formatter="formatterC">客户名称</th> -->
            <th field="CONSUMERSIMPLENAME" width="120" sortable="true" formatter="formatterAddYX">客户简称</th>
            <!-- 					<th field="PRODUCTTYPE" sortable="true" width="80" formatter="formatterType">产品属性</th> -->
            <th field="PROCESSBOMCODE" width="130" styler="vStyler">工艺代码</th>
            <th field="PROCESSBOMVERSION" width="80">工艺版本</th>
            <!-- <th field="PROCESSBOMVERSION" width="80" >工艺版本</th> -->
            <th field="PREQ" width="130" styler="bvStyler">包装需求</th>
            <th field="BCBOMCODE" width="130" styler="bvStyler">包装代码</th>
            <th field="BCBOMVERSION" width="80">包装版本</th>
            <!-- <th field="BCBOMVERSION" width="80" >包装版本</th> -->
            <th field="YX" width="50">销售订单备注</th>
            <th field="COM" width="50">订单产品备注</th>
            <th field="ISCOMEFROMTC" sortable="true" hidden="hidden" formatter="formatterIsFinish"></th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>